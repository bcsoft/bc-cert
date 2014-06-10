package cn.bc.cert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.bc.cert.Dao.CertCfgTypeDao;
import cn.bc.cert.domain.CertType;
import cn.bc.core.service.DefaultCrudService;

public class CertCfgTypeServiceImpl  extends DefaultCrudService<CertType> implements CertCfgTypeService {

	private CertCfgTypeDao certCfgTypeDao;
	
	@Autowired
	public void setCertCfgTypeDao(CertCfgTypeDao certCfgTypeDao) {
		this.certCfgTypeDao = certCfgTypeDao;
		this.setCrudDao(certCfgTypeDao);
	}
	
	public List<Map<String, String>> findCertTypes() {
		
		return this.certCfgTypeDao.findCertTypes();
	}
}
