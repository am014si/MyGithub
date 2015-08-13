package com.agoda;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rateAggregate")
public class HotelRateService {
	private static final String ASC_SORTING_ORDER = "ASC";
	private static final String DESC_SORTING_ORDER = "DESC";
	private static Map<String, Integer> apiKeysMap = new HashMap<String,Integer>(){};
	private static Map<String, List<HotelDBBean>> hotelsByCityMap = new HashMap<String, List<HotelDBBean>>(){};
	//API_KEY,Time of suspension
	private static Map<String,Timestamp> suspendedApiKeyMap = new HashMap<String,Timestamp>();
	private static ConcurrentMap<String,List<RequestBean>> apiRequestMap = new ConcurrentHashMap<String,List<RequestBean>>();
	static {
		//Map<API Key,Number of Requests per seconds>
		apiKeysMap.put("112134", 3);
		apiKeysMap.put("1d3134", 2);
		apiKeysMap.put("116h54", 2);
		apiKeysMap.put("333h54", 1);
		try {
			Scanner sc = new Scanner(new File("hoteldb.csv"), "UTF-8");
			String city = null; 
			String hotelId = null;
			String roomType = null;
			int roomPrice = 0;
			{
				sc.nextLine();
				String[] csvLineArray = null;
				while (sc.hasNextLine()) {
					List<HotelDBBean> hotelDBList = null;
					String line = sc.nextLine();
					if(line != null && !line.isEmpty()){
						csvLineArray = line.split(",");
						city = csvLineArray[0];
						hotelId = csvLineArray[1];
						roomType = csvLineArray[2];
						roomPrice = Integer.parseInt(csvLineArray[3]);
						if(hotelsByCityMap.containsKey(city)){
							hotelDBList = hotelsByCityMap.get(city);
						} else {
							hotelDBList = new ArrayList<HotelDBBean>();
						}
						hotelDBList.add(new HotelDBBean(hotelId, roomType, roomPrice));
						hotelsByCityMap.put(city, hotelDBList);
					}
				}
				if (sc.ioException() != null) {
					throw sc.ioException();
				}
			}
		}catch(IOException ie){
			ie.printStackTrace();
			//Log the Message
		}
	}
	
	@GET
    @Path("{city}/{sortOrder}/{apiKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HotelDBBean> getListofHotelsWithCity(@PathParam("city") String city, @PathParam("sortOrder") String sortingOrder, @PathParam("apiKey") String key){
		List<HotelDBBean> hotelDBList = null;
		Timestamp sq = new Timestamp(new Date().getTime());
		boolean isKeySuspended = true;
		if(suspendedApiKeyMap != null && suspendedApiKeyMap.containsKey(key)){
			Timestamp suspTime = suspendedApiKeyMap.get(key);
			int minutesDiff = sq.getMinutes() - suspTime.getMinutes();
			if(minutesDiff > 5){
				isKeySuspended = false;
			}
		}
		if(!isKeySuspended){
			if(validateRateLimit(key)){
				if(hotelsByCityMap.containsKey(city)){
					hotelDBList = hotelsByCityMap.get(city);
				}
				if(ASC_SORTING_ORDER.equalsIgnoreCase(sortingOrder)){
					Collections.sort(hotelDBList, new HotelDBBean.HotelBeanComparator());
				} else if(DESC_SORTING_ORDER.equalsIgnoreCase(sortingOrder)){
					Collections.sort(hotelDBList, Collections.reverseOrder(new HotelDBBean.HotelBeanComparator()));
				}
			} else {
				suspendedApiKeyMap.put(key, sq);
			}
		}
		return hotelDBList;
	}
	
	private boolean validateRateLimit(String apiKey){
		boolean validated = false;
		Timestamp sq = new Timestamp(new Date().getTime());
		List<RequestBean> list = apiRequestMap.get(apiKey);
		if(list == null || list.isEmpty()){
			list = new ArrayList<RequestBean>();
			list.add(new RequestBean(0, sq));
			apiRequestMap.put(apiKey, list);
		} else {
			Collections.sort(list);
			int lastIndex = list.size();
			int secsDiff = sq.getSeconds() - list.get(lastIndex-1).getReqTime().getSeconds();
			secsDiff = secsDiff + list.get(lastIndex-1).getDiffInSeconds();
			if(secsDiff <= 10){
				validated = true;
			}
			list.add(new RequestBean(secsDiff, sq));
			apiRequestMap.put(apiKey, list);
		}
		if(!validated){
			int messgPerSecond = apiKeysMap.get(apiKey);
			if(messgPerSecond == 0){
				messgPerSecond = 1;
			}
			validated = isRateLimited(messgPerSecond);
		}
		return validated;
	}
	
	private boolean isRateLimited(int msgs_per_sec) {
		long check_time = System.currentTimeMillis();
		int msgs_sent_count = 0;
	    if (System.currentTimeMillis() - check_time > 1000) {
	        check_time = System.currentTimeMillis();
	        msgs_sent_count = 0;
	    }

	    if (msgs_sent_count > (msgs_per_sec - 1)) {
	        return true;
	    } else {
	        msgs_sent_count++;
	    }

	    return false;
	}

}
