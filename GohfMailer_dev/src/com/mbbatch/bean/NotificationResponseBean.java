package com.mbbatch.bean;

public class NotificationResponseBean {
	private static final long serialVersionUID = -8386223148647262762L;
	public NotificationResponseBean(){
		super();
	}
	public NotificationResponseBean(String responseRemarks, boolean status){
		this.success = status;
		this.responseRemarks = responseRemarks;
	}
	private boolean success;
	private String responseRemarks;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getResponseRemarks() {
		return responseRemarks;
	}
	public void setResponseRemarks(String responseRemarks) {
		this.responseRemarks = responseRemarks;
	}
	
}
