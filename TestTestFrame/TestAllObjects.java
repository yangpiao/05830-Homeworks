import java.awt.*;
import java.io.IOException;

public class TestAllObjects extends TestFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TestAllObjects();
	}

	public TestAllObjects() { 
		super("TestAllObjects", 300, 400);

		println("OutlineRect");
		addChild(new OutlineRect(10, 10, 50, 50, Color.black, 1));
		addChild(new OutlineRect(70, 10, 80, 50, Color.red, 2));

		println("FilledRect");
		addChild(new FilledRect(10, 70, 50, 50, Color.black));
		addChild(new FilledRect(70, 70, 80, 50, Color.red));

		println("Line");
		addChild(new Line(10, 130, 10, 180, Color.black, 1));
		addChild(new Line(20, 130, 60, 130, Color.red, 3));
		addChild(new Line(70, 130, 120, 180, Color.blue, 10));
		addChild(new Line(70, 130, 120, 180, Color.blue, 10));

		println("Icon");
		try {
			addChild(new Icon(loadImageFully("duke.gif"), 10, 200));
		} catch (IOException e) {
			println("duke.gif failed to load");
		}
		try {
			addChild(new Icon(loadImageFully("dog.gif"), 80, 200));
		} catch (IOException e) {
			println("dog.gif failed to load");
		}

		println("Text");
		addChild(new Text("going", 10, 350, new Font("Monospaced", Font.PLAIN,
				10), Color.black));
		addChild(new Text("going", 70, 350,
				new Font("SansSerif", Font.BOLD, 14), Color.red));
		addChild(new Text("gone", 140, 350, new Font("Serif", Font.PLAIN, 24),
				Color.black));
		addChild(new Line(10, 350, 250, 350, Color.black, 1));

		println("close the window to exit");
	}

}
