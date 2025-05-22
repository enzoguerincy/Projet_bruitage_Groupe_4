package Controle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import Abstraction.CollectionVecteur;
import Abstraction.ImageBruitee;
import Abstraction.Patch;
import Abstraction.Seuillage;
import Abstraction.Vecteur;
import javax.imageio.ImageIO;

public class MethodesController implements ControllerByMain {

    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane imageContainer;

    @FXML
    private Slider sliderV;

    @FXML
    private Label labelValeur;

    @FXML
    private ToggleButton btnLocal;

    @FXML
    private ToggleButton btnGlobal;

    @FXML
    private ToggleButton btnDoux;

    @FXML
    private ToggleButton btnDur;
    
    @FXML
    private Button btnVisu;
    
    @FXML
    private Button btnBayes;
    
    @FXML
    private Button btnDebruiter;

    private final String violetStyleLeft = "-fx-background-color: #ac89fc;"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 20 0 0 20;"
            + "-fx-border-radius: 20 0 0 20;";

    private final String blancStyleRight = "-fx-background-color: white;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 0 20 20 0;"
            + "-fx-border-radius: 0 20 20 0;";

    private final String violetStyleRight = "-fx-background-color: #ac89fc;"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 0 20 20 0;"
            + "-fx-border-radius: 0 20 20 0;";

    private final String blancStyleLeft = "-fx-background-color: white;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 20 0 0 20;"
            + "-fx-border-radius: 20 0 0 20;";

