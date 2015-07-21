package cn.bc.cert.service;


import cn.bc.cert.domain.CertCfg;
import cn.bc.core.service.CrudService;

import java.util.List;
import java.util.Map;

public interface CertCfgService extends CrudService<CertCfg> {
	/**
	 * 通过pid找到对应的CertCfg对象
	 *
	 * @return
	 */
	CertCfg loadById(Long id);

	/**
	 * 获取当前可用的证件下拉列表信息
	 *
	 * @param typeCode 证件类别的编码
	 * @return 返回结果中的元素Map格式为：：key - CertCfg的code, value - CertCfg的name
	 */
	List<Map<String, String>> findEnabled4Option(String typeCode);

	/**
	 * 获取证件配置信息
	 *
	 * @param typeCode
	 * @param cfgCode
	 * @return
	 */
	CertCfg loadByCode(String typeCode, String cfgCode);

	/**
	 * 根据证件类别获得所有证件的信息
	 *
	 * @param typeCode
	 * @param pid
	 * @param userCode 当前登录的用户的code
	 * @return
	 */
	List<Map<String, String>> find4AllCertsInfo(String typeCode, Long pid, String userCode);

	/**
	 * 通过司机的id找到司机招聘的对应的id
	 *
	 * @param carId 司机的id
	 * @return
	 */
	Map<String, Object> findDriverTempByCarMan(int carId);

	/**
	 * 通过类型的编码查找对应类型的所有证件名称
	 *
	 * @param typeCode，证件类型的编码，为空表示查找所有的类型
	 * @return
	 */
	List<Map<String, Object>> find4AllCertsNameAndIdCfgByTypeCode(String typeCode);
}