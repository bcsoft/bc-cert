<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div title='<s:text name="certForm.title"/>' data-type='form' class="bc-page"
	data-saveUrl='<s:url value="/modules/bc/cert/seeAllByParent/save" />'
	data-js='<s:url value="/modules/bc/cert/seeAllByParent/form.js" />,<s:url value="/modules/bc/cert/api.js" />'
	data-option='{"width":690,"height":400,"minWidth":250,"minHeight":200,
	"buttons":[{"text":"刷新","click":"bc.seeAllByParent.refresh"}]}' 
	data-initMethod='bc.seeAllByParent.init'
	style="overflow-y:auto;width: auto;">
	<s:form name="" theme="simple" >
		<div class="ui-widget-content" style="width:auto;height: 50px;border-bottom:none">
			<div class="ui-widget-header title" style="position:relative;">
				<span class="text" >汇总信息</span>
				<ul class="inputIcons">
					<li class="inputIcon bc-cert-showAllGroups ui-icon ui-icon ui-icon-carat-2-n-s" style="display:none;" title='展开全部证件图片'></li>
					<li class="inputIcon bc-cert-hiddenAllGroups ui-icon ui-icon ui-icon-carat-2-n-s" title='隐藏全部证件图片'></li>
				</ul>
			</div>
			<div id="showBaseUploadInfo" style="border-width:1px 1px 0 0;margin-bottom:none;">
				<s:if test="totalCerts == yetUpload"> 
					<s:property value="totalCerts"/>种证件均没上传
				</s:if>
				<s:elseif test="totalCerts == alreadlyUpload">
					<s:property value="totalCerts"/>种证件已全部上传
				</s:elseif>
				<s:else>
					已上传<s:property value="alreadlyUpload"/>种证件，尚欠<s:property value="yetUpload"/>种证件没有上传
				</s:else>
			</div>
		</div>
			<div class="bc-cert-containers" style="border-top:1px;">
			<s:iterator value="listInfo">	
				<s:if test="isUpload=='no'">
					<div class="cert-containers ui-widget-content" style="width:auto;border-bottom:none">
						<div class="header ui-widget-header title" style="position:relative;">
							<span class="text" >[未上传] <s:property value="name"/></span>
							<ul class="inputIcons">
								<li class="bc-cert-print inputIcon ui-icon ui-icon-print" title='打印此证件图片'></li>
								<li class="inputIcon bc-cert-showGroups ui-icon ui-icon-carat-1-s" style="display:none;" title='展开此证件图片'></li>
								<li class="inputIcon bc-cert-hiddenGroups ui-icon ui-icon-carat-1-n" title='隐藏此证件图片'></li>
							</ul>
						</div>
						<div style="border-width:1px 1px 0 0;margin-bottom:none;" class="bc-cert-Groups">
							<img src="${htmlPageNamespace}/bc/libs/themes/default/images/face/sad48.png"/>
						</div>	
					</div>
				</s:if>	
				<s:else>
					<div class="cert-containers ui-widget-content" style="width:auto;border-bottom:none">
						<div class="header ui-widget-header title" style="position:relative;">
							<span class="text" >[已上传] <s:property value="name"/> <s:if test="version!=''">v<s:property value="version"/></s:if></span>
							<input type="hidden" name="code" value="${code}"/>
							<input type="hidden" name="ver" value="${version}"/>
							<ul class="inputIcons">
								<li class="bc-cert-print inputIcon ui-icon ui-icon-print" title='打印此证件图片'></li>
								<li class="inputIcon bc-cert-showGroups ui-icon ui-icon-carat-1-s" style="display:none;" title='展开此证件图片'></li>
								<li class="inputIcon bc-cert-hiddenGroups ui-icon ui-icon-carat-1-n" title='隐藏此证件图片'></li>
							</ul>
						</div>
						<div style="border-width:1px 1px 0 0;margin-bottom:none;" class="bc-cert-Groups">
							<img src="${htmlPageNamespace}/bc/image/download?id=${attach_id}&ts=${appTs}" style="max-width:100%;" />
						</div>	
					</div>
				</s:else>
			</s:iterator>						
			</div>	
	</s:form>
	<input type="hidden" name="pid" value="${pid}"/>
	<input type="hidden" name="type" value="${type}"/>
</div>