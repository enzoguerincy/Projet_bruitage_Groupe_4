package Controle;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ResultatController implements ControllerByMain {

	private MainController mainController;
	
	public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
	
	@FXML
	private ImageView imageView;

	public void initView() {
	    BufferedImage img = DataHolder.getImageDebruitee();
	    if (img != null) {
	        javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(img, null);
	        imageView.setImage(fxImage); 
	        imageView.setFitWidth(fxImage.getWidth());
	        imageView.setFitHeight(fxImage.getHeight());
	        mainController.setCurrentImageView(imageView);

	        System.out.println("Image chargée");
	    } else {
	        System.out.println("Aucune image à afficher.");
	    }
	}
	
	@FXML
    private void handleAllerInsertion(ActionEvent event) {
        mainController.loadView("/Presentation/page_menu.fxml");
        mainController.surlignerLabelPage("Insertion");
    }

}
