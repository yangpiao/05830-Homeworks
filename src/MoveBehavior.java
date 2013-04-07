import java.util.List;
import java.awt.*;


public class MoveBehavior implements Behavior {
    protected Group group;
    protected int state = IDLE;
    protected BehaviorEvent startEvent;
    protected BehaviorEvent stopEvent;
    
    protected GraphicalObject target = null;
    protected Rectangle initBounding = new Rectangle(0, 0, 0, 0);
    protected Point initCursor = new Point(0, 0);
    
    protected int gridSize;
    
    public MoveBehavior() {
        gridSize = 0;
    }
    
    public MoveBehavior(int gridSize) {
        this.gridSize = (gridSize >= 0) ? gridSize : 0;
    }
    
    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
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
    public int getState() {
        return state;
    }

    @Override
    public BehaviorEvent getStartEvent() {
        return startEvent;
    }

    @Override
    public void setStartEvent(BehaviorEvent mask) {
        startEvent = mask;
    }

    @Override
    public BehaviorEvent getStopEvent() {
        return stopEvent;
    }

    @Override
    public void setStopEvent(BehaviorEvent mask) {
        stopEvent = mask;
    }

    @Override
    public void start(BehaviorEvent event) {
        if (state == IDLE) {
            int x = event.getX(), y = event.getY();
            List<GraphicalObject> children = group.getChildren();
            for (GraphicalObject o : children) {
                Rectangle br = o.getBoundingBox();
                // Point p = group.parentToChild(new Point(x, y));
                // if (br.contains(p)) {
                if (br.contains(x, y)) {
                    target = o;
                    initBounding.setLocation(br.x, br.y);
                    initBounding.setSize(br.width, br.height);
                    // initCursor.setLocation(p);
                    initCursor.setLocation(x, y);
                    state = RUNNING_INSIDE;
                    // System.out.println("start");
                    return;
                }
            }
        }
    }

    @Override
    public void running(BehaviorEvent event) {
        if (state != IDLE) {
            if (target == null) return;
            int x = event.getX(), y = event.getY();
            if (group.contains(x, y)) {
                Point p = group.parentToChild(new Point(x, y));
                state = RUNNING_INSIDE;
                int cx = initCursor.x, cy = initCursor.y;
                int x0 = initBounding.x, y0 = initBounding.y;
                if (gridSize == 0) {
                    target.moveTo(x0 + p.x - cx, y0 + p.y - cy);
                } else if (Math.abs(p.x - cx) > gridSize || 
                        Math.abs(p.y - cy) > gridSize) {
                    int gx = (p.x - cx) / gridSize;
                    int gy = (p.y - cy) / gridSize;
                    target.moveTo(x0 + gx * gridSize, y0 + gy * gridSize);
                }
                
            } else {
                state = RUNNING_OUTSIDE;
            }
            // System.out.println("running");
        }
    }

    @Override
    public void stop(BehaviorEvent event) {
        if (state != IDLE) {
            state = IDLE;
        }
        if (target != null) {
            target = null;
        }
        // System.out.println("stop");
    }

    @Override
    public void cancel(BehaviorEvent event) {
        if (state != IDLE) {
            state = IDLE;
        }
        if (target != null) {
            target.moveTo(initBounding.x, initBounding.y);
            target = null;
        }
        // System.out.println("cancel");
    }

}
