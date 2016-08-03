package fourSquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class fourSquare {


	private static Logger logger = Logger.getLogger("SystemErr");  
	private static Properties prop = new Properties();
	
	
	public static void main(String[] args) {
		
		//Set up logging
		
	    FileHandler fh;  
	    try {
			fh = new FileHandler("SystemErr.log");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter); 
	    
	    } catch (SecurityException e2) {
			
			e2.printStackTrace();
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}  
	    
	    setupPropertyFiles();
	    String near = captureInput();
		
		
		String clientId=prop.getProperty("clientId");
		String clientSecret=prop.getProperty("clientSecret");
		String uri = prop.getProperty("URL")+"client_id="+clientId+"&client_secret="+clientSecret+"&v=20160601&near="+near;
		URL url;
		try {
				url = new URL(uri);
				
				callFourSquare(url);
	
			} catch (IOException e) {
				
			System.out.print(prop.getProperty("genericError"));
			logger.severe(e.getMessage());
			}
			

		}
	
	public static void setupPropertyFiles() {
	
		InputStream input = null;
	
		try {


			input = fourSquare.class.getResourceAsStream("/resources/system.properties");
			// load  properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}

	private static String captureInput() {
		String near="";
		BufferedReader br = null;
		
		System.out.println(prop.getProperty("entry"));
		br = new BufferedReader(new InputStreamReader(System.in));

		try {
			near = br.readLine();
			if (near.equals(""))
			{
				captureInput();
			}
		} catch (IOException e1) {
			
			System.out.print(prop.getProperty("tryAgain"));
			e1.printStackTrace();
			logger.severe(e1.getMessage());
		}
		return near;
	}

	public static String callFourSquare(URL url) throws IOException {
		

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		String responseString = "";
		
		connection.setRequestMethod("GET");
		 
		connection.setRequestProperty("Accept", "application/xml");
		if(connection != null && connection.getResponseCode() == 200)
		{

			InputStream response = connection.getInputStream();

			responseString = outputJSON(response);
			
			connection.disconnect();
			
			return responseString;
			
		}else if(connection.getResponseCode() == 400)
		{
			
			responseString = prop.getProperty("badParam");
			logger.severe("error 400");
			
		}else if(connection.getResponseCode() == 500)
		{
			responseString = prop.getProperty("serversDown");
		}else
		{
			responseString = prop.getProperty("genericError");
		}	
		
		System.out.print(responseString);
		
		connection.disconnect();

		return responseString;
		
	}
	
	private static String outputJSON(InputStream response) {
		
		JsonReader rdr = Json.createReader(response) ;
		JsonObject obj = rdr.readObject();
		
		// TO-DO null checking
		
		String inputString = obj.toString();
		JsonArray results = obj.getJsonObject("response").getJsonArray("groups");
		if (results != null)
		{
			for (JsonObject result : results.getValuesAs(JsonObject.class)) 
			{
				
				JsonArray items = result.getJsonArray("items");
				if (items != null)
				{
					for (JsonObject item : items.getValuesAs(JsonObject.class)) 
					{
						System.out.print(prop.getProperty("name"));
						System.out.println(item.getJsonObject("venue").getString("name"));
						System.out.print(prop.getProperty("category"));
						//get the first category
					    System.out.println(item.getJsonObject("venue").getJsonArray("categories").getJsonObject(0).getString("name"));
						System.out.print(prop.getProperty("address"));
						String address = item.getJsonObject("venue").getJsonObject("location").getJsonArray("formattedAddress").toString();
					    address = address.replaceAll("\\[", "");
					    address = address.replaceAll("\\]", "");
					    address = address.replaceAll("\"", "");
						System.out.println(address);
					    System.out.print(prop.getProperty("rating"));
					    System.out.println(item.getJsonObject("venue").getJsonNumber("rating"));
					    System.out.println("-----------");
					}
				}
			}
		}
			
			//return the JSON object
			
			return inputString;
	}


}
