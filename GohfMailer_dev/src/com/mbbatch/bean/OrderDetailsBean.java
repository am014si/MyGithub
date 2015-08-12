package com.mbbatch.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderDetailsBean implements Serializable{

	private static final long serialVersionUID = -8386275978647262762L;
	private String orderNo;
	private String userName;
	private String userEmail;
	private String userMobile;
	private String orderStatus;
	private String unitId;
	private String bookingAmount;
	private String couponCode;
	private Timestamp createDate;
	private Timestamp modifiedDate;
	private String offerApplied;
	private String referrerURL;
	private String projectId;
	private String userMobileIsd;
	private String developerDisplayMobile;
	private String developerName;
	private String developerEmail;
	private String projectTitle;
	private String unitBHkType;
	private String projectAddress;
	private String unitPrice;
	private String unitArea;
	private String offerPrice;
	private String offerValidity;
	private boolean mailSentToUser;
	private boolean mailSentToDeveloper;
	private boolean smsSentToUser;
	private boolean smsSentToDeveloper;
	private String developerCompanyName;
	private String orderCommStatus;
	private String orderCommStatusRemarks;
	private String termsAndConditions;
	private String nextSteps;
	private String coveredArea;
	private String coveredAraUnit;
	private String plotArea;
	private String plotAreaUnit;
	private String propertyType;
	private String location;
	private String offerType;
	private String developerLeadSMSMobile;
	private String city;
	private String developerCommStatusRemarks;
	private String id;
	private String slug;
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeveloperCommStatusRemarks() {
		return developerCommStatusRemarks;
	}
	public void setDeveloperCommStatusRemarks(String developerCommStatusRemarks) {
		this.developerCommStatusRemarks = developerCommStatusRemarks;
	}
	public String getOrderCommStatusRemarks() {
		return orderCommStatusRemarks;
	}
	public void setOrderCommStatusRemarks(String orderCommStatusRemarks) {
		this.orderCommStatusRemarks = orderCommStatusRemarks;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDeveloperDisplayMobile() {
		return developerDisplayMobile;
	}
	public void setDeveloperDisplayMobile(String developerDisplayMobile) {
		this.developerDisplayMobile = developerDisplayMobile;
	}
	public String getDeveloperLeadSMSMobile() {
		return developerLeadSMSMobile;
	}
	public void setDeveloperLeadSMSMobile(String developerLeadSMSMobile) {
		this.developerLeadSMSMobile = developerLeadSMSMobile;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getCoveredArea() {
		return coveredArea;
	}
	public void setCoveredArea(String coveredArea) {
		this.coveredArea = coveredArea;
	}
	public String getCoveredAraUnit() {
		return coveredAraUnit;
	}
	public void setCoveredAraUnit(String coveredAraUnit) {
		this.coveredAraUnit = coveredAraUnit;
	}
	public String getPlotArea() {
		return plotArea;
	}
	public void setPlotArea(String plotArea) {
		this.plotArea = plotArea;
	}
	public String getPlotAreaUnit() {
		return plotAreaUnit;
	}
	public void setPlotAreaUnit(String plotAreaUnit) {
		this.plotAreaUnit = plotAreaUnit;
	}
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	public String getNextSteps() {
		return nextSteps;
	}
	public void setNextSteps(String nextSteps) {
		this.nextSteps = nextSteps;
	}
	public String getOrderCommStatus() {
		return orderCommStatus;
	}
	public void setOrderCommStatus(String orderCommStatus) {
		this.orderCommStatus = orderCommStatus;
	}
	public String getDeveloperCompanyName() {
		return developerCompanyName;
	}
	public void setDeveloperCompanyName(String developerCompanyName) {
		this.developerCompanyName = developerCompanyName;
	}
	public boolean isMailSentToUser() {
		return mailSentToUser;
	}
	public void setMailSentToUser(boolean mailSentToUser) {
		this.mailSentToUser = mailSentToUser;
	}
	public boolean isMailSentToDeveloper() {
		return mailSentToDeveloper;
	}
	public void setMailSentToDeveloper(boolean mailSentToDeveloper) {
		this.mailSentToDeveloper = mailSentToDeveloper;
	}
	public boolean isSmsSentToUser() {
		return smsSentToUser;
	}
	public void setSmsSentToUser(boolean smsSentToUser) {
		this.smsSentToUser = smsSentToUser;
	}
	public boolean isSmsSentToDeveloper() {
		return smsSentToDeveloper;
	}
	public void setSmsSentToDeveloper(boolean smsSentToDeveloper) {
		this.smsSentToDeveloper = smsSentToDeveloper;
	}
	public String getOfferValidity() {
		return offerValidity;
	}
	public void setOfferValidity(String offerValidity) {
		this.offerValidity = offerValidity;
	}
	public String getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}
	public String getUnitArea() {
		return unitArea;
	}
	public void setUnitArea(String unitArea) {
		this.unitArea = unitArea;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getProjectAddress() {
		return projectAddress;
	}
	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}
	public String getDeveloperName() {
		return developerName;
	}
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	public String getDeveloperEmail() {
		return developerEmail;
	}
	public void setDeveloperEmail(String developerEmail) {
		this.developerEmail = developerEmail;
	}
	public String getProjectTitle() {
		return projectTitle;
	}
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getUnitBHkType() {
		return unitBHkType;
	}
	public void setUnitBHkType(String unitBHkType) {
		this.unitBHkType = unitBHkType;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getUserMobileIsd() {
		return userMobileIsd;
	}
	public void setUserMobileIsd(String userMobileIsd) {
		this.userMobileIsd = userMobileIsd;
	}
	public String getOfferApplied() {
		return offerApplied;
	}
	public void setOfferApplied(String offerApplied) {
		this.offerApplied = offerApplied;
	}
	public String getReferrerURL() {
		return referrerURL;
	}
	public void setReferrerURL(String referrerURL) {
		this.referrerURL = referrerURL;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getBookingAmount() {
		return bookingAmount;
	}
	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Override
	public String toString() {
		return "OrderDetailsBean [bookingAmount=" + bookingAmount + ", city="
				+ city + ", couponCode=" + couponCode + ", coveredAraUnit="
				+ coveredAraUnit + ", coveredArea=" + coveredArea
				+ ", createDate=" + createDate
				+ ", developerCommStatusRemarks=" + developerCommStatusRemarks
				+ ", developerCompanyName=" + developerCompanyName
				+ ", developerDisplayMobile=" + developerDisplayMobile
				+ ", developerEmail=" + developerEmail
				+ ", developerLeadSMSMobile=" + developerLeadSMSMobile
				+ ", developerName=" + developerName + ", id=" + id
				+ ", location=" + location + ", mailSentToDeveloper="
				+ mailSentToDeveloper + ", mailSentToUser=" + mailSentToUser
				+ ", modifiedDate=" + modifiedDate + ", offerApplied=" + offerApplied + ", offerPrice="
				+ offerPrice + ", offerType=" + offerType + ", offerValidity="
				+ offerValidity + ", orderCommStatus=" + orderCommStatus
				+ ", orderCommStatusRemarks=" + orderCommStatusRemarks
				+ ", orderNo=" + orderNo + ", orderStatus=" + orderStatus
				+ ", plotArea=" + plotArea + ", plotAreaUnit=" + plotAreaUnit
				+ ", projectAddress=" + projectAddress + ", projectId="
				+ projectId + ", projectTitle=" + projectTitle
				+ ", propertyType=" + propertyType + ", referrerURL="
				+ referrerURL + ", smsSentToDeveloper=" + smsSentToDeveloper
				+ ", smsSentToUser=" + smsSentToUser + ", unitArea=" + unitArea
				+ ", unitBHkType=" + unitBHkType + ", unitId=" + unitId
				+ ", unitPrice=" + unitPrice + ", userEmail=" + userEmail
				+ ", userMobile=" + userMobile + ", userMobileIsd="
				+ userMobileIsd + ", userName=" + userName + "]";
	}
}
