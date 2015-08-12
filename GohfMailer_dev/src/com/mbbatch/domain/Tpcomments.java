package com.mbbatch.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "TPCOMMENTS")
public class Tpcomments implements java.io.Serializable {
	@Id
	@Column(name = "COMRFNUM", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long comrfnum;
	
	/*@Column(name = "COMCURFNUM")
	private Long comcurfnum;*/
	
	@Column(name = "COMCMSITE")
	private Long comcmsite;
	@Column(name = "COMCMTYPE")
	private Long comcmtype;
	@Column(name = "COMTYPERFNUM")
	private Long comtyperfnum;
	@Column(name = "COMDESC")
	private String comdesc;
	@Column(name = "COMCOMRFNUM")
	private Long comcomrfnum;
	@Column(name = "COMMODERSTATUS")
	private String commoderstatus;
	@Column(name = "COMMODERRESULT")
	private Long commoderresult;
	@Column(name = "COMMODERCOMMENT")
	private String commodercomment;
	@Column(name = "COMAPPROVEDBY")
	private Long comapprovedby;
	
	@Column(name = "COMLIKECOUNT")
	private Long comlikecount;
	@Column(name = "COMISACTIVE")
	private String comisactive;
	@Column(name = "DELETED")
	private String deleted;
	
	@Column(name = "CREATEDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	@Column(name = "MODIDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modiDate;
	@Column(name = "COMCOMTOPRFNUM")
	private Long comcomtoprfnum;
	@Column(name = "COMCURFNUM")
	private Long comcurfnum;
	
	
	public Long getComrfnum() {
		return comrfnum;
	}
	public void setComrfnum(Long comrfnum) {
		this.comrfnum = comrfnum;
	}
/*	public Long getComcurfnum() {
		return comcurfnum;
	}
	public void setComcurfnum(Long comcurfnum) {
		this.comcurfnum = comcurfnum;
	}*/
	public Long getComcmsite() {
		return comcmsite;
	}
	public void setComcmsite(Long comcmsite) {
		this.comcmsite = comcmsite;
	}
	public Long getComcmtype() {
		return comcmtype;
	}
	public void setComcmtype(Long comcmtype) {
		this.comcmtype = comcmtype;
	}
	public Long getComtyperfnum() {
		return comtyperfnum;
	}
	public void setComtyperfnum(Long comtyperfnum) {
		this.comtyperfnum = comtyperfnum;
	}
	public String getComdesc() {
		return comdesc;
	}
	public void setComdesc(String comdesc) {
		this.comdesc = comdesc;
	}
	public Long getComcomrfnum() {
		return comcomrfnum;
	}
	public void setComcomrfnum(Long comcomrfnum) {
		this.comcomrfnum = comcomrfnum;
	}
	public String getCommoderstatus() {
		return commoderstatus;
	}
	public void setCommoderstatus(String commoderstatus) {
		this.commoderstatus = commoderstatus;
	}
	public Long getCommoderresult() {
		return commoderresult;
	}
	public void setCommoderresult(Long commoderresult) {
		this.commoderresult = commoderresult;
	}
	public String getCommodercomment() {
		return commodercomment;
	}
	public void setCommodercomment(String commodercomment) {
		this.commodercomment = commodercomment;
	}
	public Long getComapprovedby() {
		return comapprovedby;
	}
	public void setComapprovedby(Long comapprovedby) {
		this.comapprovedby = comapprovedby;
	}
	public Long getComlikecount() {
		return comlikecount;
	}
	public void setComlikecount(Long comlikecount) {
		this.comlikecount = comlikecount;
	}
	public String getComisactive() {
		return comisactive;
	}
	public void setComisactive(String comisactive) {
		this.comisactive = comisactive;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getModiDate() {
		return modiDate;
	}
	public void setModiDate(Date modiDate) {
		this.modiDate = modiDate;
	}
	
	public Long getComcomtoprfnum() {
		return comcomtoprfnum;
	}
	public void setComcomtoprfnum(Long comcomtoprfnum) {
		this.comcomtoprfnum = comcomtoprfnum;
	}
	public Long getComcurfnum() {
		return comcurfnum;
	}
	public void setComcurfnum(Long comcurfnum) {
		this.comcurfnum = comcurfnum;
	}

	
	
}
