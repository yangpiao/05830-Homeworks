import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;


public class OutlineRect implements GraphicalObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private int lineThickness;
    private Rectangle rect = null;
    private Group group = null;
    
    /**
     * Create a rectangle using the metrics given
     */
    private void createRect() {
        int newX, newY, newW, newH, halfLine = lineThickness / 2;
        newX = x + halfLine;
        newY = y + halfLine;
        newW = width - lineThickness;
        newH = height - lineThickness;
        if (rect == null) {
            rect = new Rectangle(newX, newY, newW, newH);
        } else {
            rect.setLocation(newX, newY);
            rect.setSize(newW, newH);
        }
    }

    public OutlineRect() {
        this(0, 0, 0, 0, Color.BLACK, 1);
    }

    public OutlineRect(int x, int y, int width, int height, 
            Color color, int lineThickness) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.lineThickness = lineThickness;
        createRect();
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (this.x != x) {
            this.x = x;
            createRect();
            if (this.group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (this.y != y) {
            this.y = y;
            createRect();
            if (this.group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            this.width = width;
            createRect();
            if (this.group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            createRect();
            if (this.group != null) {
                group.resizeChild(this);
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            this.lineThickness = lineThickness;
            createRect();
            // changing lineThickness won't change the bounding box
            // if (this.group != null) {
            //     group.resizeChild(this);
            // }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        graphics.setStroke(new BasicStroke(lineThickness));
        graphics.setColor(color);
        graphics.draw(rect);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, width, height);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            createRect();
            if (this.group != null) {
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
        if (x >= this.x && x < this.x + width && 
                y >= this.y && y < this.y + height) {
            return true;
        }
        return false;
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
