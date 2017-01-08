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

	public void getData(){
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.reddit.com/r/elderscrollsonline/").timeout(50000).userAgent("Mozilla").get();
			Elements pulledThreads = doc.getElementsByClass("title may-blank ");
			
			for(Element thread : pulledThreads) {
				threads.add(new Thread("https://www.reddit.com" + thread.attr("href"),
						thread.text(), null));
				//System.out.println(thread.text());
			}
			pulledThreads = doc.getElementsByClass("title may-blank outbound");
			for(Element thread : pulledThreads) {
				threads.add(new Thread(thread.attr("href"), thread.text(), null));
				System.out.println(thread.text());
			}
			pulledThreads = doc.getElementsByAttributeValue("data-event-action", "comments");
			for (int i = 0; i < threads.size(); i++) {
				Thread thread = this.threads.get(i);
				thread.setComments(pulledThreads.get(i).text());
				//System.out.println(threads.size());
			}
			
			for (Thread thread : threads) {
				if (!thread.getLink().contains("https://www.reddit.com")) {					
					//Open a URL Stream
					Response resultImageResponse = Jsoup.connect(thread.getLink()).ignoreContentType(true).execute();
					// output here
					//System.out.println(thread.getLink());
					//String type = thread.getLink().replace("/", "");
					//System.out.println(type);
					//FileOutputStream out = (new FileOutputStream(new java.io.File("C:\\Users\\Jackson\\Desktop\\"  
					//+ thread.getLink().hashCode())));
					//out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
					//out.close();
					InputStream in = new ByteArrayInputStream(resultImageResponse.bodyAsBytes());
					BufferedImage bImageFromConvert = ImageIO.read(in);
					thread.setImage(bImageFromConvert);
					if (thread.getImage() == null) {
						System.out.println("image null");
					} else {
						System.out.println("good");
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
		rbb.getData();
		System.out.println("Done: " + rbb.threads.size());
	}

	public List<Thread> getThreads() {
		return threads;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}
}
