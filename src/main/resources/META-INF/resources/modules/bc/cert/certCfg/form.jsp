<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div title='<s:text name="certForm.title"/>' data-type='form' class="bc-page"
  data-saveUrl='<s:url value="/modules/bc/cert/certCfg/save" />'
  data-js='js:bc_identity,<s:url value="/bc-business/bs.js" />,<s:url value="/modules/bc/cert/api.js" />,<s:url value="/modules/bc/cert/certCfg/form.js" />,<s:url value="/bc-business/carManCert/view.js" />,<s:url value="/bc/form/api.js" />,<s:url value="/bc-business/carManCert/carManCert.js" />,<s:url value="/bc-business/carManCert/form.js" />'
  data-initMethod='bc.certCfgForm.init'
  data-option='<s:property value="formPageOption"/>' style="overflow-y:auto;">
  <s:form name="certCfgForm" theme="simple" >
    <div class="ui-widget-header title" style="position:relative;border-width:0!important;">
      <span class="text" >基本信息:</span>
    </div>
    <div class="formFields ui-widget-content" id="formEditorDiv" >
      <table class="formFields ui-widget-content" cellspacing="2" cellpadding="0">
        <tr class="widthMarker">
          <td style="width: 80px;">&nbsp;</td>
          <td style="width: 220px;">&nbsp;</td>
          <td style="width: 80px;">&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certCfg.type_name"/>:</td>
          <td class="value">
            <s:select name="e.certType.id" list="certTypes" data-validate="required" listKey="key"
                listValue="value"
                cssClass="ui-widget-content" headerKey="" headerValue="" />
          </td>
          <td class="label">*<s:text name="certCfg.combine"/>:</td>
          <td class="value">
            <s:textfield name="e.combine" cssClass="ui-widget-content" readonly="false"/>
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certCfg.name"/>:</td>
          <td class="value">
            <s:textfield name="e.name" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certCfg.width"/>:</td>
          <td class="value">
            <s:textfield name="e.width" cssStyle="width:60px;" cssClass="ui-widget-content" readonly="false"
            data-validate='{required:true,type:"number",min:0.01}' />毫米
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certCfg.code"/>:</td>
          <td class="value">
            <s:textfield name="e.code" cssClass="ui-widget-content" readonly="false"
            data-validate="required" />
          </td>
          <td class="label">*<s:text name="certCfg.print_direction"/>:</td>
          <td class="value">
            <s:radio name="e.print_direction" list="printDirectionsList" cssStyle="width:auto;">
            </s:radio>
          </td>
        </tr>
        <tr>
          <%-- <td colspan="2" style="text-align: center;">
            状态：<s:if test="%{e.status == 0}"><s:text name="certCfg.status.enabled"/></s:if>
              <s:elseif test="%{e.status == 1}"><s:text name="certCfg.status.disabled"/></s:elseif>，
          </td>   --%>
          <td class="label">*<s:text name="certCfg.tpl"/>:</td>
          <td class="value">
            <s:textfield name="e.tpl" cssClass="ui-widget-content" readonly="false" data-validate="required"/>
          </td>
          <td class="label">*<s:text name="certCfg.one_page_one_typography"/>:</td>
          <td class="value">
            <s:radio name="e.one_page_one_typography" list="onePageOneTypographyList" cssStyle="width:auto;">
            </s:radio>
          </td>
        </tr>
        <tr>
          <td class="label">*<s:text name="certCfg.pageCount"/>:</td>
          <td class="value">
            <s:textfield name="e.pageCount" cssClass="ui-widget-content" readonly="false"
            data-validate='{"min": 1,"required":true,"type":"digits"}' />
          </td>
          <td class="label">*<s:text name="certCfg.status"/>:</td>
          <td class="value">
            <s:radio name="e.status" list="statusList" cssStyle="width:auto;">
            </s:radio>
          </td>
        </tr>
        <tr>
          <td class="label"><s:text name="certCfg.order"/>:</td>
          <td class="value">
            <s:textfield name="e.orderNo" cssClass= "ui-widget-content" readonly="false"  />
          </td>
        </tr>

      </table>
    </div>
    <!-- 各页参数部分的配置  开始-->

    <div class="ui-widget-content item" data-item-name='各页参数' data-item-type='item' style="margin-bottom:2px;">
      <div class="ui-widget-header title" style="position:relative;">
        <span class="text" >各分拆页配置</span>
        <ul class="inputIcons">
          <!-- <li class="upItem inputIcon ui-icon ui-icon-circle-arrow-n"     title='上移选中条目'></li>
          <li class="downItem inputIcon ui-icon ui-icon-circle-arrow-s"     title='下移选中条目'></li> -->
          <li class="addItem inputIcon ui-icon ui-icon-circle-plus"      title='添加条目'></li>
          <li class="deleteItem inputIcon ui-icon ui-icon-circle-minus"    title='删除条目'></li>
          <li class="showItem inputIcon ui-icon ui-icon-carat-1-n toggle"    title='点击隐藏内容' data-show-status="1"></li>
        </ul>
      </div>
        <div class="bc-grid header">
        <table class="table" id="itemsTable" cellspacing="0" cellpadding="0" style="width:100%;">
          <tr class="ui-state-default">
            <!-- <td class="first" style="width: 1em;">&nbsp;</td> -->
            <td class="middle" style="width: 2.5em;text-align: center;">页码</td>
            <td class="middle" style="width: 10em;text-align: center;">标记</td>
            <td class="middle" style="text-align: center;">打印宽度(毫米)</td>
            <td class="last" style="width: 1px;">&nbsp;</td>
          </tr>
          <s:iterator var="certCfgDetail" value="e.details">
            <tr class="ui-widget-content row" data-id='<s:property value="id"/>' >
              <%-- <td class="id first" style="padding:0 0 0 2px;"><span class="ui-icon"></span></td> --%>
              <!-- 页码 -->
              <td class="middle" style="padding:0 0 0 2px;">
                <input type="text"
                     class="ui-widget-content bc-cert-certCfgDetailItem-page_no"
                     style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;text-align: center;"
                     value='<s:property value="pageNo"/>' readonly="true" data-validate="required" />
              </td>
              <!-- 标记-->
              <td class="middle" style="padding:0 0 0 2px;">
                <input type="text"
                       class="ui-widget-content bc-cert-certCfgDetailItem-name"
                       style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;text-align: center;"
                     value='<s:property value="name"/>' data-validate="required" />
                <input type="hidden" class="bc-cert-certCfgDetailItem-id" value='<s:property value="id"/>' />
              </td>
              <!-- 打印宽度(毫米)-->
              <td class="middle" style="padding:0 0 0 2px;">
                <input type="text"
                       class="ui-widget-content bc-cert-certCfgDetailItem-width"
                       style="width:100%;height:100%;padding:0;margin:0;border:none;background:none;"
                     value='<s:property value="width"/>'
                     data-validate='{required:true,type:"number",min:0.01}' />
              </td>
              <td  class="last" style="width:0.001em">

              </td>
            </tr>
          </s:iterator>
        </table>
      </div>
      <div class="formTopInfo">
              登记：<s:property value="e.author.name" />(<s:date name="e.fileDate" format="yyyy-MM-dd HH:mm:ss"/>)
              <s:if test="%{e.modifier != null}">
              最后修改：<s:property value="e.modifier.name" />(<s:date name="e.modifiedDate" format="yyyy-MM-dd HH:mm:ss"/>)
              </s:if>
      </div>
    </div>

    <s:hidden name="e.id" />
    <s:hidden name="e.uid" />
    <s:hidden name="e.author.id" />
    <s:hidden name="e.certType.code" />
    <input type="hidden" name="details"/>
    <input type="hidden" name="previewDatas"/>
    <input type="hidden" name="e.fileDate" value='<s:date format="yyyy-MM-dd HH:mm:ss" name="e.fileDate" />'/>
  </s:form>
</div>