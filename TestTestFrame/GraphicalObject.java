import java.awt.*;
import java.awt.geom.AffineTransform;

public interface GraphicalObject {
	public void draw(Graphics2D graphics, Shape clipShape);
	public BoundaryRectangle getBoundingBox();
	public void moveTo(int x, int y);
	public Group getGroup();
	public void setGroup(Group group);
	public boolean contains(int x, int y);
	public void setAffineTransform(AffineTransform af);
	public AffineTransform getAffineTransform();
}
