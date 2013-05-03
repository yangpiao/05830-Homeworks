import java.awt.*;
import java.util.List;


public class WidgetsNodeEdgeEditor extends Application {
    private static final long serialVersionUID = 1L;
    private static final String S_MOVE = "Move";
    private static final String S_NODE = "Node";
    private static final String S_EDGE = "Edge";
    
    private Color color = Color.orange;
    private int thickness = 2;
    private int nodeWidth = 15;
    private Font font = new Font("SansSerif", Font.BOLD, 14);
    private Group canvas;
    private Behavior canvasBehavior;
    private BehaviorEvent canvasStartEvent;
    private BehaviorEvent canvasStopEvent;
    private WSelectionHandles selection;
    
    public static void main(String[] args) {
        new WidgetsNodeEdgeEditor(args);
    }

    public WidgetsNodeEdgeEditor(String[] args) {
        super("Node-and-Edge Editor", 800, 500);
        try {
            addChild(new OutlineRect(9, 9, 782, 482, Color.black, 1));
            addChild(new OutlineRect(19, 19, 592, 462, Color.lightGray, 1));
            addTools();
            addCanvas();
            println("Node-and-Edge Editor");
        } catch (Exception e) {
            println("Exception: " + e);
        }
    }

    private void addCanvas() {
        canvas = new SimpleGroup(20, 20, 590, 460);
        addChild(canvas);
        canvasStartEvent = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        canvasStopEvent = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        canvasBehavior = new MoveNodeBehavior();
        canvasBehavior.setStartEvent(canvasStartEvent);
        canvasBehavior.setStopEvent(canvasStopEvent);
        canvasBehavior.setGroup(canvas);
        addBehavior(canvasBehavior);
    }
    
    private static final String S_ORANGE = "orange";
    private static final String S_RED = "red";
    private static final String S_BLUE = "blue";
    private static final String S_CYAN = "cyan";
    private static final String S_PINK = "pink";
    private static final String S_GRAY = "gray";
    private static final String S_GREEN = "green";
    private static final String S_MAGENTA = "magenta";
    
