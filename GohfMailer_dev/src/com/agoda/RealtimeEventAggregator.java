package com.agoda;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/hello")
public class RealtimeEventAggregator {
	
	public static void main(String[] args) throws Exception{
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Long datetimestamp = Long.parseLong("1438741673129");
		Long datetimestampear = Long.parseLong("1438700673129");
    	Date start = new Date(datetimestamp);
    	Date end = new Date(datetimestampear);
    	System.out.println(end);
    	long result = ((start.getTime()/60000) - (end.getTime()/60000));
    	System.out.println(result);
    	
    	//System.out.println(formatter.format(start));
    	//Date startDate = new Date(formatter.format(start));
    	//System.out.println(startDate);
    	//System.out.println(startDate.getMinutes());
    	Long mins = end.getMinutes()+result;
    	end.setMinutes(mins.intValue());
    	System.out.println(end);
	}

	Queue<Event> queue = new ConcurrentLinkedQueue<Event>();
	ConcurrentMap<Event, AtomicInteger> concurrentMap = new ConcurrentHashMap<Event, AtomicInteger>();
	DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy HH:mm");
	@POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendEvent(String data) throws Exception{ 
		
		JSONObject store = new JSONObject(data);
		Date date = null; Event event = null; String pageView = null; String url = null;
		String dateFormatted = null;
		if(store.has("Timestamp")) {
			Integer datetimestamp = Integer.parseInt(store.getString("Timestamp").replaceAll("\\D", ""));
			date = new Date(datetimestamp);
			dateFormatted = formatter.format(date);
		}
		if(store.has("PageView")){
			pageView = store.getString("PageView");
		}
		if(store.has("Url")){
			url = store.getString("Url");
		}
		if(date != null && pageView != null && url != null){
			
			event = new Event(url,dateFormatted);
			//queue.offer(event);
			//return Response.status(200).build();
			AtomicInteger atomIntger = concurrentMap.get(event);
			if(atomIntger != null){
				atomIntger.incrementAndGet();
				concurrentMap.put(event, atomIntger);
			} else {
				concurrentMap.put(event, new AtomicInteger(1));
			}
			
		}
		
	}
	
	/*private void constructConcurrHashMap(){
		//Event myObject = queue.poll();
		AtomicInteger atomIntger = concurrentMap.get(myObject);
		if(atomIntger != null){
			atomIntger.incrementAndGet();
			concurrentMap.put(myObject, atomIntger);
		} else {
			concurrentMap.put(myObject, new AtomicInteger(1));
		}
	}*/
	@GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response CountEvents(@PathParam("url") String url,@PathParam("startTime") String startTime,@PathParam("endTime") String endTime) {
        Date start = null; Date end = null;
        Map<String, Integer> outputMap = new HashMap<String,Integer>();
        Event eventStart = null;
        try{
	        if(startTime != null && !startTime.isEmpty()){ 
	        	start = formatter.parse(startTime);
	        }
	        if(endTime != null && !endTime.isEmpty()){
	        	end = formatter.parse(endTime);
	        }
        }catch(ParseException pse){
        	System.out.println("Invalid path params for StartTime or EndTime");
        	return Response.status(500).build();
        }
        if(url != null && !url.isEmpty() && start != null && end != null){
        	long diff = start.getTime() - end.getTime();
        	long diffMinutes = diff / (60 * 1000) % 60;
        	for(int countDiff = 1;countDiff <= diffMinutes;countDiff++){
        		start = incrementDate(start,countDiff);
        		eventStart = new Event(url, start.toString());
        		if(concurrentMap.containsKey(eventStart)){
        			outputMap.put(start.toString(), concurrentMap.get(eventStart).get());
        		}
        	}
        }
        JSONObject mapAsJson = new JSONObject(outputMap);
        return Response.status(200).entity(mapAsJson).build();
    }
	
	private Date incrementDate(Date start,int countDiff){
		int mins = start.getMinutes();
		start.setMinutes(mins+countDiff);
		 return start;
	}
}
