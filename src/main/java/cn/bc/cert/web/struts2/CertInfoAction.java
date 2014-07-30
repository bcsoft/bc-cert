package cn.bc.cert.web.struts2;

import java.util.Calendar;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.cert.service.CertInfoService;
import cn.bc.form.domain.Form;

import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.struts2.FileEntityAction;
import cn.bc.web.ui.html.page.ButtonOption;
import cn.bc.web.ui.html.page.PageOption;

/**
 * 证件信息表单action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertInfoAction extends FileEntityAction<Long, Form> implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(getClass());

	private CertInfoService certInfoService;

	public String details; //证件配置明细json字符串
	
	public Map<String,String> statusList = null; //状态列表
	
	public Form e = null;
	
	@Autowired
	public void setCertInfoService(CertInfoService certInfoService){
		this.certInfoService = certInfoService;
		this.setCrudService(certInfoService);
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
		super.initForm(editable);
	}
	
	@Override
	protected PageOption buildFormPageOption(boolean editable) {
		return super.buildFormPageOption(editable).setWidth(650)
				.setMinHeight(200).setMinWidth(600);
	}
		
	@Override
	protected void buildFormPageButtons(PageOption option, boolean editable) {		
		// 保存按钮
		ButtonOption saveButtonOption = new ButtonOption(
				getText("certInfo.save"), null, "bc.certInfoForm.save");
		
		// 保存并关闭按钮
		ButtonOption previewButtonOption = new ButtonOption(
				getText("certInfo.saveAndClose"), null, "bc.certInfoForm.saveAndClose");
		
		option.addButton(previewButtonOption);
		option.addButton(saveButtonOption);
		
	}
	
	@Override
	protected void afterCreate(Form entity) {
		SystemContext context = (SystemContext) this.getContext();
		Form e = entity;	
		e.setFileDate(Calendar.getInstance()); // 设置创建人
		e.setAuthor(context.getUserHistory()); // 设置创建时间
	}

	@Override
	protected void beforeSave(Form entity) {
		SystemContext context = (SystemContext) this.getContext();
		entity.setModifier(context.getUserHistory()); // 设置修改人
		entity.setModifiedDate(Calendar.getInstance()); // 设置修改时间
	}
	
	@Override
	public String save() throws Exception {
		Form e = this.getE();
		
		this.beforeSave(this.getE());
		this.certInfoService.save(e);
		this.afterSave(this.getE());

		JSONObject json = new JSONObject();
		String msg = "保存成功！";
		json.put("success", true);
		json.put("id", e.getId());
		json.put("msg", msg);
		this.json = json.toString();

		return "json";
	}

	@Override
    protected boolean useFormPrint() {
        return false;
    }
}
