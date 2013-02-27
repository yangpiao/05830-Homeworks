import java.awt.*;
import java.awt.geom.*;


public class Line implements GraphicalObject {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color color;
    private int lineThickness;
    private Group group = null;
    
//    private int[] xPoints;
//    private int[] yPoints;
//    private Line2D line;

    public Line() {
        this(0, 0, 0, 0, Color.BLACK, 1);
    }

    public Line(int x1, int y1, int x2, int y2, 
            Color color, int lineThickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.lineThickness = lineThickness;
        
        // test
//        int dX = x2 - x1;
//        int dY = y2 - y1;
//        double lineLength = Math.sqrt(dX * dX + dY * dY);
//        double thick = (lineThickness % 2 == 0) ? lineThickness : (lineThickness - 1);
//        double scale = thick / (2 * lineLength);
//        double ddx = -scale * (double)dY;
//        double ddy = scale * (double)dX;
//        ddx = Math.abs(ddx) + 0.5;
//        ddy = Math.abs(ddy) + 0.5;
//        int dx = (int)ddx;
//        int dy = (int)ddy;
//        xPoints = new int[4];
//        yPoints = new int[4];
//        xPoints[0] = x1 + dx; yPoints[0] = y1 - dy;
//        xPoints[1] = x1 - dx; yPoints[1] = y1 + dy;
//        xPoints[2] = x2 - dx; yPoints[2] = y2 + dy;
//        xPoints[3] = x2 + dx; yPoints[3] = y2 - dy;
//        
//        line = new Line2D.Double(x1, y1, x2, y2);
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        if (this.x1 != x1) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.x1 = x1;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        if (this.y1 != y1) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.y1 = y1;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        if (this.x2 != x2) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.x2 = x2;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        if (this.y2 != y2) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.y2 = y2;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;
            if (group != null) {
                group.damage(getBoundingBox());
            }
        }
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        if (this.lineThickness != lineThickness) {
            if (group != null) {
                group.damage(getBoundingBox());
            }
            this.lineThickness = lineThickness;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        graphics.setStroke(new BasicStroke(lineThickness));
        graphics.setColor(color);
        graphics.drawLine(x1, y1, x2, y2);
        
        // test
//        double slope = (double)Math.abs(y2 - y1) / Math.abs(x2 - x1);
//        double angle = Math.atan(slope);
//        double sin = Math.sin(angle);
//        double cos = Math.cos(angle);
//        double halfLine = lineThickness / 2.0;
//        int x0 = (int)(halfLine * cos);
//        int y0 = (int)(halfLine * sin);
//        graphics.setColor(Color.orange);
//        graphics.fillPolygon(
//                new int[]{ x1 - x0 - 1, x1 + x0 + 1, x2 + x0 - 1, x2 - x0 + 1 }, 
//                new int[]{ y1 + y0, y1 - y0, y2 - y0, y2 + y0 }, 
//                4);
//
//        graphics.setColor(Color.cyan);
//        // graphics.fill(new Polygon(xPoints, yPoints, 4));
//        graphics.fillPolygon(xPoints, yPoints, 4);
    }

    @Override
    public BoundaryRectangle getBoundingBox() {
        if (lineThickness == 1) {
            return new BoundaryRectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        } else {
            int offset = 0, x0 = 0, y0 = 0;
            if (x2 == x1 || y2 == y1) {
                offset = (lineThickness - 1) / 2;
            } else {
                offset = (lineThickness + 1) / 2;
                
                // double temp = (double)(lineThickness * lineThickness) / (slope * slope + 1);
                // temp = Math.sqrt(temp);
                // offset = (int)Math.round(temp);
            }
            // Rectangle r = new Polygon(xPoints, yPoints, 4).getBounds();
            // return new BoundaryRectangle(r.x - 5, r.y - 5, r.width + 10, r.height + 10); 
            
            return new BoundaryRectangle(x1 - offset, 
                    y1 - offset, 
                    x2 + offset - (x1 - offset) + 1, 
                    y2 + offset - (y1 - offset) + 1);
        }
    }

    @Override
    public void moveTo(int x, int y) {
        BoundaryRectangle r = getBoundingBox();
        int offsetX = x - r.x, offsetY = y - r.y;
        if (offsetX != 0 || offsetY != 0) {
            if (group != null) {
                group.damage(r);
            }
            x1 += offsetX;
            y1 += offsetY;
            x2 += offsetX;
            y2 += offsetY;
            if (group != null) {
                group.damage(getBoundingBox());
                group.resizeChild(this);
            }
        }
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean contains(int x, int y) {
        // TODO: more precise `contains`
        return getBoundingBox().contains(x, y);
    }

    @Override
    public void setAffineTransform(AffineTransform af) {
        // TODO Auto-generated method stub

    }

    @Override
    public AffineTransform getAffineTransform() {
        // TODO Auto-generated method stub
        return null;
    }

}
