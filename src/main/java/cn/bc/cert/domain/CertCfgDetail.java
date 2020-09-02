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
  private static final long serialVersionUID = 1L;
  private int pageNo; //页码
  private BigDecimal width; //打印宽度
  private String name; //标记

  private CertCfg certCfg;

  @Column(name = "PAGE_NO")
  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
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

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "pid", referencedColumnName = "ID")
  public CertCfg getCertCfg() {
    return certCfg;
  }

  public void setCertCfg(CertCfg certCfg) {
    this.certCfg = certCfg;
  }
}