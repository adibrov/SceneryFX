package sceneryfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



/**
 * Created by dibrov on 18/01/17.
 */
public class JavaFXPanel extends Application {

    public static void main(String[] args) {
        //   System.out.println("Hello World!");
        launch(args);

    }

    public JavaFXPanel()
    {

    }

    public static void start() {
        new JFXPanel(); // initializes JavaFX environment

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Stage stage = new Stage();
                JavaFXPanel lJavaFxPanel = new JavaFXPanel();
                lJavaFxPanel.start(stage);
            }
        });

    }

    @Override
    public void start(Stage primaryStage) {
        // window
        Stage window = primaryStage;
        window.setTitle("Set Acquisition");
        int windowSizeX = 400;
        int windowSizeY = 400;

        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setSpacing(10);
        grid.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(grid, windowSizeX, windowSizeY);

        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        System.out.println();

    }

}
