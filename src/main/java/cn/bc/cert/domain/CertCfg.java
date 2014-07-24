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

	private String code;//编码
	private String name; //名称
	private CertType certType; //证件类型，多对一的关系
	private int pageCount; //面数
	private BigDecimal width; //打印的宽度，毫米单位
	private String combine; //合并配置
	private String orderNo; //排序号
	private String tpl; //表单模板
	
	private Set<CertCfgDetail> details;

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
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
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
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "TPL",length=1000)
	public String getTpl() {
		return tpl;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	@OneToMany(mappedBy="certCfg",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy(value = "pageNo asc")
	public Set<CertCfgDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<CertCfgDetail> details) {
		this.details = details;
	}
	
}