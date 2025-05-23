package Controle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;

public class MainController {

    @FXML private BorderPane root;
    @FXML private VBox sideMenu;
    @FXML private VBox zoomContainer;
    @FXML private Slider sliderZoom;
    @FXML private Label labelInsertion, labelBruitage, labelMethode, labelResultat;

    private boolean menuVisible = true;
    private ImageView currentImageView = null;

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
    
    @FXML
    private void initialize() {
        if (sliderZoom != null) {
            sliderZoom.valueProperty().addListener((obs, oldVal, newVal) -> {
                appliquerZoom(newVal.doubleValue());
            });
        }
    }


    @FXML
    private void toggleMenu() {
        if (menuVisible) {
            root.setLeft(null);
        } else {
            root.setLeft(sideMenu);
        }
        menuVisible = !menuVisible;
    }

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

    public void setCurrentImageView(ImageView imageView) {
        this.currentImageView = imageView;
        appliquerZoom();
    }
    
    private Canvas currentCanvas = null;

    public void setCurrentCanvas(Canvas canvas) {
        this.currentCanvas = canvas;
        appliquerZoomCanvas();
    }

    public void appliquerZoomCanvas() {
        if (sliderZoom == null || currentCanvas == null)
            return;

        double scale = sliderZoom.getValue();
        currentCanvas.setScaleX(scale);
        currentCanvas.setScaleY(scale);
    }
    
    public double getZoomValue() {
        return sliderZoom != null ? sliderZoom.getValue() : 1.0;
    }
    

    public void appliquerZoom() {
        if (sliderZoom == null || currentImageView == null || currentImageView.getImage() == null)
            return;

        double scale = sliderZoom.getValue();
        currentImageView.setFitWidth(currentImageView.getImage().getWidth() * scale);
        currentImageView.setFitHeight(currentImageView.getImage().getHeight() * scale);
    }

    public void appliquerZoom(double facteur) {
        if (currentImageView != null && currentImageView.getImage() != null) {
            currentImageView.setFitWidth(currentImageView.getImage().getWidth() * facteur);
            currentImageView.setFitHeight(currentImageView.getImage().getHeight() * facteur);
        }
    }

}
