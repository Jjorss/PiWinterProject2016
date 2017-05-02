package controller;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;


public class AnimationController {
	
	private int x = 0;
	private int y = 0;
	private int speed = 0;
	private Point startPoint;
	private Point endPoint;
	private BufferedImage image;
	private UiController ui;
	private int imageWidth;
	private int imageHeight;
	private int yMax;
	private int yMin;
	private boolean displayWentOffScreen = false;
	
	public AnimationController(int speed, Point startPoint, Point endPoint, BufferedImage image, UiController ui) {
		this.setStartPoint(startPoint);
		this.setX(this.startPoint.x);
		this.setY(this.startPoint.y);
		this.setSpeed(speed);
		this.setEndPoint(endPoint);
		this.setImage(image);
		this.ui = ui;
		this.imageWidth = (int)(ui.getWeatherDataImage().getWidth()*0.15);
		this.imageHeight = this.imageWidth;
		this.yMax = (int)ui.getWeatherIconRect().getY();// - (int)(ui.getWeatherIconRect().getHeight() - ui.getWeatherDataImage().getHeight());
		this.yMin = (int)(ui.getWeatherIconRect().getY() + (ui.getWeatherIconRect().getHeight() - image.getHeight()));
	}
	
	public AnimationController(int speed, Point startPoint, Point endPoint, UiController ui) {
		this.setStartPoint(startPoint);
		this.setX(this.startPoint.x);
		this.setY(this.startPoint.y);
		this.setSpeed(speed);
		this.setEndPoint(endPoint);
		this.ui = ui;
		
	}

	public void render(Graphics2D g2) {
		g2.drawImage(image, x, y, this.imageWidth, this.imageHeight, null);
	}
	
	public void update(){
		this.moveLeftToRight();
	}
	
	public void moveLeftToRight(){
		if(this.x <= this.endPoint.x){
			this.setX(this.startPoint.x);
			int randomSpeed = (int )(Math.random() * 3 + 1);
			this.setSpeed(randomSpeed);
		
		} else{
			this.setX(this.getX() - this.getSpeed());
		}
		// TODO make random Y paths smooth
//		if(this.y <= this.endPoint.y) {
//			this.setY(this.endPoint.y +1);
//		} else{
//			int randomNum = -2 + (int)(Math.random() * 4); 
//			this.setY(this.getY() + (randomNum*this.getSpeed()));
//		}
	}
	
public void runDisplayAnimation(){
		
		if (ui.getDisplayRect().getX() > ui.getContentBox().getX() + ui.getContentBox().getWidth()) {
			ui.getDisplayRect().setRect(ui.getContentBox().getX() - ui.getDisplayRect().getWidth(), ui.getDisplayRect().getY(),
					ui.getDisplayRect().getWidth(), ui.getDisplayRect().getHeight());
			this.displayWentOffScreen = true;
		} else {
			ui.getDisplayRect().setRect(ui.getDisplayRect().getX()+300, ui.getDisplayRect().getY(),
					ui.getDisplayRect().getWidth(), ui.getDisplayRect().getHeight());
		}
		
		if (this.displayWentOffScreen) {
			if(ui.getDisplayRect().getX() + this.speed >= this.endPoint.getX()) {
				ui.setRunDisplayanimation(false);
				ui.getDisplayRect().setRect(this.getEndPoint().getX(), this.getEndPoint().getY(),
						ui.getDisplayRect().getWidth(), ui.getDisplayRect().getHeight());
				this.displayWentOffScreen = false;
			}
		}
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
