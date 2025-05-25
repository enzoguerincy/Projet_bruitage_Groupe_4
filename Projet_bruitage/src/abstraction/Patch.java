package abstraction;
import java.awt.image.BufferedImage;

public class Patch {
	/**
     * L'image correspondant au patch (sous-image de l'image originale).
     */
    public BufferedImage image;

    /**
     * Coordonnée horizontale (colonne) du coin supérieur gauche du patch dans l'image originale.
     */
    public int x;

    /**
     * Coordonnée verticale (ligne) du coin supérieur gauche du patch dans l'image originale.
     */
    public int y;

    /**
     * Construit un nouveau patch avec son image et ses coordonnées dans l'image originale.
     *
     * @param image L'image correspondant au patch.
     * @param x     Position horizontale du patch dans l'image originale.
     * @param y     Position verticale du patch dans l'image originale.
     */
    public Patch(BufferedImage image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }
}
