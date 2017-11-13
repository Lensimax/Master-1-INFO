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

public class ButtonAddEvent_Handler implements EventHandler<ActionEvent> {



    public ButtonAddEvent_Handler(){
    }


    @Override
    public void handle(ActionEvent actionEvent) {

        Popup_function popup = new Popup_function();
        popup.show();

    }
}
