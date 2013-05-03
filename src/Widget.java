import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;


public abstract class Widget implements GraphicalObject {
    protected List<Behavior> behaviors;
    protected List<Constraint> constraints;
    protected Group container;
    
    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public Group getContainer() {
        return container;
    }

    public Widget() {
        behaviors = new ArrayList<Behavior>();
        constraints = new ArrayList<Constraint>();
    }
    
    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        container.draw(graphics, clipShape);
    }
    @Override
    public BoundaryRectangle getBoundingBox() {
        return container.getBoundingBox();
    }
    @Override
    public void moveTo(int x, int y) {
        container.moveTo(x, y);
    }
    @Override
    public void resize(int width, int height) {
        container.resize(width, height);
    }
    @Override
    public Group getGroup() {
        return container.getGroup();
    }
    @Override
    public void setGroup(Group group) {
        container.setGroup(group);
    }
    @Override
    public boolean contains(int x, int y) {
        return container.contains(x, y);
    }
    @Override
    public void setAffineTransform(AffineTransform af) {
    }
    @Override
    public AffineTransform getAffineTransform() {
        return null;
    }
    @Override
    public void setSelectionFeedback(boolean feedback) {
        container.setSelectionFeedback(feedback);
    }
}
