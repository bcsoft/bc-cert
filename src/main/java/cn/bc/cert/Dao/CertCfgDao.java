package cn.bc.cert.Dao;

import cn.bc.cert.domain.CertCfg;
import cn.bc.core.dao.CrudDao;

import java.util.List;
import java.util.Map;

/**
 * 证件配置Dao
 * 
 * @author LeeDane
 *
 */

public interface CertCfgDao extends CrudDao<CertCfg> {
	/**
	 * 通过pid找到对应的CertCfg对象
	 * 
	 * @return
	 */
	public CertCfg loadById(Long id);

    /**
     * 获取当前可用的证件下拉列表信息
     *
     * @param typeCode 证件类别的编码
     * @return 返回结果中的元素Map格式为：key - CertCfg的code, value - CertCfg的name
     */
    List<Map<String, String>> findEnabled4Option(String typeCode);

    /**
     * 获取证件配置的选项列表
     * @param statuses 证件配置的状态列表
     * @param typeCodes 证件类别的编码列表
     * @return 返回结果中的元素Map格式为：：key - CertCfg的code, value - CertCfg的name
     */
    List<Map<String, String>> find4Option(Integer[] statuses, String[] typeCodes);
}
