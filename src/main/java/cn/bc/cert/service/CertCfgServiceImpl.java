package cn.bc.cert.service;

import org.springframework.beans.factory.annotation.Autowired;

import cn.bc.cert.Dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.core.service.DefaultCrudService;

import java.util.List;
import java.util.Map;

public class CertCfgServiceImpl extends DefaultCrudService<CertCfg> implements CertCfgService {

	private CertCfgDao certCfgDao;
	
	@Autowired
	public void setCertCfgTypeDao(CertCfgDao certCfgDao) {
		this.certCfgDao = certCfgDao;
		this.setCrudDao(certCfgDao);
	}
	
	public CertCfg loadById(Long id) {
		return this.certCfgDao.loadById(id);
	}

	public List<Map<String, String>> findEnabled4Option(String typeCode) {
		return this.certCfgDao.findEnabled4Option(typeCode);
	}

	public CertCfg loadByCode(String typeCode, String cfgCode) {
		return certCfgDao.loadByCode(typeCode, cfgCode);
	}

	public List<Map<String, String>> find4AllCertsInfo(String typeCode, Long pid ,String userCode) {
		return certCfgDao.find4AllCertsInfo(typeCode, pid ,userCode);
	}

	public Map<String,Object> findDriverTempByCarMan(int carId) {
		return certCfgDao.findDriverTempByCarMan(carId);
	}


}
