package abstraction;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.*;

public class CollectionVecteur {
	
	  /** Liste des vecteurs contenus dans la collection. */
    private List<Vecteur> vecteurs;

    /** Nombre total de vecteurs dans la collection. */
    private int nbVecteur;

    /**
     * Construit une nouvelle collection de vecteurs à partir d'une liste.
     *
     * @param vecteurs La liste de vecteurs à inclure.
     */
    public CollectionVecteur(List<Vecteur> vecteurs) {
        this.vecteurs = vecteurs;
        this.nbVecteur = vecteurs.size();
    }

    /**
     * Calcule le vecteur moyen de la collection.
     *
     * @return Un vecteur représentant la moyenne des vecteurs de la collection.
     */
    public Vecteur calculerVecteurMoyen() {
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

    /**
     * Calcule la matrice de covariance de la collection de vecteurs.
     *
     * @return Une matrice de covariance (tableau 2D).
     */
    public double[][] calculerCovariance() {
        Vecteur moyenne = calculerVecteurMoyen();
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

    /**
     * Centre les vecteurs en soustrayant le vecteur moyen.
     *
     * @return Une liste de vecteurs centrés.
     */
    public List<Vecteur> calculerVecteurCentré() {
        Vecteur moyenne = calculerVecteurMoyen();
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

    /**
     * Méthode façade qui retourne la moyenne, la covariance et les vecteurs centrés.
     *
     * @return Un objet contenant la moyenne, la covariance et les vecteurs centrés.
     */
    public MoyCovResult moyCov() {
        Vecteur moyenne = this.calculerVecteurMoyen();
        double[][] covariance = this.calculerCovariance();
        List<Vecteur> centres = this.calculerVecteurCentré();

        return new MoyCovResult(moyenne, covariance, centres);
    }

    /**
     * Conteneur de résultats pour la moyenne, la covariance et les vecteurs centrés.
     */
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

    /**
     * Applique l'Analyse en Composantes Principales (ACP) à une liste de vecteurs.
     *
     * @param vecteurs Liste de vecteurs à analyser.
     * @return Liste des composantes principales (vecteurs propres).
     */
    public static List<Vecteur> acp(List<Vecteur> vecteurs) {
        int nbVecteurs = vecteurs.size();
        int dim = vecteurs.get(0).dimension();

        double[] moyenne = new double[dim];
        for (Vecteur v : vecteurs) {
            for (int i = 0; i < dim; i++) {
                moyenne[i] += v.valeurs[i];
            }
        }
        for (int i = 0; i < dim; i++) {
            moyenne[i] /= nbVecteurs;
        }

        double[][] donneesCentrees = new double[nbVecteurs][dim];
        for (int i = 0; i < nbVecteurs; i++) {
            Vecteur v = vecteurs.get(i);
            for (int j = 0; j < dim; j++) {
                donneesCentrees[i][j] = v.valeurs[j] - moyenne[j];
            }
        }

        RealMatrix M = MatrixUtils.createRealMatrix(donneesCentrees);
        RealMatrix covariance = M.transpose().multiply(M).scalarMultiply(1.0 / nbVecteurs);

        EigenDecomposition eig = new EigenDecomposition(covariance);

        List<Vecteur> baseOrthonormale = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            double[] composantes = eig.getEigenvector(i).toArray();
            baseOrthonormale.add(new Vecteur(composantes));
        }

        return baseOrthonormale;
    }

    /**
     * Calcule le produit scalaire entre deux vecteurs.
     *
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Produit scalaire (double).
     */
    private double produitScalaire(Vecteur v1, Vecteur v2) {
        double sum = 0.0;
        for (int i = 0; i < v1.dimension(); i++) {
            sum += v1.valeurs[i] * v2.valeurs[i];
        }
        return sum;
    }

    /**
     * Projette une liste de vecteurs sur une base orthonormée.
     *
     * @param U Base orthonormée (vecteurs propres).
     * @param Vc Vecteurs centrés à projeter.
     * @return Liste des contributions projetées (coordonnées dans la base).
     */
    public List<Vecteur> proj(List<Vecteur> U, List<Vecteur> Vc) {
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
     * Reconstruit des patchs image à partir de leurs projections PCA.
     *
     * @param projections Contributions projetées (vecteurs).
     * @param base Base PCA.
     * @param moyenne Vecteur moyen des données d’origine.
     * @param taillePatch Taille du patch (ex : 8).
     * @param patchsOriginaux Liste de patchs originaux (pour les positions).
     * @return Liste des patchs reconstruits (images avec positions).
     */
    public static List<Patch> reconstruirePatchsDepuisContributions(
            List<Vecteur> projections,
            double[][] base, 
            double[] moyenne,
            int taillePatch,
            List<Patch> patchsOriginaux 
    ) {
        List<Patch> patchs = new ArrayList<>();

        for (int k = 0; k < projections.size(); k++) {
            double[] alpha = projections.get(k).valeurs;
            int dim = base.length;
            int nbComposantes = base[0].length;

            
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

	    /**
	     * Convertit une base de vecteurs en une matrice de taille 2x2.
	     *
	     * @param base Liste des vecteurs de la base.
	     * @return Matrice double[][] représentant la base.
	     */
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

}
