package grapher.ui;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import static javafx.scene.Cursor.CLOSED_HAND;
import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.MOVE;

public class Handler implements EventHandler<MouseEvent> {

    GrapherCanvas canvas;

    Point2D p;

    public static final int D_DRAG = 5;

    enum State {IDLE,RIGHT_CLICK_DRAG_OR_CLICK, SELECT_ZOOM, DRAG_OR_CLICK, DRAG}

    State state = State.IDLE;

    public Handler(GrapherCanvas c){
        this.canvas = c;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {

        switch(state){

            case IDLE:


                    switch(mouseEvent.getEventType().getName()){
                        case "MOUSE_PRESSED":  // TODO a changer c'était du debug
                            if(mouseEvent.isSecondaryButtonDown()){
                                p = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                                state = State.DRAG_OR_CLICK;
                            } else if(mouseEvent.isPrimaryButtonDown()){
                                p = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                                state = State.RIGHT_CLICK_DRAG_OR_CLICK;
                            }
                            break;

                        default:
                            break;
                    }

                break;


            case DRAG_OR_CLICK:

                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":
                        if(p.distance(mouseEvent.getX(), mouseEvent.getY()) > D_DRAG){
                            state = State.DRAG;
                            this.canvas.setCursor(MOVE);
                        }
                        break;

                    case "MOUSE_RELEASED":

                        this.canvas.zoom(p, 5);
                        state = State.IDLE;

                        break;

                    default:
                        break;
                }


                break;

            case DRAG:

                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":

                        this.canvas.translate(mouseEvent.getX() - p.getX(), mouseEvent.getY()- p.getY());

                        state = State.DRAG;

                        break;

                    case "MOUSE_RELEASED":
                        state = State.IDLE;
                        this.canvas.setCursor(DEFAULT);
                        break;

                    default:
                        break;
                }

                break;

            case RIGHT_CLICK_DRAG_OR_CLICK:

                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":
                        if(p.distance(mouseEvent.getX(), mouseEvent.getY()) > D_DRAG){
                            state = State.SELECT_ZOOM;

                            this.drawRectangleDashed(mouseEvent);
                        }
                        break;

                    default:
                        break;
                }

                break;


            case SELECT_ZOOM:

                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":
                        this.canvas.redraw();
                        this.drawRectangleDashed(mouseEvent);
                        state = State.SELECT_ZOOM;
                        break;


                    case "MOUSE_RELEASED":
                        // zoom
                        this.canvas.redraw();
                        state = State.IDLE;
                        break;
                }

                break;

            default:
                break;
        }

    }

    private void drawRectangleDashed(MouseEvent mouseEvent){
        double top_x;
        double top_y;
        double width = p.getX() - mouseEvent.getX();
        double heigth = p.getY() - mouseEvent.getY();

        if(width > 0){
            top_x = mouseEvent.getX();
        } else {
            top_x = p.getX();
        }

        if(heigth > 0){
            top_y = mouseEvent.getY();
        } else {
            top_y = p.getY();
        }

        // TODO c'est pas bon A CORRIGER !!!!!!!!!!!!

        System.out.println("p :"+p+" Top x: "+top_x+" Top y: "+top_y+" width: "+width+" heigth: "+heigth);


        this.canvas.getGraphicsContext2D().setLineDashes(new double[]{ 3.f, 3.f });

        //dessiner

        this.canvas.getGraphicsContext2D().setLineDashes(null);
    }


    // valeur absolue
    private double abs(double i){
        if(i < 0){
            return i * (-1);
        } else {
            return i;
        }
    }
}
