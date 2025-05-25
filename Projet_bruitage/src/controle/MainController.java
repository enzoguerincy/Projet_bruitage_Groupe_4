package controle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;

public class MainController {

	@FXML
	private BorderPane root;
	@FXML
	private VBox sideMenu;
	@FXML
	private VBox zoomContainer;
	@FXML
	private Slider sliderZoom;
	@FXML
	private Label labelInsertion, labelBruitage, labelMethode, labelResultat;

	private boolean insertionEffectuee = false;
	private boolean bruitageEffectue = false;
	private boolean methodeChoisie = false;

	private boolean menuVisible = true;
	private ImageView currentImageView = null;

	/**
	 * Charge dynamiquement une vue FXML et connecte son contrôleur au
	 * MainController.
	 *
	 * @param fxmlPath chemin du fichier FXML (relatif au dossier ressources)
	 */
	public void loadView(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Node view = loader.load();

			Object controller = loader.getController();
			if (controller instanceof ControllerByMain) {
				((ControllerByMain) controller).setMainController(this);
			}

			root.setCenter(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialise le contrôleur : applique le zoom à la valeur actuelle du slider.
	 */
	@FXML
	private void initialize() {
		if (sliderZoom != null) {
			sliderZoom.valueProperty().addListener((obs, oldVal, newVal) -> {
				appliquerZoom(newVal.doubleValue());
			});
		}
	}

	/**
	 * Affiche ou masque le menu latéral gauche.
	 */
	@FXML
	private void toggleMenu() {
		if (menuVisible) {
			root.setLeft(null);
		} else {
			root.setLeft(sideMenu);
		}
		menuVisible = !menuVisible;
	}

	/**
	 * Met en gras le label de la page actuellement affichée.
	 *
	 * @param nomPage le nom de la page : "Insertion", "Bruitage", "Méthode",
	 *                "Résultat"
	 */
	public void surlignerLabelPage(String nomPage) {
		labelInsertion.setStyle("-fx-font-size: 16px;");
		labelBruitage.setStyle("-fx-font-size: 16px;");
		labelMethode.setStyle("-fx-font-size: 16px;");
		labelResultat.setStyle("-fx-font-size: 16px;");

		switch (nomPage) {
		case "Insertion" -> labelInsertion.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		case "Bruitage" -> labelBruitage.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		case "Méthode" -> labelMethode.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		case "Résultat" -> labelResultat.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		}
	}

	public void setInsertionEffectuee(boolean value) {
		this.insertionEffectuee = value;
	}

	public void setBruitageEffectue(boolean value) {
		this.bruitageEffectue = value;
	}

	public void setMethodeChoisie(boolean value) {
		this.methodeChoisie = value;
	}

	/**
	 * Affiche une boîte d'alerte avec un message.
	 *
	 * @param titre   le titre de l’alerte
	 * @param message le message affiché à l’utilisateur
	 */
	private void showAlert(String titre, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(titre);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// ----- Navigation entre les pages -----

	@FXML
	private void goToInsertion() {
		loadView("/presentation/page_menu.fxml");
		surlignerLabelPage("Insertion");
	}

	@FXML
	private void goToBruitage() {
		if (!insertionEffectuee) {
			showAlert("Étape requise", "Veuillez d'abord insérer une image avant d'effectuer le bruitage.");
			return;
		}
		loadView("/presentation/page_bruitage.fxml");
		surlignerLabelPage("Bruitage");
	}

	@FXML
	private void goToMethode() {
		if (!bruitageEffectue) {
			showAlert("Étape requise", "Veuillez d'abord effectuer le bruitage avant de choisir une méthode.");
			return;
		}
		loadView("/presentation/page_methodes.fxml");
		surlignerLabelPage("Méthode");
	}

	@FXML
	private void goToResultat() {
		if (!methodeChoisie) {
			showAlert("Étape requise", "Veuillez d'abord choisir une méthode avant de voir les résultats.");
			return;
		}
		loadView("/presentation/page_resultat.fxml");
		surlignerLabelPage("Résultat");
	}

	/**
	 * Définit l’image actuellement affichée à zoomer.
	 *
	 * @param imageView l’image JavaFX à gérer
	 */
	public void setCurrentImageView(ImageView imageView) {
		this.currentImageView = imageView;
		appliquerZoom();
	}

	private Canvas currentCanvas = null;

	/**
	 * Définit la zone de dessin actuelle (Canvas) à zoomer.
	 *
	 * @param canvas le Canvas à zoomer
	 */
	public void setCurrentCanvas(Canvas canvas) {
		this.currentCanvas = canvas;
		appliquerZoomCanvas();
	}

	/**
	 * Applique le zoom actuel au Canvas.
	 */
	public void appliquerZoomCanvas() {
		if (sliderZoom == null || currentCanvas == null)
			return;

		double scale = sliderZoom.getValue();
		currentCanvas.setScaleX(scale);
		currentCanvas.setScaleY(scale);
	}

	/**
	 * Retourne la valeur actuelle du zoom.
	 * 
	 * @return valeur du slider (facteur de zoom)
	 */
	public double getZoomValue() {
		return sliderZoom != null ? sliderZoom.getValue() : 1.0;
	}

	/**
	 * Applique le zoom selon la valeur actuelle du slider sur l’image courante.
	 */
	public void appliquerZoom() {
		if (sliderZoom == null || currentImageView == null || currentImageView.getImage() == null)
			return;

		double scale = sliderZoom.getValue();
		currentImageView.setFitWidth(currentImageView.getImage().getWidth() * scale);
		currentImageView.setFitHeight(currentImageView.getImage().getHeight() * scale);
	}

	/**
	 * Applique un zoom selon un facteur donné sur l’image courante.
	 *
	 * @param facteur facteur multiplicatif du zoom
	 */
	public void appliquerZoom(double facteur) {
		if (currentImageView != null && currentImageView.getImage() != null) {
			currentImageView.setFitWidth(currentImageView.getImage().getWidth() * facteur);
			currentImageView.setFitHeight(currentImageView.getImage().getHeight() * facteur);
		}
	}

}
