package grapher.ui;


import grapher.fc.Function;
import grapher.fc.FunctionFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Grapher_Function {
    Color color;
    Function function;

    ColorPicker color_picker;



    double lineWidth;
    SimpleStringProperty string_function;



    public Grapher_Function(Function f, Color c, double l){
        this.function = f;
        this.color = c;
        this.lineWidth = l;
        this.string_function = new SimpleStringProperty(f.toString());
        this.color_picker = new ColorPicker(this.color);
    }

    public Grapher_Function(Function f){
        this.function = f;
        this.color = Color.BLACK;
        this.lineWidth = 1;
        this.string_function = new SimpleStringProperty(f.toString());
        this.color_picker = new ColorPicker(this.color);
    }

    public Grapher_Function(String s){
        this.function = FunctionFactory.createFunction(s);
        this.string_function = new SimpleStringProperty(s);
        this.color = Color.BLACK;
        this.lineWidth = 1;
        this.color_picker = new ColorPicker(this.color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public String getString_function() {
        return string_function.get();
    }

    public SimpleStringProperty string_functionProperty() {
        return string_function;
    }

    public void setString_function(String string_function) {
        this.string_function.set(string_function);
    }
}
