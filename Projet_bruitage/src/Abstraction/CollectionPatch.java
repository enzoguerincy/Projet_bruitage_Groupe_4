package Abstraction;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CollectionPatch {
    private List<Patch> patchs;
    private int nbPatch;

    public CollectionPatch() {
        this.patchs = new ArrayList<>();
        this.nbPatch = 0;
    }

    public void addPatch(Patch patch) {
        patchs.add(patch);
        nbPatch++;
    }

    public void removePatch(Patch patch) {
        if (patchs.remove(patch)) {
            nbPatch--;
        }
    }

    public int getCount() {
        return nbPatch;
    }

    public List<Patch> getPatches() {
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