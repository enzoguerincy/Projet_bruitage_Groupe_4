package abstraction;
public class Vecteur {
	/**
     * Les valeurs numériques du vecteur.
     */
    public double[] valeurs;

    /**
     * Coordonnée horizontale associée au vecteur (optionnelle).
     */
    public int x;

    /**
     * Coordonnée verticale associée au vecteur (optionnelle).
     */
    public int y;

    /**
     * Construit un vecteur avec uniquement les valeurs (sans position).
     * @param valeurs Le tableau de coefficients.
     */
    public Vecteur(double[] valeurs) {
        this.valeurs = valeurs;
    }

    /**
     * Construit un vecteur avec des valeurs et une position (x, y).
     *
     * @param valeurs Le tableau de coefficients.
     * @param x La coordonnée horizontale.
     * @param y La coordonnée verticale.
     */
    public Vecteur(double[] valeurs, int x, int y) {
        this.valeurs = valeurs;
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la dimension (nombre de coefficients) du vecteur.
     * @return La taille du tableau {@code valeurs}.
     */
    public int dimension() {
        return valeurs.length;
    }
}
