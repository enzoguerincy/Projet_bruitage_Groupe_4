import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class ImageFinale {
	
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
	
	public double psnr(float mse) {
		return (10 * Math.log10(255 / mse));
	}
	
}
