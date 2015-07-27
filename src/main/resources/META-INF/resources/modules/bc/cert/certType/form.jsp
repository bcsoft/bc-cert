<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div title='<s:text name="certForm.title"/>' data-type='form' class="bc-page"
	data-saveUrl='<s:url value="/modules/bc/cert/certType/save" />'
	data-option='<s:property value="formPageOption"/>' style="overflow-y:auto;width: 100px;">
	<s:form name="certTypeForm" theme="simple" >
		<table  cellspacing="2" cellpadding="0" style="width:100%;"  >
			<tbody>
				<tr class="widthMarker">
					<td style="width: 5.7em;"></td>
					<td >&nbsp;</td>
					<td >&nbsp;</td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="label">*<s:text name="certType.code"/>:</td>
					<td class="value"><s:textfield name="e.code" cssClass="ui-widget-content" data-validate="required" /></td>
				</tr>	
				<tr>
					<td class="label">*<s:text name="certType.name"/>:</td>
					<td class="value"><s:textfield name="e.name" cssClass="ui-widget-content" data-validate="required" /></td>
				</tr>				
				<tr>
					<td class="label"><s:text name="certType.order"/>:</td>
					<td class="value"><s:textfield name="e.order_no" cssClass="ui-widget-content" /></td>
				</tr>
				<tr>
					<td class="label" colspan="4">
						<div class="formTopInfo">
							登记：<s:property value="e.author.name" />(<s:date name="e.fileDate" format="yyyy-MM-dd HH:mm:ss"/>)<br>
							<s:if test="%{e.modifier != null}">
							最后修改：<s:property value="e.modifier.name" />(<s:date name="e.modifiedDate" format="yyyy-MM-dd HH:mm:ss"/>)
							</s:if>
						</div>
					</td>
				</tr>		
			</tbody>
		</table>
		<s:hidden name="e.id" />
		<s:hidden name="e.author.id" />
		<input type="hidden" name="e.fileDate" value='<s:date format="yyyy-MM-dd HH:mm:ss" name="e.fileDate" />'/>
	</s:form>
</div>