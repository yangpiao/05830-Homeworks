import java.awt.*;
import java.awt.geom.AffineTransform;


public class Icon implements GraphicalObject {
    private Image image;
    private int x;
    private int y;
    private Group group = null;

    public Icon() {
        this(null, 0, 0);
    }
    
    public Icon(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if (this.image != image) {
            int ow = this.image.getWidth(null), oh = this.image.getHeight(null);
            int w = image.getWidth(null), h = image.getHeight(null);
            this.image = image;
            if (group != null) {
                group.damage(new BoundaryRectangle(x, y, ow, oh));
                group.damage(new BoundaryRectangle(x, y, w, h));
                group.resizeChild(this);
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        int w = image.getWidth(null), h = image.getHeight(null);
        if (this.x != x) {
            if (group != null) {
                group.damage(new BoundaryRectangle(this.x, y, w, h));
                group.damage(new BoundaryRectangle(x, y, w, h));
            }
            this.x = x;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        int w = image.getWidth(null), h = image.getHeight(null);
        if (this.y != y) {
            if (group != null) {
                group.damage(new BoundaryRectangle(x, this.y, w, h));
                group.damage(new BoundaryRectangle(x, y, w, h));
            }
            this.y = y;
            if (group != null) {
                group.resizeChild(this);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        graphics.drawImage(image, x, y, null);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        return new BoundaryRectangle(x, y, 
                image.getWidth(null), image.getHeight(null));
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.x != x || this.y != y) {
            int w = image.getWidth(null), h = image.getHeight(null);
            if (group != null) {
                group.damage(new BoundaryRectangle(this.x, this.y, w, h));
                group.damage(new BoundaryRectangle(x, y, w, h));
            }
            this.x = x;
            this.y = y;
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

}
