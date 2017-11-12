package grapher.ui;

import grapher.fc.Function;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;



public class Main extends Application {

    static final double width_bold = 3;
    static final double width_default = 1;



	public void start(Stage stage) {
		SplitPane split = new SplitPane();
        GrapherCanvas grapher = new GrapherCanvas(getParameters());
        ObservableList<Grapher_Function> list_function = creationFunction_list(grapher);

        TableColumn functions = new TableColumn("Functions");
        functions.setMinWidth(100);
        functions.setCellValueFactory(new PropertyValueFactory<Grapher_Function, Function>("function"));


        TableCell<ColorPicker, Color> color = new TableCell<>();
        color.setMinWidth(50);
//        color.setCellValueFactory(new PropertyValueFactory<Grapher_Function, ColorPicker>("color"));


        TableView<Grapher_Function> table_functions = new TableView<>();
        table_functions.setEditable(true);
        table_functions.setItems(list_function);



        table_functions.getColumns().add(0,functions);
//        table_functions.getColumns().add(1,color);





        MenuBarGrapher toolbar = new MenuBarGrapher(null, grapher);

        BorderPane root = new BorderPane();
        root.setCenter(split);
        root.setTop(toolbar);





		// boutons add delete
        Button but_add = new Button("+");
//        but_add.setOnAction(new ButtonAddEvent(list_function, grapher));

        Button but_delete = new Button("-");
//        but_delete.setOnAction(new ButtonDeleteEvent(list_function, grapher));

        ToolBar buttons = new ToolBar();
        buttons.getItems().addAll(but_add,new Separator(), but_delete);
        buttons.setStyle("-fx-padding: 5px;");

        BorderPane left_splitview = new BorderPane();


        left_splitview.setBottom(buttons);
        left_splitview.setCenter(table_functions);

		split.getItems().addAll(left_splitview, grapher);
		split.setDividerPositions(0.2);


		stage.setTitle("Grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}


    public ObservableList<Grapher_Function> creationFunction_list(GrapherCanvas grapher){

        // liste de fonctions
        ObservableList<Grapher_Function>list_function = FXCollections.observableArrayList();

        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.add(new Grapher_Function(param));
        }


        return list_function;
    }





	/*public ObservableList<Grapher_Function> creationListFunction(GrapherCanvas grapher){

        // liste de fonctions
        ObservableList<Grapher_Function>list_function = FXCollections.observableArrayList();

        list_function.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {


            // back to normal
            Font old_font;
            Text t;
            FontWeight fontw;
            ObservableList<Integer> obs_tab = list_function.getSelectionModel().getSelectedIndices();


            for(int i=0; i<list_function.getItems().size(); i++){
                t = list_function.getItems().get(i);
                old_font = t.getFont();

                if(obs_tab.contains(i)){
                    fontw = FontWeight.BOLD;
                    grapher.change_width_function(i, width_bold);
                } else {
                    fontw = FontWeight.NORMAL;
                    grapher.change_width_function(i, width_default);
                }

                t.setFont(Font.font(old_font.getName(), fontw, old_font.getSize()));
            }
            grapher.redraw();

        });


        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.getItems().add(new Text(param));
        }

        return list_function;
    }

*/

}