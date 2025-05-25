package controle;

import java.awt.image.BufferedImage;

import abstraction.ImageBruitee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class BruitageController implements ControllerByMain {

	private MainController mainController;

	@FXML
	private Button boutonMethodes;

	@FXML
	private ImageView imageView;

	@FXML
	private StackPane imageContainer;

	@FXML
	private Slider sliderV;

	@FXML
	private Label labelValeur;

	@FXML
	private BorderPane rootPane;

	/**
	 * Initialise le contrôleur avec le contrôleur principal de l'application.
	 * 
	 * @param mainController le contrôleur principal
	 */
	@Override
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
		postInit();
	}

	/**
	 * Initialisation supplémentaire après affectation du contrôleur principal.
	 * Associe l'imageView courante, applique le zoom, et met à jour l'interface.
	 */
	private void postInit() {
		if (mainController != null && imageView.getImage() != null) {
			mainController.setCurrentImageView(imageView);
			mainController.appliquerZoom();
			mainController.surlignerLabelPage("Bruitage");
		}
	}

	/**
	 * Initialise la vue : affiche l’image bruitée si elle existe, ou l’image
	 * originale. Lie le slider à l'affichage numérique de sa valeur.
	 */
	@FXML
	public void initialize() {
		BufferedImage buffered = DataHolder.getImageBruitee();
		boutonMethodes.setDisable(true);
		if (buffered == null) {
			buffered = DataHolder.getImageOriginale();
		}

		if (buffered != null) {
			Image fxImage = ImageBruitee.toFXImage(buffered);
			imageView.setImage(fxImage);
			imageView.setFitWidth(fxImage.getWidth());
			imageView.setFitHeight(fxImage.getHeight());

			if (mainController != null) {
				mainController.setCurrentImageView(imageView);
				mainController.appliquerZoom();
				mainController.surlignerLabelPage("Bruitage");
			}
		}

		sliderV.valueProperty().addListener((obs, oldVal, newVal) -> {
			labelValeur.setText(String.format("%.0f", newVal.doubleValue()));
		});
	}

	/**
	 * Applique un bruit gaussien à l’image originale selon la valeur du slider.
	 * Affiche l’image bruitée et active le bouton pour continuer vers les méthodes.
	 */
	@FXML
	private void handleBruiter() {
		BufferedImage inputBuffered = DataHolder.getImageOriginale();
		if (inputBuffered == null)
			return;

		double sigma = sliderV.getValue();
		BufferedImage noisyBuffered = ImageBruitee.noising(inputBuffered, sigma);
		Image noisyFx = ImageBruitee.toFXImage(noisyBuffered);

		imageView.setImage(noisyFx);
		DataHolder.setImageBruitee(noisyBuffered);
		DataHolder.setNiveauBruitage(sigma);

		if (mainController != null) {
			mainController.setCurrentImageView(imageView);
			mainController.appliquerZoom();
		}
		boutonMethodes.setDisable(false);

	}

	/**
	 * Gère le passage à la vue des méthodes de débruitage. Appelé lorsque
	 * l'utilisateur clique sur "Méthodes ->".
	 * 
	 * @param event L'événement de clic
	 */
	@FXML
	private void handleAllerMethodes(ActionEvent event) {
		mainController.setBruitageEffectue(true);
		mainController.loadView("/presentation/page_methodes.fxml");
		mainController.surlignerLabelPage("Méthode");
	}
}
