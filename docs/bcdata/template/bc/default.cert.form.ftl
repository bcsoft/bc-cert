<div class="bc-page" data-type='form' style="overflow-y:auto"
	 data-initMethod='${_initMethod}' data-namespace='${_namespace}'
	 data-js='${_jscss}'
	 data-option='${_pageOption}'>
<form class="bc-from" method="post">
<!-- 基本信息区 -->
<div class="blockContainer formTable2 ui-widget-content" style="width:auto">
	<div class="header ui-widget-header title" style="position:relative;">
		<span class="text">基本信息:</span>
		<ul class="inputIcons">
			<li class="toggleAll inputIcon verticalMiddle ui-icon ui-icon-carat-2-n-s" title="折叠|展开所有区域"></li>
			<li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
		</ul>
	</div>
	<div class="detail">
		<table class="formFields ui-widget-content" cellspacing="2" cellpadding="0">
			<tr class="widthMarker">
				<td style="width: 6em;">&nbsp;</td>
				<td style="width: 25em;">&nbsp;</td>
				<td style="width: 6em;">&nbsp;</td>
				<td >&nbsp;</td>
			</tr>
			<tr>
				<td class="label">*标题:</td>
				<td class="value">
					<input type="text" name="subject" value="${f.subject!}" data-scope="form"
						cssClass="ui-widget-content" data-validate="required" readonly/>
				</td>
				<td class="label">*版本号:</td>
				<td class="value">
					<input type="text" name="version" value="${f.version!}" data-scope="form"
						cssClass="ui-widget-content" data-validate="required" readonly/>
				</td>
			</tr>
			<tr>
				<td class="label">备注:</td>
				<td class="value" colspan="3">
					<input type="text" name="description" value="${f.description!}" data-scope="form" cssClass="ui-widget-content"/>
				</td>
			</tr>
		</table>
		<div class="fileInfo">
			登记：${f.author.name}(${f.fileDate.time?string("yyyy-MM-dd HH:mm")}),
			最后修改：${f.modifier.name}(${f.modifiedDate.time?string("yyyy-MM-dd HH:mm")})
		</div>
	</div>
</div>

<!-- 图片区：主图 -->
<div class="blockContainer formTable2 ui-widget-content" style="width:auto">
	<div class="header ui-widget-header title" style="position:relative;">
		<span class="text">证件图片:</span>
		<span >实物宽度<input type="text" name="attach_width" data-label="主图实物宽度"
			value='<#if _replace || !(ff.attach_width??)>${certCfg.width?c}<#else>${ff.attach_width?c}</#if>'
			data-validate='{"required":true,"type":"number"}'
			class="ui-widget-content attach-width" style="width:50px;"/>毫米</span>
		<ul class="inputIcons">
			<#if certCfg.pageCount gt 1>
			<li class="mergeImage inputIcon ui-icon ui-icon-link" data-mixConfig='${certCfg.combine!}' title='合并各分拆页的图片'></li>
			</#if>
			<li class="editImage inputIcon ui-icon ui-icon-pencil" title='编辑'></li>
			<li class="printImage inputIcon ui-icon ui-icon-print" title='打印'></li>
			<li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
		</ul>
	</div>
	<div class="detail">
		<#if ff.attach_id?? && ff.attach_id != "">
			<img src="${_path}/bc/image/download?id=${ff.attach_id}&ts=${_ts}" style="max-width:100%;" />
		<#else>
			<div class="warn" style="margin:2px;">未上传</div>
		</#if>
	</div>
	<input type="hidden" name="attach_id" data-label="主图附件ID" value="${ff.attach_id!}"/>
</div>

<#if certCfg.pageCount gt 1>
<!-- 图片区：拆分图 -->
<#assign _ds=certCfg.details/>
<#list 1..certCfg.pageCount as n>
<#list certCfg.details as i><#if i_index == n><#assign _d=i/><#break></#if></#list>
<div class="blockContainer formTable2 ui-widget-content" style="width:auto" data-num="${n}">
	<div class="header ui-widget-header title" style="position:relative;">
		<span class="text">第${n}页(${_d.name})图片:</span>
		<span >实物宽度<input type="text" name="attach_width_${n}" data-label="实物宽度"
			value="<#if _replace || !(.vars['attach_width_' + n]??)>${_d.width?c}<#else>${.vars['attach_width_' + n]}</#if>"
			data-validate='{"required":true,"type":"number"}'
			class="ui-widget-content attach-width" style="width:50px;"/>毫米</span>
		<ul class="inputIcons">
			<li class="editImage inputIcon ui-icon ui-icon-pencil" title='编辑'></li>
			<li class="printImage inputIcon ui-icon ui-icon-print" title='打印'></li>
			<li class="toggleMe inputIcon verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
		</ul>
	</div>
	<div class="detail">
		<#if .vars['attach_id_' + n]?? && .vars['attach_id_' + n] != "">
			<img src="${_path}/bc/image/download?id=${.vars['attach_id_' + n]!}&ts=${_ts}" style="max-width:100%;" />
		<#else>
			<div class="warn" style="margin:2px;">未上传</div>
		</#if>
	</div>
	<input type="hidden" name="attach_id_${n}" data-label="拆分图附件ID" value="${.vars['attach_id_'+n]!}" class="attach-id"/>
</div>
</#list>
</#if>

<!-- 证件标识信息 -->
<input type="hidden" name="type" class="ignore" value="${f.type}"/>
<input type="hidden" name="code" class="ignore" value="${f.code}"/>
<input type="hidden" name="pid" class="ignore" value="${f.pid?c}"/>
<input type="hidden" name="version" class="ignore" value="${f.version}"/>

<#-- 附件修改记录标记 -->
<input type="hidden" name="attach_flag" value="${attach_flag!}" data-type="int"/>
</form>
</div>