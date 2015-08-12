package com.mbbatch.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "TPCOMMENTSUSER")
public class TpcommentsUser implements java.io.Serializable {
	@Id
	@Column(name = "CURFNUM", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long curfnum;
	
	@Column(name = "CUUSERID")
	private String cuuserid;
	
	@Column(name = "CUISREGISTERED")
	private String cuisregistered;
	
	@Column(name = "CUIPADDRESS")
	private String cuipaddress;
	@Column(name = "CUUSERNAME")
	private String cuusername;
	@Column(name = "CUUSERTYPE")
	private String cuusertype;
	@Column(name = "CREATEDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	@Column(name = "MODIDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modiDate;
	
	public Long getCurfnum() {
		return curfnum;
	}
	public void setCurfnum(Long curfnum) {
		this.curfnum = curfnum;
	}
	public String getCuuserid() {
		return cuuserid;
	}
	public void setCuuserid(String cuuserid) {
		this.cuuserid = cuuserid;
	}
	public String getCuisregistered() {
		return cuisregistered;
	}
	public void setCuisregistered(String cuisregistered) {
		this.cuisregistered = cuisregistered;
	}
	public String getCuipaddress() {
		return cuipaddress;
	}
	public void setCuipaddress(String cuipaddress) {
		this.cuipaddress = cuipaddress;
	}
	public String getCuusername() {
		return cuusername;
	}
	public void setCuusername(String cuusername) {
		this.cuusername = cuusername;
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
	public String getCuusertype() {
		return cuusertype;
	}
	public void setCuusertype(String cuusertype) {
		this.cuusertype = cuusertype;
	}
	
	
}
