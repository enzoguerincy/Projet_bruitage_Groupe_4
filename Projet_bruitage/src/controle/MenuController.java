package controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;

import abstraction.ImageBruitee;

public class MenuController implements ControllerByMain {

	private MainController mainController;

	/**
	 * Injecte une référence vers le contrôleur principal pour navigation et
	 * interactions globales.
	 *
	 * @param mainController le contrôleur principal de l'application
	 */
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	@FXML
	private StackPane imageContainer;
	@FXML
	private Button boutonInsererImage;
	@FXML
	private ImageView imageView;
	@FXML
	private Button boutonValiderImage;

	/**
	 * Méthode d'initialisation appelée automatiquement après chargement de la vue.
	 * Active ou désactive le bouton "Valider" selon la présence d'une image
	 * sélectionnée.
	 */
	@FXML
	private void initialize() {
		boutonValiderImage.setDisable(DataHolder.getImageSelectionnee() == null);
	}

	/**
	 * Gère l’action de l’utilisateur lorsqu’il clique sur "Insérer une image".
	 * Ouvre un sélecteur de fichiers, charge l’image choisie, l’affiche, et la
	 * stocke.
	 *
	 * @param event l’événement déclenché par le bouton
	 */
	@FXML
	private void handleInsererImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Sélectionner une image");
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

		File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());

		if (file != null) {
			Image image = new Image(file.toURI().toString());
			BufferedImage buffered = ImageBruitee.fromFXImage(image);

			DataHolder.setImageOriginale(buffered);
			DataHolder.setImageBruitee(null);

			imageView.setImage(image);
			imageView.setFitWidth(image.getWidth());
			imageView.setFitHeight(image.getHeight());

			boutonValiderImage.setDisable(false);

			mainController.setCurrentImageView(imageView);
			mainController.appliquerZoom();
		}
	}

	/**
	 * Gère l’action de l’utilisateur lorsqu’il clique sur "Valider l’image". Marque
	 * l’étape comme effectuée, passe à l'étape de bruitage et met à jour
	 * l’interface.
	 *
	 * @param event l’événement déclenché par le bouton
	 */
	@FXML
	private void handleValiderImage(ActionEvent event) {
		mainController.setInsertionEffectuee(true);
		mainController.loadView("/presentation/page_bruitage.fxml");
		mainController.surlignerLabelPage("Bruitage");
	}
}
