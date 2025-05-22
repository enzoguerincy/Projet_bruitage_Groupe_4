public static List<Patch> extractPatchs2(BufferedImage image, int s) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> patchs = new ArrayList<>();
       int x_compt = 0;
       int y_compt = 0;

       for (int y = 0; y <= hauteur - s; y=y+s/2) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+s/2) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=s/2;
           }
           y_compt +=s/2;
       }
       if  (y_compt < hauteur - s) {
    	   for (int x = 0; x <= largeur - s; x=x+s/2) {
               BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
               patchs.add(new Patch(patchImage, x,  hauteur-s));
           }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y <= hauteur - s; y=y+s/2) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage(largeur-s,hauteur-s , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

       return patchs;
   }
   
   public static List<Patch> extractPatchs3(BufferedImage image, double pers) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       int s =  (int) (image.getWidth() * pers);
       System.out.println(s);
       System.out.println(largeur);
       System.out.println(largeur - s);
       int decal = s/3+1;
       int x_compt = 0;
       int y_compt = 0;
       List<Patch> patchs = new ArrayList<>();

       for (int y = 0; y <= hauteur - s; y=y+decal) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+decal) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=decal;
           }
           y_compt+=decal;
       }
       if  (y_compt < hauteur - s) {
	       for (int x = 0; x <= largeur - s; x=x+decal) {
	           BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
	           patchs.add(new Patch(patchImage, x, hauteur-s));
	       }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y <= hauteur - s; y=y+decal) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage((largeur-s),(hauteur-s) , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

       return patchs;
   }
   
   public static List<Patch> extractPatchs4(BufferedImage image, int s) {
       int largeur = image.getWidth();
       int hauteur = image.getHeight();
       List<Patch> patchs = new ArrayList<>();
       int x_compt = 0;
       int y_compt = 0;

       for (int y = 0; y <= hauteur - s; y=y+s-1) {
    	   x_compt = 0;
           for (int x = 0; x <= largeur - s; x=x+s-1) {
               BufferedImage patchImage = image.getSubimage(x, y, s, s);
               patchs.add(new Patch(patchImage, x, y));
               x_compt+=s/2;
           }
           y_compt +=s/2;
       }
       if  (y_compt < hauteur - s) {
    	   for (int x = 0; x < largeur - s; x=x+s-1) {
               BufferedImage patchImage = image.getSubimage(x,hauteur-s , s, s);
               patchs.add(new Patch(patchImage, x,  hauteur-s));
           }
       }
       if  (x_compt < largeur - s) {
	       for (int y = 0; y < hauteur - s; y=y+s-1) {
	           BufferedImage patchImage = image.getSubimage(largeur-s,y , s, s);
	           patchs.add(new Patch(patchImage, largeur-s, y));
	       }
       }
       BufferedImage patchImage = image.getSubimage(largeur-s,hauteur-s , s, s);
       patchs.add(new Patch(patchImage, largeur-s, hauteur-s));
       

       return patchs;
   }
