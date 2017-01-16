package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RedditBoardBuilder {
	
	private List<Thread> threads = new ArrayList<Thread>();

	public void getData(String subReddit){
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.reddit.com/" + subReddit).timeout(50000).userAgent("Mozilla").get();
			Elements pulledThreads = doc.getElementsByClass("title may-blank ");
			
			for(Element thread : pulledThreads) {
				threads.add(new Thread("https://www.reddit.com" + thread.attr("href"),
						thread.text(), null));
				//System.out.println(thread.getAllElements().tagName("img").attr("src"));
			}
			pulledThreads = doc.getElementsByClass("title may-blank outbound");
			for(Element thread : pulledThreads) {
				threads.add(new Thread(thread.attr("href"), thread.text(), null));
				//System.out.println(thread.getAllElements());
			}
			pulledThreads = doc.getElementsByAttributeValue("data-event-action", "comments");
			for (int i = 0; i < threads.size(); i++) {
				Thread thread = this.threads.get(i);
				thread.setComments(pulledThreads.get(i).text());
				//System.out.println(threads.size());
			}
			pulledThreads = doc.getElementsByClass("thing");
			for(int i = 0; i < threads.size(); i++) {
				//System.out.println("https:" + pulledThreads.get(i).getElementsByTag("img").attr("src"));
				if(!pulledThreads.get(i).attr("data-url").contains("http")) {
					threads.get(i).setImgLink("https://www.reddit.com" + pulledThreads.get(i).attr("data-url"));
				} else {
					threads.get(i).setImgLink(pulledThreads.get(i).attr("data-url"));
				}
				
				System.out.println("data urls: " + threads.get(i).getImgLink());
			}
			
			for (Thread thread : threads) {
				if (!thread.getImgLink().contains("https://www.reddit.com")) {	
					if(thread.getImgLink().contains("imgur.com")) {
						try {
							doc = Jsoup.connect(thread.getImgLink()).ignoreContentType(true).timeout(50000).userAgent("Mozilla").get();
							Elements html = doc.getElementsByClass("post-image-placeholder");
							//System.out.println("https:" + html.attr("src"));
							thread.setImgLink("https:" + html.attr("src"));
							System.out.println("FOUND IMAGE!!!!!!!!!!");
						}catch(Exception e) {
							try {
								thread.setImgLink(thread.getImgLink().replace("http", "https"));
							} catch(Exception e2){
								e.printStackTrace();
								e2.printStackTrace();
								//System.out.println(e.getCause());
								System.out.println("Link already dirrects to a img: " +thread.getImgLink());
							}
							
						}
					}
					
					//Open a URL Stream
					Response resultImageResponse = Jsoup.connect(
							thread.getImgLink()).ignoreContentType(true).timeout(50000).execute();
					//System.out.println(thread.getLink());
					
					InputStream in = new ByteArrayInputStream(resultImageResponse.bodyAsBytes());
					BufferedImage bImageFromConvert = ImageIO.read(in);
					thread.setImage(bImageFromConvert);
					if (thread.getImage() == null) {
						System.out.println("image null" + "\t" + "Finally link: " + thread.getImgLink());
						
					} else {
						System.out.println("good" + "\t" + "Finally link: " + thread.getImgLink());
					}
				} else {
					//System.out.println(thread.getLink());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	// test main
	public static void main(String[] args) {
		RedditBoardBuilder rbb = new RedditBoardBuilder();
		rbb.getData("r/elderscrollsonline/");
		System.out.println("Done: " + rbb.threads.size());
	}

	public List<Thread> getThreads() {
		return threads;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}
}
