import java.awt.*;
import java.io.*;
import java.util.List;


public class TestApplication extends Application {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        new TestApplication(args);
    }
    
    public TestApplication(String[] args) throws Exception {
        super("TestApplication", 800, 600);
        try {
            addChild(new OutlineRect(5, 5, 390, 290, Color.black, 1));
            addChild(new OutlineRect(405, 5, 390, 290, Color.black, 1));
            addChild(new OutlineRect(5, 305, 390, 290, Color.black, 1));
            addChild(new OutlineRect(405, 305, 390, 290, Color.black, 1));
            
            t1 = new Text("", 10, 20, font, Color.black);
            t2 = new Text("", 410, 20, font, Color.black);
            t3 = new Text("", 10, 320, font, Color.black);
            t4 = new Text("", 410, 320, font, Color.black);
            addChild(t1);
            addChild(t2);
            addChild(t3);
            addChild(t4);
            
            next = new WButtonPanel(690, 560, 100, 50);
            next.add("NEXT TEST", "next");
            nextCallback = new WButtonPanel.Callback() {
                public void onSelect(GraphicalObject o, String userValue) {
                    if (userValue.equals("next")) {
                        unpause();
                    }
                }
            };
            next.addCallback(nextCallback);
            addChild(next);
            
            testButtonPanel();
            testCheckboxPanel();
            testRadioboxPanel();
            testNumberSlider();
            testSelectionHandles();
            println("=============== End of Tests ===============");
        } catch (Exception e) {
            println("Exception: " + e);
        }
    }
    
    private static final Font font = new Font("SansSerif", Font.PLAIN, 16);
    private Text t1;
    private Text t2;
    private Text t3;
    private Text t4;
    private WButtonPanel next;
    private WButtonPanel.Callback nextCallback;
    
    private void testButtonPanel() throws IOException {
        println("=============== Button Panels ===============");
        t1.setText("1. Vertical, no final feedback, single selection");
        t2.setText("2. Horizontal, final feedback, toggle selection");
        t3.setText("3. Grid, final feedback, multiple selection");
        
        WButtonPanel bp1 = new WButtonPanel(50, 50, 100, 300);
        bp1.add("Test 1", "Test button 1");
        bp1.add("Test 2"); // default user value is the same as the label
        bp1.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        bp1.add(new FilledRect(0, 0, 80, 30, Color.blue), 
                "FilledRect button");
        bp1.addCallback(new WButtonPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 1 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WButtonPanel bp2 = new WButtonPanel(410, 50, 300, 80, 
                LayoutGroup.HORIZONTAL, 10, true, ChoiceBehavior.TOGGLE);
        bp2.add("Test 1", "Test button 1");
        bp2.add("Test 2"); // default user value is the same as the label
        bp2.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        bp2.add(new FilledRect(0, 0, 80, 30, Color.blue), 
                "FilledRect button");
        bp2.addCallback(new WButtonPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 2 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WButtonPanel bp3 = new WButtonPanel(10, 350, 380, 300, 
                LayoutGroup.GRID, 10, 6, 3, true, ChoiceBehavior.MULTIPLE);
        bp3.add("Test 1", "Test button 1");
        bp3.add("Test 2"); // default user value is the same as the label
        bp3.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        bp3.add(new FilledRect(0, 0, 120, 30, Color.orange), 
                "FilledRect button");
        bp3.add(new OutlineRect(0, 0, 120, 50, Color.orange, 5), 
                "FilledRect button");
        bp3.add(new FilledEllipse(0, 0, 120, 80, Color.orange), 
                "FilledRect button");
        bp3.addCallback(new WButtonPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 3 - value: " + o + " | user value: " + userValue);
            }
        });
        
        addChild(bp1);
        addChild(bp2);
        addChild(bp3);
        
        pause();
        removeChild(bp1);
        removeChild(bp2);
        removeChild(bp3);
        t1.setText("");
        t2.setText("");
        t3.setText("");
    }
    
    private void testCheckboxPanel() throws IOException {
        println("=============== Checkbox Panels ===============");
        t1.setText("1. Vertical");
        t2.setText("2. Horizontal");
        t3.setText("3. Grid");
        
        WCheckboxPanel p1 = new WCheckboxPanel(50, 50, 100, 300);
        p1.add("Test 1", "Test button 1");
        p1.add("Test 2"); // default user value is the same as the label
        p1.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        p1.add(new FilledRect(0, 0, 80, 30, Color.blue), 
                "FilledRect button");
        p1.addCallback(new WCheckboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 1 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WCheckboxPanel p2 = new WCheckboxPanel(410, 50, 300, 80, 
                LayoutGroup.HORIZONTAL, 20);
        p2.add("Test 1", "Test button 1");
        p2.add("Test 2"); // default user value is the same as the label
        p2.add(new Icon(loadImageFully("button.png"), 0, 0), "Image button");
        p2.addCallback(new WCheckboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 2 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WCheckboxPanel p3 = new WCheckboxPanel(10, 350, 380, 300, 
                LayoutGroup.GRID, 10, 6, 3);
        p3.add("Test 1", "Test button 1");
        p3.add("Test 2"); // default user value is the same as the label
        p3.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        p3.add(new FilledRect(0, 0, 80, 30, Color.orange), 
                "FilledRect button");
        p3.add(new OutlineRect(0, 0, 80, 50, Color.orange, 5), 
                "FilledRect button");
        p3.add(new FilledEllipse(0, 0, 80, 80, Color.orange), 
                "FilledRect button");
        p3.addCallback(new WCheckboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 3 - value: " + o + " | user value: " + userValue);
            }
        });
        
        addChild(p1);
        addChild(p2);
        addChild(p3);
        
        pause();
        removeChild(p1);
        removeChild(p2);
        removeChild(p3);
        t1.setText("");
        t2.setText("");
        t3.setText("");
    }
    
    private void testRadioboxPanel() throws IOException {
        println("=============== Radiobox Panels ===============");
        t1.setText("1. Vertical");
        t2.setText("2. Horizontal");
        t3.setText("3. Grid");
        
        WRadioboxPanel p1 = new WRadioboxPanel(50, 50, 100, 300);
        p1.add("Test 1", "Test button 1");
        p1.add("Test 2"); // default user value is the same as the label
        p1.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        p1.add(new FilledRect(0, 0, 80, 30, Color.blue), 
                "FilledRect button");
        p1.addCallback(new WRadioboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 1 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WRadioboxPanel p2 = new WRadioboxPanel(410, 50, 300, 80, 
                LayoutGroup.HORIZONTAL, 20);
        p2.add("Test 1", "Test button 1");
        p2.add("Test 2"); // default user value is the same as the label
        p2.add(new Icon(loadImageFully("button.png"), 0, 0), "Image button");
        p2.addCallback(new WRadioboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 2 - value: " + o + " | user value: " + userValue);
            }
        });
        
        WRadioboxPanel p3 = new WRadioboxPanel(10, 350, 380, 300, 
                LayoutGroup.GRID, 10, 6, 3);
        p3.add("Test 1", "Test button 1");
        p3.add("Test 2"); // default user value is the same as the label
        p3.add(new Icon(loadImageFully("button.png"), 0, 0), 
                "Image button");
        p3.add(new FilledRect(0, 0, 80, 30, Color.orange), 
                "FilledRect button");
        p3.add(new OutlineRect(0, 0, 80, 50, Color.orange, 5), 
                "FilledRect button");
        p3.add(new FilledEllipse(0, 0, 80, 80, Color.orange), 
                "FilledRect button");
        p3.addCallback(new WRadioboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                println("Panel 3 - value: " + o + " | user value: " + userValue);
            }
        });
        
        addChild(p1);
        addChild(p2);
        addChild(p3);
        
        pause();
        removeChild(p1);
        removeChild(p2);
        removeChild(p3);
        t1.setText("");
        t2.setText("");
        t3.setText("");
    }
    
    private void testNumberSlider() throws IOException {
        println("=============== Number Sliders ===============");
        t1.setText("Slider 1 (min: 0, max: 100, step: 10)");
        t2.setText("Slider 2 (min: 30, max: 10, step: -2.5)");
        t4.setText("Constraints");
        
        WNumberSlider slider1 = new WNumberSlider(50, 50, 300);
        slider1.addCallback(new WNumberSlider.Callback() {
            public void onChange(double value) {
                println("Slider 1 value: " + value);
            }
        });
        WNumberSlider slider2 = new WNumberSlider(410, 50, 200, 30, 10, -2.5);
        slider2.addCallback(new WNumberSlider.Callback() {
            public void onChange(double value) {
                println("Slider 2 value: " + value);
            }
        });
        addChild(slider1);
        addChild(slider2);
        
        Text text1 = new Text("", 450, 350, font, Color.darkGray);
        Text text2 = new Text("", 450, 380, font, Color.darkGray);
        addChild(text1);
        addChild(text2);
        new Constraint()
            .setTarget(text1, "text")
            .addSource(slider1, "value")
            .setFormula(new Constraint.Formula() {
                public void evaluate(ConstraintVariable target,
                        List<ConstraintVariable> sources) {
                    if (sources.size() < 1) return;
                    target.setValue("Slider value:" + 
                            sources.get(0).getValue());
                }
            })
            .addToSystem();
        new Constraint()
            .setTarget(text2, "text")
            .addSource(slider2, "value")
            .setFormula(new Constraint.Formula() {
                public void evaluate(ConstraintVariable target,
                        List<ConstraintVariable> sources) {
                    if (sources.size() < 1) return;
                    target.setValue("Slider value:" + 
                            sources.get(0).getValue());
                }
            })
            .addToSystem();
        
        pause();
        removeChild(slider1);
        removeChild(slider2);
        removeChild(text1);
        removeChild(text2);
        t1.setText("");
        t2.setText("");
    }
    
    private void testSelectionHandles() throws IOException {
        println("=============== Selection Handles ===============");
        t1.setText("Selection Handles");
        
        Group g = new SimpleGroup(50, 50, 340, 240);
        g.addChild(new FilledRect(0, 0, 60, 60, Color.lightGray));
        g.addChild(new FilledRect(80, 0, 60, 60, Color.orange));
        g.addChild(new FilledRect(160, 0, 60, 60, Color.lightGray));
        g.addChild(new FilledRect(0, 80, 60, 60, Color.orange));
        g.addChild(new FilledRect(80, 80, 60, 60, Color.lightGray));
        g.addChild(new FilledRect(160, 80, 60, 60, Color.orange));
        g.addChild(new FilledRect(0, 160, 60, 60, Color.lightGray));
        g.addChild(new FilledRect(80, 160, 60, 60, Color.orange));
        g.addChild(new FilledRect(160, 160, 60, 60, Color.lightGray));
        
        addChild(g);
        WSelectionHandles sh = new WSelectionHandles(g);
        sh.addCallback(new WSelectionHandles.Callback() {
            public void onChange(GraphicalObject value) {
                println("Selection handles value: " + value);
            }
        });
        addChild(sh);
        
        Text text1 = new Text("", 450, 350, font, Color.darkGray);
        addChild(text1);
        new Constraint()
            .setTarget(text1, "text")
            .addSource(sh, "value")
            .setFormula(new Constraint.Formula() {
                public void evaluate(ConstraintVariable target,
                        List<ConstraintVariable> sources) {
                    if (sources.size() < 1) return;
                    target.setValue("Value:" + sources.get(0).getValue());
                }
            })
            .addToSystem();
        
        pause();
        removeChild(sh);
        removeChild(g);
        removeChild(text1);
        t1.setText("");
    }
}
