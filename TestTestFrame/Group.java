import java.awt.*;

public interface Group extends GraphicalObject {
	public void addChild(GraphicalObject child) throws AlreadyHasGroupRunTimeException;
	public void removeChild(GraphicalObject child);
	public void resizeChild(GraphicalObject child);
	public void bringChildToFront(GraphicalObject child);
	public void resizeToChildren();
	public void damage(BoundaryRectangle rectangle);
	public java.util.List<GraphicalObject> getChildren();
	public Point parentToChild(Point pt);
	public Point childToParent(Point pt);

	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	// maybe use enum but then couldn't have sub-classes with additional values
}
