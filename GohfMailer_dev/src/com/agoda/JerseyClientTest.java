package com.agoda;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClientTest {

  public static void main(String[] args) {
	  /*ConcurrentMap<Event, AtomicInteger> concurrentMap = new ConcurrentHashMap<Event, AtomicInteger>();
	  Event e1 = new Event("1.htm", "2014-08-04");
	  concurrentMap.put(e1, new AtomicInteger(2));
	  Event e2 = new Event("1.htm", "2014-08-04");
	  AtomicInteger atomIntger = concurrentMap.get(e2);
	  atomIntger.incrementAndGet();
	  concurrentMap.put(e2,atomIntger);
	  System.out.println(concurrentMap.get(e1));*/
	  DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy HH:mm");
	  Date start = new Date();
	  int mins = start.getMinutes();
	  start.setMinutes(mins+180);
	  System.out.println(formatter.format(start));
    /*try {

        Client client = Client.create();

        WebResource webResource = client.resource("http://localhost:8080/your-app/rest/data/post");

        String input = "{\"message\":\"Hello\"}";

        ClientResponse response = webResource.type("application/json")
           .post(ClientResponse.class, input);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                 + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

      } catch (Exception e) {

        e.printStackTrace();

      }*/

    }
}
