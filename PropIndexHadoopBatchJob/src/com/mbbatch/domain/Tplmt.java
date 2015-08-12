package com.mbbatch.domain;

import java.util.Date;

/**
 * Tplmt generated by MyEclipse Persistence Tools
 */

public class Tplmt implements java.io.Serializable {

	// Fields

	private Long lmtrfnum;
	private Long lmtcndzip;
	private Long lmtcndstate;
	private Long lmtcndcountry;
	private Long lmtcndcity;
	private String lmtname;
	private String lmtdisc;
	private Date createdate;
	private Date modidate;
	private String deleted;
	private Long createdby;
	private Long exfield1;
	private String exfield2;
	private String lmtisactive;
	private Double longitude;
	private Double latitude;
	private String lmtnearbyrfnum;
	private String lmtalias1;
	private String lmtalias2;
	private String lmtpdfavail;
	private Date lmtpdfmodidate;
	private Long lmtlmtrfnum;
	private String lmtrestrict;
	private Long lmtpopular;
	private String lmtdispname;
	private String lmtadvice;
	private String lmtiswap;
	// Constructors

	public String getLmtnearbyrfnum() {
		return lmtnearbyrfnum;
	}

	public void setLmtnearbyrfnum(String lmtnearbyrfnum) {
		this.lmtnearbyrfnum = lmtnearbyrfnum;
	}

	/** default constructor */
	public Tplmt() {
	}

	/** minimal constructor */
	public Tplmt(Long lmtrfnum, Long lmtcndzip,
			Long lmtcndstate, Long lmtcndcountry,
			Long lmtcndcity, String lmtname, Date createdate,
			Date modidate, String deleted, Long createdby, String lmtisactive,String lmtdispname,String lmtadvice) {
		this.lmtrfnum = lmtrfnum;
		this.lmtcndzip = lmtcndzip;
		this.lmtcndstate = lmtcndstate;
		this.lmtcndcountry = lmtcndcountry;
		this.lmtcndcity = lmtcndcity;
		this.lmtname = lmtname;
		this.createdate = createdate;
		this.modidate = modidate;
		this.deleted = deleted;
		this.createdby = createdby;
		this.lmtisactive = lmtisactive;
		this.lmtdispname=lmtdispname;
		this.lmtadvice=lmtadvice;
	}

	/** full constructor */
	public Tplmt(Long lmtrfnum, Long lmtcndzip,
			Long lmtcndstate, Long lmtcndcountry,
			Long lmtcndcity, String lmtname, String lmtdisc,
			Date createdate, Date modidate, String deleted, Long createdby,
			Long exfield1, String exfield2, String lmtisactive,Double longitude,Double latitude,String lmtnearbyrfnum,String lmtdispname,String lmtadvice) {
		this.lmtrfnum = lmtrfnum;
		this.lmtcndzip = lmtcndzip;
		this.lmtcndstate = lmtcndstate;
		this.lmtcndcountry = lmtcndcountry;
		this.lmtcndcity = lmtcndcity;
		this.lmtname = lmtname;
		this.lmtdisc = lmtdisc;
		this.createdate = createdate;
		this.modidate = modidate;
		this.deleted = deleted;
		this.createdby = createdby;
		this.exfield1 = exfield1;
		this.exfield2 = exfield2;
		this.lmtisactive = lmtisactive;
		this.longitude=longitude;
		this.latitude=latitude;
		this.lmtnearbyrfnum=lmtnearbyrfnum;
		this.lmtdispname=lmtdispname;
		this.lmtadvice=lmtadvice;
	}

	// Property accessors

	public Long getLmtrfnum() {
		return this.lmtrfnum;
	}

	public void setLmtrfnum(Long lmtrfnum) {
		this.lmtrfnum = lmtrfnum;
	}


	public String getLmtname() {
		return this.lmtname;
	}

	public void setLmtname(String lmtname) {
		this.lmtname = lmtname;
	}

	public String getLmtdisc() {
		return this.lmtdisc;
	}

	public void setLmtdisc(String lmtdisc) {
		this.lmtdisc = lmtdisc;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModidate() {
		return this.modidate;
	}

	public void setModidate(Date modidate) {
		this.modidate = modidate;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Long getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}

	public Long getExfield1() {
		return this.exfield1;
	}

	public void setExfield1(Long exfield1) {
		this.exfield1 = exfield1;
	}

	public String getExfield2() {
		return this.exfield2;
	}

	public void setExfield2(String exfield2) {
		this.exfield2 = exfield2;
	}

	public String getLmtisactive() {
		return this.lmtisactive;
	}

	public void setLmtisactive(String lmtisactive) {
		this.lmtisactive = lmtisactive;
	}

	public Long getLmtcndzip() {
		return lmtcndzip;
	}

	public void setLmtcndzip(Long lmtcndzip) {
		this.lmtcndzip = lmtcndzip;
	}

	public Long getLmtcndstate() {
		return lmtcndstate;
	}

	public void setLmtcndstate(Long lmtcndstate) {
		this.lmtcndstate = lmtcndstate;
	}

	public Long getLmtcndcountry() {
		return lmtcndcountry;
	}

	public void setLmtcndcountry(Long lmtcndcountry) {
		this.lmtcndcountry = lmtcndcountry;
	}

	public Long getLmtcndcity() {
		return lmtcndcity;
	}

	public void setLmtcndcity(Long lmtcndcity) {
		this.lmtcndcity = lmtcndcity;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLmtalias1() {
		return lmtalias1;
	}

	public void setLmtalias1(String lmtalias1) {
		this.lmtalias1 = lmtalias1;
	}

	public String getLmtalias2() {
		return lmtalias2;
	}

	public void setLmtalias2(String lmtalias2) {
		this.lmtalias2 = lmtalias2;
	}

	public String getLmtpdfavail() {
		return lmtpdfavail;
	}

	public void setLmtpdfavail(String lmtpdfavail) {
		this.lmtpdfavail = lmtpdfavail;
	}

	public Date getLmtpdfmodidate() {
		return lmtpdfmodidate;
	}

	public void setLmtpdfmodidate(Date lmtpdfmodidate) {
		this.lmtpdfmodidate = lmtpdfmodidate;
	}

	public Long getLmtlmtrfnum() {
		return lmtlmtrfnum;
	}

	public void setLmtlmtrfnum(Long lmtlmtrfnum) {
		this.lmtlmtrfnum = lmtlmtrfnum;
	}

	/**
	 * @return the lmtrestrict
	 */
	public String getLmtrestrict() {
		return lmtrestrict;
	}

	/**
	 * @param lmtrestrict the lmtrestrict to set
	 */
	public void setLmtrestrict(String lmtrestrict) {
		this.lmtrestrict = lmtrestrict;
	}

	public Long getLmtpopular() {
		return lmtpopular;
	}

	public void setLmtpopular(Long lmtpopular) {
		this.lmtpopular = lmtpopular;
	}

	public String getLmtdispname() {
		return lmtdispname;
	}

	public void setLmtdispname(String lmtdispname) {
		this.lmtdispname = lmtdispname;
	}

	public String getLmtadvice() {
		return lmtadvice;
	}

	public void setLmtadvice(String lmtadvice) {
		this.lmtadvice = lmtadvice;
	}

	public String getLmtiswap() {
		return lmtiswap;
	}

	public void setLmtiswap(String lmtiswap) {
		this.lmtiswap = lmtiswap;
	}
	
}