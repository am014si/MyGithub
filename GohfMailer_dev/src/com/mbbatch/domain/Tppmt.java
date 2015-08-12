package com.mbbatch.domain;

import java.util.Date;
import java.util.Set;

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



/**
 * Tppmt entity. @author MyEclipse Persistence Tools
 */
@Entity
/*@Table(name = "TPPMT", schema = "PROPERTY", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PMTRFNUM", "PMTISACTIVE", "DELETED", "PMTISSTD" }))*/
@Table(name = "TPPMT")
public class Tppmt implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PMTRFNUM", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long pmtrfnum;
	
	@Column(name = "PMTCNDBACLONY")
	private Long pmtcndbalconies;
	
	@Column(name = "PMTCNDCOVUNIT")
	private Long pmtcndcovunit;
	
	@Column(name = "PMTCNDBEDRMS")
	private Long pmtcndbedrms;
	
	@Column(name = "PMTPITRFNUM")
	private Long pmtpitrfnum;
	
	@Column(name = "PMTCNDTRANTTY")
	private Long pmtcndtrantty;
	
	@Column(name = "PMTLMTRFNUM")
	private Long pmtlmtrfnum;
	
	@Column(name = "PMTCNDPLUNIT")
	private Long pmtcndplunit;
	
	@Column(name = "PMTCNDTNOF")
	private Long pmtcndtnof;
	
	@Column(name = "PMTCNDPROPTYPE")
	private Long pmtcndproptype;
	
	@Column(name = "PMTCNDAOC")
	private Long pmtcndaoc;
	
	@Column(name = "PMTUBISUPERUSER")
	private Long pmtubisuperuser;
	
	@Column(name = "PMTCNDFURNISH")
	private Long pmtcndfurnish;
	
	@Column(name = "PMTCNDTOW")
	private Long pmtcndtow;
	
	@Column(name = "PMTCNDBATHRMS")
	private Long pmtcndbathrms;
	
	@Column(name = "PMTMODERRESULT")
	private Long pmtmoderresult;
	
	@Column(name = "PMTCNDFACING")
	private Long pmtcndfacing;
	
	@Column(name = "PMTUBIRFNUM")
	private Long pmtubirfnum;
	
	@Column(name = "PMTCNDFLN")
	private Long pmtcndfln;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PMTASSIGNTO")
	private Tpmat tpmat;*/
	
	@Column(name = "PMTADID", length = 32)
	private String pmtadid;
	
	@Column(name = "PMTLISTTYPE", length = 1)
	private String pmtlisttype;
	
	@Column(name = "PMTSALEPRICE")
	private Long pmtsaleprice;
	
	@Column(name = "PMTRENTPRICE")
	private Long pmtrentprice;
	
	@Column(name = "PMTDUES", length = 3000)
	private String pmtdues;
	
	@Column(name = "PMTCNDAVAILUNIT")
	private Long pmtcndavailunit;
	
	@Column(name = "PMTBRIEFDISC", length = 1024)
	private String pmtbriefdisc;
	
	@Column(name = "PMTDETAILDISC", length = 3000)
	private String pmtdetaildisc;
	
	@Column(name = "PMTKEYDISC", length = 2048)
	private String pmtkeydisc;
	
	@Column(name = "PMTTNC", length = 3000)
	private String pmttnc;
	
	@Column(name = "PMTISLISTED", length = 1)
	private String pmtislisted;
	
	@Column(name = "PMTISSTD", length = 1)
	private String pmtisstd;
	
	@Column(name = "PMTISACTIVE", length = 1)
	private String pmtisactive;
	
	@Column(name = "PMTMODERSTATUS", length = 1)
	private String pmtmoderstatus;
	
	@Column(name = "PMTMODERCOMMENT", length = 3072)
	private String pmtmodercomment;
	
	@Column(name = "PMTSTARTDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmtstartdate;
	
	@Column(name = "PMTENDDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmtenddate;
	
	@Column(name = "CREATEDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@Column(name = "MODIDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modidate;
	
	@Column(name = "DELETED", length = 1)
	private String deleted;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "EXFIELD1")
	private Long exfield1;
	
	@Column(name = "EXFIELD2", length = 512)
	private String exfield2;
	
	@Column(name = "PMTCOVAREA", length = 64)
	private String pmtcovarea;
	
	@Column(name = "PMTPLAREA", length = 64)
	private String pmtplarea;
	
	@Column(name = "PMTFLRTY", length = 1024)
	private String pmtflrty;
	
	@Column(name = "PMTWOODWRK", length = 1024)
	private String pmtwoodwrk;
	
	@Column(name = "PMTWALLFIN", length = 1024)
	private String pmtwallfin;
	
	@Column(name = "PMTTVPHONE", length = 1024)
	private String pmttvphone;
	
	@Column(name = "PMTWATERSUP", length = 1024)
	private String pmtwatersup;
	
	@Column(name = "PMTNEARHOS", length = 512)
	private String pmtnearhos;
	
	@Column(name = "PMTNEARAIR", length = 512)
	private String pmtnearair;
	
	@Column(name = "PMTNEARRAIL", length = 512)
	private String pmtnearrail;
	
	@Column(name = "PMTNEARSCH", length = 512)
	private String pmtnearsch;
	
	@Column(name = "PMTLMANDNEI", length = 3000)
	private String pmtlmandnei;
	
	@Column(name = "PMTCITYCEN", length = 512)
	private String pmtcitycen;
	
	@Column(name = "PMTNFVIEWS")
	private Long pmtnfviews;
	
	@Column(name = "PMTISVIRTUAL", length = 1)
	private String pmtisvirtual;
	
	@Column(name = "PMTADCODE", length = 128)
	private String pmtadcode;
	
	@Column(name = "PMTUNQUSER")
	private Long pmtunquser;
	
	@Column(name = "EDITEDBY")
	private Long editedby;
	
	@Column(name = "RANKDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date rankdate;
	
	@Column(name = "PMTSOURCE", length = 128)
	private String pmtsource;
	
	@Column(name = "CONTACTSVIEWED")
	private Integer contactsviewed;
	
	@Column(name = "PMTRESPONSETIME", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmtresponsetime;
	
	@Column(name = "PMTSAPSALESORG", length = 128)
	private String pmtsapsalesorg;
	
	@Column(name = "PMTSAPSALESOFFICE", length = 128)
	private String pmtsapsalesoffice;
	
	/*@Column(name = "PMTISPRICENEGBLE", length = 1)
	private String pmtispricenegble;*/
	

	@Column(name = "PMTSORTCODE")
	private Integer pmtsortcode;
	
	@Column(name = "PMTCNDAMENITYFACING", length = 256)
	private String pmtcndamenityfacing;
	
	@Column(name = "PMTCARPETAREA", precision = 53, scale = 0)
	private Double pmtcarpetarea;
	
	@Column(name = "PMTCNDCARPETUNIT")
	private Long pmtcndcarpetunit;
	
	@Column(name = "PMTNEARMETRO", length = 512)
	private String pmtnearmetro;
	
	@Column(name = "PMTNEARBANK", length = 512)
	private String pmtnearbank;
	
	@Column(name = "PMTLOCSORTCODE")
	private Integer pmtlocsortcode;
	
	@Column(name = "PMTCOMPLETIONSCORE", precision = 53, scale = 0)
	private Double pmtcompletionscore;
	
	@Column(name = "PMTUOTRFNUM")
	private Long pmtuotrfnum;
	
	@Column(name = "PMTPRICECATEGORY", length = 4)
	private String pmtpricecategory;
	
	@Column(name = "PMTCNDLISTTYPE")
	private Long pmtcndlisttype;
	
	@Column(name = "LOCRANKDATE", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date locrankdate;
	
	@Column(name = "PMTMODPRIORITY")
	private Integer pmtmodpriority;
	
	@Column(name = "PMTMODERTAG")
	private Long pmtmodertag;
	@Column(name = "PMTISPRICENEGBLE", length = 1)
	private String priceNegotiableFlag;
	@Column(name = "PMTAVAILAFTER", length = 26)
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmtavaildate;
	public Long getPmtrfnum() {
		return pmtrfnum;
	}
	public void setPmtrfnum(Long pmtrfnum) {
		this.pmtrfnum = pmtrfnum;
	}
	public Long getPmtcndbalconies() {
		return pmtcndbalconies;
	}
	public void setPmtcndbalconies(Long pmtcndbalconies) {
		this.pmtcndbalconies = pmtcndbalconies;
	}
	public Long getPmtcndcovunit() {
		return pmtcndcovunit;
	}
	public void setPmtcndcovunit(Long pmtcndcovunit) {
		this.pmtcndcovunit = pmtcndcovunit;
	}
	public Long getPmtcndbedrms() {
		return pmtcndbedrms;
	}
	public void setPmtcndbedrms(Long pmtcndbedrms) {
		this.pmtcndbedrms = pmtcndbedrms;
	}
	public Long getPmtpitrfnum() {
		return pmtpitrfnum;
	}
	public void setPmtpitrfnum(Long pmtpitrfnum) {
		this.pmtpitrfnum = pmtpitrfnum;
	}
	public Long getPmtcndtrantty() {
		return pmtcndtrantty;
	}
	public void setPmtcndtrantty(Long pmtcndtrantty) {
		this.pmtcndtrantty = pmtcndtrantty;
	}
	public Long getPmtlmtrfnum() {
		return pmtlmtrfnum;
	}
	public void setPmtlmtrfnum(Long pmtlmtrfnum) {
		this.pmtlmtrfnum = pmtlmtrfnum;
	}
	public Long getPmtcndplunit() {
		return pmtcndplunit;
	}
	public void setPmtcndplunit(Long pmtcndplunit) {
		this.pmtcndplunit = pmtcndplunit;
	}
	public Long getPmtcndtnof() {
		return pmtcndtnof;
	}
	public void setPmtcndtnof(Long pmtcndtnof) {
		this.pmtcndtnof = pmtcndtnof;
	}
	public Long getPmtcndproptype() {
		return pmtcndproptype;
	}
	public void setPmtcndproptype(Long pmtcndproptype) {
		this.pmtcndproptype = pmtcndproptype;
	}
	public Long getPmtcndaoc() {
		return pmtcndaoc;
	}
	public void setPmtcndaoc(Long pmtcndaoc) {
		this.pmtcndaoc = pmtcndaoc;
	}
	public Long getPmtubisuperuser() {
		return pmtubisuperuser;
	}
	public void setPmtubisuperuser(Long pmtubisuperuser) {
		this.pmtubisuperuser = pmtubisuperuser;
	}
	public Long getPmtcndfurnish() {
		return pmtcndfurnish;
	}
	public void setPmtcndfurnish(Long pmtcndfurnish) {
		this.pmtcndfurnish = pmtcndfurnish;
	}
	public Long getPmtcndtow() {
		return pmtcndtow;
	}
	public void setPmtcndtow(Long pmtcndtow) {
		this.pmtcndtow = pmtcndtow;
	}
	public Long getPmtcndbathrms() {
		return pmtcndbathrms;
	}
	public void setPmtcndbathrms(Long pmtcndbathrms) {
		this.pmtcndbathrms = pmtcndbathrms;
	}
	public Long getPmtmoderresult() {
		return pmtmoderresult;
	}
	public void setPmtmoderresult(Long pmtmoderresult) {
		this.pmtmoderresult = pmtmoderresult;
	}
	public Long getPmtcndfacing() {
		return pmtcndfacing;
	}
	public void setPmtcndfacing(Long pmtcndfacing) {
		this.pmtcndfacing = pmtcndfacing;
	}
	public Long getPmtubirfnum() {
		return pmtubirfnum;
	}
	public void setPmtubirfnum(Long pmtubirfnum) {
		this.pmtubirfnum = pmtubirfnum;
	}
	public Long getPmtcndfln() {
		return pmtcndfln;
	}
	public void setPmtcndfln(Long pmtcndfln) {
		this.pmtcndfln = pmtcndfln;
	}
	public String getPmtadid() {
		return pmtadid;
	}
	public void setPmtadid(String pmtadid) {
		this.pmtadid = pmtadid;
	}
	public String getPmtlisttype() {
		return pmtlisttype;
	}
	public void setPmtlisttype(String pmtlisttype) {
		this.pmtlisttype = pmtlisttype;
	}
	public Long getPmtsaleprice() {
		return pmtsaleprice;
	}
	public void setPmtsaleprice(Long pmtsaleprice) {
		this.pmtsaleprice = pmtsaleprice;
	}
	public Long getPmtrentprice() {
		return pmtrentprice;
	}
	public void setPmtrentprice(Long pmtrentprice) {
		this.pmtrentprice = pmtrentprice;
	}
	public String getPmtdues() {
		return pmtdues;
	}
	public void setPmtdues(String pmtdues) {
		this.pmtdues = pmtdues;
	}
	public Long getPmtcndavailunit() {
		return pmtcndavailunit;
	}
	public void setPmtcndavailunit(Long pmtcndavailunit) {
		this.pmtcndavailunit = pmtcndavailunit;
	}
	public String getPmtbriefdisc() {
		return pmtbriefdisc;
	}
	public void setPmtbriefdisc(String pmtbriefdisc) {
		this.pmtbriefdisc = pmtbriefdisc;
	}
	public String getPmtdetaildisc() {
		return pmtdetaildisc;
	}
	public void setPmtdetaildisc(String pmtdetaildisc) {
		this.pmtdetaildisc = pmtdetaildisc;
	}
	public String getPmtkeydisc() {
		return pmtkeydisc;
	}
	public void setPmtkeydisc(String pmtkeydisc) {
		this.pmtkeydisc = pmtkeydisc;
	}
	public String getPmttnc() {
		return pmttnc;
	}
	public void setPmttnc(String pmttnc) {
		this.pmttnc = pmttnc;
	}
	public String getPmtislisted() {
		return pmtislisted;
	}
	public void setPmtislisted(String pmtislisted) {
		this.pmtislisted = pmtislisted;
	}
	public String getPmtisstd() {
		return pmtisstd;
	}
	public void setPmtisstd(String pmtisstd) {
		this.pmtisstd = pmtisstd;
	}
	public String getPmtisactive() {
		return pmtisactive;
	}
	public void setPmtisactive(String pmtisactive) {
		this.pmtisactive = pmtisactive;
	}
	public String getPmtmoderstatus() {
		return pmtmoderstatus;
	}
	public void setPmtmoderstatus(String pmtmoderstatus) {
		this.pmtmoderstatus = pmtmoderstatus;
	}
	public String getPmtmodercomment() {
		return pmtmodercomment;
	}
	public void setPmtmodercomment(String pmtmodercomment) {
		this.pmtmodercomment = pmtmodercomment;
	}
	public Date getPmtstartdate() {
		return pmtstartdate;
	}
	public void setPmtstartdate(Date pmtstartdate) {
		this.pmtstartdate = pmtstartdate;
	}
	public Date getPmtenddate() {
		return pmtenddate;
	}
	public void setPmtenddate(Date pmtenddate) {
		this.pmtenddate = pmtenddate;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getModidate() {
		return modidate;
	}
	public void setModidate(Date modidate) {
		this.modidate = modidate;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public Long getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
	public Long getExfield1() {
		return exfield1;
	}
	public void setExfield1(Long exfield1) {
		this.exfield1 = exfield1;
	}
	public String getExfield2() {
		return exfield2;
	}
	public void setExfield2(String exfield2) {
		this.exfield2 = exfield2;
	}
	public String getPmtcovarea() {
		return pmtcovarea;
	}
	public void setPmtcovarea(String pmtcovarea) {
		this.pmtcovarea = pmtcovarea;
	}
	public String getPmtplarea() {
		return pmtplarea;
	}
	public void setPmtplarea(String pmtplarea) {
		this.pmtplarea = pmtplarea;
	}
	public String getPmtflrty() {
		return pmtflrty;
	}
	public void setPmtflrty(String pmtflrty) {
		this.pmtflrty = pmtflrty;
	}
	public String getPmtwoodwrk() {
		return pmtwoodwrk;
	}
	public void setPmtwoodwrk(String pmtwoodwrk) {
		this.pmtwoodwrk = pmtwoodwrk;
	}
	public String getPmtwallfin() {
		return pmtwallfin;
	}
	public void setPmtwallfin(String pmtwallfin) {
		this.pmtwallfin = pmtwallfin;
	}
	public String getPmttvphone() {
		return pmttvphone;
	}
	public void setPmttvphone(String pmttvphone) {
		this.pmttvphone = pmttvphone;
	}
	public String getPmtwatersup() {
		return pmtwatersup;
	}
	public void setPmtwatersup(String pmtwatersup) {
		this.pmtwatersup = pmtwatersup;
	}
	public String getPmtnearhos() {
		return pmtnearhos;
	}
	public void setPmtnearhos(String pmtnearhos) {
		this.pmtnearhos = pmtnearhos;
	}
	public String getPmtnearair() {
		return pmtnearair;
	}
	public void setPmtnearair(String pmtnearair) {
		this.pmtnearair = pmtnearair;
	}
	public String getPmtnearrail() {
		return pmtnearrail;
	}
	public void setPmtnearrail(String pmtnearrail) {
		this.pmtnearrail = pmtnearrail;
	}
	public String getPmtnearsch() {
		return pmtnearsch;
	}
	public void setPmtnearsch(String pmtnearsch) {
		this.pmtnearsch = pmtnearsch;
	}
	public String getPmtlmandnei() {
		return pmtlmandnei;
	}
	public void setPmtlmandnei(String pmtlmandnei) {
		this.pmtlmandnei = pmtlmandnei;
	}
	public String getPmtcitycen() {
		return pmtcitycen;
	}
	public void setPmtcitycen(String pmtcitycen) {
		this.pmtcitycen = pmtcitycen;
	}
	public Long getPmtnfviews() {
		return pmtnfviews;
	}
	public void setPmtnfviews(Long pmtnfviews) {
		this.pmtnfviews = pmtnfviews;
	}
	public String getPmtisvirtual() {
		return pmtisvirtual;
	}
	public void setPmtisvirtual(String pmtisvirtual) {
		this.pmtisvirtual = pmtisvirtual;
	}
	public String getPmtadcode() {
		return pmtadcode;
	}
	public void setPmtadcode(String pmtadcode) {
		this.pmtadcode = pmtadcode;
	}
	public Long getPmtunquser() {
		return pmtunquser;
	}
	public void setPmtunquser(Long pmtunquser) {
		this.pmtunquser = pmtunquser;
	}
	public Long getEditedby() {
		return editedby;
	}
	public void setEditedby(Long editedby) {
		this.editedby = editedby;
	}
	public Date getRankdate() {
		return rankdate;
	}
	public void setRankdate(Date rankdate) {
		this.rankdate = rankdate;
	}
	public String getPmtsource() {
		return pmtsource;
	}
	public void setPmtsource(String pmtsource) {
		this.pmtsource = pmtsource;
	}
	public Integer getContactsviewed() {
		return contactsviewed;
	}
	public void setContactsviewed(Integer contactsviewed) {
		this.contactsviewed = contactsviewed;
	}
	public Date getPmtresponsetime() {
		return pmtresponsetime;
	}
	public void setPmtresponsetime(Date pmtresponsetime) {
		this.pmtresponsetime = pmtresponsetime;
	}
	public String getPmtsapsalesorg() {
		return pmtsapsalesorg;
	}
	public void setPmtsapsalesorg(String pmtsapsalesorg) {
		this.pmtsapsalesorg = pmtsapsalesorg;
	}
	public String getPmtsapsalesoffice() {
		return pmtsapsalesoffice;
	}
	public void setPmtsapsalesoffice(String pmtsapsalesoffice) {
		this.pmtsapsalesoffice = pmtsapsalesoffice;
	}
	public Integer getPmtsortcode() {
		return pmtsortcode;
	}
	public void setPmtsortcode(Integer pmtsortcode) {
		this.pmtsortcode = pmtsortcode;
	}
	public String getPmtcndamenityfacing() {
		return pmtcndamenityfacing;
	}
	public void setPmtcndamenityfacing(String pmtcndamenityfacing) {
		this.pmtcndamenityfacing = pmtcndamenityfacing;
	}
	public Double getPmtcarpetarea() {
		return pmtcarpetarea;
	}
	public void setPmtcarpetarea(Double pmtcarpetarea) {
		this.pmtcarpetarea = pmtcarpetarea;
	}
	public Long getPmtcndcarpetunit() {
		return pmtcndcarpetunit;
	}
	public void setPmtcndcarpetunit(Long pmtcndcarpetunit) {
		this.pmtcndcarpetunit = pmtcndcarpetunit;
	}
	public String getPmtnearmetro() {
		return pmtnearmetro;
	}
	public void setPmtnearmetro(String pmtnearmetro) {
		this.pmtnearmetro = pmtnearmetro;
	}
	public String getPmtnearbank() {
		return pmtnearbank;
	}
	public void setPmtnearbank(String pmtnearbank) {
		this.pmtnearbank = pmtnearbank;
	}
	public Integer getPmtlocsortcode() {
		return pmtlocsortcode;
	}
	public void setPmtlocsortcode(Integer pmtlocsortcode) {
		this.pmtlocsortcode = pmtlocsortcode;
	}
	public Double getPmtcompletionscore() {
		return pmtcompletionscore;
	}
	public void setPmtcompletionscore(Double pmtcompletionscore) {
		this.pmtcompletionscore = pmtcompletionscore;
	}
	public Long getPmtuotrfnum() {
		return pmtuotrfnum;
	}
	public void setPmtuotrfnum(Long pmtuotrfnum) {
		this.pmtuotrfnum = pmtuotrfnum;
	}
	public String getPmtpricecategory() {
		return pmtpricecategory;
	}
	public void setPmtpricecategory(String pmtpricecategory) {
		this.pmtpricecategory = pmtpricecategory;
	}
	public Long getPmtcndlisttype() {
		return pmtcndlisttype;
	}
	public void setPmtcndlisttype(Long pmtcndlisttype) {
		this.pmtcndlisttype = pmtcndlisttype;
	}
	public Date getLocrankdate() {
		return locrankdate;
	}
	public void setLocrankdate(Date locrankdate) {
		this.locrankdate = locrankdate;
	}
	public Integer getPmtmodpriority() {
		return pmtmodpriority;
	}
	public void setPmtmodpriority(Integer pmtmodpriority) {
		this.pmtmodpriority = pmtmodpriority;
	}
	public Long getPmtmodertag() {
		return pmtmodertag;
	}
	public void setPmtmodertag(Long pmtmodertag) {
		this.pmtmodertag = pmtmodertag;
	}
	public String getPriceNegotiableFlag() {
		return priceNegotiableFlag;
	}
	public void setPriceNegotiableFlag(String priceNegotiableFlag) {
		this.priceNegotiableFlag = priceNegotiableFlag;
	}
	public Date getPmtavaildate() {
		return pmtavaildate;
	}
	public void setPmtavaildate(Date pmtavaildate) {
		this.pmtavaildate = pmtavaildate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
}