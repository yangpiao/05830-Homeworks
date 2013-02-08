import java.awt.*;

public class TestSimpleGroup extends TestFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TestSimpleGroup(args);
	}

	public TestSimpleGroup(String[] args) {
		super("TestSimpleGroup", 200, 200);

		int nObjects = 4;
		try {
			nObjects = Integer.parseInt(args[0]);
			println("nObjects = " + nObjects);
		} catch (Exception e) {
			println("usage:  TestSimpleGroup [# of graphical objects]\n"
					+ "using " + nObjects + " objects by default");
		}

		println("creating black frame");
		addChild(new OutlineRect(9, 9, 182, 182, Color.black, 1));

		println("creating SimpleGroup inside black frame");
		Group group = new SimpleGroup(10, 10, 180, 180);
		addChild(group);

		println("creating Rects at random places");
		GraphicalObject[] objects = new GraphicalObject[nObjects];
		Color[] colors = { Color.black, Color.red, Color.blue };
		for (int i = 0; i < nObjects; ++i) {
			objects[i] = new OutlineRect(-20 + random(200), -20 + random(200),
					random(100), random(100), (Color) random(colors), 1);
			try {
				group.addChild(objects[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		redraw(group);
		pause();

		println("moving rectangles 1000 times");
		println("close the window to stop");
		for (int i = 0; i < 1000; ++i) {
			GraphicalObject gobj = (GraphicalObject) random(objects);
			gobj.moveTo(-20 + random(200), -20 + random(200));
			group.bringChildToFront(gobj);
			redraw(group);
			sleep(500);
		}
	}

}
