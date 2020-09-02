bc.namespace("bc.certCfg.form");
bc.certCfgForm = {
  init : function() {
    var $page = $(this);
    var $form = $("form",$page);

    //新建时默认明细默认添加8行空行
    var eId = $form.find("input[name='e.id']").val();
    if(eId == "") {
      var $table = $form.find("#itemsTable");
      var num = 8;
    }

    // 绑定点击按钮展开明细事件
    $form.delegate(".showItem", "click", function() {
      var $li = $(this);
      var $item = $li.closest(".item");
      var status = $li.attr("data-show-status");
      $li.toggleClass("ui-icon-carat-1-s ui-icon-carat-1-n");
      var $table = $item.find("table");
      $table.toggle("fast", function() {
        if (status == "1") {
          $li.attr("data-show-status", "2");
          $li.attr('title', '点击展出内容');
        } else {
          $li.attr("data-show-status", "1");
          $li.attr('title', '点击隐藏内容');
        }
      });
    });

    //页码改变的事件
    $form.delegate("input[name='e.pageCount']","change",function(){

      var $page = $(this);
      var $form = $(this).closest("form");
      var $table = $form.find("#itemsTable");

      var pageCount = parseInt($(this).val());
      var $trs = $form.find(".ui-widget-content .row");
      var curRowSize = $trs.length; // 当前明细条目数
      var num = 0;

      /*if (!bc.validator.validate($form)) { //如果验证失败
        return false;
      }*/

      var validate = JSON.parse($page.attr("data-validate"));
      var required = validate.required;
      if(required){//必填或有值时

          //再验证其他细化的参数
          if(validate.type == "number" || validate.type == "digits"){//数字或整数
            //最小值验证
            if(validate.min || validate.min === 0 ){
              ok = bc.validator.methods.min.call(validate,this);
              if(!ok){
                bc.validator.remind(this, "min", [validate.min+""],validate);
                return false;
              }else{//验证是否是整数
                ok = bc.validator.methods.digits.call(validate,this);
                if(!ok){
                  bc.validator.remind(this, "digits", [validate.digits+""],validate);
                  return false;
                }
              }
            }

          }

      }

      //当页码是1时
      if(pageCount == 1){
        for(var i = 0 ; i < curRowSize;i++){
          $($trs[i]).remove();
        }
        $form.find("input[name='e.combine']").val("");
      }else{
        if(pageCount > curRowSize){
          num = pageCount - curRowSize;
          bc.certCfgForm.autoAddItem($form,$table,num);
        }else if(pageCount < curRowSize){

          for ( var i = pageCount; i < curRowSize; i++) {
            $($trs[i]).remove();
          }
          var $item = $(this).closest(".item");

          // 对执行删除行操作之后的处理
          var $afterDelRows = $item.find("tr.row");
          if (logger.infoEnabled) {
            logger
                .info("$afterDelRows="
                    + $afterDelRows.length);
          }
          //调整序号
          $afterDelRows.each(function(index, value) {
            $rowNumInput = $(this).find(
                "input.bc-cert-certCfgDetailItem-page_no");
            $rowNumInput.val(index + 1);
          });


        }else{

        }
      }

    });

    // 点击选中行
    $form.find(".item").delegate(
        "tr.ui-widget-content.row>td.id",
        "click",
        function() {
          $(this).parent().toggleClass("ui-state-highlight").find(
              "td:eq(0)>span.ui-icon").toggleClass(
              "ui-icon-check");
        });
    $form.find(".item").delegate(
        "tr.ui-widget-content.row input",
        "focus",
        function() {
          $(this).closest("tr.row").removeClass("ui-state-highlight")
              .find("td:eq(0)>span.ui-icon").removeClass(
                  "ui-icon-check");
        });

    // 上移表中选中的明细项目
    $form.find(".upItem").click(
        function() {
          var $item = $(this).closest(".item");
          var $trs = $item.find("tr.ui-state-highlight");
          if ($trs.length == 0) {
            bc.msg.slide("请先选择要上移的！");
            return;
          } else {
            $trs.each(function() {
              var $tr = $(this);
              if ($tr[0].rowIndex < 2) {
                bc.msg.slide("选中的为第一条信息,不能再上移！");
              } else {
                var $beroreTr = $tr.prev();
                var trOrderNo = parseInt($tr.find(
                    ".bc-cert-certCfgDetailItem-page_no").val()); // 该行明细的序号
                var beroreTrOrderNo = parseInt($beroreTr.find(
                    ".bc-cert-certCfgDetailItem-page_no").val()); // 该行前一行明细的序号

                $beroreTr.insertAfter($tr); // 该行前一行插到改行之后
                $tr.find(".bc-cert-certCfgDetailItem-page_no").val(
                    trOrderNo - 1); // 该行明细的序号减1
                $beroreTr.find(".bc-cert-certCfgDetailItem-page_no")
                    .val(beroreTrOrderNo + 1); // 该行前一行明细的序号加1
              }
            });
          }
        });

    // 下移表中选中的明细项目
    $form.find(".downItem").click(
        function() {
          var $item = $(this).closest(".item");
          var $trs = $item.find("tr.ui-state-highlight");
          if ($trs.length == 0) {
            bc.msg.slide("请先选择要下移的！");
            return;
          } else {
            for ( var i = $trs.length; i > 0; i--) {
              var $tr = $($trs[i - 1]);
              var $afterTr = $tr.next();
              if ($afterTr.length == 0) {
                bc.msg.slide("选中的为最后一条信息,不能再下移！");
              } else {
                var trOrderNo = parseInt($tr.find(
                    ".bc-cert-certCfgDetailItem-page_no").val()); // 该行明细的序号
                var afterTrOrderNo = parseInt($afterTr.find(
                    ".bc-cert-certCfgDetailItem-page_no").val()); // 该行后一行明细的序号

                $afterTr.insertBefore($tr); // 该行后一行插到改行之前
                $tr.find(".bc-cert-certCfgDetailItem-page_no").val(
                    trOrderNo + 1); // 该行明细的序号加1
                $afterTr.find(".bc-cert-certCfgDetailItem-page_no")
                    .val(afterTrOrderNo - 1); // 该行前一行明细的序号减1
              }
            }
          }
        });

    // 添加明细条目
    $form.find(".addItem").click(function() {
      var $item = $(this).closest(".item");
      var name = $item.attr("data-item-name");
      var type = $item.attr("data-item-type");
      if (type == null || type == '')
        return;

      var $form = $(this).closest("form");
      var $table = $form.find("#itemsTable");

      var $trs = $form.find(".ui-widget-content .row");
      var curRowSize = $trs.length; // 当前明细条目数

      if(curRowSize == 0){
        for(var i = 0 ; i < 2 ; i++){
          bc.certCfgForm.addItem($form,$table);
        }
        $form.find("input[name='e.pageCount']").val(2);

      }else{
        bc.certCfgForm.addItem($form,$table);
        $form.find("input[name='e.pageCount']").val(curRowSize+1);

      }


    });

    // 删除最后一行(当剩下2行时，全部删除)数据
    $form.find(".deleteItem").click(
        function() {
          var $form =  $(this).closest("form");
          var $item = $(this).closest(".item");
          var name = $item.attr("data-item-name");

          //var $trs = $item.find("tr.ui-state-highlight");
          var $trs = $form.find(".ui-widget-content .row");
          var length = $trs.length;
          if ( length == 0) {
            bc.msg.slide("没有需要删除的明细！");
            return;
          }

          if ( length == 2) {
            //删除最后两条
            for(var i = 0 ; i < 2; i ++){
              $($trs[i]).remove();
            }

            //页码为1
            $form.find("input[name='e.pageCount']").val(1);

            $form.find("input[name='e.combine']").val("");//将合并配置设为空
          }else{
            //删除最后一条
            $($trs[length - 1]).remove();

            //页码减掉1
            $form.find("input[name='e.pageCount']").val($trs.length-1);
          }
        });
  },

  /** 自动添加指定数量明细空条目
   * @param $form 表单上下文
   * @param $table 需要添加明细的表格
   * @param num 需要添加的条目数量
   */
  autoAddItem : function($form,$table,num) {
    for (var i=0; i<num; i++) {
      bc.certCfgForm.addItem($form,$table);
    }
  },

  /**
   * 添加明细
   * @param $form 表单的上下文
   * @param $table 需要添加明细的表格
   */
  addItem : function($form,$table) {
    var tableEl = $table[0];
    var extraData = $form.find("input[name='data']").val();

    // 插入行
    var newRow = tableEl.insertRow(tableEl.rows.length);
    newRow.setAttribute("class", "ui-widget-content row");

    var colNums = 0; // 列序号

    // 序号列
    var curRowSize = $form.find(".ui-widget-content .row").length; // 当前明细条目数
    if (logger.infoEnabled) {
      logger.info("curRowSize=" + curRowSize);
    }

    // 设置页码
    var page_no = '<input type="text" style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;text-align:center;"'
        + 'class="ui-widget-content bc-cert-certCfgDetailItem-page_no"'
        + 'value='
        + '"'
        + curRowSize
        + '"'
        + 'data-validate="required" readonly="false" />'

    bc.certCfgForm.insertMiddleCell(newRow, colNums++, page_no);



    // 标记
    bc.certCfgForm.insertMiddleCell(
        newRow,
        colNums++,
        '<input type="text" style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;text-align: center;"'
            + 'class="ui-widget-content bc-cert-certCfgDetailItem-name" data-validate="required" />');

    // 打印宽度(毫米)
    bc.certCfgForm.insertMiddleCell(
        newRow,
        colNums++,
        '<input type="text" style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;"'
            + 'class="ui-widget-content bc-cert-certCfgDetailItem-width"'
            + 'data-validate='
            + "'"
            + '{required:true,type:"number",min:0.01}'
            + "'"
            + '/>');

    // 配件id列
    var cell = newRow.insertCell(colNums++);
    cell.style.minWidth = "0.001em";
    cell.setAttribute("class", "last");
  },

  /**
   * 插入表格中间列
   */
  insertMiddleCell : function(newRow, index, innerHTML) {
    var cell = newRow.insertCell(index);
    cell.style.padding = "0 0 0 2px";
    cell.setAttribute("class", "middle");
    cell.innerHTML = innerHTML;
  },

  /**
   * 预览的处理
   */
  preview : function() {

    var $page = $(this);
    var $form = $("form", $page);
    var $table = $form.find("#itemsTable");
    var $rows = $form.find("#itemsTable .row");
    /*对合并配置项的处理*/
    var combineValue = $form.find("input[name='e.combine']").val();
    var pageCountValue = parseInt($form.find("input[name='e.pageCount']").val());
    if(pageCountValue > 1){  //分拆页大于1
      if(combineValue == "" || typeof(combineValue)=="undefined"){
        bc.msg.slide("合并配置不能为空！");
        return;
      }
    }

    if(bc.certCfgForm.beforeSave.call(this,'')==false){
      return;
    }

    var id = $form.find("input[name='e.id']").val();

    if(id==""){
      bc.msg.slide("请先保存！");
      return;
    }
    var tpl = $form.find("input[name='e.tpl']").val();
    var type= $form.find("input[name='e.certType.code']").val();
    var code = $form.find("input[name='e.code']").val();
    var subject = $form.find("input[name='e.name']").val();
    var combine =  $form.find("input[name='e.combine']").val();
    var uid = $form.find("input[name='e.uid']").val();
    var total_width = $form.find("input[name='e.width']").val();

    var page_num = 0;

    var $tr_r=$page.find(">.bc-grid>.data>.right tr.ui-state-highlight");

    var list = new Array();
    $rows.each(function(index) {
      var $inputs = $(this).find(":input");
      list.push($inputs.filter(".bc-cert-certCfgDetailItem-page_no").val());
      list.push($inputs.filter(".bc-cert-certCfgDetailItem-width").val());
      list.push($inputs.filter(".bc-cert-certCfgDetailItem-name").val());
      page_num++;
    });

    var extraData = [{name:"man_puid",value:uid,type:"string"},{name:"man_ptype",value:code,type:"string"},{name:"combine",value:combine,type:"string"},
                    {name:"attach_width",value:total_width,type:"string"},{name:"operate_type",value:"preview",type:"string"},
                    {name:"page_num",value:page_num,"type":"int"}, {name:"subject",value:subject,"type":"string"}];
    if(page_num > 1){
      for(var i=0; i<page_num; i++){
        var no = i+1;
        var page_no = {name:"page_no",value:list[3*i+0],"type":"int"};
        var page_width = {name:"attach_width_"+no,value:list[3*i+1],"type":"string"};
        var page_name = {name:"attach_name_"+no,value:list[3*i+2],"type":"string"};
        extraData.push(page_no);
        extraData.push(page_width);
        extraData.push(page_name);
      }
    }

    var option_ = {
      tpl:tpl
      ,type:type
      ,pid:id
      ,id:id
      ,code:code
      ,subject:subject
      ,uid:uid
      ,id:id
      ,isCert:true
      ,extraData:extraData
      ,isGetDetails:false
      ,buttons: [
        {click : "bs.carManCertForm.save",text : "保存"}
        ,{click : "bs.carManCertForm.saveAndClose",text : "保存并关闭"}
      ]
    };
    bc.cert.edit(option_);
    //bc.customForm.render(option_);
  },


  /**
   * 保存的处理
   */
  save : function() {

    var $page = $(this);
    var $form = $("form", $page);
    var $table = $form.find("#itemsTable");
    var method = "save";

    /*对合并配置项的处理*/
    var combineValue = $form.find("input[name='e.combine']").val();
    var pageCountValue = parseInt($form.find("input[name='e.pageCount']").val());
    if(pageCountValue > 1){  //分拆页大于1
      if(combineValue == "" || typeof(combineValue)=="undefined"){
        bc.msg.slide("合并配置不能为空！");
        return;
      }
    }

    //bc.certCfgForm.getDetailData.call(this);

    if(bc.certCfgForm.beforeSave.call(this,method)==false){
      return;
    }

    /*if (!bc.validator.validate($form)) { //如果验证失败
      return false;
    }*/

    /*var url = bc.root + "/modules/bc/cert/certCfg/save";
    var data = $form.serialize();*/
    // 使用ajax保存
    /*bc.ajax({
      url : url,
      data : data,
      dataType : "json",
      success : function(json) {

        //bc.msg.slide(json.msg);

        bc.certCfgForm.afterSave($page,method,json);
      }
    });*/

    //调用标准的方法执行保存
    bc.page.save.call(this,{callback: function(json){
      if(json.success){
        bc.msg.slide(json.msg);
      }else{
        bc.msg.alert(json.msg);
      }
      return false;
    }});
  },

  beforeSave : function(method){
    var $page = $(this);
    var $form = $("form", $page);
    var $table = $form.find("#itemsTable");
    var $emptyRows = bc.certCfgForm.findEmptyRows($table); //找出明细中的空行

    var page_no = $form.find("input[name='e.pageCount").val();

    if(parseInt(page_no)==1){  //page_no为1时的处理
      if (!bc.validator.validate($form)) { //如果验证失败
        return false;
      }

      //保存明细信息
      bc.certCfgForm.getDetailData.call(this);
    }else{
      // 验证明细是否有填写
      var $itemRows = $form.find("tr.ui-widget-content.row");
      if ($itemRows.length == 0 || $itemRows.length == $emptyRows.size()) { // 检测检测是否填写明细信息
        bc.msg.alert("请先填写各分拆页参数信息！");
        return false;
      }

      if (!bc.validator.validate($form)) { //如果验证失败
        return false;
      }

      //保存明细信息
      bc.certCfgForm.getDetailData.call(this);
    }


  },

  /**
   * 保存后的处理
   */
  /*afterSave : function($page,method,json) {
    var $form = $("form", $page);

    if (json.success == false) {
      bc.msg.alert(json.msg);
    } else {
      if (logger.debugEnabled)
        logger.debug("issue success.json="
            + jQuery.toJSON(json));
      if (json.id) {
        $form.find("input[name='e.id']").val(json.id);
      }

      //记录明细保存后的id
      if(json.items) {
        var $rows = $form.find("#itemsTable .row");
        for(var i=0; i<eval(json.items).length; i++) {
          $rows.eq(i).attr("data-id",eval(json.items)[i].id)
        }
      }

      var data_status ="";
      if(method == "save") {
        data_status =  "saved";
      } else {
        data_status = "stored";
      }

      // 记录表单已保存状态
      $page.attr("data-status",data_status).data("data-status",data_status);


      // 显示提示信息
      bc.msg.slide(json.msg);
    }
  },*/

  /**
   * 查找空行
   * return $emptyRows 返回空行jquery对象集合
   */
  findEmptyRows : function($table) {
    var $rows = $table.find(".row");
    var $emptyRows = $();

    //遍历所有明细行
    $rows.each(function() {
      var $row = $(this);
      //如果该行为空行
      if(bc.certCfgForm.isEmptyRow($row) == true) {
        $emptyRows = $emptyRows.add($row);
      }
    });

    return $emptyRows;
  },

  /**
   * 判断该行是否为空行
   * retrun true/false
   */
  isEmptyRow : function($row) {
    var isEmptyRow = true;
    //判断该行是否含有除序号列外的非空列
    var $tds = $row.find("td.middle:gt(0)");
    $tds.each(function() {
      var $td = $(this);
      var isEmptyCol = true;
      //查找该列的控件
      var $fields = $td.find(":text,select");

      //遍历 控件，如果集合里含有值为非空的控件，isEmptyCol变为false
      $fields.each(function() {
        var $field = $(this);
        if($field.val().trim() != "") {
          isEmptyCol = false;
          return false; //跳出循环
        }
      });

      //如果该列为非空列
      if(isEmptyCol == false) {
        //该行为非空行
        isEmptyRow = false
        return false;//跳出循环
      }
    });
    return isEmptyRow;
  },

  /**
   * 获得配置明细的信息
   */
  getDetailData : function() {
    var $form = $(this);
    var $rows = $form.find("#itemsTable .row");
    var items = [];
    $rows.each(function(index) {
      var $inputs = $(this).find(":input");

      var json = {
        pageNo : $inputs.filter(".bc-cert-certCfgDetailItem-page_no").val(),
        width : $inputs.filter(".bc-cert-certCfgDetailItem-width").val(),
        name : $inputs.filter(".bc-cert-certCfgDetailItem-name").val(),
        id : $inputs.filter(".bc-cert-certCfgDetailItem-id").val(),
        pid : $form.find("input[name='e.id']").val()
      };
      var id = $(this).attr("data-id");
      if (id && id.length > 0)
        json.id = id;
      items.push(json);

    });
    $form.find("input:[name='details']").val($.toJSON(items));
  },

  /**
   * 获得预览的参数信息
   */
  getPreiewData : function() {
    var $form = $(this);
    var $rows = $form.find("#itemsTable .row");
    var previewDatas = [];
    $rows.each(function(index) {
      var $inputs = $(this).find(":input");

      var json = {
        tpl : $inputs.filter(".bc-cert-certCfgDetailItem-page_no")
            .val(),
        subject : $inputs.filter(
            ".bc-cert-certCfgDetailItem-width").val(),
        type : $inputs.filter(
            ".bc-cert-certCfgDetailItem-name").val(),
        code : $inputs.filter(".bc-cert-certCfgDetailItem-id")
            .val(),
        pid : $form.find("input[name='e.id']").val()
      };
      var id = $(this).attr("data-id");
      if (id && id.length > 0)
        json.id = id;
      previewDatas.push(json);

    });
    $form.find("input:[name='previewDatas']").val($.toJSON(previewDatas));
  },

};
