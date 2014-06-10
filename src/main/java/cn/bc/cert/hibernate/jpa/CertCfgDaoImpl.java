package cn.bc.cert.hibernate.jpa;

import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;

public class CertCfgDaoImpl extends HibernateCrudJpaDao<CertCfg> implements CertCfgDao {


	public CertCfg loadById(Long id) {
		AndCondition ac = new AndCondition();
		ac.add(new EqualsCondition("id", id));
		if (this.createQuery().condition(ac).count() == 0) {
			return null;
		} else {
			return this.createQuery().condition(ac).list().get(0);
		}
	}

}
