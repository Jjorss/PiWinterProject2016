package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import model.Resolution;
import view.Program;

public class BufferedImageController {
	
	private Program p;
	private UiController ui;
	
	private String[] sideBarPaths = new String[]{"home.png","reddit.png", "draw.png", "calendar.png", "question_mark.png"};
	private List<BufferedImage> sideBarIcons = new ArrayList<BufferedImage>();
	
	private int homeAlpha = 170;
	
	public BufferedImageController(Program p, UiController ui){
		this.p = p;
		this.ui = ui;
	}

	public void loadSideBarIcons() {
		BufferedImage bi = null;
		for(String path : sideBarPaths) {
			try {
				URL url = BufferedImageController.class.getResource(
						 "/sideBar/" + path);
				bi = ImageIO.read(url);
			} catch(Exception e) {
				e.printStackTrace();
			}
			this.sideBarIcons.add(bi);
		}
	}
	
	public BufferedImage createHomeIcon() {
		BufferedImage bi = null;
		try {
			URL url = BufferedImageController.class.getResource(
					 "/sideBar/home.png");
			bi = ImageIO.read(url);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bi;
	}
	
	public BufferedImage createWeatherIcon(String icon) {
		BufferedImage bi = null;
		int rowSize = 6;
		int colSize = 8;
		
		try {
			 URL url = BufferedImageController.class.getResource(
					 "/weather/weatherIcons.png");
			bi = ImageIO.read(url);
		} catch(Exception e) {
			e.printStackTrace();
		}
		int imgWidth = bi.getWidth()/6;
		System.out.println(imgWidth);
		int imgHeight = bi.getHeight()/8;
		System.out.println(imgHeight);
		switch(icon) {
		case "clear-day":
			//0,1
			return bi.getSubimage(imgWidth*1,
					imgHeight*0,
					imgWidth,
					imgHeight);
		case "clear-night":
			// 0,2
			return bi.getSubimage(imgWidth*2,
					imgHeight*0,
					imgWidth,
					imgHeight);
			
		case "rain":
			// 2,5
			return bi.getSubimage(imgWidth*5,
					imgHeight*2,
					imgWidth,
					imgHeight);
			
		case "snow":
			// 3,4
			return bi.getSubimage(imgWidth*4,
					imgHeight*3,
					imgWidth,
					imgHeight);
			
		case "sleet":
			// 3, 5
			return bi.getSubimage(imgWidth*5,
					imgHeight*3,
					imgWidth,
					imgHeight);
			
		case "wind":
			//3, 0
			return bi.getSubimage(imgWidth*0,
					imgHeight*2,
					imgWidth,
					imgHeight);
			
		case "fog":
			//2, 0
			return bi.getSubimage(imgWidth*0,
					imgHeight*2,
					imgWidth,
					imgHeight);
			
		case "cloudy":
			// 2, 1
			return bi.getSubimage(imgWidth*1,
					imgHeight*2,
					imgWidth,
					imgHeight);
			
		case "partly-cloudy-day":
			// 1, 1
			return bi.getSubimage(imgWidth*1,
					imgHeight*1,
					imgWidth,
					imgHeight);
		case "partly-cloudy-night":
			// 1,2
			return bi.getSubimage(imgWidth*2,
					imgHeight*1,
					imgWidth,
					imgHeight);
		default:
			return bi.getSubimage(imgWidth*2,
					imgHeight*7,
					imgWidth,
					imgHeight);
			
		}
	}
	
	public void makeThread(Graphics g, Graphics2D g2, String title, String comment, int width, BufferedImage tn){
		int size = (int)(ui.getBoxes().get(0).getHeight() * 0.062);
		int commentSize = (int)(size*0.85);
		if (title.length() > 90) {
			title = title.substring(0, 85) + "....";
		}
		
		String string = "<html><body style='width:"+ width +"px; padding: 5px;'>"
                + "<h1 style='font-size:" + size + "px;'>" + title +  "</h1>";
				
		if (tn!=null) {
			string = string 
					+ "<h2 style='font-size:" + commentSize + "px;'>" + "(Tap to see image)</h2>"
					+ "<h2 style='font-size:" + commentSize + "px;'>" + comment + "</h2>"
					+ "</body></html>";
		} else {
			string = string
					+ "<h2 style='font-size:" + commentSize + "px;'>" + comment + "</h2>"
					+ "</body></html>";
		}

		JLabel textLabel = new JLabel(string);
        textLabel.setSize(textLabel.getPreferredSize());

        Dimension d = textLabel.getPreferredSize();
        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, 128));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        ui.getPosts().add(bi);
        
	}
	
	public BufferedImage makeTemperature(Graphics g, Graphics2D g2, String temp, String feelsLike){
		int size = (int)(ui.getTempRect().getHeight()*0.28);
		int padding = (int)(ui.getContentBox().getWidth() * 0.01);
		//15
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px;'>" + temp + "&deg;"+  "</h1>"
                + "<h2 style='font-size:" + (int)(size*0.3) + "px;'>Feels Like: " + feelsLike + "&deg;" + "</h2>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getTempRect().getWidth()*0.95),
				(int)(ui.getTempRect().getHeight()*0.95)));

        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, homeAlpha));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makeWeatherData(Graphics g, Graphics2D g2, String precip,
			String dewPoint, String humidity, String windSpeed, String storm){
		int size = (int)(ui.getTempRect().getHeight()*0.08);
		int padding = (int)(ui.getContentBox().getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px, 'margin: 0px';'>"
                + "<p style='font-size:" + size + "px;'>" + "Dew Point: " + dewPoint + "&deg;" +  "</p>"
                + "<p style='font-size:" + size + "px;'>" + "Humidity: " + humidity + "&#37;" +  "</p>"
                + "<p style='font-size:" + size + "px;'>" + "Chance of Rain: " + precip +  "</p>"
                + "<p style='font-size:" + size + "px;'>" + "Wind Speed: " + windSpeed + "MPH" +  "</p>"
                + "<p style='font-size:" + size + "px;'>" + "Nearest Storm Distance: " + storm + "M" +  "</p>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getWeatherDataRect().getWidth()*0.95),
				(int)(ui.getWeatherDataRect().getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, homeAlpha));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makeWeatherSummary(Graphics g, Graphics2D g2, String summary, String hourly){
		int size = (int)(ui.getTempRect().getWidth()*0.05);
		int padding = (int)(ui.getContentBox().getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px;'>" + "Today's forcast: " + summary + "</h1>"
                + "<h1 style='font-size:" + size + "px;'>" + "Currently: " + hourly + "</h1>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getWeatherSummaryRect().getWidth()*0.95),
				(int)(ui.getWeatherSummaryRect().getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, homeAlpha));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makeTime(Graphics g, Graphics2D g2, String time, String date){
		int size = (int)(ui.getTimeRect().getHeight()*0.16);
		int padding = (int)(ui.getContentBox().getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px; text-align: center;'>"  + time + "</h1>"
                + "<h1 style='font-size:" + size + "px; text-align: center;'>" + date + "</h1>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getTimeRect().getWidth()*0.95),
				(int)(ui.getTimeRect().getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, homeAlpha));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makeQuote(Graphics g, Graphics2D g2, String quote, String author){
		int size = (int)(ui.getDisplayRect().getHeight()*0.05);
		int padding = (int)(ui.getContentBox().getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px; text-align: center;'>"  + quote + "</h1>"
                + "<h2 style='font-size:" + size + "px; text-align: right;'>" + author + "</h2>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getDisplayRect().getWidth()*0.95),
				(int)(ui.getDisplayRect().getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, homeAlpha));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makePin(Graphics g, Graphics2D g2, String title){
		int size = (int)(ui.getPinnedGrid().get(0).getWidth()*0.05);
		int padding = (int)(ui.getPinnedGrid().get(0).getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px; text-align: center;'>"  + title + "</h1>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getPinnedGrid().get(0).getWidth()*0.95),
				(int)(ui.getPinnedGrid().get(0).getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255, 128));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public BufferedImage makeDrawClearButton(Graphics g, Graphics2D g2, String title){
		int size = (int)(ui.getContentBox().getWidth()*0.1);
		int padding = (int)(ui.getPinnedGrid().get(0).getWidth() * 0.01);
		String string = "<html><body style='padding: "+ padding +"px;'>"
                + "<h1 style='font-size:" + size + "px; text-align: center;'>"  + title + "</h1>"
                + "</body></html>";
				
		
		
		JLabel textLabel = new JLabel(string);
		textLabel.setSize(new Dimension(
				(int)(ui.getPinnedGrid().get(0).getWidth()*0.95),
				(int)(ui.getPinnedGrid().get(0).getHeight()*0.95)));


        Dimension d = textLabel.getSize();

        BufferedImage bi = new BufferedImage(
            d.width,
            d.height,
            BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setColor(new Color(200, 200, 200, 128));
        g.fillRoundRect(
            0,
            0,
            bi.getWidth(p),
            bi.getHeight(p),
            15,
            10);
        g.setColor(Color.black);
        
        textLabel.paint(g);

        return bi;
        
	}
	
	public Resolution scaleImage(int width, int height, int maxWidth, int maxHeight) {
		boolean scaled = false;
		int newWidth = width;
		int newHeight = height;
		while(!scaled) {
			if(newWidth > maxWidth || newHeight > maxHeight) {
				newWidth = (int)(newWidth*0.9);
				newHeight = (int)(newHeight*0.9);
			} else {
				scaled = true;
			}
		}
		
		return new Resolution(newWidth, newHeight);
	}
	
	public Resolution scaleImageUp(int width, int height, int maxWidth, int maxHeight) {
		boolean scaled = false;
		int newWidth = width;
		int newHeight = height;
		while(!scaled) {
			if(newWidth < maxWidth && newHeight < maxHeight) {
				newWidth = (int)(newWidth*1.1);
				newHeight = (int)(newHeight*1.1);
			} else {
				newWidth = (int)(newWidth/1.1);
				newHeight = (int)(newHeight/1.1);
				scaled = true;
			}
		}
		return new Resolution(newWidth, newHeight);
	}

	public List<BufferedImage> getSideBarIcons() {
		return sideBarIcons;
	}

	public void setSideBarIcons(List<BufferedImage> sideBarIcons) {
		this.sideBarIcons = sideBarIcons;
	}

	public int getHomeAlpha() {
		return homeAlpha;
	}
	
}
