package cn.bc.cert.service;


import cn.bc.cert.domain.CertCfg;
import cn.bc.core.service.CrudService;

public interface CertCfgService extends CrudService<CertCfg>{

	/**
	 *通过pid找到对应的CertCfg对象
	 * 
	 * @return
	 */
	public CertCfg loadById(Long id);
}
