package cn.bc.cert.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.bc.identity.domain.RichFileEntityImpl;

/**
 * 证件配置
 * @author dragon
 *
 */
@Entity
@Table(name = "BC_CERT_CFG")
public class CertCfg extends RichFileEntityImpl {
	private static final long serialVersionUID = 1L;
	public static final String KEY_UID = "cert.uid";
	public static final String ATTACH_TYPE =CertCfg.class.getSimpleName();
	/** 正常 */
	public static final int STATUS_ENABLED = 0;
	/** 禁用 */
	public static final int STATUS_DISABLED = 1;
	private String name; //名称
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}