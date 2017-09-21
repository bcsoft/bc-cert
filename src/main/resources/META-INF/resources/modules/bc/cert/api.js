/**
 * 证件模块通用API
 */
bc.namespace("bc.cert");

/**
 * 打印证件图片
 * @param {Array|Json} certs 要打印的1个或多个证件的配置信息
 * @cert {String} type 类别编码
 * @cert {String} code 证件编码
 * @cert {Integer} pid 业务ID
 * @cert {String} ver 版本号
 * @cert {boolean} [可选] isDirectlyPrint 是否直接打印。默认是false,意思就是参数在后台不需要请求数据库处理！
 */
bc.cert.print = function (certs, isOpenNewWin, autoPrint) {
	if (!certs) return;
	if (!$.isArray(certs)) {
		certs = [certs];
	}
	var url = bc.root + "/modules/bc/cert/print";
	bc.print({
		method: "POST",
		url: url,
		data: {certs: $.toJSON(certs)},
		isOpenNewWin: isOpenNewWin,
		autoPrint: autoPrint
	});
};

/**
 * 查看证件图片
 * @param {Array|Json} certs 要查看的1个或多个证件的配置信息,格式与bc.cert.print函数参数相同
 */
bc.cert.preview = function (certs) {
	bc.cert.print(certs, true, false);
};

/**
 * 渲染证件表单
 * 如果证件的表单不存在会自动创建一个
 * @param {Object} option 配置参数
 * @option {String} type [必填]证件类别，如：CarManCert（司机证件）
 * @option {String} code [必填]证件编码，如：certIdentity（身份证）
 * @option {Integer} pid [必填]证件业务ID，如：司机招聘表的ID
 * @option {String} ver [可选]版本号，没有指定代表版本1
 * @option {String} subject [必填]标题
 * @option {String} role [可选]对证件进行编辑需要的角色，使用"|"连接多个角色编码代表或关系，使用"+"连接多个角色编码代表和关系
 * @option {boolean} readonly [可选]是否强制只读显示，默认自动根据 role 参数的值判断
 * @option {String} onOk [可选]证件成功修改后的回调函数
 */
bc.cert.render = function (option) {
	if (!option || !option.type || !option.code || !option.pid || !option.subject) {
		bc.msg.alert("必须配置 type、code、pid、subject 参数 - bc.cert.render");
		return;
	}

	// 组装参数
	var data = {
		type: option.type,
		code: option.code,
		pid: option.pid,
		subject: option.subject,
		ver: option.ver || 1,
		pname: option.pname,
		replace: false
	};
	if (option.role) data.role = option.role;
	if (option.ver) data.ver = option.ver;
	if (option.readonly) data.readonly = option.readonly;

	// 弹出渲染窗口
	bc.page.newWin({
		url: bc.root + "/modules/bc/cert/render",
		data: data,
		mid: option.type + "." + option.code + "." + option.pid + "." + (option.ver ? option.ver : "newest"),
		name: option.subject,
		title: option.subject,
		afterClose: function (status) {
			//console.log("bc.cert.render: afterClose: " + status);
			if (status && option.onOk) {
				//console.log("call onOk");
				option.onOk.call(this, status);
			}
		}
	});
};

/**
 * 查看业务对象的所有证件
 * @param {Json} option 配置信息
 * @cert {String} type 类别编码
 * @cert {Integer} pid 业务ID
 * @cert {String} label 业务对象的描述性名称，如司机姓名、车辆车牌
 * @option {json} data 附加的data
 * @data {String} pid [必填]
 * @data {String} type [必填] 所属的类别
 * @data {Integer} id [必填] 业务的id ,如司机的id,车辆的id
 * @data {String}
 *
 */
bc.cert.seeAllByParent = function (option) {
	console.debug("bc.cert.seeAllByParent: " + $.toJSON(option));
	var option_ = {
		url: bc.root + "/modules/bc/cert/seeAllByParent/seeAll",
		data: {pid: option.pid, type: option.type, id: option.pid}, //SEE_ALL_BY_PARENT是查看所有证件的模板文件的code
		mid: option.pid + option.label + option.cert_type_name,
		name: option.label + "的" + option.cert_type_name,
		title: option.label + "的" + option.cert_type_name,
	};
	bc.page.newWin(option_);
};

