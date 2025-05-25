package controle;

import static abstraction.Seuillage.calculSeuilBayesShrink;
import static abstraction.Seuillage.calculSeuilVisuShrink;
import static abstraction.Seuillage.calculerVarianceXb;

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

import javax.imageio.ImageIO;

import com.gluonhq.charm.glisten.control.Alert;

import abstraction.CollectionVecteur;
import abstraction.ImageBruitee;
import abstraction.Patch;
import abstraction.Seuillage;
import abstraction.Vecteur;

public class MethodesController implements ControllerByMain {

	private MainController mainController;

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

	private final String violetStyleLeft = "-fx-background-color: #ac89fc;" + "-fx-text-fill: white;"
			+ "-fx-font-weight: bold;" + "-fx-font-size: 18px;" + "-fx-background-radius: 20 0 0 20;"
			+ "-fx-border-radius: 20 0 0 20;";

	private final String blancStyleRight = "-fx-background-color: white;" + "-fx-text-fill: black;"
			+ "-fx-font-weight: bold;" + "-fx-font-size: 18px;" + "-fx-background-radius: 0 20 20 0;"
			+ "-fx-border-radius: 0 20 20 0;";

	private final String violetStyleRight = "-fx-background-color: #ac89fc;" + "-fx-text-fill: white;"
			+ "-fx-font-weight: bold;" + "-fx-font-size: 18px;" + "-fx-background-radius: 0 20 20 0;"
			+ "-fx-border-radius: 0 20 20 0;";

	private final String blancStyleLeft = "-fx-background-color: white;" + "-fx-text-fill: black;"
			+ "-fx-font-weight: bold;" + "-fx-font-size: 18px;" + "-fx-background-radius: 20 0 0 20;"
			+ "-fx-border-radius: 20 0 0 20;";

	
	@FXML
	public void initialize() {

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
			System.out.println("test");
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
			double sigma = DataHolder.getNiveauBruitage();
			BufferedImage img = DataHolder.getImageBruitee();
			if (img == null)
				img = DataHolder.getImageOriginale();

			if (img != null) {
				int nbPixels = img.getWidth() * img.getHeight();
				double seuil = calculSeuilVisuShrink(sigma, nbPixels);
				sliderV.setValue(seuil);
			} else {
				System.out.println("Aucune image trouvée !");
			}
			DataHolder.setCalculSelectionne(DataHolder.Calcul.VISU);
		});

		btnBayes.setOnAction(e -> {
			double sigma = DataHolder.getNiveauBruitage();
			double sigma2 = sigma * sigma;

			BufferedImage img = DataHolder.getImageBruitee();
			if (img == null)
				img = DataHolder.getImageOriginale();

			if (img != null) {
				List<Patch> patchs = ImageBruitee.extractPatchs4(img, 8);
				List<Vecteur> vecteurs = ImageBruitee.vectorPatchs(patchs);
				double varianceXb = calculerVarianceXb(vecteurs);
				double seuil = calculSeuilBayesShrink(sigma2, varianceXb);
				sliderV.setValue(seuil);
			}
			DataHolder.setCalculSelectionne(DataHolder.Calcul.BAYES);
		});

		sliderV.valueProperty().addListener((obs, oldVal, newVal) -> {
			labelValeur.setText(String.valueOf(newVal.intValue()));
			DataHolder.setCalculSelectionne(DataHolder.Calcul.SLIDER);
		});
	}

	private static List<Vecteur> cloneVecteurs(List<Vecteur> original) {
		return original.stream().map(v -> new Vecteur(v.valeurs.clone())).toList();
	}


	@FXML
	private void handleAllerResultat(ActionEvent event) {
		mainController.setMethodeChoisie(true);
		mainController.loadView("/presentation/page_resultat.fxml");
		mainController.surlignerLabelPage("Résultat");
	}

	@FXML
	public void onDebruiter(ActionEvent event) throws IOException {
		System.out.println("Débruitage lancé !");
		System.out.println("Mode : " + DataHolder.getModeSelectionne());
		System.out.println("Seuillage : " + DataHolder.getSeuillageSelectionne());
		System.out.println("Calcul : " + DataHolder.getCalculSelectionne());

		BufferedImage img = DataHolder.getImageBruitee();

		int taillePatch = 8;
		double lambda = 0.0;
		List<Vecteur> projFinal;
		BufferedImage imageFinale;

		List<Patch> patchs;
		List<Vecteur> vecteurs;

		if (DataHolder.getModeSelectionne() == DataHolder.Mode.GLOBAL) {
			patchs = ImageBruitee.extractPatchs4(img, taillePatch);
		} else {
			patchs = ImageBruitee.decoupeImage(img, 32, 8); // blocs de 32x32
		}

		vecteurs = ImageBruitee.vectorPatchs(patchs);
		CollectionVecteur cv = new CollectionVecteur(vecteurs);
		CollectionVecteur.MoyCovResult mcr = cv.moyCov();
		List<Vecteur> base = CollectionVecteur.acp(vecteurs);
		double[][] baseMat = CollectionVecteur.toMatriceBase(base);
		List<Vecteur> projBase = cv.proj(base, mcr.vecteursCentres);

		// Calcul du lambda
		switch (DataHolder.getCalculSelectionne()) {
		case VISU:
			lambda = Seuillage.calculSeuilVisuShrink(DataHolder.getNiveauBruitage(),
					DataHolder.getImageOriginale().getHeight() * DataHolder.getImageOriginale().getWidth());
			break;
		case BAYES:
			lambda = Seuillage.calculSeuilBayesShrink(Math.pow(DataHolder.getNiveauBruitage(), 2),
					Seuillage.calculerVarianceXb(vecteurs));
			break;
		case SLIDER:
			lambda = sliderV.getValue(); // On suppose que le slider modifie directement cette valeur
			break;
		}

		// Application du seuillage
		if (DataHolder.getSeuillageSelectionne() == DataHolder.Seuillage.DOUX) {
			List<Vecteur> projDoux = cloneVecteurs(projBase);
			for (Vecteur v : projDoux)
				v.valeurs = Seuillage.seuillageDoux(lambda, v.valeurs);
			projFinal = projDoux;
		} else {
			List<Vecteur> projDur = cloneVecteurs(projBase);
			for (Vecteur v : projDur)
				v.valeurs = Seuillage.seuillageDur(lambda, v.valeurs);
			projFinal = projDur;
		}

		// Reconstruction finale
		List<Patch> rec = CollectionVecteur.reconstruirePatchsDepuisContributions(projFinal, baseMat,
				mcr.moyenne.valeurs, taillePatch, patchs);
		imageFinale = ImageBruitee.reconstructPatchs(rec, img.getHeight(), img.getWidth());

		DataHolder.setImageDebruitee(imageFinale);
		System.out.println("Image débruitée avec succès.");
		System.out.println("Valeur du niveau de bruitage : " + DataHolder.getNiveauBruitage());
		System.out.println("Valeur du seuil (lambda) : " + lambda);

		ImageIO.write(imageFinale, "jpeg", new File("out/imageFinale.jpeg"));
		handleAllerResultat(event);
	}

}