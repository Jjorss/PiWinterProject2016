package model;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class QuoteBuilder {
	private String dailyQuotes = "";
	private String author = "";

	public void getData() {
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.eduro.com/").timeout(50000).userAgent("Mozilla").get();
			Elements pulledQuote = doc.getElementsByTag("dailyquote").get(0).getElementsByTag("p");
			System.out.println(pulledQuote.get(0).text());
			System.out.println(pulledQuote.get(1).getElementsByClass("author").text());
			this.setDailyQuotes(pulledQuote.get(0).text());
			this.setAuthor(pulledQuote.get(1).getElementsByClass("author").text());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		QuoteBuilder qb = new QuoteBuilder();
		qb.getData();
	}

	public String getDailyQuotes() {
		return dailyQuotes;
	}

	public void setDailyQuotes(String dailyQuotes) {
		this.dailyQuotes = dailyQuotes;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}