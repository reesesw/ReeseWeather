

import java.text.SimpleDateFormat;
import java.util.Date;

import kong.unirest.AsyncClient;
import kong.unirest.Client;
import kong.unirest.Config;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.apache.ApacheAsyncClient;
import kong.unirest.apache.ApacheClient;

import com.google.gson.Gson;



///////////////////////////////////////////////////////////////////////////////////////////////////

class WrCoord {
	float lon;
	float lat;
}

class WeatherDesc {
	int id;
	String main;
	String description;
	String icon;
}

class WrMainWeather {
	float temp;
	float feels_like;
	float temp_min;
	float temp_max;
	int pressure;
	int humidity;
}

class WrWind {
	float speed;
	int deg;
}

class WrClouds {
	int all;
}

class WrWeatherSys {
	int type;
	int id;
	String country;
	long sunrise;
	long sunset;
}


class WeatherResponse {
	WrCoord coord;
	WeatherDesc[] weather;
	String base;
	WrMainWeather main;
	int visibility;
	WrWind wind;
	WrClouds clouds;
	long dt;
	WrWeatherSys sys;
	int timezone;
	long id;
	String name;
	int cod;
}

///////////////////////////////////////////////////////////////////////////////////////////////////

/*
{"coord":{"lon":-81.84,"lat":41.31},"weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02n"}],"base":"stations","main":{"temp":44.44,"feels_like":36.23,"temp_min":41,"temp_max":46.99,"pressure":1018,"humidity":81},"visibility":10000,"wind":{"speed":10.29,"deg":200},"clouds":{"all":20},"dt":1601982353,"sys":{"type":1,"id":3646,"country":"US","sunrise":1601983782,"sunset":1602025239},"timezone":-14400,"id":5173237,"name":"Strongsville","cod":200}

 */
public class WeatherReese {

	static String REESE_APPID="myappid_from_openweathermap.org";
	static String CITY="Strongsville";
	
	public static void main(String[] args) {
		System.out.println("Weather API Development\n");
		String[] winddirection = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
		String wdir="Invalid";
		String restgetstr;
		
		// Use the unirest library to get Strongsville weather
		restgetstr = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + REESE_APPID + "&units=imperial";
		HttpResponse<JsonNode> response = Unirest.get(restgetstr)
				  .header("Accept", "application/json")
				  .asJson();
		System.out.println(response.getBody());
		
		System.out.println("convert to string.");
		String wrstr = response.getBody().toString();
		System.out.println(wrstr);
		Gson gson = new Gson();
		WeatherResponse wr = gson.fromJson(wrstr, WeatherResponse.class);
		System.out.println("Coordinates:  " + wr.coord.lon + ", " + wr.coord.lat);
		System.out.println("Description:  " + wr.weather[0].main + ", " + wr.weather[0].description);
		System.out.println("Temperature:  " + wr.main.temp);
		System.out.println("Humidity:  " + wr.main.humidity);
		System.out.println("Wind Speed:  " + wr.wind.speed);
		int dirindex = (int)((float)wr.wind.deg / 11.25F);
		if (dirindex == 31)
			wdir = winddirection[0];
		else if (dirindex < 32)
			wdir = winddirection[((dirindex-1)/2)];
		System.out.println("Wind Direction:  " + wdir);
		System.out.println("City:  " + wr.name);
		
		// convert seconds to milliseconds
		Date date = new java.util.Date(wr.sys.sunrise*1000L); 
		// the format of your date
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
		// give a timezone reference for formatting (see comment at the bottom)
		System.out.println("Timezone:  " + wr.timezone/3600);
		String stz = "GMT" + wr.timezone/3600;
		sdf.setTimeZone(java.util.TimeZone.getTimeZone(stz)); 
		String formattedDate = sdf.format(date);
		System.out.println("Sunrise:  " + formattedDate);		
	}
}


///////////////////////////////////////////////////////////////////////////////////////////////////
