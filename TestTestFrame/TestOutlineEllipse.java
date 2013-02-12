import java.awt.*;

public class TestOutlineEllipse extends TestFrame {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new TestOutlineEllipse(args);
    }

    public TestOutlineEllipse(String[] args) {
        super("TestOutlineEllipse", 200, 200);
        int lineThickness = 1;
        try {
            lineThickness = Integer.parseInt(args[0]);
            println("line thickness = " + lineThickness);
        } catch (Exception e) {
            println("usage: TestOutlineEllipse [line thickness]\n"
            + "using line thickness = "
            + lineThickness + " by default");
        }
        println("creating OutlineEllipse");
        OutlineEllipse e = new OutlineEllipse(10, 10, 60, 40, Color.red, lineThickness);
        addChild(e);
        pause();
        println("moving ellipse with setX(), setY()");
        for (int x = 10; x < 150; x += 30) {
            e.setX(x);
            for (int y = 10; y < 150; y += 30) {
                e.setY(y);
                addClipRect(e.getBoundingBox());
                redraw(e);
                sleep(100);
            }
        }
        println("final bounding box is " + e.getBoundingBox());
        println("final x/y position is "
        + e.getX() + "," + e.getY());
        pause();
        println("changing to blue");
        e.setColor(Color.blue);
        addClipRect(e.getBoundingBox());
        redraw(e);
        pause();
        println("moving ellipse with moveTo ()");
        for (int x = 10; x < 150; x += 30) {
            for (int y = 10; y < 150; y += 30) {
                e.moveTo(x, y);
                addClipRect(e.getBoundingBox());
                redraw(e);
                sleep(100);
            }
        }
        println("final bounding box is " + e.getBoundingBox());
        println("final x/y position is "
        + e.getX() + "," + e.getY());
        pause();
        println("doubling line thickness to " + lineThickness * 2);
        e.setLineThickness(lineThickness * 2);
        addClipRect(e.getBoundingBox());
        redraw(e);
        pause();
        println("moving ellipse with moveTo ()");
        for (int x = 10; x < 150; x += 30) {
            for (int y = 10; y < 150; y += 30) {
                e.moveTo(x, y);
                addClipRect(e.getBoundingBox());
                redraw(e);
                sleep(100);
            }
        }
        println("final bounding box is " + e.getBoundingBox());
        println("final x/y position is "
        + e.getX() + "," + e.getY());
        println("close the window to exit");
    }
}
