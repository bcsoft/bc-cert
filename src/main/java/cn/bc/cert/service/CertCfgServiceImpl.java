package cn.bc.cert.service;

import org.springframework.beans.factory.annotation.Autowired;

import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.service.DefaultCrudService;

public class CertCfgServiceImpl  extends DefaultCrudService<CertCfg> implements CertCfgService {

	private CertCfgDao certCfgDao;
	
	@Autowired
	public void setCertCfgTypeDao(CertCfgDao certCfgDao) {
		this.certCfgDao = certCfgDao;
		this.setCrudDao(certCfgDao);
	}
	
	public CertCfg loadById(Long id) {
		
		return this.certCfgDao.loadById(id);
	}
}
