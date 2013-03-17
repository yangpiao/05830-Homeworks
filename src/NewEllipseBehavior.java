import java.awt.*;


public class NewEllipseBehavior extends NewBehavior {
    private Color color;
    private int lineThickness;
    
    public NewEllipseBehavior() {
        super();
        color = Color.black;
        lineThickness = 1;
    }
    
    public NewEllipseBehavior(Color color, int lineThickness) {
        super();
        this.color = color;
        this.lineThickness = lineThickness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (!color.equals(this.color)) {
            this.color = color;
        }
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            this.lineThickness = lineThickness;
        }
    }

    @Override
    public GraphicalObject make(int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            x1 = x1 ^ x2;
            x2 = x1 ^ x2;
            x1 = x1 ^ x2;
        }
        if (y2 < y1) {
            y1 = y1 ^ y2;
            y2 = y1 ^ y2;
            y1 = y1 ^ y2;
        }
        return new OutlineEllipse(x1, y1, x2 - x1, y2 - y1, 
                color, lineThickness);
    }

    @Override
    public void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            x1 = x1 ^ x2;
            x2 = x1 ^ x2;
            x1 = x1 ^ x2;
        }
        if (y2 < y1) {
            y1 = y1 ^ y2;
            y2 = y1 ^ y2;
            y1 = y1 ^ y2;
        }
        gobj.moveTo(x1, y1);
        gobj.resize(x2 - x1, y2 - y1);
    }

}
