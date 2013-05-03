import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;


public class WNumberSlider extends Widget {
    // public static final int HORIZONTAL = 1;
    // public static final int VERTICAL = 2;
    public static final String leftArrowImageName = "arrow_left.png";
    public static final String rightArrowImageName = "arrow_right.png";

    private static final int lineWidth = 4;
    private static final Color barColor = new Color(74, 74, 74);
    private static final Color indColor = new Color(90, 90, 90);
    private SimpleGroup bar;
    private FilledEllipse indicator;
    private Arrow leftArrow;
    private Arrow rightArrow;
    
    private static final double defaultMin = 0.0;
    private static final double defaultMax = 100.0;
    private static final double defaultStep = 10.0;
    private double min;
    private double max;
    private double step;
    
    private int currStep;
    private double value;
    private int grid;
    
    public WNumberSlider() throws IOException {
        this(0, 0, 0, 0, defaultMin, defaultMax, defaultStep);
    }
    public WNumberSlider(int x, int y, int length) throws IOException {
        this(x, y, length, 0, defaultMin, defaultMax, defaultStep);
    }
    public WNumberSlider(int x, int y, int length,
            double min, double max, double step) throws IOException {
        this(x, y, length, 0, min, max, step);
    }
    
    // TODO: *layout* is not implemented yet
    public WNumberSlider(int x, int y, int length, int layout,
            double min, double max, double step) throws IOException {
        super();
        if (max == min) {
            throw new RuntimeException("Max & Min cannot be same");
        }
        if (max > min && step < 0 || max < min && step > 0 || step == 0) {
            throw new RuntimeException("Step is not set properly");
        }
        this.min = min;
        this.max = max;
        this.step = step;
        value = min;
        currStep = 0;
        
        Image imageL = ImageIO.read(new File(leftArrowImageName));
        Image imageR = ImageIO.read(new File(rightArrowImageName));
        leftArrow = new Arrow(imageL, 0, 0);
        rightArrow = new Arrow(imageR, 0, 0);
        Rectangle r = leftArrow.getBoundingBox();
        int height = r.height;
        int barWidth = length - 2 * r.width - 2 * 5;
        bar = new SimpleGroup(0, 0, barWidth, height);
        bar.addChild(new FilledRect(0, (height - lineWidth) / 2,
                barWidth, lineWidth, barColor));
        indicator = new FilledEllipse(0, 0, height, height, indColor);
        bar.addChild(indicator);
        
        container = new LayoutGroup(x, y, length, r.height, 
                LayoutGroup.HORIZONTAL, 5);
        container.addChild(leftArrow);
        container.addChild(bar);
        container.addChild(rightArrow);
        
        BehaviorEvent start = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                0, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        grid = (int) Math.round((barWidth - height) / (max - min) * step);
        MoveBehavior mb = new MoveBehavior(grid) {
            @Override
            public void running(BehaviorEvent event) {
                if (state != IDLE) {
                    if (target == null) return;
                    int x = event.getX(), y = event.getY();
                    Point p = group.parentToChild(new Point(x, y));
                    Rectangle r = group.getBoundingBox();
                    int xp = p.x; //, yp = p.y;
                    int xc0 = initCursor.x;
                    int x0 = initBounding.x, y0 = initBounding.y;
                    if (group.contains(x, y)) {
                        state = RUNNING_INSIDE;
                    } else {
                        state = RUNNING_OUTSIDE;
                        if (xp < 0) xp = 0;
                        if (xp > r.width) xp = r.width;
                    }
                    if (Math.abs(xp - xc0) > gridSize) {
                        int gx = (xp - xc0) / gridSize;
                        target.moveTo(x0 + gx * gridSize, y0);
                        change(x0 / gridSize + gx);
                    }
                }
            }
        };
        // MoveBehavior mb = new MoveBehavior(grid);
        mb.setStartEvent(start);
        mb.setStopEvent(stop);
        mb.setGroup(bar);
        
        BehaviorEvent selStart = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent selStop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior sb = new ChoiceBehavior(ChoiceBehavior.SINGLE, true);
        sb.setStartEvent(selStart);
        sb.setStopEvent(selStop);
        sb.addListener(new ChoiceBehavior.SelectListener() {
            @Override
            public void onSelect(GraphicalObject o) {
                select(o);
            }
        });
        sb.setGroup(container);
        
        behaviors.add(mb);
        behaviors.add(sb);
    }
    
    private void change(int n) {
        if (currStep != n) {
            currStep = n;
            value = min + step * n;
            executeCallbacks(value);
        }
    }
    
    private void select(GraphicalObject o) {
        if (o == leftArrow) {
            goLeft();
        } else if (o == rightArrow) {
            goRight();
        }
        ((Selectable) o).setSelected(false);
    }
    
    private void goLeft() {
        if (currStep - 1 >= 0) {
            change(currStep - 1);
            // int width = bar.getWidth();
            int x = indicator.getX() - grid;
            if (x < 0) x = 0;
            indicator.setX(x);
        }
    }
    private void goRight() {
        if ((min < max && min + step * (currStep + 1) <= max) ||
                (min > max && min + step * (currStep + 1) >= max)) {
            change(currStep + 1);
            int width = bar.getWidth();
            int x = indicator.getX() + grid;
            if (x > width) x = width;
            indicator.setX(x);
        }
    }

    public double getMin() {
        return min;
    }
    public void setMin(double min) {
        this.min = min;
    }
    public double getMax() {
        return max;
    }
    public void setMax(double max) {
        this.max = max;
    }
    public double getStep() {
        return step;
    }
    public void setStep(double step) {
        this.step = step;
    }
    public double getValue() {
        return value;
    }
    
    public interface Callback {
        public void onChange(double value);
    }
    protected List<Callback> callbacks = new ArrayList<Callback>();
    protected void executeCallbacks(double value) {
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
