package grapher.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;



public class ButtonDeleteEvent implements EventHandler<ActionEvent> {

    private ListView<Text> list_function;
    private GrapherCanvas grapher;


    public ButtonDeleteEvent(ListView l, GrapherCanvas g){
        this.list_function = l;
        this.grapher = g;
    }

    @Override
    public void handle(ActionEvent actionEvent) {

        ObservableList<Integer> tab = list_function.getSelectionModel().getSelectedIndices();

        for(int i: tab){
            list_function.getItems().remove(i);
            grapher.delete_function(i);
            grapher.redraw();
        }

    }
}
