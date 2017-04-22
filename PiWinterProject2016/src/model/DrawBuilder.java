package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class DrawBuilder {

	private List<Line2D> pixels = new ArrayList<Line2D>();
	private int thickness = 5;
	private Color color = Color.RED;
	private DrawState currentState = DrawState.START;
	
	public DrawBuilder() {
		
	}
	
	public void createPixel(Point2D p1, Point2D p2){
		this.pixels.add(new Line2D.Double(
				p1.getX(),
				p1.getY(),
				p2.getX(),//new x
				p2.getY()));//new y
	}
	
	public void handleDraw(Point p) {		
		switch(this.currentState) {
		case DRAW:
			this.createPixel( this.pixels.get(this.pixels.size()-1).getP2(), p);
			System.out.println("Amount of pixels: " + this.pixels.size());
			break;
		case END:
			break;
		case START:
			this.createPixel(p, p);
			System.out.println("Amount of pixels: " + this.pixels.size());
			break;
		default:
			break;
		
		}

	}
	
	public void eraseAll() {
		this.pixels.clear();
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public List<Line2D> getPixels() {
		return pixels;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public DrawState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(DrawState currentState) {
		this.currentState = currentState;
	}

	

	
}
