package cn.bc.cert.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
	//private int status; //-- 状态 : 0-正常,1-禁用

	private String code;//编码
	private String name; //名称
	private CertType certType; //证件类型，多对一的关系
	private int page_count; //面数
	private BigDecimal width; //打印的宽度，毫米单位
	private String combine; //合并配置
	private String order_no; //排序号
	private String tpl; //表单模板
	
	private Set<CertCfgDetail> certCfgDetails;

	/*@Column(name = "STATUS_")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}*/
	@Column(name = "CODE",length=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "NAME",length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
	public CertType getCertType() {
		return certType;
	}

	public void setCertType(CertType certType) {
		this.certType = certType;
	}

	@Column(name = "PAGE_COUNT")
	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	@Column(name = "WIDTH")
	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	@Column(name = "COMBINE",length=50)
	public String getCombine() {
		return combine;
	}

	public void setCombine(String combine) {
		this.combine = combine;
	}

	@Column(name = "ORDER_NO",length=10)
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@Column(name = "TPL",length=1000)
	public String getTpl() {
		return tpl;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	@OneToMany(mappedBy="certCfg",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy(value = "page_no asc")
	public Set<CertCfgDetail> getCertCfgDetails() {
		return certCfgDetails;
	}

	public void setCertCfgDetails(Set<CertCfgDetail> certCfgDetails) {
		this.certCfgDetails = certCfgDetails;
	}
	
}