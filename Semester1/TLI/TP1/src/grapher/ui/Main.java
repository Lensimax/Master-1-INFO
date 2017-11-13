package grapher.ui;

import grapher.fc.Function;
import grapher.fc.FunctionFactory;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.util.StringConverter;


public class Main extends Application {

    static final double width_bold = 3;
    static final double width_default = 1;

    public static ObservableList<Grapher_Type> list_function;
    public static GrapherCanvas grapher;
    public static TableView<Grapher_Type> table_functions;


	public void start(Stage stage) {
        SplitPane split = new SplitPane();
        grapher = new GrapherCanvas(getParameters());
        list_function = creationFunction_list(grapher);






        MenuBarGrapher toolbar = new MenuBarGrapher(null, grapher);

        BorderPane root = new BorderPane();
        root.setCenter(split);
        root.setTop(toolbar);

        table_functions = creationTableView();



		// boutons add delete
        Button but_add = new Button("+");
        but_add.setOnAction(new ButtonAddEvent_Handler());

        Button but_delete = new Button("-");
        but_delete.setOnAction(new ButtonDeleteEvent_Handler());

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


    public ObservableList<Grapher_Type> creationFunction_list(GrapherCanvas grapher){

        // liste de fonctions
        ObservableList<Grapher_Type>list_function = FXCollections.observableArrayList();

        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.add(new Grapher_Type(param));
        }

        return list_function;
    }


    /// creation de la tableView

    public TableView<Grapher_Type> creationTableView() {
        TableView<Grapher_Type> table_functions = new TableView<Grapher_Type>();


        /// COLONNE DES FONCTIONS
        TableColumn<Grapher_Type, Function> columnFunctions = new TableColumn<Grapher_Type, Function>("function");
        columnFunctions.setCellValueFactory(new PropertyValueFactory<Grapher_Type, Function>("function"));

        // pour pouvoir editer les cellules
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

        // pour commit les changements effectués
        columnFunctions.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Grapher_Type, Function>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Grapher_Type, Function> event) {
                final Function f = event.getNewValue();
                Grapher_Type column = event.getRowValue();
                column.setFunction(f); // mise a jour function
                int index = event.getTablePosition().getRow();
                table_functions.getItems().set(index, column); // ajout a la table
            }
        });


        /// COLONNE DES COLORPICKER
        TableColumn<Grapher_Type, ColorPicker> columnColorPicker = new TableColumn<Grapher_Type, ColorPicker>("color_picker");
        columnColorPicker.setCellValueFactory(new PropertyValueFactory<Grapher_Type, ColorPicker>("color_picker"));
        // le changement des colorPicker est fait dans le handler setOnAction de chaque colorPicker (Grapher_Type)

        table_functions.setEditable(true);
        table_functions.setItems(list_function);

        // pour mettre a jour le grapher canvas quand on change un truc de la table
        table_functions.getItems().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                grapher.redraw();
            }
        });

        table_functions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // mettre en gras les courbes
        table_functions.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ObservableList<Integer> obs_tab = table_functions.getSelectionModel().getSelectedIndices();

                for(int i=0; i<list_function.size(); i++){
                    if(obs_tab.contains(i)){
                        list_function.get(i).setLineWidth(width_bold);
                    } else {
                        list_function.get(i).setLineWidth(width_default);
                    }
                }

                grapher.redraw();

            }
        });


        table_functions.getColumns().addAll(columnFunctions, columnColorPicker);


        return table_functions;
    }
}