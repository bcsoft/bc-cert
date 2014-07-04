package cn.bc.cert.Dao;

import java.util.List;

import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.core.dao.CrudDao;
import cn.bc.form.domain.Form;

/**
 * 证件信息Dao
 * 
 * @author LeeDane
 *
 */

public interface CertInfoDao extends CrudDao<Form> {

	/**
	 * 通过id找到对应的Form对象
	 * 
	 * @return
	 */
	public Form loadById(Long id);
	
}
