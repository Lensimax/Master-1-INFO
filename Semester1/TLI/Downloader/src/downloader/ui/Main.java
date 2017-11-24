package downloader.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Application {

    static public ArrayList<Download_type> tab_download;
    static public VBox conteneur;

    public void start(Stage stage) {

        conteneur = new VBox();

        BorderPane root = new BorderPane(){{
            setCenter(new ScrollPane(){{
                setFitToWidth(true);
                setContent(conteneur);

            }});



            setBottom(new BorderPane(){{
                //input text field
                TextField field = new TextField();

                Button add = new Button("Add");
                add.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Download_type d = new Download_type(field.getText());
                        tab_download.add(d);
                        ajout_progress_bar(conteneur, d);

                    }
                });

                setCenter(field);
                setRight(add);
            }});
        }};


        for(String s: getParameters().getRaw()){
            Download_type d = new Download_type(s);
            tab_download.add(d);
            VBox v = ajout_progress_bar(conteneur, d);
            conteneur.setMargin(v, new Insets(5));
        }


        stage.setTitle("Downloader");
        stage.setScene(new Scene(root));
        stage.setHeight(400);
        stage.setWidth(400);
        stage.show();
    }

    public VBox ajout_progress_bar(VBox box, Download_type d){
        VBox vbox = new VBox(){{


            setStyle("-fx-border-color: black; -fx-border-radius: 3px; -fx-padding: 2px");

            getChildren().add(new Label(d.getUrl()));

            getChildren().add(new BorderPane(){{

                setCenter(new ProgressBar(){{
                    ProgressBar pr = this;
                    progressProperty().bind(d.getD().progressProperty());
                    setPrefWidth(Integer.MAX_VALUE);
                    setStyle("-fx-padding: 5px");

                    progressProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                            if(observableValue.getValue().doubleValue() == 1.0){
                                lookup(".bar").setStyle("-fx-background-color: green");
                            }
                        }
                    });


                }});

                setRight(new HBox(){{
                    ToggleButton pause = new ToggleButton("||");
                    pause.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            if(pause.isSelected()){
                                d.pause();
                                pause.setText(">");

                            } else {
                                d.resume();
                                pause.setText("||");
                            }
                        }
                    });
                    Button suppr = new Button("X");
                    suppr.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // TODO detruire thread
//                            getParent().getCh

                        }
                    });

                    getChildren().addAll(pause, suppr);
                }});



            }});
        }};



        box.getChildren().add(vbox);

        return vbox;
    }

    public static void main(String argv[]) {

        tab_download = new ArrayList<>();



        launch(argv);
    }
}
