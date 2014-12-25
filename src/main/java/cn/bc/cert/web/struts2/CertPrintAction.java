package cn.bc.cert.web.struts2;

import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.SystemContextHolder;
import cn.bc.template.service.TemplateService;
import cn.bc.template.util.FreeMarkerUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.Array;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证件打印
 * 
 * @author dragon
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertPrintAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(CertPrintAction.class);
	@Autowired
	private TemplateService templateService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 要打印的多个证件信息，为JSONArray格式的字符串 格式为：[{pid, type, code, ver}, ...]
	 */
	public String certs;
	public String json;

	@Override
	public String execute() throws Exception {
		JSONArray cfgs = new JSONArray(this.certs);

		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> certs1 = new ArrayList<Map<String, Object>>();
		if (cfgs.length() > 0) {
			if (dealGetNullKeyValueByJSONArray(cfgs, 0, "isDirectlyPrint") != null
					&& Boolean.parseBoolean(String
							.valueOf(dealGetNullKeyValueByJSONArray(cfgs, 0,
									"isDirectlyPrint"))) == true) { // 是否对certs数据进行处理
				for (int i = 0; i < cfgs.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("attachId",
							dealGetNullKeyValueByJSONArray(cfgs, i, "attachId"));
					map.put("attachWidth",
							dealGetNullKeyValueByJSONArray(cfgs, i,
									"attachWidth"));
					map.put("one_page_one_typography", 2);
					map.put("print_direction", 2);
					certs1.add(map);
				}
				params.put("is_control_print_direction", false); //是否控制打印方向
				params.put("is_control_one_page_one_typography", false);//是否控制一页一版
			} else {

				if (cfgs.getJSONObject(0).has("isSpecialPrint")) {
					for (int i = 0; i < cfgs.length(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("typecode",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"typecode"));
						map.put("certcode",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"certcode"));
						map.put("pid",
								dealGetNullKeyValueByJSONArray(cfgs, i, "pid"));
						map.put("version",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"version").toString());
						map.put("attachId",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"attachid").toString());
						map.put("attachWidth",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"attachwidth").toString());
						map.put("subject",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"subject").toString());
						map.put("one_page_one_typography",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"one_page_one_typography"));
						map.put("print_direction",
								dealGetNullKeyValueByJSONArray(cfgs, i,
										"print_direction"));
						certs1.add(map);
					}
				} else {
					// 获取证件打印参数
					certs1 = getCertsData4Print(cfgs, false);
				}
				params.put("is_control_print_direction", true);
				params.put("is_control_one_page_one_typography", true);
			}

		}
		params.put("certs", certs1);
		// 添加系统上下文和时间戳
		SystemContext context = SystemContextHolder.get();
		params.put("htmlPageNamespace",
				context.getAttr(SystemContext.KEY_HTMLPAGENAMESPACE));
		params.put("appTs", context.getAttr(SystemContext.KEY_APPTS));

		// 渲染表单
		String html = this.templateService.getContent("DEFAULT_CERT_PRINT");
		html = FreeMarkerUtils.format(html, params);
		// TODO 创建打印日志
		this.json = html;
		return "json";
	}

	/**
	 * 处理JSONArray中键不存在而去取值的问题
	 * @param jsonArray JSONArray
	 * @param index 索引
	 * @param key 需要的key值
	 * @return
	 */
	private Object dealGetNullKeyValueByJSONArray(JSONArray jsonArray,
			int index, String key) {
		Object value = null;
		try {
			if (jsonArray.getJSONObject(index).has(key)) {
				value = jsonArray.getJSONObject(index).get(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 在特殊打印里显示pname的列名字
	 * 
	 * @param string
	 * @return
	 */
	private String getLabelValueByTypeCode(String typecode) {
		String rv = ""; // 返回值
		if (typecode != null) {
			if (typecode.equals("CarManCert")) {
				rv = "司机名称";
			} else if (typecode.equalsIgnoreCase("CarCert")) {
				rv = "车牌号码";
			}
		}
		return rv;
	}

	public String pname_label; // pname所在列名称

	public List<Map<String, Object>> certss;

	public String specialPrintForm() throws Exception {
		JSONArray cfgs = new JSONArray(this.certs);
		certss = getCertsData4Print(cfgs, true);
		if (certss.size() > 0)
			pname_label = this.getLabelValueByTypeCode(String.valueOf(certss
					.get(0).get("typecode")));
		return "success";
	}

	private List<Map<String, Object>> getCertsData4Print(JSONArray cfgs,
			boolean isSpecialPrint) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("with cfg(type_, code, pid, ver_, order_) as (\r\n");
		JSONObject cfg;
		for (int i = 0; i < cfgs.length(); i++) {
			cfg = cfgs.getJSONObject(i);
			if (i > 0) {
				sql.append("	union\r\n");
			}
			sql.append("	select '" + cfg.getString("type") + "'::text");
			sql.append(", '" + cfg.getString("code") + "'::text");
			sql.append(", " + cfg.getString("pid"));
			if (cfg.has("ver")) {
				sql.append(", " + cfg.getString("ver"));
			} else {
				sql.append(", null::numeric");
			}
			sql.append(", " + i);
		}
		sql.append("\r\n) select ct.name typeName, ct.code typeCode, cc.name certName, cc.code certCode, c.pid pid, c.ver_ as version\r\n");
		if (isSpecialPrint) {
			sql.append(" ,f.uid_ \r\n");
			sql.append(" , (case when (select * from(select string_agg(d.name,',') from bc_cert_cfg_detail d where d.pid = cc.id) dnames) is null\r\n");
			sql.append(" then '合并页' else '合并页,' || (select * from(select string_agg(t.detail_name,',') from (select d.name detail_name from bc_cert_cfg_detail d where d.pid = cc.id order by d.page_no)t ) dnames) end) choose_print_page\r\n");
			sql.append(" , (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'pname' ) subject \r\n");
			sql.append(" , (select string_agg(value_,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_width%') attachWidth \r\n");
			sql.append(" , (select string_agg(value_,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_id%') attachId \r\n");
			sql.append(" , (select String_agg((case when ff.value_ = '' or ff.value_ is null then 1 else 0 end)::text,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_id%' ) isupload");
		} else {
			sql.append(" , f.subject subject\r\n");
			sql.append(" , (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_width') attachWidth \r\n");
			sql.append(" , (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attachId \r\n");
		}
		sql.append("	, f.desc_ as description\r\n");
		sql.append("	, cc.one_page_one_typography as one_page_one_typography\r\n");
		sql.append("	, cc.print_direction as print_direction\r\n");
		sql.append("	from bc_form f\r\n");
		sql.append("	inner join cfg c on (c.type_ = f.type_ and c.code = f.code and c.pid = f.pid and c.ver_ = f.ver_)\r\n");
		sql.append("	inner join bc_cert_cfg cc on (cc.code = c.code)\r\n");
		sql.append("	inner join bc_cert_type ct on (ct.code = c.type_)\r\n");
		sql.append("	order by c.order_, ct.order_no, cc.order_no;\r\n");

		if (logger.isDebugEnabled()) {
			logger.debug("sql=" + sql);
		}

		return this.jdbcTemplate.queryForList(sql.toString());
	}
}