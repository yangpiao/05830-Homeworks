import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class NewEdgeBehavior extends NewBehavior {
    private Color color;
    private int lineThickness;
    private GraphicalObject startObj;
    private GraphicalObject endObj;
    private List<Edge> edges = new ArrayList<Edge>();
    
    public NewEdgeBehavior() {
        super();
        color = Color.black;
        lineThickness = 1;
    }
    
    public NewEdgeBehavior(Color color, int lineThickness) {
        super();
        this.color = color;
        this.lineThickness = lineThickness;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (!color.equals(this.color)) {
            this.color = color;
        }
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            this.lineThickness = lineThickness;
        }
    }
    
    @Override
    public void start(BehaviorEvent event) {
        if (state == IDLE) {
            int x = event.getX(), y = event.getY();
            List<GraphicalObject> children = group.getChildren();
            for (GraphicalObject o : children) {
                if (o instanceof FilledRect) {
                    Rectangle br = o.getBoundingBox();
                    if (br.contains(x, y)) {
                        startObj = o;
                        state = RUNNING_INSIDE;
                        target = make(x, y, x, y);
                        group.addChild(target);
                        group.bringChildToFront(o);
                        startPoint = new Point(x, y);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void stop(BehaviorEvent event) {
        if (state != IDLE) {
            int x = event.getX(), y = event.getY();
            List<GraphicalObject> children = group.getChildren();
            for (GraphicalObject o : children) {
                if (o instanceof FilledRect && o != startObj) {
                    Rectangle br = o.getBoundingBox();
                    Point p = group.parentToChild(new Point(x, y));
                    if (br.contains(p)) {
                        endObj = o;
                        if (edges.contains(new Edge(startObj, endObj))) {
                            cancel(event);
                            System.out.println("cancel");
                            return;
                        }
                        addConstraint();
                        group.bringChildToFront(o);
                        state = IDLE;
                        target = null;
                        startPoint = null;
                        return;
                    }
                }
            }
            cancel(event);
        }
    }

    @Override
    public void cancel(BehaviorEvent event) {
        if (state != IDLE && target != null) {
            group.removeChild(target);
            startObj = null;
            endObj = null;
            state = IDLE;
            target = null;
            startPoint = null;
        }
    }

    @Override
    public GraphicalObject make(int x1, int y1, int x2, int y2) {
        return new Line(x1, y1, x2, y2, color, lineThickness);
    }

    @Override
    public void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2) {
        Line line = (Line) gobj;
        line.setX2(x2);
        line.setY2(y2);
    }


    private void addConstraint() {
        if (startObj == null || endObj == null) return;
//        new Constraint().setTarget(target, "x1").addSource(startObj, "x")
//            .setFormula(new Constraint.Formula() {
//                public void evaluate(ConstraintVariable t, 
//                        List<ConstraintVariable> src) {
//                    if (src.size() < 1) return;
//                    t.setValue((int)src.get(0).getValue() + 10);
//                }
//            }).addToSystem();
//        new Constraint().setTarget(target, "y1").addSource(startObj, "y")
//            .setFormula(new Constraint.Formula() {
//                public void evaluate(ConstraintVariable t, 
//                        List<ConstraintVariable> src) {
//                    if (src.size() < 1) return;
//                    t.setValue((int)src.get(0).getValue() + 10);
//                }
//            }).addToSystem();
//        new Constraint().setTarget(target, "x2").addSource(endObj, "x")
//            .setFormula(new Constraint.Formula() {
//                public void evaluate(ConstraintVariable t, 
//                        List<ConstraintVariable> src) {
//                    if (src.size() < 1) return;
//                    t.setValue((int)src.get(0).getValue() + 10);
//                }
//            }).addToSystem();
//        new Constraint().setTarget(target, "y2").addSource(endObj, "y")
//            .setFormula(new Constraint.Formula() {
//                public void evaluate(ConstraintVariable t, 
//                        List<ConstraintVariable> src) {
//                    if (src.size() < 1) return;
//                    t.setValue((int)src.get(0).getValue() + 10);
//                }
//            }).addToSystem();
        new Constraint().setTarget(target, "x1")
            .addSource(startObj, "x").addSource(startObj, "y")
            .addSource(endObj, "x").addSource(endObj, "y")
            .setFormula(new Constraint.Formula() {
                public void evaluate(ConstraintVariable t, 
                        List<ConstraintVariable> src) {
                    if (src.size() < 4) return;
                    Line line = (Line) t.getObject();
                    FilledRect s = (FilledRect) src.get(0).getObject();
                    FilledRect e = (FilledRect) src.get(2).getObject();
                    // int offset = 7;
                    int startOffset = s.getWidth() / 2;
                    int endOffset = e.getWidth() / 2;
                    line.setX1((int)src.get(0).getValue() + startOffset);
                    line.setY1((int)src.get(1).getValue() + startOffset);
                    line.setX2((int)src.get(2).getValue() + endOffset);
                    line.setY2((int)src.get(3).getValue() + endOffset);
                }
            }).addToSystem();
    }
    
    protected class Edge {
        private GraphicalObject start;
        private GraphicalObject end;
        
        public Edge(GraphicalObject start, GraphicalObject end) {
            this.start = start;
            this.end = end;
        }
        
        public boolean equals(Edge e) {
            return (start == e.start && end == e.end) ||
                    (start == e.end && end == e.start);
        }
    }
}