    private void addTools() throws Exception {
        WRadioboxPanel draw = new WRadioboxPanel(620, 30, 160, 100);
        draw.add("Move Node", S_MOVE);
        draw.add("Draw Node", S_NODE);
        draw.add("Draw Line", S_EDGE);
        draw.addCallback(new WRadioboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                setTool(userValue);
            }
        });
        
        Text t1 = new Text("Enable selection", 645, 130, font, Color.darkGray);
        Text t2 = new Text("(only for \"Move\")", 645, 150, font, Color.gray);
        WCheckboxPanel cb = new WCheckboxPanel(645, 160, 100, 30);
        cb.add("Enable", "enable");
        cb.addCallback(new WCheckboxPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                boolean enable = ((Selectable) o).isSelected();
                setEnableSelection(enable);
                // toggleSelection(enable);
                setTool(tool);
            }
        });
        
        WButtonPanel colors = new WButtonPanel(620, 215, 160, 80, 
                LayoutGroup.FILL, 10, true);
        colors.add(new FilledRect(0, 0, 32, 20, Color.orange), S_ORANGE);
        colors.add(new FilledRect(0, 0, 32, 20, Color.red), S_RED);
        colors.add(new FilledRect(0, 0, 32, 20, Color.blue), S_BLUE);
        colors.add(new FilledRect(0, 0, 32, 20, Color.cyan), S_CYAN);
        colors.add(new FilledRect(0, 0, 32, 20, Color.pink), S_PINK);
        colors.add(new FilledRect(0, 0, 32, 20, Color.gray), S_GRAY);
        colors.add(new FilledRect(0, 0, 32, 20, Color.green), S_GREEN);
        colors.add(new FilledRect(0, 0, 32, 20, Color.magenta), S_MAGENTA);
        colors.addCallback(new WButtonPanel.Callback() {
            public void onSelect(GraphicalObject o, String userValue) {
                setColor(userValue);
            }
        });
        
        txtThick = new Text("Thickness: " + thickness, 
                620, 320, font, Color.black);
        WNumberSlider slider1 = new WNumberSlider(620, 330, 160, 2, 20, 2);
        slider1.addCallback(new WNumberSlider.Callback() {
            public void onChange(double value) {
                setThickness((int) value);
            }
        });
        txtWidth = new Text("Node Width: " + nodeWidth, 
                620, 370, font, Color.black);
        WNumberSlider slider2 = new WNumberSlider(620, 380, 160, 15, 40, 5);
        slider2.addCallback(new WNumberSlider.Callback() {
            public void onChange(double value) {
                setNodeWidth((int) value);
            }
        });
        
        addChild(txtThick);
        addChild(txtWidth);
        addChild(t1);
        addChild(t2);
        addChild(draw);
        addChild(colors);
        addChild(slider1);
        addChild(slider2);
        addChild(cb);
    }
    
    private String tool = S_MOVE;
    private void setTool(String type) {
        // if (!tool.equals(type)) {
        removeBehavior(canvasBehavior);
        tool = type;
        switch (type) {
        case S_MOVE:
            if (enableSelection) {
                toggleSelection(true);
            } else {
                toggleSelection(false);
                canvasBehavior = new MoveNodeBehavior();
            }
            break;
        case S_NODE:
            toggleSelection(false);
            canvasBehavior = new NewNodeBehavior(color, nodeWidth);
            break;
        case S_EDGE:
            toggleSelection(false);
            canvasBehavior = new NewEdgeBehavior(color, thickness);
            break;
        }
        canvasBehavior.setStartEvent(canvasStartEvent);
        canvasBehavior.setStopEvent(canvasStopEvent);
        canvasBehavior.setGroup(canvas);
        addBehavior(canvasBehavior);
        // }
    }
    
    private String optionColor = S_ORANGE;
    private void setColor(String opt) {
        if (!optionColor.equals(opt)) {
            optionColor = opt;
            switch (opt) {
            case S_ORANGE:
                color = Color.orange;
                break;
            case S_RED:
                color = Color.red;
                break;
            case S_BLUE:
                color = Color.blue;
                break;
            case S_CYAN:
                color = Color.cyan;
                break;
            case S_PINK:
                color = Color.pink;
                break;
            case S_GRAY:
                color = Color.gray;
                break;
            case S_GREEN:
                color = Color.green;
                break;
            case S_MAGENTA:
                color = Color.magenta;
                break;
            }
            if (canvasBehavior instanceof NewNodeBehavior) {
                ((NewNodeBehavior) canvasBehavior).setColor(color);
            } else if (canvasBehavior instanceof NewEdgeBehavior) {
                ((NewEdgeBehavior) canvasBehavior).setColor(color);
            }
        }
    }
    
    private Text txtThick;
    private Text txtWidth;
    private void setThickness(int value) {
        if (thickness != value) {
            thickness = value;
            txtThick.setText("Thickness: " + thickness);
            if (canvasBehavior instanceof NewEdgeBehavior) {
                ((NewEdgeBehavior) canvasBehavior).setLineThickness(thickness);
            }
        }
    }
    private void setNodeWidth(int value) {
        if (thickness != value) {
            nodeWidth = value;
            txtWidth.setText("Node Width: " + nodeWidth);
            if (canvasBehavior instanceof NewNodeBehavior) {
                ((NewNodeBehavior) canvasBehavior).setSide(nodeWidth);
            }
        }
    }
    
    private boolean enableSelection = false;
    private void setEnableSelection(boolean enable) {
        enableSelection = enable;
    }
    private void toggleSelection(boolean enable) {
        if (selection == null) {
            selection = makeSelectionHandles();
        }
        if (enable && tool.equals(S_MOVE)) {
            removeBehavior(canvasBehavior);
            addChild(selection);
        } else if (!enable) {
            removeChild(selection);
        }
    }
    
    
    private WSelectionHandles makeSelectionHandles() {
        return new WSelectionHandles(canvas) {
            protected MoveBehavior createMoveBehavior() {
                return new MoveNodeBehavior() {
                    public void start(BehaviorEvent event) {
                        if (state == IDLE) {
                            int x = event.getX(), y = event.getY();
                            for (GraphicalObject o : group.getChildren()) {
                                if (o instanceof FilledRect && 
                                        ((Selectable) o).isSelected()) {
                                    Rectangle br = o.getBoundingBox();
                                    if (br.contains(x, y)) {
                                        target = o;
                                        initBounding.setLocation(br.x, br.y);
                                        initBounding.setSize(
                                                br.width, br.height);
                                        initCursor.setLocation(x, y);
                                        state = RUNNING_INSIDE;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                };
            }
            protected ChoiceBehavior createChoiceBehavior(boolean firstOnly) {
                return new ChoiceBehavior(0, firstOnly) {
                    public void start(BehaviorEvent event) {
                        if (state == IDLE) {
                            int x = event.getX(), y = event.getY();
                            List<GraphicalObject> children = getGroup().getChildren();
                            for (GraphicalObject o : children) {
                                Rectangle br = o.getBoundingBox();
                                if (br.contains(x, y)) {
                                    if (o instanceof FilledRect) {
                                        hovered = ((Selectable) o);
                                        if (!hovered.isSelected()) {
                                            hovered.setInterimSelected(true);
                                            state = RUNNING_INSIDE;
                                        }
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
        };
    }
}
