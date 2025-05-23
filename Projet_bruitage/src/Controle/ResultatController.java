package Controle;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;

import Abstraction.ImageBruitee;

public class ResultatController implements ControllerByMain {

    private MainController mainController;

    @FXML
    private Canvas canvas;

    @FXML
    private Slider comparaisonSlider;

    @FXML
    private Button boutonAjouter;

    @FXML
    private StackPane imageContainer;

    private Image imageOriginale;
    private Image imageBruitee;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        System.out.println("Page des résultats chargée !");

        BufferedImage originaleBuffered = DataHolder.getImageOriginale();
        BufferedImage bruieeBuffered = DataHolder.getImageBruitee();

        if (originaleBuffered != null && bruieeBuffered != null) {
            imageOriginale = ImageBruitee.toFXImage(originaleBuffered);
            imageBruitee = ImageBruitee.toFXImage(bruieeBuffered);
            System.out.println("Image originale : " + (imageOriginale != null));
            System.out.println("Image bruitée : " + (imageBruitee != null));
        }

        comparaisonSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            drawComparison(newVal.doubleValue());
        });

        if (mainController != null) {
            mainController.setCurrentCanvas(canvas);  // Connecte le zoom au canvas
            mainController.appliquerZoom();           // Applique le zoom si défini
            mainController.surlignerLabelPage("Résultat");
        }

        // Appliquer une première fois le rendu
        drawComparison(comparaisonSlider.getValue());
    }

    private void drawComparison(double ratio) {
        if (imageOriginale == null || imageBruitee == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double zoom = (mainController != null) ? mainController.getZoomValue() : 1.0;

        int imageWidth = (int) imageOriginale.getWidth();
        int imageHeight = (int) imageOriginale.getHeight();
        int scaledWidth = (int) (imageWidth * zoom);
        int scaledHeight = (int) (imageHeight * zoom);

        // Calcul des coordonnées pour centrer
        double offsetX = (canvas.getWidth() - scaledWidth) / 2;
        double offsetY = (canvas.getHeight() - scaledHeight) / 2;

        PixelReader readerOriginale = imageOriginale.getPixelReader();
        PixelReader readerBruitee = imageBruitee.getPixelReader();

        for (int y = 0; y < scaledHeight; y++) {
            for (int x = 0; x < scaledWidth; x++) {
                int srcX = (int) (x / zoom);
                int srcY = (int) (y / zoom);

                if (srcX >= imageWidth || srcY >= imageHeight) continue;

                if (x < scaledWidth * ratio) {
                    gc.getPixelWriter().setColor((int) offsetX + x, (int) offsetY + y,
                            readerOriginale.getColor(srcX, srcY));
                } else {
                    gc.getPixelWriter().setColor((int) offsetX + x, (int) offsetY + y,
                            readerBruitee.getColor(srcX, srcY));
                }
            }
        }
    }

    @FXML
    private void handleSliderChanged() {
        drawComparison(comparaisonSlider.getValue());
    }

    @FXML
    private void handleAjouterCollection() {
        System.out.println("Image ajoutée à la collection (à implémenter)");
        // 👉 Tu peux ici appeler un service ou sauvegarder dans une liste statique
    }
}