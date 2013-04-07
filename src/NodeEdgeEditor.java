import java.awt.*;

// TODO: [NEED FIX] sometimes there are redraw problems

public class NodeEdgeEditor extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;
    private static final String S_MOVE = " Move ";
    private static final String S_NODE = " Node ";
    private static final String S_EDGE = " Edge ";
    
    private Group canvas;
    private Color color = Color.darkGray;
    private int thickness = 2;
    private Font font = new Font("SansSerif", Font.PLAIN, 18);
    
    private Behavior canvasBehavior;
    private BehaviorEvent canvasStartEvent;
    private BehaviorEvent canvasStopEvent;

    
    public static void main(String[] args) {
        new NodeEdgeEditor(args);
    }
    
    public NodeEdgeEditor(String[] args) {
        super("Node-and-Edge Editor", 700, 500);
        try {
            addChild(new OutlineRect(9, 9, 682, 482, Color.black, 1));
            addChild(new OutlineRect(19, 19, 592, 462, Color.lightGray, 1));
            SimpleGroup g = new SimpleGroup(10, 10, 690, 490);
            addChild(g);
            createTools(g);
            createCanvas(g);
            println("Node-and-Edge Editor");
        } catch (Exception e) {
            // System.out.println(e);
        }
    }

    
    private void createCanvas(Group g) {
        canvas = new SimpleGroup(10, 10, 590, 460);
        g.addChild(canvas);
        
        canvasStartEvent = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
//        canvasStartEvent = new BehaviorEvent(BehaviorEvent.MOUSE_MOVE_ID, 
//                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        canvasStopEvent = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        canvasBehavior = new MoveNodeBehavior();
        canvasBehavior.setStartEvent(canvasStartEvent);
        canvasBehavior.setStopEvent(canvasStopEvent);
        canvasBehavior.setGroup(canvas);
        addBehavior(canvasBehavior);
    }

    private void createTools(Group g) {
        LayoutGroup tools = new LayoutGroup(605, 20, 70, 250,
                LayoutGroup.VERTICAL, 10);
        
        Text move = new Text(S_MOVE, 0, 0, font, Color.black);
        Text line = new Text(S_NODE, 0, 0, font, Color.black);
        Text rect = new Text(S_EDGE, 0, 0, font, Color.black);
        tools.addChild(move);
        tools.addChild(line);
        tools.addChild(rect);
        g.addChild(tools);
        
        BehaviorEvent toolsStart = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent toolsStop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior toolsSelect = new ChoiceBehavior();
        toolsSelect.setStartEvent(toolsStart);
        toolsSelect.setStopEvent(toolsStop);
        toolsSelect.setGroup(tools);
        toolsSelect.addListener(new ChoiceBehavior.SelectListener() {
            public void onSelect(GraphicalObject o) {
                Text to = (Text) o;
                String text = to.getText();
                switch (text) {
                case S_MOVE:
                    setCurrentTool(0);
                    break;
                case S_NODE:
                    setCurrentTool(1);
                    break;
                case S_EDGE:
                    setCurrentTool(2);
                    break;
                }
            }
        });
        addBehavior(toolsSelect);
    }

    private int tool = 0;
    private void setCurrentTool(int type) {
        if (tool != type) {
            removeBehavior(canvasBehavior);
            tool = type;
            switch (type) {
            case 0:
                canvasBehavior = new MoveNodeBehavior();
                break;
            case 1:
                canvasBehavior = new NewNodeBehavior(color, 15);
                break;
            case 2:
                canvasBehavior = new NewEdgeBehavior(color, thickness);
                break;
            }
            canvasBehavior.setStartEvent(canvasStartEvent);
            canvasBehavior.setStopEvent(canvasStopEvent);
            canvasBehavior.setGroup(canvas);
            addBehavior(canvasBehavior);
        }
    }
}
