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
    
    public CollectionVecteur vectorPatch(int s) {
        CollectionVecteur collection = new CollectionVecteur();

        for (Patch patch : patchs) {
            BufferedImage img = patch.image;
            double[] vecteur = new double[s * s];
            int index = 0;

            for (int j = 0; j < s; j++) {
                for (int i = 0; i < s; i++) {
                    int rgb = img.getRGB(i, j);
                    
                    
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    double gray = (r + g + b) / 3.0;

                    vecteur[index++] = gray;
                }
            }

            Vecteur v = new Vecteur(vecteur, patch.x, patch.y);
            collection.ajouterVecteur(v);
        }

        return collection;
    }
    
}
