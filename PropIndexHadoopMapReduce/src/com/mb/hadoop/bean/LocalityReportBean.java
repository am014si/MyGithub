package com.mb.hadoop.bean;

public class LocalityReportBean {
	private String localityName;
	private String propertyType;
	private String listingType;
	private long minCapitalValue;
	private long maxCapitalValue;
	private long avgCapitalValue;
	public String getLocalityName() {
		return localityName;
	}
	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getListingType() {
		return listingType;
	}
	public void setListingType(String listingType) {
		this.listingType = listingType;
	}
	public long getMinCapitalValue() {
		return minCapitalValue;
	}
	public void setMinCapitalValue(long minCapitalValue) {
		this.minCapitalValue = minCapitalValue;
	}
	public long getMaxCapitalValue() {
		return maxCapitalValue;
	}
	public void setMaxCapitalValue(long maxCapitalValue) {
		this.maxCapitalValue = maxCapitalValue;
	}
	public long getAvgCapitalValue() {
		return avgCapitalValue;
	}
	public void setAvgCapitalValue(long avgCapitalValue) {
		this.avgCapitalValue = avgCapitalValue;
	}
	public boolean equals(LocalityReportBean otherBean){
		return (localityName.equalsIgnoreCase(otherBean.getLocalityName()) && propertyType.equalsIgnoreCase(otherBean.getPropertyType()) && (listingType.equalsIgnoreCase(otherBean.getListingType())));
	}
}
