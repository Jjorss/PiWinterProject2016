package model;

public class PinSubReddit {

	private String link;
	private String title;
	
	public PinSubReddit(String link, String title) {
		this.setLink(link);
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
