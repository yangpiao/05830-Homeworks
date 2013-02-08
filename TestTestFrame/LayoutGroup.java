import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;


public class LayoutGroup implements Group {
    private int x;
    private int y;
    private int width;
    private int height;
    private int layout;
    private int offset;
    
    public LayoutGroup() {
        this(0, 0, 0, 0, 0, 0);
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        // TODO Auto-generated method stub

    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moveTo(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public Group getGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGroup(Group group) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean contains(int x, int y) {
        // TODO Auto-generated method stub
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

    @Override
    public void addChild(GraphicalObject child)
            throws AlreadyHasGroupRunTimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeChild(GraphicalObject child) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resizeChild(GraphicalObject child) {
        // TODO Auto-generated method stub

    }

    @Override
    public void bringChildToFront(GraphicalObject child) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resizeToChildren() {
        // TODO Auto-generated method stub

    }

    @Override
    public void damage(BoundaryRectangle rectangle) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<GraphicalObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point parentToChild(Point pt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point childToParent(Point pt) {
        // TODO Auto-generated method stub
        return null;
    }

}
