import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.util.*;


public class SimpleGroup implements Group, Selectable {
    private int x;
    private int y;
    private int width;
    private int height;
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

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        // if damagedArea is not null, only draw inside the area
        Shape drawArea;
//        if (damagedArea != null) {
//            // transform the coordinates of the damaged area
//            drawArea = new BoundaryRectangle(damagedArea.x + x, 
//                    damagedArea.y + y, damagedArea.width, damagedArea.height);
//        } else {
//            drawArea = clipShape;
//        }
//        Rectangle groupArea = new Rectangle(x, y, width, height);
//        drawArea = groupArea.intersection((Rectangle) drawArea);
        
        // drawArea = clipShape;
        drawArea = getBoundingBox();
        graphics.setClip(drawArea);
        
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
        
        if ((selected || interimSelected) && selectionFeedback) {
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
    public void resize(int width, int height) {
        if (this.width != width || this.height != height) {
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, this.width, this.height));
            }
            this.width = width;
            this.height = height;
            damagedArea = null;
            if (group != null) {
                group.resizeChild(this);
                group.damage(new BoundaryRectangle(x, y, width, height));
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
        // return getBoundingBox().contains(x, y);
        return (new Rectangle(this.x, this.y, width, height)).contains(x, y);
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
        /*
        if (group != null && damagedArea != null) {
            group.damage(new BoundaryRectangle(damagedArea.x + x, 
                    damagedArea.y + y, damagedArea.width, damagedArea.height));
        }
        */
        // update damaged area
        BoundaryRectangle r = child.getBoundingBox();
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
        /*
        if (group != null && damagedArea != null) {
            group.damage(new BoundaryRectangle(damagedArea.x + x, 
                    damagedArea.y + y, damagedArea.width, damagedArea.height));
        }
        */
        // update damaged area
        BoundaryRectangle r = child.getBoundingBox();
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
            Rectangle r = child.getBoundingBox();
            Rectangle groupArea = new Rectangle(0, 0, width, height);
            Rectangle resized = r.intersection(groupArea);
            group.damage(new BoundaryRectangle(resized.x + x, resized.y + y, 
                    resized.width, resized.height));
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
        
        // if part or all of the damaged area is outside the group, resize
        Rectangle groupArea = new Rectangle(0, 0, width, height);
        Rectangle resized = rectangle.intersection(groupArea);
        if (group != null) {
            // transform the coordinates and propagate to the parent group
            group.damage(new BoundaryRectangle(resized.x + x, resized.y + y, 
                    resized.width, resized.height));
        }
    }

    @Override
    public List<GraphicalObject> getChildren() {
        List<GraphicalObject> copy = new ArrayList<GraphicalObject>();
        int end = children.size();
        ListIterator<GraphicalObject> it = children.listIterator(end);
        while (it.hasPrevious()) {
            copy.add(it.previous());
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

    private boolean selectionFeedback = true;
    @Override
    public void setSelectionFeedback(boolean feedback) {
        selectionFeedback = feedback;
        if (group != null) {
            group.damage(getBoundingBox());
        }
    }
}
