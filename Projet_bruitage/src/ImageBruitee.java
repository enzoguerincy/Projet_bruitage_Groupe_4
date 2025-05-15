import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ImageBruitee {
    
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
   
   /**
    * Extrait tous les patchs s×s d'une image, avec leur position.
    * @param image L'image d'entrée.
    * @param s La taille du patch (s × s).
    * @return Liste des patchs (image + position).
    */
   public static List<Patch> extractPatchs(BufferedImage image, int s) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> patchs = new ArrayList<>();

       for (int y = 0; y <= hauteur - s; y++) {
           for (int x = 0; x <= largeur - s; x++) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
           }
       }

       return patchs;
   }
   
   
   /**
    * Reconstitue une image à partir des patchs et de leur position.
    * En cas de recouvrement, fait la moyenne des pixels.
    *
    * @param patchs La liste des patchs (image + position).
    * @param lignes Hauteur de l'image finale.
    * @param colonnes Largeur de l'image finale.
    * @return Image reconstruite.
    */
   public static BufferedImage reconstructPatchs(List<Patch> patchs, int lignes, int colonnes) {
       BufferedImage imageReconstruite = new BufferedImage(colonnes, lignes, BufferedImage.TYPE_INT_RGB);

       // Pour gérer la moyenne des pixels superposés
       int[][] sommePixels = new int[lignes][colonnes];
       int[][] compteurPixels = new int[lignes][colonnes];

       for (Patch patch : patchs) {
           BufferedImage imgPatch = patch.image;
           int patchSize = imgPatch.getWidth(); // on suppose carré

           for (int dy = 0; dy < patchSize; dy++) {
               for (int dx = 0; dx < patchSize; dx++) {
                   int x = patch.x + dx;
                   int y = patch.y + dy;

                   if (x < colonnes && y < lignes) {
                       Color couleur = new Color(imgPatch.getRGB(dx, dy));
                       int gris = couleur.getRed(); // R = G = B

                       sommePixels[y][x] += gris;
                       compteurPixels[y][x]++;
                   }
               }
           }
       }

       // Crée l'image finale en divisant les sommes par les compteurs
       for (int y = 0; y < lignes; y++) {
           for (int x = 0; x < colonnes; x++) {
               int moyenne = compteurPixels[y][x] == 0 ? 0 : sommePixels[y][x] / compteurPixels[y][x];
               Color c = new Color(moyenne, moyenne, moyenne);
               imageReconstruite.setRGB(x, y, c.getRGB());
           }
       } 

       return imageReconstruite;
   }
   	
   
   /**
    * Découpe l'image en n imagettes carrées de taille W × W à des positions aléatoires valides.
    * @param image L'image source.
    * @param W La taille de chaque imagette (W × W).
    * @param n Le nombre d’imagettes à extraire.
    * @return Liste des imagettes Patch (image + position).
    */
   public static List<Patch> decoupeImage(BufferedImage image, int W, int n) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> imagettes = new ArrayList<>();
       Random random = new Random();

       for (int i = 0; i < n; i++) {
           int x = random.nextInt(largeur - W + 1); // position x valide
           int y = random.nextInt(hauteur - W + 1); // position y valide
           BufferedImage imagette = image.getSubimage(x, y, W, W);
           imagettes.add(new Patch(imagette, x, y));
       }

       return imagettes;
   }
   
   
}