package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("BasicApplication_css\\BasicApplication_css.fxml"));

        root.getStylesheets().add(getClass().getResource("\\BasicApplication_css\\BasicApplication.css").toString());

        primaryStage.setTitle("KML Exporter");
        primaryStage.setScene(new Scene(root, 700, 420));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
