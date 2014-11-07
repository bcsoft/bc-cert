package cn.bc.cert.hibernate.jpa;

import cn.bc.BCConstants;
import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.core.util.DateUtils;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.identity.web.SystemContext;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;
import cn.bc.orm.hibernate.jpa.HibernateJpaNativeQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

public class CertCfgDaoImpl extends HibernateCrudJpaDao<CertCfg> implements CertCfgDao {
    private static Log logger = LogFactory.getLog(CertCfgDaoImpl.class);
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public CertCfg loadById(Long id) {
		AndCondition ac = new AndCondition();
		ac.add(new EqualsCondition("id", id));
		if (this.createQuery().condition(ac).count() == 0) {
			return null;
		} else {
			return this.createQuery().condition(ac).list().get(0);
		}
	}

    public List<Map<String, String>> findEnabled4Option(String typeCode) {
        return this.find4Option(new Integer[]{BCConstants.STATUS_ENABLED}
                , typeCode != null ? new String[]{typeCode} : null);
    }

    public List<Map<String, String>> find4Option(Integer[] statuses, String[] typeCodes) {
        String hql = "select c.code code, c.name as name, t.code typeCode from BC_CERT_CFG c";
        hql += " inner join BC_CERT_TYPE t on t.id = c.type_id";
        List<Object> args = new ArrayList<Object>();
        boolean isAnd = false;

        // 证件配置状态
        if (statuses != null && statuses.length > 0) {
            if (statuses.length == 1) {
                hql += " where c.status_ = ?";
                args.add(statuses[0]);
            } else {
                hql += " where c.status_ in (";
                for (int i = 0; i < statuses.length; i++) {
                    hql += (i == 0 ? "?" : ",?");
                    args.add(statuses[i]);
                }
                hql += ")";
            }
            isAnd = true;
        }

        if (typeCodes != null && typeCodes.length > 0) {
            if (typeCodes.length == 1) {
                hql += (isAnd ? " and" : " where") + " t.code = ?";
                args.add(typeCodes[0]);
            } else {
                hql += (isAnd ? " and" : " where") + " t.code in (";
                for (int i = 0; i < typeCodes.length; i++) {
                    hql += (i == 0 ? "?" : ",?");
                    args.add(typeCodes[i]);
                }
                hql += ")";
            }
        }

        hql += " order by t.order_no, c.order_no";
        if (logger.isDebugEnabled()) {
            logger.debug("args=" + StringUtils.collectionToCommaDelimitedString(args) + "hql=" + hql);
        }
        return HibernateJpaNativeQuery.executeNativeSql(getJpaTemplate(), hql,
                args.toArray(), new RowMapper<Map<String, String>>() {
                    public Map<String, String> mapRow(Object[] rs, int rowNum) {
                        Map<String, String> oi = new HashMap<String, String>();
                        int i = 0;
                        oi.put("key", rs[i++].toString());
                        oi.put("value", rs[i++].toString());
                        oi.put("typeCode", rs[i++].toString());
                        return oi;
                    }
                });
    }

	
	public CertCfg loadByCode(String typeCode, String cfgCode) {
		return this.createQuery().condition(new AndCondition()
			.add(new EqualsCondition("certType.code", typeCode))
			.add(new EqualsCondition("code", cfgCode)))
			.singleResult();
	}

