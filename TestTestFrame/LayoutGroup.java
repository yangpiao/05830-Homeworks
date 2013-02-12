import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class LayoutGroup implements Group {
    private int x;
    private int y;
    private int width;
    private int height;
    private int layout;
    private int offset;
    private Group group = null;
    private ArrayList<GraphicalObject> children = 
            new ArrayList<GraphicalObject>();
    private BoundaryRectangle damagedArea = null;
    private boolean layoutChanged = false;
    
    public static final int FILL = 3;
    public static final int GRID = 4;
    private int rows = 0;
    private int cols = 0;
    
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
    
    private void changeLayout() {
        ListIterator<GraphicalObject> it = children.listIterator();
        if (!it.hasNext()) return;
        GraphicalObject next = it.next();
        int x = 0, y = 0, rowHeight;
        next.moveTo(x, y);
        BoundaryRectangle r = next.getBoundingBox();
        rowHeight = r.height;
        switch (layout) {
        case HORIZONTAL:
            x += r.width + offset;
            while (it.hasNext()) {
                next = it.next();
                next.moveTo(x, y);
                r = next.getBoundingBox();
                x += r.width + offset;
            }
            break;
        case VERTICAL:
            x += r.width + offset;
            while (it.hasNext()) {
                next = it.next();
                next.moveTo(x, y);
                r = next.getBoundingBox();
                x += r.width + offset;
            }
            break;
        case FILL:
            x += r.width + offset;
            while (it.hasNext()) {
                next = it.next();
                r = next.getBoundingBox();
                if (x + r.width >= width) { // overflow
                    x = 0;
                    y += rowHeight + offset;
                    rowHeight = 0;
                }
                next.moveTo(x, y);
                x += r.width + offset;
                if (r.height > rowHeight) {
                    rowHeight = r.height;
                }
            }
            break;
        case GRID:
            int w = (width - (cols - 1) * offset) / cols;
            int h = (height - (rows - 1) * offset) / rows;
            int count = 1;
            x += w + offset;
            while (it.hasNext()) {
                next = it.next();
                if (count < rows * cols) {
                    if (count % cols == 0) { // end of a row
                        x = 0;
                        y += h + offset;
                    }
                    next.moveTo(x, y);
                    x += w + offset;
                } else {
                    // if the number of children exceeds rows * cols,
                    // hide the rest of children
                    next.moveTo(-100000, -1000000);
                }
                count++;
            }
            break;
        }

        layoutChanged = false;
    }
    
    public LayoutGroup() {
        this(0, 0, 0, 0, HORIZONTAL, 0);
    }
    
    public LayoutGroup(int x, int y, int width, int height, 
            int layout, int offset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.offset = offset;
    }
    
    public LayoutGroup(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols) {
        this(x, y, width, height, HORIZONTAL, offset);
        if (layout == GRID) {
            this.layout = GRID;
            this.rows = rows;
            this.cols = cols;
        }
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

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        if (this.layout != layout) {
            this.layout = layout;
            // changeLayout();
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (this.offset != offset) {
            this.offset = offset;
            // changeLayout();
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        if (this.rows != rows) {
            this.rows = rows <= 0 ? 1 : rows;
            // changeLayout();
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }
    
    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        if (this.cols != cols) {
            this.cols = cols <= 0 ? 1 : cols;
            // changeLayout();
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, width, height));
                group.resizeChild(this);
            }
        }
    }
    
    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        if (layoutChanged) changeLayout();
        
        // if damagedArea is not null, only draw inside the area
        Shape drawArea;
        if (damagedArea != null) {
            // transform the coordinates of the damaged area
            drawArea = new BoundaryRectangle(damagedArea.x + x, 
                    damagedArea.y + y, damagedArea.width, damagedArea.height);
        } else {
            drawArea = clipShape;
        }
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
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        if (layoutChanged) changeLayout();
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
        changeLayout();
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
        changeLayout();
        // update damaged area
        damagedArea.setLocation(0, 0);
        damagedArea.setSize(width, height);
        if (group != null) {
            group.damage(new BoundaryRectangle(x, y, width, height));
            group.resizeChild(this);
        }
    }

    @Override
    public void resizeChild(GraphicalObject child) {
        layoutChanged = true;
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
        changeLayout();
        if (group != null) {
            group.damage(new BoundaryRectangle(x, y, width, height));
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
        layoutChanged = true;
        if (group != null) {
            group.damage(new BoundaryRectangle(x, y, width, height));
        }
        
        /*
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
        */
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
        return new Point(pt.x - x, pt.y - y);
    }

    @Override
    public Point childToParent(Point pt) {
        return new Point(pt.x + x, pt.y + y);
    }

}
