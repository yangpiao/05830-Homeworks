import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public abstract class WButtonPanelBase extends Widget {
    protected boolean finalFeedback;
    protected int selectionType;
    protected boolean firstOnly;
    protected ChoiceBehavior selBehavior;
    protected GraphicalObject value;
    protected Hashtable<Integer, String> userValues;
    
    public static final Font defaultFont = new Font("SansSerif", Font.BOLD, 16);
    public static final Color defaultColor = new Color(230, 230, 230);
    
    public WButtonPanelBase() {
        this(0, 0, 0, 0, 0, 0, 0, 0, false, 0, false);
    }
    
    public WButtonPanelBase(int x, int y, int width, int height, 
            int layout, int offset, int rows, int cols,
            boolean finalFeedback, int selectionType, boolean firstOnly) {
        super();
        userValues = new Hashtable<Integer, String>();
        container = new LayoutGroup(x, y, width, height, 
                layout, offset, rows, cols);
        
        this.finalFeedback = finalFeedback;
        if (finalFeedback) {
            this.selectionType = selectionType;
        } else {
            this.selectionType = ChoiceBehavior.SINGLE;
        }
        this.firstOnly = firstOnly;
        
        BehaviorEvent start = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior sel = new ChoiceBehavior(selectionType, firstOnly) {
            @Override
            public void start(BehaviorEvent event) {
                if (state == IDLE) {
                    int x = event.getX(), y = event.getY();
                    List<GraphicalObject> children = getGroup().getChildren();
                    for (GraphicalObject o : children) {
                        Rectangle br = o.getBoundingBox();
                        if (br.contains(x, y) && o instanceof Selectable) {
                            hovered = ((Selectable) o);
                            hovered.setInterimSelected(true);
                            state = RUNNING_INSIDE;
                            return;
                        }
                    }
                }
            }
        };
        sel.setStartEvent(start);
        sel.setStopEvent(stop);
        sel.addListener(new ChoiceBehavior.SelectListener() {
            @Override
            public void onSelect(GraphicalObject o) {
                select(o);
            }
        });
        sel.setGroup(container);
        behaviors.add(sel);
        selBehavior = sel;
    }
    
//    protected void setContainer(Group g) {
//        container = g;
//        selBehavior.setGroup(g);
//    }
    
    public GraphicalObject getValue() {
        return value;
    }
    
    public String getUserValue(GraphicalObject o) {
        if (o != null) {
            return userValues.get(o.hashCode());
        } else {
            return null;
        }
    }
    
    public void setUserValue(GraphicalObject o, String value) {
        if (o == null) return;
        userValues.put(o.hashCode(), value);
    }
    
    public boolean isFinalFeedback() {
        return finalFeedback;
    }
    public void setFinalFeedback(boolean finalFeedback) {
        if (this.finalFeedback != finalFeedback) {
            this.finalFeedback = finalFeedback;
        }
    }
    public int getSelectionType() {
        return selectionType;
    }
    public void setSelectionType(int selectionType) {
        if (this.selectionType != selectionType) {
            this.selectionType = selectionType;
            selBehavior.setType(selectionType);
        }
    }
    
    protected abstract void select(GraphicalObject o);
    public abstract void add(String label);
    public abstract void add(GraphicalObject label);
    public abstract void add(String label, String userValue);
    public abstract void add(GraphicalObject label, String userValue);
    
    public interface Callback {
        public void onSelect(GraphicalObject o, String userValue);
    }
    protected List<Callback> callbacks = new ArrayList<Callback>();
    protected void executeCallbacks(GraphicalObject o) {
        for (Callback cb : callbacks) {
            if (o != null) {
                cb.onSelect(o, userValues.get(o.hashCode()));
            } else {
                cb.onSelect(null, null);
            }
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
