import java.awt.*;


public class WRadioboxPanel extends WButtonPanelBase {
    public WRadioboxPanel() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }
    public WRadioboxPanel(int x, int y, int width, int height) {
        this(x, y, width, height, LayoutGroup.VERTICAL, 10, 5, 5);
    }
    public WRadioboxPanel(int x, int y, int width, int height, int layout) {
        this(x, y, width, height, layout, 10, 5, 5);
    }
    public WRadioboxPanel(int x, int y, int width, int height, 
            int layout, int offset) {
        this(x, y, width, height, layout, offset, 5, 5);
    }
    public WRadioboxPanel(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols) {
        super(x, y, width, height, layout, offset, rows, cols,
                true, ChoiceBehavior.SINGLE, true);
    }
    
    @Override
    protected void select(GraphicalObject o) {
        if (o != null) {
            value = ((Radiobox) o).getLabel();
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
        add(new Text(label, 0, 0, defaultFont, Color.black), userValue);
    }

    @Override
    public void add(GraphicalObject label, String userValue) {
        Radiobox box = new Radiobox(label);
        container.addChild(box);
        userValues.put(label.hashCode(), userValue);
    }
}
