import java.awt.image.BufferedImage;

public class Patch {
    public BufferedImage image;
    public int x;
    public int y;

    public Patch(BufferedImage image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }
}
