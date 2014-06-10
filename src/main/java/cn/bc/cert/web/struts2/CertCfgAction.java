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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.cert.service.CertCfgService;
import cn.bc.cert.service.CertCfgTypeService;
import cn.bc.core.service.CrudService;

import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.struts2.FileEntityAction;
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
	//private CertCfgService certCfgService;
	
	public String details; //证件配置明细json字符串
	
	@Autowired
	public void setCertCfgTypeService(CertCfgTypeService certCfgTypeService){
		this.certCfgTypeService = certCfgTypeService;
	}
	
	/*@Autowired
	public void setCertCfgService(CertCfgService certCfgService){
		this.certCfgService = certCfgService;
	}*/


	@Autowired
	public void setCertCfgService(
			@Qualifier(value = "certCfgService") CrudService<CertCfg> crudService) {
		this.setCrudService(crudService);
	}


	@Override
	public boolean isReadonly() {
		
		
		SystemContext context = (SystemContext) this.getContext();
		
		boolean flag = context.hasAnyRole(
				getText("key.role.bc.cert.manage"));
		return !flag;
		
	}
	
	@Override
	protected void initForm(boolean editable) throws Exception {
		//初始化销售对象类型下拉列表
		this.certTypes = getCertTypes(); 
		super.initForm(editable);
	}
	
	@Override
	protected PageOption buildFormPageOption(boolean editable) {
		return super.buildFormPageOption(editable).setWidth(680)
				.setMinHeight(200).setMinWidth(280);
	}
	
	@Override
	protected void buildFormPageButtons(PageOption option, boolean editable) {
		
		// 保存按钮
		ButtonOption saveButtonOption = new ButtonOption(
				getText("certCfg.save"), null, "bc.certCfgForm.save");
		option.addButton(saveButtonOption);
		
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
				//certCfgDetail.setStatus(PartOut.STATUS_WAIT_FOR_OUT); // 由于hibernate级联更新时，先update主控方，
				// 再update被动方，所以这里统一设置进货单状态为待出库，否则当进行出库操作时，触发器会出错
				long pid = (long)25;
				;
				//certCfgService.loadById(pid);
				certCfgDetail.setCertCfg(this.getCrudService().load(pid));
				

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
	

}
