package cn.bc.cert.web.struts2;

import cn.bc.core.util.TemplateUtils;
import cn.bc.identity.web.SystemContext;
import cn.bc.identity.web.SystemContextHolder;
import cn.bc.template.service.TemplateService;
import cn.bc.template.util.FreeMarkerUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.Calendar;
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

        // 获取数据,格式为: [{typeName, typeCode, certName, certCode, version, attachWidth, attachId},...]
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
            sql.append(", '" + cfg.getString("ver") + "'::text");
            sql.append(", " + i);
        }
        sql.append("\r\n) select ct.name typeName, ct.code typeCode, cc.name certName, cc.code certCode, c.pid pid, c.ver_ as version\r\n");
        sql.append("	, f.subject subject, f.desc_ as description\r\n");
        sql.append("	, (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_width') attachWidth\r\n");
        sql.append("	, (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attachId \r\n");
        sql.append("	from bc_form f\r\n");
        sql.append("	inner join cfg c on (c.type_ = f.type_ and c.code = f.code and c.pid = f.pid and c.ver_ = f.ver_)\r\n");
        sql.append("	inner join bc_cert_cfg cc on (cc.code = c.code)\r\n");
        sql.append("	inner join bc_cert_type ct on (ct.code = c.type_)\r\n");
        sql.append("	order by c.order_, ct.order_no, cc.order_no;\r\n");
        if(logger.isDebugEnabled()){
            logger.debug("sql=" + sql);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        // 获取证件打印参数
        List<Map<String, Object>> certs = this.jdbcTemplate.queryForList(sql.toString());
        params.put("certs", certs);

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
}