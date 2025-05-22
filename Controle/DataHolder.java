package Controle;

import java.awt.image.BufferedImage;

import javafx.scene.image.Image;


public class DataHolder {

	private static BufferedImage ImageOriginale; // Image d'origine (non bruitée)
	private static BufferedImage ImageBruitee; // Image après bruitage
	private static double niveauBruitage = 0.0; // Dernier sigma utilisé pour le bruit
    private static Image imageSelectionnee;

    public static Image getImageSelectionnee() {
        return imageSelectionnee;
    }

    public static void setImageSelectionnee(Image image) {
        imageSelectionnee = image;
    }
	
	public static BufferedImage getImageOriginale() {
		return ImageOriginale;
	}

	public static void setImageOriginale(BufferedImage image) {
		ImageOriginale = image;
		ImageBruitee = null; // On réinitialise le bruitage à chaque nouvelle image
	}

	public static BufferedImage getImageBruitee() {
		return ImageBruitee;
	}

	public static void setImageBruitee(BufferedImage image) {
		ImageBruitee = image;
	}

	public static double getNiveauBruitage() {
		return niveauBruitage;
	}

	public static void setNiveauBruitage(double niveau) {
		niveauBruitage = niveau;
	}

	public static void clear() {
		ImageOriginale = null;
		ImageBruitee = null;
		niveauBruitage = 0.0;
	}

}
