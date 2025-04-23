import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageUtils {
    
	/**
 	* Ajoute du bruit gaussien à une image.
 	* @param img Image originale.
 	* @param sigma Écart-type du bruit gaussien.
 	* @return Image bruitée.
 	*/
	public static BufferedImage noising(BufferedImage img, double sigma) {
    	int largeur = img.getWidth();
    	int hauteur = img.getHeight();
    	BufferedImage noisyImage = new BufferedImage(largeur, hauteur, img.getType());
    	Random random = new Random();

    	for (int i = 0; i < largeur ; i++) {
        	for (int j = 0; j < hauteur; j++) {
            	Color couleur = new Color(img.getRGB(i, j));
            	int gris = couleur.getRed(); // Image en niveau de gris, R=G=B

            	// Ajout du bruit gaussien
            	int noisyGris = (int) (gris + random.nextGaussian() * sigma);

            	// Saturation pour rester dans [0, 255]
            	noisyGris = Math.min(Math.max(noisyGris, 0), 255);

            	Color noisyCouleur = new Color(noisyGris, noisyGris, noisyGris);
            	noisyImage.setRGB(i, j, noisyCouleur.getRGB());
        	}
    	}

    	return noisyImage;
	}
	
   public static float comparaison_bruitee_base( BufferedImage img_base, BufferedImage img_bruitee) {
	   int largeur = img_base.getWidth();
   	   int hauteur = img_base.getHeight();
   	   float pourcentage_ressemblance = 0;
   	   for (int i = 0; i < largeur; i++) {
   		   for (int j = 0; j < hauteur; j++) {
   			   Color couleur_base = new Color(img_base.getRGB(i, j));
   			   Color couleur_bruitee = new Color(img_bruitee.getRGB(i, j));  
   			   int gris_base = couleur_base.getRed();
   			   int gris_bruitee = couleur_bruitee.getRed();
   			   pourcentage_ressemblance = pourcentage_ressemblance + ((float)Math.abs(gris_base - gris_bruitee))/255 ;
   			   
   		   }
   	   }
   	   return pourcentage_ressemblance/(hauteur*largeur);
   }
}