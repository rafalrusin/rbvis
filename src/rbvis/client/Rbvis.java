package rbvis.client;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Rbvis implements EntryPoint {
	private Canvas canvasScreen;
	private Context2d contextScreen;
	
	final CssColor backgroundColor = CssColor.make("rgba(255,255,255,1.0)");
	final CssColor drawColor = CssColor.make("rgba(0,0,0,1.0)");
	final CssColor blackColor = CssColor.make("rgba(0,0,0,1.0)");
	final CssColor redColor = CssColor.make("rgba(255,0,0,1.0)");
	

	int width = 800, height = 400;
	int i = 0;
	
	String message = "";

	private RBTree tree;
	
	private long frameTime, startTime;
	private boolean refreshFrame = false;
	private boolean autoplay = false;
	
	public void onModuleLoad() {
	   canvasScreen = Canvas.createIfSupported();
	     
	    if (canvasScreen == null) {
	      RootPanel.get().add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
	      return;
	    }
	    canvasScreen.setCoordinateSpaceHeight(height);
	    canvasScreen.setCoordinateSpaceWidth(width);
	    canvasScreen.setSize(width + "px", height + "px");
	    
	    contextScreen = canvasScreen.getContext2d();
	    
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        tree = new RBTree(comparator);

        Button autoplayButton = new Button("Auto Play", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startAutoplay();
			}
        });
	    RootPanel.get().add(autoplayButton);
	    
        Button singleStepButton = new Button("Single Step", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				singleStep();
			}
        });
	    RootPanel.get().add(singleStepButton);

	    
	    RootPanel.get().add(canvasScreen);
	    
	    final Timer timer = new Timer() {
	      @Override
	      public void run() {
	        doUpdate();
	      }
	    };
	    timer.scheduleRepeating(50);
	    
	    startAutoplay();
	}
	
	private void startAutoplay() {
	    frameTime = new Date().getTime();
	    startTime = frameTime;
	    autoplay = true;
	}
	
	private void singleStep() {
		autoplay = false;
		processFrame();
	}
	
	private void drawTree(RBNode n, float x1, float x2, float y1, float y2) {
		if (n != null) {
			if (n.left != null) {
				drawConnection((x1 + x2)/2.0f, y1, (x2 - x1)*1.f/4.0f + x1, y2);
			}
			if (n.right != null) {
				drawConnection((x1 + x2)/2.0f, y1, (x2 - x1)*3.f/4.0f + x1, y2);
			}
			drawNode("" + n.value, n.isBlack, (x1 + x2)/2.0f, y1);
			drawTree(n.left, (x2 - x1)*0.f/4.0f + x1, (x2 - x1)*2.f/4.0f + x1, y2, y2 + (y2-y1));
			drawTree(n.right, (x2 - x1)*2.f/4.0f + x1, (x2 - x1)*4.f/4.0f + x1, y2, y2 + (y2-y1));
		}
	}
	
	private void drawConnection(float x1, float y1, float x2, float y2) {
		contextScreen.setStrokeStyle(drawColor);
		contextScreen.beginPath();
		contextScreen.moveTo(x1, y1);
		contextScreen.lineTo(x2, y2);
		contextScreen.closePath();
		contextScreen.stroke();
	}

	private void drawNode(String label, boolean isBlack, float x, float y) {
		contextScreen.beginPath();
		contextScreen.arc(x, y, 15, 0, 2*Math.PI);
		contextScreen.closePath();
	    contextScreen.setFillStyle(isBlack ? blackColor : redColor);
	    contextScreen.fill();
		contextScreen.setStrokeStyle(drawColor);
		contextScreen.stroke();
	    contextScreen.setFillStyle(isBlack ? backgroundColor : blackColor);
		contextScreen.fillText(label, x - label.length() * 4 + 2, y + 4);
	}

	private void doUpdate() {
		long currentTime = new Date().getTime();
		
		if (autoplay) {
			while (frameTime < currentTime) {
				processFrame();
				frameTime += 200;
			}
		}
		
		if (refreshFrame) {
			refreshFrame = false;			
		    contextScreen.setFillStyle(backgroundColor);
			contextScreen.fillRect(0, 0, width, height);
			drawTree(tree.getRoot(), 20, width-20, 40, 75);
			
		    contextScreen.setFillStyle(blackColor);
			contextScreen.fillText(message, 10, 10);
		}
	}

	private void processFrame() {
		int range = 64;
		i = (i + 1)%range;
		if (i < range/2) {
			tree.insert(i);
			message = "Insert " + i;
		} else {
			int j = i - range/2;
			tree.remove(j);
			message = "Remove " + j;
		}
		message = message + " Time: " + (frameTime - startTime);
		refreshFrame = true;
	}
}
