

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
                    root.setCenter(treeTableView);
                    primaryStage.setHeight(Top.getHeight() + treeTableView.getPrefHeight() + heightWindow);
                } else {
                    root.setCenter(null);
                    primaryStage.setHeight(Top.getHeight() + heightWindow);

                }
            }
        });
        file.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean number, Boolean t1) {
                if(file.isSelected()){
                    file.setStyle("-fx-background-color: deepskyblue");
                } else {
                    file.setStyle("-fx-background-color: white");
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

        primaryStage.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(primaryStage.getHeight() < 200){
                    System.out.println("Resize");
                    root.setCenter(null);
                    file.setSelected(false);
                    primaryStage.setHeight(Top.getHeight() + heightWindow);
                }
            }
        });


        primaryStage.setTitle("Player VLC");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void creationTreeTableView(){

        // artist ACDC

        final TreeItem<Music> ACDC = new TreeItem<>(new Music("ACDC", "", ""));

        final TreeItem<Music> Black_Ice = new TreeItem<>(new Music("Black Ice", "", ""));

        // album Black Ice
        Black_Ice.setExpanded(true);
        Black_Ice.getChildren().addAll(
                new TreeItem<Music>(new Music("Highway To Hell", "ACDC", "3:34")),
                new TreeItem<Music>(new Music("Black Ice", "ACDC", "3:25")),
                new TreeItem<Music>(new Music("Money Made", "ACDC", "4:15"))
        );

        // album High Voltage
        final TreeItem<Music> High_Voltage = new TreeItem<>(new Music("High Voltage", "", ""));
        High_Voltage.setExpanded(true);
        High_Voltage.getChildren().addAll(
                new TreeItem<Music>(new Music("Live Wire", "ACDC", "5:49")),
                new TreeItem<Music>(new Music("T.N.T", "ACDC", "3:35")),
                new TreeItem<Music>(new Music("She's got balls", "ACDC", "4:51"))
        );


        ACDC.getChildren().addAll(Black_Ice, High_Voltage);
        ACDC.setExpanded(true);


        // artist Billy Talent
        final TreeItem<Music> Billy_Talent = new TreeItem<>(new Music("Billy Talent", "", ""));

        // album Billy Talent II
        final TreeItem<Music> Billy_Talent_II = new TreeItem<>(new Music("Billy Talent II", "", ""));
        Billy_Talent_II.setExpanded(true);
        Billy_Talent_II.getChildren().addAll(
                new TreeItem<Music>(new Music("Red Flah", "Billy Talent", "3:17")),
                new TreeItem<Music>(new Music("Fallen Leaves", "Billy Talent", "3:19"))
        );

        // album Billy Talent III
        final TreeItem<Music> Billy_Talent_III = new TreeItem<>(new Music("Billy Talent III", "", ""));
        Billy_Talent_III.setExpanded(true);
        Billy_Talent_III.getChildren().addAll(
                new TreeItem<Music>(new Music("Rusted From The Rain", "Billy Talent", "4:13"))
        );


        Billy_Talent.getChildren().addAll(Billy_Talent_II, Billy_Talent_III);
        Billy_Talent.setExpanded(true);

//        TreeTableColumn<String, String> Artiste = new TreeTableColumn<>();

        final TreeItem<Music> root = new TreeItem<Music>();
        root.getChildren().addAll(ACDC, Billy_Talent); // pour ajouter autant de noeud que l'on veut

        treeTableView = new TreeTableView(root);
        treeTableView.setShowRoot(false); // pour ne pas voir le root


        // creation des colonnes
        TreeTableColumn<Music,String> name_music_column = new TreeTableColumn<Music, String>("Name");
        // on indique comment les remplir
        name_music_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Music, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        // same
        TreeTableColumn<Music,String> auteur_column = new TreeTableColumn<>("Auteur");
        auteur_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Music, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getAuteur()));
        // same
        TreeTableColumn<Music,String> duree_column = new TreeTableColumn<>("Duree");
        duree_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Music, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getDuree()));

        treeTableView.getColumns().addAll(name_music_column, auteur_column, duree_column); // ajout de toutes les colonnes
        treeTableView.setPrefHeight(300);
    }



    public static void main(String[] args) {
        launch(args);
    }

}
