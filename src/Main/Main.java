package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author Illuminate
 */
public class Main extends Application {
    private static Stage stg;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../View/Auth/Login.fxml"));
        
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("E-LITE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void render(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
