import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


// TODO: This file is not done yet, so it has bugs now!
public class SelectionHandles implements Group, Selectable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Group group = null;
    private ArrayList<GraphicalObject> children = 
            new ArrayList<GraphicalObject>();
    
    public SelectionHandles (Color color) {
        this.color = color;
        x = y = width = height = 0;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        if (!this.color.equals(color)) {
            this.color = color;
            if (group != null) {
                group.damage(getBoundingBox());
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        Rectangle drawArea = (Rectangle) clipShape;
        Rectangle groupArea = new Rectangle(x, y, width, height);
        drawArea = groupArea.intersection(drawArea);
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
        return getBoundingBox().contains(x, y);
    }

    @Override
    public void setAffineTransform(AffineTransform af) {
    }
    @Override
    public AffineTransform getAffineTransform() {
        return null;
    }

    @Override
    public void addChild(GraphicalObject child)
            throws AlreadyHasGroupRunTimeException {
        // only one child
        if (children.size() > 0) return;
        if (child.getGroup() != null) {
            throw new AlreadyHasGroupRunTimeException(
                    "Object has already been added to a group.");
        }
        child.setGroup(this);
        BoundaryRectangle r = child.getBoundingBox();
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
        child.moveTo(0, 0);
        children.add(child);

        if (group != null) {
            group.damage(r);
        }
    }

    @Override
    public void removeChild(GraphicalObject child) {
        if (child.getGroup() != this) return;
        child.setGroup(null);
        children.remove(child);
        if (group != null) {
            group.damage(getBoundingBox());
        }
    }

    @Override
    public void resizeChild(GraphicalObject child) {
        BoundaryRectangle r = child.getBoundingBox();
        if (r.width == width && r.height == height) {
            child.moveTo(0, 0);
            return;
        }
        this.width = r.width;
        this.height = r.height;
        child.moveTo(0, 0);
        if (group != null) {
            group.resizeChild(this);
        }
    }

    @Override
    public void bringChildToFront(GraphicalObject child) {
        // no use because there's only one child
    }

    @Override
    public void resizeToChildren() {
        if (children.isEmpty()) {
            return;
        }
        BoundaryRectangle r = children.get(0).getBoundingBox();
        this.width = r.width;
        this.height = r.height;
    }

    @Override
    public void damage(BoundaryRectangle r) {
        if (group != null) {
            // transform the coordinates and propagate to the parent group
            group.damage(getBoundingBox());
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
}
