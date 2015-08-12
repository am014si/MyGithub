package com.agoda;

import java.text.DateFormat;
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
import com.agoda.eventBean.Event;
import com.google.gson.Gson;

@Path("/aggregate")
public class RealtimeEventAggregator {

	static Queue<Event> queue = new ConcurrentLinkedQueue<Event>();
	static ConcurrentMap<Event, AtomicInteger> concurrentMap = new ConcurrentHashMap<Event, AtomicInteger>();
	DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	@POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendEvent(String data) throws Exception{ 
		
		JSONObject store = new JSONObject(data);
		Date date = null; Event event = null; String pageView = null; String url = null;
		String dateFormatted = null;
		
		Runnable r = new Runnable() {
			Event myEvent = null;
			public void run() {
				if(!queue.isEmpty()){
					myEvent = queue.poll();
					constructConcurrMap(myEvent);
					System.out.println("Event object "+myEvent.getUrl()+ " "+myEvent.getDate());
					System.out.println("Queue size "+queue.size());
				}
			}
		};
		
		if(store.has("Timestamp")) {
			Long datetimestamp = Long.parseLong(store.getString("Timestamp").replaceAll("\\D", ""));
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
			queue.offer(event);
			Thread t = new Thread(r);
			t.start();
		}
		
	}
	
	private void constructConcurrMap(Event event){
		System.out.println("Before addition: Concurr Map size "+concurrentMap.size());
		AtomicInteger atomIntger = concurrentMap.get(event);
		if(atomIntger != null){
			atomIntger.incrementAndGet();
			concurrentMap.put(event, atomIntger);
		} else {
			concurrentMap.put(event, new AtomicInteger(1));
		}
		System.out.println("After addition: Concurr Map size "+concurrentMap.size()+" "+concurrentMap.get(event));
	}
	
	@GET
    @Path("{url}/{startTime}/{endTime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response CountEvents(@PathParam("url") String url,@PathParam("startTime") String startTime,@PathParam("endTime") String endTime) {
        Date start = null; Date end = null; Long datetimestamp = null;
        Map<String, Integer> outputMap = new HashMap<String,Integer>();
        Event eventStart = null;
        System.out.println("Params "+url + " "+ startTime + " "+endTime);
        try{
	        if(startTime != null && !startTime.isEmpty()){ 
	        	datetimestamp = Long.parseLong(startTime);
	        	start = new Date(datetimestamp);
	        	start = new Date(formatter.format(start));
	        }
	        if(endTime != null && !endTime.isEmpty()){
	        	datetimestamp = Long.parseLong(endTime);
	        	end = new Date(datetimestamp);
	        	end = new Date(formatter.format(end));
	        }
        }catch(NumberFormatException pse){
        	System.out.println("Invalid path params for StartTime or EndTime");
        	return Response.status(500).entity("Invalid path params").build();
        }
        if(url != null && !url.isEmpty() && start != null && end != null){
        	long diffMinutes = ((end.getTime()/60000) - (start.getTime()/60000));
        	System.out.println(diffMinutes);
        	for(long countDiff = 1;countDiff <= diffMinutes;countDiff++){
        		start = incrementDate(start,countDiff);
        		eventStart = new Event(url, start.toString());
        		if(concurrentMap.containsKey(eventStart)){
        			outputMap.put(start.toString(), concurrentMap.get(eventStart).get());
        		}
        	}
        } else {
        	return Response.status(500).entity("Invalid path params").build();
        }
        //JSONObject mapAsJson = new JSONObject(outputMap);
        Gson gson = new Gson();
        String json = gson.toJson(outputMap,HashMap.class);
        return Response.status(200).entity(json).build();
    }
	
	private Date incrementDate(Date start,long countDiff){
		int mins = start.getMinutes();
		Long minsToSet = mins + countDiff;
		start.setMinutes(minsToSet.intValue());
		 return start;
	}
}
