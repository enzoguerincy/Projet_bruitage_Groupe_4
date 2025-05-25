package abstraction;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CollectionPatch {
	
    /** Liste des patchs stockés dans la collection. */
    private List<Patch> patchs;

    /** Nombre total de patchs dans la collection. */
    private int nbPatch;

    /**
     * Constructeur par défaut. Initialise une collection vide de patchs.
     */
    public CollectionPatch() {
        this.patchs = new ArrayList<>();
        this.nbPatch = 0;
    }

    /**
     * Ajoute un patch à la collection.
     * @param patch Le patch à ajouter.
     */
    public void addPatch(Patch patch) {
        patchs.add(patch);
        nbPatch++;
    }

    /**
     * Supprime un patch de la collection s'il est présent.
     * @param patch Le patch à supprimer.
     */
    public void removePatch(Patch patch) {
        if (patchs.remove(patch)) {
            nbPatch--;
        }
    }

    /**
     * Renvoie le nombre de patchs dans la collection.
     * @return Le nombre total de patchs.
     */
    public int getCount() {
        return nbPatch;
    }

    /**
     * Renvoie la liste des patchs contenus dans la collection.
     * @return Liste des patchs.
     */
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
            int w = img.getWidth(); 
            double[] vecteur = new double[w * w];
            int index = 0;

            for (int y = 0; y < w; y++) {
                for (int x = 0; x < w; x++) {
                    Color c = new Color(img.getRGB(x, y));
                    vecteur[index++] = c.getRed(); 
                }
            }

            listvecteurs.add(new Vecteur(vecteur, p.x, p.y));
        }

        return listvecteurs;
    }

    
}