/**
 * 在指定容器内嵌入显示某业务对象(PID + type)的所有证件信息
 * @param {Object} option 配置参数
 * @option {Element} container 要嵌入到的容器的jQuery对象
 * @option {String} type 证件类别编码
 * @option {Integer} pid 业务对象ID
 * @option {Boolean} readonly 是否只读显示
 * @option {function} onOk [可选]成功后的回调函数(渲染后的所有证件表单)
 * @option {String} uid [可填] 业务的uid，上传就需要
 * @option {String} pname [必填] 业务的名称,如司机证件的司机名，为了上传时拼接成subject对象
 * @option {String} role [可填] 用户的角色
 * @option {String} onOk [可选] 回调函数
 */
bc.cert.nestedGrid = function (option) {
	console.log("bc.cert.nestedGrid");
	console.log(option);
	if (!option || !option.container || !option.type || !option.pid) {
		bc.msg.alert("必须配置 container、type、pid参数 - bc.cert.nestedGrid");
		return;
	}
	var dataHeader =
		'<div class=\"bc-grid bs-tempDriver-Groups\">'
		+ '<table class=\"table\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\" >'
		+ '<colgroup>'
		+ '<col style=\"width: 3em;\"/>'
		+ '<col style=\"width: 10em;\"/>'
		+ '<col style=\"width: 4em;\"/>'
		+ '<col style=\"width: 5em;\"/>'
		+ '<col style=\"width: 12em;\"/>'
		+ '<col style=\"width: auto;\"/>'
		+ '</colgroup>'
		+ '<tr class=\"ui-widget-content row rowHeader\">'
		+ '<td class=\"first\" title=\"点击全选或反选\" style=\"text-align: center;vertical-align: middle\"><input type=\"checkbox\" name=\"toggleSellectAll\"/></td>'
		+ '<td class=\"middle\" style=\"text-align: center\">证件名称</td>'
		+ '<td class=\"middle\" style=\"text-align: center\">版本</td>'
		+ '<td class=\"middle\" style=\"text-align: center\">上传状态</td>'
		+ '<td class=\"middle\" style=\"text-align: center\">操作</td>'
		+ '<td class=\"last\" style=\"text-align: center\">最后修改</td>'
		+ '</tr>'
		+ '</table>'
		+ '</div>'
	;

	var dataTr =
		'<tr class=\"ui-widget-content row\">'
		+ '<td class=\"id first toggleSellect\" style=\"text-align: center\">'
		+ '<span class=\"ui-icon\"></span><span class=\"index\">{{index}}</span>'
		+ '</td>'
		+ '<td class=\"middle\">{{certname}}</td>'
		+ '<td class=\"middle\">{{version}}</td>'
		+ '<td class=\"middle\">{{isUpload}}</td>'
		+ '<td class=\"middle\">{{&operate}}</td>'
		+ '<td class=\"last\">{{&lastModifyDate}}</td>'
		+ '</tr>'
	;

	var $container = option.container;
	bc.ajax({
		type: 'post',
		data: {type: option.type, pid: option.pid},
		url: bc.root + "/modules/bc/cert/seeAllByParent/nestedGrid",
		dataType: "json",
		success: function (json) {
			if (json.success = false) {
				bc.msg.slide(json.msg);
			} else {
				if (option.onOk) // 回调json数据
					option.onOk.call(this, json);

				var $header = option.container.children(".ui-widget-header");
				$header.after(dataHeader);
				var $rowHeader = option.container.find(".rowHeader");
				var lists = json.lists;
				var length = lists.length;
				var operate = "";
				var lastModifyDate = "";
				var isUpload = "";
				var $trs;
				for (var i = length - 1; i >= 0; i--) {
					var HiddenDate = "<input type='hidden' class='ignore' name='code' value='" + lists[i].code + "'><input type='hidden' class='ignore' name='typeCode' value='" + lists[i].typeCode + "'>" +
						"<input type='hidden' class='ignore' name='pid' value='" + option.pid + "'><input type='hidden' class='ignore' name='ver' value='" + lists[i].version + "'>" +
						"<input type='hidden' class='ignore' name='uid' value='" + lists[i].uid + "'><input type='hidden' class='ignore' name='fid' value='" + lists[i].fid + "'>" +
						"<input type='hidden' class='ignore' name='readonly' value='" + option.readonly + "'><input type='hidden' class='ignore' name='name' value='" + lists[i].name + "'>" +
						"<input type='hidden' class='ignore' name='pname' value='" + option.pname + "'><input type='hidden' class='ignore' name='uuid' value='" + option.uid + "'>" +
						"<input type='hidden' class='ignore' name='version' value='" + lists[i].version + "'><input type='hidden' class='ignore' name='role' value='" + option.role + "'>" +
						"<input type='hidden' class='ignore' name='tpl' value='" + lists[i].tpl + "'><input type='hidden' class='ignore' name='subject' value='" + lists[i].subject + "'>";

					if ("" == lists[i].actor_name) {
						lastModifyDate = "";
					} else {
						lastModifyDate = "<label title='" + lists[i].actor_name + "(" + lists[i].modified_date + ")'>" + lists[i].actor_name + "(" + lists[i].modified_date + ") </label>";
					}

					if (option.readonly == true) { // 没有相关的权限
						if (lists[i].isUpload == 'yes') {
							operate = '<a href=\"#\" class=\"printCert\" style=\"margin-left:10px;\">打印</a><a href=\"#\" class=\"readerCert\" style=\"margin-left:10px;\">查看</a>' + HiddenDate;
						} else {
							operate = "" + HiddenDate;
						}
					} else {                       // 有相关的权限
						if (lists[i].isUpload == 'yes') {
							operate = '<a href=\"#\" class=\"modifyCert\" style=\"margin-left:10px;\">修改</a>' +
								'<a href=\"#\" class=\"printCert\" style=\"margin-left:5px;\">打印</a><a href=\"#\" class=\"readerCert\" style=\"margin-left:5px;\">查看</a><a href=\"#\" class=\"deleteCert\" style=\"margin-left:5px;\">删除</a>' + HiddenDate;
						} else {
							operate = '<a href=\"#\" class=\"uploadCert\" style=\"margin-left:10px;\">上传</a>' + HiddenDate;
						}
					}

					if (lists[i].isUpload == 'yes') {
						isUpload = "已上传";
					} else {
						isUpload = "未上传";
					}
					$trs = $(bc.formatTpl(dataTr, $.extend({
						index: i + 1,
						certname: lists[i].name,
						version: lists[i].version,
						isUpload: isUpload,
						operate: operate,
						lastModifyDate: lastModifyDate
					})));

					$rowHeader.after($trs);
				}

				// 点击切换行的选择状态
				$container.find(".toggleSellect").on("click", function () {
					$(this).parent().toggleClass("ui-state-highlight")
						.find("td:eq(0)>span.ui-icon").toggleClass("ui-icon-check");
				});
				// 全选|反选
				$container.find("input[name='toggleSellectAll'").change(function () {
					$container.find("tr.row").toggleClass("ui-state-highlight", this.checked)
						.find("td:eq(0)>span.ui-icon").toggleClass("ui-icon-check", this.checked);
				});

				// 先为鼠标单击打印事件解绑
				$container.find(".printCert").off("click");

				// 鼠标单击打印事件
				$container.find(".printCert").on("click", function () {
					var code = $(this).parent("td").find("input[name=\"code\"]").val();
					var type = $(this).parent("td").find("input[name=\"typeCode\"]").val();
					var pid = $(this).parent("td").find("input[name=\"pid\"]").val();
					var ver = $(this).parent("td").find("input[name=\"ver\"]").val();

					var option = {
						code: code,
						type: type,
						pid: pid,
						ver: ver
					};
					bc.cert.print(option, false, true);
				});

				// 先为鼠标单击修改事件解绑
				$container.find(".modifyCert").off("click");

				// 鼠标单击修改事件
				$container.find(".modifyCert").on("click", function () {
					var code = $(this).parent("td").find("input[name=\"code\"]").val();
					var type = $(this).parent("td").find("input[name=\"typeCode\"]").val();
					var pid = $(this).parent("td").find("input[name=\"pid\"]").val();
					var ver = $(this).parent("td").find("input[name=\"ver\"]").val();
					var tpl = $(this).parent("td").find("input[name=\"tpl\"]").val();
					var uid = $(this).parent("td").find("input[name=\"uid\"]").val();
					var subject = $(this).parent("td").find("input[name=\"subject\"]").val();
					var role = $(this).parent("td").find("input[name=\"role\"]").val();
					var readonly = $(this).parent("td").find("input[name=\"readonly\"]").val();
					var pname = $(this).parent("td").find("input[name=\"pname\"]").val();

					if (readonly == "true") { //有权限
						readonly = false;
					}

					var option = {
						code: code,
						type: type,
						pid: pid,
						ver: ver,
						id: pid,
						tpl: tpl,
						uid: uid,
						pname: pname,
						subject: subject,
						readonly: readonly,
						role: role,
						isGetDetails: false,
						upload_status: true,
						onOk: function (json) {
							if (json.success == true) { //修改成功后执行刷新操作
								var option = {$container: $container};
								bc.cert.nestedGrid.refresh(option);
							}
						}
					};
					bc.cert.render(option);
				});

				// 先为鼠标单击查看事件解绑
				$container.find(".readerCert").off("click");
				// 鼠标单击查看事件
				$container.find(".readerCert").on("click", function () {
					var code = $(this).parent("td").find("input[name=\"code\"]").val();
					var type = $(this).parent("td").find("input[name=\"typeCode\"]").val();
					var pid = $(this).parent("td").find("input[name=\"pid\"]").val();
					var ver = $(this).parent("td").find("input[name=\"ver\"]").val();
					var option = {
						code: code,
						type: type,
						pid: pid,
						ver: ver
					};
					bc.cert.preview(option);
				});

				// 先为鼠标单击删除事件解绑
				$container.find(".deleteCert").off("click");
				// 鼠标单击删除事件
				$container.find(".deleteCert").on("click", function () {
					var code = $(this).parent("td").find("input[name=\"code\"]").val();
					var type = $(this).parent("td").find("input[name=\"typeCode\"]").val();
					var pid = $(this).parent("td").find("input[name=\"pid\"]").val();
					var tpl = $(this).parent("td").find("input[name=\"tpl\"]").val();
					var subject = $(this).parent("td").find("input[name=\"subject\"]").val();
					var ver = $(this).parent("td").find("input[name=\"ver\"]").val();

					var option = {
						pid: pid,
						tpl: tpl,
						type: type,
						subject: subject,
						code: code,
						ver: ver,
						onOk: function (json) {
							if (json.success == true) {//删除成功后执行隐藏对应表格列的操作
								var option = {$container: $container};
								bc.cert.nestedGrid.refresh(option);
							}
						}
					};
					bc.cform.delete_(option);
				});

				// 先为鼠标单击上传事件解绑
				$container.find(".uploadCert").off("click");
				// 鼠标单击上传事件
				$container.find(".uploadCert").on("click", function () {
					var code = $(this).parent("td").find("input[name=\"code\"]").val();
					var type = $(this).parent("td").find("input[name=\"typeCode\"]").val();
					var pid = $(this).parent("td").find("input[name=\"pid\"]").val();
					var tpl = $(this).parent("td").find("input[name=\"tpl\"]").val();
					var uid = $(this).parent("td").find("input[name=\"uuid\"]").val();
					var pname = $(this).parent("td").find("input[name=\"pname\"]").val();
					var name = $(this).parent("td").find("input[name=\"name\"]").val();
					var readonly = $(this).parent("td").find("input[name=\"readonly\"]").val();
					var role = $(this).parent("td").find("input[name=\"role\"]").val();

					var read = false;
					if (readonly == "false") { //有权限
						readonly = true;
					}
					var option = {
						code: code,
						type: type,
						pid: pid,
						id: pid,
						tpl: tpl,
						uid: uid,
						pname: pname,
						subject: pname + name,
						readonly: read,
						role: role,
						operate_type: "preview",
						onOk: function (json) {
							if (json.success == true) { //上传成功后执行刷新操作
								var option = {$container: $container};
								bc.cert.nestedGrid.refresh(option);
							}
						}
					};
					bc.cert.render(option);
				});

				// 先为鼠标点击打印多证件事件解绑
				$container.find(".bc-cert-print").off("click");
				// 鼠标点击打印多证件的事件
				$container.find(".bc-cert-print").on("click", function () {
					var option = {container: $container, operateType: false};
					bc.cert.nestedGrid.printORReadAll(option);
				});

				// 先为鼠标点击查看多证件事件解绑
				$container.find(".bc-cert-read").off("click");
				// 鼠标点击查看多证件的事件
				$container.find(".bc-cert-read").on("click", function () {
					var option = {container: $container, operateType: true};
					bc.cert.nestedGrid.printORReadAll(option);
				});

				// 先为鼠标点击刷新事件解绑
				$container.find(".bc-cert-refresh").off("click");
				// 鼠标点击刷新的事件
				$container.find(".bc-cert-refresh").on("click", function () {
					var option = {$container: $container};
					bc.cert.nestedGrid.refresh(option);
				});
			}
		}
	});
};
/**
 * 封装在指定容器内嵌入显示某业务对象时的刷新操作
 * @param {String} option
 * @option {Element} container [必填] 要嵌入到的容器的jQuery对象
 */
