

bc.certInfoFormView = {
  /** 访问配置 **/
  access : function(){
    var $page = $(this);
    // 获取用户选中的条目
    var ids = bc.grid.getSelected($page.find(".bc-grid"));
    if(ids.length == 0){
      bc.msg.slide("请先选择需要访问配置的信息！");
      return;
    }

    if(ids.length > 1){
      bc.msg.slide("只能对一个信息操作！");
      return;
    }

    var $tr = $page.find(".bc-grid>.data>.right tr.ui-state-highlight");
    var $hidden = $tr.data("hidden");

    bc.certInfoFormView.addAccessControl({
      docId:ids[0],
      docType:$hidden.accessControlDocType,
      docName:$hidden.accessControlDocName,
      showRole:"01"
    });
  },
  /**
   * 添加访问监控配置
   * @param {Object} option 配置参数
   * @option {String} docId [必填] 文档标识
   * @option {String} docType [必填] 文档类型
   * @option {String} docName [必填] 文档名称
   * @option {String} title [可选]  监控配置窗口标题
   * @option {String} name [可选]  监控配置窗口名称
   * @option {String} mid [可选] 监控配置窗口标识
   */
  addAccessControl : function(option) {
    if(option.docId==null){
      bc.msg.alert("未配置文档标识");
      return;
    }

    if(option.docType==null){
      bc.msg.alert("未配置文档类型");
      return;
    }

    if(option.docName==null){
      bc.msg.alert("未配置文档名称");
      return;
    }

    //弹出选择对话框
    bc.page.newWin({
        url:bc.root+"/bc/accessControl/configureFromDoc",
        data:{  docId:option.docId,
            docType:option.docType,
            docName:option.docName,
            isFromDoc:true,
            showRole:option.showRole?option.showRole:null},
        mid:option.mid ? option.mid : "configureFromDoc."+option.docId+"."+option.docType,
        name:option.name ? option.name : "["+option.docName+"]监控配置",
        title:option.title ? option.title : "["+option.docName+"]监控配置",
      });
  },

  edit : function(){
    var $page = $(this);
    // 确定选中的行
    var $trs = $page.find(">.bc-grid>.data>.left tr.ui-state-highlight");
    if($trs.size()==0){
      bc.msg.slide("请选择需要编辑的司机图片！");
      return;
    }else if($trs.size()>1){
      bc.msg.slide("一次只能选择一个司机的证件图片！");
      return;
    }

    //选中的右边行
    var $tr_r=$page.find(">.bc-grid>.data>.right tr.ui-state-highlight");
    // 取得隐藏列数据
    var $hidden=$tr_r.data("hidden");

    var option=$.extend($hidden,{afterClose: function(status){
      if(status)
        bc.grid.reloadData($page);
    }});
    logger.info($.toJSON(option));

    var uid = option.uid;
    var code = option.code;

    option.buttons=[{click : "bc.customForm.save",text : "保存"}];

    option.isGetDetails=false;
    bc.cert.edit(option); //调用证件模块通用的编辑功能
  },


  /**
   * 保存
   */
  /*save : function(){

    var $page = $(this);
    var $form = $("form", $page);

    // 将表单的data-scope设为form
    var formInfo = bc.customForm.setFormInfo($form, {
      scope : 'form'
    });

    bc.customForm.save();
  },*/


};