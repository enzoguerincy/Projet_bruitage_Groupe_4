package abstraction;

import java.util.List;

public class Seuillage {
	
	public static double calculerVarianceXb(List<Vecteur> vecteurs) {
	       int totalCoeff = 0;
	       double somme = 0;
	       double sommeCarres = 0;
	       for (Vecteur v : vecteurs) {
	           for (double val : v.valeurs) {
	               somme += val;
	               sommeCarres += val * val;
	               totalCoeff++;
	           }
	       }
	       double moyenne = somme / totalCoeff;
	       return (sommeCarres / totalCoeff) - (moyenne * moyenne);
	}
	
	public static double[] seuillageDur(double lambda, double[] x) {
	       double[] resultat = new double[x.length];
	       for (int i = 0; i < x.length; i++) {
	    	   if (Math.abs(x[i]) <= lambda) {
	    		   resultat[i] = 0;
	    	   }else {
	    		   resultat[i] = x[i];
	    	   }
	       }
	       return resultat;
	   }

	   /**
	    * Applique un seuillage doux (Soft Thresholding).
	    * Réduit de λ les coefficients en valeur absolue > λ, met à 0 les autres.
	    *
	    * @param lambda Le seuil à appliquer.
	    * @param x Le tableau de coefficients à seuiller.
	    * @return Un tableau seuillé (soft thresholding).
	    */
	   public static double[] seuillageDoux(double lambda, double[] x) {
	       double[] resultat = new double[x.length];
	       for (int i = 0; i < x.length; i++) {
	           double xi = x[i];
	           if (Math.abs(xi) <= lambda) {
	               resultat[i] = 0;
	           } else if (xi > 0) {
	               resultat[i] = xi - lambda;
	           } else {
	               resultat[i] = xi + lambda;
	           }
	       }
	       return resultat;
	   }

	   /**
	    * Calcule le seuil VisuShrink proposé par Donoho et Johnstone.
	    *
	    * @param sigma L’écart-type du bruit.
	    * @param L Le nombre total de pixels (taille de l’image ou du vecteur).
	    * @return Le seuil VisuShrink λ.
	    */
	   public static double calculSeuilVisuShrink(double sigma, int L) {
	       return sigma * Math.sqrt(2 * Math.log(L));
	   }

	   /**
	    * Calcule le seuil BayesShrink pour le seuillage adaptatif.
	    *
	    * @param sigma2 La variance du bruit.
	    * @param varianceXb La variance estimée du signal bruité.
	    * @return Le seuil BayesShrink λ.
	    */
	   public static double calculSeuilBayesShrink(double sigma2, double varianceXb) {
	       double sigmaX = Math.sqrt(Math.max(varianceXb - sigma2, 0));
	       if (sigmaX == 0) return Double.MAX_VALUE; 
	       return sigma2 / sigmaX;
	   }
}