bc.cert.nestedGrid.refresh = function (option) {
	$container = option.$container;
	var rows = $container.find("tr.row");
	var type = rows.eq(1).find("input[name=\"typeCode\"]").eq(0).val();
	var pid = rows.eq(1).find("input[name=\"pid\"]").eq(0).val();
	var uid = rows.eq(1).find("input[name=\"uuid\"]").eq(0).val();
	var pname = rows.eq(1).find("input[name=\"pname\"]").eq(0).val();
	var role = rows.eq(1).find("input[name=\"role\"]").eq(0).val();
	var readonly = rows.eq(1).find("input[name=\"readonly\"]").eq(0).val();

	if (readonly == "true") {
		readonly = true;
	} else {
		readonly = false;
	}

	var $header = $container.children(".ui-widget-header");
	$header.nextAll().empty();
	if (pid != "") {
		bc.cert.nestedGrid({
			container: $container,
			type: type,
			pid: pid,
			uid: uid,
			pname: pname,
			readonly: readonly,
			role: role
		});
	}
};

/**
 * 封装在指定容器内嵌入显示某业务对象时的打印和查看操作
 * @param {String} option
 * @option {Element} container [必填] 要嵌入到的容器的jQuery对象
 * @option {Boolean} operateType [可填] 操作类型，默认是打印操作false,查看操作true
 */
