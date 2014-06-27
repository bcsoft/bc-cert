<div  data-type='form' class="bc-page" data-option='{"width":670,"height":400}' style="overflow-y:auto;"
	data-js='${htmlPageNamespace}/bc-business/carManCert/form.js?ts=${appTs}'
	data-initMethod='bs.carManCertForm.init'>
<form class="bc-from" method="post" data-form-info='${form_info!}'>
<#assign x=0>
<#assign attach=''>
<!-- 新建和编辑的时候-->
<#if operate_type!='preview'>
	<div class="ui-widget-header title" style="position:relative;border-width:0!important;">
		<span class="text" >基本信息:</span>
	</div>
	<div class="formFields ui-widget-content" id="formEditorDiv" >
		<table class="formFields ui-widget-content" cellspacing="2" cellpadding="0">
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
					<input type="text" name="subject" value="${form.subject!}" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
				<td class="label">*版本号:</td>
				<td class="value">
					<input type="text" name="version" value="${form.version!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content" data-validate="required"/>
				</td>
			</tr>
			<tr>
				<td class="label">*PID:</td>
				<td class="value">
					<input type="text" name="pid" value="${form.pid!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>
				<td class="label">*类别编码:</td>
				<td class="value">
					<input type="text" name="type" value="${form.type!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
			</tr>
			<tr>
				<td class="label">*UID:</td>
				<td class="value">
					<input type="text" name="uid" value="${form.uid!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
				<td class="label">*证件编码:</td>
				<td class="value">
					<input type="text" name="code" value="${form.code!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
			</tr>
			<tr>
				<td class="label">*状态:</td>
				<td class="value">
					<input type="text" name="status" value="${form.status!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
				<td class="label">*所用模板:</td>
				<td class="value">
					<input type="text" name="tpl" value="${form.tpl!}" cssStyle="width:200px;" data-scope="form" cssClass="ui-widget-content" data-validate="required"/>
				</td>	
			</tr>
			<tr>
				<td class="label">备注:</td>
				<td class="value" colspan="3">
					<input type="text" name="description" value="${form.description!}" data-scope="form" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
				</td>	
			</tr>
			<tr>
				<td colspan="4">
					<div class="formTopInfo">登记：${form_author}(${form_fileDate})<#if !form_isNew>,最后修改：${form_modifier}(${form_modifiedDate})</#if></div>
				</td>
			</tr>
		</table>
	</div>

	<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
	<div class="ui-widget-header title" style="position:relative;">
		<span class="text" >${subject!}图片:</span>
		<span >实物宽度<input type="text" name="attach_width" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${totle_width!171.2}" data-label="${subject!}宽度"/>毫米</span>
		<ul class="inputIcons">
			<li class="bs-cert-print inputIcon ui-icon ui-icon-print" title='打印'></li>
			<li class="bs-cert-merge inputIcon ui-icon ui-icon-disk" data-mixConfig='${combine!}' title='正背图片合并'></li>
			<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
			<li class="bs-carManCert-showGroups inputIcon ui-icon ui-icon-carat-1-s" style="display:none;" title='展开'></li>
			<li class="bs-carManCert-hiddenGroups inputIcon ui-icon ui-icon-carat-1-n" title='隐藏'></li>
		</ul>
	</div>
	<div class="bs-carManCert-Groups" style="border-width:1px 1px 0 0;margin-bottom:8px;">
		<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${attach_id}&ts=${appTs}" style="width:100%;" /></#if>
	</div>
		<input type="hidden" class="bs-cert-attach-id bs-cert-added" name="attach_id" value="${attach_id!}" data-label="${subject!}"/>
	</div>
	<#if page_num !=1>
		<#list 0..page_num-1 as num>
			<#assign x=x+1>
				<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
					<div class="ui-widget-header title" style="position:relative;">
						<span class="text" >图片:</span>
						<span >实物宽度<input type="text" name="attach_width_${x}" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${.vars['attach_width_'+x]!171.2}" data-label="宽度"/>毫米</span>
						<ul class="inputIcons">
							<li class="bs-cert-print inputIcon ui-icon ui-icon-print" data-print-width="120mm" title='打印'></li>
							<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
							<li class="bs-carManCert-showGroups inputIcon ui-icon ui-icon-carat-1-s" style="display:none;" title='展开'></li>
							<li class="bs-carManCert-hiddenGroups inputIcon ui-icon ui-icon-carat-1-n" title='隐藏'></li>
						</ul>
					</div>
					<div class="bs-carManCert-Groups" style="border-width:1px 1px 0 0;margin-bottom:8px;">
						<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${.vars['attach_id_'+x]!}&ts=${appTs}" style="width:100%;" /></#if>
					</div>
					<input type="hidden" class="bs-cert-attach-id bs-cert-add" name="attach_id_${x}" value="${.vars['attach_id_'+x]!}" data-label="${subject!}"/>
				</div>
		</#list>
	</#if>
