package Abstraction;
public class Vecteur {
    public double[] valeurs;
    public int x;
    public int y;
    
    public Vecteur(double[] valeurs) {
        this.valeurs = valeurs;
    }

    public Vecteur(double[] valeurs, int x, int y) {
        this.valeurs = valeurs;
        this.x = x;
        this.y = y;
    }
    public int dimension() {
        return valeurs.length;
    }
}
