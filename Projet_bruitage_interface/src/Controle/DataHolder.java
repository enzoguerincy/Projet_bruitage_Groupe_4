package Controle;

import java.awt.image.BufferedImage;

import javafx.scene.image.Image;


public class DataHolder {

	private static BufferedImage ImageOriginale; // Image d'origine (non bruitée)
	private static BufferedImage ImageBruitee; // Image après bruitage
	private static double niveauBruitage = 0.0; // Dernier sigma utilisé pour le bruit
    private static Image imageSelectionnee;
    public enum Mode { GLOBAL, LOCAL }
    public enum Seuillage { DOUX, DUR }
    public enum Calcul {VISU, BAYES}
    private static BufferedImage ImageDebruitee;

    private static Mode modeSelectionne = Mode.GLOBAL;
    private static Seuillage seuillageSelectionne = Seuillage.DOUX;
    private static Calcul calculSelectionne = Calcul.VISU;

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
	
	public static Mode getModeSelectionne() {
	    return modeSelectionne;
	}
	public static void setModeSelectionne(Mode mode) {
	    modeSelectionne = mode;
	}

	public static Seuillage getSeuillageSelectionne() {
	    return seuillageSelectionne;
	}
	public static void setSeuillageSelectionne(Seuillage seuillage) {
	    seuillageSelectionne = seuillage;
	}
	
	public static Calcul getCalculSelectionne() {
		return calculSelectionne;
	}
	
	public static void setCalculSelectionne(Calcul calcul) {
		calculSelectionne = calcul;
	}
	
	public static BufferedImage getImageDebruitee() {
		return ImageDebruitee;
	}
	
	public static void setImageDebruitee(BufferedImage image) {
		ImageDebruitee = image;
	}

}