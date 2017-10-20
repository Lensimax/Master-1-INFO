package grapher.ui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ButtonAddEvent implements EventHandler<ActionEvent> {


    private ListView<Text> list_function;
    private GrapherCanvas grapher;
    private Stage stage;


    public ButtonAddEvent(Stage s, ListView l, GrapherCanvas g){
        this.list_function = l;
        this.grapher = g;
        this.stage = s;
    }

    // TODO skin à améliorer


    @Override
    public void handle(ActionEvent actionEvent) {

        Popup popup = new Popup();
        popup.setHeight(100);
        popup.setWidth(150);

        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: white; -fx-padding: 5px;");

        HBox input_func = new HBox();
        TextField textField = new TextField();
        Label label_func = new Label("Function:");

        input_func.getChildren().addAll(label_func, textField);

        ToolBar buttons = new ToolBar();
        buttons.setStyle("-fx-background-color: white;");

        Button close = new Button("Close");
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popup.hide();
            }
        });

        Button submit = new Button("Submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                list_function.getItems().add(new Text(textField.getText()));
                grapher.add_function(textField.getText());
                grapher.redraw();
                popup.hide();
            }
        });

        buttons.getItems().addAll(close, submit);

        page.setCenter(input_func);
        page.setBottom(buttons);
        popup.getContent().addAll(page);

        popup.show(this.stage);




    }
}
