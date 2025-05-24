package Controle;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.awt.image.BufferedImage;

import Abstraction.ImageBruitee;

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

    private Rectangle clipRect;

    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        BufferedImage bufferedBruitee = DataHolder.getImageBruitee();
        BufferedImage bufferedDebruitee = DataHolder.getImageDebruitee();

        if (bufferedBruitee != null) {
            Image imageBruitee = ImageBruitee.toFXImage(bufferedBruitee);
            imageBruiteeView.setImage(imageBruitee);
        }

        if (bufferedDebruitee != null) {
            Image imageDebruitee = ImageBruitee.toFXImage(bufferedDebruitee);
            imageDebruiteeView.setImage(imageDebruitee);
        }

        // Initialiser le rectangle de clipping
        clipRect = new Rectangle();
        imageDebruiteeView.setClip(clipRect);

        // Mettre à jour le clipping en fonction du slider
        sliderTransition.valueProperty().addListener((obs, oldVal, newVal) -> updateClip());

        // Réagir aux changements de taille du StackPane (fenêtre redimensionnée)
        stackPane.widthProperty().addListener((obs, oldVal, newVal) -> updateClip());
        stackPane.heightProperty().addListener((obs, oldVal, newVal) -> updateClip());

        updateClip();
    }

    private void updateClip() {
        double width = stackPane.getWidth();
        double height = stackPane.getHeight();
        double proportion = sliderTransition.getValue();

        clipRect.setWidth(width * proportion);
        clipRect.setHeight(height);
    }
}