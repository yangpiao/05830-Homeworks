import java.awt.*;
import java.awt.geom.AffineTransform;


public class FilledRect implements GraphicalObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Rectangle rect = null;
    private Group group = null;
    
    private void createRect() {
        // test
        rect = new Rectangle(x, y, width, height);
    }

    public FilledRect() {
        this(0, 0, 0, 0, Color.BLACK);
    }
    
    public FilledRect(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        createRect();
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (this.x != x) {
            if (group != null) {
                group.damage(new BoundaryRectangle(this.x, y, width, height));
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
            this.x = x;
            createRect();
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
                group.damage(new BoundaryRectangle(x, this.y, width, height));
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
            this.y = y;
            createRect();
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, this.width, height));
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
            this.width = width;
            createRect();
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, this.height));
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
            this.height = height;
            createRect();
            if (group != null) {
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
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        graphics.setColor(color);
        graphics.fill(rect);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, width, height);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.x != x || this.y != y) {
            if (group != null) {
                group.damage(new BoundaryRectangle(this.x, this.y, 
                        width, height));
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
            this.x = x;
            this.y = y;
            createRect();
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