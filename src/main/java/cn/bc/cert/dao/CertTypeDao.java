package cn.bc.cert.dao;

import cn.bc.cert.domain.CertType;
import cn.bc.core.dao.CrudDao;

import java.util.List;
import java.util.Map;

/**
 * 查找证件类型Dao
 *
 * @author LeeDane
 */

public interface CertTypeDao extends CrudDao<CertType> {
	/**
	 * 查找所有证件类别
	 *
	 * @return
	 */
	List<Map<String, String>> findCertTypes();

	/**
	 * 根据ID找到对应的证件类型
	 *
	 * @return
	 */
	CertType loadById(Long id);

	/**
	 * 判断编号是否唯一
	 *
	 * @return
	 */
	boolean isUnique(Long id, String code);
}