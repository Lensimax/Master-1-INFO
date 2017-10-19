package grapher.ui;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ScrollEvent;

public class ScrollerHandler implements EventHandler<ScrollEvent> {

    GrapherCanvas canvas;

    Point2D p;

    public ScrollerHandler(GrapherCanvas c){
        this.canvas = c;
    }

    @Override
    public void handle(ScrollEvent scrollEvent) {

        if(scrollEvent.getDeltaY() > 0){
            canvas.zoom(new Point2D(scrollEvent.getX(), scrollEvent.getY()), 5);
        } else {
            canvas.zoom(new Point2D(scrollEvent.getX(), scrollEvent.getY()), -5);
        }
    }
}
