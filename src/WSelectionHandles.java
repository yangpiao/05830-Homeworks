import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class WSelectionHandles extends Widget {
    private Group target;
    private GraphicalObject value;
    
    public WSelectionHandles(Group g) {
        this(g, false);
    }
    
    public WSelectionHandles(Group g, boolean firstOnly) {
        super();
        target = g;
        container = new SimpleGroup();
        
        BehaviorEvent start = new BehaviorEvent(BehaviorEvent.MOUSE_MOVE_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        MoveBehavior mb = createMoveBehavior();
        mb.setStartEvent(start);
        mb.setStopEvent(stop);
        mb.setGroup(target);
        behaviors.add(mb);
        
        BehaviorEvent selStart = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent selStop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior sb = createChoiceBehavior(firstOnly);
        sb.setStartEvent(selStart);
        sb.setStopEvent(selStop);
        sb.addListener(new ChoiceBehavior.SelectListener() {
            @Override
            public void onSelect(GraphicalObject o) {
                change(o);
            }
        });
        sb.setGroup(target);
        behaviors.add(sb);
    }
    
    protected MoveBehavior createMoveBehavior() {
        return new MoveBehavior() {
            @Override
            public void start(BehaviorEvent event) {
                if (state == IDLE) {
                    int x = event.getX(), y = event.getY();
                    List<GraphicalObject> children = group.getChildren();
                    for (GraphicalObject o : children) {
                        Rectangle br = o.getBoundingBox();
                        if (br.contains(x, y) &&
                                ((Selectable) o).isSelected()) {
                            target = o;
                            initBounding.setLocation(br.x, br.y);
                            initBounding.setSize(br.width, br.height);
                            initCursor.setLocation(x, y);
                            state = RUNNING_INSIDE;
                            return;
                        }
                    }
                }
            }
        };
    }
    
    protected ChoiceBehavior createChoiceBehavior(boolean firstOnly) {
        return new ChoiceBehavior(0, firstOnly) {
            @Override
            public void start(BehaviorEvent event) {
                if (state == IDLE) {
                    int x = event.getX(), y = event.getY();
                    List<GraphicalObject> children = getGroup().getChildren();
                    for (GraphicalObject o : children) {
                        Rectangle br = o.getBoundingBox();
                        if (br.contains(x, y) && o instanceof Selectable) {
                            hovered = ((Selectable) o);
                            if (!hovered.isSelected()) {
                                hovered.setInterimSelected(true);
                                state = RUNNING_INSIDE;
                            }
                            return;
                        }
                    }
                    state = RUNNING_OUTSIDE;
                    select(null);
                }
            }
        };
    }
    
    public Group getTarget() {
        return target;
    }
    public GraphicalObject getValue() {
        return value;
    }
    
    private void change(GraphicalObject o) {
        if (value != o) {
            value = o;
            executeCallbacks(o);
        }
    }
    
    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        // selection handles do not need to draw anything
    }
    
    public interface Callback {
        public void onChange(GraphicalObject value);
    }
    protected List<Callback> callbacks = new ArrayList<Callback>();
    protected void executeCallbacks(GraphicalObject value) {
        for (Callback cb : callbacks) {
            cb.onChange(value);
        }
    }
    public void addCallback(Callback cb) {
        if (cb != null && !callbacks.contains(cb)) {
            callbacks.add(cb);
        }
    }
    public void removeCallback(Callback cb) {
        if (cb == null) {
            callbacks.clear();
        } else {
            callbacks.remove(cb);
        }
    }
}
