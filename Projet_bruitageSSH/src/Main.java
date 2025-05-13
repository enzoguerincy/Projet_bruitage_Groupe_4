import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileWriter;
import java.util.List;

public class Main {
    @SuppressWarnings("resource")
	public static void main(String[] args) {
        try {
            File inputFile = new File("images_test/lemurien.jpeg");
            FileWriter writer = new FileWriter("data/donnees.csv");

            if (!inputFile.exists()) {
                throw new RuntimeException("Le fichier images_test/lemurien.jpeg est introuvable !");
            }

            BufferedImage imageOriginale = ImageIO.read(inputFile);
            int sigmaTEST = 50;
            BufferedImage imageBruit = ImageUtils.noising(imageOriginale, sigmaTEST);
            File sortie = new File("images_test_reconstruit/lemurien_bruit_sigma_" + sigmaTEST + ".jpeg");
            ImageIO.write(imageBruit, "jpeg", sortie);
            
            int[] sigmas = new int[40];
            for (int i = 0; i < sigmas.length; i++) {
                sigmas[i] = 10 * i;
                writer.append(sigmas[i] + (i == sigmas.length - 1 ? "\n" : ","));
            }

            for (int i = 0; i < sigmas.length; i++) {
                int sigma = sigmas[i];
                BufferedImage imageBruitee = ImageUtils.noising(imageOriginale, sigma);
                File output = new File("images_test_bruite/lemurien_bruitee_sigma_" + sigma + ".jpeg");
                ImageIO.write(imageBruitee, "jpeg", output);

                float ressemblance = ImageUtils.comparaison_bruitee_base(imageOriginale, imageBruitee);
                System.out.println("σ = " + sigma + " → distance : " + ressemblance);
                writer.append(ressemblance + (i == sigmas.length - 1 ? "\n" : ","));
            }

            writer.close();
            System.out.println("Toutes les images et les données ont été générées avec succès !");
            
         // Extraire les patchs de taille 
            int taillepatch = 50;
            List<Patch> patchs = ImageUtils.extractPatchs(imageOriginale, taillepatch);

            System.out.println("Nombre de patchs extraits : " + patchs.size());

			//// Dossier de sortie des patchs
			//File patchDir = new File("patch_test");
			//if (!patchDir.exists()) {
			//    patchDir.mkdirs(); // Crée le dossier s'il n'existe pas
			//}
			//
			//// Sauvegarde des patchs extraits
			//int index = 0;
			//for (Patch p : patchs) {
			//    File outFile = new File(patchDir, "patch_" + index + "_x" + p.x + "_y" + p.y + ".png");
			//    ImageIO.write(p.image, "png", outFile);
			//    index++;
			//}
			//System.out.println("Tous les patchs ont été extraits et sauvegardés dans patch_test/");
			//
            
            // Reconstruction de l'image à partir des patchs
            BufferedImage imageReconstruite = ImageUtils.reconstructPatchs(patchs, imageOriginale.getHeight(), imageOriginale.getWidth());

            // Sauvegarde de l'image reconstruite
            ImageIO.write(imageReconstruite, "jpeg", new File("images_test_reconstruit/reconstruit_sigma_" + 0 + ".jpeg"));
            System.out.println("Image reconstruite sauvegardée.");
            
            
         // Découpe 10 imagettes 32x32 à partir de l'image bruitée
            List<Patch> imagettes = ImageUtils.decoupeImage(imageBruit, 100, 30);

            // Sauvegarde les imagettes
            File imagetteDir = new File("imagettes");
            imagetteDir.mkdirs();
            int id = 0;
            for (Patch p : imagettes) {
                File f = new File(imagetteDir, "imagette_" + id + "_x" + p.x + "_y" + p.y + ".png");
                ImageIO.write(p.image, "png", f);
                id++;
            }
            System.out.print(id);
            
            
         // Vectorisation des patchs
            List<Vecteur> vectors = ImageUtils.vectorPatchs(patchs);

            // Affichage d’un exemple
            System.out.println("Premier vecteur (patch 0) :");
            for (int i = 0; i < vectors.get(0).vecteurs.length; i++) {
                System.out.print(vectors.get(0).vecteurs[i] + " ");
            }
            System.out.println();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
