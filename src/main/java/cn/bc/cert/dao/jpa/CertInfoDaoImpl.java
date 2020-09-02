package cn.bc.cert.dao.jpa;

import cn.bc.cert.dao.CertInfoDao;
import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.form.domain.Form;
import cn.bc.orm.jpa.JpaCrudDao;
import org.springframework.stereotype.Component;

@Component
public class CertInfoDaoImpl extends JpaCrudDao<Form> implements CertInfoDao {
  public Form loadById(Long id) {
    AndCondition ac = new AndCondition();
    ac.add(new EqualsCondition("id", id));
    if (this.createQuery().condition(ac).count() == 0) {
      return null;
    } else {
      return this.createQuery().condition(ac).list().get(0);
    }
  }
}