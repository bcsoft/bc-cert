package cn.bc.cert.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.bc.core.EntityImpl;

/**
 * 证件配置明细 domain
 * @author LeeDane
 *
 */
@Entity
@Table(name = "BC_CERT_CFG_DETAIL")
public class CertCfgDetail extends EntityImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*id integer NOT NULL, -- ID
	  pid integer NOT NULL, -- 所属配置ID
	  page_no integer NOT NULL, -- 页码
	  width numeric(10,2) NOT NULL, -- 打印宽度 : 毫米单位
	  name character varying(100) NOT NULL, -- 名称*/
	private int page_no; //页码
	private BigDecimal width; //打印宽度
	private String name; //标记
	
	private CertCfgDetail certCfgDetail; //原始条目
	
	//private CertCfg certCfg;

	@Column(name = "PAGE_NO")
	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	@Column(name = "WIDTH")
	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", referencedColumnName = "ID")
	public CertCfg getCertCfg() {
		return certCfg;
	}

	public void setCertCfg(CertCfg certCfg) {
		this.certCfg = certCfg;
	}*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", referencedColumnName = "ID")
	public CertCfgDetail getCertCfgDetail() {
		return certCfgDetail;
	}

	public void setCertCfgDetail(CertCfgDetail certCfgDetail) {
		this.certCfgDetail = certCfgDetail;
	}
	
	
	
}
