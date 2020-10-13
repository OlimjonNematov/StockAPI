package project1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyBot extends PircBot {
    
	static MyBot bot=new MyBot();
	
    public MyBot() {
        this.setName("OjBot6");
    }
    public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		//Use this function to read the message that comes in 
		//For example, you can have an if statment that says:
		if (message.contains("weather")) {
			//the user wants weather, so call the weather API you created in part 1	
			if (message.equals("weather")||message.equals("weather ")) {
				sendMessage(channel,"Please enter 'weather name'+ city'");
			}
			sendMessage(channel, getTemp(message.substring(8, message.length()), channel));
		} 
		//or to start, do something small like:
		else if (message.contains("hello")) {
			//the user wants weather, so call the weather API you created in part 1
			//this is how you send a message back to the channel 
			sendMessage(channel, "Hey " + sender + "!");
		
		} else if (message.contains("price of")) {
			sendMessage(channel, getPrice(message.substring(10,message.length()-1)));
		}
		else if(message.contains("leave")) {
			this.disconnect();
		}else {
			//return this if the user wants a functionallity that the bot cannot process
			sendMessage(channel,"I dont understand, try again");
		}
	}
    
    public static String getPrice(String tickerSymbol) {
		String urlString="https://financialmodelingprep.com/api/v3/profile/"+tickerSymbol+"?apikey=f28d44badf2c7daf661273dd9c45ac37";
		StringBuffer content= new StringBuffer();
		URL url;
		try {
			//create a request
			url = new URL(urlString);
			HttpURLConnection connection;
			try {
				//initialize the httpurl object
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				//create buffer
				BufferedReader bfReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				//create a string buffer
			
				String inputString;
				
				//while the api has lines to be read, read
				while ((inputString=bfReader.readLine())!=null) {
					//add lines read to the input string
					content.append(inputString);
				}
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Gson gson=new Gson();
    	//save temp
    	java.lang.reflect.Type stockLisType= new TypeToken<ArrayList<stockObj>>() {}.getType();
    	List<stockObj> list= gson.fromJson(content+"", stockLisType);
//		stockObj obj=gson.fromJson(content+"", stockObj.class);
    	stockObj obj= list.get(0);
    	String answerString="The price of "+tickerSymbol+": $"+obj.price+"\n"+
    	"Last change: $"+obj.changes+""+"\n"
    	+tickerSymbol+" is traded in the "+obj.exchange;
    	
    	System.out.println(answerString);
    	
    	return answerString;
    }
    
    public static String getTemp(String city, String channel) {
		String urlString="https://api.openweathermap.org/data/2.5/weather?q="+city+",us&appid="+"12a5696ac6297dc4d1459000c9975525";
		StringBuffer content= new StringBuffer();
		URL url;
		try {
			//create a request
			url = new URL(urlString);
			HttpURLConnection connection;
			try {
				//initialize the httpurl object
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				//create buffer
				BufferedReader bfReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				//create a string buffer
			
				String inputString;
				
				//while the api has lines to be read, read
				while ((inputString=bfReader.readLine())!=null) {
					//add lines read to the input string
					content.append(inputString);
				}
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Gson gson=new Gson();
    	//save temp
		weatherObj obj=gson.fromJson(content+"", weatherObj.class);
    	
    	return "it is "+ Math.round((obj.main.temp-273.15) *1.0) / 1.0+" Celcius in "+city;
    }

    


}