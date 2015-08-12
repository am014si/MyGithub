package com.mb.hadoop.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
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
public static void main(String[] args) {
	/*try{
	String logServer = "dss.magicbricks.com";
	String url="http://"+logServer+"/mbRunstats/log.html?h="+"test"+"&trk=WEB"+"&nav=L"+"&ac="+"BAD-BOT"+"&k="+"test"+"&ap="+"&ui="+"test"
	+"&ut="+"test"+"&pg="+"test"+"&tit="+"test"+"&tra="+"test"+"&ip="+"test";
	HttpClient client = new HttpClient();
	System.out.println("##########");
	PostMethod method = new PostMethod(url);
	System.out.println("##########"+client.executeMethod(method));}catch(Exception ex){
		System.out.println(ex.getMessage());
		ex.printStackTrace();
	}*/
	System.out.println(new Timestamp((new Date()).getTime()));
	ExecutorService executor = Executors.newSingleThreadExecutor();
	executor.submit(new Runnable() {
		public void run() {
            try {
            	System.out.println(Thread.currentThread());
                System.out.println("Hello");
                
                String logServer = "dss.magicbricks.com";
                
				String url="http://"+logServer+"/mbRunstats/log.html?h="+"mbqc.magicbricks.com"+"&trk=WEB"+"&nav=L"+"&ac="+"BAD-BOT"+"&k="+"ea6974cc90db8fa8a0204d86499cc3a6"+"&ap="+"&ui="
					+"&ut="+"&pg="+"ss2Captcha"+"&tit="+"ss2Captcha"+"&tra=Direct"+"test"+"&ip="+"10.150.240.146";
				
				/*h=http%3A%2F%2Fmbqc.magicbricks.com%2F&trk=WEB&nav=L&ac=BAD-BOT&k=ea6974cc90db8fa8a0204d86499cc3a6&ap=&ui=&ut=
					&pg=ss2Captcha&tit=ss2Captcha&tra=Direct&ip=10.150.240.146*/
				
				HttpClient client = new HttpClient();
				System.out.println("##########");
				PostMethod method = new PostMethod(url);
				//int returnCode = client.executeMethod(method);
				System.out.println("##########"+client.executeMethod(method));
            } catch(Exception e) {
          	}
		}
	});
	executor.shutdown();
	}
}
