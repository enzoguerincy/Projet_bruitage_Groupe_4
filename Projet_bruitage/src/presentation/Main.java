package presentation;

import controle.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
	/**
     * Point d'entrée de l'application JavaFX.
     * Charge la scène principale à partir du fichier FXML, récupère le contrôleur principal,
     * charge la vue initiale et configure la fenêtre principale.
     * 
     * @param primaryStage la fenêtre principale de l'application
     * @throws Exception si une erreur survient lors du chargement du fichier FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        controller.loadView("/presentation/page_menu.fxml");
        controller.surlignerLabelPage("Insertion");

        Scene scene = new Scene(root);
        primaryStage.setHeight(1000); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("Débruitage ACP");
        primaryStage.show();
    }

    /**
     * Méthode principale qui lance l'application JavaFX.
     * 
     * @param args les arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch(args);
    }
}	
