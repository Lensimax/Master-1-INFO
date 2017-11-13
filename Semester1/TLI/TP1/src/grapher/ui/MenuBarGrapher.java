package grapher.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Text;


public class MenuBarGrapher extends MenuBar {

    private ListView<Text> list_function;
    private GrapherCanvas grapher;

    // Edition
    MenuItem settings;

    // View
    MenuItem zoom;
    MenuItem unzoom;

    // Expression
    MenuItem add;
    MenuItem delete;
    MenuItem change_color;
    MenuItem change_function;



    public MenuBarGrapher(ListView l, GrapherCanvas g){

        this.list_function = l;
        this.grapher = g;

        // File
        Menu file = new Menu("File");

        // Edition
        Menu edition = new Menu("Edition");
        settings = new MenuItem("Settings");
        edition.getItems().addAll(settings);

        // View
        Menu view = new Menu("View");
        zoom = new MenuItem("Zoom");
        unzoom = new MenuItem("Unzoom");

        view.getItems().addAll(zoom, unzoom);

        // Menu expressions
        Menu expression = new Menu("Expression");
        add = new MenuItem("Add");
        delete = new MenuItem("Delete");
        expression.getItems().addAll(add, delete);



        this.getMenus().addAll(file, edition, view, expression);

        // add event

        zoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.grapher.zoom_from_center(5);
            }
        });


        unzoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.grapher.zoom_from_center(-5);
            }
        });

        add.setOnAction(new ButtonAddEvent_Handler());
        add.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        
        delete.setOnAction(new ButtonDeleteEvent_Handler());
        delete.setAccelerator(KeyCombination.keyCombination("Ctrl+Backspace"));


    }
}
