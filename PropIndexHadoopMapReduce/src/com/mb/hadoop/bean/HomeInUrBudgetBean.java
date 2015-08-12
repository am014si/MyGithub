package com.mb.hadoop.bean;

import java.util.Date;

public class HomeInUrBudgetBean {

	private String city;
	private String localityName;
	private String propertyType;
	private Date date;
	private String salePriceBucket;
	private int count;
	
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
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSalePriceBucket() {
		return salePriceBucket;
	}
	public void setSalePriceBucket(String salePriceBucket) {
		this.salePriceBucket = salePriceBucket;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isDataNullOrEmpty(){
		return (this.city == null || this.city.isEmpty() || this.city.equalsIgnoreCase("null") || this.localityName == null || this.localityName.isEmpty() || this.localityName.equalsIgnoreCase("null") ||
				this.count == 0 || this.salePriceBucket == null || this.salePriceBucket.isEmpty());
	}
}
