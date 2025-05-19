import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.*;

public class CollectionVecteur {
    private List<Vecteur> vecteurs;
    private int nbVecteur;

    public CollectionVecteur(List<Vecteur> vecteurs) {
        this.vecteurs = vecteurs;
        this.nbVecteur = vecteurs.size();
    }

    public Vecteur CalculerVecteurMoyen() {
        int dim = vecteurs.get(0).dimension();
        double[] moyenne = new double[dim];

        for (Vecteur v : vecteurs) {
            for (int i = 0; i < dim; i++) {
                moyenne[i] += v.valeurs[i];
            }
        }

        for (int i = 0; i < dim; i++) {
            moyenne[i] /= nbVecteur;
        }

        return new Vecteur(moyenne);
    }

    public double[][] CalculerCovariance() {
        Vecteur moyenne = CalculerVecteurMoyen();
        int d = moyenne.dimension();
        double[][] covariance = new double[d][d];

        for (Vecteur v : vecteurs) {
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    covariance[i][j] += (v.valeurs[i] - moyenne.valeurs[i]) * (v.valeurs[j] - moyenne.valeurs[j]);
                }
            }
        }

        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                covariance[i][j] /= nbVecteur;
            }
        }

        return covariance;
    }

    public List<Vecteur> CalculerVecteurCentré() {
        Vecteur moyenne = CalculerVecteurMoyen();
        List<Vecteur> vecteursCentres = new ArrayList<>();

        for (Vecteur v : vecteurs) {
            double[] centre = new double[v.dimension()];
            for (int i = 0; i < v.dimension(); i++) {
                centre[i] = v.valeurs[i] - moyenne.valeurs[i];
            }
            vecteursCentres.add(new Vecteur(centre));
        }

        return vecteursCentres;
    }

    // MoyCov() : façade qui appelle moyenne, covariance et centrage
    public MoyCovResult MoyCov() {
        Vecteur moyenne = this.CalculerVecteurMoyen();
        double[][] covariance = this.CalculerCovariance();
        List<Vecteur> centres = this.CalculerVecteurCentré();

        return new MoyCovResult(moyenne, covariance, centres);
    }

    // Classe interne ou externe pour stocker les résultats
    public static class MoyCovResult {
        public Vecteur moyenne;
        public double[][] covariance;
        public List<Vecteur> vecteursCentres;

        public MoyCovResult(Vecteur moyenne, double[][] covariance, List<Vecteur> vecteursCentres) {
            this.moyenne = moyenne;
            this.covariance = covariance;
            this.vecteursCentres = vecteursCentres;
        }
    }
<<<<<<< HEAD
    
=======
>>>>>>> main
    public static List<Vecteur> acp(List<Vecteur> vecteurs) {
        int nbVecteurs = vecteurs.size();
        int dim = vecteurs.get(0).dimension();

        // 1. Calcul du vecteur moyen
        double[] moyenne = new double[dim];
        for (Vecteur v : vecteurs) {
            for (int i = 0; i < dim; i++) {
                moyenne[i] += v.valeurs[i];
            }
        }
        for (int i = 0; i < dim; i++) {
            moyenne[i] /= nbVecteurs;
        }

        // 2. Centrage des vecteurs
        double[][] donneesCentrees = new double[nbVecteurs][dim];
        for (int i = 0; i < nbVecteurs; i++) {
            Vecteur v = vecteurs.get(i);
            for (int j = 0; j < dim; j++) {
                donneesCentrees[i][j] = v.valeurs[j] - moyenne[j];
            }
        }

        // 3. Matrice de covariance
        RealMatrix M = MatrixUtils.createRealMatrix(donneesCentrees);
        RealMatrix covariance = M.transpose().multiply(M).scalarMultiply(1.0 / nbVecteurs);

        // 4. Décomposition en valeurs propres
        EigenDecomposition eig = new EigenDecomposition(covariance);

        // 5. Construction de la base orthonormale (vecteurs propres)
        List<Vecteur> baseOrthonormale = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            double[] composantes = eig.getEigenvector(i).toArray();
            baseOrthonormale.add(new Vecteur(composantes));
        }

        return baseOrthonormale;
    }
<<<<<<< HEAD

=======
    
>>>>>>> main
    // Méthode auxiliaire à ajouter aussi dans CollectionVecteur
    private double produitScalaire(Vecteur v1, Vecteur v2) {
        double sum = 0.0;
        for (int i = 0; i < v1.dimension(); i++) {
            sum += v1.valeurs[i] * v2.valeurs[i];
        }
        return sum;
    }

