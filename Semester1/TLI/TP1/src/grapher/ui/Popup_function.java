package grapher.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Popup_function extends Stage {


    public Popup_function(ListView<Text> list_function, GrapherCanvas grapher){
        this.setHeight(100);
        this.setWidth(250);

        Stage popup = this;

        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: white; -fx-padding: 5px;");

        HBox input_func = new HBox();
        TextField textField = new TextField();
        Label label_func = new Label("Function:");

        input_func.getChildren().addAll(label_func, textField);

        ToolBar buttons = new ToolBar();
        buttons.setStyle("-fx-background-color: white;");


        Button submit = new Button("Submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                list_function.getItems().add(new Text(textField.getText()));
                grapher.add_function(textField.getText());
                grapher.redraw();

                popup.close();

            }
        });

        Button abort = new Button("Abort");
        abort.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                popup.close();

            }
        });


        buttons.getItems().addAll(abort,submit);

        page.setCenter(input_func);
        page.setBottom(buttons);
        this.setScene(new Scene(page));
        this.setTitle("Expression");
    }


}
