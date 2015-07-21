package cn.bc.cert.service;

import cn.bc.cert.dao.CertTypeDao;
import cn.bc.cert.domain.CertType;
import cn.bc.core.service.DefaultCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertTypeServiceImpl extends DefaultCrudService<CertType> implements CertTypeService {
	private CertTypeDao certTypeDao;

	@Autowired
	public void setCertTypeDao(CertTypeDao certTypeDao) {
		this.certTypeDao = certTypeDao;
		this.setCrudDao(certTypeDao);
	}

	public List<Map<String, String>> findCertTypes() {
		return this.certTypeDao.findCertTypes();
	}

	public CertType loadById(Long id) {
		return this.certTypeDao.loadById(id);
	}

	public boolean isUnique(Long id, String code) {
		return this.certTypeDao.isUnique(id, code);
	}
}