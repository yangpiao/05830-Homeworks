import java.awt.*;
import java.util.List;


public class MoveNodeBehavior extends MoveBehavior {
    @Override
    public void start(BehaviorEvent event) {
        if (state == IDLE) {
            int x = event.getX(), y = event.getY();
            List<GraphicalObject> children = group.getChildren();
            for (GraphicalObject o : children) {
                if (o instanceof FilledRect) {
                    Rectangle br = o.getBoundingBox();
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
    }
}
