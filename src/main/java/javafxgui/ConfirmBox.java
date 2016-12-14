package javafxgui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by dibrov on 13/12/16.
 */
public class ConfirmBox {


        private static boolean asnwer;

        public static boolean display(String title, String message) {
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            window.setMinWidth(200);

            Label label1 = new Label();
            label1.setText(message);

            Button yesButton = new Button("Yes");
            yesButton.setOnAction(e -> {
                asnwer = true;
                window.close();
            });

            Button noButton = new Button("No");
            noButton.setOnAction(e -> {
                asnwer = false;
                window.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label1, yesButton, noButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

            return asnwer;

        }


}
