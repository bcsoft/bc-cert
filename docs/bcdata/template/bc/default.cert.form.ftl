<div class="bc-page" data-type='form' style="overflow-y:auto"
   data-initMethod='${ctx.initMethod}' data-namespace='${ctx.namespace}'
   data-js='${ctx.jscss}'
   data-option='${ctx.pageOption}'>
<form class="bc-from" method="post">
<!-- 基本信息区 -->
<div class="blockContainer ui-widget-content" style="width:auto;border-bottom:none">
  <div class="header ui-widget-header title" style="position:relative;">
    <span class="text">基本信息:</span>
    <ul class="inputIcons ignore">
      <li class="toggleAll inputIcon verticalMiddle ui-icon ui-icon-carat-2-n-s" title="折叠|展开所有区域"></li>
      <li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
    </ul>
  </div>
  <div class="detail" style="max-width:42.5em">
    <table class="formFields ui-widget-content" cellspacing="2" cellpadding="0">
      <tr class="widthMarker">
        <td style="width: 6em;">&nbsp;</td>
        <td>&nbsp;</td>
        <td style="min-width: 3em;">&nbsp;</td>
        <td style="width: 4em;">&nbsp;</td>
      </tr>
      <tr>
        <td class="label">标题:</td>
        <td class="value">
          <input type="text" name="subject" value="${form.subject!}" data-scope="form"
            class="ui-widget-content ui-state-disabled" data-validate="required" readonly/>
        </td>
        <td class="label">版本:</td>
        <td class="value">
          <input type="text" name="ver" value="${form.ver!}" data-scope="form" data-type="float"
            class="ui-widget-content ui-state-disabled" data-validate="required" readonly/>
        </td>
      </tr>
      <tr>
        <td class="label">备注:</td>
        <td class="value" colspan="3">
          <input type="text" name="description" value="${form.description!}" data-scope="form" class="ui-widget-content"/>
        </td>
      </tr>
    </table>
    <div class="fileInfo">
      登记：${form.author.name}(${form.fileDate.time?string("yyyy-MM-dd HH:mm")}),
      最后修改：${form.modifier.name}(${form.modifiedDate.time?string("yyyy-MM-dd HH:mm")})
    </div>
  </div>
</div>
<!-- 测试信息区
<div class="blockContainer ui-widget-content" style="width:auto;border-bottom:none">
  <div class="header ui-widget-header title" style="position:relative;">
    <span class="text">测试信息:</span>
    <ul class="inputIcons ignore">
      <li class="toggleAll inputIcon verticalMiddle ui-icon ui-icon-carat-2-n-s" title="折叠|展开所有区域"></li>
      <li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
    </ul>
  </div>
  <div class="detail">
    <label>文本值：<input type="text" name="text1" value="${text1!}" class="ui-widget-content"/></label>
    <label>数字值：<input type="text" name="num1" value="${num1!}" class="ui-widget-content"
      data-validate='{"type": "number"}' data-type="float"/></label>
  </div>
</div> -->

<!-- 图片区：主图 -->
<div class="main blockContainer ui-widget-content" style="width:auto<#if ctx.cfg.pageCount gt 1>;border-bottom-width:0</#if>"
   data-label="${form.subject!}" data-default-format="png">
  <div class="header ui-widget-header title" style="position:relative;">
    <span class="subject">证件图片:</span>
    <span >实物宽度<input type="text" name="attach_width" data-label="主图实物宽度"
      value='<#if ctx.replace || !(attach_width??)>${ctx.cfg.width?string("#.##")}<#else>${attach_width?string("#.##")}</#if>'
      data-validate='{"required":true,"type":"number"}' data-type="float"
      class="ui-widget-content attach-width" style="width:4em"/>毫米</span>
    <ul class="inputIcons ignore">
      <#if !ctx.readonly>
      <#if ctx.cfg.pageCount gt 1>
      <li style="float:left" class="mergeImage" data-combine='${ctx.cfg.combine!}' title='合并各分拆页的图片'>
        <a href="#" style="margin:0 0.2em">合并</a>
      </li>
      </#if>
      <li style="float:left" class="editImage"><a href="#" style="margin:0 0.2em">编辑</a></li>
      </#if>
      <li style="float:left" class="printImage"><a href="#" style="margin:0 0.2em">打印</a></li>
      <li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
    </ul>
  </div>
  <div class="detail">
    <#if attach_id??>
      <img src="${ctx.root}/bc/image/download?id=${attach_id?c}&ts=${ctx.ts}" style="max-width:100%;" />
    <#else>
      <div class="warn" style="padding:2px;">未上传</div>
    </#if>
  </div>
  <input type="hidden" class="attach-id" name="attach_id" data-label="主图" data-type="long" data-empty2delete="true"
    <#if attach_id??>value="${attach_id?c}"</#if>/>
</div>

<#if ctx.cfg.pageCount gt 1>
<!-- 图片区：拆分图 -->
<#assign _ds=ctx.cfg.details/>
<#list 1..ctx.cfg.pageCount as n>
<#list ctx.cfg.details as i><#if i_index == n - 1><#assign _d=i/><#break></#if></#list>
<div class="sub blockContainer ui-widget-content" style="width:auto<#if n != ctx.cfg.pageCount>;border-bottom-width:0</#if>" data-num="${n}"
  data-label="${form.subject}${_d.name}" data-default-format="png">
  <div class="header ui-widget-header title" style="position:relative;">
    <span class="subject">第${n}页(${_d.name})图片:</span>
    <span >实物宽度<input type="text" name="attach_width_${n}" data-label="实物宽度"
      value="<#if ctx.replace || !(.vars['attach_width_' + n]??)>${_d.width?string("#.##")}<#else>${.vars['attach_width_' + n]?string("#.##")}</#if>"
      data-validate='{"required":true,"type":"number"}' data-type="float"
      class="ui-widget-content attach-width" style="width:4em"/>毫米</span>
    <ul class="inputIcons ignore">
      <#if !ctx.readonly>
      <li style="float:left" class="editImage"><a href="#" style="margin:0 0.2em">编辑</a></li>
      </#if>
      <li style="float:left" class="printImage"><a href="#" style="margin:0 0.2em">打印</a></li>
      <li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
    </ul>
  </div>
  <div class="detail">
    <#if .vars['attach_id_' + n]??>
      <img src="${ctx.root}/bc/image/download?id=${.vars['attach_id_' + n]!?c}&ts=${ctx.ts}" style="max-width:100%" />
    <#else>
      <div class="warn" style="padding:2px;">未上传</div>
    </#if>
  </div>
  <input type="hidden" class="attach-id" name="attach_id_${n}" data-label="拆分图附件ID" data-type="long"
  <#if .vars['attach_id_' + n]??>value="${.vars['attach_id_'+n]?c}"</#if>/>
</div>
</#list>
</#if>

<!-- 证件标识信息 -->
<input type="hidden" name="id" data-scope="form" class="ignore"<#if form.id??> value="${form.id?c}"</#if> data-type="long"/>
<input type="hidden" name="type" data-scope="form" class="ignore" value="${form.type}"/>
<input type="hidden" name="code" data-scope="form" class="ignore" value="${form.code}"/>
<input type="hidden" name="pid" data-scope="form" class="ignore" value="${form.pid?c}" data-type="long"/>
<input type="hidden" name="uid" data-scope="form" value="${form.uid}"/>
<input type="hidden" name="tpl" data-scope="form" value="${form.tpl}"/>
<input type="hidden" name="pname" value="${pname!}" data-type="string"  data-validate="required"  />
</form>
</div>