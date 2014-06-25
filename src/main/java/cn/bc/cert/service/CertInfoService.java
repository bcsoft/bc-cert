package cn.bc.cert.service;

import cn.bc.core.service.CrudService;
import cn.bc.form.domain.Form;

public interface CertInfoService extends CrudService<Form>{

	/**
	 *通过id找到对应的Form对象
	 * 
	 * @return
	 */
	public Form loadById(Long id);
}
