package cn.bc.cert.web.struts2;

import cn.bc.BCConstants;
import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.cert.service.CertCfgService;
import cn.bc.cert.service.CertTypeService;
import cn.bc.identity.service.IdGeneratorService;
import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.struts2.FileEntityAction;
import cn.bc.option.service.OptionService;
import cn.bc.web.ui.html.page.ButtonOption;
import cn.bc.web.ui.html.page.PageOption;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;

/**
 * 证件配置表单action
 *
 * @author LeeDane
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertCfgAction extends FileEntityAction<Long, CertCfg> implements SessionAware {
  private static final long serialVersionUID = 1L;
  private static Logger logger = LoggerFactory.getLogger(CertCfgAction.class);

  @Autowired
  private CertTypeService certTypeService;
  @Autowired
  private IdGeneratorService idGeneratorService;
  @Autowired
  private OptionService optionService;

  public Map<String, String> certTypes = null;//证件类别列表
  public String details; //证件配置明细json字符串
  public Map<String, String> statusList = null; //状态列表
  public Map<String, String> onePageOneTypographyList = null; //一页一版列表
  public Map<String, String> printDirectionsList = null; // 打印方向列表
  public String codes;

  private CertCfgService certCfgService;

  @Autowired
  public void setCertCfgService(CertCfgService certCfgService) {
    this.setCrudService(certCfgService);
    this.certCfgService = certCfgService;
  }

  @Override
  public boolean isReadonly() {
    SystemContext context = (SystemContext) this.getContext();
    return !context.hasAnyRole(getText("key.role.bc.cert.manage"), getText("key.role.bc.admin"));
  }

  @Override
  protected void initForm(boolean editable) throws Exception {
    //初始化销售对象类型下拉列表
    this.certTypes = getCertTypes();
    this.statusList = getStatusList();
    this.onePageOneTypographyList = getOnePageOneTypographyList();
    this.printDirectionsList = getPrintDirectionsList();

    super.initForm(editable);
  }

  public String typeCode;
  public String cfgCode;

  /**
   * 根据配置获得配置信息的数据
   */
  public String getData() throws JSONException {
    CertCfg certCfg = this.certCfgService.loadByCode(typeCode, cfgCode);
    JSONObject json = new JSONObject();
    if (certCfg.getDetails() != null) {
      Set<CertCfgDetail> details = certCfg.getDetails();

      json.put("detail", dealDetail(details));
    }

    json.put("success", true);
    json.put("combine", certCfg.getCombine());
    json.put("attach_width", certCfg.getWidth());
    json.put("page_num", certCfg.getPageCount());
    json.put("uid", certCfg.getUid());
    this.json = json.toString();
    return "json";
  }

  public String getWidths() {
    JSONObject json = new JSONObject();
    json.put("widths", this.certCfgService.findCertWidthByCfgCode(this.cfgCode));
    this.json = json.toString();
    return "json";
  }

  public JSONArray dealDetail(Set<CertCfgDetail> details) throws JSONException {
    JSONArray jsons = new JSONArray();
    JSONObject object;
    Iterator<CertCfgDetail> it = details.iterator();
    while (it.hasNext()) {
      CertCfgDetail cd = it.next();
      object = new JSONObject();
      object.put("page_no", cd.getPageNo());
      object.put("width", cd.getWidth());
      object.put("attach_name", cd.getName());
      jsons.put(object);
    }
    return jsons;
  }

  @Override
  protected PageOption buildFormPageOption(boolean editable) {
    return super.buildFormPageOption(editable).setWidth(650)
        .setMinHeight(200).setMinWidth(600);
  }

  @Override
  protected void buildFormPageButtons(PageOption option, boolean editable) {
    if (editable && !isReadonly()) {

      // 保存按钮
      ButtonOption saveButtonOption = new ButtonOption(
          getText("certCfg.save"), null, "bc.certCfgForm.save");

      // 预览按钮
      ButtonOption previewButtonOption = new ButtonOption(
          getText("certCfg.preview"), null, "bc.certCfgForm.preview");

      option.addButton(previewButtonOption);
      option.addButton(saveButtonOption);
    }
  }

  @Override
  protected void afterCreate(CertCfg entity) {
    SystemContext context = (SystemContext) this.getContext();
    CertCfg e = entity;
    e.setTpl(this.optionService.getItemValue("carman.cert", "cert.Cfg.defaultTplCode")); // 设置默认模板
    e.setPageCount(1);
    e.setFileDate(Calendar.getInstance()); // 设置创建人
    e.setAuthor(context.getUserHistory()); // 设置创建时间
    e.setUid(this.idGeneratorService.next("cert.cfg"));
  }

  @Override
  protected void beforeSave(CertCfg entity) {
    SystemContext context = (SystemContext) this.getContext();
    entity.setModifier(context.getUserHistory()); // 设置修改人
    entity.setModifiedDate(Calendar.getInstance()); // 设置修改时间
    try {
      // 插入明细条目
      this.addDetails();
    } catch (JSONException e) {
      logger.error(e.getMessage(), e);
      try {
        throw e;
      } catch (JSONException e1) {
        e1.printStackTrace();
      }
    }
  }

  @Override
  public String save() throws Exception {
    CertCfg e = this.getE();

    this.beforeSave(this.getE());
    try {
      this.certCfgService.save(e);
      this.afterSave(this.getE());
      JSONObject json = new JSONObject();
      String msg = "保存成功！";
      json.put("success", true);
      json.put("id", e.getId());
      json.put("msg", msg);
      this.json = json.toString();
    } catch (Exception e2) {
      String ms = "{\"msg\":\"保存失败,请确认编码" + this.getE().getCode() + "是否已经存在！\",\"success\":true}";
      this.json = ms;//对出现的异常进行友好地提示给用户，如：编码已经存在
    }

    return "json";
  }

  private void addDetails() throws JSONException {
    Set<CertCfgDetail> certCfgDetails = null;
    if (this.details != null && this.details.length() > 0) {
      certCfgDetails = new LinkedHashSet<CertCfgDetail>();
      JSONArray jsons = new JSONArray(this.details);
      JSONObject json;
      CertCfgDetail certCfgDetail;

      for (int i = 0; i < jsons.length(); i++) {
        json = jsons.getJSONObject(i);
        certCfgDetail = new CertCfgDetail();
        if (json.has("id"))
          certCfgDetail.setId(json.getLong("id"));
        certCfgDetail.setName(json.getString("name"));
        certCfgDetail.setPageNo(json.getInt("pageNo"));
        certCfgDetail.setWidth(new BigDecimal(json.getString("width")));
        certCfgDetail.setCertCfg(this.getE());
        certCfgDetails.add(certCfgDetail);
      }
    }
    if (this.getE().getDetails() != null) {
      this.getE().getDetails().clear();
      this.getE().getDetails().addAll(certCfgDetails);
    } else {
      this.getE().setDetails(certCfgDetails);
    }
  }

  @Override
  protected boolean useFormPrint() {
    return false;
  }

  // 获取证件类别对象类型下拉列表
  private Map<String, String> getCertTypes() {
    Map<String, String> map = new LinkedHashMap<String, String>();

    List<Map<String, String>> listTypes = certTypeService.findCertTypes();

    for (Map<String, String> listType : listTypes) {
      map.put(listType.get("key"), listType.get("value"));
    }
    return map;
  }

  //状态的下拉列表
  private Map<String, String> getStatusList() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put(String.valueOf(BCConstants.STATUS_ENABLED), getText("certCfg.status.enabled"));
    map.put(String.valueOf(BCConstants.STATUS_DISABLED), getText("certCfg.status.disabled"));
    return map;
  }

  //一页一版的列表
  private Map<String, String> getOnePageOneTypographyList() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("0", "是");
    map.put("1", "否");
    return map;
  }

  //打印方向的列表
  private Map<String, String> getPrintDirectionsList() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("0", "纵向");
    map.put("1", "横向");
    return map;
  }
}