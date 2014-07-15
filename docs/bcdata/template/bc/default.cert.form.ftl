<div data-type='form' class="bc-page" data-option='{"min-width":640,"width":670,"min-height":250,"height":400}'
	data-js='${htmlPageNamespace}/modules/bc/cert/default/form.js?ts=${appTs}'
	data-initMethod='bc.defaultCertForm.init' style="overflow-y:auto">
<form class="bc-from" method="post" data-form-info='${form_info!}'>
	<!-- 基本信息区 -->
	<div class="blockContainer formTable2 ui-widget-content" style="width:auto">
		<div class="header ui-widget-header title" style="position:relative;">
			<span class="text">基本信息:</span>
			<ul class="inputIcons">
				<li class="inputIcon toggleAll verticalMiddle ui-icon ui-icon-carat-2-n-s" title="折叠|展开所有区域"></li>
				<li class="inputIcon toggleMe verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
			</ul>
		</div>
		<#assign info=form_info?eval />
		<#if operate_type=='edit'><!-- 新建和编辑的时候-->
		<table class="detail formFields ui-widget-content" cellspacing="2" cellpadding="0">
			<tr class="widthMarker">
				<td style="width: 80px;">&nbsp;</td>
				<td style="width: 220px;">&nbsp;</td>
				<td style="width: 100px;">&nbsp;</td>
				<td style="width: 220px;">&nbsp;</td>
				<td >&nbsp;</td>
			</tr>			
			<tr>
				<td class="label">*标题:</td>
				<td class="value">
					<input type="text" name="subject" value="<#if form??>${form.subject!}<#else>${info.subject!}</#if>" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
				<td class="label">*版本号:</td>
				<td class="value">
					<input type="text" name="version" value="<#if form??>${form.version!}<#else>${info.version!}</#if>" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>
			</tr>
			<tr>
				<td class="label">*PID:</td>
				<td class="value">
					<input type="text" name="pid" value='${(info.pid)?c}' cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate='{"required":true,"type":"digits"}'/>
				</td>
				<td class="label">*类别编码:</td>
				<td class="value">
					<input type="text" name="type" value="<#if form??>${form.type!}<#else>${info.type!}</#if>" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
			</tr>
			<tr>
				<td class="label">*UID:</td>
				<td class="value">
					<input type="text" name="uid" value="<#if form??>${form.uid!}<#else>${info.uid!}</#if>" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
				<td class="label">*证件编码:</td>
				<td class="value">
					<input type="text" name="code" value='<#if form??>${form.code!}<#else>${info.code!}</#if>' cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
			</tr>
			<tr>
				<td class="label">*状态:</td>
				<td class="value">
					<input type="text" name="status" value='<#if form??>${form.status!}<#else>${info.status!}</#if>' cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate='{"required":true,"type":"digits"}'/>
				</td>	
				<td class="label">*所用模板:</td>
				<td class="value">
					<input type="text" name="tpl" value='<#if form??>${form.tpl!}<#else>${info.tpl!}</#if>' cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
					
				</td>	
			</tr>
			<tr>
				<td class="label">备注:</td>
				<td class="value" colspan="3">
					<input type="text" name="description" value="<#if form??>${form.description!}</#if>" data-scope="form" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
				</td>	
			</tr>
			<tr>
				<td colspan="4">
					<div class="formTopInfo">登记：${form_author}(${form_fileDate})<#if !form_isNew>,最后修改：${form_modifier}(${form_modifiedDate})</#if></div>
				</td>
			</tr>
		</table>
		<#elseif operate_type='preview'><!-- 预览的时候-->
		<table class="detail formFields ui-widget-content" cellspacing="2" cellpadding="0">
			<tr class="widthMarker">
				<td style="width: 80px;">&nbsp;</td>
				<td style="width: 220px;">&nbsp;</td>
				<td style="width: 100px;">&nbsp;</td>
				<td style="width: 220px;">&nbsp;</td>
				<td >&nbsp;</td>
			</tr>
			
			<#if form??><!--新建表单-->
				<tr>
					<td class="label">*标题:</td>
					<td class="value">
						<input type="text" name="subject" value="${form.subject!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
					</td>	
					<td class="label">*版本号:</td>
					<td class="value">
						<input type="text" name="version" value="${form.version!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>									
				</tr>	
				<tr>
					<td class="label">备注:</td>
					<td class="value" colspan="3">
						<input type="text" name="description" value="${form.description!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
					</td>	
				</tr>
			<#else>
				<tr>
					<td class="label">*标题:</td>
					<td class="value">
						<input type="text" name="subject" value="${info.subject!2}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
					</td>	
					<td class="label">*版本号:</td>
					<td class="value">
						<input type="text" name="version" value="${info.version!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>									
				</tr>	
				<tr>
					<td class="label">备注:</td>
					<td class="value" colspan="3">
						<input type="text" name="description" value="${info.description!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
					</td>	
				</tr>
			</#if>
			<tr>
				<td colspan="4">
					<div class="formTopInfo">登记：${form_author}(${form_fileDate})<#if !form_isNew>,最后修改：${form_modifier}(${form_modifiedDate})</#if></div>
				</td>
			</tr>
		</table>
		</#if>
	</div>

	<!-- 图片区：主图 -->
	<div class="blockContainer formTable2 ui-widget-content" style="width:auto">
		<div class="header ui-widget-header title" style="position:relative;">
			<span class="text" >证件图片:</span>
			<span >实物宽度<input type="text" name="attach_width" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="<#if total_width?exists>${total_width!171.0}<#else>${attach_width!171.1}</#if>" data-label="${subject!}宽度"/>毫米</span>
			<ul class="inputIcons">
				<li class="bs-cert-print inputIcon ui-icon ui-icon-print" title='打印'></li>
				<#if page_num gt 1>
					<li class="bs-cert-merge inputIcon ui-icon ui-icon-disk" data-mixConfig='${combine!}' title='正背图片合并'></li>
				</#if>
				<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
				<li class="inputIcon toggleMe verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
			</ul>
		</div>
		<div class="detail" style="border-width:1px 1px 0 0;margin-bottom:8px;">
			<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${attach_id}&ts=${appTs}" style="max-width:100%;" /></#if>
		</div>
		<input type="hidden" class="bs-cert-attach-id bs-cert-added" name="attach_id" value="${attach_id!}" data-label="${subject!}"/>
	</div>

	<#if page_num gt 1>
	<!-- 图片区：拆分图 -->
	<#assign x=0>
	<#list 0..page_num-1 as num>
	<#assign x=x+1>
	<div class="blockContainer formTable2 ui-widget-content" style="width:auto">
		<div class="header ui-widget-header title" style="position:relative;">
			<span class="text" >第${x}页(${.vars['attach_name_'+x]!})图片:</span>
			<span >实物宽度<input type="text" name="attach_width_${x}" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${.vars['attach_width_'+x]!}" data-label="${.vars['attach_width_'+x]!}宽度"/>毫米</span>
			<ul class="inputIcons">
				<li class="bs-cert-print inputIcon ui-icon ui-icon-print" data-print-width="120mm" title='打印'></li>
				<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
				<li class="inputIcon toggleMe verticalMiddle ui-icon ui-icon-triangle-1-n" title="折叠|展开此区域"></li>
			</ul>
		</div>
		<div class="detail" style="border-width:1px 1px 0 0;margin-bottom:8px;">
			<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${.vars['attach_id_'+x]!}&ts=${appTs}" style="max-width:100%;" /></#if>
		</div>
		<input type="hidden" class="bs-cert-attach-id bs-cert-add" name="attach_id_${x}" value="${.vars['attach_id_'+x]!}" data-label="${.vars['attach_name_'+x]!}"/>
	</div>
	</#list>
	</#if>

	<input type="hidden" name="man_puid" class="ignore" value="${man_puid!}"/>
	<input type="hidden" name="man_ptype" class="ignore" value="${man_ptype!}"/>
	<#-- 附件修改记录标记 -->
	<input type="hidden" name="attach_flag" value="${attach_flag!}" data-type="int"/>
</form>
</div>