import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;


public class ChoiceBehavior implements Behavior {
    public static final int SINGLE = 0;
    public static final int TOGGLE = 1;
    public static final int MULTIPLE = 2;
    
    private Group group;
    private int state = IDLE;
    private BehaviorEvent startEvent;
    private BehaviorEvent stopEvent;
    private ArrayList<GraphicalObject> targets;
    private Selectable hovered = null;
    private int type;
    private boolean firstOnly;
    
    private void select(GraphicalObject obj) {
        switch (type) {
        case SINGLE:
            for (GraphicalObject t : targets) {
                Selectable s = ((Selectable) t);
                s.setInterimSelected(false);
                s.setSelected(false);
            }
            targets.clear();
            if (obj != null) {
                Selectable s = ((Selectable) obj);
                s.setInterimSelected(false);
                s.setSelected(true);
                targets.add(obj);
                execListeners(obj);
            }
            break;
        case TOGGLE:
            for (GraphicalObject t : targets) {
                Selectable s = ((Selectable) t);
                s.setInterimSelected(false);
                if (t != obj) {
                    s.setSelected(false);
                }
            }
            targets.clear();
            if (obj != null) {
                Selectable s = ((Selectable) obj);
                s.setInterimSelected(false);
                if (s.isSelected()) {
                    s.setSelected(false);
                } else {
                    s.setSelected(true);
                    targets.add(obj);
                    execListeners(obj);
                }
            }
            break;
        case MULTIPLE:
            if (obj != null) {
                Selectable s = ((Selectable) obj);
                if (targets.contains(obj)) {
                    s.setInterimSelected(false);
                    s.setSelected(false);
                    targets.remove(obj);
                } else {
                    s.setInterimSelected(false);
                    s.setSelected(true);
                    targets.add(obj);
                    execListeners(obj);
                }
            }
            break;
        }
    }
    
    public ChoiceBehavior() {
        this(SINGLE, false);
    }
    
    public ChoiceBehavior(int type, boolean firstOnly) {
        if (type != SINGLE && type != TOGGLE && type != MULTIPLE) {
            throw new RuntimeException("Invalid type");
        }
        targets = new ArrayList<GraphicalObject>();
        this.type = type;
        this.firstOnly = firstOnly;
    }
    
    public List<GraphicalObject> getSelection() {
        return null;
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
               if (br.contains(x, y) && o instanceof Selectable) {
                   hovered = ((Selectable) o);
                   hovered.setInterimSelected(true);
                   state = RUNNING_INSIDE;
                   // System.out.println("choice: start");
                   return;
               }
           }
           select(null);
       }
    }

    @Override
    public void running(BehaviorEvent event) {
        if (state != IDLE) {
            int x = event.getX(), y = event.getY();
            if (group.contains(x, y)) {
                Point p = group.parentToChild(new Point(x, y));
                List<GraphicalObject> children = group.getChildren();
                for (GraphicalObject o : children) {
                    Rectangle br = o.getBoundingBox();
                    if (br.contains(p) && o instanceof Selectable) {
                        if (!firstOnly) {
                            hovered = ((Selectable) o);
                            hovered.setInterimSelected(true);
                            state = RUNNING_INSIDE;
                        }
                        return;
                    }
                }
                if (hovered != null) {
                    hovered.setInterimSelected(false);
                    hovered = null;
                    state = RUNNING_OUTSIDE;
                }
            } else {
                if (hovered != null) {
                    hovered.setInterimSelected(false);
                    hovered = null;
                    state = RUNNING_OUTSIDE;
                }
            }
        }
    }

    @Override
    public void stop(BehaviorEvent event) {
        if (state != IDLE) {
            state = IDLE;
            int x = event.getX(), y = event.getY();
            Point p = group.parentToChild(new Point(x, y));
            List<GraphicalObject> children = group.getChildren();
            for (GraphicalObject o : children) {
                Rectangle br = o.getBoundingBox();
                if (br.contains(p) && o instanceof Selectable) {
                    if ((firstOnly && hovered == o) || !firstOnly) {
                        select(o);
                    }
                    break;
                }
            }
        }
        // System.out.println("choice: stop");
    }

    @Override
    public void cancel(BehaviorEvent event) {
        if (state != IDLE) {
            state = IDLE;
            if (hovered != null) {
                hovered.setInterimSelected(false);
                hovered = null;
            }
        }
        // System.out.println("choice: cancel");
    }
    
    public interface SelectListener {
        void onSelect(GraphicalObject o);
    }
    
    private ArrayList<SelectListener> listeners = 
            new ArrayList<SelectListener>();
    
    private void execListeners(GraphicalObject o) {
        for (SelectListener l : listeners) {
            l.onSelect(o);
        }
    }
    
    public void addListener(SelectListener l) {
        if (l != null && !listeners.contains(l)) {
            listeners.add(l);
        }
    }
    
    public void removeListener(SelectListener l) {
        if (l == null) {
            listeners.clear();
        } else if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

}
