import java.awt.*;


public class Arrow extends Icon {
    public Arrow(Image image, int x, int y) {
        super(image, x, y);
    }
    
    @Override
    public void draw(Graphics2D graphics, Shape clipShape) {
        graphics.setClip(clipShape);
        Image image = getImage();
        int x = getX(), y = getY();
        graphics.drawImage(image, x, y, null);
    }
}
