import java.awt.*;


public class NewNodeBehavior extends NewBehavior {
    private Color color;
    private int side;
    
    public NewNodeBehavior() {
        super(true);
        color = Color.black;
        side = 0;
    }
    
    public NewNodeBehavior(Color color, int side) {
        super(true);
        this.color = color;
        this.side = side;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (!color.equals(this.color)) {
            this.color = color;
        }
    }
    
    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
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
        return new FilledRect(x1, y1, side, side, color);
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
    }

}
