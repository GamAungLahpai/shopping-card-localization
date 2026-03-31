package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/FXML/layout.fxml")
        );

        Scene scene = new Scene(loader.load(), 460, 580);

        stage.setTitle("TWE HE GAM AUNG/ Shopping Cart");
        stage.setScene(scene);
        stage.show();
    }
}
