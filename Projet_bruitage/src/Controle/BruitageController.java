package Controle;

import java.awt.image.BufferedImage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import Abstraction.ImageBruitee;

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

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        postInit(); // On appelle l'initialisation dépendante ici
    }

    private void postInit() {
        if (mainController != null && imageView.getImage() != null) {
            mainController.setCurrentImageView(imageView);
            mainController.appliquerZoom();
            mainController.surlignerLabelPage("Bruitage");
        }
    }
    
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

            // ⚙️ Zoom dynamique via MainController
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

        // Actualise l’image à zoomer
        if (mainController != null) {
            mainController.setCurrentImageView(imageView);
            mainController.appliquerZoom();
        }
        boutonMethodes.setDisable(false);

    }

    @FXML
    private void handleAllerMethodes(ActionEvent event) {
    	mainController.setBruitageEffectue(true);
        mainController.loadView("/Presentation/page_methodes.fxml");
        mainController.surlignerLabelPage("Méthode");
    }
}
