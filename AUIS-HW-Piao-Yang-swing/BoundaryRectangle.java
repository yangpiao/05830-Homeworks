

import java.awt.Rectangle;

public class BoundaryRectangle extends Rectangle {
	private static final long serialVersionUID = 1L;
	public BoundaryRectangle(BoundaryRectangle r) {
		super (r);
	}
	public BoundaryRectangle(int x, int y, int w, int h) {
		super (x,y,w,h);
	}
	public BoundaryRectangle() {
		super ();
	}
}
