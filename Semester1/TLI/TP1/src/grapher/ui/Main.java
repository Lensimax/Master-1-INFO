package grapher.ui;

import grapher.fc.Function;
import grapher.fc.FunctionFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class Main extends Application {

    static final double width_bold = 3;
    static final double width_default = 1;

    public static ObservableList<Grapher_Function> list_function;



	public void start(Stage stage) {
        SplitPane split = new SplitPane();
        GrapherCanvas grapher = new GrapherCanvas(getParameters());
        list_function = creationFunction_list(grapher);



        MenuBarGrapher toolbar = new MenuBarGrapher(null, grapher);

        BorderPane root = new BorderPane();
        root.setCenter(split);
        root.setTop(toolbar);

        TableView<Grapher_Function> table_functions = creationTableView();



		// boutons add delete
        Button but_add = new Button("+");
//        but_add.setOnAction(new ButtonAddEvent_Handler(list_function, grapher));

        Button but_delete = new Button("-");
//        but_delete.setOnAction(new ButtonDeleteEvent_Handler(list_function, grapher));

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


    public TableView<Grapher_Function> creationTableView() {
        TableView<Grapher_Function> table_functions = new TableView<Grapher_Function>();

        TableColumn<Grapher_Function, Function> columnFunctions = new TableColumn<Grapher_Function, Function>("function");
        columnFunctions.setCellValueFactory(new PropertyValueFactory<Grapher_Function, Function>("function"));
        columnFunctions.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Function>() {
            @Override
            public String toString(Function object) {
                return object.toString();
            }

            @Override
            public Function fromString(String string) {
                Function func = FunctionFactory.createFunction(string);

                if(func != null){
                    return func;
                } else {
                    return null;
                }
            }
        }));

        columnFunctions.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Grapher_Function, Function>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Grapher_Function, Function> event) {
                final Function f = event.getNewValue();
                Grapher_Function col = event.getRowValue();
                col.setFunction(f);
                int index = event.getTablePosition().getRow();
                table_functions.getItems().set(index, col);
            }
        });

        // TODO a changer un peu


        TableColumn<Grapher_Function, ColorPicker> columnColorPicker = new TableColumn<Grapher_Function, ColorPicker>("color_picker");
        columnColorPicker.setCellValueFactory(new PropertyValueFactory<Grapher_Function, ColorPicker>("color_picker"));
//        columnColorPicker.setMaxWidth(100);

        table_functions.setEditable(true);
        table_functions.setItems(list_function);
//        table_functions.setMinWidth(250);



        table_functions.getColumns().addAll(columnFunctions, columnColorPicker);



        return table_functions;
    }
}