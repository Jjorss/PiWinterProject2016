package model;

import java.awt.image.BufferedImage;

public class Thread {

	private String link;
	private String title;
	private String comments;
	private String imgLink = "";
	private BufferedImage image = null;
	private boolean showImage = false;
	
	public Thread(String link, String title, String comments, String imgLink){
		this.setLink(link);
		this.setTitle(title);
		this.setComments(comments);
		this.setImgLink(imgLink);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public boolean isShowImage() {
		return showImage;
	}

	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
}
