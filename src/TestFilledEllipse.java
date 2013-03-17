import java.awt.*;

public class TestFilledEllipse extends TestFrame {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new TestFilledEllipse(args);
    }

    public TestFilledEllipse(String[] args) {
        super("TestFilledEllipse", 200, 200);
        int lineThickness = 1;
        try {
            lineThickness = Integer.parseInt(args[0]);
            println("line thickness = " + lineThickness);
        } catch (Exception e) {
            println("usage: TestFilledEllipse [line thickness]\n"
            + "using line thickness = "
            + lineThickness + " by default");
        }
        println("creating FilledEllipse");
        FilledEllipse e = new FilledEllipse(10, 10, 60, 40, Color.orange);
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
        println("close the window to exit");
    }
}
