import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrawingEditor extends InteractiveWindowGroup {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new DrawingEditor(args);
    }
    
    private Group canvas;
    private Behavior canvasBehavior;
    private BehaviorEvent canvasStartEvent;
    private BehaviorEvent canvasStopEvent;
    
    private Color color = Color.orange;
    private int thickness = 2;
    private Font font = new Font("SansSerif", Font.PLAIN, 18);
    
    JFileChooser filePicker;
    
    public DrawingEditor(String[] args) {
        super("Drawing Editor", 700, 500);
        init();
        addChild(new OutlineRect(9, 9, 682, 482, Color.black, 1));
        addChild(new OutlineRect(19, 19, 592, 462, Color.lightGray, 1));
        SimpleGroup g = new SimpleGroup(10, 10, 690, 490);
        createTools(g);
        createCanvas(g);
        createOptions(g);
        addChild(g);
        println("Drawing Editor");
        println("Usage: choose a tool from the palette, and click & drag to " +
                "move or create an object.");
        println("Multiple selection: ctrl + click\n");
        println("Tools: 'Move' => move an object,    " +
                "'Line' => create a Line,\n"+
                "'Rect' => create an OutlineRect,    " +
                "'FRect' => create a FilledRect,\n" +
                "'Ellps' => create an OutlineEllipse,    " +
                "'FEllps' => create a FilledEllipse,\n" +
                "'Text' => create a Text,    " +
                "'Image' => create an Icon.");
    }
    
    private void createTools(Group g) {
        LayoutGroup tools = new LayoutGroup(605, 20, 70, 250,
                LayoutGroup.VERTICAL, 10);
        Text move = new Text(S_MOVE, 0, 0, font, Color.black);
        Text line = new Text(S_LINE, 0, 0, font, Color.black);
        Text rect = new Text(S_RECT, 0, 0, font, Color.black);
        Text fillR = new Text(S_FRECT, 0, 0, font, Color.black);
        Text ellps = new Text(S_ELLPS, 0, 0, font, Color.black);
        Text fillE = new Text(S_FELLPS, 0, 0, font, Color.black);
        Text txt = new Text(S_TEXT, 0, 0, font, Color.black);
        Text img = new Text(S_IMAGE, 0, 0, font, Color.black);
        tools.addChild(move);
        tools.addChild(line);
        tools.addChild(rect);
        tools.addChild(fillR);
        tools.addChild(ellps);
        tools.addChild(fillE);
        tools.addChild(txt);
        tools.addChild(img);
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
                setCurrentTool(toolTypes.get(text));
            }
        });
        addBehavior(toolsSelect);
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
        canvasBehavior = new MoveBehavior();
        canvasBehavior.setStartEvent(canvasStartEvent);
        canvasBehavior.setStopEvent(canvasStopEvent);
        canvasBehavior.setGroup(canvas);
        addBehavior(canvasBehavior);
        
