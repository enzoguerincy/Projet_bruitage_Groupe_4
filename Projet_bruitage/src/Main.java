import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            File inputFile = new File("images_test/lemurien.jpeg");

            if (!inputFile.exists()) {
                throw new RuntimeException("Le fichier images_test/lemurien.jpeg est introuvable !");
            }

            BufferedImage imageOriginale = ImageIO.read(inputFile);

            // --- Génération d’un bruit test unique (sigma = 50) ---
            int sigmaTest = 50;
            BufferedImage imageBruit = ImageUtils.noising(imageOriginale, sigmaTest);
            ImageIO.write(imageBruit, "jpeg", new File("images_test_reconstruit/lemurien_bruit_sigma_" + sigmaTest + ".jpeg"));

            // --- CSV pour ressemblance : écriture avec try-with-resources ---
            try (FileWriter writer = new FileWriter("data/donnees.csv")) {
                // Ligne d'entête : les valeurs de sigma
                for (int i = 0; i < 40; i++) {
                    int sigma = 10 * i;
                    writer.append(sigma + (i == 39 ? "\n" : ","));
                }

                // Calcul ressemblance et sauvegarde images bruitées
                for (int i = 0; i < 40; i++) {
                    int sigma = 10 * i;
                    BufferedImage imageBruitee = ImageUtils.noising(imageOriginale, sigma);
                    File output = new File("images_test_bruite/lemurien_bruitee_sigma_" + sigma + ".jpeg");
                    ImageIO.write(imageBruitee, "jpeg", output);

                    float ressemblance = ImageUtils.comparaison_bruitee_base(imageOriginale, imageBruitee);
                    System.out.println("σ = " + sigma + " → distance : " + ressemblance);
                    writer.append(ressemblance + (i == 39 ? "\n" : ","));
                }
            }

            // --- Extraction des patchs sur l'image originale ---
            int taillePatch = 8;
            List<Patch> patchs = ImageUtils.extractPatchs(imageOriginale, taillePatch);
            System.out.println("Nombre de patchs extraits : " + patchs.size());

            // --- Reconstruction de l'image à partir des patchs ---
            BufferedImage imageReconstruite = ImageUtils.reconstructPatchs(patchs, imageOriginale.getHeight(), imageOriginale.getWidth());
            ImageIO.write(imageReconstruite, "jpeg", new File("images_test_reconstruit/reconstruit_sigma_0.jpeg"));
            System.out.println("Image reconstruite sauvegardée.");

            // --- Découpe d'imagettes à partir de l'image bruitée (sigmaTest = 50) ---
            List<Patch> imagettes = ImageUtils.decoupeImage(imageBruit, 100, 30);
            File imagetteDir = new File("imagettes");
            imagetteDir.mkdirs();

            for (int id = 0; id < imagettes.size(); id++) {
                Patch p = imagettes.get(id);
                File f = new File(imagetteDir, "imagette_" + id + "_x" + p.x + "_y" + p.y + ".png");
                ImageIO.write(p.image, "png", f);
            }
            System.out.println(imagettes.size() + " imagettes enregistrées.");

            // --- Vectorisation des patchs ---
            List<Vecteur> vectors = CollectionPatch.vectorPatchs(patchs);
            System.out.println("Premier vecteur (patch 0) :");
            int compteur = 0;
            for (int i = 0; i < vectors.get(0).valeurs.length; i++ , compteur++) {
                System.out.print(vectors.get(0).valeurs[i] + " ");
            }
            System.out.println(compteur);

            // --- Moyenne + Covariance ---
            CollectionVecteur collection = new CollectionVecteur(vectors);
            CollectionVecteur.MoyCovResult result = collection.MoyCov();
            System.out.println("\nMoyenne[0] = " + result.moyenne.valeurs[0]);
            System.out.println("Covariance[0][0] = " + result.covariance[0][0]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}