bc.cert.nestedGrid.printORReadAll = function (option) {
	if (!option || !option.container) {
		bc.msg.info("必须指定嵌入的容器对象");
		return;
	}
	var $container = option.container;
	var $selected = $container.find(".ui-state-highlight:not('.rowHeader')");//获得所有选中行
	var isSelectAll = $container.find("input[name='toggleSellectAll']").attr("checked") == "checked" ? true : false;//用户是否选择了全选
	var certs = [];
	if ($selected.length == 0 || isSelectAll) { // 查看所有的证件或者全选时，自动过滤掉未上传的证件
		var $rows = $container.find("tr.row");    // 找到所有的行
		for (var i = 0; i < $rows.length; i++) {
			var fid = $rows.eq(i).find("input[name=\"fid\"]").val();
			if (fid) {                              //此时只处理上传的证件
				var type = $rows.eq(i).find("input[name=\"typeCode\"]").val();
				var code = $rows.eq(i).find("input[name=\"code\"]").val();
				var pid = $rows.eq(i).find("input[name=\"pid\"]").val();
				var ver = $rows.eq(i).find("input[name=\"version\"]").val();
				certs.push({
					type: type,
					code: code,
					pid: pid,
					ver: ver
				});
			}
		}
		if (certs.length > 0) {
			if (option.operateType == true) {
				bc.cert.preview(certs);
			} else {
				bc.cert.print(certs);
			}
		} else {
			bc.msg.info("不能查看/打印未上传的司机证件！");
			return;
		}
	} else {// 查看选定的这证件
		for (var i = 0; i < $selected.length; i++) {
			var fid = $selected.eq(i).find("input[name=\"fid\"]").val();
			if (fid) {
				var type = $selected.eq(i).find("input[name=\"typeCode\"]").val();
				var code = $selected.eq(i).find("input[name=\"code\"]").val();
				var pid = $selected.eq(i).find("input[name=\"pid\"]").val();
				var ver = $selected.eq(i).find("input[name=\"version\"]").val();
				certs.push({
					type: type,
					code: code,
					pid: pid,
					ver: ver
				});
			}
		}
		if (certs.length > 0) {
			if (option.operateType == true) {
				bc.cert.preview(certs);
			} else {
				bc.cert.print(certs);
			}
		} else {//一张证件都没有上传的时候提示给用户的信息
			bc.msg.info("不能查看/打印未上传的司机证件！");
			return;
		}
		if (option.operateType == true) {
			bc.cert.preview(certs);
		} else {
			bc.cert.print(certs);
		}
	}
};

