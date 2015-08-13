package com.agoda;

import java.sql.Timestamp;

public class RequestBean  implements Comparable<RequestBean> {
	private int diffInSeconds = 0;
	private Timestamp reqTime = null;
	
	public RequestBean(int diffInSeconds, Timestamp reqTime) {
		super();
		this.diffInSeconds = diffInSeconds;
		this.reqTime = reqTime;
	}
	@Override
	public String toString() {
		return "RequestBean [diffInSeconds=" + diffInSeconds + ", reqTime="
				+ reqTime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diffInSeconds;
		result = prime * result + ((reqTime == null) ? 0 : reqTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestBean other = (RequestBean) obj;
		if (diffInSeconds != other.diffInSeconds)
			return false;
		if (reqTime == null) {
			if (other.reqTime != null)
				return false;
		} else if (!reqTime.equals(other.reqTime))
			return false;
		return true;
	}
	public int getDiffInSeconds() {
		return diffInSeconds;
	}
	public void setDiffInSeconds(int diffInSeconds) {
		this.diffInSeconds = diffInSeconds;
	}
	public Timestamp getReqTime() {
		return reqTime;
	}
	public void setReqTime(Timestamp reqTime) {
		this.reqTime = reqTime;
	}
	@Override
	public int compareTo(RequestBean bean) {
	    return getReqTime().compareTo(bean.getReqTime());
	}
	
}
