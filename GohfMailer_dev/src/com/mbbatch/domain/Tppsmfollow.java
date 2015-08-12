package com.mbbatch.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TPPSMFOLLOW")
public class Tppsmfollow implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PSMFRFNUM")
	private Long psmfrfnum;
	
	@Column(name="PSMFPSMRFNUM")
	private Long psmfpsmrfnum;

	@Column(name="PSMFNAME")
	private String psmfname;
	
	@Column(name="PSMFEMAIL")
	private String psmfemail;

	@Column(name="PSMFMOBILE")
	private Long psmfmobile;

	@Column(name="PSMFISACTIVE")
	private String psmfisactive;

	@Column(name="CREATEDATE")
	private Date createdate;

	@Column(name="MODIDATE")
	private Date modidate;

	@Column(name="EXFIELD1")
	private Long exfield1;

	@Column(name="EXFIELD2")
	private String exfield2;

	@Column(name="EXFIELD3")
	private Long exfield3;

	@Column(name="EXFIELD4")
	private String exfield4;

	@Column(name="PSMLFDATE")
	private Date psmlfdate;

	@Column(name="PSMFISD")
	private Long psmfisd;


	public Long getPsmfrfnum() {
		return psmfrfnum;
	}

	public void setPsmfrfnum(Long psmfrfnum) {
		this.psmfrfnum = psmfrfnum;
	}

	@Override
	public String toString() {
		return "Tppsmfollow [createdate=" + createdate + ", exfield1="
				+ exfield1 + ", exfield2=" + exfield2 + ", exfield3="
				+ exfield3 + ", exfield4=" + exfield4 + ", modidate="
				+ modidate + ", psmfemail=" + psmfemail + ", psmfisactive="
				+ psmfisactive + ", psmfisd=" + psmfisd + ", psmfmobile="
				+ psmfmobile + ", psmfname=" + psmfname + ", psmfpsmrfnum="
				+ psmfpsmrfnum + ", psmfrfnum=" + psmfrfnum + ", psmlfdate="
				+ psmlfdate + "]";
	}

	public Long getPsmfpsmrfnum() {
		return psmfpsmrfnum;
	}

	public void setPsmfpsmrfnum(Long psmfpsmrfnum) {
		this.psmfpsmrfnum = psmfpsmrfnum;
	}

	public String getPsmfname() {
		return psmfname;
	}

	public void setPsmfname(String psmfname) {
		this.psmfname = psmfname;
	}

	public String getPsmfemail() {
		return psmfemail;
	}

	public void setPsmfemail(String psmfemail) {
		this.psmfemail = psmfemail;
	}

	public Long getPsmfmobile() {
		return psmfmobile;
	}

	public void setPsmfmobile(Long psmfmobile) {
		this.psmfmobile = psmfmobile;
	}

	public String getPsmfisactive() {
		return psmfisactive;
	}

	public void setPsmfisactive(String psmfisactive) {
		this.psmfisactive = psmfisactive;
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

	public Long getExfield3() {
		return exfield3;
	}

	public void setExfield3(Long exfield3) {
		this.exfield3 = exfield3;
	}

	public String getExfield4() {
		return exfield4;
	}

	public void setExfield4(String exfield4) {
		this.exfield4 = exfield4;
	}

	public Date getPsmlfdate() {
		return psmlfdate;
	}

	public void setPsmlfdate(Date psmlfdate) {
		this.psmlfdate = psmlfdate;
	}

	public void setPsmfisd(Long psmfisd) {
		this.psmfisd = psmfisd;
	}

	public Long getPsmfisd() {
		return psmfisd;
	}

}