import java.util.ArrayList;
import java.util.List;

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
}
