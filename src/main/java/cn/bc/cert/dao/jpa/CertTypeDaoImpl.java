package cn.bc.cert.dao.jpa;

import cn.bc.cert.dao.CertTypeDao;
import cn.bc.cert.domain.CertType;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.core.query.condition.impl.NotEqualsCondition;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.orm.jpa.JpaCrudDao;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertTypeDaoImpl extends JpaCrudDao<CertType> implements CertTypeDao {
	public List<Map<String, String>> findCertTypes() {
		String sql = "select id,name from bc_cert_type";
		return executeNativeQuery(sql, (Object[]) null, new RowMapper<Map<String, String>>() {
			public Map<String, String> mapRow(Object[] rs, int rowNum) {
				Map<String, String> oi = new HashMap<>();
				int i = 0;
				oi.put("key", rs[i++].toString());
				oi.put("value", rs[i++].toString());
				return oi;
			}
		});
	}

	public CertType loadById(Long id) {
		AndCondition ac = new AndCondition();
		ac.add(new EqualsCondition("id", id));
		if (this.createQuery().condition(ac).count() == 0) {
			return null;
		} else {
			return this.createQuery().condition(ac).list().get(0);
		}
	}

	public boolean isUnique(Long id, String code) {
		Assert.notNull(code);
		AndCondition ac = new AndCondition();
		if (id != null) {
			ac.add(new NotEqualsCondition("id", id));
		}
		ac.add(new EqualsCondition("code", code));

		return this.createQuery().condition(ac).count() == 0;
	}
}