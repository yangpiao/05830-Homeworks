import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.*;

public class TestFrame extends JFrame implements Group {
	private static final long serialVersionUID = 1L;
	private Image buffer;
	private JComponent canvas;
	private JTextArea message;
	private AffineTransform ax;

	public TestFrame(String title, int width, int height) {
		super(title);
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				unpause();
			}
		};
		addMouseListener(ml);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});

		Container content = getContentPane();
		content.setLayout(new BorderLayout());

		canvas = new JComponent() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				if (buffer != null)
					g.drawImage(buffer, 0, 0, null);
			}
		};
		canvas.setBackground(Color.white);
		canvas.setPreferredSize(new Dimension(width, height));
		content.add(BorderLayout.CENTER, canvas);

		message = new JTextArea(10, 40);
		message.setEditable(false);
		message.addMouseListener(ml);
		content.add(BorderLayout.SOUTH, new JScrollPane(message));

		pack();
		setVisible(true); // formerly show()

		makeBuffer(width, height); // must be after setVisible
	}

	BoundaryRectangle savedClipRect = null;

	//
	// Drawing GraphicalObjects in the window.
	//

	public void redraw(GraphicalObject gobj) {
		if (savedClipRect != null) {
			Graphics2D g = (Graphics2D) buffer.getGraphics();
			g.setColor(canvas.getBackground());
			// g.setColor(Color.lightGray); // test
			g.fill(savedClipRect);
			gobj.draw(g, savedClipRect);
			canvas.repaint();
			savedClipRect = null;
		} else
			println("no clip rectangle");
	}

	private void makeBuffer(int width, int height) {
		buffer = createImage(width, height);
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setColor(canvas.getBackground());
		g.fillRect(0, 0, width, height);
	}

	public void addClipRect(BoundaryRectangle r) {
		if (savedClipRect != null)
			savedClipRect.add(r);
		else
			savedClipRect = new BoundaryRectangle(r);
	}

	//
	// Group interface
	//

	// This class is NOT a correct implementation of Group.
	// It's just a test harness that displays GraphicalObjects
	// in a window.
	java.util.ArrayList<GraphicalObject> children = new java.util.ArrayList<GraphicalObject>();

	public void addChild(GraphicalObject child) {
		child.setGroup(this);
		addClipRect(child.getBoundingBox());
		redraw(child);
		// put child in a hash table just to keep it from
		// being garbage-collected
		children.add(child);

	}

	public void removeChild(GraphicalObject child) {
	}

	public void bringChildToFront(GraphicalObject child) {
	}

	public void resizeChild(GraphicalObject child) {
	}

	public void resizeToChildren() {
	}

	public void damage(BoundaryRectangle rectangle) {
		addClipRect(rectangle);
	}

	public void draw(Graphics2D graphics, Shape clipRect) {
	}

	public BoundaryRectangle getBoundingBox() {
		return (BoundaryRectangle)canvas.getBounds();
	}

	public void moveTo(int x, int y) {
	}

	public Group getGroup() {
		return null;
	}

	public void setGroup(Group group) {
	}

	public java.util.List<GraphicalObject> getChildren() {
		return new java.util.ArrayList<GraphicalObject>(children);
	}

	public Point parentToChild(Point pt) {
		return pt;
	}

	public Point childToParent(Point pt) {
		return pt;
	}

	// 
	// Message output
	//

	public void print(Object msg) {
		message.append(msg.toString());
		message.setCaretPosition(message.getDocument().getLength());
	}

	public void println(Object msg) {
		print(msg + "\n");
	}

	// 
	// Sleeping
	//

	public void sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
		}
	}

	//
	// Waiting for mouse clicks
	//

	public void pause() {
		println("click to continue...");
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public void unpause() {
		synchronized (this) {
			notify();
		}
	}

	//
	// Random selections
	//

	private static java.util.Random r = new java.util.Random();

	public int random(int n) {
		return r.nextInt(n);
	}

	public Object random(Object[] things) {
		return things[random(things.length)];
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

	public AffineTransform getAffineTransform() {
		return ax;
	}

	public void setAffineTransform(AffineTransform af) {
		ax = af;
	}

    public void setSelectionFeedback(boolean feedback) {
    }
}
