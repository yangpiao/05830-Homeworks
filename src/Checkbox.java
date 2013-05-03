import java.awt.*;
import java.awt.geom.AffineTransform;

public class Checkbox implements GraphicalObject, Selectable {
    private static final int markPadding = 2;
    private static final Color maskColor = new Color(150, 150, 150, 30);
    private static final Color defaultColor = Color.black;
    private static final int defaultThickness = 2;
    private static final int defaultSize = 16;
    private static final int defaultMargin = 10;
    private static final int defaultAlign = LayoutGroup.HORIZONTAL;
    private static final GraphicalObject defaultLabel = 
            new OutlineRect(0, 0, 0, 0, defaultColor, 0);
    
    protected GraphicalObject label;
    protected Group gObj;
    protected GraphicalObject box;
    protected int boxSize;
    protected int margin;
    protected Color color;
    protected int lineThickness;
    
    public Checkbox() {
        this(defaultLabel);
    }
    public Checkbox(GraphicalObject label) {
        this(label, 0, 0, defaultMargin, defaultSize, 
                defaultColor, defaultThickness);
    }
    public Checkbox(GraphicalObject label, int x, int y) {
        this(label, x, y, defaultMargin, defaultSize, 
                defaultColor, defaultThickness);
    }
    public Checkbox(GraphicalObject label, int x, int y, int margin) {
        this(label, x, y, margin, defaultSize,
                defaultColor, defaultThickness);
    }
    public Checkbox(GraphicalObject label, int x, int y, 
            int margin, int boxSize) {
        this(label, x, y, margin, boxSize, defaultColor, defaultThickness);
    }
    
    public Checkbox(GraphicalObject label, int x, int y, 
            int margin, int boxSize, Color color, int lineThickness) {
        this.label = label;
        this.boxSize = boxSize;
        this.margin = margin;
        this.color = color;
        this.lineThickness = lineThickness;
        Rectangle r = label.getBoundingBox();
        int wl = r.width, hl = r.height;
        int w = boxSize + margin + wl;
        int h = Math.max(boxSize, hl);
        gObj = new LayoutGroup(x, y, w, h, defaultAlign, margin);
        box = new OutlineRect(0, 0, boxSize, boxSize, color, lineThickness);
        gObj.addChild(box);
        gObj.addChild(label);
    }
    
    private void resetLabel(GraphicalObject newLabel) {
        Rectangle r = newLabel.getBoundingBox();
        int wl = r.width, hl = r.height;
        int w = boxSize + margin + wl;
        int h = Math.max(boxSize, hl);
        gObj.resize(w, h);
        gObj.removeChild(label);
        gObj.addChild(newLabel);
        label = newLabel;
    }
    
    public GraphicalObject getLabel() {
        return label;
    }
    public void setLabel(GraphicalObject label) {
        if (label == null) {
            resetLabel(label);
        }
        if (this.label != label) {
            if (label == null) {
                resetLabel(defaultLabel);
            } else {
                resetLabel(label);
            }
        }
    }
    public int getBoxSize() {
        return boxSize;
    }
    public void setBoxSize(int boxSize) {
        if (this.boxSize != boxSize) {
            this.boxSize = boxSize;
            box.resize(boxSize, boxSize);
            Rectangle r = label.getBoundingBox();
            int wl = r.width, hl = r.height;
            int w = boxSize + margin + wl;
            int h = Math.max(boxSize, hl);
            gObj.resize(w, h);
        }
    }
    public int getMargin() {
        return margin;
    }
    public void setMargin(int margin) {
        if (this.margin != margin) {
            this.margin = margin;
            ((LayoutGroup) gObj).setOffset(margin);
            Rectangle r = label.getBoundingBox();
            int wl = r.width, hl = r.height;
            int w = boxSize + margin + wl;
            int h = Math.max(boxSize, hl);
            gObj.resize(w, h);
        }
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
                int thick = 3;
                int mx = r.x + lineThickness + padding;
                int my = r.y + lineThickness + padding;
                int mw = boxSize - 2 * lineThickness - 2 * padding;
                // int mh = boxSize - 2 * lineThickness - 2 * padding;
                mw = (mw <= 0) ? 1 : mw;
                // mh = (mh <= 0) ? 1 : mh;
                graphics.setColor(color);
                graphics.setStroke(new BasicStroke(thick));
                Rectangle mark = new Rectangle(mx, my, mw, mw);
                graphics.fill(mark);
                // graphics.fillOval(mx, my, mw, mh);
                // graphics.drawLine(mx, my, mx + mw, my + mh);
                // graphics.drawLine(mx + mw, my, mx, my + mh);
            }
            if (interimSelected) {
                Rectangle mask = new Rectangle(r.x, r.y, boxSize, boxSize);
                graphics.setColor(maskColor);
                graphics.fill(mask);
            }
        }
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        return gObj.getBoundingBox();
    }
    @Override
    public void moveTo(int x, int y) {
        gObj.moveTo(x, y);
    }
    @Override
    public void resize(int width, int height) {
        gObj.resize(width, height);
    }
    @Override
    public Group getGroup() {
        return gObj.getGroup();
    }
    @Override
    public void setGroup(Group group) {
        gObj.setGroup(group);
    }
    @Override
    public boolean contains(int x, int y) {
        return gObj.contains(x, y);
    }
    @Override
    public void setAffineTransform(AffineTransform af) {
    }
    @Override
    public AffineTransform getAffineTransform() {
        return null;
    }
    
    protected boolean interimSelected = false;
    protected boolean selected = false;
    @Override
    public void setInterimSelected(boolean interimSelected) {
        if (this.interimSelected != interimSelected) {
            this.interimSelected = interimSelected;
            Group group = getGroup();
            if (group != null) {
                group.damage(getBoundingBox());
            }
        }
    }
    @Override
    public boolean isInterimSelected() {
        return interimSelected;
    }
    @Override
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            Group group = getGroup();
            if (group != null) {
                group.damage(getBoundingBox());
            }
        }
    }
    @Override
    public boolean isSelected() {
        return selected;
    }
    
    @Override
    public void setSelectionFeedback(boolean feedback) {
        gObj.setSelectionFeedback(feedback);
    }
}
