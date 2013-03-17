public class BehaviorEvent {
    private int id;
    private int modifiers;
    private int key;
    private int x;
    private int y;

    public BehaviorEvent(int id, int modifiers, int key, int x, int y) {
        this.id = id;
        if (modifiers == 0) {
            this.modifiers = BehaviorEvent.NO_MODIFIER;
        } else {
            this.modifiers = modifiers;
        }
        this.key = key;
        this.x = x;
        this.y = y;
    }

    public int getID() {
        return id;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getKey() {
        return key;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean matches(BehaviorEvent event) {
        return 
            event.id == id
            && ((modifiers & event.modifiers) == modifiers) //sometimes there are some out of range
            && event.key == key;
    }

    public final static int KEY_DOWN_ID = 0;
    public final static int KEY_UP_ID = 1;
    public final static int MOUSE_DOWN_ID = 2;
    public final static int MOUSE_UP_ID = 3;
    public final static int MOUSE_MOVE_ID = 4;
    public final static int SCROLLWHEEL_ID = 5;

    public final static int SHIFT_MODIFIER = 0x1;
    public final static int CONTROL_MODIFIER = 0x2;
    public final static int ALT_MODIFIER = 0x4;
    public final static int WINDOWS_KEY_MODIFIER = 0x8;
    public final static int FUNCTION_KEY_MODIFIER = 0x10;
    public final static int COMMAND_KEY_MODIFIER = 0x20;
    public final static int NO_MODIFIER = 0x10000;
    
    public final static int LEFT_MOUSE_KEY = 10000;
    public final static int MIDDLE_MOUSE_KEY = 10001;
    public final static int RIGHT_MOUSE_KEY = 10002;
    public final static int SCROLLWHEEL_UP_KEY = 10003;
    public final static int SCROLLWHEEL_DOWN_KEY = 10004;

}
