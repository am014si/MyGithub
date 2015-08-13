package com.agoda;

import java.util.Comparator;

public class HotelDBBean {
	private String hotelId = null;
	private String roomType = null;
	private Integer roomPrice = 0;
	public HotelDBBean() {
		super();
	}
	public HotelDBBean(String hotelId, String roomType, int roomPrice) {
		super();
		this.hotelId = hotelId;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public Integer getRoomPrice() {
		return roomPrice;
	}
	public void setRoomPrice(int roomPrice) {
		this.roomPrice = roomPrice;
	}
	@Override
	public String toString() {
		return "HotelDBBean [hotelId=" + hotelId + ", roomPrice=" + roomPrice
				+ ", roomType=" + roomType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hotelId == null) ? 0 : hotelId.hashCode());
		result = prime * result + roomPrice;
		result = prime * result
				+ ((roomType == null) ? 0 : roomType.hashCode());
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
		HotelDBBean other = (HotelDBBean) obj;
		if (hotelId == null) {
			if (other.hotelId != null)
				return false;
		} else if (!hotelId.equals(other.hotelId))
			return false;
		if (roomPrice != other.roomPrice)
			return false;
		if (roomType == null) {
			if (other.roomType != null)
				return false;
		} else if (!roomType.equals(other.roomType))
			return false;
		return true;
	}
    public int compareTo(HotelDBBean hotelBean){
    	return getRoomPrice().compareTo(hotelBean.getRoomPrice());
    }
    static class HotelBeanComparator implements Comparator<HotelDBBean>{
    	public int compare(HotelDBBean bean1, HotelDBBean bean2)
    	{
    		int room1Price = bean1.getRoomPrice();
    		int room2Price = bean2.getRoomPrice();
    		if (room1Price == room2Price)
    			return 0;
    		else if (room1Price > room2Price)
    			return 1;
    		else
    			return -1;
    	}
    }
}
