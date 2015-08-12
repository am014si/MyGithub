package com.mb.hadoop.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PreferredLocalityReportBean {

	private String city;
	private String localityName;
	private String saleOrRent;
	private int listingCount;
	private int bedrooms;
	private long minCapitalValue;
	private long maxCapitalValue;
	private long avgCapitalValue;
	private String propertyType;
	private Date modiDate;
	
	
	public Date getModiDate() {
		return modiDate;
	}
	public void setModiDate(Date modiDate) {
		this.modiDate = modiDate;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocalityName() {
		return localityName;
	}
	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}
	public String getSaleOrRent() {
		return saleOrRent;
	}
	public void setSaleOrRent(String saleOrRent) {
		this.saleOrRent = saleOrRent;
	}
	public int getListingCount() {
		return listingCount;
	}
	public void setListingCount(int listingCount) {
		this.listingCount = listingCount;
	}
	public double getMinCapitalValue() {
		return minCapitalValue;
	}
	public void setMinCapitalValue(long minCapitalValue) {
		this.minCapitalValue = minCapitalValue;
	}
	public double getMaxCapitalValue() {
		return maxCapitalValue;
	}
	public void setMaxCapitalValue(long maxCapitalValue) {
		this.maxCapitalValue = maxCapitalValue;
	}
	public double getAvgCapitalValue() {
		return avgCapitalValue;
	}
	public void setAvgCapitalValue(long avgCapitalValue) {
		this.avgCapitalValue = avgCapitalValue;
	}
	public int getBedrooms() {
		return bedrooms;
	}
	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}
	public String toString(){
		return this.localityName + "," + this.city + "," + this.propertyType + "," + this.saleOrRent + "," + this.minCapitalValue 
		+ "," + this.maxCapitalValue + "," + this.avgCapitalValue + "," + formatDateString() + "," + this.listingCount;
	}
	
	private String formatDateString(){
		if(this.modiDate != null){
			return new SimpleDateFormat("yyyy-MM-dd").format(this.modiDate).toString();
		} else {
			return null;
		}
	}
	public boolean isDataNullOrEmpty(){
		return (this.city == null || this.city.isEmpty() || this.city.equalsIgnoreCase("null") || this.localityName == null || this.localityName.isEmpty() || this.localityName.equalsIgnoreCase("null") ||
				this.minCapitalValue == 0 || this.maxCapitalValue == 0 || this.modiDate == null || this.modiDate.toString().equalsIgnoreCase("null") || this.listingCount == 0
				|| this.saleOrRent == null || this.saleOrRent.isEmpty() || this.avgCapitalValue == 0);
	}
}
