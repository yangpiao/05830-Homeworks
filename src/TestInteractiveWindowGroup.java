import java.awt.*;
import java.io.File;

import javax.imageio.ImageIO;

public class TestInteractiveWindowGroup extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new TestInteractiveWindowGroup(args);
    }

    public TestInteractiveWindowGroup(String[] args) {
        super("TestInteractiveWindowGroup", 700, 500);
        
        SimpleGroup g = new SimpleGroup(10, 10, 690, 490);
        addChild(g);
        OutlineRect rb1 = new OutlineRect(9, 9, 682, 482, Color.black, 1);
        addChild(rb1);

        SimpleGroup g1 = new SimpleGroup(40, 40, 250, 250);
        g.addChild(g1);
        FilledRect r1 = new FilledRect(10, 10, 100, 100, Color.orange);
        FilledRect r2 = new FilledRect(30, 30, 100, 100, Color.blue);
        g1.addChild(r1);
        SimpleGroup gsub1 = new SimpleGroup(80, 80, 150, 150);
        gsub1.addChild(r2);
        gsub1.addChild(new FilledRect(50, 50, 100, 100, Color.red));
        g1.addChild(gsub1);
        // SelectionHandles h = new SelectionHandles(Color.red);
        // h.addChild(r2);
        // g1.addChild(h);
        // g1.addChild(r2);
        OutlineRect rb2 = new OutlineRect(49, 49, 252, 252, Color.lightGray, 1);
        addChild(rb2);
        
        LayoutGroup g2 = new LayoutGroup(300, 40, 250, 250, 
                LayoutGroup.GRID, 10, 3, 3);
        g.addChild(g2);
        FilledRect r3 = new FilledRect(10, 10, 70, 70, Color.orange);
        OutlineRect r4 = new OutlineRect(30, 30, 70, 70, Color.blue, 1);
        g2.addChild(r3);
        g2.addChild(r4);
        g2.addChild(new Checkbox(new Text("Checkbox", 10, 350, 
                new Font("SansSerif", Font.PLAIN, 16), Color.black), 0, 0, 0, 12));
        g2.addChild(new Line(0, 0, 50, 50, Color.black, 3));
        try {
            File file = new File("duke.gif");
            Image image = ImageIO.read(file);
            g2.addChild(new Icon(image, 10, 200));
        } catch (Exception e) {}
        g2.addChild(new Text("going", 10, 350, 
                new Font("Monospaced", Font.PLAIN, 20), Color.black));
        g2.addChild(new FilledEllipse(0, 0, 60, 40, Color.blue));
        g2.addChild(new OutlineEllipse(0, 0, 60, 40, Color.blue, 2));
        OutlineRect rb3 = new OutlineRect(309, 49, 252, 252, Color.lightGray, 1);
        addChild(rb3);
        
        SimpleGroup g3 = new SimpleGroup(40, 310, 510, 150);
        addChild(new OutlineRect(49, 319, 512, 152, Color.lightGray, 1));
        g.addChild(g3);
        
        
        BehaviorEvent start = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        MoveBehavior mb = new MoveBehavior();
        mb.setStartEvent(start);
        mb.setStopEvent(stop);
        mb.setGroup(g1);
        addBehavior(mb);
        
        BehaviorEvent start1 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.CONTROL_MODIFIER, 
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop1 = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.CONTROL_MODIFIER, 
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior mb1 = new ChoiceBehavior(ChoiceBehavior.MULTIPLE, true);
        mb1.setStartEvent(start1);
        mb1.setStopEvent(stop1);
        mb1.setGroup(g1);
        addBehavior(mb1);
        
        BehaviorEvent start2 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop2 = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior mb2 = new ChoiceBehavior(ChoiceBehavior.TOGGLE, false);
        mb2.setStartEvent(start2);
        mb2.setStopEvent(stop2);
        mb2.setGroup(g2);
        addBehavior(mb2);
        
        BehaviorEvent start3 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID,
                BehaviorEvent.SHIFT_MODIFIER,
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop3 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, 
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        NewRectBehavior mb3 = new NewRectBehavior(Color.red, 3);
        mb3.setStartEvent(start3);
        mb3.setStopEvent(stop3);
        mb3.setGroup(g3);
        addBehavior(mb3);
        BehaviorEvent start4 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID,
                BehaviorEvent.CONTROL_MODIFIER,
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop4 = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, 
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        NewLineBehavior mb4 = new NewLineBehavior(Color.red, 3);
        mb4.setStartEvent(start4);
        mb4.setStopEvent(stop4);
        mb4.setGroup(g3);
        addBehavior(mb4);
    }
}
