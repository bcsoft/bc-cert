package cn.bc.cert.Dao;

import cn.bc.cert.domain.CertCfg;
import cn.bc.core.dao.CrudDao;

/**
 * 证件配置Dao
 * 
 * @author LeeDane
 *
 */

public interface CertCfgDao extends CrudDao<CertCfg> {

	/**
	 * 通过pid找到对应的CertCfg对象
	 * 
	 * @return
	 */
	public CertCfg loadById(Long id);
}
