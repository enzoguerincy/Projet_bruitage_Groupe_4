package Controle;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

public class PageController extends BruitageController {

    @FXML
    private ToggleButton btnLocal;

    @FXML
    private ToggleButton btnGlobal;

    @FXML
    private ToggleButton btnDoux;

    @FXML
    private ToggleButton btnDur;
    
    @FXML
    private Slider sliderV;
    
    @FXML
    private javafx.scene.control.Label labelValeur;

    private final String violetStyleLeft = 
    	    "-fx-background-color: #ac89fc;" +
    	    "-fx-text-fill: white;" +
    	    "-fx-background-radius: 20 0 0 20;" +
    	    "-fx-border-color: #888;" +
    	    "-fx-border-radius: 20 0 0 20;" +
    	    "-fx-font-weight: bold;" +
    	    "-fx-font-size: 26px;";

    	private final String blancStyleRight = 
    	    "-fx-background-color: white;" +
    	    "-fx-text-fill: black;" +
    	    "-fx-background-radius: 0 20 20 0;" +
    	    "-fx-border-color: #888;" +
    	    "-fx-border-radius: 0 20 20 0;" +
    	    "-fx-font-weight: bold;" +
    	    "-fx-font-size: 26px;";

    	private final String violetStyleRight = 
    	    "-fx-background-color: #ac89fc;" +
    	    "-fx-text-fill: white;" +
    	    "-fx-background-radius: 0 20 20 0;" +
    	    "-fx-border-color: #888;" +
    	    "-fx-border-radius: 0 20 20 0;" +
    	    "-fx-font-weight: bold;" +
    	    "-fx-font-size: 26px;";

    	private final String blancStyleLeft = 
    	    "-fx-background-color: white;" +
    	    "-fx-text-fill: black;" +
    	    "-fx-background-radius: 20 0 0 20;" +
    	    "-fx-border-color: #888;" +
    	    "-fx-border-radius: 20 0 0 20;" +
    	    "-fx-font-weight: bold;" +
    	    "-fx-font-size: 26px;";




    	@FXML
		public void initialize() {
    		System.out.println("Page des méthodes chargée !");
            // Toggle styles
            btnLocal.setOnAction(e -> {
                btnLocal.setStyle(violetStyleLeft);
                btnGlobal.setStyle(blancStyleRight);
            });

            btnGlobal.setOnAction(e -> {
                btnGlobal.setStyle(violetStyleRight);
                btnLocal.setStyle(blancStyleLeft);
            });

            btnDoux.setOnAction(e -> {
                btnDoux.setStyle(violetStyleLeft);
                btnDur.setStyle(blancStyleRight);
            });

            btnDur.setOnAction(e -> {
                btnDur.setStyle(violetStyleRight);
                btnDoux.setStyle(blancStyleLeft);
            });

            // Slider listener → met à jour le label avec valeur entière
            sliderV.valueProperty().addListener((obs, oldVal, newVal) -> {
                labelValeur.setText(String.valueOf(newVal.intValue()));
            });
        }

}
