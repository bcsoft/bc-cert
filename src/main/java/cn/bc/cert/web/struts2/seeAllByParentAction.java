package cn.bc.cert.web.struts2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import cn.bc.cert.service.CertCfgService;
import cn.bc.core.query.condition.Direction;
import cn.bc.core.query.condition.impl.OrderCondition;
import cn.bc.core.util.StringUtils;
import cn.bc.form.domain.Field;
import cn.bc.form.domain.Form;
import cn.bc.form.service.FieldService;
import cn.bc.form.service.FormService;
import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.SystemContextHolder;
import cn.bc.template.engine.TemplateEngine;
import cn.bc.template.service.TemplateService;

/**
 * 查看所有证件的表单action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class seeAllByParentAction extends ActionSupport implements
SessionAware, RequestAware {
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(getClass());
	
	protected Map<String, Object> session;
	protected Map<String, Object> request;
	
	public String pid;  //pid
	public String type; //业务类别，如司机证件、车辆证件
	
	
	public int totalCerts ;//总的证件数
	public int alreadlyUpload; //已经上传的证件数
	public int yetUpload;//未上传的证件数
	public List<Map<String, String>> listInfo = null; //所有的证件列表信息
	
	public String appTs;
	public String htmlPageNamespace;

	private CertCfgService certCfgService;

	@Autowired
	public void setCertCfgService(CertCfgService certCfgService){
		this.certCfgService = certCfgService;
	}

	/**
	 * 查看所有的证件信息
	 * @return
	 */
	public String seeAll(){
		Map<String,Object> args = new HashMap<String, Object>();
		getTotalCerts(args);  //根据证件类型获取全部的证件
		System.out.println(listInfo);
		return "form";
	}
	
	/**
	 * 得到总的证件配置里面的证件信息，根据证件类别，和pid查
	 */
	public void getTotalCerts(Map<String, Object> args) {  //args保存的是参数
		
		listInfo = certCfgService.find4AllCertsInfo(this.type, Long.parseLong(pid));
		totalCerts = listInfo.size();
		if(totalCerts > 0){
			for(int i = 0 ; i< totalCerts ; i++){
				if(listInfo.get(i).get("isUpload").toString().equals("no")){
					yetUpload++;
				}else{
					alreadlyUpload++;
				}
			}
		}
		SystemContext context = SystemContextHolder.get();
		htmlPageNamespace = context.getAttr(SystemContext.KEY_HTMLPAGENAMESPACE);
		appTs = context.getAttr(SystemContext.KEY_APPTS);	
	}
	
	public String json; //保存刷新得到的所有数据
	
	/**
	 * 刷新查看所有司机证件
	 * @throws JSONException 
	 */
	public String refresh() throws JSONException {
		JSONObject json = new JSONObject();
		Map<String,Object> args = new HashMap<String, Object>();
		getTotalCerts(args);
		try {
			json.put("lists", listInfo);
			json.put("totalCerts", totalCerts);
			json.put("alreadlyUpload", alreadlyUpload);
			json.put("yetUpload", yetUpload);
			json.put("htmlPageNamespace", htmlPageNamespace);
			json.put("appTs", appTs);
			json.put("success", true);
			json.put("msg", "刷新成功！");
		} catch (JSONException e) {
			json.put("success", false);
			json.put("msg", "刷新失败！");
		}
		this.json = json.toString();
		return "json";
	}
	

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


}
