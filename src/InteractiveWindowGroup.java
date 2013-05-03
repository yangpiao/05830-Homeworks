import java.awt.*;
import java.awt.event.*;
import java.util.*;
// import java.util.List;


public class InteractiveWindowGroup extends WindowGroup {
    private static final long serialVersionUID = 1L;
    private ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
    private Behavior current = null;
    
    private boolean testMode = true;
    public boolean getTestMode() {
        return testMode;
    }
    public void setTestMode(boolean t) {
        testMode = t;
    }
    
    private BehaviorEvent wrapEvent(InputEvent e) {
        int id = e.getID(), bid = -1, modifiers = 0, key = 0, x = 0, y = 0;
        int type = 0;
        
        switch (id) {
        case KeyEvent.KEY_PRESSED:
            bid = BehaviorEvent.KEY_DOWN_ID;
            type = 1;
            break;
        case KeyEvent.KEY_RELEASED:
            bid = BehaviorEvent.KEY_UP_ID;
            type = 1;
            break;
        case MouseEvent.MOUSE_PRESSED:
            bid = BehaviorEvent.MOUSE_DOWN_ID;
            type = 2;
            break;
        case MouseEvent.MOUSE_RELEASED:
            bid = BehaviorEvent.MOUSE_UP_ID;
            type = 2;
            break;
        case MouseEvent.MOUSE_MOVED:
        case MouseEvent.MOUSE_DRAGGED:
            bid = BehaviorEvent.MOUSE_MOVE_ID;
            type = 2;
            break;
        case MouseEvent.MOUSE_WHEEL:
            bid = BehaviorEvent.SCROLLWHEEL_ID;
            type = 2;
            break;
        // case KeyEvent.KEY_TYPED:
        // case MouseEvent.MOUSE_CLICKED:
        // case MouseEvent.MOUSE_ENTERED:
        // case MouseEvent.MOUSE_EXITED:
        }
        
        int mdfx = e.getModifiersEx();
        if ((mdfx & InputEvent.SHIFT_DOWN_MASK) > 0) { // shift
            modifiers |= BehaviorEvent.SHIFT_MODIFIER;
        } else if ((mdfx & InputEvent.CTRL_DOWN_MASK) > 0) { // control
            modifiers |= BehaviorEvent.CONTROL_MODIFIER;
        } else if ((mdfx & InputEvent.META_DOWN_MASK) > 0) { // command on mac
            modifiers |= BehaviorEvent.COMMAND_KEY_MODIFIER;
            // TODO: Win key on Windows
            modifiers |= BehaviorEvent.WINDOWS_KEY_MODIFIER;
        } else if ((mdfx & InputEvent.ALT_DOWN_MASK) > 0) { // option/alt
            modifiers |= BehaviorEvent.ALT_MODIFIER;
        }
        if (modifiers == 0) modifiers = BehaviorEvent.NO_MODIFIER;
        // TODO: BehaviorEvent.FUNCTION_KEY_MODIFIER ?
        // BUTTON1_DOWN_MASK: 1024, mouse left button
        // BUTTON2_DOWN_MASK: 2048, mouse middle button ?
        // BUTTON3_DOWN_MASK: 4096, mouse right button
        
        if (type == 1) { // keyboard event
            KeyEvent ke = (KeyEvent) e;
            key = ke.getKeyCode();
            
        } else if (type == 2) { // mouse event
            MouseEvent me = (MouseEvent) e;
            Insets insets = this.getInsets();
            x = me.getX() - insets.left;
            y = me.getY() - insets.top - 1;
            int btn = me.getButton();
            // println(x + ", " + y);
            switch (btn) {
            case MouseEvent.BUTTON1:
                key = BehaviorEvent.LEFT_MOUSE_KEY;
                break;
            case MouseEvent.BUTTON2:
                key = BehaviorEvent.MIDDLE_MOUSE_KEY;
                break;
            case MouseEvent.BUTTON3:
                key = BehaviorEvent.RIGHT_MOUSE_KEY;
                break;
            }
            
        } else {
            println("the event type is not supported");
        }
        
        return new BehaviorEvent(bid, modifiers, key, x, y);
    }
    
    private Point transformCoordinates(Point p, Group g) {
        g = g.getGroup();
        while (g != null && g != this) {
            p = g.parentToChild(p);
            g = g.getGroup();
        }
        return p;
    }
    
