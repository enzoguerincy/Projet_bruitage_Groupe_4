package presentation;

import controle.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
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

    public static void main(String[] args) {
        launch(args);
    }
}	