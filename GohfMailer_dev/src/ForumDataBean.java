import java.sql.Timestamp;


public class ForumDataBean {

	private String likeCnt = null; 
	private String queRfNum = null; 
	private String ansQueRfNum = null; 
	private String viewedCnt = null; 
	private String queDesc = null; 
	private String ansDesc = null;
    private String city = null; 
    private String locality = null;
    private String fubiRfNum = null;
    private String ansRfNum = null;
    private int ansLikeCount = 0;
    private String ansIsActive = null;
    private String ansUbiRfNum = null;
    private String cityRfNum = null;
    private String lmtRfNum = null;
    public String getCityRfNum() {
		return cityRfNum;
	}
	public void setCityRfNum(String cityRfNum) {
		this.cityRfNum = cityRfNum;
	}
	public String getLmtRfNum() {
		return lmtRfNum;
	}
	public void setLmtRfNum(String lmtRfNum) {
		this.lmtRfNum = lmtRfNum;
	}
	public String getAnsUbiRfNum() {
		return ansUbiRfNum;
	}
	public void setAnsUbiRfNum(String ansUbiRfNum) {
		this.ansUbiRfNum = ansUbiRfNum;
	}
	private String ansIsFirst = null;
    private Timestamp ansCreateDate = null;
    public String getAnsRfNum() {
		return ansRfNum;
	}
	public void setAnsRfNum(String ansRfNum) {
		this.ansRfNum = ansRfNum;
	}
	public int getAnsLikeCount() {
		return ansLikeCount;
	}
	public void setAnsLikeCount(int ansLikeCount) {
		this.ansLikeCount = ansLikeCount;
	}
	public String getAnsIsActive() {
		return ansIsActive;
	}
	public void setAnsIsActive(String ansIsActive) {
		this.ansIsActive = ansIsActive;
	}
	public String getAnsIsFirst() {
		return ansIsFirst;
	}
	public void setAnsIsFirst(String ansIsFirst) {
		this.ansIsFirst = ansIsFirst;
	}
	public Timestamp getAnsCreateDate() {
		return ansCreateDate;
	}
	public void setAnsCreateDate(Timestamp ansCreateDate) {
		this.ansCreateDate = ansCreateDate;
	}
	private Timestamp createDate = null;
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getFubiRfNum() {
		return fubiRfNum;
	}
	public void setFubiRfNum(String fubiRfNum) {
		this.fubiRfNum = fubiRfNum;
	}
	public String getLikeCnt() {
		return likeCnt;
	}
	public void setLikeCnt(String likeCnt) {
		this.likeCnt = likeCnt;
	}
	public String getQueRfNum() {
		return queRfNum;
	}
	public void setQueRfNum(String queRfNum) {
		this.queRfNum = queRfNum;
	}
	public String getAnsQueRfNum() {
		return ansQueRfNum;
	}
	public void setAnsQueRfNum(String ansQueRfNum) {
		this.ansQueRfNum = ansQueRfNum;
	}
	public String getViewedCnt() {
		return viewedCnt;
	}
	public void setViewedCnt(String viewedCnt) {
		this.viewedCnt = viewedCnt;
	}
	public String getQueDesc() {
		return queDesc;
	}
	public void setQueDesc(String queDesc) {
		this.queDesc = queDesc;
	}
	public String getAnsDesc() {
		return ansDesc;
	}
	public void setAnsDesc(String ansDesc) {
		this.ansDesc = ansDesc;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	} 
    
    
}