    // Assumption: 1 Behavior running at a time
    private void dispatchToBehaviors(InputEvent e) {
        BehaviorEvent be = wrapEvent(e);
        if (current != null) {
            Point p = transformCoordinates(new Point(be.getX(), be.getY()), 
                    current.getGroup());
            BehaviorEvent ev = new BehaviorEvent(be.getID(), be.getModifiers(),
                    be.getModifiers(), p.x, p.y);
            if (be.getKey() == KeyEvent.VK_ESCAPE) {
                current.cancel(ev);
                current = null;
                return;
            }
            if (be.matches(current.getStopEvent())) {
                current.stop(ev);
                if (current.getState() != Behavior.IDLE) {
                    println("[error] behavior didn't stop");
                }
                current = null;
            } else if (current.getState() != Behavior.IDLE) {
                current.running(ev);
            }
        } else {
            for (Behavior b : behaviors) {
                if (be.matches(b.getStartEvent())) {
                    // println(b.getStartEvent().getModifiers());
                    // check if the event occurs inside one of the objects
                    // in the group
                    Group g = b.getGroup();
                    // Rectangle r = g.getBoundingBox();
                    Point p = transformCoordinates(
                            new Point(be.getX(), be.getY()), g);
                    // println(r);
                    // println(p);
                    
//                    List<GraphicalObject> children = g.getChildren();
//                    for (GraphicalObject o : children) {
//                        Rectangle br = o.getBoundingBox();
//                        Point pc = g.parentToChild(p);
//                        if (br.contains(pc)) {
//                            current = b;
//                            b.start(new BehaviorEvent(be.getID(), 
//                                    be.getModifiers(), be.getKey(),
//                                    pc.x, pc.y));
//                        }
//                    }
                    
                    // if (r.contains(p)) {
                    if (g.contains(p.x, p.y)) {
                        Point pc = g.parentToChild(p);
                        b.start(new BehaviorEvent(be.getID(), be.getModifiers(),
                                be.getKey(), pc.x, pc.y));
                        if (b.getState() != Behavior.IDLE) {
                            current = b;
                        }
                        return;
                    }
                }
            }
            // if the event does not match any behavior, and the event is 
            // mouse-down, call unpause()
            if (testMode && be.getID() == BehaviorEvent.MOUSE_DOWN_ID) {
                unpause();
            }
        }
    }
    
    private class CustomKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() != KeyEvent.KEY_TYPED) {
                dispatchToBehaviors(e);
            }
            return false;
        }
    }
    

    public InteractiveWindowGroup() {
        this("", 0, 0);
    }
    
    public InteractiveWindowGroup(String title, int width, int height) {
        super(title, width, height);
        
        // handles keyboard events
        /*
        KeyListener keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                println("key code: " + keyCode + " (" +
                        KeyEvent.getKeyText(keyCode) + ")");
            }
            public void keyReleased(KeyEvent e) {
                println("key released");
            }
            public void keyTyped(KeyEvent e) {
                println("key typed");
            }
        };
        addKeyListener(keyListener);
        */
        KeyboardFocusManager manager = 
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomKeyDispatcher());
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dispatchToBehaviors(e);
            }
            public void mouseReleased(MouseEvent e) {
                dispatchToBehaviors(e);
            }
            public void mouseDragged(MouseEvent e) {
                dispatchToBehaviors(e);
            }
            public void mouseMoved(MouseEvent e) {
                dispatchToBehaviors(e);
            }
            public void mouseWheelMoved(MouseWheelEvent e) {
                dispatchToBehaviors(e);
            }
//            public void mouseClicked(MouseEvent e) {
//                // dispatchToBehaviors(e);
//            }
//            public void mouseEntered(MouseEvent e) {
//                dispatchToBehaviors(e);
//            }
//            public void mouseExited(MouseEvent e) {
//                dispatchToBehaviors(e);
//            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener((MouseMotionListener) mouseListener);
        addMouseWheelListener((MouseWheelListener) mouseListener);
    }

    public void addBehavior(Behavior inter)
            throws AlreadyHasGroupRunTimeException {
        // if (inter.getGroup() != null) {
        //     throw new AlreadyHasGroupRunTimeException(
        //             "Behavior has already been added to another group.");
        // }
        // inter.setGroup(this);
        behaviors.add(inter);
    }

    public void removeBehavior(Behavior inter) {
        // if (inter.getGroup() != this) return;
        // inter.setGroup(null);
        if (!behaviors.contains(inter)) return;
        behaviors.remove(inter);
    }
    
    public Behavior[] getBehaviors() {
        return (Behavior[]) behaviors.toArray();
    }
}
