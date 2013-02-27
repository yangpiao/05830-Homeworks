import java.awt.*;

public class TestOtherLayouts extends TestFrame {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        new TestOtherLayouts(args);
    }

    public TestOtherLayouts(String[] args) throws Exception {
        super("TestOtherLayouts", 500, 400);

        int nObjects = 4;
        try {
            nObjects = Integer.parseInt(args[0]);
            println("nObjects = " + nObjects);
        } catch (Exception e) {
            println("usage:  TestOtherLayouts [# of graphical objects]\n"
                    + "using " + nObjects + " objects by default");
        }

        println("creating black frames");
        addChild(new OutlineRect(9, 9, 482, 182, Color.black, 1));
        addChild(new OutlineRect(9, 209, 482, 182, Color.black, 1));

        println("creating LayoutGroup with FILL layout");
        Group group = new LayoutGroup(10, 10, 480, 180, LayoutGroup.FILL, 20);
        addChild(group);

        println("creating random OutlineRects in the group");
        GraphicalObject[] objects = new GraphicalObject[nObjects];
        Color[] colors = { Color.black, Color.red, Color.blue };
        for (int i = 0; i < nObjects; ++i) {
            objects[i] = new OutlineRect(random(200), random(200), 
                    100 + random(50), 20 + random(50), 
                    (Color)random(colors), 1 + random(5));
            group.addChild(objects[i]);
        }
        redraw(group);
        pause();

        println("shuffling rectangles 5 times");
        GraphicalObject front = objects[objects.length - 1];
        for (int i = 0; i < 5; ++i) {
            GraphicalObject gobj;
            while ((gobj = (GraphicalObject) random(objects)) == front)
                ;
            group.bringChildToFront(gobj);
            front = gobj;
            redraw(group);
            sleep(800);
        }
        pause();

        println("doubling rectangle widths");
        for (int i = 0; i < objects.length; ++i) {
            OutlineRect r = (OutlineRect) objects[i];
            r.setWidth(r.getWidth() * 2);
            redraw(group);
            sleep(800);
        }
        pause();
        
        println("creating LayoutGroup with GRID layout (5 rows, 2 columns)");
        LayoutGroup grid = new LayoutGroup(10, 210, 480, 180, 
                LayoutGroup.GRID, 10, 2, 5);
        addChild(grid);
        println("creating random OutlineRects in the group");
        OutlineRect[] rects = new OutlineRect[10];
        for (int i = 0; i < 10; ++i) {
            rects[i] = new OutlineRect(random(200), random(200), 
                    60 + random(20), 20 + random(30),
                    (Color)random(colors), 1 + random(5));
            grid.addChild(rects[i]);
        }
        redraw(grid);
        pause();
        
        println("reset to 3 rows and 3 cols (1 item will be hidden)");
        grid.setRows(3);
        grid.setCols(3);
        redraw(grid);
        
        println("close the window to exit");
    }
}
