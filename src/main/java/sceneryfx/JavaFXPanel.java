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
import net.imglib2.realtransform.AffineTransform3D;

import java.awt.*;

import static tools.RectangleTransform.transformRectangle;


/**
 * Created by dibrov on 18/01/17.
 */
public class JavaFXPanel extends Application {

    private Viewer mViewer;
    private ClearVolumeUnit mCVu;

    public static void main(String[] args) {
        //   System.out.println("Hello World!");
        launch(args);

    }

    public JavaFXPanel(Viewer pViewer, ClearVolumeUnit pCVu) {
        this.mViewer = pViewer;
        this.mCVu = pCVu;
    }

    public static void start(Viewer pViewer, ClearVolumeUnit pCVu) {
        new JFXPanel(); // initializes JavaFX environment

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Stage stage = new Stage();
                JavaFXPanel lJavaFxPanel = new JavaFXPanel(pViewer, pCVu);
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
            System.out.println("ssl test: " + ssl.activeLabelExists());
            if (ssl.activeLabelExists()) {
                Rectangle rect = mViewer.getStackSelectionLabel().getRealRectangle();
                int z = ssl.getFirstLabelTimePointIndex();
                int lastZ = ssl.getLastLabelTimepointIndex();
                System.out.println("z to begin: " + z);
                System.out.println("z ti end: " + lastZ);
                long[] loc = new long[]{rect.x, rect.y, z};

                AcquisitionUnit au = AcquisitionUnit.getAcquisitionUnitWithRandomSubstack(loc, new long[]{rect.width - 1,
                        rect
                                .height - 1, lastZ - z + 1});

                AffineTransform3D lAffine3D = new AffineTransform3D();
                mViewer.getBdv().getBdvHandle().getViewerPanel().getState
                        ().getViewerTransform(lAffine3D);
                mViewer.getAcquisitionModel().addAcquisitionUnitWithLabel(au, transformRectangle(mViewer
                                .getStackSelectionLabel().getRectangle(), lAffine3D.inverse())
                        , loc,
                        lastZ);
                System.out.println("inside");
                ssl.resetLabel();
                mViewer.getAcquisitionModel().setSubstack(loc, au.getSubstack());
                mViewer.getBdv().getBdvHandle().getViewerPanel().requestRepaint();


            }
        });

        Button lCVButton = new Button("Show in CV");
        lCVButton.setOnAction(e -> {
            Rectangle rect = mViewer.getSelectedLabel();
            if (rect == null) {
                System.out.println("nothing to show");
            } else {
                mCVu.initializeAndShow(mViewer.getAcquisitionModel().getLabelRectangleMap().get(rect).getSubstack());
            }
        });

        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setSpacing(10);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.getChildren().addAll(lAcquireButton, lCVButton);

        Scene scene = new Scene(grid, windowSizeX, windowSizeY);

        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        System.out.println();

    }

}
