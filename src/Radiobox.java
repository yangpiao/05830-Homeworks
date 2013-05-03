import java.awt.*;


public class Radiobox extends Checkbox {
    private static final int markPadding = 2;
    private static final Color maskColor = new Color(150, 150, 150, 30);
    private static final Color defaultColor = Color.black;
    private static final int defaultThickness = 2;
    private static final int defaultSize = 16;
    private static final int defaultMargin = 10;
    private static final GraphicalObject defaultLabel = 
            new OutlineRect(0, 0, 0, 0, defaultColor, 0);
    
    public Radiobox() {
        this(defaultLabel);
    }
    public Radiobox(GraphicalObject label) {
        this(label, 0, 0, defaultMargin, defaultSize, 
                defaultColor, defaultThickness);
    }
    public Radiobox(GraphicalObject label, int x, int y) {
        this(label, x, y, defaultMargin, defaultSize, 
                defaultColor, defaultThickness);
    }
    public Radiobox(GraphicalObject label, int x, int y, int margin) {
        this(label, x, y, margin, defaultSize,
                defaultColor, defaultThickness);
    }
    public Radiobox(GraphicalObject label, int x, int y, 
            int margin, int boxSize) {
        this(label, x, y, margin, boxSize, defaultColor, defaultThickness);
    }
    public Radiobox(GraphicalObject label, int x, int y, 
            int margin, int boxSize, Color color, int lineThickness) {
        super(label, x, y, margin, boxSize, color, lineThickness);
        gObj.removeChild(box);
        gObj.removeChild(label);
        box = new OutlineEllipse(0, 0, boxSize, boxSize, color, lineThickness);
        gObj.addChild(box);
        gObj.addChild(label);
    }
    
    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        gObj.draw(graphics, clipShape);
        graphics.setClip(clipShape);
        if (selected || interimSelected) {
            Rectangle r = getBoundingBox();
            if (selected) {
                // int padding = (lineThickness > 3) ? lineThickness : markPadding;
                int padding = markPadding;
                // int thick = 3;
                // graphics.setStroke(new BasicStroke(thick));
                int mx = r.x + lineThickness + padding;
                int my = r.y + lineThickness + padding;
                int mw = boxSize - 2 * lineThickness - 2 * padding;
                // int mh = boxSize - 2 * lineThickness - 2 * padding;
                mw = (mw <= 0) ? 1 : mw;
                // mh = (mh <= 0) ? 1 : mh;
                graphics.setColor(color);
                graphics.fillOval(mx, my, mw, mw);
            }
            if (interimSelected) {
                graphics.setColor(maskColor);
                graphics.fillOval(r.x, r.y, boxSize, boxSize);
            }
        }
    }
}
