package cn.bc.cert.web.struts2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.cert.domain.CertType;
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
	
	

	@Autowired
	public void setCertTypeService(
			@Qualifier(value = "certTypeService") CrudService<CertType> crudService) {
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
	protected PageOption buildFormPageOption(boolean editable) {
		return super.buildFormPageOption(editable).setWidth(510)
				.setMinHeight(200).setMinWidth(150);
	}
	
	@Override
    protected boolean useFormPrint() {
        return false;
    }
	
	

}
