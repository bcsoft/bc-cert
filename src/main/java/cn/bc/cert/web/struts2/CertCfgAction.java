package cn.bc.cert.web.struts2;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.cert.service.CertCfgService;
import cn.bc.cert.service.CertCfgTypeService;

import cn.bc.identity.service.IdGeneratorService;
import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.struts2.FileEntityAction;
import cn.bc.option.service.OptionService;
import cn.bc.web.ui.html.page.ButtonOption;
import cn.bc.web.ui.html.page.PageOption;

/**
 * 证件配置表单action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertCfgAction extends FileEntityAction<Long, CertCfg> implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(getClass());
	public Map<String,String> certTypes = null;//证件类别列表
	
	private CertCfgTypeService certCfgTypeService;
	private CertCfgService certCfgService;
	private IdGeneratorService idGeneratorService;	
	private OptionService optionService;
	
	public String details; //证件配置明细json字符串
	
	public Map<String,String> statusList = null; //状态列表
	
	@Autowired
	public void setCertCfgTypeService(CertCfgTypeService certCfgTypeService){
		this.certCfgTypeService = certCfgTypeService;
	}
	
	@Autowired
	public void setIdGeneratorService(IdGeneratorService idGeneratorService){
		this.idGeneratorService = idGeneratorService;
	}
	
	
	@Autowired
	public void setCertCfgService(CertCfgService certCfgService){
		this.setCrudService(certCfgService);
		this.certCfgService = certCfgService;
	}


	@Autowired
	public void setOptionService(OptionService optionService) {
		this.optionService = optionService;
	}

	@Override
	public boolean isReadonly() {
		
		
		SystemContext context = (SystemContext) this.getContext();
		
		boolean flag = context.hasAnyRole(
				getText("key.role.bc.cert.manage"),getText("key.role.bc.admin"));
		return !flag;
		
	}
	
	@Override
	protected void initForm(boolean editable) throws Exception {
		//初始化销售对象类型下拉列表
		this.certTypes = getCertTypes(); 
		this.statusList = getStatusList();
		
		super.initForm(editable);
	}
	

	@Override
	protected PageOption buildFormPageOption(boolean editable) {
		return super.buildFormPageOption(editable).setWidth(650)
				.setMinHeight(200).setMinWidth(600);
	}
	
	
	@Override
	protected void buildFormPageButtons(PageOption option, boolean editable) {
		
		if(editable && !isReadonly()){

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
		e.setPage_count(1);
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
		this.certCfgService.save(e);
		this.afterSave(this.getE());

		JSONObject json = new JSONObject();
		String msg = "保存成功！";
		json.put("success", true);
		json.put("id", e.getId());
		json.put("msg", msg);
		this.json = json.toString();
	

		return "json";
	}

	private void addDetails() throws JSONException{
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
				certCfgDetail.setPage_no(json.getInt("page_no"));
				certCfgDetail.setWidth(new BigDecimal(json.getString("width")));
				certCfgDetail.setCertCfg(this.getE());
				certCfgDetails.add(certCfgDetail);
			}
		}

		if (this.getE().getCertCfgDetails() != null) {
			this.getE().getCertCfgDetails().clear();
			this.getE().getCertCfgDetails().addAll(certCfgDetails);
		} else {
			this.getE().setCertCfgDetails(certCfgDetails);
		}
		
	}
	

	@Override
    protected boolean useFormPrint() {
        return false;
    }
	
	// 获取证件类别对象类型下拉列表
	private Map<String, String> getCertTypes() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		List<Map<String,String>> listTypes =certCfgTypeService.findCertTypes();
		
		for(Map<String , String> listType : listTypes){
			map.put(listType.get("key"), listType.get("value"));
		}	
		return map;
	}
	//状态的下拉列表
	private Map<String, String> getStatusList() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(String.valueOf(CertCfg.STATUS_ENABLED), getText("certCfg.status.enabled"));
		map.put(String.valueOf(CertCfg.STATUS_DISABLED), getText("certCfg.status.disabled"));
		return map;
	}

}
