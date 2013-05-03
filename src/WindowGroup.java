import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.util.*;
import java.util.List;

import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class WindowGroup extends JFrame implements Group {

	private static final long serialVersionUID = 1L;
    private Image buffer;
    private JComponent canvas;
    private JTextArea message;
	// private AffineTransform ax;
    
    public WindowGroup (String title, int width, int height) {
        super (title);

//        MouseListener ml = new MouseAdapter () {
//                public void mousePressed (MouseEvent event) {
//                    unpause ();
//                }
//            };
//        addMouseListener (ml);

        addWindowListener (new WindowAdapter () {
            public void windowClosing (WindowEvent event) {
                System.exit (0);
            }
        });
        
        Container content = getContentPane ();
        content.setLayout (new BorderLayout ());
        
        canvas = new JComponent () {
		    private static final long serialVersionUID = 1L;
            public void paintComponent (Graphics g) {
                redraw();
                if (buffer != null)
                    g.drawImage(buffer, 0, 0, null);
            }
        };
        canvas.setBackground (Color.white);
        canvas.setPreferredSize (new Dimension (width, height));
        content.add (BorderLayout.CENTER, canvas);

        message = new JTextArea (10, 40);
        message.setEditable (false);
//        message.addMouseListener (ml);
        content.add (BorderLayout.SOUTH, new JScrollPane (message));
    
        pack ();
        setVisible(true);
        
        // Test
        // content.setFocusable(true);
        // boolean requestedFocus = content.requestFocusInWindow();
        //System.out.println("Requested Focus: " + requestedFocus);

        makeBuffer (width, height); // must be after show()
    }

    //
    // Drawing GraphicalObjects in the window.
    //
    BoundaryRectangle savedClipRect = null;
    protected List<GraphicalObject> children = 
            Collections.synchronizedList(new LinkedList<GraphicalObject> ());

    //
    // Drawing GraphicalObjects in the window.
    //
    
    public synchronized void redraw () {
//        Rectangle drawArea;
//        if (savedClipRect != null) {
//            // transform the coordinates of the damaged area
//            drawArea = savedClipRect;
//        } else {
//            drawArea = getBoundingBox();
//        }
//        Graphics2D g = (Graphics2D) buffer.getGraphics();
//        g.setColor(canvas.getBackground());
//        g.fill(drawArea);
//        ListIterator<GraphicalObject> iter = children.listIterator();
//        while (iter.hasNext()) {
//            GraphicalObject gobj = iter.next();
//            BoundaryRectangle r = gobj.getBoundingBox();
//            if (r.intersects(drawArea)) {
//                gobj.draw(g, drawArea);
//            }
//        }
//        savedClipRect = null;
        
    	if (savedClipRect != null) {
    	    Graphics2D g = (Graphics2D) buffer.getGraphics();
            g.setColor(canvas.getBackground());
            g.fill (savedClipRect);
            Rectangle area = canvas.getBounds();
            synchronized (children) {
        	    for (GraphicalObject gobj : children) {
        	    	BoundaryRectangle r = gobj.getBoundingBox();
        	    	// gobj.draw(g, r);
        	    	// if (r.intersects(savedClipRect))
        	    	// 	gobj.draw(g, savedClipRect);
        	    	if (r.intersects(area))
                        gobj.draw(g, area);
        	    }
            }
    	    savedClipRect = null;
    	} else {
    	    // println("no clip rectangle");
    	}
    }

    private void makeBuffer (int width, int height) {
        buffer = createImage (width, height);
        Graphics2D g = (Graphics2D) buffer.getGraphics ();
        g.setColor (canvas.getBackground ());
        g.fillRect (0, 0, width, height);
    }

    private void addClipRect (BoundaryRectangle r) {
        if (savedClipRect != null)
            savedClipRect.add(r);
        else
            savedClipRect = new BoundaryRectangle(r);
    }


    public Shape getBoundingShape () {
        return getBoundingBox();
    }
 
    //
    // Group interface
    //


    public void addChild (GraphicalObject child) {
        child.setGroup (this);
        children.add (child); 
        damage (child.getBoundingBox ());
    }
    public void removeChild (GraphicalObject child) {
        children.remove (child);
        damage (child.getBoundingBox ());
    }
    public void bringChildToFront (GraphicalObject child) {
        children.remove (child);
        children.add (child);
    }
    public void resizeToChildren () {
    }
    
    public synchronized void damage (BoundaryRectangle rectangle) {
    	addClipRect(rectangle);
        canvas.repaint ();
    }
    
    public void draw (Graphics2D graphics) {
    }
    
    public BoundaryRectangle getBoundingBox () {
    	Rectangle r = canvas.getBounds ();
    	BoundaryRectangle br = new BoundaryRectangle(r.x, r.y, r.width, r.height);
        return br;
    }
    
    public void moveTo (int x, int y) {
    }
    
    public Group getGroup () {
        return null;
    }
    
    public void setGroup (Group group) {
    }
    
    public List<GraphicalObject> getChildren () {
        return children;
    }
    
    public Point parentToChild (Point pt) {
        return pt;
    }
    
    public Point childToParent (Point pt) {
        return pt;
    }

    // 
    // Message output
    //

    public void print (Object msg) {
        message.append (msg.toString ());
        message.setCaretPosition (message.getDocument ().getLength ());
    }

    public void println (Object msg) {
        print (msg + "\n");
    }

    // 
    // Sleeping
    //

    public void sleep (int msec) {
        try {
            Thread.sleep (msec);
        } catch (InterruptedException e) {
        }
    }

    //
    // Waiting for mouse clicks
    //

    public void pause () {
        println ("click to continue...");
        synchronized (this) {
            try {
                wait ();
            } catch (InterruptedException e) {
            }
        }
    }

    public void unpause () {
        synchronized (this) {
            notify ();
        }
    }


    //
    // Random selections
    //

    private static Random r = new Random ();

    public int random (int n) {
        return r.nextInt (n);
    }

    public Object random (Object[] things) {
        return things[random (things.length)];
    }


    //
    // Loading images from disk
    // Guarantees that the image is loaded, so you don't have to pass
    // an ImageObserver when you call getWidth(), getHeight(), or
    // drawImage().
    // 

	//These were used by the old way to do this:
	//private MediaTracker tracker = new MediaTracker(new Label(""));
	//private int nextID = 0;

	public Image loadImageFully(String filename) throws IOException {
      /*  from  http://www.exampledepot.com/egs/javax.imageio/BasicImageRead.html */
        File file = new File(filename);
        Image image = ImageIO.read(file);
        return image;
        
     /* This is the old way to do it
		Image image = getToolkit().getImage(filename);
		int id = nextID++;
		tracker.addImage(image, id);
		try {
			try {
				tracker.waitForID(id);
			} finally {
				tracker.removeImage(image);
			}
		} catch (InterruptedException e) {
			throw new IOException();
		}
		return image;
     */
	}

    public void resizeChild(GraphicalObject child) {
        // TODO Auto-generated method stub       
    }

    public void draw(Graphics2D graphics, Shape clipRect) {
        // TODO Auto-generated method stub
    }

    public void setAffineTransform(AffineTransform af) {
        // TODO Auto-generated method stub
    }

    public AffineTransform getAffineTransform() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSelectionFeedback(boolean feedback) {
    }
}
