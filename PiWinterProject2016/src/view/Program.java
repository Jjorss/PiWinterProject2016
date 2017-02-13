package view;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.UiController;
import model.DrawState;
import model.State;

public class Program extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private UiController ui = new UiController(this);
	private Point sourcePoint = null;
	
	 MouseAdapter ma = new MouseAdapter() {
		@Override
        public void mouseReleased(MouseEvent e) {
           if (ui.getCurrentState() == State.DRAW && 
        		   ui.getDb().getCurrentState() == DrawState.DRAW) {
        	   ui.getDb().setCurrentState(DrawState.END);
           }
           ui.handleOnRelease();
            System.out.println("Moused Released!");
        }
		@Override
		public void mousePressed(MouseEvent e) {
			setSourcePoint(e.getPoint());
			if (ui.getCurrentState() == State.DRAW) {
				ui.getDb().setCurrentState(DrawState.START);
				ui.getDb().handleDraw(e.getPoint());
				System.out.println("Moused Pressed !");
			}
			ui.handleOnPress();
			
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			ui.handleOnClick(e.getPoint());
			System.out.println("Mouse Clicked !");
		}
	
	};
	
	transient MouseAdapter mad = new MouseAdapter() {
		
		@Override
		public void mouseDragged(MouseEvent e) {
			ui.handleDrag(e.getPoint());
			if (ui.getDb().getCurrentState() == DrawState.START) {
				ui.getDb().setCurrentState(DrawState.DRAW);
			}
			System.out.println("Moused Dragged!");
			 ActionListener getSource = new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		                setSourcePoint(e.getPoint());
		                
		            }
		        };
		       Timer timer = new Timer(15 ,getSource);
		        timer.setRepeats(false);
		        timer.start();
			//System.out.println("dragging");
		}
	};

	public Program() {
		super();
		JFrame frame = new JFrame("Dash Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setFocusable(true);
        frame.getContentPane().add(this);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.addKeyListener(new KeyListener() {

        	@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
					
				}
				
				System.out.println("IM PRESsING A KEY");
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        this.addMouseListener(ma);
        this.addMouseMotionListener(mad);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch(this.ui.getCurrentState()) {
        case LOADING:
        	g.drawString("Loading...", this.WIDTH/2, this.HEIGHT/2);
			break;
        case HOME:
			this.ui.render(g);
			break;
        case REDDIT:
        	this.ui.render(g);
        	break;
        case DRAW:
        	this.ui.render(g);
        	break;
		default:
			break;
        
        }
        
	}
	
	public void loop() {
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 20;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; 
		long lastFpsTime = 0;
		int fps = 0;   
		   // keep looping round til the game ends
		   while (true) {
		      // work out how long its been since the last update, this
		      // will be used to calculate how far the entities should
		      // move this loop
		      long now = System.nanoTime();
		      long updateLength = now - lastLoopTime;
		      lastLoopTime = now;
		      double delta = updateLength / ((double)OPTIMAL_TIME);

		      // update the frame counter
		      lastFpsTime += updateLength;
		      fps++;
		      
		      // update our FPS counter if a second has passed since
		      // we last recorded
		      if (lastFpsTime >= 1000000000) {
		         System.out.println("(FPS: "+fps+")");
		         lastFpsTime = 0;
		         fps = 0;
		      }
		      
		      // update the game logic
		      updateGame();
		      
		      // draw everyting
		      repaint();
		      
		      // we want each frame to take 10 milliseconds, to do this
		      // we've recorded when we started the frame. We add 10 milliseconds
		      // to this and then factor in the current time to give 
		      // us our final value to wait for
		      // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
		      try{
		    	  Thread.sleep( (System.nanoTime()-lastLoopTime + OPTIMAL_TIME)/1000000 );
		    	  
		      }catch(Exception e) {
		    	  e.printStackTrace();
		    	  System.out.println((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
		    	  long sleepTime = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000;
		    	  try {
					Thread.sleep(-1*sleepTime);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }
		   }
	}
	
	
	public void updateGame() {
		this.ui.loop();
	}
	
	public static void main(String[] args) {
		Program program = new Program();
		program.loop();
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public Point getSourcePoint() {
		return sourcePoint;
	}

	public void setSourcePoint(Point sourcePoint) {
		this.sourcePoint = sourcePoint;
	}

}