/**
 * 渲染证件
 * @param {String} option
 * @option {String} code [必填] 编码，新建为空字符串
 * @option {String} type [必填] 类型
 * @option {String} uid  [必填] uid
 * @option {String} tpl   [必填] 模板
 * @option {String} subject [必填] 标题
 * @option {String} pid   [必填] 新建时为0
 * @option {int} id   [必填] id
 * @option {boolean} readonly [可选] 是否只读，默认是非只读false
 * @option {String} operate_type [可选] 操作类型，编辑，查看，预览等，默认是编辑
 * @option {boolean} isCert [可选] 是否是证件配置的预览，默认false,不是证件配置预览，除了此处，其他地方不需要配置
 * @option {boolean} isGetDetails [可选] 是否需要获取证件明细，默认是需要true
 * @option {boolean} upload_status [可选] 是否已经上传，默认是false未上传
 */
bc.cert.edit = function (option) {
	if (!(option && option.code && option.uid && option.id && option.type && option.tpl && option.subject && option.pid)) {
		bc.msg.slide("必须设置option参数！");
		return;
	}

	var type = option.type;
	var code = option.code;

	var operate_type = option.operate_type;  //获得操作类型，编辑，查看，预览
	var isCert = option.isCert;  //获得类型
	var isGetDetails = option.isGetDetails;//是否获取证件明细

	if (operate_type == null && typeof(operate_type) == "undefined") {
		operate_type = "edit";
	}
	var isCert = option.isCert;

	if (isCert == null && typeof(operate_type) == "undefined") {
		isCert = false;
	}
	if (isGetDetails == null && typeof(isGetDetails) == "undefined") {
		isGetDetails = true;
	}

	if (option.upload_status == 0) {//已经上传
		isGetDetails = false;
	}

	option.role = "BC_CERT_MANAGE";

	if (!isCert) {
		bc.cert.getDetail(isGetDetails, operate_type, type, code, function (result) { //根据证件编码和类别编码得到相对应的证件明细
			console.log(result);
			option.extraData = result;
			bc.cert.render(option);
		});
	} else {
		bc.cert.render(option);
	}
};

