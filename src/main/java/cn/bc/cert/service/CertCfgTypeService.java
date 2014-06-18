package cn.bc.cert.service;

import java.util.List;
import java.util.Map;

import cn.bc.cert.domain.CertType;
import cn.bc.core.service.CrudService;

public interface CertCfgTypeService extends CrudService<CertType>{

	/**
	 * 查找所有证件类别
	 * 
	 * @return
	 */
	public List<Map<String, String>> findCertTypes();
	
	/**
	 * 根据Id找到对应的证件类型
	 * 
	 * @return
	 */
	public CertType loadById(Long id);
}