	public List<Map<String,String>> find4AllCertsInfo(String typeCode,Long pid,String userCode) {
		
		String hql = "with actor(id) as (select id from bc_identity_actor where code = ? union select identity_find_actor_ancestor_ids(?)),";
		//-- 证件配置
			hql+=" cert_cfg(id, type_code, type_name, code, name, tpl, order_) as (select c.id, t.code, t.name, c.code, c.name, c.tpl, c.order_no from bc_cert_cfg c inner join bc_cert_type t on t.id = c.type_id where t.code = ? and c.status_ = 0),";
		//有 ACL 控制权限的证件配置
			hql+=" acl(cert_id, role_) as (select c.id, aa.role from bc_acl_actor aa inner join bc_acl_doc ad on ad.id = aa.pid inner join cert_cfg c on (cast(c.id as text) = ad.doc_id and ad.doc_type = 'CertCfg')where aa.aid in (select id from actor)),";
		//查找所有被配置ACL控制的证件配置
			hql+=" acl_no(cert_id) as (select distinct c.id from bc_acl_actor aa inner join bc_acl_doc ad on ad.id = aa.pid inner join cert_cfg c on (cast(c.id as text) = ad.doc_id and ad.doc_type = 'CertCfg')),";
		//有权限查阅的证件配置
			hql+=" cert_cfg_with_acl(id, type_code, type_name, code, name, tpl, order_) as (select * from cert_cfg c where (";
			//有 ACL 查阅或编辑权限
			hql+=" exists (select 0 from acl where acl.cert_id = c.id and acl.role_ in ('10', '11', '01'))";
			//无 ACL 权限控制：没有配置时默认为允许查阅
			//or not exists (select 0 from acl where acl.cert_id = c.id)
			hql+=" or not exists (select 0 from acl_no where c.id = acl_no.cert_id))";
		//无 ACL 禁止查阅控制
			hql+=" and not exists (select 0 from acl where acl.cert_id = c.id and acl.role_ = '00'))";
			hql+=" select c.name as name,f.ver_ as version, ";
			hql+=" (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attach_id ,";
			hql+=" (case f.id is null when true then 'no' else 'yes' end) upload_status,";
			hql+=" c.code code,f.modifier_id modifier_id,ich.actor_name actor_name,f.modified_date modified_date,";
			hql+=" c.type_code typeCode,f.pid pid,f.id fid,f.uid_ uid,f.subject subject,c.tpl tpl ";
			hql+=" from cert_cfg_with_acl c";// 有查阅权限的证件
			hql+=" left join bc_form f on (f.type_ = c.type_code and f.code = c.code and f.pid=? )";
			hql+=" left join bc_identity_actor_history ich on f.modifier_id = ich.id";
			hql+=" order by c.order_ asc,f.ver_ desc ";

        
        List<Object> args = new ArrayList<Object>();
    	args.add(userCode);
    	args.add(userCode);
        args.add(typeCode);
        args.add(pid);
        
		return HibernateJpaNativeQuery.executeNativeSql(getJpaTemplate(), hql,
                args.toArray(), new RowMapper<Map<String, String>>() {
            public Map<String, String> mapRow(Object[] rs, int rowNum) {
                Map<String, String> oi = new HashMap<String, String>();
                int i = 0;
                oi.put("name", dealNullValue(rs[i++]));
                oi.put("version", dealVersionStyle(dealNullValue(rs[i++])));
                oi.put("attach_id", dealNullValue(rs[i++]));
                oi.put("isUpload", dealNullValue(rs[i++]));
                oi.put("code", dealNullValue(rs[i++]));
                oi.put("modifier_id", dealNullValue(rs[i++]));
                oi.put("actor_name", dealNullValue(rs[i++]));
                oi.put("modified_date", DateUtils
						.formatDateTime2Minute((Date)rs[i++]));   
                oi.put("typeCode", dealNullValue(rs[i++]));
                oi.put("pid", dealNullValue(rs[i++]));
                oi.put("fid", dealNullValue(rs[i++]));
                oi.put("uid", dealNullValue(rs[i++]));
                oi.put("subject", dealNullValue(rs[i++]));
                oi.put("tpl", dealNullValue(rs[i++]));
                return oi;
            }
        });
	}
	/**
	 * 处理某些空值的对象
	 * @param origin
	 * @return
	 */
	public String dealNullValue(Object origin){
		return origin == null ? "" : origin.toString();
	}
	
	/**
	 * 处理版本号的显示效果，如1.00显示成1
	 */
	public String dealVersionStyle(String version){
		if(version != "" && !"".equals(version))
			return new DecimalFormat("#.##").format(Float.valueOf(String.valueOf(version)));
		else
			return "";
	}

	public Map<String,Object> findDriverTempByCarMan(int carId) {
		 String sql = "select td.id as driverTempId from BS_TEMP_DRIVER td inner join BS_CARMAN c on c.cert_identity = td.cert_identity where c.id = ?";
		 Map<String,Object> map= this.jdbcTemplate.queryForMap(sql, carId);	
        return map;
	}

	public List<Map<String, Object>> find4AllCertsCfgByTypeCode(String typeCode) {
		String sql = "";
		if(typeCode == null){
			sql = " select f.name from bc_cert_cfg f inner join bc_cert_type t on f.type_id = t.id where f.status_ = 0 ";
		}else{
			sql = " select f.name from bc_cert_cfg f inner join bc_cert_type t on f.type_id = t.id where f.status_ = 0 and t.code = '" + typeCode+ "' ";
		}
		return this.jdbcTemplate.queryForList(sql);
	}
}