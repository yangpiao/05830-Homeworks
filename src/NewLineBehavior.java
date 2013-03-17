import java.awt.*;

public class NewLineBehavior extends NewBehavior {
    private Color color;
    private int lineThickness;
    
    public NewLineBehavior() {
        super();
        color = Color.black;
        lineThickness = 1;
    }
    
    public NewLineBehavior(Color color, int lineThickness) {
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
        return new Line(x1, y1, x2, y2, color, lineThickness);
    }
    
    @Override
    public void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2) {
        // gobj.resize(x2 - x1, y2 - y1); buggy
        Line line = (Line) gobj;
        line.setX2(x2);
        line.setY2(y2);
    }

}
