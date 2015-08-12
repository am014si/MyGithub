package com.mbbatch.domain;

import java.io.Serializable;
import java.util.Date;

public class Tpcat implements Serializable {

    private Long catpmtrfnum;
    private String catadd1;
    private String catadd2;
    private String cathowtoreach;
    private String catisvisible;
    private String catlandmarinfo;
    private String catzipother;
    private Date createdate;
    private Long createdby;
    private String isentered;
    private String isdisplaydev;
    private String isprojectorsociety;
    private String deleted;
    private Long exfield1;
    private String exfield2;
    private Date modidate;
    private Long catcndcountry;
    private Long catcndcity;
    private Long catcndstate;
    private Long catcndzip;
    private Long catsubloclmtrfnum;
    private String catpincode;
    private String catsocietyname;
    private String catcndaddroom;
    private Long catcndpossstatus;
    private String  catvideolink;
    private Double catlatitude;
    private Double catlongitude;
    private Integer catzoomlevel;
    private String catdealdesc;
    private Date catoffervaliddate;
  //Added fields for Verified Property
  	private String catisverified;
  	private Date catVerifiedDate;
  	private String catVerificationComments;
  	private Integer catqualityscore;
  	private String catisduplicate;
  	private Long catpsmrfnum;
  	private Long catdownpayment;
  	
	public Long getCatdownpayment() {
		return catdownpayment;
	}

	public void setCatdownpayment(Long catdownpayment) {
		this.catdownpayment = catdownpayment;
	}

	public Long getCatpsmrfnum() {
		return catpsmrfnum;
	}

	public void setCatpsmrfnum(Long catpsmrfnum) {
		this.catpsmrfnum = catpsmrfnum;
	}

	public String getCatisduplicate() {
		return catisduplicate;
	}

	public void setCatisduplicate(String catisduplicate) {
		this.catisduplicate = catisduplicate;
	}

	public Integer getCatqualityscore() {
		return catqualityscore;
	}

	public void setCatqualityscore(Integer catqualityscore) {
		this.catqualityscore = catqualityscore;
	}

	public String getCatisverified() {
		return catisverified;
	}

	public void setCatisverified(String catisverified) {
		this.catisverified = catisverified;
	}

	public Date getCatVerifiedDate() {
		return catVerifiedDate;
	}

	public void setCatVerifiedDate(Date catVerifiedDate) {
		this.catVerifiedDate = catVerifiedDate;
	}

	public String getCatVerificationComments() {
		return catVerificationComments;
	}

	public void setCatVerificationComments(String catVerificationComments) {
		this.catVerificationComments = catVerificationComments;
	}

	public Integer getCatzoomlevel() {
		return catzoomlevel;
	}

	public void setCatzoomlevel(Integer catzoomlevel) {
		this.catzoomlevel = catzoomlevel;
	}

	// default cons
    public Tpcat() {
    }

    // full cons
    public Tpcat(Long catpmtrfnum, String catadd1, String catadd2,
			String cathowtoreach, String catisvisible, String catlandmarinfo,
			String catzipother, Date createdate, Long createdby,
			String deleted, Long exfield1, String exfield2, Date modidate,
			Long catcndcountry, Long catcndcity,
			Long catcndstate, Long catcndzip,String catpincode ,String catsocietyname,
			String catcndaddroom,Long catcndpossstatus,String catvideolink,Double catlatitude,Double catlongitude, String catdealdesc, Date catoffervaliddate) {
		super();
		this.catpmtrfnum = catpmtrfnum;
		this.catadd1 = catadd1;
		this.catadd2 = catadd2;
		this.cathowtoreach = cathowtoreach;
		this.catisvisible = catisvisible;
		this.catlandmarinfo = catlandmarinfo;
		this.catzipother = catzipother;
		this.createdate = createdate;
		this.createdby = createdby;
		this.deleted = deleted;
		this.exfield1 = exfield1;
		this.exfield2 = exfield2;
		this.modidate = modidate;
		this.catcndcountry = catcndcountry;
		this.catcndcity = catcndcity;
		this.catcndstate = catcndstate;
		this.catcndzip = catcndzip;
		this.catpincode = catpincode;
		this.catsocietyname = catsocietyname;
		this.catcndaddroom = catcndaddroom;
		this.catcndpossstatus = catcndpossstatus;
		this.catvideolink = catvideolink;
		this.catlatitude = catlatitude;
		this.catlongitude = catlongitude;
		this.catdealdesc = catdealdesc;
		this.catdealdesc = catdealdesc;
		this.catoffervaliddate = catoffervaliddate;
	}


    
	public Tpcat(Long catpmtrfnum, String catadd1, String catadd2,
			String cathowtoreach, String catisvisible, String catlandmarinfo,
			String catzipother, Date createdate, Long createdby,
			String deleted, Long exfield1, String exfield2, Date modidate,
			Long catcndcountry, Long catcndcity,
			Long catcndstate, Long catcndzip, String catdealdesc, Date catoffervaliddate) {
		super();
		this.catpmtrfnum = catpmtrfnum;
		this.catadd1 = catadd1;
		this.catadd2 = catadd2;
		this.cathowtoreach = cathowtoreach;
		this.catisvisible = catisvisible;
		this.catlandmarinfo = catlandmarinfo;
		this.catzipother = catzipother;
		this.createdate = createdate;
		this.createdby = createdby;
		this.deleted = deleted;
		this.exfield1 = exfield1;
		this.exfield2 = exfield2;
		this.modidate = modidate;
		this.catcndcountry = catcndcountry;
		this.catcndcity = catcndcity;
		this.catcndstate = catcndstate;
		this.catcndzip = catcndzip;
		this.catdealdesc = catdealdesc;
		this.catoffervaliddate = catoffervaliddate;
	}


