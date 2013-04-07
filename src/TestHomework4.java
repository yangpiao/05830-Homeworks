import java.awt.*;
import java.io.IOException;
// import java.awt.geom.*;
import java.util.List;

public class TestHomework4 extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    public static void main (String[] args) {
        new TestHomework4 (args);
    }

    public TestHomework4 (String[] args) {
        super ("TestHomework4", 500, 500);
    	try {
            println ("1. creating blue and red rects");
            OutlineRect blueRect = new OutlineRect (0, 0, 50, 80, Color.blue, 5);
            OutlineRect redRect = new OutlineRect (100, 0, 50, 80, Color.red, 1);
            Group windowgroup = new SimpleGroup (0, 0, 500, 500);
            Group group = new SimpleGroup (0, 0, 500, 500);
            addChild (windowgroup);
    
            windowgroup.addChild (group);
            group.addChild (blueRect);
            group.addChild (redRect);
    
            println ("2. moving blue to 30,30, red shouldn't move");
            pause();
            blueRect.moveTo(30,30);
            
            println ("3. adding constraint to red rect to be at right of blue");
            println ("     red should move to be at 80,30");
            pause();
            
            new Constraint()
                .setTarget(redRect, "x")
                .addSource(blueRect, "x").addSource(blueRect, "width")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 2) return;
                        int value = (int) sources.get(0).getValue() + 
                                (int) sources.get(1).getValue();
                        target.setValue(value);
                    }
                })
                .addToSystem();
            
            new Constraint()
                .setTarget(redRect, "y")
                .addSource(blueRect, "y")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 1) return;
                        target.setValue(sources.get(0).getValue());
                    }
                })
                .addToSystem();
            
            println("4. Move Blue, red should move automatically");
            pause();
            
            blueRect.moveTo(0,0);
            pause();
            
            
            println("5. Add a black rect");
            FilledRect blackRect = new FilledRect(50, 100, 50, 50, Color.black);
            group.addChild(blackRect);
            
            println("6. Add constraint to black rect's color to be the color");
            println("   of red rect");
            pause();
            
            new Constraint()
                .setTarget(blackRect, "color")
                .addSource(redRect, "color")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 1) return;
                        target.setValue(sources.get(0).getValue());
                    }
                })
                .addToSystem();
            
            println("7. Change the color of red rect to orange");
            pause();
            redRect.setColor(Color.orange);
            
            println("8. Add a Text object");
            pause();
            Text text = new Text("Text", 10, 100, 
                    new Font("SansSerif", Font.PLAIN, 18), Color.black);
            group.addChild(text);
            
            println("9. Add constraint to text so that it shows the number of");
            println("   line thickness of blue rect");
            pause();
            
            new Constraint()
                .setTarget(text, "text")
                .addSource(blueRect, "lineThickness")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 1) return;
                        target.setValue("line thickness: " + 
                                sources.get(0).getValue());
                    }
                })
                .addToSystem();
            
            println("10. Change line thickness of blue rect");
            pause();
            blueRect.setLineThickness(3);
            pause();
            blueRect.setLineThickness(8);
            pause();
            blueRect.setLineThickness(1);
            
            println("11. Add 2 rects");
            pause();
            FilledRect r1 = new FilledRect(50, 200, 10, 10, Color.darkGray);
            FilledRect r2 = new FilledRect(250, 230, 10, 10, Color.darkGray);
            group.addChild(r1);
            group.addChild(r2);
            
            println("12. Add another rect with the constraint to be");
            println("    in the middle of the 2 rects");
            pause();
            FilledRect r3 = new FilledRect(250, 230, 10, 10, Color.lightGray);
            group.addChild(r3);
            
            new Constraint()
                .setTarget(r3, "x")
                .addSource(r1, "x")
                .addSource(r2, "x")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 2) return;
                        int x = ((int)sources.get(0).getValue() + 
                                (int)sources.get(1).getValue()) / 2;
                        target.setValue(x);
                    }
                })
                .addToSystem();
            
            new Constraint()
                .setTarget(r3, "y")
                .addSource(r1, "y")
                .addSource(r2, "y")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 2) return;
                        int y = ((int)sources.get(0).getValue() + 
                                (int)sources.get(1).getValue()) / 2;
                        target.setValue(y);
                    }
                })
                .addToSystem();
            
            println("13. Move left one to 30, 250");
            pause();
            r1.moveTo(30, 250);
            println("14. Move right one to 270, 190");
            pause();
            r2.moveTo(270, 190);
            println("15. Move right one to 200, 90");
            pause();
            r2.moveTo(200, 90);
            
            
            println("16. Create constraint that an Icon object changes with");
            println("    a Text object");
            pause();
            Text t = new Text("duke.gif", 300, 20, 
                    new Font("SansSerif", Font.PLAIN, 18), Color.black);
            group.addChild(t);
            try {
                Icon icon = new Icon(loadImageFully("duke.gif"), 300, 30);
                group.addChild(icon);
                
                new Constraint()
                    .setTarget(icon, "image")
                    .addSource(t, "text")
                    .setFormula(new Constraint.Formula() {
                        public void evaluate(ConstraintVariable target, 
                                List<ConstraintVariable> sources) {
                            if (sources.size() < 1) return;
                            String s = (String) sources.get(0).getValue();
                            try {
                                target.setValue(loadImageFully(s));
                            } catch(IOException e) {
                                println("Image failed to load");
                            }
                        }
                    })
                    .addToSystem();
                
            } catch (IOException e) {
                println("Image failed to load");
            }
            
            println("17. Change text to \"dog.gif\"");
            pause();
            t.setText("dog.gif");
            println("18. Change text to \"duke.gif\"");
            pause();
            t.setText("duke.gif");
            
            
            println("19. Add a Text object showing the number of children in");
            println("    the group");
            pause();
            Text numOfChildren = new Text("Number of children: ", 10, 300, 
                    new Font("SansSerif", Font.PLAIN, 18), Color.black);
            group.addChild(numOfChildren);
            
            new Constraint()
                .setTarget(numOfChildren, "text")
                .addSource(group, "children")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 1) return;
                        List<?> ch = (List<?>) sources.get(0).getValue();
                        target.setValue("Number of children: " + ch.size());
                    }
                })
                .addToSystem();

            println("20. Add a child to the group");
            pause();
            FilledRect temp = new FilledRect(350, 350, 80, 60, Color.red);
            group.addChild(temp);
            println("21. Remove a child");
            pause();
            group.removeChild(temp);
            
            println("22. Add a rect whose width changes with its height");
            pause();
            group.addChild(temp);
            
            println("23. Add constraint that changes the rect's width ");
            println("    with its height");
            pause();
            
            new Constraint()
                .setTarget(temp, "width")
                .addSource(temp, "height")
                .setFormula(new Constraint.Formula() {
                    public void evaluate(ConstraintVariable target, 
                            List<ConstraintVariable> sources) {
                        if (sources.size() < 1) return;
                        target.setValue(sources.get(0).getValue());
                    }
                })
                .addToSystem();
            
            println("24. Change the rect's height");
            pause();
            temp.setHeight(130);
            pause();
            temp.setHeight(30);
            
            println("DONE. close the window to stop");
    
    	} catch(Exception e) {
    	    println ("got an exception " + e);
    	}
    }
}
