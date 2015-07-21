package cn.bc.cert.web.struts2;

import cn.bc.cert.service.CertCfgService;
import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.SystemContextHolder;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看所有证件的表单action
 *
 * @author LeeDane
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class SeeAllByParentAction extends ActionSupport implements SessionAware, RequestAware {
	private static final long serialVersionUID = 1L;

	@Autowired
	private CertCfgService certCfgService;

	protected Map<String, Object> session;
	protected Map<String, Object> request;

	public String pid;  //pid
	public String type; //业务类别，如司机证件、车辆证件

	public SystemContext getContext() {
		return SystemContextHolder.get();
	}

	public int totalCerts;//总的证件数
	public int alreadlyUpload; //已经上传的证件数
	public int yetUpload;//未上传的证件数
	public List<Map<String, String>> listInfo = null; //所有的证件列表信息

	public JSONObject jsonData = new JSONObject();//封装全部的json返回数据

	public String appTs;
	public String htmlPageNamespace;

	/**
	 * 查看所有的证件信息
	 *
	 * @return
	 */
	public String seeAll() throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		getTotalCerts(args);  //根据证件类型获取全部的证件
		return "form";
	}

	/**
	 * 得到总的证件配置里面的证件信息，根据证件类别，和pid查
	 */
	public void getTotalCerts(Map<String, Object> args) throws JSONException {  //args保存的是参数
		String userCode = getContext().getUser().getCode();
		listInfo = certCfgService.find4AllCertsInfo(this.type, Long.parseLong(pid), userCode);
		Map<String, Object> allCerts = new HashMap<String, Object>();

		int allCertsNum = listInfo.size();
		if (allCertsNum > 0) {
			for (int i = 0; i < allCertsNum; i++) {
				if (listInfo.get(i).get("isUpload").toString().equals("no")) {
					yetUpload++;
				}
				allCerts.put(listInfo.get(i).get("name"), listInfo.get(i).get("name"));
			}
		}

		totalCerts = allCerts.size();
		alreadlyUpload = totalCerts - yetUpload;

		htmlPageNamespace = getContext().getAttr(SystemContext.KEY_HTMLPAGENAMESPACE);
		appTs = getContext().getAttr(SystemContext.KEY_APPTS);

		try {
			jsonData.put("lists", listInfo);
			jsonData.put("totalCerts", totalCerts);
			jsonData.put("alreadlyUpload", alreadlyUpload);
			jsonData.put("yetUpload", yetUpload);
			jsonData.put("htmlPageNamespace", htmlPageNamespace);
			jsonData.put("appTs", appTs);
			jsonData.put("success", true);
			jsonData.put("msg", "刷新成功！");
		} catch (JSONException e) {
			jsonData.put("success", false);
			jsonData.put("msg", "刷新失败！");
		}
		this.json = jsonData.toString();
	}

	public String json; //保存刷新得到的所有数据

	/**
	 * 封装了所有的证件信息的json数据
	 *
	 * @throws JSONException
	 */
	public String seeAllCertsData() throws JSONException {
		Map<String, Object> args = new HashMap<String, Object>();
		getTotalCerts(args);
		return "json";
	}

	public int carId;
	public String carUid;
	public boolean carIsNew;
	public String carPlateType;

	public boolean isCarReadonly;
	public boolean isCarRead;

	/**
	 * 获取车辆的信息
	 */
	public String findCar() {
		this.isCarReadonly = !getContext().hasAnyRole("BS_CAR_CERT_MANAGE", "BC_ADMIN");
		this.isCarRead = !getContext().hasAnyRole("BS_CAR_CERT_MANAGE", "BS_CAR_CERT_READ", "BC_ADMIN");
		return "cert";
	}

	public int driverId;
	public int driverTempId;
	public String driverUid;
	public boolean driverIsNew;
	public String pname;
	public boolean isDriverReadonly;
	public boolean isDriverRead;

	/**
	 * 获取司机信息
	 */
	public String findDriver() {
		this.isDriverReadonly = !getContext().hasAnyRole("BS_DRIVER_CERT_MANAGE", "BC_ADMIN");
		this.isDriverRead = !getContext().hasAnyRole("BS_DRIVER_CERT_MANAGE", "BS_DRIVER_CERT_READ", "BC_ADMIN");
		Map<String, Object> maps = new HashMap<String, Object>();
		try {
			maps = certCfgService.findDriverTempByCarMan(driverId);
			Object vl = maps.get("driverTempId");
			if (vl != null) {
				driverTempId = Integer.parseInt(String.valueOf(vl));
			}
			return "driver";
		} catch (DataAccessException e) {
			JSONObject json = new JSONObject();
			String msg = "该司机不存在！请确认该司机在司机招聘的身份证和司机表单上的身份证是否一致。";
			try {
				json.put("msg", msg);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			this.json = json.toString();
			return "json";
		}
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
