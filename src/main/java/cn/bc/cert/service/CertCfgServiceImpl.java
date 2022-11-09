package cn.bc.cert.service;

import cn.bc.cert.dao.CertCfgDao;
import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.core.service.DefaultCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertCfgServiceImpl extends DefaultCrudService<CertCfg> implements CertCfgService {
  private CertCfgDao certCfgDao;

  @Autowired
  public void setCertCfgDao(CertCfgDao certCfgDao) {
    this.certCfgDao = certCfgDao;
    this.setCrudDao(certCfgDao);
  }

  public CertCfg loadById(Long id) {
    return this.certCfgDao.loadById(id);
  }

  public List<Map<String, String>> findEnabled4Option(String typeCode) {
    return this.certCfgDao.findEnabled4Option(typeCode);
  }

  @Override
  public List<Map<String, String>> find4Option(Integer[] statuses, String[] typeCodes) {
    return certCfgDao.find4Option(statuses, typeCodes);
  }

  public CertCfg loadByCode(String typeCode, String cfgCode) {
    return certCfgDao.loadByCode(typeCode, cfgCode);
  }

  public List<Map<String, String>> find4AllCertsInfo(String typeCode, Long pid, String userCode) {
    return certCfgDao.find4AllCertsInfo(typeCode, pid, userCode);
  }

  public Map<String, Object> findDriverTempByCarMan(int carId) {
    return certCfgDao.findDriverTempByCarMan(carId);
  }

  public List<Map<String, Object>> find4AllCertsNameAndIdCfgByTypeCode(String typeCode) {

    return certCfgDao.find4AllCertsNameAndIdCfgByTypeCode(typeCode);
  }

  @Override
  public List<Map<String, Object>> findCertWidthByCfgCode(String code) {
    return certCfgDao.findCertWidthByCfgCode(code);
  }
}
