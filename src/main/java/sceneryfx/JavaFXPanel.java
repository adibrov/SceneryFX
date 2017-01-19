package sceneryfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;


/**
 * Created by dibrov on 18/01/17.
 */
public class JavaFXPanel extends Application {

    private Viewer mViewer;

    public static void main(String[] args) {
        //   System.out.println("Hello World!");
        launch(args);

    }

    public JavaFXPanel(Viewer pViewer) {
        this.mViewer = pViewer;
    }

    public static void start(Viewer pViewer) {
        new JFXPanel(); // initializes JavaFX environment

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Stage stage = new Stage();
                JavaFXPanel lJavaFxPanel = new JavaFXPanel(pViewer);
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

        Button lAcquireButton = new Button("Acquire");
        lAcquireButton.setOnAction(e -> {
            StackSelectionLabel ssl = mViewer.getStackSelectionLabel();
            System.out.println("Acquire click");
            System.out.println("ssl test: " +ssl.activeLabelExists());
            if (ssl.activeLabelExists()) {
                Rectangle rect = mViewer.getStackSelectionLabel().getRectangle();
                int z = ssl.getFirstLabelTimePointIndex();
                int lastZ = ssl.getLastLabelTimepointIndex();
                System.out.println("z to begin: " + z);
                System.out.println("z ti end: " + lastZ);
                int[] loc = new int[] {rect.x, rect.y, z};
                AcquisitionUnit au = new AcquisitionUnit(loc);
                mViewer.getAcquisitionModel().addAcquisitionUnitWithLabel(au, rect, loc, lastZ);
                System.out.println("inside");
                ssl.resetLabel();


            }
        });

        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setSpacing(10);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.getChildren().add(lAcquireButton);

        Scene scene = new Scene(grid, windowSizeX, windowSizeY);

        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        System.out.println();

    }

}
