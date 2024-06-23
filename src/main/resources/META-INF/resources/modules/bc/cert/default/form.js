bc.namespace("bc.defaultCertForm");
bc.defaultCertForm = {
  init: function (option, readonly) {
    var $page = $(this);
    if (readonly) return;

    // 区域的折叠或展开
    $page.on("click", ".toggleMe", function () {
      var $this = $(this);
      $this.toggleClass("ui-icon-triangle-1-n ui-icon-triangle-1-s");
      $this.closest(".blockContainer").find(".detail").toggleClass("hide");
      return false;
    });
    $page.on("click", ".toggleAll", function () {
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
    $page.on("click", ".printImage", function () {
      var $block = $(this).closest(".blockContainer");
      var attachId = $block.find("input.attach-id").val();
      var attachWidth = $block.find("input.attach-width").val();
      if (!attachId || attachId.length == 0) {
        bc.msg.info("请先上传图片！");
        return false;
      }
      if (!attachWidth || attachWidth.length == 0) {
        bc.msg.info("请先设置图片的实物宽度！");
        return false;
      }
      //新版打印接口的数据格式certs:[{"pid":11771810,"ver":1,"code":"certJZZ","type":"CarManCert"}]
      //bc.image.print(attachId, attachWidth);
      var certs = [];
      certs.push({
        attachId: attachId
        , attachWidth: attachWidth
        , code: code
        , type: type
        , isDirectlyPrint: true
      });
      bc.cert.print(certs);
      return false;
    });

    // 编辑图片
    $page.on("click", ".editImage", function () {
      var $block = $(this).closest(".blockContainer");
      var pageNo = $block.attr("data-num");
      var $attachId = $block.find("input.attach-id");
      var attachId = $attachId.val();
      var data = {};
      data.fname = $block.attr("data-label");
      data.format = $block.attr("data-default-format") || "png";

      if (attachId && attachId.length > 0) {// 已上传的图片
        data.id = "attach:" + attachId + ":true";// 设为true标记编辑后生成新的图片附件
      }
      data.puid = uid;// 表单的uid
      data.ptype = code + (pageNo ? "." + pageNo : "");// 证件配置的编码
      data.onOk = function (attach) {
        var id = attach.id.split(":")[1];
        $attachId.val(id);
        var $img = $block.find("img");
        var src = bc.root + "/bc/image/download?id=" + id;// + "&ts=" + new Date().getTime();
        if ($img.length > 0) {
          $img.attr("src", src);
        } else {
          $block.find(".detail").empty().append('<img src="' + src + '" style="max-width:100%" />');
        }

        // 如果分图上传了，则标记主图需要重新合并
        $page.find(":hidden[name=needReMerge]").val($block.hasClass("sub"));
      }
      bc.photo.handle(data);
      return false;
    });

    // 合并图片
    $page.on("click", "li.mergeImage", function () {
      bc.defaultCertForm.doMergeImage.call($page[0], "确定合并图片吗？");
    });

    // 拖拽上传图片
    $page.on("dragover", ".detail:not(.baseInfo),.detail:not(.baseInfo) *", function (e) {
      e.stopPropagation();
      e.preventDefault();//取消默认浏览器拖拽效果
    });
    $page.on("drop", ".detail:not(.baseInfo),.detail:not(.baseInfo) *", function (e) {
      e.stopPropagation();
      e.preventDefault();//取消默认浏览器拖拽效果
      var $detail = $(this).closest(".detail");

      var pageNo = $(this).closest(".blockContainer").attr("data-num");
      $detail.removeClass("ui-state-active");

      if (e.originalEvent.dataTransfer.files && e.originalEvent.dataTransfer.files.length) {
        var file = e.originalEvent.dataTransfer.files[0];

        // 上传图片
        var reader = new window.FileReader();
        reader.onload = function (e) {
          //console.log(e.target.result);
          var name = file.name.split(".")[0];
          var format = file.name.split(".")[1];
          var image = {
            data: e.target.result,
            format: format,
            fname: name,
            ptype: `${code}${(pageNo ? "." + pageNo : "")}`,
            puid: uid
          };

          $.ajax({
            type: "post",
            method: "post",
            dataType: "json",
            url: bc.root + "/bc/photo/upload",
            data: image,
            success: function (attach) {
              if (attach.success) {
                //展现图片
                var id = attach.id.split(":")[1];
                $detail.siblings(":hidden.attach-id").val(id);
                var $img = $detail.find("img");
                var src = bc.root + "/bc/image/download?id=" + id;// + "&ts=" + new Date().getTime();
                if ($img.length > 0) {
                  $img.attr("src", src);
                } else {
                  $detail.empty().append('<img src="' + src + '" style="max-width:100%" />');
                }

                // 有分页图片时才更换主图片需合并标识
                $page.find(":hidden[name=needReMerge]").val($detail.closest(".blockContainer").hasClass("sub"));
              } else {
                bc.msg.info(attach.msg);
              }
            },
            error: function (e) {
              alert("上传图片异常！");
            }
          });
        };
        reader.readAsDataURL(file);
      }
    });

    $page.on("dragenter", ".detail:not(.baseInfo),.detail:not(.baseInfo) *", function (e) {
      e.stopPropagation();
      e.preventDefault();//取消默认浏览器拖拽效果
      var $detail = $(this).closest(".detail");
      // 鼠标经过时添加被选中央视
      $detail.addClass("ui-state-active");
    });

    $page.on("dragend", ".detail:not(.baseInfo),.detail:not(.baseInfo) *", function (e) {
      e.stopPropagation();
      e.preventDefault();//取消默认浏览器拖拽效果
      var $detail = $(this).closest(".detail");
      // 鼠标离开时删除被选中央视
      $detail.removeClass("ui-state-active");
    });

    $page.on("dragleave", ".detail:not(.baseInfo),.detail:not(.baseInfo) *", function (e) {
      e.stopPropagation();
      e.preventDefault();//取消默认浏览器拖拽效果
      var $detail = $(this).closest(".detail");
      // 鼠标离开时删除被选中央视
      $detail.removeClass("ui-state-active");
    });
  },
  /**
   * 表单分页图片合并为主图
   *
   * @param confirmInfo    合并确认提示信息
   * @param callbackOption 回调函数选项配置, {callFrom: 调用源, func: 回调执行函数, params: 回调执行函数参数}
   */
  doMergeImage: async function (confirmInfo, callbackOption) {
    // 获取图片原始的像素宽度
    function getImageOriginalPixelWidth(url) {
      return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => resolve(img.width);
        img.onerror = () => reject(new Error(`图片加载失败！url=${url}`));
        img.src = url;
      })
    }

    var $page = $(this);
    var $mainBlock = $page.find(".blockContainer.main");
    var $mainAttachId = $mainBlock.find("input.attach-id");

    // 获取合并配置参数
    var combine = $page.find("li.mergeImage").attr("data-combine");
    if (!combine || combine === "") {
      bc.msg.alert("缺少合并配置参数！");
      return false;
    }

    var type = $page.find(":input[name='type']").val();// 证件类别编码
    var code = $page.find(":input[name='code']").val();// 证件编码
    var uid = $page.find(":input[name='uid']").val();// 表单UID

    // 判断是否是连续水平合并或垂直合并（true代表只需提供至少2个分拆图就即可执行合并，否则所有分拆图都必须提供才能合并）
    var isHV = (combine.indexOf("h") !== -1 || combine.indexOf("v") !== -1);

    // 获取上传的分拆图([0-id,1-实物宽度,2-图片地址])
    var subAttachs = [];
    var ok = true;
    $mainBlock.siblings(".sub").each(function () {
      const $this = $(this);
      const attachId = $this.find("input.attach-id").val();
      if (!attachId || attachId === "") {
        if (!isHV) {
          bc.msg.alert($(this).attr("data-label") + "未上传！");
          ok = false;
        }
      } else {
        const attachRealWidth = parseInt($this.find("input.attach-width")[0].value);
        const attachImageSrc = $this.find("img")[0].src;
        subAttachs.push([attachId, attachRealWidth, attachImageSrc]);
      }
    });
    if (!ok) return;
    if (subAttachs.length < 1) {
      bc.msg.alert("至少需要1张以上图片才能进行合并！");
      return;
    }
    var format = $mainBlock.attr("data-default-format") || "png";
    var filename = $mainBlock.attr("data-label");

    // 重构 combine，按第一页图片的像素宽的和实物宽度为基数1求其它页的缩放比例，然后添加到 combine 配置内
    // 带缩放的格式为 1:5 6:0.5,1;1,1，第二个 : 号后的数字（可选配置） 0.5 代表缩放到 0.5 倍后再参与合并
    if (!isHV) {
      const pixelWidth0 = await getImageOriginalPixelWidth(subAttachs[0][2])
      const realWidth0 = subAttachs[0][1]
      let imageIndex = 0;
      combine = (await Promise.all(combine
        .split(";")
        .map(async (h, i) => {
          imageIndex += i;
          return (await Promise.all(h.split(",")
            .map(async (p, j) => {
              imageIndex += j;
              // 第一页作为基数1，不处理
              if (i === 0 && j === 0) return p;

              // 以第一页为基数计算其余页的缩放比例 scale = Wsn / Ws1 x Wp1 / Wpn
              const ps = p.split(":")
              const pixelWidth = await getImageOriginalPixelWidth(subAttachs[imageIndex][2])
              const realWidth = subAttachs[imageIndex][1]
              const scale = realWidth / realWidth0 * pixelWidth0 / pixelWidth
              return (ps.length > 1 ? [ps[0], ps[1], scale] : [ps[0], "", scale]).join(":")
            })
          )).join(",")
        })
      )).join(";")
    }

    bc.msg.confirm(confirmInfo, function () {
      bc.ajax({
        url: bc.root + "/bc/image/combine",
        data: {
          ids: subAttachs.map(a => a[0]).join(","),
          mixConfig: combine,
          fileType: format,
          filename: filename,
          puid: uid,
          ptype: `${type}.${code}`
        },
        dataType: "json",
        success: function (json) {
          if (json.success) {
            bc.msg.slide("图片合并成功！");
            $mainAttachId.val(json.id);
            var $img = $mainBlock.find("img");
            var src = bc.root + "/bc/image/download?id=" + json.id;
            if ($img.length > 0) {
              $img.attr("src", src);
            } else {
              $mainBlock.find(".detail").empty().append('<img src="' + src + '" style="max-width:100%" />');
            }

            $page.find(":hidden[name=needReMerge]").val(false);

            if (callbackOption && callbackOption.hasOwnProperty("func")) {
              if (callbackOption.hasOwnProperty("callFrom"))
                callbackOption.func.call(callbackOption.callFrom, ...callbackOption.params)
              else callbackOption.func(...callbackOption.params);
            }
          }
        }
      });
    }, function () {
      // 点击否时直接保存不合并
      if (callbackOption && callbackOption.hasOwnProperty("func")) {
        if (callbackOption.hasOwnProperty("callFrom"))
          callbackOption.func.call(callbackOption.callFrom, ...callbackOption.params)
        else callbackOption.func(...callbackOption.params);
      }
    });
  },
  baseSave: function (afterClose) {
    var $page = $(this);
    var needReMerge = $page.find(":hidden[name=needReMerge]").val().toLowerCase() === "true";
    var callbackOption = {
      callFrom: this,
      params: [afterClose],
      func: bc.cform.doSave
    };
    // 验证是否需要重新合并主图
    if (needReMerge) {
      bc.defaultCertForm.doMergeImage.call(this,"分页图片有更新，是否自动合并再保存？“是”则合并保存，“否”则临时保存。", callbackOption);
    } else { // 否则直接保存
      bc.cform.doSave.call(this, afterClose)
    }
  },
  save: function () {
    bc.defaultCertForm.baseSave.call(this, false);
  },
  saveAndClose: function () {
    bc.defaultCertForm.baseSave.call(this, true);
  },
  /** 自定义的验证 */
  validate: function () {
    var $page = $(this);
    var mainAttachId = $page.find(".main input.attach-id").val();
    if (!mainAttachId || mainAttachId == "") {
      bc.msg.alert("证件图片未上传！");
      return false;
    }
    return bc.validator.validate($page);
  }
};