	public Long getCatpmtrfnum() {
		return catpmtrfnum;
	}


	public void setCatpmtrfnum(Long catpmtrfnum) {
		this.catpmtrfnum = catpmtrfnum;
	}


	public String getCatadd1() {
		return catadd1;
	}


	public void setCatadd1(String catadd1) {
		this.catadd1 = catadd1;
	}


	public String getCatadd2() {
		return catadd2;
	}


	public void setCatadd2(String catadd2) {
		this.catadd2 = catadd2;
	}


	public String getCathowtoreach() {
		return cathowtoreach;
	}


	public void setCathowtoreach(String cathowtoreach) {
		this.cathowtoreach = cathowtoreach;
	}


	public String getCatisvisible() {
		return catisvisible;
	}


	public void setCatisvisible(String catisvisible) {
		this.catisvisible = catisvisible;
	}


	public String getCatlandmarinfo() {
		return catlandmarinfo;
	}


	public void setCatlandmarinfo(String catlandmarinfo) {
		this.catlandmarinfo = catlandmarinfo;
	}


	public String getCatzipother() {
		return catzipother;
	}


	public void setCatzipother(String catzipother) {
		this.catzipother = catzipother;
	}


	public Date getCreatedate() {
		return createdate;
	}


	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	public Long getCreatedby() {
		return createdby;
	}


	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}


	public String getDeleted() {
		return deleted;
	}


	public void setDeleted(String deleted) {
		this.deleted = deleted;
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


	public Date getModidate() {
		return modidate;
	}


	public void setModidate(Date modidate) {
		this.modidate = modidate;
	}

	public Long getCatcndcountry() {
		return catcndcountry;
	}

	public void setCatcndcountry(Long catcndcountry) {
		this.catcndcountry = catcndcountry;
	}

	public Long getCatcndcity() {
		return catcndcity;
	}

	public void setCatcndcity(Long catcndcity) {
		this.catcndcity = catcndcity;
	}

	public Long getCatcndstate() {
		return catcndstate;
	}

	public void setCatcndstate(Long catcndstate) {
		this.catcndstate = catcndstate;
	}

	public Long getCatcndzip() {
		return catcndzip;
	}

	public void setCatcndzip(Long catcndzip) {
		this.catcndzip = catcndzip;
	}

	/**
	 * @return the catpincode
	 */
	public String getCatpincode() {
		return catpincode;
	}

	/**
	 * @param catpincode the catpincode to set
	 */
	public void setCatpincode(String catpincode) {
		this.catpincode = catpincode;
	}

	/**
	 * @return the catsocietyname
	 */
	public String getCatsocietyname() {
		return catsocietyname;
	}

	/**
	 * @param catsocietyname the catsocietyname to set
	 */
	public void setCatsocietyname(String catsocietyname) {
		this.catsocietyname = catsocietyname;
	}

	/**
	 * @return the catcndaddroom
	 */
	public String getCatcndaddroom() {
		return catcndaddroom;
	}

	/**
	 * @param catcndaddroom the catcndaddroom to set
	 */
	public void setCatcndaddroom(String catcndaddroom) {
		this.catcndaddroom = catcndaddroom;
	}

	/**
	 * @return the catcndpossstatus
	 */
	public Long getCatcndpossstatus() {
		return catcndpossstatus;
	}

	/**
	 * @param catcndpossstatus the catcndpossstatus to set
	 */
	public void setCatcndpossstatus(Long catcndpossstatus) {
		this.catcndpossstatus = catcndpossstatus;
	}

	public String getCatvideolink() {
		return catvideolink;
	}

	public void setCatvideolink(String catvideolink) {
		this.catvideolink = catvideolink;
	}

	public Double getCatlatitude() {
		return catlatitude;
	}

	public void setCatlatitude(Double catlatitude) {
		this.catlatitude = catlatitude;
	}

	public Double getCatlongitude() {
		return catlongitude;
	}

	public void setCatlongitude(Double catlongitude) {
		this.catlongitude = catlongitude;
	}

	public String getIsentered() {
		return isentered;
	}

	public void setIsentered(String isentered) {
		this.isentered = isentered;
	}

	public String getIsdisplaydev() {
		return isdisplaydev;
	}

	public void setIsdisplaydev(String isdisplaydev) {
		this.isdisplaydev = isdisplaydev;
	}

	public String getIsprojectorsociety() {
		return isprojectorsociety;
	}

	public void setIsprojectorsociety(String isprojectorsociety) {
		this.isprojectorsociety = isprojectorsociety;
	}

	public Long getCatsubloclmtrfnum() {
		return catsubloclmtrfnum;
	}

	public void setCatsubloclmtrfnum(Long catsubloclmtrfnum) {
		this.catsubloclmtrfnum = catsubloclmtrfnum;
	}
	
	public String getCatdealdesc() {
		return catdealdesc;
	}

	public void setCatdealdesc(String catdealdesc) {
		this.catdealdesc = catdealdesc;
	}

	public Date getCatoffervaliddate() {
		return catoffervaliddate;
	}

	public void setCatoffervaliddate(Date catoffervaliddate) {
		this.catoffervaliddate = catoffervaliddate;
	}
}