//        BehaviorEvent selectStart = new BehaviorEvent(
//                BehaviorEvent.MOUSE_DOWN_ID, BehaviorEvent.SHIFT_MODIFIER,
//                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
//        BehaviorEvent selectStop = new BehaviorEvent(
//                BehaviorEvent.MOUSE_UP_ID, BehaviorEvent.SHIFT_MODIFIER,
//                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
//        ChoiceBehavior select = new ChoiceBehavior(
//                ChoiceBehavior.TOGGLE, true);
//        select.setStartEvent(selectStart);
//        select.setStopEvent(selectStop);
//        select.setGroup(canvas);
//        addBehavior(select);
        
        BehaviorEvent mselectStart = new BehaviorEvent(
                BehaviorEvent.MOUSE_DOWN_ID, BehaviorEvent.CONTROL_MODIFIER,
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent mselectStop = new BehaviorEvent(
                BehaviorEvent.MOUSE_UP_ID, BehaviorEvent.CONTROL_MODIFIER,
                BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior multiSelect = new ChoiceBehavior(
                ChoiceBehavior.MULTIPLE, false);
        multiSelect.setStartEvent(mselectStart);
        multiSelect.setStopEvent(mselectStop);
        multiSelect.setGroup(canvas);
        addBehavior(multiSelect);
    }
    
    
    private void createOptions(Group g) {
        LayoutGroup opts = new LayoutGroup(610, 410, 70, 70,
                LayoutGroup.VERTICAL, 5);
        Text optC = new Text("[Color]", 0, 0, font, Color.blue);
        Text optL = new Text("[Line]", 0, 0, font, Color.blue);
        opts.addChild(optC);
        opts.addChild(optL);
        g.addChild(opts);
        BehaviorEvent start = new BehaviorEvent(BehaviorEvent.MOUSE_DOWN_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        BehaviorEvent stop = new BehaviorEvent(BehaviorEvent.MOUSE_UP_ID, 
                BehaviorEvent.NO_MODIFIER, BehaviorEvent.LEFT_MOUSE_KEY, 0, 0);
        ChoiceBehavior select = new ChoiceBehavior();
        select.setStartEvent(start);
        select.setStopEvent(stop);
        select.setGroup(opts);
        select.addListener(new ChoiceBehavior.SelectListener() {
            public void onSelect(GraphicalObject o) {
                optionHelper(((Text) o).getText());
            }
        });
        addBehavior(select);
    }
    
    private void optionHelper(String opt) {
        if (opt.equals("[Color]")) {
            Color c = JColorChooser.showDialog(this,
                    "Choose Color", Color.black);
            if (c != null) {
                color = c;
                optionColor(c);
            }
        } else { // opt.equals(" Line ")
            String s = JOptionPane.showInputDialog("Enter line thickness:",
                    thickness);
            if (s != null && !s.isEmpty()) {
                try {
                    int n = Integer.parseInt(s);
                    thickness = n;
                    optionThickness(n);
                } catch(Exception e) {}
            }
        }
    }
    
    private void optionColor(Color c) {
        if (canvasBehavior != null) {
            switch (currentTool) {
            case LINE:
                ((NewLineBehavior) canvasBehavior).setColor(c);
                break;
            case RECT:
                ((NewRectBehavior) canvasBehavior).setColor(c);
                break;
            case FRECT:
                ((NewFilledRectBehavior) canvasBehavior).setColor(c);
                break;
            case ELLPS:
                ((NewEllipseBehavior) canvasBehavior).setColor(c);
                break;
            case FELLPS:
                ((NewFilledEllipseBehavior) canvasBehavior).setColor(c);
                break;
            case TEXT:
                ((NewTextBehavior) canvasBehavior).setColor(c);
                break;
            case MOVE:
            case IMAGE:
                break;
            }
        }
    }
    
    private void optionThickness(int n) {
        if (canvasBehavior != null) {
            switch (currentTool) {
            case LINE:
                ((NewLineBehavior) canvasBehavior).setLineThickness(n);
                break;
            case RECT:
                ((NewRectBehavior) canvasBehavior).setLineThickness(n);
                break;
            case ELLPS:
                ((NewEllipseBehavior) canvasBehavior).setLineThickness(n);
                break;
            case MOVE:
            case FRECT:
            case FELLPS:
            case TEXT:
            case IMAGE:
                break;
            }
        }
    }
    
    
    private ToolType currentTool = ToolType.MOVE;
    private void setCurrentTool(ToolType t) {
        if (t == null) t = ToolType.MOVE;
        if (currentTool != t) {
            removeBehavior(canvasBehavior);
            currentTool = t;
            switch (t) {
            case MOVE:
                canvasBehavior = new MoveBehavior();
                break;
            case LINE:
                canvasBehavior = new NewLineBehavior(color, thickness);
                break;
            case RECT:
                canvasBehavior = new NewRectBehavior(color, thickness);
                break;
            case FRECT:
                canvasBehavior = new NewFilledRectBehavior(color);
                break;
            case ELLPS:
                canvasBehavior = new NewEllipseBehavior(color, thickness);
                break;
            case FELLPS:
                canvasBehavior = new NewFilledEllipseBehavior(color);
                break;
            case TEXT:
                String s = JOptionPane.showInputDialog("Enter a string:");
                if (s != null && !s.isEmpty()) {
                    canvasBehavior = new NewTextBehavior(s, color, font);
                } else {
                    canvasBehavior = new MoveBehavior();
                    currentTool = ToolType.MOVE;
                }
                break;
            case IMAGE:
                int returnVal = filePicker.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        Image image = ImageIO.read(filePicker.getSelectedFile());
                        canvasBehavior = new NewIconBehavior(image);
                    } catch (Exception e) {
                        canvasBehavior = new MoveBehavior();
                        currentTool = ToolType.MOVE;
                    }
                } else {
                    canvasBehavior = new MoveBehavior();
                    currentTool = ToolType.MOVE;
                }
                break;
            }
            canvasBehavior.setStartEvent(canvasStartEvent);
            canvasBehavior.setStopEvent(canvasStopEvent);
            canvasBehavior.setGroup(canvas);
            addBehavior(canvasBehavior);
            
        } else if (t == ToolType.TEXT &&
                canvasBehavior instanceof NewTextBehavior) {
            String s = JOptionPane.showInputDialog("Enter a string:");
            if (s != null && !s.isEmpty()) {
                ((NewTextBehavior) canvasBehavior).setText(s);
            }
            
        } else if (t == ToolType.IMAGE &&
                canvasBehavior instanceof NewIconBehavior) {
            int returnVal = filePicker.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    Image image = ImageIO.read(filePicker.getSelectedFile());
                    ((NewIconBehavior) canvasBehavior).setImage(image);
                } catch (Exception e) {}
            }
        }
    }
    
    private static final String S_MOVE = " Move ";
    private static final String S_LINE = " Line ";
    private static final String S_RECT = " Rect ";
    private static final String S_FRECT = " FRect ";
    private static final String S_ELLPS = " Ellps ";
    private static final String S_FELLPS = " FEllps ";
    private static final String S_TEXT = " Text ";
    private static final String S_IMAGE = " Image ";
    private enum ToolType {
        MOVE, LINE, RECT, FRECT, ELLPS, FELLPS, TEXT, IMAGE
    }
    private Hashtable<String, ToolType> toolTypes;
    private void init() {
        toolTypes = new Hashtable<String, ToolType>();
        toolTypes.put(S_MOVE, ToolType.MOVE);
        toolTypes.put(S_LINE, ToolType.LINE);
        toolTypes.put(S_RECT, ToolType.RECT);
        toolTypes.put(S_FRECT, ToolType.FRECT);
        toolTypes.put(S_ELLPS, ToolType.ELLPS);
        toolTypes.put(S_FELLPS, ToolType.FELLPS);
        toolTypes.put(S_TEXT, ToolType.TEXT);
        toolTypes.put(S_IMAGE, ToolType.IMAGE);
        
        filePicker = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PNG, or GIF Images", "jpg", "png", "gif");
        filePicker.setFileFilter(filter);
    }
}
