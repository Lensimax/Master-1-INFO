package grapher.ui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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


    public ButtonAddEvent(ListView l, GrapherCanvas g){
        this.list_function = l;
        this.grapher = g;
    }


    @Override
    public void handle(ActionEvent actionEvent) {

        Popup_function popup = new Popup_function(this.list_function, this.grapher);

        popup.show();

    }
}
