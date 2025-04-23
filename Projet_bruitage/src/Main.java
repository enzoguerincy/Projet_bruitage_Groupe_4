import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
   public static void main(String[] args) {
       try {
           // Chemin vers l'image correcte
           File inputFile = new File("images_test/lemurien.jpeg");
           String csvFile = "donnees.csv";
           FileWriter writer = new FileWriter(csvFile);
           // Vérification d'existence du fichier
           if (inputFile.exists() != true) {
               throw new RuntimeException("Le fichier images_test/lemurien.jpeg est introuvable !");
           }
           // Lecture de l'image JPEG
           BufferedImage imageOriginale = ImageIO.read(inputFile);
           // Génération d'une image bruitée
           BufferedImage imageBruitee = ImageUtilis.noising(imageOriginale, 20);
           ImageIO.write(imageBruitee, "jpeg", new File("images_test/lemurien_bruitee.jpeg"));
           // Génération des images bruitées selon les différents σ
           int[] sigmas = new int [40];
           for (int i = 0; i<40; i++) {
        	   sigmas[i]=10*i;
        	   writer.append(sigmas[i]+",");
           }
           writer.append("\n");
           for (int sigma : sigmas) {
               BufferedImage image_bruitee = ImageUtilis.noising(imageOriginale, sigma);
               ImageIO.write(image_bruitee, "jpeg", new File("images_test/lemurien_bruitee_sigma_" + sigma + ".jpeg"));
               float pourcentage_ressemblance = ImageUtilis.comparaison_bruitee_base(imageOriginale, image_bruitee);
               System.out.println(pourcentage_ressemblance);
               writer.append(pourcentage_ressemblance+",");
           }
           System.out.println("Les images bruitées ont été générées avec succès !");
           
         
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
