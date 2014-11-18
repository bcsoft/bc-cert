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
     * 要打印的多个证件信息，为JSONArray格式的字符串
     * 格式为：[{pid, type, code, ver}, ...]
     */
    public String certs;
    public String json;

    @Override
    public String execute() throws Exception {
        JSONArray cfgs = new JSONArray(this.certs);
        
        Map<String, Object> params = new HashMap<String, Object>();
        List<Map<String, Object>> certs1 = new ArrayList<Map<String,Object>>();;
        if(cfgs.length()> 0) {
		   if(cfgs.getJSONObject(0).has("isSpecialPrint")){	  
			   for(int i =0; i < cfgs.length() ; i++){
				   Map<String,Object> map = new HashMap<String,Object>();
				   map.put("typecode", cfgs.getJSONObject(i).get("typecode"));
				   map.put("certcode", cfgs.getJSONObject(i).get("certcode"));
				   map.put("pid", cfgs.getJSONObject(i).getInt("pid"));
				   map.put("version", cfgs.getJSONObject(i).get("version").toString());
				   map.put("attachId", cfgs.getJSONObject(i).get("attachid").toString());
				   map.put("attachWidth", cfgs.getJSONObject(i).get("attachwidth").toString());
				   map.put("subject", cfgs.getJSONObject(i).get("subject").toString());
				   map.put("one_page_one_typography", cfgs.getJSONObject(i).get("one_page_one_typography").toString());
				   certs1.add(map);
			   }
		   }else{
			   // 获取证件打印参数
		        certs1 = getCertsData4Print(cfgs, false);
		   }
		}
        params.put("certs", certs1);
        // 添加系统上下文和时间戳
        SystemContext context = SystemContextHolder.get();
        params.put("htmlPageNamespace", context.getAttr(SystemContext.KEY_HTMLPAGENAMESPACE));
        params.put("appTs", context.getAttr(SystemContext.KEY_APPTS));

        // 渲染表单
        String html = this.templateService.getContent("DEFAULT_CERT_PRINT");
        html = FreeMarkerUtils.format(html, params);
        // TODO 创建打印日志
        this.json = html;
        return "json";
    }
    
    public List<Map<String, Object>> certss;
    public String specialPrintForm() throws Exception{
    	JSONArray cfgs = new JSONArray(this.certs);
        certss =  getCertsData4Print(cfgs, true);
    	return "success";
    }
    
    private List<Map<String, Object>> getCertsData4Print (JSONArray cfgs, boolean isSpecialPrint)throws Exception{
    	StringBuffer sql = new StringBuffer(); 
		sql.append("with cfg(type_, code, pid, ver_, order_) as (\r\n");
		JSONObject cfg;
		for (int i = 0; i < cfgs.length(); i++) {
		    cfg = cfgs.getJSONObject(i);
		    if(i > 0){
		        sql.append("	union\r\n");
		}
		sql.append("	select '" + cfg.getString("type") + "'::text");
		sql.append(", '" + cfg.getString("code") + "'::text");
		sql.append(", " + cfg.getString("pid"));
		if(cfg.has("ver")) {
		sql.append(", " + cfg.getString("ver"));
		}else{
		    sql.append(", null::numeric");
		}
		sql.append(", " + i);
		}
		sql.append("\r\n) select ct.name typeName, ct.code typeCode, cc.name certName, cc.code certCode, c.pid pid, c.ver_ as version\r\n");
		if(isSpecialPrint){
			sql.append(" ,f.uid_ \r\n");
			sql.append(" , (case when (select * from(select string_agg(d.name,',') from bc_cert_cfg_detail d where d.pid = cc.id) dnames) is null\r\n"); 
			sql.append(" then '合并页' else '合并页,' || (select * from(select string_agg(d.name,',') from bc_cert_cfg_detail d where d.pid = cc.id) dnames) end) choose_print_page\r\n");
			sql.append(" , substr(f.subject,0,9) subject\r\n");
			sql.append(" , (select string_agg(value_,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_width%') attachWidth \r\n");
			sql.append(" , (select string_agg(value_,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_id%') attachId \r\n");
			sql.append(" , (select String_agg((case when ff.value_ = '' or ff.value_ is null then 1 else 0 end)::text,',') from bc_form_field ff where ff.pid = f.id and ff.name_ like 'attach_id%' ) isupload");
		}
		else{
			sql.append(" , f.subject subject\r\n");
			sql.append(" , (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_width') attachWidth \r\n");
			sql.append(" , (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attachId \r\n");
		}
		sql.append("	, f.desc_ as description\r\n");
		sql.append("	, cc.one_page_one_typography as one_page_one_typography\r\n");
		sql.append("	from bc_form f\r\n");
		sql.append("	inner join cfg c on (c.type_ = f.type_ and c.code = f.code and c.pid = f.pid and c.ver_ = f.ver_)\r\n");
		sql.append("	inner join bc_cert_cfg cc on (cc.code = c.code)\r\n");
		sql.append("	inner join bc_cert_type ct on (ct.code = c.type_)\r\n");
		sql.append("	order by c.order_, ct.order_no, cc.order_no;\r\n");
		
		if(logger.isDebugEnabled()){
            logger.debug("sql=" + sql);
        }
	
    	return this.jdbcTemplate.queryForList(sql.toString());
    }
}