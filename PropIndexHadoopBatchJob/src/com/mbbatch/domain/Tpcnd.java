package com.mbbatch.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        Master Code and Description Table. Values within a group are sorted based on the sequence number.
 *     
*/
public class Tpcnd implements Serializable {

    /** identifier field */
    private Long cndrfnum;

    /** persistent field */
    private String cndcode;

    /** persistent field */
    private String cnddesc;
    
    private Long cndcndrfnum;

    /** nullable persistent field */
    private String cndfieldtype;

    /** persistent field */
    private String cndgroup;

    /** persistent field */
    private Double cndseqnum;

    /** persistent field */
    private Date createdate;

    /** persistent field */
    private Long createdby;

    /** persistent field */
    private String deleted;

    /** nullable persistent field */
    private Long exfield1;

    /** nullable persistent field */
    private String exfield2;

    /** persistent field */
    private String isactive;

    /** persistent field */
    private Date modidate;
    
    // It is transient not available after querying
    //
    //private List<Tppoilockey> pois =new ArrayList<Tppoilockey>();

    /** persistent field */

    /** full constructor */
    public Tpcnd(Long cndrfnum, String cndcode, String cnddesc, String cndfieldtype, String cndgroup, Double cndseqnum, Date createdate, Long createdby, String deleted, Long exfield1, String exfield2, String isactive, Date modidate) {
        this.cndrfnum = cndrfnum;
        this.cndcode = cndcode;
        this.cnddesc = cnddesc;
        this.cndfieldtype = cndfieldtype;
        this.cndgroup = cndgroup;
        this.cndseqnum = cndseqnum;
        this.createdate = createdate;
        this.createdby = createdby;
        this.deleted = deleted;
        this.exfield1 = exfield1;
        this.exfield2 = exfield2;
        this.isactive = isactive;
        this.modidate = modidate;
    }

    /** default constructor */
    public Tpcnd() {
    }

    /** minimal constructor */
    public Tpcnd(Long cndrfnum, String cndcode, String cnddesc, String cndgroup, Double cndseqnum, Date createdate, Long createdby, String deleted, String isactive, Date modidate) {
        this.cndrfnum = cndrfnum;
        this.cndcode = cndcode;
        this.cnddesc = cnddesc;
        this.cndgroup = cndgroup;
        this.cndseqnum = cndseqnum;
        this.createdate = createdate;
        this.createdby = createdby;
        this.deleted = deleted;
        this.isactive = isactive;
        this.modidate = modidate;
    }
    
    public Tpcnd(Long cndrfnum, String cnddesc,String cndcode, String exfield2) {
        this.cndrfnum = cndrfnum;
        this.cnddesc = cnddesc;
        this.cndcode = (cndcode!= null) ?cndcode.trim() : cndcode;
        this.exfield2 = exfield2;
    }
    /** 
     * 		       this will contain the reference number
     * 		    
     */
    public Long getCndrfnum() {
        return this.cndrfnum;
    }

    public void setCndrfnum(Long cndrfnum) {
        this.cndrfnum = cndrfnum;
    }

    public String getCndcode() {
        return this.cndcode;
    }

    public void setCndcode(String cndcode) {
        this.cndcode = cndcode;
    }

    public String getCnddesc() {
        return this.cnddesc;
    }

    public void setCnddesc(String cnddesc) {
        this.cnddesc = cnddesc;
    }

    public String getCndfieldtype() {
        return this.cndfieldtype;
    }

    public void setCndfieldtype(String cndfieldtype) {
        this.cndfieldtype = cndfieldtype;
    }

    public String getCndgroup() {
        return this.cndgroup;
    }

    public void setCndgroup(String cndgroup) {
        this.cndgroup = cndgroup;
    }

    public Double getCndseqnum() {
        return this.cndseqnum;
    }

    public void setCndseqnum(Double cndseqnum) {
        this.cndseqnum = cndseqnum;
    }

    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Long getCreatedby() {
        return this.createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public String getDeleted() {
        return this.deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public String getIsactive() {
        return this.isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public Date getModidate() {
        return this.modidate;
    }

    public void setModidate(Date modidate) {
        this.modidate = modidate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cndrfnum", getCndrfnum())
            .toString();
    }

	public Long getCndcndrfnum() {
		return cndcndrfnum;
	}

	public void setCndcndrfnum(Long cndcndrfnum) {
		this.cndcndrfnum = cndcndrfnum;
	}

	/*public List<Tppoilockey> getPois() {
		return pois;
	}

	public void setPois(List<Tppoilockey> pois) {
		this.pois = pois;
	}*/

}
