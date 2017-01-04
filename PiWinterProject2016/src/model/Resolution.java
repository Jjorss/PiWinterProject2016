package model;

public class Resolution {

	private int width = 0;
	private int height = 0;
	
	public Resolution(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
