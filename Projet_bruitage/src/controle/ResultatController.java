package controle;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import javafx.scene.control.Label;
import java.awt.image.BufferedImage;

import abstraction.ImageBruitee;
import abstraction.ImageFinale;

public class ResultatController implements ControllerByMain {

	@FXML
	private ImageView imageBruiteeView;

	@FXML
	private ImageView imageDebruiteeView;

	@FXML
	private Slider sliderTransition;

	@FXML
	private StackPane clipContainer;

	@FXML
	private StackPane stackPane;

	@FXML
	private ComboBox<String> comboChoixComparaison;

	@FXML
	private Label labelPSNR;

	@FXML
	private Label labelSE;

	private Image imageBruitee;
	private Image imageOriginale;
	private Image imageDebruitee;

	private Rectangle clipRect;

	@SuppressWarnings("unused")
	private MainController mainController;

	/**
	 * Définit le contrôleur principal pour interaction globale.
	 * 
	 * @param mainController le contrôleur principal de l'application
	 */
	@Override
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Initialise le contrôleur et configure l'interface. Charge les images depuis
	 * le DataHolder, met à jour les vues, configure les listeners pour le slider et
	 * la ComboBox, et calcule les métriques PSNR et MSE.
	 */
	@FXML
	public void initialize() {
		BufferedImage bufferedBruitee = DataHolder.getImageBruitee();
		BufferedImage bufferedOriginale = DataHolder.getImageOriginale();
		BufferedImage bufferedDebruitee = DataHolder.getImageDebruitee();

		if (bufferedBruitee != null) {
			imageBruitee = ImageBruitee.toFXImage(bufferedBruitee);
		}

		if (bufferedOriginale != null) {
			imageOriginale = ImageBruitee.toFXImage(bufferedOriginale);
		}

		if (bufferedDebruitee != null) {
			imageDebruitee = ImageBruitee.toFXImage(bufferedDebruitee);
			imageDebruiteeView.setImage(imageDebruitee);
		}

		// Image par défaut : bruitée vs débruitée
		imageBruiteeView.setImage(imageBruitee);

		// Initialiser le rectangle de clipping
		clipRect = new Rectangle();
		imageDebruiteeView.setClip(clipRect);

		// Réagir aux changements de taille ou slider
		sliderTransition.valueProperty().addListener((obs, oldVal, newVal) -> updateClip());
		stackPane.widthProperty().addListener((obs, oldVal, newVal) -> updateClip());
		stackPane.heightProperty().addListener((obs, oldVal, newVal) -> updateClip());

		// Listener pour la ComboBox
		comboChoixComparaison.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				if (newVal.equals("Bruitée vs Débruitée")) {
					imageBruiteeView.setImage(imageBruitee);
				} else if (newVal.equals("Originale vs Débruitée")) {
					imageBruiteeView.setImage(imageOriginale);
				}
			}
		});

		// Sélection par défaut
		comboChoixComparaison.getSelectionModel().selectFirst();

		// Calcul et affichage du PSNR / MSE
		if (bufferedOriginale != null && bufferedDebruitee != null) {

			double mse = ImageFinale.mse(bufferedOriginale, bufferedDebruitee);
			double psnr = ImageFinale.psnr(mse);

			labelPSNR.setText(String.format("PSNR : %.2f dB", psnr));
			labelSE.setText(String.format("MSE : %.2f", mse));
		}

		updateClip();
	}

	/**
	 * Met à jour dynamiquement la largeur du rectangle de clipping en fonction de
	 * la valeur du slider et de la taille du conteneur. Crée un effet visuel
	 * permettant de comparer deux images côte à côte.
	 */
	private void updateClip() {
		double width = stackPane.getWidth();
		double height = stackPane.getHeight();
		double proportion = sliderTransition.getValue();

		clipRect.setWidth(width * proportion);
		clipRect.setHeight(height);
	}
}
