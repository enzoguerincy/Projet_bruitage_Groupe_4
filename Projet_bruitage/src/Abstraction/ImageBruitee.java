package Abstraction;
import java.awt.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ImageBruitee {
    
	
    public static BufferedImage fromFXImage(Image fxImage) {
        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    public static Image toFXImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
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
   
   public static List<Patch> extractPatchs2(BufferedImage image, int s) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> patchs = new ArrayList<>();
       int x_compt = 0;
       int y_compt = 0;

       for (int y = 0; y <= hauteur - s; y=y+s/2) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+s/2) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=s/2;
           }
           y_compt +=s/2;
       }
       if  (y_compt < hauteur - s) {
    	   for (int x = 0; x <= largeur - s; x=x+s/2) {
               BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
               patchs.add(new Patch(patchImage, x,  hauteur-s));
           }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y <= hauteur - s; y=y+s/2) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage(largeur-s,hauteur-s , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

       return patchs;
   }
   
   public static List<Patch> extractPatchs3(BufferedImage image, double pers) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       int s =  (int) (image.getWidth() * pers);
       System.out.println(s);
       System.out.println(largeur);
       System.out.println(largeur - s);
       int decal = s/3+1;
       int x_compt = 0;
       int y_compt = 0;
       List<Patch> patchs = new ArrayList<>();

       for (int y = 0; y <= hauteur - s; y=y+decal) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+decal) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=decal;
           }
           y_compt+=decal;
       }
       if  (y_compt < hauteur - s) {
	       for (int x = 0; x <= largeur - s; x=x+decal) {
	           BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
	           patchs.add(new Patch(patchImage, x, hauteur-s));
	       }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y <= hauteur - s; y=y+decal) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage((largeur-s),(hauteur-s) , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

       return patchs;
   }
   
   public static List<Patch> extractPatchs4(BufferedImage image, int s) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> patchs = new ArrayList<>();
       int x_compt = 0;
       int y_compt = 0;

       for (int y = 0; y <= hauteur - s; y=y+s-1) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+s-1) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=s/2;
           }
           y_compt +=s/2;
       }
       if  (y_compt < hauteur - s) {
    	   for (int x = 0; x < largeur - s; x=x+s-1) {
               BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
               patchs.add(new Patch(patchImage, x,  hauteur-s));
           }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y < hauteur - s; y=y+s-1) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage(largeur-s,hauteur-s , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

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
    * Découpe toute l’image en blocs de taille Ws × Ws (avec recouvrement partiel si besoin),
    * et extrait les patchs s × s dans chaque bloc.
    * @param image Image d'entrée
    * @param Ws Taille du bloc (ex : 32 ou 64)
    * @param s Taille des patchs (ex : 8)
    * @return Liste des patchs (BufferedImage + position)
    */
   public static List<Patch> decoupeImage(BufferedImage image, int Ws, int s) {
	    int largeur = image.getWidth();
	    int hauteur = image.getHeight();
	    List<Patch> patchs = new ArrayList<>();

	    int pas = Ws;

	    for (int yBloc = 0; yBloc <= hauteur - s; yBloc += pas) {
	        int yEff = (yBloc + Ws > hauteur) ? hauteur - Ws : yBloc;

	        for (int xBloc = 0; xBloc <= largeur - s; xBloc += pas) {
	            int xEff = (xBloc + Ws > largeur) ? largeur - Ws : xBloc;

	            // Bloc local (Ws x Ws)
	            BufferedImage bloc = image.getSubimage(xEff, yEff, Ws, Ws);

	            // Extraire les patchs à l'aide de extractPatchs4
	            List<Patch> patchsBloc = extractPatchs4(bloc, s);

	            // Ajuster les coordonnées des patchs
	            for (Patch p : patchsBloc) {
	                patchs.add(new Patch(p.image, p.x + xEff, p.y + yEff));
	            }
	        }
	    }

	    return patchs;
	}



   
   
   /**
    * Transforme chaque patch en un vecteur de taille s².
    * @param patchs La liste des patchs (image + position).
    * @return Liste de vecteurs (double[]) et leur position.
    */
   public static List<Vecteur> vectorPatchs(List<Patch> patchs) {
       List<Vecteur> listvecteurs = new ArrayList<>();

       for (Patch p : patchs) {
           BufferedImage img = p.image;
           int w = img.getWidth(); // on suppose carré
           double[] vecteur = new double[w * w];
           int index = 0;

           for (int y = 0; y < w; y++) {
               for (int x = 0; x < w; x++) {
                   Color c = new Color(img.getRGB(x, y));
                   vecteur[index++] = c.getRed(); // ou (R + G + B)/3 si couleur
               }
           }

           listvecteurs.add(new Vecteur(vecteur, p.x, p.y));
       }

       return listvecteurs;
   }
   
   
}