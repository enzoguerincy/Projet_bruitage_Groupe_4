package abstraction;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class ImageFinale {
	
	/**
     * Calcule l'erreur quadratique moyenne (MSE) entre deux images en niveaux de gris.
     * Les deux images doivent avoir la même taille.
     *
     * @param img1 L'image de référence.
     * @param img2 L'image à comparer.
     * @return La valeur de l'erreur quadratique moyenne.
     * @throws IllegalArgumentException si les dimensions des images ne correspondent pas.
     */
	public static double mse(BufferedImage img1, BufferedImage img2) {
	    int width = img1.getWidth();
	    int height = img1.getHeight();

	    if (width != img2.getWidth() || height != img2.getHeight()) {
	        throw new IllegalArgumentException("Les dimensions des images ne correspondent pas.");
	    }

	    double sommeErreursCarrees = 0.0;

	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int pixel1 = new Color(img1.getRGB(x, y)).getRed(); // R = G = B en niveau de gris
	            int pixel2 = new Color(img2.getRGB(x, y)).getRed();
	            double erreur = pixel1 - pixel2;
	            sommeErreursCarrees += erreur * erreur;
	        }
	    }

	    return sommeErreursCarrees / (width * height);
	}
	
	/**
     * Calcule le PSNR (Peak Signal-to-Noise Ratio) à partir d'une valeur de MSE donnée.
     *
     * @param mse L'erreur quadratique moyenne entre deux images.
     * @return La valeur du PSNR en décibels. Retourne {@code Double.POSITIVE_INFINITY} si {@code mse == 0}.
     */
	public static double psnr(double mse) {
	    if (mse == 0) return Double.POSITIVE_INFINITY;
	    return 10 * Math.log10((255.0 * 255.0) / mse);
	}

	
}
