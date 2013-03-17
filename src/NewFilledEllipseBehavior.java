import java.awt.*;

public class NewFilledEllipseBehavior extends NewBehavior {
    private Color color;
    
    public NewFilledEllipseBehavior() {
        super();
        color = Color.black;
    }
    
    public NewFilledEllipseBehavior(Color color) {
        super();
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (!color.equals(this.color)) {
            this.color = color;
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
        return new FilledEllipse(x1, y1, x2 - x1, y2 - y1, color);
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
