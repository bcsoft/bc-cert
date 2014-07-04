package cn.bc.cert.service;


import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.domain.CertCfgDetail;
import cn.bc.core.service.CrudService;

import java.util.List;
import java.util.Map;

public interface CertCfgService extends CrudService<CertCfg>{
	/**
	 *通过pid找到对应的CertCfg对象
	 * 
	 * @return
	 */
	public CertCfg loadById(Long id);

    /**
     * 获取当前可用的证件下拉列表信息
     *
     * @param typeCode 证件类别的编码
     * @return 返回结果中的元素Map格式为：：key - CertCfg的code, value - CertCfg的name
     */
    public List<Map<String, String>> findEnabled4Option(String typeCode);
    
    /**
     * 获取证件配置信息
     * @param typeCode
     * @param cfgCode
     * @return
     */
	public CertCfg loadByCode(String typeCode, String cfgCode);
}
