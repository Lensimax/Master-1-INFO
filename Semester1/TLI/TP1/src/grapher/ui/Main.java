package grapher.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;



public class Main extends Application {

    static final double width_bold = 3;
    static final double width_default = 1;



	public void start(Stage stage) {
		SplitPane split = new SplitPane();
        GrapherCanvas grapĥer = new GrapherCanvas(getParameters());

		StackPane root = new StackPane();
		root.getChildren().addAll(split);


		BorderPane repere = new BorderPane();
		repere.setCenter(grapĥer);

		// liste de fonctions
		ListView<Text> list_function = new ListView<>();
		list_function.setStyle("-fx-alignment: center; -fx-background-color: white;");
        list_function.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
                    grapĥer.change_width_function(i, width_bold);
                } else {
                    fontw = FontWeight.NORMAL;
                    grapĥer.change_width_function(i, width_default);
                }

                t.setFont(Font.font(old_font.getName(), fontw, old_font.getSize()));
            }
            grapĥer.redraw();

        });


        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.getItems().add(new Text(param));
        }

		// boutons add delete
        Button but_add = new Button("+");
        but_add.setOnAction(new ButtonAddEvent(stage, list_function, (GrapherCanvas)repere.getCenter()));

        Button but_delete = new Button("-");
        but_delete.setOnAction(new ButtonDeleteEvent(list_function, (GrapherCanvas)repere.getCenter()));

        ToolBar buttons = new ToolBar();
        buttons.getItems().addAll(but_add,new Separator(), but_delete);
        buttons.setStyle("-fx-padding: 5px;");

        BorderPane functions = new BorderPane();


        functions.setBottom(buttons);
        functions.setCenter(list_function);

		split.getItems().addAll(functions, repere);
		split.setDividerPositions(0.2);


		
		stage.setTitle("Grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}


}