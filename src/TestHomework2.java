import java.awt.*;

public class TestHomework2 extends TestFrame {
	private static final long serialVersionUID = 1L;

    public static void main (String[] args) {
        new TestHomework2 (args);
    }

    public TestHomework2 (String[] args) {
        super ("TestHomework2", 400, 400);

	try {

        println ("1. creating blue rect, thick = 4, x,y=0");
	GraphicalObject r = new OutlineRect (0, 0, 50, 80, Color.blue, 4);
        Group windowgroup = new SimpleGroup (0, 0, 300, 400);
        Group group = new SimpleGroup (0, 0, 300, 400);
        addChild (windowgroup);
        BoundaryRectangle bounds;

	windowgroup.addChild (group);
        group.addChild (r);
	redraw (windowgroup);

        println ("2. moving to 30,30");
	pause();
	r.moveTo(30,30);
	redraw (windowgroup);

	println ("3. remove from group--see if goes away");
	pause();
	group.removeChild (r);
	redraw (windowgroup);

	println("4. put back; overlap with a red rectangle");
	pause();
        group.addChild (r);
        OutlineRect r2 = new OutlineRect (10, 20, 50, 80, Color.red, 8);
        group.addChild (r2);
	redraw (windowgroup);

	println("5. bring blue to front");
	pause();
	group.bringChildToFront(r);
	redraw (windowgroup);

	println("6. moving red rect while behind blue");
	pause();
	r2.moveTo(20,30);
	redraw (windowgroup);

	println("7. change color of red rectangle to green");
	pause();
	r2.setColor(Color.green);
	redraw (windowgroup);

	println("8. Creating Filled Yellow Rect");
	pause();
	GraphicalObject r3 = new FilledRect (30, 40, 100, 20, Color.yellow);
	group.addChild(r3);
	redraw (windowgroup);

	println("9. Putting yellow next to blue");
	pause();
	bounds = r.getBoundingBox();
	r3.moveTo(bounds.x+bounds.width, bounds.y+(bounds.height/2));
	redraw (windowgroup);

	println("10. creating lines and icons");
	pause();
	
        println ("Line");
        Line line1 = new Line (70, 130, 120, 180, Color.blue, 10);
	group.addChild (line1);
        group.addChild (new Line (10, 130, 10, 180, Color.black, 1));
        group.addChild (new Line (20, 130, 60, 130, Color.red, 3));

        println ("Icon");
	Image duke = loadImageFully ("duke.gif");
       group.addChild (new Icon (duke, 10, 200));
	Icon icon1 = new Icon (loadImageFully ("dog.gif"), 80, 200);
      group.addChild (icon1);

	redraw (windowgroup);

	println("11. moving blue line behind red line using setX1");
	pause();
	line1.setX1(40);
	line1.setY1(110);
	redraw (windowgroup);

	println("12. moving blue line behind black line using moveTo");
	pause();
	line1.moveTo(1, 150);
	redraw (windowgroup);

	println("13. moving big icon using setX");
	pause();
	icon1.setX(30);
	redraw (windowgroup);
     
 	println("14. moving big icon in front of little icon");
	pause();
	icon1.moveTo(30, 220);
	redraw (windowgroup);

	println("15. Test group clipping");
	pause();
        group.addChild (new Line (299, 0, 299, 400, Color.black, 1));
	redraw (windowgroup);
	println("15a. filledrect");
	pause();
	group.addChild (new FilledRect(250, 100, 100, 40, Color.yellow));
	redraw (windowgroup);

	println("15b. line");
	pause();
	group.addChild (new Line (250, 110, 400, 200, Color.green, 4));
	redraw (windowgroup);

	println("15c. text");
	pause();
	group.addChild (new Text ("reallyLongStringShouldGetCutOff", 250, 200,
				  new Font ("Serif", Font.BOLD, 20),
				  Color.black));
	redraw (windowgroup);
	
        Group lg = new LayoutGroup (0, 0, 400, 400, Group.VERTICAL,2);
	println("16. adding r to another group, should crash");
	pause();
	try {
	  lg.addChild(r); //might crash
	} catch(Exception e) { println ("addChild raised exception:" + e); }
	println("16z: about to remove all. Get ready for different groups");
	pause();
	windowgroup.removeChild(group);
	redraw (windowgroup);
	pause();

	GraphicalObject la1 = new OutlineRect (0, 0, 50, 80, Color.orange, 4);
	GraphicalObject la2 = new FilledRect (60, 100, 10, 30, Color.yellow);
	GraphicalObject la3 = new Line (10, 200, 70, 20, Color.red, 8);
	GraphicalObject la4 = new Icon (duke, 0, 10);
	GraphicalObject lb1 = new OutlineRect (0, 0, 50, 80, Color.orange, 4);
	GraphicalObject lb2 = new FilledRect (60, 100, 10, 30, Color.yellow);
	GraphicalObject lb3 = new Line (10, 200, 70, 20, Color.red, 8);
	GraphicalObject lb4 = new Icon (duke, 0, 10);

	println("17. adding new objects to simplegroup");
	pause();

        Group topgroup = new SimpleGroup (0, 0, 400, 400);

        windowgroup.addChild (topgroup);
        Group sgroup = new SimpleGroup (10, 10, 200, 400);
	topgroup.addChild(sgroup);
	sgroup.addChild(la1);
	sgroup.addChild(la2);
	sgroup.addChild(la3);
	sgroup.addChild(la4);
	redraw (windowgroup);
	
	println("18. adding new objects to layoutgroup");
	pause();
        LayoutGroup layoutgroup = new LayoutGroup (220, 0, 150, 400,
					     Group.VERTICAL, 2);
	layoutgroup.addChild(lb1);
	layoutgroup.addChild(lb2);
	layoutgroup.addChild(lb3); //?
	layoutgroup.addChild(lb4);
	topgroup.addChild(layoutgroup);
	redraw (windowgroup);

    println("19. removing long line from layout group");
	pause();
	layoutgroup.removeChild(lb3);
	redraw (windowgroup);

	println("20. moving simple group to right and down");
	pause();
	sgroup.moveTo(30,30);
	redraw (windowgroup);

	println("21. changing layout group's offset to 30");
	pause();
	layoutgroup.setOffset(30);
	redraw (windowgroup);

	println("22. changing layout group to horizontal at x=10");
	pause();
	layoutgroup.setX(10);
	layoutgroup.setY(200);
	layoutgroup.setLayout(HORIZONTAL);
	redraw (windowgroup);
	
	println("23. changing layout group to be wider");
	pause();
	layoutgroup.setWidth(400);
	redraw (windowgroup);

	println("23a. moving filled rect in layout group to see what happens");
	pause();
	((FilledRect)lb2).setY(20);
	redraw (windowgroup);

	println("23b. moving simplegroup to try to make sure visible");
	pause();
	sgroup.moveTo(35,0);
	redraw (windowgroup);

	println("23c. resize simplegroup to children. Bounds should be (35,0,200,400). Then (35,0,~75,~205)");
	bounds = sgroup.getBoundingBox();
	println("   before bounds = "+bounds.x+", "+bounds.y+", "+bounds.width+", "+bounds.height);
	sgroup.resizeToChildren();
	bounds = sgroup.getBoundingBox();
	println("    after bounds = "+bounds.x+", "+bounds.y+", "+bounds.width+", "+bounds.height);
	
 //Scaled group is supposed to be implemented
	println("24. New scale group with scale = 0.5, 2.0");
	pause();
	GraphicalObject lc1 = new OutlineRect (0, 0, 50, 80, Color.orange, 4);
	GraphicalObject lc2 = new FilledRect (60, 100, 10, 30, Color.yellow);
	GraphicalObject lc3 = new Line (10, 200, 70, 20, Color.red, 8);
	GraphicalObject lc4 = new Icon (duke, 0, 10);
        ScaledGroup scalegroup = new ScaledGroup (220, 0, 150, 400, 0.5, 2.0);
	scalegroup.addChild(lc1);
	scalegroup.addChild(lc2);
	scalegroup.addChild(lc3);
	scalegroup.addChild(lc4);
	topgroup.addChild(scalegroup);
	redraw (windowgroup);
	
	println("25. Change the scale to be even smaller: 0.2, 0.4");
	pause();
	scalegroup.setScaleX(0.2);
	scalegroup.setScaleY(0.4);
	redraw (windowgroup);
	pause();
	
	println("removing all. Getting ready for different groups");
	windowgroup.removeChild(topgroup);
	pause();
	redraw (windowgroup);

	println("26. Test overlapping regions");
	pause();
	println("26a. first group");
        Group topgroup2 = new SimpleGroup (0, 0, 400, 400);
        windowgroup.addChild (topgroup2);
        Group sgroup2 = new SimpleGroup (10, 10, 200, 400);
	FilledRect ld1 = new FilledRect (30, 20, 70, 150, Color.yellow);
	GraphicalObject ld2 = new FilledRect (10, 60, 110, 70, Color.green);
	sgroup2.addChild(ld1);
	sgroup2.addChild(ld2);
	topgroup2.addChild(sgroup2);
	redraw (windowgroup);

	println("26b. adding second group to the right of the first");
	pause();
        Group sgroup3 = new SimpleGroup (175, 10, 200, 400);
	FilledRect le1 = new FilledRect (30, 20, 70, 150, Color.red);
	GraphicalObject le2 = new FilledRect (10, 60, 110, 70, Color.blue);
	sgroup3.addChild(le1);
	sgroup3.addChild(le2);
	topgroup2.addChild(sgroup3);
	redraw (windowgroup);

	println("26c. move r-b second group to be on top of first");
	pause();
	sgroup3.moveTo(45, 35);
	redraw (windowgroup);

	println("26d. change color on top: red -> gray");
	pause();
	le1.setColor(Color.gray);
	redraw (windowgroup);

	println("26e. change color on bottom: yellow -> black");
	pause();
	ld1.setColor(Color.black);
	redraw (windowgroup);

	println("26f. move black on bottom");
	pause();
	ld1.moveTo(15, 10);
	redraw (windowgroup);

    /*  // Tests for RotatedGroup, if implemented
	println("removing all. Getting ready for rotating group");
	windowgroup.removeChild(topgroup2);
	pause();
	redraw (windowgroup);
	println("27. Test rotateGroup");
	println("27a. create group; not rotated");
	pause();
        RotatedGroup rotgroup = new RotatedGroup (100, 100, 50, 70, 0.0);
        windowgroup.addChild (rotgroup);
        OutlineRect rr0 = new OutlineRect (0,0,50,70,Color.black,1);
	FilledRect rr1 = new FilledRect (10, 20, 30, 40, Color.yellow);
	FilledRect rr2 = new FilledRect (5, 5, 40, 10, Color.green);
	rotgroup.addChild(rr0);
	rotgroup.addChild(rr1);
	rotgroup.addChild(rr2);
	redraw (windowgroup);
	println("27b. rotate by 45 degrees");
	pause();
	rotgroup.setTheta( ((3.14159*2) * (45.0 / 360.0)) ); // in radians
	redraw (windowgroup);

	/**/ // end RotatedGroup Tests

        println ("DONE. close the window to stop");

	} catch(Exception e) { println ("got an exception: " + e); }
    }

}
