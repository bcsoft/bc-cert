<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div title='<s:text name="certForm.title"/>' data-type='form' class="bc-page"
  data-saveUrl='<s:url value="/modules/bc/cert/certInfo/save" />'
  data-js='js:bc_identity,<s:url value="/modules/bc/cert/certInfo/form.js" />'
  data-initMethod='bc.certInfoForm.init'
  data-option='<s:property value="formPageOption"/>' style="overflow-y:auto;">
  <s:form name="certInfoForm" theme="simple" >
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
          <td class="label">*<s:text name="certInfo.subject"/>:</td>
          <td class="value">
            <s:textfield name="e.subject" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certInfo.version1"/>:</td>
          <td class="value">
            <s:textfield name="e.version" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certInfo.pid"/>:</td>
          <td class="value">
            <s:textfield name="e.pid" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certInfo.type.code"/>:</td>
          <td class="value">
            <s:textfield name="e.type" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certInfo.uid"/>:</td>
          <td class="value">
            <s:textfield name="e.uid" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certInfo.cert.code"/>:</td>
          <td class="value">
            <s:textfield name="e.code" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certInfo.status"/>:</td>
          <td class="value">
            <s:textfield name="e.status" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certInfo.tpl"/>:</td>
          <td class="value">
            <s:textfield name="e.tpl" cssStyle="width:200px;" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
        </tr>
        <tr>
          <td class="label"><s:text name="certInfo.desc"/>:</td>
          <td class="value" colspan="3">
            <s:textfield name="e.desc" cssStyle="width:520px;" cssClass="ui-widget-content" readonly="false" />
          </td>
        </tr>
        <tr>
          <td colspan="4">
            <div class="formTopInfo">
              登记：<s:property value="e.author.name" />(<s:date name="e.fileDate" format="yyyy-MM-dd HH:mm:ss"/>)
              <s:if test="%{e.modifier != null}">
              最后修改：<s:property value="e.modifier.name" />(<s:date name="e.modifiedDate" format="yyyy-MM-dd HH:mm:ss"/>)
              </s:if>
            </div>
          </td>
        </tr>
      </table>
    </div>

    <s:hidden name="e.id" />
    <s:hidden name="e.author.id" />
    <input type="hidden" name="e.fileDate" value='<s:date format="yyyy-MM-dd HH:mm:ss" name="e.fileDate" />'/>
  </s:form>
</div>