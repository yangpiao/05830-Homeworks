import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.font.*;


public class Text implements GraphicalObject {
    private String text;
    private int x;
    private int y;
    private Font font;
    private Color color;
    private FontRenderContext frc = new FontRenderContext(null, false, false);
    private Rectangle2D bounds;
    private Group group = null;

    public Text() {
        this("", 0, 0, null, null);
    }
    
    public Text(String text, int x, int y, Font font, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.font = font;
        this.color = color;
        bounds = font.getStringBounds(text, frc);
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!this.text.equals(text)) {
            this.text = text;
            bounds = font.getStringBounds(text, frc);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, x, y);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x + (int)bounds.getX(), 
                y + (int)bounds.getY(), 
                (int)bounds.getWidth(), 
                (int)bounds.getHeight());
    }

    @Override
    public void moveTo(int x, int y) {
        this.x = x - (int)bounds.getX();
        this.y = y - (int)bounds.getY();
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean contains(int x, int y) {
        return getBoundingBox().contains(x, y);
    }

    @Override
    public void setAffineTransform(AffineTransform af) {
        // TODO Auto-generated method stub

    }

    @Override
    public AffineTransform getAffineTransform() {
        // TODO Auto-generated method stub
        return null;
    }

}
