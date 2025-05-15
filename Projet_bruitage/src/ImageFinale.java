import java.awt.image.BufferedImage;

public class ImageFinale {
	public static double calculMSE(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();

        if (width != image2.getWidth() || height != image2.getHeight()) {
            System.out.println("Les images n'ont pas la même taille.");
            return -1;
        }

        double somme = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = image1.getRGB(x, y);
                int pixel2 = image2.getRGB(x, y);

                int diff = pixel1 - pixel2;
                somme += diff * diff;
            }
        }

        int totalPixels = width * height;
        return somme / (double)(totalPixels);
    }
	
	public static double calculPSNR(double mse) {
        double maxPixelValue = 255.0;
        return 10 * Math.log10((maxPixelValue * maxPixelValue) / mse);
    }
}