    @FXML
    public void initialize() {
        System.out.println("Page des méthodes chargée !");

        BufferedImage buffered = DataHolder.getImageBruitee();
        if (buffered == null) {
            buffered = DataHolder.getImageOriginale();
        }

        if (buffered != null) {
            Image fxImage = ImageBruitee.toFXImage(buffered);
            imageView.setImage(fxImage);
            imageView.setFitWidth(fxImage.getWidth());
            imageView.setFitHeight(fxImage.getHeight());
        }

        if (mainController != null) {
            mainController.setCurrentImageView(imageView);
            mainController.appliquerZoom();
            mainController.surlignerLabelPage("Méthode");
        }

        btnLocal.setOnAction(e -> {
            btnLocal.setStyle(violetStyleLeft);
            btnGlobal.setStyle(blancStyleRight);
            DataHolder.setModeSelectionne(DataHolder.Mode.LOCAL);
        });

        btnGlobal.setOnAction(e -> {
            btnGlobal.setStyle(violetStyleRight);
            btnLocal.setStyle(blancStyleLeft);
            DataHolder.setModeSelectionne(DataHolder.Mode.GLOBAL);
        });

        btnDoux.setOnAction(e -> {
            btnDoux.setStyle(violetStyleLeft);
            btnDur.setStyle(blancStyleRight);
            DataHolder.setSeuillageSelectionne(DataHolder.Seuillage.DOUX);
        });

        btnDur.setOnAction(e -> {
            btnDur.setStyle(violetStyleRight);
            btnDoux.setStyle(blancStyleLeft);
            DataHolder.setSeuillageSelectionne(DataHolder.Seuillage.DUR);
        });
        
        btnVisu.setOnAction(e -> {
        	DataHolder.setCalculSelectionne(DataHolder.Calcul.VISU);
        });
        
        btnBayes.setOnAction(e -> {
        	DataHolder.setCalculSelectionne(DataHolder.Calcul.BAYES);
        });

        sliderV.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelValeur.setText(String.valueOf(newVal.intValue()));
        });
    }
    
    private static List<Vecteur> cloneVecteurs(List<Vecteur> original) {
        return original.stream()
                .map(v -> new Vecteur(v.valeurs.clone()))
                .toList();
    }
    
    @FXML
    private void handleAllerResultat(ActionEvent event) {
        mainController.loadView("/Presentation/page_resultat.fxml");
        mainController.surlignerLabelPage("Résultat");
    }
    
    
    @FXML
    public void onDebruiter(ActionEvent event) throws IOException {
        System.out.println("Débruitage lancé !");
        System.out.println("Mode : " + DataHolder.getModeSelectionne());
        System.out.println("Seuillage : " + DataHolder.getSeuillageSelectionne());
        System.out.println("Calcul : " + DataHolder.getCalculSelectionne());
        int taillePatch = 8;
        double lambda;
        List<Vecteur> projDoux;
        List<Vecteur> projDur;
        List<Vecteur> projFinal;
        BufferedImage imageFinale;
        // Méthode Globale
        if (DataHolder.getModeSelectionne() == DataHolder.Mode.GLOBAL) {
        	List<Patch> patchs = ImageBruitee.extractPatchs(DataHolder.getImageBruitee(), taillePatch);
            List<Vecteur> vecteurs = ImageBruitee.vectorPatchs(patchs);
            CollectionVecteur cv = new CollectionVecteur(vecteurs);
            CollectionVecteur.MoyCovResult mcr = cv.MoyCov();
            List<Vecteur> base = CollectionVecteur.acp(vecteurs);
            double[][] baseMat = CollectionVecteur.toMatriceBase(base);

            List<Vecteur> projBase = cv.Proj(base, mcr.vecteursCentres);
            if (DataHolder.getCalculSelectionne() == DataHolder.Calcul.VISU) {
       		 lambda = Seuillage.calculSeuilVisuShrink(DataHolder.getNiveauBruitage(),DataHolder.getImageOriginale().getHeight() * DataHolder.getImageOriginale().getWidth());
            }else {
            	 lambda = Seuillage.calculSeuilBayesShrink(DataHolder.getNiveauBruitage()*DataHolder.getNiveauBruitage(), Seuillage.calculerVarianceXb(vecteurs));
            }
            // Doux
            if (DataHolder.getSeuillageSelectionne() == DataHolder.Seuillage.DOUX) {
            	projDoux = cloneVecteurs(projBase);
            	for (Vecteur v : projDoux) v.valeurs = Seuillage.seuillageDoux(lambda, v.valeurs);
            	projFinal = projDoux;
            // Dur
            } else {
            	projDur = cloneVecteurs(projBase);
            	for (Vecteur v : projDur) v.valeurs = Seuillage.seuillageDur(lambda, v.valeurs); 
            	projFinal = projDur;
            }
            List<Patch> rec = CollectionVecteur.reconstruirePatchsDepuisContributions(projFinal, baseMat, mcr.moyenne.valeurs, taillePatch, patchs);
            imageFinale =  ImageBruitee.reconstructPatchs(rec, DataHolder.getImageBruitee().getHeight(), DataHolder.getImageBruitee().getWidth());
        // Méthode Locale
        } else {
        	List<Patch> patchs = ImageBruitee.decoupeImage(DataHolder.getImageBruitee(), 32, 8); // blocs de 32x32
            List<Vecteur> vecteurs = ImageBruitee.vectorPatchs(patchs);
            CollectionVecteur cv = new CollectionVecteur(vecteurs);
            CollectionVecteur.MoyCovResult mcr = cv.MoyCov();
            List<Vecteur> base = CollectionVecteur.acp(vecteurs);
            double[][] baseMat = CollectionVecteur.toMatriceBase(base);

            List<Vecteur> projBase = cv.Proj(base, mcr.vecteursCentres);
            if (DataHolder.getCalculSelectionne() == DataHolder.Calcul.VISU) {
          		 lambda = Seuillage.calculSeuilVisuShrink(DataHolder.getNiveauBruitage(),DataHolder.getImageOriginale().getHeight() * DataHolder.getImageOriginale().getWidth());
            }else {
            	 lambda = Seuillage.calculSeuilBayesShrink(DataHolder.getNiveauBruitage()*DataHolder.getNiveauBruitage(), Seuillage.calculerVarianceXb(vecteurs));
            }
         // Doux
            if (DataHolder.getSeuillageSelectionne() == DataHolder.Seuillage.DOUX) {
            	projDoux = cloneVecteurs(projBase);
            	for (Vecteur v : projDoux) v.valeurs = Seuillage.seuillageDoux(lambda, v.valeurs);
            	projFinal = projDoux;
            // Dur
            } else {
            	projDur = cloneVecteurs(projBase);
            	for (Vecteur v : projDur) v.valeurs = Seuillage.seuillageDur(lambda, v.valeurs); 
            	projFinal = projDur;
            }
            List<Patch> rec = CollectionVecteur.reconstruirePatchsDepuisContributions(projFinal, baseMat, mcr.moyenne.valeurs, taillePatch, patchs);
            imageFinale = ImageBruitee.reconstructPatchs(rec, DataHolder.getImageBruitee().getHeight(), DataHolder.getImageBruitee().getWidth());
        }
        DataHolder.setImageDebruitee(imageFinale);
        System.out.println("Image débruitée avec succès.");
        System.out.println("Valeur du niveau de bruitage :" + DataHolder.getNiveauBruitage());
        System.out.println("Valeur du seuil : " + lambda);
        ImageIO.write(imageFinale, "jpeg", new File("image_finale/imageFinale.jpeg"));
        handleAllerResultat(event);
    }
    
}