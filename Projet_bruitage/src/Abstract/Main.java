package Abstract;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        try {
            // === PARAMÈTRES ===
            String cheminImage = "images_test/lemurien.jpeg";
            double sigma = 20;
            int taillePatch = 8;
            double lambda = 50;

            // === 1. Charger l'image originale ===
            BufferedImage imageOriginale = ImageIO.read(new File(cheminImage));

            // === 2. Ajouter du bruit ===
            BufferedImage imageBruitee = ImageBruitee.noising(imageOriginale, sigma);
            ImageIO.write(imageBruitee, "jpeg", new File("out/image_bruitee.jpeg"));

            // === 3. MODE GLOBAL ===
            System.out.println("==== MODE ACP GLOBALE ====");
            List<Patch> patchsG = ImageBruitee.extractPatchs(imageBruitee, taillePatch);
            List<Vecteur> vecteursG = ImageBruitee.vectorPatchs(patchsG);
            CollectionVecteur cvG = new CollectionVecteur(vecteursG);
            CollectionVecteur.MoyCovResult mcrG = cvG.moyCov();
            List<Vecteur> baseG = CollectionVecteur.acp(vecteursG);
            double[][] baseMatG = CollectionVecteur.toMatriceBase(baseG);

            List<Vecteur> projGBase = cvG.proj(baseG, mcrG.vecteursCentres);

            // --- Seuillage doux
            List<Vecteur> projGDoux = cloneVecteurs(projGBase);
            for (Vecteur v : projGDoux) v.valeurs = Seuillage.seuillageDoux(lambda, v.valeurs);
            List<Patch> recGDoux = CollectionVecteur.reconstruirePatchsDepuisContributions(projGDoux, baseMatG, mcrG.moyenne.valeurs, taillePatch, patchsG);
            BufferedImage imageGDoux = ImageBruitee.reconstructPatchs(recGDoux, imageBruitee.getHeight(), imageBruitee.getWidth());
            ImageIO.write(imageGDoux, "jpeg", new File("out/global/image_debruitee_doux.jpeg"));

            // Évaluer
            double mseGD = ImageFinale.mse(imageOriginale, imageGDoux);
            double psnrGD = ImageFinale.psnr(mseGD);
            System.out.printf("GLOBAL DOUX : MSE = %.2f | PSNR = %.2f dB%n", mseGD, psnrGD);

            // --- Seuillage dur
            List<Vecteur> projGDur = cloneVecteurs(projGBase);
            for (Vecteur v : projGDur) v.valeurs = Seuillage.seuillageDur(lambda, v.valeurs);
            List<Patch> recGDur = CollectionVecteur.reconstruirePatchsDepuisContributions(projGDur, baseMatG, mcrG.moyenne.valeurs, taillePatch, patchsG);
            BufferedImage imageGDur = ImageBruitee.reconstructPatchs(recGDur, imageBruitee.getHeight(), imageBruitee.getWidth());
            ImageIO.write(imageGDur, "jpeg", new File("out/global/image_debruitee_dur.jpeg"));

            // Évaluer
            double mseGU = ImageFinale.mse(imageOriginale, imageGDur);
            double psnrGU = ImageFinale.psnr(mseGU);
            System.out.printf("GLOBAL DUR   : MSE = %.2f | PSNR = %.2f dB%n", mseGU, psnrGU);

            // === 4. MODE LOCAL ===
            System.out.println("==== MODE ACP LOCALE ====");
            List<Patch> patchsL = ImageBruitee.decoupeImage(imageBruitee, 32, 8); // blocs de 32x32
            List<Vecteur> vecteursL = ImageBruitee.vectorPatchs(patchsL);
            CollectionVecteur cvL = new CollectionVecteur(vecteursL);
            CollectionVecteur.MoyCovResult mcrL = cvL.moyCov();
            List<Vecteur> baseL = CollectionVecteur.acp(vecteursL);
            double[][] baseMatL = CollectionVecteur.toMatriceBase(baseL);

            List<Vecteur> projLBase = cvL.proj(baseL, mcrL.vecteursCentres);

            // --- Seuillage doux
            List<Vecteur> projLDoux = cloneVecteurs(projLBase);
            for (Vecteur v : projLDoux) v.valeurs = Seuillage.seuillageDoux(lambda, v.valeurs);
            List<Patch> recLDoux = CollectionVecteur.reconstruirePatchsDepuisContributions(projLDoux, baseMatL, mcrL.moyenne.valeurs, taillePatch, patchsL);
            BufferedImage imageLDoux = ImageBruitee.reconstructPatchs(recLDoux, imageBruitee.getHeight(), imageBruitee.getWidth());
            ImageIO.write(imageLDoux, "jpeg", new File("out/local/image_debruitee_doux.jpeg"));

            double mseLD = ImageFinale.mse(imageOriginale, imageLDoux);
            double psnrLD = ImageFinale.psnr(mseLD);
            System.out.printf("LOCAL DOUX   : MSE = %.2f | PSNR = %.2f dB%n", mseLD, psnrLD);

            // --- Seuillage dur
            List<Vecteur> projLDur = cloneVecteurs(projLBase);
            for (Vecteur v : projLDur) v.valeurs = Seuillage.seuillageDur(lambda, v.valeurs);
            List<Patch> recLDur = CollectionVecteur.reconstruirePatchsDepuisContributions(projLDur, baseMatL, mcrL.moyenne.valeurs, taillePatch, patchsL);
            BufferedImage imageLDur = ImageBruitee.reconstructPatchs(recLDur, imageBruitee.getHeight(), imageBruitee.getWidth());
            ImageIO.write(imageLDur, "jpeg", new File("out/local/image_debruitee_dur.jpeg"));

            double mseLU = ImageFinale.mse(imageOriginale, imageLDur);
            double psnrLU = ImageFinale.psnr(mseLU);
            System.out.printf("LOCAL DUR    : MSE = %.2f | PSNR = %.2f dB%n", mseLU, psnrLU);

            System.out.println("Toutes les images et mesures ont été générées dans out/global/ et out/local/");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Vecteur> cloneVecteurs(List<Vecteur> original) {
        return original.stream()
                .map(v -> new Vecteur(v.valeurs.clone()))
                .toList();
    }
}
