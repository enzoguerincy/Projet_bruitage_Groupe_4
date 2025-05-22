package Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import Abstraction.ImageBruitee;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuController implements ControllerByMain {

    private MainController mainController;

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

    @FXML
    private void initialize() {
        boutonValiderImage.setDisable(DataHolder.getImageSelectionnee() == null);
    }

    @FXML
    private void handleInsererImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

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

            // Si tu veux appliquer le zoom géré par le MainController
            mainController.setCurrentImageView(imageView);
            mainController.appliquerZoom(); // Avec facteur sliderZoom actuel
        }
    }

    @FXML
    private void handleValiderImage(ActionEvent event) {
        mainController.loadView("/Presentation/page_bruitage.fxml");
        mainController.surlignerLabelPage("Bruitage");
    }
}
