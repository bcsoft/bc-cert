package cn.bc.cert.hibernate.jpa;

import cn.bc.BCConstants;
import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;
import cn.bc.orm.hibernate.jpa.HibernateJpaNativeQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
}