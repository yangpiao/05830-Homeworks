import java.awt.*;

public class NewTextBehavior extends NewBehavior {
    private Color color;
    private Font font;
    private String text;
    
    public NewTextBehavior() {
        super(true);
        text = "";
        color = Color.black;
        font = new Font("SansSerif", Font.PLAIN, 12);
    }
    
    public NewTextBehavior(String text, Color color, Font font) {
        super(true);
        this.text = text;
        this.color = color;
        this.font = font;
    }
    
    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (!font.equals(this.font)) {
            this.font = font;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!text.equals(this.text)) {
            this.text = text;
        }
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
        return new Text(text, x1, y1, font, color);
    }

    @Override
    public void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2) {
        gobj.moveTo(x2, y2);
    }

}
