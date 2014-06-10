package cn.bc.cert.hibernate.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bc.cert.Dao.CertCfgTypeDao;
import cn.bc.cert.domain.CertType;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;
import cn.bc.orm.hibernate.jpa.HibernateJpaNativeQuery;

public class CertCfgTypeDaoImpl extends HibernateCrudJpaDao<CertType> implements CertCfgTypeDao {

	public List<Map<String, String>> findCertTypes() {
		
			String sql = "select id,name from bc_cert_type";
			return HibernateJpaNativeQuery.executeNativeSql(getJpaTemplate(), sql,
					null, new RowMapper<Map<String, String>>() {
						public Map<String, String> mapRow(Object[] rs, int rowNum) {
							Map<String, String> oi = new HashMap<String, String>();
							int i = 0;
							oi.put("key", rs[i++].toString());
							oi.put("value", rs[i++].toString());
							return oi;
						}
					});
		}

}
