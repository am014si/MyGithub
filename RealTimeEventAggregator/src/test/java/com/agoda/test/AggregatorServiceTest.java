package com.agoda.test;

import static org.junit.Assert.assertEquals;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
 
public class AggregatorServiceTest extends JerseyTest {
	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().build();
	}
 
	@Test
	public void testSubmitEvents() throws JSONException,
			URISyntaxException {
		try {
	        Client client = Client.create();
	        WebResource webResource = client.resource("http://localhost:8080/RealTimeEventAggregator/aggregate/post");
	        String input = "{\"Timestamp\":\"1438741673129\", \"PageView\":\"pageview\", \"Url\":\"user.html\"}";
	        ClientResponse response = webResource.type("application/json")
	           .post(ClientResponse.class, input);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	}
 
	@Test
	public void testCountEvents() {
		Client client = Client.create();
		try{
		WebResource webResource = client().resource("http://localhost:8080/");
		JSONObject json = webResource.path("RealTimeEventAggregator/aggregate/user.html/1438700673129/1438741673129")
				.get(JSONObject.class);
		//ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		//System.out.println(response.getEntity(String.class));
		//JSONObject jsonOutput = (JSONObject) JsonSerializer<T>.toJSON( response.getEntity(String.class) );
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		long datetimestamp = Long.parseLong("1438741673129".replaceAll("\\D", ""));
		Date start = new Date(datetimestamp);
		String dateFormatted = formatter.format(start);
		System.out.println("Date Formatted "+dateFormatted+" JSON "+json);
			assertEquals(1, json.get(dateFormatted));
		//assertEquals("1", "1");
		}catch(Exception jsEx){
			System.out.println("Assert failed");
		}
	}
}
