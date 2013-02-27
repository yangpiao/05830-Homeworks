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
            Rectangle2D oldBounds = bounds;
            this.text = text;
            bounds = font.getStringBounds(text, frc);
            if (group != null) {
                int oldW = (int)oldBounds.getWidth(); 
                int oldH = (int)oldBounds.getHeight();
                int oldX = (int)oldBounds.getX();
                int oldY = (int)oldBounds.getY();
                int width = (int)bounds.getWidth(); 
                int height = (int)bounds.getHeight();
                int bx = (int)bounds.getX();
                int by = (int)bounds.getY();
                group.damage(new BoundaryRectangle(x + oldX, y + oldY, 
                        oldW, oldH));
                group.damage(new BoundaryRectangle(x + bx, y + by, 
                        width, height));
                group.resizeChild(this);
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (this.x != x) {
            if (group != null) {
                int width = (int)bounds.getWidth();
                int height = (int)bounds.getHeight();
                int bx = (int)bounds.getX();
                int by = (int)bounds.getY();
                group.damage(new BoundaryRectangle(this.x + bx, y + by,
                        width, height));
                group.damage(new BoundaryRectangle(x + bx, y + by, 
                        width, height));
            }
            this.x = x;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (this.y != y) {
            if (group != null) {
                int width = (int)bounds.getWidth(); 
                int height = (int)bounds.getHeight();
                int bx = (int)bounds.getX();
                int by = (int)bounds.getY();
                group.damage(new BoundaryRectangle(x + bx, this.y + by,
                        width, height));
                group.damage(new BoundaryRectangle(x + bx, y + by,
                        width, height));
            }
            this.y = y;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (this.font != font) {
            Rectangle2D oldBounds = bounds;
            this.font = font;
            bounds = font.getStringBounds(text, frc);
            if (group != null) {
                int oldW = (int)oldBounds.getWidth(); 
                int oldH = (int)oldBounds.getHeight();
                int oldX = (int)oldBounds.getX();
                int oldY = (int)oldBounds.getY();
                int width = (int)bounds.getWidth(); 
                int height = (int)bounds.getHeight();
                int bx = (int)bounds.getX();
                int by = (int)bounds.getY();
                group.damage(new BoundaryRectangle(x + oldX, y + oldY, 
                        oldW, oldH));
                group.damage(new BoundaryRectangle(x + bx, y + by, 
                        width, height));
                group.resizeChild(this);
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;
            if (group != null) {
                int w = (int)bounds.getWidth(); 
                int h = (int)bounds.getHeight();
                int bx = (int)bounds.getX();
                int by = (int)bounds.getY();
                group.damage(new BoundaryRectangle(x + bx, y + by, w, h));
            }
        }
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
        int bx = (int)bounds.getX();
        int by = (int)bounds.getY();
        if (this.x != x - bx || this.y != y - by) {
            int w = (int)bounds.getWidth(); 
            int h = (int)bounds.getHeight();
            if (group != null) {
                group.damage(new BoundaryRectangle(this.x + bx, 
                        this.y + by, w, h));
                group.damage(new BoundaryRectangle(x + bx, y + by, w, h));
            }
            this.x = x - bx;
            this.y = y - by;
            if (group != null) {
                group.resizeChild(this);
            }
        }

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