/**
 * 获取证件明细
 * @param {String} type 证件类别编码
 * @param {String} code 证件配置编码
 * @param {Function} onOk 获取对应的证件明细信息后的回掉函数
 */
bc.cert.getDetail = function (isGetDetails, operate_type, type, code, onOk) {
	var result = [];
	var operate = operate_type;
	var data = {
		typeCode: type, //证件类型
		cfgCode: code   //证件编码
	};

	bc.ajax({
		type: 'post',
		data: data,
		url: bc.root + "/modules/bc/cert/certCfg/getDataByDetail",
		dataType: "json",
		success: function (json) {
			if (json.success) {
				if (isGetDetails) { // 需要获取detail，这将覆盖住form-field的数据
					var details = json.detail;
					if (details.length > 0) {  // 有明细信息
						for (var i = 0; i < details.length; i++) {  // 遍历获取全部的明细信息，并封装成{name:"" ,value:"", type:""}格式
							var page_no = {name: "page_no", value: details[i].page_no, type: "int"};
							var page_width = {name: "attach_width_" + details[i].page_no, value: details[i].width, type: "string"};
							var page_name = {
								name: "attach_name_" + details[i].page_no,
								value: details[i].attach_name,
								type: "string"
							};
							result.push(page_no);     // 分拆的页数
							result.push(page_width);  // 分拆的页的宽度
							result.push(page_name);   // 分拆的页的标记
						}
					}
					var attach_width = {name: "total_width", value: json.attach_width, type: "string"};
					result.push(attach_width); // 总的宽度，即主图片的宽度
				} else {// 不用获取details，也要获取标记的名称
					var details = json.detail;
					if (details.length > 0) {  //有明细信息
						for (var i = 0; i < details.length; i++) {  // 遍历获取全部的明细信息，并封装成{name:"" ,value:"", type:""}格式
							var page_name = {
								name: "attach_name_" + details[i].page_no,
								value: details[i].attach_name,
								type: "string"
							};
							result.push(page_name);  //分拆的页的标记
						}
					}
				}

				var page_num = {name: "page_num", value: json.page_num, type: "int"};
				if (json.combine == null || typeof(json.combine) == "undefined") {  // combine为空，没有合并配置的处理
					var combine = {name: "combine", value: "", type: "string"};
				} else {
					var combine = {name: "combine", value: json.combine, type: "string"};
				}

				var operate_type = {name: "operate_type", value: operate, type: "string"};
				var man_puid = {name: "man_puid", value: json.uid, type: "string"};  // man_puid 为了兼容之前的模板，名字暂时取这个，对应是uid
				var man_ptype = {name: "man_ptype", value: code, type: "string"};

				result.push(page_num);      // 分拆的页数
				result.push(combine);       // 合并配置
				result.push(operate_type);  // 操作类型，预览和编辑
				result.push(man_puid);
				result.push(man_ptype);

				if (onOk) {
					onOk.call(this, result);  // 调用回调函数
				}
			} else {
				bc.msg.slide("获取证件明细失败！");
			}
		}
	});
};
