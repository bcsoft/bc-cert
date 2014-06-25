package cn.bc.cert.service;

import org.springframework.beans.factory.annotation.Autowired;

import cn.bc.cert.Dao.CertInfoDao;
import cn.bc.core.service.DefaultCrudService;
import cn.bc.form.domain.Form;

public class CertInfoServiceImpl  extends DefaultCrudService<Form> implements CertInfoService {

	private CertInfoDao certInfoDao;
	
	@Autowired
	public void setCertCfgTypeDao(CertInfoDao certInfoDao) {
		this.certInfoDao = certInfoDao;
		this.setCrudDao(certInfoDao);
	}
	
	public Form loadById(Long id) {
		
		return this.certInfoDao.loadById(id);
	}
}
