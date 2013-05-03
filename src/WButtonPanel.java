import java.awt.*;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.ArrayList;
import java.util.List;


public class WButtonPanel extends WButtonPanelBase {
    public WButtonPanel() {
        this(0, 0, 0, 0, LayoutGroup.VERTICAL, 10, 5, 5, 
                false, ChoiceBehavior.SINGLE);
    }
    public WButtonPanel(int x, int y, int width, int height) {
        this(x, y, width, height, LayoutGroup.VERTICAL, 10, 5, 5,
                false, ChoiceBehavior.SINGLE);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset) {
        this(x, y, width, height, layout, offset, 5, 5,
                false, ChoiceBehavior.SINGLE);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset, boolean finalFeedback) {
        this(x, y, width, height, layout, offset, 5, 5, 
                finalFeedback, ChoiceBehavior.SINGLE);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset, boolean finalFeedback, int selectionType) {
        this(x, y, width, height, layout, offset, 5, 5, 
                finalFeedback, selectionType);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            boolean finalFeedback, int selectionType) {
        this(x, y, width, height, LayoutGroup.VERTICAL, 10, 5, 5, 
                finalFeedback, selectionType);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols) {
        this(x, y, width, height, layout, offset, rows, cols, 
                false, ChoiceBehavior.SINGLE);
    }
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols, boolean finalFeedback) {
        this(x, y, width, height, layout, offset, rows, cols, 
                finalFeedback, ChoiceBehavior.SINGLE);
    }
    
    public WButtonPanel(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols,
            boolean finalFeedback, int selectionType) {
        super(x, y, width, height, layout, offset, rows, cols, 
                finalFeedback, selectionType, false);
    }
    
    @Override
    protected void select(GraphicalObject o) {
        if (o != null) {
            if (!finalFeedback) {
                ((Selectable) o).setSelected(false);
            }
            Group g = (Group) o;
            List<GraphicalObject> children = g.getChildren();
            if (children.isEmpty()) return;
            value = children.get(0);
        } else {
            value = null;
        }
        executeCallbacks(value);
    }
    
    @Override
    public void add(String label) {
        add(label, label);
    }
    
    @Override
    public void add(GraphicalObject label) {
        add(label, "");
    }
    
    @Override
    public void add(String label, String userValue) {
        Text text = new Text(label, 5, 0, defaultFont, Color.black);
        Rectangle b = text.getBoundingBox();
        text.setY(b.height + 3);
//        FilledRect rect = new FilledRect(0, 0, 
//                b.width + 20, b.height + 20, defaultColor);
        FilledRect rect = 
                new FilledRect(0, 0, b.width + 10, b.height + 10, Color.white);
        SimpleGroup g = new SimpleGroup(0, 0, b.width + 10, b.height + 10);
        g.addChild(rect);
        g.addChild(text);
        container.addChild(g);
        userValues.put(text.hashCode(), userValue);
    }
    
    @Override
    public void add(GraphicalObject label, String userValue) {
        // container.addChild(label);
        Rectangle b = label.getBoundingBox();
        if (label instanceof Text) {
            add(((Text) label).getText(), userValue);
            return;
        }
        // FilledRect rect = new FilledRect(0, 0, b.width, b.height, defaultColor);
        FilledRect rect = new FilledRect(0, 0, b.width, b.height, Color.white);
        SimpleGroup g = new SimpleGroup(0, 0, b.width, b.height);
        g.addChild(rect);
        g.addChild(label);
        container.addChild(g);
        userValues.put(label.hashCode(), userValue);
    }
}
