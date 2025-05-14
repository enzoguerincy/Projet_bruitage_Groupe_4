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
    
    public List<Vecteur> getVecteurs() {
        return vecteurs;
    }
    
    public int getTaille() {
    	return nbVecteur;
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

    // Méthode auxiliaire à ajouter aussi dans CollectionVecteur
    private double produitScalaire(Vecteur v1, Vecteur v2) {
        double sum = 0.0;
        for (int i = 0; i < v1.dimension(); i++) {
            sum += v1.valeurs[i] * v2.valeurs[i];
        }
        return sum;
    }

    public List<Vecteur> Proj(List<Vecteur> U, List<Vecteur> Vc) {
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
}
