package Controle;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import Abstraction.ImageBruitee;

public class BruitageController extends MenuController {

	@FXML private ImageView imageView;
	@FXML private StackPane imageContainer;
    @FXML private Slider sliderV;
    @FXML private Label labelValeur;
    

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

            sliderV.valueProperty().addListener((obs, oldVal, newVal) -> {
                labelValeur.setText(String.format("%.0f", newVal.doubleValue()));
            });
        }
        MenuController.getInstance().appliquerZoom();
        //MenuController menuController = this; // ou via stockage statique si nécessaire
        //menuController.surlignerLabelPage("bruitage");
    }


	@FXML
	private void handleRetourMenu(ActionEvent event) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/page_menu.fxml"));
	        Parent menuPage = loader.load();

	        // Récupère le BorderPane racine de la scène actuelle
	        BorderPane root = (BorderPane) imageView.getScene().getRoot();

	        // Remplace uniquement le contenu central (comme demandé)
	        Node centerContent = menuPage.lookup("#centerPane");
	        root.setCenter(centerContent);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	@FXML
	private void handleBruiter() {
	    BufferedImage inputBuffered = DataHolder.getImageOriginale();
	    if (inputBuffered == null) return;

	    double sigma = sliderV.getValue();
	    BufferedImage noisyBuffered = ImageBruitee.noising(inputBuffered, sigma);
	    Image noisyFx = ImageBruitee.toFXImage(noisyBuffered);

	    imageView.setImage(noisyFx);
	    DataHolder.setImageBruitee(noisyBuffered);
	}
	
	private void handleAllerMethodes(ActionEvent event) {
//	    System.out.println("➡ Navigation vers la page des méthodes");
//	    // Exemple : charger une nouvelle vue (à adapter si tu remplaces juste le center)
//	    try {
//	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/page_methodes.fxml"));
//	        Parent methodesPane = loader.load();
//
//	        // Remplacer le contenu du center uniquement
//	        BorderPane root = (BorderPane) imageView.getScene().getRoot();  // ou rootPane si injecté
//	        root.setCenter(methodesPane);
//
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
	}
}
