package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import model.DrawBuilder;
import model.QuoteBuilder;
import model.RedditBoardBuilder;
import model.RedditState;
import model.Resolution;
import model.State;
import model.WeatherMaker;
import view.Program;

public class UiController {
	
	private Program program;
	private RedditBoardBuilder rb;
	private BufferedImageController bi;
	private WeatherMaker weather;
	private QuoteBuilder qb;
	private DrawBuilder db;
	private State currentState = State.LOADING;
	private RedditState currentRedditState = RedditState.Home;
	
	private Rectangle2D sideBar;
	private Rectangle2D contentBox;
	// home screen rectangles
	private Rectangle2D weatherRect;
	private Rectangle2D tempRect;
	private Rectangle2D weatherDataRect;
	private Rectangle2D weatherSummaryRect;
	private Rectangle2D weatherIconRect;
	private Rectangle2D timeRect;
	private Rectangle2D displayRect; // temp bottom rect on home screen
	// reddit rectangles
	private Rectangle2D redditHomeRect;
	private RoundRectangle2D redditContainer;
	
	private List<Rectangle2D> boxes = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> iconBoxes = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> icons = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> pinnedGrid = new ArrayList<Rectangle2D>();
	
	
	private List<BufferedImage> posts = new ArrayList<BufferedImage>();
	private List<BufferedImage> pins = new ArrayList<BufferedImage>();
	
	
	private BufferedImage temperatureImage;
	private BufferedImage weatherDataImage;
	private BufferedImage summaryImage; 
	private BufferedImage timeImage;
	private BufferedImage quoteImage;
	private BufferedImage focusedThreadImage;
	
	private DateFormat dateFormatTime;
	private DateFormat dateFormatDate;
	private Date date;
	
	private int numRows;
	private int numCols;
	private int totalNumCols = 0;

	
	private String[] tempArray = new String[]{"H", "R", "D", "T", "T", "T"};
	private String[] tempPinnedArray = new String[]{"r/elderscrollsonline/", "Reddit", "r/frugalmalefashion/"};
	
	private String loadingThreadStatus = "0%";
	
	public UiController(Program program){
		this.program = program;
	}

