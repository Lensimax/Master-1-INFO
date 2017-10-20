package grapher.ui;

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
        for(Text t: list_function.getSelectionModel().getSelectedItems()){
            list_function.getItems().remove(t);
            // TODO remove du repere
            grapher.delete_function(t.getText());
            grapher.redraw();
        }
    }
}
