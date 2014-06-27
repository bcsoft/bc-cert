package cn.bc.cert.web.struts2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.cert.domain.CertType;
import cn.bc.cert.service.CertCfgTypeService;
import cn.bc.core.service.CrudService;

import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.struts2.FileEntityAction;
import cn.bc.web.ui.html.page.PageOption;

/**
 * 证件类型表单action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertTypeAction extends FileEntityAction<Long, CertType> implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(getClass());
	
	private CertCfgTypeService certCfgTypeService;
	
	@Autowired
	public void setCertCfgTypeService(CertCfgTypeService certCfgTypeService) {
		this.certCfgTypeService = certCfgTypeService;
	}

	@Autowired
	public void setCertTypeService(
			@Qualifier(value = "certTypeService") CrudService<CertType> crudService) {
		this.setCrudService(crudService);
	}


	@Override
	public boolean isReadonly() {
		
		
		SystemContext context = (SystemContext) this.getContext();
		
		boolean flag = context.hasAnyRole(
				getText("key.role.bc.cert.manage"),getText("key.role.bc.admin"));
		return !flag;
		
	}
	
	@Override
	protected PageOption buildFormPageOption(boolean editable) {
		return super.buildFormPageOption(editable).setWidth(510)
				.setMinHeight(200).setMinWidth(150);
	}
	
	@Override
    protected boolean useFormPrint() {
        return false;
    }

	@Override
	public String save() throws Exception {	
		CertType e = this.getE();
		boolean isUnique = true;
		Long id = e.isNew() ? null : e.getId();

		// 验证进货单唯一性
		isUnique = this.certCfgTypeService.isUnique(id, e.getCode());
		if (!isUnique) {
			String msg = "系统已存在相同的" + getText("certInfo.type") + "，不能够保存！";
			JSONObject json = new JSONObject();
			json.put("success", false);
			json.put("msg", msg);
			this.json = json.toString();

		} else {
			this.beforeSave(this.getE());
			this.getCrudService().save(e);
			this.afterSave(this.getE());

			JSONObject json = new JSONObject();
			String msg = "证件类别保存成功！";
			json.put("success", true);
			json.put("id", e.getId());
			json.put("msg", msg);
			this.json = json.toString();
		}

		return "json";
	}

	
	

}
