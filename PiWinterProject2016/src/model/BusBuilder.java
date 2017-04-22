package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BusBuilder {

	private List<String> times = new ArrayList<String>();
	private String title = "Stop Smith NB on Weekend West Loop bus arrival times";
	
	public void getData() {
		Document doc = null;
		times.clear();
		title = "";
		try {
			doc = Jsoup.connect("https://www.udshuttle.com/simple/routes/636/stops/226640")
					.timeout(50000).userAgent("Mozilla").get();
			Elements list = doc.getElementsByTag("ul");
			Elements title = doc.getElementsByTag("h2");
			this.title = title.text();
			Element time = list.get(1);
			Elements times =  time.getElementsByTag("li");
			
			for(Element t : times){
				this.times.add(t.text());
			}
			
			System.out.println(this.title);
			System.out.println(this.times);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getTimes() {
		return times;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
//	public static void main(String[] args) {
//		BusBuilder bb = new BusBuilder();
//		bb.getData();
//
//	}

}
