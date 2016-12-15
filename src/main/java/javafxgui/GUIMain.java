package javafxgui;

import cleargl.GLVector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sceneryfx.RectangleExample;
import sceneryfx.Model;


/**
 * Created by dibrov on 13/12/16.
 */
public class GUIMain extends Application {

    Button button;
    Stage window;
    Model model;

    private ChangeListener scr;
    Stage stage;


    public GUIMain(Model model) {
        this.model = model;


    }

    public static void main(String[] args) {
        //   System.out.println("Hello World!");
        launch(args);
    }

    public static void start(Model model) {
        new JFXPanel(); // initializes JavaFX environment

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Stage stage = new Stage();
                GUIMain main = new GUIMain(model);
                main.start(stage);
            }
        });

    }

    @Override
    public void start(Stage primaryStage) {

        // window
        window = primaryStage;
        window.setTitle("Set Acquisition");
        int windowSizeX = 400;
        int windowSizeY = 500;

        // close request handling
        window.setOnCloseRequest(e -> {
            e.consume();
            closeFunc();
        });

        // spinner X and Y
        Label spinnerTitle = new Label("Tile configuration:");
        Spinner<Integer> xSpinner = new Spinner<>(1, 10, 5);
       // xSpinner.valueProperty().addListener((r,p,q) -> model.changeX(q.intValue()));
        Label xLabel = new Label("x");
        Spinner<Integer> ySpinner = new Spinner<>(1, 10, 5);
       // ySpinner.valueProperty().addListener((r,p,q) -> model.changeY(q.intValue()));
        xSpinner.setMaxWidth(windowSizeX/4);
        ySpinner.setMaxWidth(windowSizeX/4);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {model.recalculate(xSpinner.valueProperty().get(), ySpinner
                .valueProperty().get());
            System.out.println("x and y are: " + xSpinner.valueProperty().get() + " " + ySpinner
                    .valueProperty().get());});

        // updater size sliders
        Label updaterSize = new Label("Updater Size:");

        Slider sizeXslider = new Slider();
        sizeXslider.setMin(0);
        sizeXslider.setMax(5);
        sizeXslider.setValue(2);
        sizeXslider.valueProperty().addListener(e -> model.getCursorBox().updateSizeX((float)sizeXslider.getValue()));

        Slider sizeYslider = new Slider();
        sizeYslider.setMin(0);
        sizeYslider.setMax(5);
        sizeYslider.setValue(2);
        sizeYslider.valueProperty().addListener(e -> model.getCursorBox().updateSizeY(
                (float)sizeYslider.getValue()));

        Slider sizeZslider = new Slider();
        sizeZslider.setMin(0);
        sizeZslider.setMax(5);
        sizeZslider.setValue(2);
        sizeZslider.valueProperty().addListener(e -> model.getCursorBox().updateSizeZ((float)sizeZslider.getValue()));

        // updater position sliders
        Label updaterPos = new Label("Updater Position:");

        Slider posXslider = new Slider();
        posXslider.setMin(-5);
        posXslider.setMax(5);
        posXslider.setValue(0);

        posXslider.valueProperty().addListener(e -> model.getCursorBox().updateX((float)posXslider.getValue()));

        Slider posYslider = new Slider();
        posYslider.setMin(-5);
        posYslider.setMax(5);
        posYslider.setValue(0);
        posYslider.valueProperty().addListener(e -> {model.getCursorBox().updateY((float)posYslider.getValue());
        System.out.println("y slider is; " + posYslider.getValue());});

        Slider posZslider = new Slider();
        posZslider.setMin(-5);
        posZslider.setMax(5);
        posZslider.setValue(0);
        posZslider.valueProperty().addListener(e -> model.getCursorBox().updateZ((float)posZslider.getValue()));

        // layout
        // main vbox
        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setSpacing(10);
        grid.setAlignment(Pos.TOP_CENTER);

        // placing spinners
        HBox spinners = new HBox();
        spinners.setPadding(new Insets(10, 10, 10, 10));
        spinners.setSpacing(8);
        spinners.getChildren().addAll(xSpinner, xLabel, ySpinner, updateButton);
        spinners.setAlignment(Pos.CENTER);

        // updater size sliders
        VBox updaterSizeBox = new VBox();
        updaterSizeBox.setSpacing(15);
        updaterSizeBox.setAlignment(Pos.TOP_CENTER);
        updaterSizeBox.getChildren().addAll(updaterSize, sizeXslider, sizeYslider, sizeZslider);


        // updater position sliders
        VBox updaterPosBox = new VBox();
        updaterPosBox.setSpacing(15);
        updaterPosBox.setAlignment(Pos.TOP_CENTER);
        updaterPosBox.getChildren().addAll(updaterPos, posXslider, posYslider, posZslider);


        grid.getChildren().addAll(spinnerTitle, spinners, updaterSizeBox, updaterPosBox);

        Scene scene = new Scene(grid, windowSizeX, windowSizeY);

        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

    }

    private void closeFunc() {
        System.out.println("about to close the window...");
        Boolean ans = ConfirmBox.display("Closing confirmation", "Do you want to leave the dialog?");
        if (ans)
            window.close();
    }


}
