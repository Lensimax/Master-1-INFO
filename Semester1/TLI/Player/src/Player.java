

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Player extends Application {

    final double heightWindow = 34;
    final double heightMinWindow = 50;

    static TreeTableView treeTableView;



    @Override
    public void start(Stage primaryStage) throws Exception{

        creationTreeTableView();

        BorderPane root = new BorderPane();
//        root.setCenter(treeTableView);
        BorderPane Top = new BorderPane();
        root.setTop(Top);

        //left Top
        VBox containerHBOX = new VBox();

        HBox containerTop = new HBox();
        containerTop.getChildren().addAll(new Button("<<"), new Button(">"), new Button(">>"));
        HBox containerBottom = new HBox();
        containerBottom.getChildren().addAll(new Button("|<"), new Button("||"), new Button(">|"));

        containerHBOX.getChildren().addAll(containerTop, containerBottom);


        // setCenter
        BorderPane barreEvolution = new BorderPane();

        Slider barreLecture = new Slider();

        BorderPane volume_equalizer = new BorderPane();
        Slider volume = new Slider();
        HBox containerButton = new HBox();
        Button equalizer = new Button("|||");
        ToggleButton file = new ToggleButton(":=");
        file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(file.isSelected()){
                    // afficher le tree table
                    if(root.getCenter() == null){

                        root.setCenter(treeTableView);
                        primaryStage.setMinHeight(Top.getHeight() + heightMinWindow + heightWindow);
                        primaryStage.setHeight(Top.getHeight() + treeTableView.getPrefHeight() + heightWindow);
                    }
                } else {
                    if(root.getCenter() != null){

                        root.setCenter(null);
                        primaryStage.setHeight(Top.getHeight() + heightWindow);
                        primaryStage.setMinHeight(Top.getHeight() + heightWindow);
                        primaryStage.setMaxHeight(Top.getHeight() + heightWindow);
                    }

                }
            }
        });
        containerButton.getChildren().addAll(equalizer, file);
        volume_equalizer.setLeft(volume);
        volume_equalizer.setRight(containerButton);




        BorderPane name_musique_barre_lecture = new BorderPane();
        TextField playing_song = new TextField("Current Song");
        TextField time_current_song = new TextField("00:00");

        name_musique_barre_lecture.setBottom(barreLecture);
        name_musique_barre_lecture.setRight(time_current_song);
        name_musique_barre_lecture.setLeft(playing_song);

        barreEvolution.setCenter(name_musique_barre_lecture);


        barreEvolution.setBottom(volume_equalizer);

        Top.setLeft(containerHBOX);
        Top.setCenter(barreEvolution);

        /*primaryStage.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(t1.intValue() < 200){
                    System.out.println("Resize");
                    file.setSelected(false);
                }
            }
        });*/


        primaryStage.setTitle("Player VLC");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        primaryStage.setMinHeight(Top.getHeight() + heightWindow);
//        primaryStage.setHeight(Top.getHeight() + heightWindow);
    }

    public void creationTreeTableView(){
        treeTableView = new TreeTableView();
        treeTableView.setStyle("-fx-background-color:green;");
        treeTableView.setPrefHeight(300);
    }



    public static void main(String[] args) {
        launch(args);
    }

}
