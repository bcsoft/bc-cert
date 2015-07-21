package cn.bc.cert.service;

import cn.bc.cert.dao.CertInfoDao;
import cn.bc.core.service.DefaultCrudService;
import cn.bc.form.domain.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertInfoServiceImpl extends DefaultCrudService<Form> implements CertInfoService {
	private CertInfoDao certInfoDao;

	@Autowired
	public void setCertInfoDao(CertInfoDao certInfoDao) {
		this.certInfoDao = certInfoDao;
		this.setCrudDao(certInfoDao);
	}

	public Form loadById(Long id) {
		return this.certInfoDao.loadById(id);
	}
}