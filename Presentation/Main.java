package Presentation;

import Controle.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        // Charge la première vue (menu par exemple)
        controller.loadView("/Presentation/page_menu.fxml");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Débruitage ACP");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
