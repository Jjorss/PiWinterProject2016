package controller;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import model.PinSubReddit;

public class PinController {

	private HashMap<PinSubReddit, Rectangle2D> pinnedThreads = new HashMap<PinSubReddit, Rectangle2D>();
	
	// read text file;
	public void loadPins() {
		
	}
	// save to text file
	public void addNewPin(PinSubReddit newPin) {
		
	}
	// edit text file
	public void deletePin(String link) {
		
	}

	public HashMap<PinSubReddit, Rectangle2D> getPinnedThreads() {
		return pinnedThreads;
	}

	public void setPinnedThreads(HashMap<PinSubReddit, Rectangle2D> pinnedThreads) {
		this.pinnedThreads = pinnedThreads;
	}
	
}
