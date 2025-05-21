package Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

import Abstraction.ImageBruitee;

public class MenuController {

	@FXML
	private StackPane imageContainer;
	@FXML
	private Button boutonInsererImage;
	@FXML
	private ImageView imageView;
	@FXML
	private Button boutonValiderImage;
	@FXML
	private VBox sideMenu;
	@FXML
	private BorderPane rootPane;
	@FXML
	private boolean menuVisible = true;
	@FXML
	private Label labelInsertion, labelBruitage, labelMethode, labelResultat;
	@FXML
	private Slider sliderZoom;
	@FXML
	private VBox zoomContainer;

	private static MenuController instance;

	public MenuController() {
		instance = this;
	}

	public static MenuController getInstance() {
		return instance;
	}

	@FXML
	private void initialize() {
		surlignerLabelPage("Insertion");
		boutonValiderImage.setDisable(DataHolder.getImageSelectionnee() == null);

		if (DataHolder.getImageSelectionnee() != null) {
			zoomContainer.setVisible(true);
			zoomContainer.setManaged(true);
		} else {
			zoomContainer.setVisible(false);
			zoomContainer.setManaged(false);
		}

		sliderZoom.valueProperty().addListener((obs, oldVal, newVal) -> {
			appliquerZoom(newVal.doubleValue());
		});

	}

	@FXML
	private void handleInsererImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Sélectionner une image");
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

		File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());

		if (file != null) {
			Image image = new Image(file.toURI().toString());
			BufferedImage buffered = ImageBruitee.fromFXImage(image); // conversion ici

			DataHolder.setImageOriginale(buffered);
			DataHolder.setImageBruitee(null); // reset

			imageView.setImage(image);
			appliquerZoom();
			imageView.setFitWidth(image.getWidth());
			imageView.setFitHeight(image.getHeight());

			// Activer le zoom
			zoomContainer.setVisible(true);
			zoomContainer.setManaged(true);

			// Reset zoom à 1
			sliderZoom.setValue(1.0);

			imageContainer.setStyle("-fx-border-color: transparent;");
			boutonValiderImage.setDisable(false);
		}
	}

	@FXML
	private void handleValiderImage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/page_bruitage.fxml"));
			Parent pageBruitageRoot = loader.load();

			// 👉 Récupère le BorderPane de la page menu
			// Et remplace seulement le contenu central
			BorderPane root = (BorderPane) imageContainer.getScene().getRoot();
			root.setCenter(pageBruitageRoot.lookup("#centerPane"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void toggleMenu(ActionEvent event) {
		if (menuVisible) {
			// Masquer avec transition
			TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), sideMenu);
			slideOut.setFromX(0);
			slideOut.setToX(-sideMenu.getWidth());
			slideOut.setOnFinished(e -> rootPane.setLeft(null));
			slideOut.play();
		} else {
			// Réafficher et animer vers sa position d'origine
			rootPane.setLeft(sideMenu);
			sideMenu.setTranslateX(-sideMenu.getWidth()); // Commence hors de l'écran
			TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), sideMenu);
			slideIn.setFromX(-sideMenu.getWidth());
			slideIn.setToX(0);
			slideIn.play();
		}
		menuVisible = !menuVisible;
	}

	public void surlignerLabelPage(String nomPage) {
		labelInsertion.setStyle("-fx-font-size: 16px;");
		labelBruitage.setStyle("-fx-font-size: 16px;");
		labelMethode.setStyle("-fx-font-size: 16px;");
		labelResultat.setStyle("-fx-font-size: 16px;");

		switch (nomPage) {
		case "Insertion":
			labelInsertion.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
			break;
		case "Bruitage":
			labelBruitage.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
			break;
		case "Méthode":
			labelMethode.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
			break;
		case "Résultat":
			labelResultat.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
			break;
		}
	}

	public void appliquerZoom() {
		if (imageView == null || sliderZoom == null || imageView.getImage() == null)
			return;

		double scale = sliderZoom.getValue();
		imageView.setFitWidth(imageView.getImage().getWidth() * scale);
		imageView.setFitHeight(imageView.getImage().getHeight() * scale);
	}

	public void appliquerZoom(double facteur) {
		Image image = imageView.getImage();
		if (image != null) {
			imageView.setFitWidth(image.getWidth() * facteur);
			imageView.setFitHeight(image.getHeight() * facteur);
		}
	}

}