import java.awt.*;


public abstract class NewBehavior implements Behavior {
    public abstract GraphicalObject make(int x1, int y1, int x2, int y2);
    public abstract void resize(GraphicalObject gobj, 
            int x1, int y1, int x2, int y2);
    
    protected boolean onePoint;
    protected Group group;
    protected int state = IDLE;
    protected BehaviorEvent startEvent;
    protected BehaviorEvent stopEvent;
    protected GraphicalObject target;
    protected Point startPoint;
    
    public NewBehavior() {
        this.onePoint = false;
    }
    public NewBehavior(boolean onePoint) {
        this.onePoint = onePoint;
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
            // Point p = group.parentToChild(new Point(x, y));
            Point p = new Point(x, y);
            state = RUNNING_INSIDE;
            target = make(p.x, p.y, p.x, p.y);
            startPoint = p;
            group.addChild(target);
        }
    }

    @Override
    public void running(BehaviorEvent event) {
        if (state != IDLE && target != null) {
            int x = event.getX(), y = event.getY();
            if (group.contains(x, y)) {
                Point p = group.parentToChild(new Point(x, y));
                if (!onePoint) {
                    resize(target, startPoint.x, startPoint.y, p.x, p.y);
                } else {
                    resize(target, p.x, p.y, p.x, p.y);
                }
                state = RUNNING_INSIDE;
            } else {
                state = RUNNING_OUTSIDE;
            }
        }
    }

    @Override
    public void stop(BehaviorEvent event) {
        if (state != IDLE) {
            state = IDLE;
            target = null;
            startPoint = null;
        }
    }

    @Override
    public void cancel(BehaviorEvent event) {
        if (state != IDLE && target != null) {
            group.removeChild(target);
            state = IDLE;
            target = null;
            startPoint = null;
        }
    }

}
