
public class Application extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;
    
    public Application(String title, int width, int height) {
        super(title, width, height);
        setTestMode(false);
    }
    
    @Override
    public void addChild(GraphicalObject o) {
        super.addChild(o);
        if (o instanceof Group) {
            traverseAdd((Group) o);
        } else if (o instanceof Widget) {
            addWidget1((Widget) o);
        }
    }
    
    @Override
    public void removeChild(GraphicalObject o) {
        if (o instanceof Group) {
            traverseRemove((Group) o);
        } else if (o instanceof Widget) {
            removeWidget1((Widget) o);
        }
        super.removeChild(o);
    }
    
    private void traverseAdd(Group g) {
        for (GraphicalObject o : g.getChildren()) {
            if (o instanceof Group) {
                traverseAdd((Group) o);
            } else if (o instanceof Widget) {
                addWidget1((Widget) o);
            }
        }
    }
    
    private void traverseRemove(Group g) {
        for (GraphicalObject o : g.getChildren()) {
            if (o instanceof Group) {
                traverseRemove((Group) o);
            } else if (o instanceof Widget) {
                removeWidget1((Widget) o);
            }
        }
    }
    
    private void addWidget1(Widget widget) {
        for (Behavior b : widget.getBehaviors()) {
            addBehavior(b);
        }
        for (Constraint c : widget.getConstraints()) {
            c.addToSystem();
        }
    }
    private void removeWidget1(Widget widget) {
        for (Behavior b : widget.getBehaviors()) {
            removeBehavior(b);
        }
        // TODO: remove constraints
    }

//    public void addWidget(Widget widget) {
//        for (Behavior b : widget.getBehaviors()) {
//            addBehavior(b);
//        }
//        for (Constraint c : widget.getConstraints()) {
//            c.addToSystem();
//        }
//        if (!children.contains(widget)) {
//            addChild(widget);
//        }
//    }
//    
//    public void removeWidget(Widget widget) {
//        for (Behavior b : widget.getBehaviors()) {
//            removeBehavior(b);
//        }
//        // TODO: remove constraints
//        removeChild(widget);
//    }
}