<#else>
	<!-- 预览的时候-->
	<div  class="formTable2 ui-widget-content bs-carManCert-containers" style="width:640px;" >
		<div class="ui-widget-header title" style="position:relative;">
			<span class="text" >${subject!}信息:</span>
			<span style="font-weight:normal;" ></span>
		</div>
	</div>
	<div class="formFields ui-widget-content" id="formEditorDiv" style="width:640px;" >
		<table class="formFields ui-widget-content" cellspacing="2" cellpadding="0">
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
					<input type="text" name="subject" value="${form.subject!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
				</td>	
				<td class="label">*版本号:</td>
				<td class="value">
					<input type="text" name="version" value="${form.version!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
				</td>									
			</tr>	
			<tr>
				<td class="label">*备注:</td>
				<td class="value" colspan="3">
					<input type="text" name="description" value="${form.description!}" data-scope="form" cssStyle="width:200px;" cssClass="ui-widget-content"/>
				</td>	
			</tr>
			<tr>
				<td colspan="4">
					<div class="formTopInfo">登记：${form_author}(${form_fileDate})<#if !form_isNew>,最后修改：${form_modifier}(${form_modifiedDate})</#if></div>
				</td>
			</tr>
		
		</table>
	</div>

	<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
	<div class="ui-widget-header title" style="position:relative;">
		<span class="text" >${subject!}图片:</span>
		<span >实物宽度<input type="text" name="attach_width" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${totle_width!171.2}" data-label="${subject!}宽度"/>毫米</span>
		<ul class="inputIcons">
			<li class="bs-cert-print inputIcon ui-icon ui-icon-print" title='打印'></li>
			<li class="bs-cert-merge inputIcon ui-icon ui-icon-disk" data-mixConfig='${combine!}' title='正背图片合并'></li>
			<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
			<li class="bs-carManCert-showGroups inputIcon ui-icon ui-icon-carat-1-s" style="display:none;" title='展开'></li>
			<li class="bs-carManCert-hiddenGroups inputIcon ui-icon ui-icon-carat-1-n" title='隐藏'></li>
		</ul>
	</div>
	<div class="bs-carManCert-Groups" style="border-width:1px 1px 0 0;margin-bottom:8px;">
		<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${attach_id}&ts=${appTs}" style="width:100%;" /></#if>
	</div>
	<input type="hidden" class="bs-cert-attach-id bs-cert-added" name="attach_id" value="${attach_id!}" data-label="${subject!}"/>
	</div>

	<#if page_num gt 1>
		
		<#list 0..page_num-1 as num>
			<#assign x=x+1>
				<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
					<div class="ui-widget-header title" style="position:relative;">
						<span class="text" >图片:</span>
						<span >实物宽度<input type="text" name="attach_width_${x}" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${.vars['attach_width_'+x]!}" data-label="${.vars['attach_width_'+x]!}宽度"/>毫米</span>
						<ul class="inputIcons">
							<li class="bs-cert-print inputIcon ui-icon ui-icon-print" data-print-width="120mm" title='打印'></li>
							<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
							<li class="bs-carManCert-showGroups inputIcon ui-icon ui-icon-carat-1-s" style="display:none;" title='展开'></li>
							<li class="bs-carManCert-hiddenGroups inputIcon ui-icon ui-icon-carat-1-n" title='隐藏'></li>
						</ul>
					</div>
					<div class="bs-carManCert-Groups" style="border-width:1px 1px 0 0;margin-bottom:8px;">
						<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${.vars['attach_id_'+x]!}&ts=${appTs}" style="width:100%;" /></#if>
					</div>
					<input type="hidden" class="bs-cert-attach-id bs-cert-add" name="attach_id_${x}" value="${.vars['attach_id_'+x]!}" data-label="${subject!}"/>
				</div>
		</#list>
	</#if>
</#if>

	<input type="hidden" name="man_puid" class="ignore" value="${man_puid!}"/>
	<input type="hidden" name="man_ptype" class="ignore" value="${man_ptype!}"/>
	<#-- 附件修改记录标记 -->
	<input type="hidden" name="attach_flag" value="${attach_flag!}" data-type="int"/>
</form>
</div>