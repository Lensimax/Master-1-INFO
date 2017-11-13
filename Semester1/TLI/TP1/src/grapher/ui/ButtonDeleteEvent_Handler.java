package grapher.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;



public class ButtonDeleteEvent_Handler implements EventHandler<ActionEvent> {



    public ButtonDeleteEvent_Handler(){
    }

    @Override
    public void handle(ActionEvent actionEvent) {

        ObservableList<Integer> tab = Main.table_functions.getSelectionModel().getSelectedIndices();

        for(int i=tab.size()-1; i>=0; i--){
            Main.list_function.remove(i);
        }

    }
}
