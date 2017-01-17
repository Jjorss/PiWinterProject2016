package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controller.UiController;
import view.Program;

public class RedditBoardBuilder extends SwingWorker<Integer, String> {
	
	private List<Thread> threads = new ArrayList<Thread>();
	private int index = 0;
	private UiController ui;
	private String subReddit;
	
	public RedditBoardBuilder(UiController ui, String subReddit) {
		this.ui = ui;
		this.subReddit = subReddit;
	}

	public void getData(String subReddit){
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.reddit.com/" + subReddit).timeout(50000).userAgent("Mozilla").get();
			Elements pulledThreads = doc.getElementsByClass("thing");
			for(Element thread : pulledThreads) {
				Thread t = new Thread(
						thread.getElementsByAttributeValue("data-event-action", "title").attr("href"),
						thread.getElementsByAttributeValue("data-event-action", "title").text(),
						thread.getElementsByAttributeValue("data-event-action", "comments").text(),
						null);
				if(!t.getLink().contains("http")) {
					t.setLink("https://www.reddit.com" + t.getLink());
				}
				System.out.println(t.getLink());
				System.out.println(t.getTitle());
				System.out.println(t.getComments());
				if (!thread.attr("data-url").contains("http")) {
					//System.out.println("https://www.reddit.com" + thread.attr("data-url"));
					t.setImgLink("https://www.reddit.com" + thread.attr("data-url"));
				} else {
					//System.out.println(thread.attr("data-url"));
					t.setImgLink(thread.attr("data-url"));
				}
				
				if (!t.getImgLink().contains("https://www.reddit.com")) {	
					if(t.getImgLink().contains("imgur.com")) {
						try {
							doc = Jsoup.connect(t.getImgLink()).ignoreContentType(true).timeout(50000).userAgent("Mozilla").get();
							Elements html = doc.getElementsByClass("post-image-placeholder");
							//System.out.println("https:" + html.attr("src"));
							t.setImgLink("https:" + html.attr("src"));
							//System.out.println("FOUND IMAGE!!!!!!!!!!");
						}catch(Exception e) {
							try {
								t.setImgLink(t.getImgLink().replace("http", "https"));
							} catch(Exception e2){
								e.printStackTrace();
								e2.printStackTrace();
								//System.out.println(e.getCause());
								System.out.println("Link already dirrects to a img: " +t.getImgLink());
							}
							
						}
					}
					
					try {
						//Open a URL Stream
						Response resultImageResponse = Jsoup.connect(
								t.getImgLink()).ignoreContentType(true).timeout(50000).execute();
						//System.out.println(thread.getLink());
						
						InputStream in = new ByteArrayInputStream(resultImageResponse.bodyAsBytes());
						BufferedImage bImageFromConvert = ImageIO.read(in);
						t.setImage(bImageFromConvert);
					} catch (org.jsoup.HttpStatusException e) {
						e.printStackTrace();
						System.out.println("Bad link, could not pull image");
						t.setImage(null);
						//continue;
						
					} catch(java.lang.IllegalArgumentException e) {
						e.printStackTrace();
						System.out.println("Bad link, Link was null");
						t.setImage(null);
						//continue;
					}
					
				} 
				System.out.println(t.getImgLink());
				if (t.getImage() == null) {
					System.out.println("image null" + "\t" + "Finally link: " + t.getImgLink());
					
				} else {
					System.out.println("good" + "\t" + "Finally link: " + t.getImgLink());
				}
				threads.add(t);
				System.out.println("==========================================================");
				index++;
				setProgress((int)(((double)this.index/27)*100));
				
			}
			
			System.out.println("Done, size: " + threads.size());
		} catch (IOException e) {
			e.printStackTrace();
			
		} 
		
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		this.getData(this.subReddit);
		return 1;
	}
	
	@Override
	  protected void process(final List<String> chunks) {
	    // Updates the messages text area
	    for (final String string : chunks) {

	    }
	  }
	
	@Override
	 public void done() {
		ui.setTotalNumCols(this.getThreads().size() / 3);
		for(int i = 0; i < ui.getTotalNumCols(); i++) {
			for(int j = 0; j < ui.getNumCols(); j++) {
				ui.getBoxes().add(new Rectangle2D.Double(
						ui.getContentBox().getX() + ((ui.getContentBox().getWidth()/ui.getNumRows()) * i),
						ui.getContentBox().getY() + ((ui.getContentBox().getHeight()/ui.getNumCols()) * j),
						ui.getContentBox().getWidth()/ui.getNumCols(),
						ui.getContentBox().getHeight()/ui.getNumRows()));
			}
		}		
		int width = (int)(ui.getBoxes().get(0).getWidth()*0.7);
		for (int i = 0; i < this.getThreads().size(); i++) {
			ui.getBi().makeThread(ui.getProgram().getGraphics(), (Graphics2D)ui.getProgram().getGraphics(),
					this.getThreads().get(i).getTitle(),
					this.getThreads().get(i).getComments(),
					width,
					this.getThreads().get(i).getImage());
		}
		ui.setCurrentRedditStat(RedditState.Page);
		System.out.println("switched to reddit state: " + ui.getCurrentRedditStat());
	}
	
	// test main
	public static void main(String[] args) {
		Program p = new Program();
		UiController ui = new UiController(p);
		RedditBoardBuilder rbb = new RedditBoardBuilder(ui, "r/frugalmalefashion/");
		rbb.getData("r/frugalmalefashion/");
		System.out.println("total number of threads: " + rbb.threads.size());
	}

	public List<Thread> getThreads() {
		return threads;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSubReddit() {
		return subReddit;
	}

	public void setSubReddit(String subReddit) {
		this.subReddit = subReddit;
	}

	
}
