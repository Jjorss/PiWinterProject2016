package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import controller.BufferedImageController;

public class WeatherMaker {

	// https://api.darksky.net/forecast/[key]/[latitude],[longitude]
	
	private double temperature = 0;
	private double precipProbability = 0;
	private double feelsLike = 0;
	private double dewPoint = 0;
	private double humidity = 0;
	private double windSpeed = 0;
	private String summary = "";
	private String hourlySummary = "";
	private long nearestStormDistance = 0;
	private String icon = "";
	private BufferedImageController bi;
	private BufferedImage weatherImage;
	
	public WeatherMaker(BufferedImageController bi) {
		this.bi = bi;
	}
	
	public void getData() {
		JSONParser parser = new JSONParser();
        String inputLine = "";
        BufferedReader in = null;	
		
		try {
			// lat and long for newark de
			URL url = new URL("https://api.darksky.net/forecast/7512b412b6225dcd3b9a7768cafd8a20/39.6837, -75.7497");
            URLConnection yc = url.openConnection();
            in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
            Object obj = null;
			while ((inputLine = in.readLine()) != null) {
			    System.out.println(inputLine);
			    obj = parser.parse(inputLine);
			}
			in.close();
			
			

			JSONObject json = (JSONObject) obj;
			System.out.println("currently: " + json.get("currently"));
			JSONObject hourly = (JSONObject) json.get("minutely");
			json = (JSONObject) json.get("currently");
			
//			this.setSummary((String)json.get("summary"));
//			this.setTemperature((double)json.get("temperature"));
//			this.setPrecipProbability((double)json.get("precipIntensity"));
//			this.setFeelsLike((double)json.get("apparentTemperature"));
//			this.setDewPoint((double)json.get("dewPoint"));
//			this.setHumidity((double)json.get("humidity"));
//			this.setWindSpeed((double)json.get("windSpeed"));
//			this.setNearestStormDistance((long)json.get("nearestStormDistance"));
//			this.setIcon((String)json.get("icon"));
//			hourly = (JSONObject) hourly.get("summary");
//			System.out.println(json);
			this.setHourlySummary((String)hourly.get("summary"));
			
			System.out.println("summary " +this.summary);
			System.out.println("temp " +this.temperature);
			System.out.println("precip " +this.precipProbability);
			System.out.println("feels like " +this.feelsLike);
			System.out.println("dew point " + this.dewPoint);
			System.out.println("humidity " + this.humidity);
			System.out.println("wind speed " + this.windSpeed);
			System.out.println("Hourly summary " +this.hourlySummary);
			System.out.println("nearestStormDistance " + this.nearestStormDistance);
			System.out.println("icon " + this.icon);

			this.setWeatherImage(bi.createWeatherIcon(this.getIcon()));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// test main
	public static void main(String[] args) {
		BufferedImageController bi = new BufferedImageController(null,null);
		WeatherMaker w = new WeatherMaker(bi);
		w.getData();
	}


	public double getTemperature() {
		return temperature;
	}


	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}


	public double getPrecipProbability() {
		return precipProbability;
	}


	public void setPrecipProbability(double precipProbability) {
		this.precipProbability = precipProbability;
	}


	public double getFeelsLike() {
		return feelsLike;
	}


	public void setFeelsLike(double feelsLike) {
		this.feelsLike = feelsLike;
	}


	public double getDewPoint() {
		return dewPoint;
	}


	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}


	public double getHumidity() {
		return humidity;
	}


	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}


	public double getWindSpeed() {
		return windSpeed;
	}


	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getHourlySummary() {
		return hourlySummary;
	}

	public void setHourlySummary(String hourlySummary) {
		this.hourlySummary = hourlySummary;
	}

	public double getNearestStormDistance() {
		return nearestStormDistance;
	}

	public void setNearestStormDistance(long nearestStormDistance) {
		this.nearestStormDistance = nearestStormDistance;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public BufferedImage getWeatherImage() {
		return weatherImage;
	}

	public void setWeatherImage(BufferedImage weatherImage) {
		this.weatherImage = weatherImage;
	}

}
