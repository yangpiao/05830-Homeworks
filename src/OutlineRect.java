import java.awt.*;
import java.awt.geom.AffineTransform;


public class OutlineRect implements GraphicalObject, Selectable {
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

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            this.lineThickness = lineThickness;
            createRect();
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
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
        // Rectangle r = rect;
        Rectangle r = new Rectangle(x, y, width - 1, height - 1);
        Rectangle r1 = new Rectangle(r.x, r.y, 4, 4);
        Rectangle r2 = new Rectangle(r.x, r.y + r.height - 3, 4, 4);
        Rectangle r3 = new Rectangle(r.x + r.width - 3, r.y, 4, 4);
        Rectangle r4 = new Rectangle(r.x + r.width - 3, 
                r.y + r.height - 3, 4, 4);
        graphics.setStroke(new BasicStroke(1));
        if (selected && !interimSelected) {
            graphics.setColor(Color.darkGray);
            graphics.draw(r);
            graphics.fill(r1);
            graphics.fill(r2);
            graphics.fill(r3);
            graphics.fill(r4);
        } else if (interimSelected) {
            graphics.setColor(Color.lightGray);
            graphics.draw(r);
            graphics.fill(r1);
            graphics.fill(r2);
            graphics.fill(r3);
            graphics.fill(r4);
        }
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
    public void resize(int width, int height) {
        if (this.width != width || this.height != height) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.width = width;
            this.height = height;
            createRect();
            if (group != null) {
                group.resizeChild(this);
                group.damage(getBoundingBox());
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
    
    private boolean interimSelected = false;
    private boolean selected = false;
    
    @Override
    public void setInterimSelected(boolean interimSelected) {
        if (this.interimSelected != interimSelected) {
            this.interimSelected = interimSelected;
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
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
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
            }
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

}
