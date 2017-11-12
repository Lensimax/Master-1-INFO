package grapher.ui;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.MOVE;

public class Handler implements EventHandler<MouseEvent> {

    GrapherCanvas canvas;

    Point2D p;

    public static final int D_DRAG = 5;

    enum State {IDLE,RIGHT_CLICK_DRAG_OR_CLICK, SELECT_ZOOM, DRAG_OR_CLICK, WHEEL_ZOOM, DRAG, MIDDLE_DRAG_OR_CLICK}

    State state = State.IDLE;

    public Handler(GrapherCanvas c){
        this.canvas = c;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {


        switch(state){

            case IDLE:


                    switch(mouseEvent.getEventType().getName()){
                        case "MOUSE_PRESSED":
                            if(isRight(mouseEvent)){

                                if(mouseEvent.isPrimaryButtonDown()){
                                    p = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                                    state = State.DRAG_OR_CLICK;
                                } else if(mouseEvent.isSecondaryButtonDown()){
                                    p = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                                    state = State.RIGHT_CLICK_DRAG_OR_CLICK;
                                } else if(mouseEvent.isMiddleButtonDown()){
                                    p = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                                    state = State.MIDDLE_DRAG_OR_CLICK;
                                }

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
                        p = new Point2D(mouseEvent.getX(), mouseEvent.getY());

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

                        }
                        break;

                    case "MOUSE_RELEASED":
                        state = State.IDLE;
                        this.canvas.zoom(p, -5);
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
                        this.canvas.zoom(p, new Point2D(mouseEvent.getX(), mouseEvent.getY()));
                        state = State.IDLE;
                        break;
                }

                break;

            case MIDDLE_DRAG_OR_CLICK:

                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":
                        if(p.distance(mouseEvent.getX(), mouseEvent.getY()) > D_DRAG){
                            state = State.WHEEL_ZOOM;
                        }
                        break;

                    case "MOUSE_RELEASED":

                        state = State.IDLE;

                        break;

                    default:
                        break;
                }

                break;

            case WHEEL_ZOOM:
                switch(mouseEvent.getEventType().getName()){
                    case "MOUSE_DRAGGED":

                        // zoom quand on monte, dezoom quand on descend
                        if(mouseEvent.getY() > p.getY()){
                            // dezoom
                            this.canvas.zoom(p, -5);
                        } else {
                            // zoom
                            this.canvas.zoom(p, 5);
                        }
                        p = new Point2D(mouseEvent.getX(), mouseEvent.getY());

                        state = State.WHEEL_ZOOM;
                        break;


                    case "MOUSE_RELEASED":
                        state = State.IDLE;
                        break;
                }
                break;

            default:
                break;
        }

    }

    private void drawRectangleDashed(MouseEvent mouseEvent){

        this.canvas.getGraphicsContext2D().setLineDashes(new double[]{ 3.f, 3.f });

        //dessiner
        // 4 lignes en pointillés

        this.canvas.getGraphicsContext2D().strokeLine(p.getX(), p.getY(), mouseEvent.getX(), p.getY());
        this.canvas.getGraphicsContext2D().strokeLine(p.getX(), p.getY(), p.getX(), mouseEvent.getY());
        this.canvas.getGraphicsContext2D().strokeLine(mouseEvent.getX(), p.getY(), mouseEvent.getX(), mouseEvent.getY());
        this.canvas.getGraphicsContext2D().strokeLine(mouseEvent.getX(), mouseEvent.getY(), p.getX(), mouseEvent.getY());

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

    // le click est dans le repère gradué
    private boolean isRight(MouseEvent e){
        return e.getX() > canvas.getMARGIN() && e.getX() < canvas.getWIDTH() - canvas.getMARGIN() &&
                e.getY() > canvas.getMARGIN() && e.getY() < canvas.getHEIGHT() - canvas.getMARGIN();
    }
}
