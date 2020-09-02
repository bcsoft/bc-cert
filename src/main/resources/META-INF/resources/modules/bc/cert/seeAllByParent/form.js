if(!window['bc'])window['bc']={};
bc.seeAllByParent = {
  init : function() {
    console.log("bc.seeAllByParent.init");
    var $form = $(this);

    //绑定展开全部事件
    $form.delegate(".bc-cert-showAllGroups","click",function(){
      $(this).hide();
      $(this).closest( ".ui-widget-content").find(".bc-cert-hiddenAllGroups").show();
      $(this).closest(".bc-page").find(".bc-cert-Groups").each(function(){$(this).show('fast')});
      $(this).closest(".bc-page").find(".bc-cert-showGroups").each(function(){$(this).hide()});
      $(this).closest(".bc-page").find(".bc-cert-hiddenGroups").each(function(){$(this).show()});
    });
    //绑定隐藏全部事件
    $form.delegate(".bc-cert-hiddenAllGroups","click",function(){
      $(this).hide();
      $(this).closest( ".ui-widget-content").find(".bc-cert-showAllGroups").show();
      $(this).closest(".bc-page").find(".bc-cert-Groups").each(function(){$(this).hide('fast')});
      $(this).closest(".bc-page").find(".bc-cert-hiddenGroups").each(function(){$(this).hide()});

      $(this).closest(".bc-page").find(".bc-cert-showGroups").each(function(){$(this).show()});
    });

    //绑定展开事件
    $form.delegate(".bc-cert-showGroups","click",function(){
      $(this).hide();
      $(this).closest(".cert-containers").find(".bc-cert-Groups").toggle('fast');
      $(this).closest( ".cert-containers").find(".bc-cert-hiddenGroups").show();
    });

    //绑定隐藏事件
    $form.delegate(".bc-cert-hiddenGroups","click",function(){
      $(this).hide();
      $(this).closest(".cert-containers").find(".bc-cert-Groups").toggle('fast');
      $(this).closest(".cert-containers").find(".bc-cert-showGroups").show();
    });

    //绑定打印事件
    $form.delegate(".bc-cert-print","click",function(){
      var code =$(this).closest(".ui-widget-header").find(":input[name='code']").val();
      if(code == null || typeof(code) =="undefined"){
        bc.msg.slide("该证件还未上传图片");
        return;
      }else{
        var pid = $(this).closest(".bc-page").find(":input[name='pid']").val();
        var type = $(this).closest(".bc-page").find(":input[name='type']").val();
        var ver =$(this).closest(".ui-widget-header").find(":input[name='ver']").val();
        if(pid == "" || pid == null || type=="" || type==null){
          bc.msg.slide("请先设置option参数");
          return;
        }
        var certs = {"type":type,"pid":pid,"code":code,"ver":ver};
        var isOpenNewWin = false;
        var autoPrint = false;
        bc.cert.print(certs, isOpenNewWin, autoPrint);
      }
    });
  },

    /**
     * 刷新
     */
  refresh : function(){
    console.log("bc.seeAllByParent.refresh");
    var $form = $(this);
    var pid = $form.find(":input[name='pid']").val();
    var type  = $form.find(":input[name='type']").val();

    if(pid == null || pid == "" || type == null || type == "") {
      bc.msg.slide("请先设置option参数");
      return;
    }

    $.ajax({
      url: bc.root+"/modules/bc/cert/seeAllByParent/refresh",
      data: {
        type: type,
        pid: pid
      },
      dataType: "json",
      success: function(json){
        if(json.success === false)
          bc.msg.slide(json.msg);
        else{
          bc.msg.slide(json.msg);
          if(json.yetUpload == json.totalCerts){
            $form.find("#showBaseUploadInfo").text(json.totalCerts+"种证件均没上传");
          }else if(json.alreadlyUpload == json.totalCerts){
            $form.find("#showBaseUploadInfo").text(json.totalCerts+"种证件已全部上传");
          }else{
            $form.find("#showBaseUploadInfo").text("已上传"+json.alreadlyUpload+"种证件，尚欠"+json.yetUpload+"种证件没有上传");
          }
          var lists = json.lists;
          var length = lists.length;

          $form.find(".bc-cert-showAllGroups").hide();
          $form.find(".bc-cert-hiddenAllGroups").show();

          var containers = $form.find(".bc-cert-containers");
          containers.html("");
          var htmls = "";
          for(var i = 0 ; i < length ; i++){
            if(lists[i].isUpload == 'no'){ //还未上传
              htmls+="<div class='cert-containers ui-widget-content' style=\"width:auto;border-bottom:none\">";
              htmls+= "<div class='ui-widget-header title' style='position:relative;'>";
              htmls+="<span class='text' >[未上传] "+lists[i].name+"</span>";
              htmls+="<ul class='inputIcons'>";
              htmls+="<li class='bc-cert-print inputIcon ui-icon ui-icon-print' title='打印此证件图片'></li>";
              htmls+="<li class='inputIcon bc-cert-showGroups ui-icon ui-icon-carat-1-s' style='display:none;' title='展开此证件图片'></li>";
              htmls+="<li class='inputIcon bc-cert-hiddenGroups ui-icon ui-icon-carat-1-n' title='隐藏此证件图片'></li>";
              htmls+="</ul>";
              htmls+="</div>";
              htmls+="<div style='border-width:1px 1px 0 0;margin-bottom:none;' class='bc-cert-Groups'>";
              htmls+="<img src='"+json.htmlPageNamespace+"/bc/libs/themes/default/images/face/sad48.png'/>";
              htmls+="</div>";
              htmls+="</div>";
            }else{ //已经上传
              htmls+="<div class='cert-containers ui-widget-content' style=\"width:auto;border-bottom:none\">";
              htmls+= "<div class='ui-widget-header title' style='position:relative;'>";
              if(lists[i].version != "" && lists[i].version != null ){
                htmls+="<span class='text' >[已上传] "+lists[i].name+" v"+lists[i].version+"</span>";
              }else{
                htmls+="<span class='text' >[已上传] "+lists[i].name+"</span>";
              }
              htmls+="<input type='hidden' name='code' value='"+lists[i].code+"'/>";
              htmls+="<input type='hidden' name='ver' value='"+lists[i].version+"'/>";
              htmls+="<ul class='inputIcons'>";
              htmls+="<li class='bc-cert-print inputIcon ui-icon ui-icon-print' title='打印此证件图片'></li>";
              htmls+="<li class='inputIcon bc-cert-showGroups ui-icon ui-icon-carat-1-s' style='display:none;' title='展开此证件图片'></li>";
              htmls+="<li class='inputIcon bc-cert-hiddenGroups ui-icon ui-icon-carat-1-n' title='隐藏此证件图片'></li>";
              htmls+="</ul>";
              htmls+="</div>";
              htmls+="<div style='border-width:1px 1px 0 0;margin-bottom:none;' class='bc-cert-Groups'>";
              htmls+="<img src='"+json.htmlPageNamespace+"/bc/image/download?id="+lists[i].attach_id+"&ts="+json.appTs+"' style='max-width:100%;' />";
              htmls+="</div>";
              htmls+="</div>";
            }
          }
          containers.html(htmls);
        }
      }
    });
  },

};

