package cn.bc.cert.service;

import cn.bc.cert.domain.CertType;
import cn.bc.core.service.CrudService;

import java.util.List;
import java.util.Map;

public interface CertTypeService extends CrudService<CertType> {
	/**
	 * 查找所有证件类别
	 *
	 * @return
	 */
	List<Map<String, String>> findCertTypes();

	/**
	 * 根据Id找到对应的证件类型
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
