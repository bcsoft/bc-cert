package cn.bc.cert.hibernate.jpa;

import cn.bc.BCConstants;
import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.core.util.DateUtils;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;
import cn.bc.orm.hibernate.jpa.HibernateJpaNativeQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertCfgDaoImpl extends HibernateCrudJpaDao<CertCfg> implements CertCfgDao {
    private static Log logger = LogFactory.getLog(CertCfgDaoImpl.class);

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

	public List<Map<String,String>> find4AllCertsInfo(String typeCode,Long pid) {
		String hql = "select c.name as name, f.ver_ as version,";
			hql+= "(select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attach_id ,";
			hql+=" (case when f.id is null then 'no' else 'yes' end) isUpload,c.code code,f.modifier_id modifier_id,ich.actor_name actor_name," ;
			hql+="f.modified_date modified_date,t.code typeCode,f.pid pid,f.id fid,f.uid_ uid,f.subject subject,c.tpl tpl";
			hql+="	from bc_cert_cfg c";
			hql+=" inner join bc_cert_type t on t.id = c.type_id";
			hql+=" left join bc_form f on (f.type_ = t.code and f.code = c.code and f.pid = ?)";
			hql+=" left join bc_identity_actor_history ich on f.modifier_id = ich.id";
			hql+=" where t.code = ?";
			hql+=" order by c.order_no,f.ver_ desc ";
        
        List<Object> args = new ArrayList<Object>();
        args.add(pid);
        args.add(typeCode);
        
		return HibernateJpaNativeQuery.executeNativeSql(getJpaTemplate(), hql,
                args.toArray(), new RowMapper<Map<String, String>>() {
            public Map<String, String> mapRow(Object[] rs, int rowNum) {
                Map<String, String> oi = new HashMap<String, String>();
                int i = 0;
                oi.put("name", dealNullValue(rs[i++]));
                oi.put("version", dealNullValue(rs[i++]));
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
}