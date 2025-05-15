import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
/**
 *La classe CollectionPatch représente une collection de Patch 
 *qui nous sert à gérer l'ajout, la suppression et la vectorisation des patchs.
*/

public class CollectionPatch {
    //Liste de Patchs stockés
    private List<Patch> patchs;
    //Nombre de Patchs
    private int nbPatch;
    //Initialisation de constructeur
    public CollectionPatch() {
        this.patchs = new ArrayList<>();
        this.nbPatch = 0;
    }
    //Ajout de Patchs
    public void addPatch(Patch patch) {
        patchs.add(patch);
        nbPatch++;
    }
    //Suppression de Patchs
    public void removePatch(Patch patch) {
        if (patchs.remove(patch)) {
            nbPatch--;
        }
    }
    //Renvoyer le nombre de Patchs
    public int getCount() {
        return nbPatch;
    }
    //Renvoyer la liste de Patchs
    public List<Patch> getPatches() {
        return patchs;
    }
    //Convertir les Patchs en liste de vecteurs 
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
