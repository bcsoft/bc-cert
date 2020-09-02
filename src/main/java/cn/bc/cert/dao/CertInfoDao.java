package cn.bc.cert.dao;

import cn.bc.core.dao.CrudDao;
import cn.bc.form.domain.Form;

/**
 * 证件信息Dao
 *
 * @author LeeDane
 */
public interface CertInfoDao extends CrudDao<Form> {
  /**
   * 通过id找到对应的Form对象
   *
   * @return
   */
  Form loadById(Long id);
}