<<<<<<< HEAD
    public List<Vecteur> Proj(List<Vecteur> U, List<Vecteur> Vc) { 
=======
    public List<Vecteur> Proj(List<Vecteur> U, List<Vecteur> Vc) {
>>>>>>> main
        List<Vecteur> contributions = new ArrayList<>();

        for (Vecteur vi : Vc) {
            double[] coeffs = new double[U.size()];

            for (int j = 0; j < U.size(); j++) {
                Vecteur uj = U.get(j);
                coeffs[j] = produitScalaire(vi, uj);
            }

            contributions.add(new Vecteur(coeffs));
        }

        return contributions;
    }
    
    /**
     * Reconstruit un vecteur dans l’espace d’origine à partir d’un vecteur projeté (contribution).
     *
     * @param vecteurContributions Le vecteur de contribution (taille réduite, ex : 20)
     * @param basePCA              La base orthonormale de l’ACP (matrice [64][20] si patchs 8×8)
     * @param vecteurMoyen         Le vecteur moyen des données originales (taille 64)
     * @return Le vecteur reconstruit dans l’espace original (taille 64)
     */
    public static double[] reconstruireVecteurComplet(double[] vecteurContributions, double[][] basePCA, double[] vecteurMoyen) {
        int dimensionOriginale = basePCA.length;      // ex: 64
        int dimensionReduite = basePCA[0].length;      // ex: 20
        double[] vecteurReconstruit = new double[dimensionOriginale];

        for (int i = 0; i < dimensionOriginale; i++) {
            for (int j = 0; j < dimensionReduite; j++) {
                vecteurReconstruit[i] += basePCA[i][j] * vecteurContributions[j];
            }
            vecteurReconstruit[i] += vecteurMoyen[i];
        }

        return vecteurReconstruit;
    }
<<<<<<< HEAD

=======
    
>>>>>>> main
    /**
     * Transforme un vecteur de taille s² en une image carrée de taille s×s.
     *
     * @param vecteur Le vecteur représentant le patch (taille s×s)
     * @param taillePatch La taille du patch (s)
     * @return Une image BufferedImage correspondant au patch.
     */
    public static BufferedImage vecteurVersImage(double[] vecteur, int taillePatch) {
        if (vecteur.length != taillePatch * taillePatch) {
            throw new IllegalArgumentException("Le vecteur ne correspond pas à un patch de " + taillePatch + "×" + taillePatch);
        }

        BufferedImage image = new BufferedImage(taillePatch, taillePatch, BufferedImage.TYPE_BYTE_GRAY);

        for (int ligne = 0; ligne < taillePatch; ligne++) {
            for (int colonne = 0; colonne < taillePatch; colonne++) {
                int valeur = (int) Math.round(vecteur[ligne * taillePatch + colonne]);
                valeur = Math.min(255, Math.max(0, valeur)); // Clamp entre 0 et 255
                Color couleur = new Color(valeur, valeur, valeur);
                image.setRGB(colonne, ligne, couleur.getRGB()); // colonne = x, ligne = y
            }
        }

        return image;
    }

    /**
     * Reconstruit une liste de patchs à partir de leurs vecteurs projetés (contributions PCA).
     *
     * @param projections La liste des vecteurs projetés (contributions) + position (classe Vecteur)
     * @param basePCA     La base PCA (matrice [64][nbComposantes])
     * @param vecteurMoyen Le vecteur moyen (taille 64)
     * @param taillePatch La taille des patchs (ex: 8)
     * @return Liste des patchs reconstruits (image + position)
     */
<<<<<<< HEAD
    public static List<Patch> reconstruirePatchsDepuisContributions(List<Vecteur> projections, double[][] basePCA, double[] vecteurMoyen, int taillePatch) {
        List<Patch> patchsReconstitues = new ArrayList<>();

        for (Vecteur projection : projections) {
            double[] vecteurReconstruit = reconstruireVecteurComplet(projection.valeurs, basePCA, vecteurMoyen);
            BufferedImage imagePatch = vecteurVersImage(vecteurReconstruit, taillePatch);
            patchsReconstitues.add(new Patch(imagePatch, projection.x, projection.y));
        }

        return patchsReconstitues;
    }
=======
    public static List<Patch> reconstruirePatchsDepuisContributions(
            List<Vecteur> projections,
            double[][] base, // base U : [d][k]
            double[] moyenne,
            int taillePatch,
            List<Patch> patchsOriginaux // ⚠️ nouveau paramètre
    ) {
        List<Patch> patchs = new ArrayList<>();

        for (int k = 0; k < projections.size(); k++) {
            double[] alpha = projections.get(k).valeurs;
            int dim = base.length;
            int nbComposantes = base[0].length;

            // Produit U * alpha + moyenne
            double[] reconstruit = new double[dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < nbComposantes; j++) {
                    reconstruit[i] += base[i][j] * alpha[j];
                }
                reconstruit[i] += moyenne[i];
            }

            // Création de l'image du patch
            BufferedImage patchImg = new BufferedImage(taillePatch, taillePatch, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < taillePatch; i++) {
                for (int j = 0; j < taillePatch; j++) {
                    int index = i * taillePatch + j;
                    int val = (int) Math.round(reconstruit[index]);
                    val = Math.max(0, Math.min(255, val)); // clamp
                    Color gris = new Color(val, val, val);
                    patchImg.setRGB(j, i, gris.getRGB());
                }
            }

            // Réutilise les coordonnées d'origine
            Patch original = patchsOriginaux.get(k);
            patchs.add(new Patch(patchImg, original.x, original.y));
        }

        return patchs;
    }

    public static double[][] toMatriceBase(List<Vecteur> base) {
        int dim = base.get(0).valeurs.length;
        int k = base.size();
        double[][] matrice = new double[dim][k];
        for (int j = 0; j < k; j++) {
            for (int i = 0; i < dim; i++) {
                matrice[i][j] = base.get(j).valeurs[i];
            }
        }
        return matrice;
    }

>>>>>>> main
}
