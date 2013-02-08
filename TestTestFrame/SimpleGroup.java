import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.*;


public class SimpleGroup implements Group {
    private int x;
    private int y;
    private int width;
    private int height;
    private Group group = null;
    private ArrayList<GraphicalObject> children = 
            new ArrayList<GraphicalObject>();
    private BoundaryRectangle damagedArea = null;
    // private BoundaryRectangle bounds = new BoundaryRectangle(0, 0, 0, 0);
    
    private void changeDamagedArea(BoundaryRectangle r) {
        if (damagedArea == null) {
            damagedArea = new BoundaryRectangle(r);
        } else {
            damagedArea.add(r);
        }
    }

    public SimpleGroup() {
        this(0, 0, 0, 0);
    }
    
    public SimpleGroup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (this.x != x) {
            // bounds.x += x - this.x;
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
            // bounds.y += y - this.y;
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
            // if (x + width < bounds.x + bounds.width) {
            //     bounds.width = width + x - bounds.x;
            // }
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
            // if (y + height < bounds.y + bounds.height) {
            //     bounds.height = height + y - bounds.y;
            // }
            this.height = height;
            // reset damaged area
            damagedArea = null;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        // if damagedArea is not null, only draw inside the area
        graphics.setClip(clipShape);
        
        // transform the coordinates of the damaged area
        Shape drawArea;
        if (damagedArea != null) {
            damagedArea.setLocation(damagedArea.x + x, damagedArea.y + y);
            drawArea = damagedArea;
        } else {
            drawArea = clipShape;
        }
        
        // draw all the children
        for (GraphicalObject child : children) {
            Rectangle r = child.getBoundingBox();
            int oldX = r.x, oldY = r.y;
            // transform the coordinates
            child.moveTo(x + oldX, y + oldY);
            child.draw(graphics, drawArea);
            // change the coordinates back
            child.moveTo(oldX, oldY);
        }
        
        // reset the damaged area after drawing
        damagedArea = null;
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        // return new BoundaryRectangle(bounds);
        BoundaryRectangle box = new BoundaryRectangle(0, 0, 0, 0);
        for (GraphicalObject child : children) {
            BoundaryRectangle r = child.getBoundingBox();
            r.setLocation(r.x + x, r.y + y);
            box.add(r);
        }
        return box;
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.x != x || this.y != y) {
            // bounds.x += x - this.x;
            // bounds.y += y - this.y;
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
        changeDamagedArea(child.getBoundingBox());
        /*
        // update bounds
        Rectangle r = child.getBoundingBox();
        r.setLocation(r.x + x, r.y + y);
        bounds.add(r);
        */
        if (group != null) {
            group.resizeChild(this);
        }
    }

    @Override
    public void removeChild(GraphicalObject child) {
        child.setGroup(null);
        children.remove(child);
        // update damaged area
        changeDamagedArea(child.getBoundingBox());
        
        /*
        // update bounds
        bounds.setLocation(0, 0);
        bounds.setSize(0, 0);
        for (GraphicalObject c : children) {
            Rectangle r = c.getBoundingBox();
            r.setLocation(r.x + x, r.y + y);
            bounds.add(r);
        }
        */
        if (group != null) {
            group.resizeChild(this);
        }
    }

    @Override
    public void resizeChild(GraphicalObject child) {
        if (group != null) {
            group.resizeChild(this);
        }
        /*
        // update bounds
        bounds.setLocation(0, 0);
        bounds.setSize(0, 0);
        for (GraphicalObject c : children) {
            Rectangle r = c.getBoundingBox();
            r.setLocation(r.x + x, r.y + y);
            bounds.add(r);
        }
        */
    }

    @Override
    public void bringChildToFront(GraphicalObject child) {
        if (child.getGroup() != this) {
            return;
        }
        // move the child to the end of the list
        children.remove(child);
        children.add(child);
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
        x1 = r.x;
        y1 = r.y;
        x2 = x1 + r.width - 1;
        y2 = y1 + r.height - 1;
        while (it.hasNext()) {
            next = it.next();
            r = next.getBoundingBox();
            cx1 = r.x;
            cy1 = r.y;
            cx2 = cx1 + r.width - 1;
            cy2 = cy1 + r.height - 1;
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
    public void damage(BoundaryRectangle rectangle) {
        // update damaged area
        changeDamagedArea(rectangle);
        
        if (this.group != null) {
            // transform the coordinates and propagate to the parent group
            group.damage(new BoundaryRectangle(rectangle.x + x, rectangle.y + y, 
                    rectangle.width, rectangle.height));
        }
    }

    @Override
    public List<GraphicalObject> getChildren() {
        List<GraphicalObject> copy = new ArrayList<GraphicalObject>();
        int end = children.size() - 1;
        ListIterator<GraphicalObject> it = children.listIterator(end);
        while (it.hasPrevious()) {
            copy.add(it.next());
        }
        return copy;
    }

    @Override
    public Point parentToChild(Point pt) {
        return new Point(pt.x - x, pt.y - y);
    }

    @Override
    public Point childToParent(Point pt) {
        return new Point(pt.x + x, pt.y + y);
    }

}
