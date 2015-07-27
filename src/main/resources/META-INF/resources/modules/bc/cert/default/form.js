bc.namespace("bc.defaultCertForm");
bc.defaultCertForm = {
	init : function(option,readonly) {
		var $page = $(this);
		if(readonly) return;

		// 区域的折叠或展开
		$page.on("click", ".toggleMe", function() {
			var $this = $(this);
			$this.toggleClass("ui-icon-triangle-1-n ui-icon-triangle-1-s");
			$this.closest(".blockContainer").find(".detail").toggleClass("hide");
			return false;
		});
		$page.on("click", ".toggleAll", function() {
			$page.find(".toggleMe").click();
			return false;
		});

		// 证件标识信息
		var type = $page.find(":input[name='type']").val();// 证件类别编码
		var code = $page.find(":input[name='code']").val();// 证件编码
		var pid = $page.find(":input[name='pid']").val();// 业务对象ID
		var ver = $page.find(":input[name='ver']").val();// 证件版本
		var uid = $page.find(":input[name='uid']").val();// 表单UID

		// 打印图片
		$page.on("click", ".printImage", function() {
			var $block = $(this).closest(".blockContainer");
			var attachId = $block.find("input.attach-id").val();
			var attachWidth = $block.find("input.attach-width").val();
			if(!attachId || attachId.length == 0){
				bc.msg.info("请先上传图片！");
				return false;
			}
			if(!attachWidth || attachWidth.length == 0){
				bc.msg.info("请先设置图片的实物宽度！");
				return false;
			}
			//新版打印接口的数据格式certs:[{"pid":11771810,"ver":1,"code":"certJZZ","type":"CarManCert"}]
			//bc.image.print(attachId, attachWidth);
			var certs = [];
			certs.push({
				attachId : attachId
					,attachWidth: attachWidth
					,code: code
					,type: type
					,isDirectlyPrint: true
				});
			bc.cert.print(certs);
			return false;
		});

		// 编辑图片
		$page.on("click", ".editImage", function() {
			var $block = $(this).closest(".blockContainer");
			var pageNo = $block.attr("data-num");
			var $attachId = $block.find("input.attach-id");
			var attachId = $attachId.val();
			var data = {};
			data.fname = $block.attr("data-label");
			data.format = $block.attr("data-default-format") || "png";

			if(attachId && attachId.length > 0) {// 已上传的图片
				data.id = "attach:" + attachId + ":true";// 设为true标记编辑后生成新的图片附件
			}
			data.puid = uid;// 表单的uid
			data.ptype = code + (pageNo ? "." + pageNo : "");// 证件配置的编码
			data.onOk = function(attach){
				var id = attach.id.split(":")[1];
				$attachId.val(id);
				var $img = $block.find("img");
				var src = bc.root + "/bc/image/download?id=" + id;// + "&ts=" + new Date().getTime();
				if($img.length > 0) {
					$img.attr("src", src);
				}else{
					$block.find(".detail").empty().append('<img src="' + src + '" style="max-width:100%" />');
				}
			}
			bc.photo.handle(data);
			return false;
		});

		// 合并图片
		$page.on("click", ".mergeImage", function() {
			var $mainBlock = $(this).closest(".blockContainer.main");
			var $mainAttachId = $mainBlock.find("input.attach-id");

			// 获取合并配置参数
			var combine = $(this).attr("data-combine");
			if(!combine || combine == ""){
				bc.msg.alert("缺少合并配置参数！");
				return false;
			}

			// 判断是否是连续水平合并或垂直合并（true代表只需提供至少2个分拆图就即可执行合并，否则所有分拆图都必须提供才能合并）
			var isHV = (combine.indexOf("h") != -1 || combine.indexOf("v") != -1);

			// 获取上传的分拆图
			var subAttachIds = [];
			var ok = true;
			$mainBlock.siblings(".sub").find("input.attach-id").each(function(){
				if(!this.value || this.value == ""){
					if(!isHV) {
						bc.msg.alert($(this).closest(".blockContainer").attr("data-label") + "未上传！");
						ok = false;
						return false;
					}
				}else{
					subAttachIds.push(this.value);
				}
			});
			if(!ok) return;
			if(subAttachIds.length < 1){
				bc.msg.alert("至少需要1张以上图片才能进行合并！");
				return false;
			}
			var format = $mainBlock.attr("data-default-format") || "png";
			var filename = $mainBlock.attr("data-label");

			bc.msg.confirm("确定合并图片吗？",function(){
				bc.ajax({
					url: bc.root + "/bc/image/combine",
					data: {ids: subAttachIds.join(","), mixConfig: combine
						, fileType: format, filename: filename
						, puid: uid, ptype: type + "." + code
					},
					dataType: "json",
					success:function(json){
						if(json.success){
							bc.msg.slide("图片合并成功！");
							$mainAttachId.val(json.id);
							var $img = $mainBlock.find("img");
							var src = bc.root + "/bc/image/download?id=" + json.id;
							if($img.length > 0) {
								$img.attr("src", src);
							}else{
								$mainBlock.find(".detail").empty().append('<img src="' + src + '" style="max-width:100%" />');
							}
						}
					}
				});
			});
			return false;
		});
	},

	/** 自定义的验证 */
	validate:function(){
		var $page = $(this);
		var mainAttachId = $page.find(".main input.attach-id").val();
		if(!mainAttachId || mainAttachId == ""){
			bc.msg.alert("证件图片未上传！");
			return false;
		}
		return bc.validator.validate($page);
	}
};