package com.mbbatch.domain;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tpprj entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TPPRJ", schema = "PROPERTY")
public class Tpprj implements java.io.Serializable {
	// Fields
	@Id
	@Column(name = "PRJRFNUM", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long prjrfnum;
	@Column(name = "PRJCNDCTRY")
	private Long prjcndctry;
	@Column(name = "PRJCNDST")
	private Long prjcndst;
	@Column(name = "PRJCNDCITY")
	private Long prjcndcity;
	@Column(name = "PRJUBIRFNUM")
	private Long prjubirfnum;
	@Column(name = "PRJBNAME")
	private String prjbname;
	@Column(name = "PRJBLOGO")
	private String prjblogo;
	@Column(name = "PRJADDRESS")
	private String prjaddress;
	@Column(name = "PRJMOBILE")
	private String prjmobile;
	@Column(name = "PRJPHONE")
	private String prjphone;
	@Column(name = "PRJEMAIL")
	private String prjemail;
	@Column(name = "PRJNAME")
	private String prjname;
	@Column(name = "PRJSOURCE")
	private Long prjsource;
	@Column(name = "PRJPPHOTO")
	private String prjpphoto;
	@Column(name = "PRJFLRPLANLNK")
	private String prjflrplanlnk;
	@Column(name = "PRJBROLNK")
	private String prjbrolnk;
	@Column(name = "PRJPROTYPE")
	private String prjprotype;
	@Column(name = "PRJPROJADD")
	private String prjprojadd;
	@Column(name = "PRJDESC")
	private String prjdesc;
	@Column(name = "PRJDEALDESC")
	private String prjdealdesc;
	@Column(name = "PRJLAUNCHDATE")
	private Date prjoffervaliddate;
	@Column(name = "PRJISACTIVE")
	private String prjisactive;
	@Column(name = "PRJREMARKS")
	private String prjremarks;
	@Column(name = "PRJLINK")
	private String prjlink;
	@Column(name = "EXFIELD1")
	private Long exfield1;
	@Column(name = "EXFIELD2")
	private Long exfield2;
	@Column(name = "EXFIELD3")
	private Long exfield3;
	@Column(name = "EXFIELD4")
	private Long exfield4;
	@Column(name = "EXFIELD5")
	private Long exfield5;
	@Column(name = "EXFIELD6")
	private String exfield6;
	@Column(name = "EXFIELD7")
	private String exfield7;
	@Column(name = "EXFIELD8")
	private String exfield8;
	@Column(name = "EXFIELD9")
	private String exfield9;
	@Column(name = "EXFIELD10")
	private String exfield10;
	@Column(name = "CREATEDATE", length = 26)
	private Date createdate;
	@Column(name = "MODIDATE", length = 26)
	private Date modidate;
	@Column(name = "DELETED")
	private String deleted;
	@Column(name = "CREATEDBY")
	private Long createdby;
	@Column(name = "ISBESTDEAL")
	private String isbestdeal;
	@Column(name = "CONTACTSVIEWED")
	private Integer contactsviewed;
	@Column(name = "PRJTEMPLRFNUM")
	private Long prjtemplrfnum;
	@Column(name = "PRJPOSSDATE")
	private Date possDate;
	@Column(name = "PRJDEVRFNUM")
	private Long developerId;
	@Column(name = "PRJNEARHOS")
    private String prjnearhos;
	@Column(name = "PRJNEARAIR")
	private String prjnearair;
	@Column(name = "PRJNEARRAIL")
	private String prjnearrail;
	@Column(name = "PRJNEARSCH")
	private String prjnearsch;
	@Column(name = "PRJCITYCEN")
	private String prjcitycen;
	@Column(name = "PRJNEARMETRO")
	private String prjnearmetro;
	@Column(name = "PRJNEARBANK")
	private String prjnearbank;
	@Column(name = "PRJMINUNITPRICE")
	private Long prjminunitprice;
	@Column(name = "PRJMAXUNITPRICE")
	private Long prjmaxunitprice;
	@Column(name = "PRJCNDPRICEUNIT")
	private Long prjcndpriceunit;
	@Column(name = "PRJDISPPRICE")
	private String prjdispprice;
	@Column(name = "PRJPROPTITLE")
	private String prjtitle;
	@Column(name = "VIDEOAVL")
	private String videoavl;
	@Column(name = "VIDEOLNK")
	private String videolnk;
	@Column(name = "PRJPRIORITY")
	private Integer prjpriority;
	@Column(name = "PRJPRIOSTDATE", length = 26)
	private Date prjpriostdate;
	@Column(name = "PRJPRIOENDDATE", length = 26)
	private Date prjprioenddate;
	@Column(name = "PRJAPPROVEDBY")
	private String prjapprovedby;
	@Column(name = "PRJISLUXURY")
	private String prjisluxury;
	@Column(name = "PRJLATITUDE")
	private Double prjlatitude;
	@Column(name = "PRJLONGITUDE")
	private Double prjlongitude;
	@Column(name = "PRJZOOMLEVEL")
	private Integer prjzoomlevel;
	@Column(name = "PRJCNDTHEMES")
	private String prjcndthemes;
	@Column(name = "PRJCNDLPHASE")
	private Long prjcndlphase;
	@Column(name = "PRJLISTTYPE")
	private String prjlisttype;
	@Column(name = "PRJSORTCODE")
	private Integer prjsortcode;
	@Column(name = "PRJPSMRFNUM")
	private Long prjpsmrfnum;
	@Column(name = "PRJISCOMPILED")
	private String prjiscompiled;
	@Column(name = "PRJREDIRECTTOMS")
	private String redirectToms;
	public Long getPrjrfnum() {
		return prjrfnum;
	}
	public Long getPrjcndctry() {
		return prjcndctry;
	}
	public Long getPrjcndst() {
		return prjcndst;
	}
	public Long getPrjcndcity() {
		return prjcndcity;
	}
	public Long getPrjubirfnum() {
		return prjubirfnum;
	}
	public String getPrjbname() {
		return prjbname;
	}
	public String getPrjblogo() {
		return prjblogo;
	}
	public String getPrjaddress() {
		return prjaddress;
	}
	public String getPrjmobile() {
		return prjmobile;
	}
	public String getPrjphone() {
		return prjphone;
	}
	public String getPrjemail() {
		return prjemail;
	}
	public String getPrjname() {
		return prjname;
	}
	public Long getPrjsource() {
		return prjsource;
	}
	public String getPrjpphoto() {
		return prjpphoto;
	}
	public String getPrjflrplanlnk() {
		return prjflrplanlnk;
	}
	public String getPrjbrolnk() {
		return prjbrolnk;
	}
	public String getPrjprotype() {
		return prjprotype;
	}
	public String getPrjprojadd() {
		return prjprojadd;
	}
	public String getPrjdesc() {
		return prjdesc;
	}
	public String getPrjdealdesc() {
		return prjdealdesc;
	}
	public Date getPrjoffervaliddate() {
		return prjoffervaliddate;
	}
	public String getPrjisactive() {
		return prjisactive;
	}
	public String getPrjremarks() {
		return prjremarks;
	}
	public String getPrjlink() {
		return prjlink;
	}
	public Long getExfield1() {
		return exfield1;
	}
	public Long getExfield2() {
		return exfield2;
	}
	public Long getExfield3() {
		return exfield3;
	}
	public Long getExfield4() {
		return exfield4;
	}
	public Long getExfield5() {
		return exfield5;
	}
	public String getExfield6() {
		return exfield6;
	}
	public String getExfield7() {
		return exfield7;
	}
	public String getExfield8() {
		return exfield8;
	}
	public String getExfield9() {
		return exfield9;
	}
	public String getExfield10() {
		return exfield10;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public Date getModidate() {
		return modidate;
	}
	public String getDeleted() {
		return deleted;
	}
	public Long getCreatedby() {
		return createdby;
	}
	public String getIsbestdeal() {
		return isbestdeal;
	}
	public Integer getContactsviewed() {
		return contactsviewed;
	}
	public Long getPrjtemplrfnum() {
		return prjtemplrfnum;
	}
	public Date getPossDate() {
		return possDate;
	}
	public Long getDeveloperId() {
		return developerId;
	}
	public String getPrjnearhos() {
		return prjnearhos;
	}
	public String getPrjnearair() {
		return prjnearair;
	}
	public String getPrjnearrail() {
		return prjnearrail;
	}
	public String getPrjnearsch() {
		return prjnearsch;
	}
	public String getPrjcitycen() {
		return prjcitycen;
	}
	public String getPrjnearmetro() {
		return prjnearmetro;
	}
	public String getPrjnearbank() {
		return prjnearbank;
	}
	public Long getPrjminunitprice() {
		return prjminunitprice;
	}
	public Long getPrjmaxunitprice() {
		return prjmaxunitprice;
	}
	public Long getPrjcndpriceunit() {
		return prjcndpriceunit;
	}
	public String getPrjdispprice() {
		return prjdispprice;
	}
	public String getPrjtitle() {
		return prjtitle;
	}
	public String getVideoavl() {
		return videoavl;
	}
	public String getVideolnk() {
		return videolnk;
	}
	public Integer getPrjpriority() {
		return prjpriority;
	}
	public Date getPrjpriostdate() {
		return prjpriostdate;
	}
	public Date getPrjprioenddate() {
		return prjprioenddate;
	}
	public String getPrjapprovedby() {
		return prjapprovedby;
	}
	public String getPrjisluxury() {
		return prjisluxury;
	}
	public Double getPrjlatitude() {
		return prjlatitude;
	}
	public Double getPrjlongitude() {
		return prjlongitude;
	}
	public Integer getPrjzoomlevel() {
		return prjzoomlevel;
	}
	public String getPrjcndthemes() {
		return prjcndthemes;
	}
	public Long getPrjcndlphase() {
		return prjcndlphase;
	}
	public String getPrjlisttype() {
		return prjlisttype;
	}
	public Integer getPrjsortcode() {
		return prjsortcode;
	}
	public Long getPrjpsmrfnum() {
		return prjpsmrfnum;
	}
	public String getPrjiscompiled() {
		return prjiscompiled;
	}
	public String getRedirectToms() {
		return redirectToms;
	}
	public void setPrjrfnum(Long prjrfnum) {
		this.prjrfnum = prjrfnum;
	}
	public void setPrjcndctry(Long prjcndctry) {
		this.prjcndctry = prjcndctry;
	}
	public void setPrjcndst(Long prjcndst) {
		this.prjcndst = prjcndst;
	}
	public void setPrjcndcity(Long prjcndcity) {
		this.prjcndcity = prjcndcity;
	}
	public void setPrjubirfnum(Long prjubirfnum) {
		this.prjubirfnum = prjubirfnum;
	}
	public void setPrjbname(String prjbname) {
		this.prjbname = prjbname;
	}
	public void setPrjblogo(String prjblogo) {
		this.prjblogo = prjblogo;
	}
	public void setPrjaddress(String prjaddress) {
		this.prjaddress = prjaddress;
	}
	public void setPrjmobile(String prjmobile) {
		this.prjmobile = prjmobile;
	}
	public void setPrjphone(String prjphone) {
		this.prjphone = prjphone;
	}
	public void setPrjemail(String prjemail) {
		this.prjemail = prjemail;
	}
	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}
	public void setPrjsource(Long prjsource) {
		this.prjsource = prjsource;
	}
	public void setPrjpphoto(String prjpphoto) {
		this.prjpphoto = prjpphoto;
	}
	public void setPrjflrplanlnk(String prjflrplanlnk) {
		this.prjflrplanlnk = prjflrplanlnk;
	}
	public void setPrjbrolnk(String prjbrolnk) {
		this.prjbrolnk = prjbrolnk;
	}
	public void setPrjprotype(String prjprotype) {
		this.prjprotype = prjprotype;
	}
	public void setPrjprojadd(String prjprojadd) {
		this.prjprojadd = prjprojadd;
	}
	public void setPrjdesc(String prjdesc) {
		this.prjdesc = prjdesc;
	}
	public void setPrjdealdesc(String prjdealdesc) {
		this.prjdealdesc = prjdealdesc;
	}
	public void setPrjoffervaliddate(Date prjoffervaliddate) {
		this.prjoffervaliddate = prjoffervaliddate;
	}
	public void setPrjisactive(String prjisactive) {
		this.prjisactive = prjisactive;
	}
	public void setPrjremarks(String prjremarks) {
		this.prjremarks = prjremarks;
	}
	public void setPrjlink(String prjlink) {
		this.prjlink = prjlink;
	}
	public void setExfield1(Long exfield1) {
		this.exfield1 = exfield1;
	}
	public void setExfield2(Long exfield2) {
		this.exfield2 = exfield2;
	}
	public void setExfield3(Long exfield3) {
		this.exfield3 = exfield3;
	}
	public void setExfield4(Long exfield4) {
		this.exfield4 = exfield4;
	}
	public void setExfield5(Long exfield5) {
		this.exfield5 = exfield5;
	}
	public void setExfield6(String exfield6) {
		this.exfield6 = exfield6;
	}
	public void setExfield7(String exfield7) {
		this.exfield7 = exfield7;
	}
	public void setExfield8(String exfield8) {
		this.exfield8 = exfield8;
	}
	public void setExfield9(String exfield9) {
		this.exfield9 = exfield9;
	}
	public void setExfield10(String exfield10) {
		this.exfield10 = exfield10;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public void setModidate(Date modidate) {
		this.modidate = modidate;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
	public void setIsbestdeal(String isbestdeal) {
		this.isbestdeal = isbestdeal;
	}
	public void setContactsviewed(Integer contactsviewed) {
		this.contactsviewed = contactsviewed;
	}
	public void setPrjtemplrfnum(Long prjtemplrfnum) {
		this.prjtemplrfnum = prjtemplrfnum;
	}
	public void setPossDate(Date possDate) {
		this.possDate = possDate;
	}
	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}
	public void setPrjnearhos(String prjnearhos) {
		this.prjnearhos = prjnearhos;
	}
	public void setPrjnearair(String prjnearair) {
		this.prjnearair = prjnearair;
	}
	public void setPrjnearrail(String prjnearrail) {
		this.prjnearrail = prjnearrail;
	}
	public void setPrjnearsch(String prjnearsch) {
		this.prjnearsch = prjnearsch;
	}
	public void setPrjcitycen(String prjcitycen) {
		this.prjcitycen = prjcitycen;
	}
	public void setPrjnearmetro(String prjnearmetro) {
		this.prjnearmetro = prjnearmetro;
	}
	public void setPrjnearbank(String prjnearbank) {
		this.prjnearbank = prjnearbank;
	}
	public void setPrjminunitprice(Long prjminunitprice) {
		this.prjminunitprice = prjminunitprice;
	}
	public void setPrjmaxunitprice(Long prjmaxunitprice) {
		this.prjmaxunitprice = prjmaxunitprice;
	}
	public void setPrjcndpriceunit(Long prjcndpriceunit) {
		this.prjcndpriceunit = prjcndpriceunit;
	}
	public void setPrjdispprice(String prjdispprice) {
		this.prjdispprice = prjdispprice;
	}
	public void setPrjtitle(String prjtitle) {
		this.prjtitle = prjtitle;
	}
	public void setVideoavl(String videoavl) {
		this.videoavl = videoavl;
	}
	public void setVideolnk(String videolnk) {
		this.videolnk = videolnk;
	}
	public void setPrjpriority(Integer prjpriority) {
		this.prjpriority = prjpriority;
	}
	public void setPrjpriostdate(Date prjpriostdate) {
		this.prjpriostdate = prjpriostdate;
	}
	public void setPrjprioenddate(Date prjprioenddate) {
		this.prjprioenddate = prjprioenddate;
	}
	public void setPrjapprovedby(String prjapprovedby) {
		this.prjapprovedby = prjapprovedby;
	}
	public void setPrjisluxury(String prjisluxury) {
		this.prjisluxury = prjisluxury;
	}
	public void setPrjlatitude(Double prjlatitude) {
		this.prjlatitude = prjlatitude;
	}
	public void setPrjlongitude(Double prjlongitude) {
		this.prjlongitude = prjlongitude;
	}
	public void setPrjzoomlevel(Integer prjzoomlevel) {
		this.prjzoomlevel = prjzoomlevel;
	}
	public void setPrjcndthemes(String prjcndthemes) {
		this.prjcndthemes = prjcndthemes;
	}
	public void setPrjcndlphase(Long prjcndlphase) {
		this.prjcndlphase = prjcndlphase;
	}
	public void setPrjlisttype(String prjlisttype) {
		this.prjlisttype = prjlisttype;
	}
	public void setPrjsortcode(Integer prjsortcode) {
		this.prjsortcode = prjsortcode;
	}
	public void setPrjpsmrfnum(Long prjpsmrfnum) {
		this.prjpsmrfnum = prjpsmrfnum;
	}
	public void setPrjiscompiled(String prjiscompiled) {
		this.prjiscompiled = prjiscompiled;
	}
	public void setRedirectToms(String redirectToms) {
		this.redirectToms = redirectToms;
	}

	/*private String[] amenityInt;
	
	private String[] amenityExt;*/
	
}