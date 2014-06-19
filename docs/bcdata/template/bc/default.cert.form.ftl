<div  data-type='form' class="bc-page" data-option='{"width":690,"height":400}' style="overflow-y:auto;"
	data-js='${htmlPageNamespace}/bc-business/carManCert/form.js?ts=${appTs}'
	data-initMethod='bs.carManCertForm.init'>
<form class="bc-from" method="post" data-form-info='${form_info!}'>
<div  class="formTable2 ui-widget-content bs-carManCert-containers" style="width:640px;" >
	<div class="ui-widget-header title" style="position:relative;">
		<span class="text" >${name!证件}信息:</span>
		<span style="font-weight:normal;" >${form_author}&nbsp; 于 &nbsp;${form_fileDate}&nbsp;创建<#if !form_isNew>，${form_modifier}&nbsp;于&nbsp;${form_modifiedDate}&nbsp;最后修改</#if></span>
	</div>
	
</div>
<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
	<div class="ui-widget-header title" style="position:relative;">
		<span class="text" >${name!证件}图片:</span>
		<span >实物宽度<input type="text" name="attach_width" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${totle_width!171.2}" data-label="${name!证件}宽度"/>毫米</span>
		<ul class="inputIcons">
			<li class="bs-cert-print inputIcon ui-icon ui-icon-print" title='打印'></li>
			<#if (datails?size>1)>
			<li class="bs-cert-merge inputIcon ui-icon ui-icon-disk" data-mixConfig='${mixConfig}' title='正背图片合并'></li>
			</#if>
			<li class="bs-cert-edit inputIcon ui-icon ui-icon-pencil" title='编辑图片'></li>
			<li class="bs-carManCert-showGroups inputIcon ui-icon ui-icon-carat-1-s" style="display:none;" title='展开'></li>
			<li class="bs-carManCert-hiddenGroups inputIcon ui-icon ui-icon-carat-1-n" title='隐藏'></li>
		</ul>
	</div>
	<div class="bs-carManCert-Groups" style="border-width:1px 1px 0 0;margin-bottom:8px;">
		<#if attach_id??&&attach_id!=""><img src="${htmlPageNamespace}/bc/image/download?id=${attach_id}&ts=${appTs}" style="width:100%;" /></#if>
	</div>
	<input type="hidden" class="bs-cert-attach-id bs-cert-added" name="attach_id" value="${attach_id!}" data-label="${name!证件}"/>
</div>
<#assign x=0>
<#assign attach=''>

<#list datails as map>
	<#assign x=x+1>
		<div  class="formTable2 ui-widget-content bs-carManCert-containers"  style="width:640px;">
			<div class="ui-widget-header title" style="position:relative;">
				<span class="text" >${map['name']}图片:</span>
				<span >实物宽度<input type="text" name="attach_width_${x}" style="width:50px;" class="bs-cert-attach-width" data-validate='{"required":true,"type":"number"}' value="${map['width']}" data-label="${map['name']!}宽度"/>毫米</span>
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
			<input type="hidden" class="bs-cert-attach-id bs-cert-add" name="attach_id_${x}" value="${.vars['attach_id_'+x]!}" data-label="${name!}${map['name']}"/>
		</div>
</#list>
	<input type="hidden" name="man_puid" class="ignore" value="${man_puid!}"/>
	<input type="hidden" name="man_ptype" class="ignore" value="${man_ptype!}"/>
	<#-- 附件修改记录标记 -->
	<input type="hidden" name="attach_flag" value="${attach_flag!}" data-type="int"/>
</form>
</div>