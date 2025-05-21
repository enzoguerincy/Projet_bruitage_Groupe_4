package Presentation;
import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class Main extends Application {
	
	 @Override
	    public void start(Stage primaryStage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("page_methodes.fxml"));
	        primaryStage.setTitle("Ma fenêtre JavaFX");
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
