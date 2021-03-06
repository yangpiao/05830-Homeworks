import java.awt.*;
import java.awt.geom.*;


public class Line implements GraphicalObject, Selectable {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color color;
    private int lineThickness;
    private Group group = null;
    private Stroke stroke = null;
    
    private void updateStroke() {
        stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER);
    }


    public Line() {
        this(0, 0, 0, 0, Color.BLACK, 1);
    }

    public Line(int x1, int y1, int x2, int y2, 
            Color color, int lineThickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.lineThickness = lineThickness;
        updateStroke();
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        if (this.x1 != x1) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.x1 = x1;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        if (this.y1 != y1) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.y1 = y1;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        if (this.x2 != x2) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.x2 = x2;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        if (this.y2 != y2) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.y2 = y2;
            if (group != null) {
                group.damage(getBoundingBox());
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
                group.damage(getBoundingBox());
            }
        }
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.lineThickness = lineThickness;
            updateStroke();
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
//        graphics.setClip(clipShape);
//        graphics.setStroke(stroke);
//        graphics.setColor(color);
//        graphics.drawLine(x1, y1, x2, y2);
        
        graphics.setClip(clipShape);
        graphics.setColor(color);
        graphics.setStroke(stroke);
        BoundaryRectangle localBox = getBoundingBox();
        Point ul = localBox.getLocation();
        int xDiff = ul.x - localBox.x;
        int yDiff = ul.y - localBox.y;
        graphics.drawLine(x1 + xDiff, y1 + yDiff, x2 + xDiff, y2 + yDiff);
        
        if ((selected || interimSelected) && selectionFeedback) {
            Rectangle r = new Rectangle(localBox.x, localBox.y, 
                    localBox.width - 1, localBox.height - 1);
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
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        if (lineThickness < 1) {
            return new BoundaryRectangle(0, 0, 0, 0);
        }
        if (lineThickness == 1) {
            return new BoundaryRectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        }

        boolean parallel = (x1 == x2 || y1 == y2);
        float adjust = (parallel && getLineThickness() % 2 == 1) ? 0.5f : 0.0f;
        Shape line = new Line2D.Float(x1 + adjust, y1 + adjust,
                x2 + adjust, y2 + adjust);
        Shape strokedLine = stroke.createStrokedShape(line);
        Rectangle r = strokedLine.getBounds();
        return new BoundaryRectangle(r.x, r.y, r.width, r.height);
    }

    @Override
    public void moveTo(int x, int y) {
        BoundaryRectangle r = getBoundingBox();
        int offsetX = x - r.x, offsetY = y - r.y;
        if (offsetX != 0 || offsetY != 0) {
            if (group != null) {
                group.damage(r);
            }
            x1 += offsetX;
            y1 += offsetY;
            x2 += offsetX;
            y2 += offsetY;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }
    
    @Override
    public void resize(int width, int height) {
        BoundaryRectangle r = getBoundingBox();
        int w = r.width, h = r.height;
        if (w != width || h != height) {
            if (group != null) {
                group.damage(r);
            }
            x2 = x2 + width - w;
            y2 = y2 + height - h;
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
        // TODO: more precise `contains`
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

    private boolean interimSelected = false;
    private boolean selected = false;
    
    @Override
    public void setInterimSelected(boolean interimSelected) {
        if (this.interimSelected != interimSelected) {
            this.interimSelected = interimSelected;
            if (group != null) {
                group.damage(getBoundingBox());
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
                group.damage(getBoundingBox());
            }
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
    
    private boolean selectionFeedback = true;
    @Override
    public void setSelectionFeedback(boolean feedback) {
        selectionFeedback = feedback;
        if (group != null) {
            group.damage(getBoundingBox());
        }
    }
}