	public void init() {
		bi = new BufferedImageController(program,this);
		this.sideBar = new Rectangle2D.Double(0,0, program.getWidth()*0.1, program.getHeight());
		this.contentBox = new Rectangle2D.Double(this.sideBar.getX()+this.sideBar.getWidth(), 0,
				program.getWidth() - this.sideBar.getWidth(), this.program.getHeight());
		
		double iconBoxesWidth = this.sideBar.getWidth() * 0.7;
		for (int i = 0; i < 5; i++) {
			this.iconBoxes.add(new Rectangle2D.Double(
					(this.sideBar.getX() + (this.sideBar.getWidth()/2)) - (iconBoxesWidth/2),
					this.sideBar.getY() + ((this.sideBar.getHeight()/5)*i),
					iconBoxesWidth,
					this.sideBar.getHeight()/5));
		}
		double iconWidth = iconBoxesWidth * 0.7;
		double iconHeight = iconWidth;
		for (int i = 0; i < 5; i++) {
			this.icons.add(new Rectangle2D.Double(
					(this.iconBoxes.get(i).getX() + (this.iconBoxes.get(i).getWidth()/2)) - (iconWidth/2),
					(this.iconBoxes.get(i).getY() + (this.iconBoxes.get(i).getWidth())) - (iconHeight/2),
					iconWidth,
					iconHeight));
		}
		
		double weatherWidth = this.contentBox.getWidth()*0.5;
		weatherRect = new Rectangle2D.Double(
				this.contentBox.getX() + (this.contentBox.getWidth()/2),
				this.contentBox.getY(),
				weatherWidth,
				this.contentBox.getHeight()/2);
		
		double tempWidth = this.weatherRect.getWidth()*0.5;
		tempRect = new Rectangle2D.Double(
				this.weatherRect.getX(),
				this.weatherRect.getY(),
				tempWidth,
				this.weatherRect.getHeight()*0.5);
		
		weather = new WeatherMaker(bi);
		weather.getData();
		temperatureImage = bi.makeTemperature(program.getGraphics(),
				(Graphics2D)program.getGraphics(), ""+weather.getTemperature(),
				"" + weather.getFeelsLike());
		weatherDataRect = new Rectangle2D.Double(
				tempRect.getX() + tempRect.getWidth(),
				weatherRect.getY(),
				weatherRect.getWidth()*0.5,
				weatherRect.getHeight() * 0.6);
		
		weatherDataImage = bi.makeWeatherData(program.getGraphics(), (Graphics2D)program.getGraphics(),
				this.weather.getPrecipProbability() + "",
				this.weather.getDewPoint() + "",
				this.weather.getHumidity() + "",
				this.weather.getWindSpeed() + "",
				this.weather.getNearestStormDistance()+ "");
		weatherSummaryRect = new Rectangle2D.Double(this.weatherRect.getX(),
				this.weatherRect.getY() + this.tempRect.getHeight(),
				this.weatherRect.getWidth()/2,
				this.weatherRect.getHeight() - this.tempRect.getHeight());
		summaryImage = bi.makeWeatherSummary(program.getGraphics(), 
				(Graphics2D)program.getGraphics(), 
				this.weather.getSummary(),
				this.weather.getHourlySummary());
		weatherIconRect = new Rectangle2D.Double(
				this.weatherSummaryRect.getX() + this.weatherSummaryRect.getWidth(),
				this.weatherDataRect.getY() + this.weatherDataRect.getHeight(),
				this.weatherRect.getWidth() - this.weatherSummaryRect.getWidth(),
				this.weatherRect.getHeight() - this.weatherDataRect.getHeight());
		timeRect = new Rectangle2D.Double(
				this.contentBox.getX(),
				this.contentBox.getY(),
				this.contentBox.getWidth()/2,
				this.contentBox.getHeight()/2);
		displayRect = new Rectangle2D.Double(
				this.contentBox.getX(),
				this.contentBox.getY() + this.timeRect.getHeight(),
				this.contentBox.getWidth(),
				this.contentBox.getHeight() - this.timeRect.getHeight());
		
		qb = new QuoteBuilder();
		qb.getData();
		this.quoteImage = bi.makeQuote(program.getGraphics(), (Graphics2D)program.getGraphics()
				, qb.getDailyQuotes(), qb.getAuthor());
		
		db = new DrawBuilder();
		dateFormatTime = new SimpleDateFormat("HH:mm:ss");
		dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
		date = new Date();
		//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		this.timeImage = bi.makeTime(program.getGraphics(), (Graphics2D)program.getGraphics(),
				dateFormatTime.format(date), dateFormatDate.format(date));
		double redditHomeWidth = this.contentBox.getWidth()*0.8;
		redditHomeRect = new Rectangle2D.Double(
				(this.contentBox.getX() + this.contentBox.getWidth()/2) - (redditHomeWidth/2),
				this.contentBox.getY() + (this.contentBox.getHeight()*0.05),
				redditHomeWidth,
				this.contentBox.getHeight()*0.6);
		numCols = 2;
		numRows = 2;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < numCols; j++) {
				this.pinnedGrid.add(new Rectangle2D.Double(
						this.redditHomeRect.getX() + ((this.redditHomeRect.getWidth()/numRows) * i),
						this.redditHomeRect.getY() + ((this.redditHomeRect.getHeight()/numCols) * j),
						this.redditHomeRect.getWidth()/numCols,
						this.redditHomeRect.getHeight()/numRows));
			}
		}
		for(int i = 0; i < this.tempPinnedArray.length; i++) {
			this.pins.add(this.bi.makePin(program.getGraphics(), (Graphics2D)program.getGraphics(), this.tempPinnedArray[i]));
		}
		double redditContainerWidth = this.contentBox.getWidth()*0.75;
		double redditContainerHeight = this.contentBox.getHeight() * 0.9;
		redditContainer = new RoundRectangle2D.Double(
				(this.contentBox.getX() + (this.contentBox.getWidth()/2)) - (redditContainerWidth/2),
				(this.contentBox.getY() + (this.contentBox.getHeight()/2)) - (redditContainerHeight/2),
				redditContainerWidth,
				redditContainerHeight,
				50,
				50);
		
		System.out.println("Initialized");		
		this.currentState = State.HOME;
		
	}
	
	public void initSubReddit(String subReddit) {
		this.posts.clear();
		this.boxes.clear();
		this.numRows = 3;
		this.numCols = 3;
		this.rb = new RedditBoardBuilder(this, subReddit);
		this.rb.execute();
		
//		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>() {
//
//			@Override
//			protected Integer doInBackground() throws Exception {
//				currentRedditState = RedditState.Loading;
//				System.out.println("switched to reddit state: " + currentRedditState);
//				rb.getData(subReddit);
//				return 1;
//			}
//			@Override
//			 public void done() {
//				totalNumCols = rb.getThreads().size() / 3;
//				for(int i = 0; i < totalNumCols; i++) {
//					for(int j = 0; j < numCols; j++) {
//						boxes.add(new Rectangle2D.Double(contentBox.getX() + ((contentBox.getWidth()/numRows) * i),
//								contentBox.getY() + ((contentBox.getHeight()/numCols) * j),
//								contentBox.getWidth()/numCols,
//								contentBox.getHeight()/numRows));
//					}
//				}		
//				int width = (int)(boxes.get(0).getWidth()*0.7);
//				for (int i = 0; i < rb.getThreads().size(); i++) {
//					bi.makeThread(program.getGraphics(), (Graphics2D)program.getGraphics(),
//							rb.getThreads().get(i).getTitle(),
//							rb.getThreads().get(i).getComments(),
//							width,
//							rb.getThreads().get(i).getImage());
//				}
//				currentRedditState = RedditState.Page;
//				System.out.println("switched to reddit state: " + currentRedditState);
//			}
//			@Override
//			protected void process(List<String> chunks) {
//			    // Messages received from the doInBackground() (when invoking the publish() method)
//			}
//			
//		};
//		worker.execute();
		
	}
	
	public void loop() {
		switch(this.currentState) {
		case LOADING:
			this.init();
			
			break;
		case HOME:
			date = new Date();
			this.timeImage = bi.makeTime(program.getGraphics(), (Graphics2D)program.getGraphics(),
					dateFormatTime.format(date), dateFormatDate.format(date));
			break;
		case REDDIT:
			break;
		case DRAW:
			break;
		default:
			break;
		
		}
	}
	
	public void moveLeft(double speed) {
		for (Rectangle2D box : boxes) {
			box.setRect(box.getX()-speed, box.getY() ,box.getWidth() ,box.getHeight());
		}
	}
	
	public void moveRight(double speed) {
		for (Rectangle2D box : boxes) {
			box.setRect(box.getX()+speed, box.getY() ,box.getWidth() ,box.getHeight());
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		switch(this.currentState) {
		case LOADING:
			break;
		case HOME:
			this.renderSideBar(g2);
			this.renderContentBox(g2);
			this.renderIconsBoxes(g2);
			this.renderIcons(g2);
			this.renderWetherRect(g2);
			this.renderTime(g2);
			this.renderDisplay(g2);
			break;
		case REDDIT:
			this.renderContentBox(g2);
			switch(this.currentRedditState) {
			case Home:
				this.renderContentBox(g2);
				this.renderRedditHome(g2);
				this.renderPinnedGrid(g2);
				this.renderPinnedSubs(g2);
				break;
			case Loading:
				this.renderContentBox(g2);
				this.renderRedditLoading(g2);
				break;
			case Page:
				this.renderContentBox(g2);
				this.renderThreads(g2, g2);
				this.renderBoxes(g2);
				break;
			case Image:
				this.renderContentBox(g2);
				this.renderThreads(g2, g2);
				this.renderBoxes(g2);
				this.renderRedditImage(g2);
				break;
			default:
				this.renderContentBox(g2);
				this.renderSideBar(g2);
				this.renderIconsBoxes(g2);
				this.renderIcons(g2);
				break;
			}
			
			this.renderSideBar(g2);
			this.renderIconsBoxes(g2);
			this.renderIcons(g2);
			break;
		case DRAW:
			this.renderContentBox(g2);
			this.renderSideBar(g2);
			this.renderIconsBoxes(g2);
			this.renderIcons(g2);
			this.renderDrawing(g2);
			break;
		default:
			break;
		}
	
	}
	
	public void renderRedditImage(Graphics2D g2) {
		g2.setColor(new Color(255, 255, 255, 200));
		g2.draw(redditContainer);
		g2.fill(redditContainer);
		
		if (this.focusedThreadImage != null) {
			BufferedImage img = this.focusedThreadImage;
			Resolution res = this.bi.scaleImage(img.getWidth(),
					img.getHeight(),
					(int)(redditContainer.getWidth()*0.9),
					(int)(redditContainer.getHeight()*0.9));
			int width = res.getWidth();
			
			int height = res.getHeight();
			g2.drawImage(
					img,
					(int)( redditContainer.getX() + (redditContainer.getWidth()/2) - (width/2) ),
					(int)(int)( redditContainer.getY() + (redditContainer.getHeight()/2) - (height/2) ),
					width,
					height,
					program);
			
		}
	}
	
	public void renderRedditHome(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		g2.draw(redditHomeRect);
		//g2.fill(redditHomeRect);
		//System.out.println(this.redditHomeRect);

	}
	public void renderPinnedGrid(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		for (Rectangle2D pinned : this.pinnedGrid) {
			g2.draw(pinned);
			//g2.fill(pinned);
		}
		
	}
	
	public void renderPinnedSubs(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		int index = 0;
		for(BufferedImage pin : this.pins) {
			Rectangle2D cell = this.getPinnedGrid().get(index);
			g2.drawImage(pin,
					(int)( cell.getX() + (cell.getWidth()/2) - (pin.getWidth()/2) ),
					(int)(int)( cell.getY() + (cell.getHeight()/2) - (pin.getHeight()/2) ),
					pin.getWidth(),
					pin.getHeight(),
					program);
			index++;
		}
	}
	
	public void renderRedditLoading(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, (int)(this.contentBox.getHeight()*0.1)));
		g2.setColor(Color.WHITE);
		g2.drawString(this.rb.getProgress()+"%",
				(int)this.contentBox.getWidth()/2, (int)this.contentBox.getHeight()/2);
		
	}
	
	public void renderDrawing(Graphics2D g2) {
		g2.setColor(db.getColor());
		g2.setStroke(new BasicStroke(db.getThickness()));
		for(Line2D line : db.getPixels()) {
			g2.draw(line);
		}
	}
	
	public void renderThreads(Graphics2D g2, Graphics g) {		
		//System.out.println(width);
		int index = 0;
		for (Rectangle2D box : boxes) {
			BufferedImage bi = this.posts.get(index);
			//System.out.println(box.getX());
			//System.out.println(box.getY());
			if (this.rb.getThreads().get(index).isShowImage() && this.rb.getThreads().get(index).getImage()!=null) {
				BufferedImage tn = this.rb.getThreads().get(index).getImage();
				Resolution res = this.bi.scaleImage(tn.getWidth(),
						tn.getHeight(),
						(int)(box.getWidth()*0.9),
						(int)(box.getHeight()*0.9));
				int width = res.getWidth();
				
				int height = res.getHeight();
				g2.drawImage(
						tn,
						(int)( box.getX() + (box.getWidth()/2) - (width/2) ),
						(int)(int)( box.getY() + (box.getHeight()/2) - (height/2) ),
						width,
						height,
						program);
			} else {
				g2.drawImage(bi,
						(int)( box.getX() + (box.getWidth()/2) - (bi.getWidth()/2) ),
						(int)(int)( box.getY() + (box.getHeight()/2) - (bi.getHeight()/2) ),
						bi.getWidth(),
						bi.getHeight(),
						program);
			}
			
			index++;
		}
		
	}
	
	
	

	
	public void renderDisplay(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(this.displayRect);
		g2.drawImage(this.quoteImage,
				(int)(this.displayRect.getX() + (this.displayRect.getWidth()/2)) - (this.quoteImage.getWidth()/2),
				(int)(this.displayRect.getY() + (this.displayRect.getHeight()/2)) - (this.quoteImage.getHeight()/2),
				(int)this.quoteImage.getWidth(),
				(int)this.quoteImage.getHeight(),
				program);
//		int displayWidth = (int)(this.displayRect.getWidth()*0.95);
//		int dsiplayHeight = (int)(this.displayRect.getHeight()*0.95);
//		g2.setColor(new Color(255, 255, 255, 128));
//		g2.fillRoundRect(
//	        	(int)(this.displayRect.getX() + (this.displayRect.getWidth()/2)) - (displayWidth/2),
//	        	(int)(this.displayRect.getY() + (this.displayRect.getHeight()/2)) - (dsiplayHeight/2),
//	        	displayWidth,
//	        	dsiplayHeight,
//	            15,
//	            10);
	}
	
	public void renderTime(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(timeRect);
		g2.drawImage(this.timeImage,
				(int)(this.timeRect.getX() + (this.timeRect.getWidth()/2)) - (this.timeImage.getWidth()/2),
				(int)(this.timeRect.getY() + (this.timeRect.getHeight()/2)) - (this.timeImage.getHeight()/2),
				(int)this.timeImage.getWidth(),
				(int)this.timeImage.getHeight(),
				program);
	}
	
	public void renderWetherRect(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(weatherRect);
		g2.draw(tempRect);
		g2.draw(weatherDataRect);
		g2.draw(weatherSummaryRect);
		g2.draw(weatherIconRect);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 50));
		g2.drawImage(this.temperatureImage,
				(int)(this.tempRect.getX() + (this.tempRect.getWidth()/2)) - (this.temperatureImage.getWidth()/2),
				(int)(this.tempRect.getY() + (this.tempRect.getHeight()/2)) - (this.temperatureImage.getHeight()/2),
				(int)this.temperatureImage.getWidth(),
				(int)this.temperatureImage.getHeight(),
				program);
		g2.drawImage(this.weatherDataImage,
				(int)(this.weatherDataRect.getX() + (this.weatherDataRect.getWidth()/2)) - (this.weatherDataImage.getWidth()/2),
				(int)(this.weatherDataRect.getY() + (this.weatherDataRect.getHeight()/2)) - (this.weatherDataImage.getHeight()/2),
				(int)this.weatherDataImage.getWidth(),
				(int)this.weatherDataImage.getHeight(),
				program);
		g2.drawImage(summaryImage,
				(int)(this.weatherSummaryRect.getX() + (this.weatherSummaryRect.getWidth()/2)) - (this.summaryImage.getWidth()/2),
				(int)(this.weatherSummaryRect.getY() + (this.weatherSummaryRect.getHeight()/2)) - (this.summaryImage.getHeight()/2),
				(int)this.summaryImage.getWidth(),
				(int)this.summaryImage.getHeight(),
				program);
		g2.setColor(new Color(255, 255, 255, 128));
		int iconWidth = (int)(this.weatherIconRect.getWidth() * 0.95);
		int iconHeight = (int)(this.weatherIconRect.getHeight() * 0.95);
        g2.fillRoundRect(
        	(int)(this.weatherIconRect.getX() + (this.weatherIconRect.getWidth()/2)) - (iconWidth/2),
        	(int)(this.weatherIconRect.getY() + (this.weatherIconRect.getHeight()/2)) - (iconHeight/2),
        	iconWidth,
        	iconHeight,
            15,
            10);
        Resolution res = bi.scaleImageUp((int)weather.getWeatherImage().getWidth(),
        		(int)weather.getWeatherImage().getHeight(), (int)(this.weatherIconRect.getWidth()),
        		(int)(this.weatherIconRect.getHeight()));
        int weatherImageWidth = res.getWidth();
        int weatherImageHeight = res.getHeight();
		g2.drawImage(weather.getWeatherImage(),
				(int)(this.weatherIconRect.getX() + (this.weatherIconRect.getWidth()/2)) - (weatherImageWidth/2),
				(int)(this.weatherIconRect.getY() + (this.weatherIconRect.getHeight()/2)) - (weatherImageHeight/2),
				weatherImageWidth,
				weatherImageHeight,
				program);
	}
	
	public void renderBoxes(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		for (Rectangle2D box : boxes){
			g2.draw(box);
			//System.out.println("Drawing boxes");
		}
	}
	
	public void renderIconsBoxes(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		for (Rectangle2D icon : iconBoxes) {
			g2.draw(icon);
		}
	}
	
	public void renderIcons(Graphics2D g2) {
		g2.setFont(new Font("Arial", Font.BOLD, 50));
		int index = 0;
		for (Rectangle2D icon : icons) {
			g2.fill(icon);
			g2.setColor(Color.white);
			g2.drawString(tempArray[index], (float)icon.getCenterX(), (float)icon.getCenterY());
			g2.setColor(Color.black);
			index++;
		}
		
	}
	
	public void renderSideBar(Graphics2D g2) {
		g2.setColor(Color.DARK_GRAY);
		g2.draw(sideBar);
		g2.fill(sideBar);
	}
	
	public void renderContentBox(Graphics2D g2) {
		g2.setColor(Color.LIGHT_GRAY);
		g2.draw(contentBox);
		g2.fill(contentBox);
	}

	
	public void handleDrag(Point p) {
		if (this.currentState == State.REDDIT) {
			if(p.getX() < program.getSourcePoint().getX()) {
				this.moveLeft(program.getSourcePoint().getX() - p.getX());
			} else {
				this.moveRight(p.getX() - program.getSourcePoint().getX());
			}
		}
		if(this.currentState == State.DRAW) {
			db.handleDraw(p);
		}
	}
	
	public void handleOnClick(Point p) {
		if (sideBar.contains(p)) {
			this.handleClickedSideButton(p);
		}
		if (this.contentBox.contains(p)) {
			switch(this.currentState) {
			case DRAW:
				break;
			case HOME:
				break;
			case LOADING:
				break;
			case REDDIT:
				switch(this.currentRedditState) {
				case Home:
					this.handleGoToSubReddit(p);
					break;
				case Loading:
					break;
				case Page:
					this.handleShowImage(p);
					break;
				case Image:
					handleHideImage();
					break;
				default:
					break;
				
				}
				break;
			default:
				break;
			
			}	
		}
	}
	
	public void handleGoToSubReddit(Point p) {
		for(int i = 0; i < this.pinnedGrid.size(); i++) {
			Rectangle2D box = this.pinnedGrid.get(i);
			if(box.contains(p)) {
				// change state to loading
				this.currentRedditState = RedditState.Loading;
				// pull threads
				// change state back at end of method call
				this.initSubReddit(this.tempPinnedArray[i]);
			}
		}
	}
	
	
	public void handleShowImage(Point p) {
		for(int i = 0; i < this.boxes.size(); i++) {
			Rectangle2D box = this.boxes.get(i);
			if(box.contains(p) && this.rb.getThreads().get(i).getImage()!=null) {
				if (this.rb.getThreads().get(i).isShowImage()) {
					this.rb.getThreads().get(i).setShowImage(false);
					this.currentRedditState = RedditState.Page;
				} else {
					//this.rb.getThreads().get(i).setShowImage(true);
					this.setFocusedThreadImage(this.rb.getThreads().get(i).getImage());
					this.currentRedditState = RedditState.Image;
				}
			}
		}
	}
	
	public void handleHideImage() {
		this.currentRedditState = RedditState.Page;
	}
	
	public void handleClickedSideButton(Point p) {
		int index = 0;
		for(Rectangle2D icon : icons) {
			if(icon.contains(p)) {
				switch(index) {
				case 0:
					this.currentState = State.HOME;
					System.out.println("Switching to home");
					break;
				case 1:
					this.currentState = State.REDDIT;
					this.currentRedditState = RedditState.Home;
					System.out.println("swtiching to reddit");
					break;
				case 2:
					this.currentState = State.DRAW;
					System.out.println("Switicng to draw board");
				default:
					break;
				}
				break;
			}
			index++;
		}
	}
	
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	public WeatherMaker getWeather() {
		return weather;
	}

	public void setWeather(WeatherMaker weather) {
		this.weather = weather;
	}

	public Rectangle2D getSideBar() {
		return sideBar;
	}

	public void setSideBar(Rectangle2D sideBar) {
		this.sideBar = sideBar;
	}

	public Rectangle2D getContentBox() {
		return contentBox;
	}

	public void setContentBox(Rectangle2D contentBox) {
		this.contentBox = contentBox;
	}

	public Rectangle2D getWeatherRect() {
		return weatherRect;
	}

	public void setWeatherRect(Rectangle2D weatherRect) {
		this.weatherRect = weatherRect;
	}

	public Rectangle2D getTempRect() {
		return tempRect;
	}

	public void setTempRect(Rectangle2D tempRect) {
		this.tempRect = tempRect;
	}

	public Rectangle2D getWeatherDataRect() {
		return weatherDataRect;
	}

	public void setWeatherDataRect(Rectangle2D weatherDataRect) {
		this.weatherDataRect = weatherDataRect;
	}

	public Rectangle2D getWeatherSummaryRect() {
		return weatherSummaryRect;
	}

	public void setWeatherSummaryRect(Rectangle2D weatherSummaryRect) {
		this.weatherSummaryRect = weatherSummaryRect;
	}

	public Rectangle2D getWeatherIconRect() {
		return weatherIconRect;
	}

	public void setWeatherIconRect(Rectangle2D weatherIconRect) {
		this.weatherIconRect = weatherIconRect;
	}

	public Rectangle2D getTimeRect() {
		return timeRect;
	}

	public void setTimeRect(Rectangle2D timeRect) {
		this.timeRect = timeRect;
	}

	public Rectangle2D getDisplayRect() {
		return displayRect;
	}

	public void setDisplayRect(Rectangle2D displayRect) {
		this.displayRect = displayRect;
	}

	public List<Rectangle2D> getIconBoxes() {
		return iconBoxes;
	}

	public void setIconBoxes(List<Rectangle2D> iconBoxes) {
		this.iconBoxes = iconBoxes;
	}

	public BufferedImage getWeatherDataImage() {
		return weatherDataImage;
	}

	public void setWeatherDataImage(BufferedImage weatherDataImage) {
		this.weatherDataImage = weatherDataImage;
	}

	public List<BufferedImage> getPosts() {
		return posts;
	}

	public void setPosts(List<BufferedImage> posts) {
		this.posts = posts;
	}
	public List<Rectangle2D> getBoxes() {
		return boxes;
	}

	public RedditState getCurrentRedditStat() {
		return currentRedditState;
	}

	public void setCurrentRedditStat(RedditState currentRedditStat) {
		this.currentRedditState = currentRedditStat;
	}

	public List<Rectangle2D> getPinnedGrid() {
		return pinnedGrid;
	}

	public void setPinnedGrid(List<Rectangle2D> pinnedGrid) {
		this.pinnedGrid = pinnedGrid;
	}

	public BufferedImage getFocusedThreadImage() {
		return focusedThreadImage;
	}

	public void setFocusedThreadImage(BufferedImage focusedThreadImage) {
		this.focusedThreadImage = focusedThreadImage;
	}

	public String getLoadingThreadStatus() {
		return loadingThreadStatus;
	}

	public void setLoadingThreadStatus(String loadingThreadStatus) {
		this.loadingThreadStatus = loadingThreadStatus;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public RedditBoardBuilder getRb() {
		return rb;
	}

	public void setRb(RedditBoardBuilder rb) {
		this.rb = rb;
	}

	public BufferedImageController getBi() {
		return bi;
	}

	public void setBi(BufferedImageController bi) {
		this.bi = bi;
	}

	public QuoteBuilder getQb() {
		return qb;
	}

	public void setQb(QuoteBuilder qb) {
		this.qb = qb;
	}

	public DrawBuilder getDb() {
		return db;
	}

	public void setDb(DrawBuilder db) {
		this.db = db;
	}

	public RedditState getCurrentRedditState() {
		return currentRedditState;
	}

	public void setCurrentRedditState(RedditState currentRedditState) {
		this.currentRedditState = currentRedditState;
	}

	public Rectangle2D getRedditHomeRect() {
		return redditHomeRect;
	}

	public void setRedditHomeRect(Rectangle2D redditHomeRect) {
		this.redditHomeRect = redditHomeRect;
	}

	public RoundRectangle2D getRedditContainer() {
		return redditContainer;
	}

	public void setRedditContainer(RoundRectangle2D redditContainer) {
		this.redditContainer = redditContainer;
	}

	public List<Rectangle2D> getIcons() {
		return icons;
	}

	public void setIcons(List<Rectangle2D> icons) {
		this.icons = icons;
	}

	public List<BufferedImage> getPins() {
		return pins;
	}

	public void setPins(List<BufferedImage> pins) {
		this.pins = pins;
	}

	public BufferedImage getTemperatureImage() {
		return temperatureImage;
	}

	public void setTemperatureImage(BufferedImage temperatureImage) {
		this.temperatureImage = temperatureImage;
	}

	public BufferedImage getSummaryImage() {
		return summaryImage;
	}

	public void setSummaryImage(BufferedImage summaryImage) {
		this.summaryImage = summaryImage;
	}

	public BufferedImage getTimeImage() {
		return timeImage;
	}

	public void setTimeImage(BufferedImage timeImage) {
		this.timeImage = timeImage;
	}

	public BufferedImage getQuoteImage() {
		return quoteImage;
	}

	public void setQuoteImage(BufferedImage quoteImage) {
		this.quoteImage = quoteImage;
	}

	public DateFormat getDateFormatTime() {
		return dateFormatTime;
	}

	public void setDateFormatTime(DateFormat dateFormatTime) {
		this.dateFormatTime = dateFormatTime;
	}

	public DateFormat getDateFormatDate() {
		return dateFormatDate;
	}

	public void setDateFormatDate(DateFormat dateFormatDate) {
		this.dateFormatDate = dateFormatDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getTotalNumCols() {
		return totalNumCols;
	}

	public void setTotalNumCols(int totalNumCols) {
		this.totalNumCols = totalNumCols;
	}

	public String[] getTempArray() {
		return tempArray;
	}

	public void setTempArray(String[] tempArray) {
		this.tempArray = tempArray;
	}

	public String[] getTempPinnedArray() {
		return tempPinnedArray;
	}

	public void setTempPinnedArray(String[] tempPinnedArray) {
		this.tempPinnedArray = tempPinnedArray;
	}

	public void setBoxes(List<Rectangle2D> boxes) {
		this.boxes = boxes;
	}

}
