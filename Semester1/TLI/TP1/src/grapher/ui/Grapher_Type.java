package grapher.ui;


import grapher.fc.Function;
import grapher.fc.FunctionFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Grapher_Type {


    Function function;
    ColorPicker color_picker;
    double lineWidth;


    public Grapher_Type(Function f){
        this.function = f;
        this.lineWidth = 1;
        this.color_picker = new ColorPicker(Color.BLACK);

        // pour mettre a jour quand on change le color picker
        color_picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.grapher.redraw();
            }
        });
    }

    public Grapher_Type(Function f, Color c, double l){
        this(f);
        this.lineWidth = l;
        this.color_picker.setValue(c);
    }

    public Grapher_Type(String s){
        this(FunctionFactory.createFunction(s));
    }




    public String toString(){
        String renvoi = "";

        renvoi += "{ f(x)= "+this.function.toString()+" width= "+this.lineWidth+ "  color= "+this.color_picker.getValue().toString()+" }";

        return renvoi;
    }


    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }


    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public ColorPicker getColor_picker() {
        return color_picker;
    }

    public void setColor_picker(ColorPicker color_picker) {
        this.color_picker = color_picker;
    }
}
