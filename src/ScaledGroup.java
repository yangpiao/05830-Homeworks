import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class ScaledGroup implements Group {
    private int x;
    private int y;
    private int width;
    private int height;
    private double scaleX;
    private double scaleY;
    private Group group = null;
    private ArrayList<GraphicalObject> children = 
            new ArrayList<GraphicalObject>();
    private BoundaryRectangle damagedArea = null;
    
    private void changeDamagedArea(BoundaryRectangle r) {
        if (damagedArea == null) {
            damagedArea = new BoundaryRectangle(r);
        } else {
            damagedArea.add(r);
        }
        
        // if part or all of the damaged area is outside the group, resize
        Rectangle groupArea = new Rectangle(0, 0, width, height);
        Rectangle resized = damagedArea.intersection(groupArea);
        damagedArea.setLocation(resized.x, resized.y);
        damagedArea.setSize(resized.width, resized.height);
    }
    
    private int calcX(int n) {
        return (int)Math.round(n * scaleX);
    }
    private int calcY(int n) {
        return (int)Math.round(n * scaleY);
    }
    
    public ScaledGroup() {
        this(0, 0, 0, 0, 1.0, 1.0);
    }
    
    public ScaledGroup(int x, int y, int width, int height, double scaleX, double scaleY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scaleX = Math.abs(scaleX);
        this.scaleY = Math.abs(scaleY);
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
            // reset damaged area
            damagedArea = null;
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
            // reset damaged area
            damagedArea = null;
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
            // reset damaged area
            damagedArea = null;
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
            // reset damaged area
            damagedArea = null;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        if (this.scaleX != Math.abs(scaleX)) {
            this.scaleX = Math.abs(scaleX);
            // reset damaged area
            damagedArea = null;
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        if (this.scaleY != Math.abs(scaleY)) {
            this.scaleY = Math.abs(scaleY);
            // reset damaged area
            damagedArea = null;
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        // if damagedArea is not null, only draw inside the area
        Rectangle clipping;
        if (damagedArea != null) {
            damagedArea.setLocation(damagedArea.x + x, damagedArea.y + y);
            clipping = (Rectangle)damagedArea;
        } else {
            clipping = (Rectangle)clipShape;
        }
        int dx = 0, dy = 0, dw = 0, dh = 0;
        if (scaleX > 0.0000000001 && scaleY > 0.0000000001) {
            // avoid divided by zero
            // transform the coordinates
            dx = (int)Math.floor((clipping.x - x) / scaleX);
            dy = (int)Math.floor((clipping.y - y) / scaleY);
            dw = (int)Math.ceil(clipping.width / scaleX);
            dh = (int)Math.ceil(clipping.height / scaleY);
        }
        BoundaryRectangle drawArea = new BoundaryRectangle(dx, dy, dw, dh);
        graphics.setClip(drawArea);
        graphics.translate(x, y);
        graphics.scale(scaleX, scaleY);
        
        // draw all the children
        for (GraphicalObject child : children) {
            child.draw(graphics, drawArea);
        }
        
        // reset transforms
        if (scaleX > 0.0000000001 && scaleY > 0.0000000001) {
            graphics.scale(1.0 / scaleX, 1.0 / scaleY);
        }
        graphics.translate(-x, -y);
        // reset the damaged area after drawing
        damagedArea = null;
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        BoundaryRectangle box = new BoundaryRectangle(0, 0, 0, 0);
        for (GraphicalObject child : children) {
            BoundaryRectangle r = child.getBoundingBox();
            r.setLocation(calcX(r.x) + x, calcY(r.y) + y);
            r.setSize(calcX(r.width), calcY(r.height));
            box.add(r);
        }
        // if part or all of the bounding box is outside the group, resize
        Rectangle groupArea = new Rectangle(x, y, width, height);
        Rectangle resized = box.intersection(groupArea);
        box.setLocation(resized.x, resized.y);
        box.setSize(resized.width, resized.height);
        return box;
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
            // reset damaged area
            damagedArea = null;
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

    @Override
    public void addChild(GraphicalObject child)
            throws AlreadyHasGroupRunTimeException {
        if (child.getGroup() != null) {
            throw new AlreadyHasGroupRunTimeException(
                    "Object has already been added to a group.");
        }
        child.setGroup(this);
        children.add(child);
        // update damaged area
        BoundaryRectangle r = child.getBoundingBox();
        r.setLocation(calcX(r.x), calcY(r.y));
        r.setSize(calcX(r.width), calcY(r.height));
        changeDamagedArea(r);
        if (group != null) {
            Rectangle groupArea = new Rectangle(0, 0, width, height);
            Rectangle resized = r.intersection(groupArea);
            r.setLocation(resized.x + x, resized.y + y);
            r.setSize(resized.width, resized.height);
            group.damage(r);
            group.resizeChild(this);
        }
    }

    @Override
    public void removeChild(GraphicalObject child) {
        if (child.getGroup() != this) return;
        child.setGroup(null);
        children.remove(child);
        BoundaryRectangle r = child.getBoundingBox();
        r.setLocation(calcX(r.x), calcY(r.y));
        r.setSize(calcX(r.width), calcY(r.height));
        changeDamagedArea(r);
        if (group != null) {
            Rectangle groupArea = new Rectangle(0, 0, width, height);
            Rectangle resized = r.intersection(groupArea);
            r.setLocation(resized.x + x, resized.y + y);
            r.setSize(resized.width, resized.height);
            group.damage(r);
            group.resizeChild(this);
        }
    }

    @Override
    public void resizeChild(GraphicalObject child) {
        if (group != null) {
            group.resizeChild(this);
        }
    }

    @Override
    public void bringChildToFront(GraphicalObject child) {
        if (child.getGroup() != this) {
            return;
        }
        // move the child to the end of the list
        children.remove(child);
        children.add(child);
        if (group != null) {
            BoundaryRectangle r = child.getBoundingBox();
            r.setLocation(calcX(r.x), calcY(r.y));
            r.setSize(calcX(r.width), calcY(r.height));
            Rectangle groupArea = new Rectangle(0, 0, width, height);
            Rectangle resized = r.intersection(groupArea);
            r.setLocation(resized.x + x, resized.y + y);
            r.setSize(resized.width, resized.height);
            group.damage(r);
        }
    }

    @Override
    public void resizeToChildren() {
        if (children.isEmpty()) {
            return;
        }
        // compute the width and height
        int x1, y1, x2, y2, cx1, cy1, cx2, cy2;
        ListIterator<GraphicalObject> it = children.listIterator();
        GraphicalObject next = it.next();
        Rectangle r = next.getBoundingBox();
        x1 = calcX(r.x);
        y1 = calcY(r.y);
        x2 = x1 + calcX(r.width) - 1;
        y2 = y1 + calcY(r.height) - 1;
        while (it.hasNext()) {
            next = it.next();
            r = next.getBoundingBox();
            cx1 = calcX(r.x);
            cy1 = calcY(r.y);
            cx2 = cx1 + calcX(r.width) - 1;
            cy2 = cy1 + calcY(r.height) - 1;
            if (cx2 > x2) {
                x2 = cx2;
            }
            if (cy2 > y2) {
                y2 = cy2;
            }
        }
        // resize
        width = x2;
        height = y2;
    }

    @Override
    public void damage(BoundaryRectangle r) {
        r.setLocation(calcX(r.x) - 1, calcY(r.y) - 1);
        r.setSize(calcX(r.width) + 2, calcY(r.height) + 2);
        changeDamagedArea(r);
        if (group != null) {
            Rectangle groupArea = new Rectangle(0, 0, width, height);
            Rectangle resized = r.intersection(groupArea);
            r.setLocation(resized.x + x, resized.y + y);
            r.setSize(resized.width, resized.height);
            group.damage(r);
        }
    }

    @Override
    public List<GraphicalObject> getChildren() {
        List<GraphicalObject> copy = new ArrayList<GraphicalObject>();
        int end = children.size() - 1;
        ListIterator<GraphicalObject> it = children.listIterator(end);
        while (it.hasPrevious()) {
            copy.add(it.previous());
        }
        return copy;
    }

    @Override
    public Point parentToChild(Point pt) {
        if (scaleX > 0.0000000001 && scaleY > 0.0000000001) {
            return new Point((int)Math.round((pt.x - x) / scaleX), 
                    (int)Math.round((pt.y - y) / scaleY));
        } else {
            return new Point(0, 0);
        }
    }

    @Override
    public Point childToParent(Point pt) {
        return new Point(calcX(pt.x) + x, calcY(pt.y) + y);
    }

}
