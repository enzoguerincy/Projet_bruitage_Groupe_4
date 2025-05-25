package controle;

import java.awt.image.BufferedImage;

import javafx.scene.image.Image;

public class DataHolder {

	private static BufferedImage ImageOriginale;
	private static BufferedImage ImageBruitee;
	private static double niveauBruitage = 0.0;
	private static Image imageSelectionnee;

	public enum Mode {
		GLOBAL, LOCAL
	}

	public enum Seuillage {
		DOUX, DUR
	}

	public enum Calcul {
		VISU, BAYES, SLIDER
	}

	private static BufferedImage ImageDebruitee;

	private static Mode modeSelectionne = Mode.LOCAL;
	private static Seuillage seuillageSelectionne = Seuillage.DOUX;
	private static Calcul calculSelectionne = Calcul.SLIDER;

	/**
	 * Retourne l'image sélectionnée par l'utilisateur depuis l'explorateur.
	 * 
	 * @return l’image sélectionnée au format JavaFX
	 */
	public static Image getImageSelectionnee() {
		return imageSelectionnee;
	}

	/**
	 * Définit l’image sélectionnée dans l’interface.
	 * 
	 * @param image l’image JavaFX sélectionnée
	 */
	public static void setImageSelectionnee(Image image) {
		imageSelectionnee = image;
	}

	/**
	 * Retourne l'image originale chargée.
	 * 
	 * @return l’image originale sans bruit
	 */
	public static BufferedImage getImageOriginale() {
		return ImageOriginale;
	}

	/**
	 * Définit l'image originale. Remet à NULL l’image bruitée.
	 * 
	 * @param image l’image brute chargée
	 */
	public static void setImageOriginale(BufferedImage image) {
		ImageOriginale = image;
		ImageBruitee = null;
	}

	/**
	 * Retourne l'image bruitée.
	 * 
	 * @return l’image contenant le bruit ajouté
	 */
	public static BufferedImage getImageBruitee() {
		return ImageBruitee;
	}

	/**
	 * Définit l’image bruitée (générée à partir de l’originale).
	 * 
	 * @param image l’image bruitée
	 */
	public static void setImageBruitee(BufferedImage image) {
		ImageBruitee = image;
	}

	/**
	 * Retourne le niveau de bruit utilisé pour la génération.
	 * 
	 * @return sigma du bruit
	 */
	public static double getNiveauBruitage() {
		return niveauBruitage;
	}

	/**
	 * Définit le niveau de bruit appliqué.
	 * 
	 * @param niveau écart-type du bruit (sigma)
	 */
	public static void setNiveauBruitage(double niveau) {
		niveauBruitage = niveau;
	}

	/**
	 * Réinitialise toutes les données liées aux images.
	 */
	public static void clear() {
		ImageOriginale = null;
		ImageBruitee = null;
		niveauBruitage = 0.0;
	}

	/**
	 * Retourne le mode d’analyse sélectionné.
	 * 
	 * @return GLOBAL ou LOCAL
	 */
	public static Mode getModeSelectionne() {
		return modeSelectionne;
	}

	/**
	 * Définit le mode d’analyse à utiliser.
	 * 
	 * @param mode le mode GLOBAL ou LOCAL
	 */
	public static void setModeSelectionne(Mode mode) {
		modeSelectionne = mode;
	}

	/**
	 * Retourne le type de seuillage sélectionné.
	 * 
	 * @return DOUX ou DUR
	 */
	public static Seuillage getSeuillageSelectionne() {
		return seuillageSelectionne;
	}

	/**
	 * Définit le type de seuillage à appliquer.
	 * 
	 * @param seuillage DOUX ou DUR
	 */
	public static void setSeuillageSelectionne(Seuillage seuillage) {
		seuillageSelectionne = seuillage;
	}

	/**
	 * Retourne le mode de calcul de seuil sélectionné.
	 * 
	 * @return VISU, BAYES ou SLIDER
	 */
	public static Calcul getCalculSelectionne() {
		return calculSelectionne;
	}

	/**
	 * Définit la méthode de calcul du seuil.
	 * 
	 * @param calcul VISU, BAYES ou SLIDER
	 */
	public static void setCalculSelectionne(Calcul calcul) {
		calculSelectionne = calcul;
	}

	/**
	 * Retourne l'image après débruitage.
	 * 
	 * @return l’image restaurée
	 */
	public static BufferedImage getImageDebruitee() {
		return ImageDebruitee;
	}

	/**
	 * Définit l’image après le débruitage.
	 * 
	 * @param image l’image restaurée
	 */
	public static void setImageDebruitee(BufferedImage image) {
		ImageDebruitee = image;
	}

}
