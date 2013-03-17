import java.awt.*;


public class NewIconBehavior extends NewBehavior {
    private Image image;
    
    public NewIconBehavior() {
        super(true);
        image = null;
    }
    
    public NewIconBehavior(Image image) {
        super(true);
        this.image = image;
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if (!image.equals(this.image)) {
            this.image = image;
        }
    }

    @Override
    public GraphicalObject make(int x1, int y1, int x2, int y2) {
        return new Icon(image, x1, y1);
    }

    @Override
    public void resize(GraphicalObject gobj, int x1, int y1, int x2, int y2) {
        gobj.moveTo(x2, y2);
    }

}
