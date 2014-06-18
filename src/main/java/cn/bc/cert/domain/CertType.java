package cn.bc.cert.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.bc.identity.domain.FileEntityImpl;

/**
 * 证件类别
 * @author LeeDane
 *
 */
@Entity
@Table(name = "BC_CERT_TYPE")
public class CertType extends FileEntityImpl{

	private static final long serialVersionUID = 1L;
	public static final String KEY_UID = "cert.uid";
	
	private String code;  //编码
	private String name ;// 名称
	private String order_no; //排序号
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "ORDER_NO",length=10)
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	@Column(name = "CODE",length=100)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
