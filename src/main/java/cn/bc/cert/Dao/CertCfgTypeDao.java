package cn.bc.cert.Dao;

import java.util.List;
import java.util.Map;

import cn.bc.cert.domain.CertType;
import cn.bc.core.dao.CrudDao;

/**
 * 查找证件类型Dao
 * 
 * @author LeeDane
 *
 */

public interface CertCfgTypeDao extends CrudDao<CertType> {

	/**
	 * 查找所有证件类别
	 * 
	 * @return
	 */
	public List<Map<String, String>> findCertTypes();
	
	/**
	 *根据ID找到对应的证件类型
	 * 
	 * @return
	 */
	public CertType loadById(Long id);
	
	/**
	 *判断编号是否唯一
	 * 
	 * @return
	 */
	public boolean isUnique(Long id,String code);
}
