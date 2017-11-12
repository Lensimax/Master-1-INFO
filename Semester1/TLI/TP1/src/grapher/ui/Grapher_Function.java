package grapher.ui;


import grapher.fc.Function;
import grapher.fc.FunctionFactory;
import javafx.scene.paint.Color;

public class Grapher_Function {
    Color color;
    Function function;
    double lineWidth;

    public Grapher_Function(Function f, Color c, double l){
        this.function = f;
        this.color = c;
        this.lineWidth = l;
    }

    public Grapher_Function(Function f){
        this.function = f;
        this.color = Color.BLACK;
        this.lineWidth = 1;
    }

    public Grapher_Function(String s){
        this.function = FunctionFactory.createFunction(s);
        this.color = Color.BLACK;
        this.lineWidth = 1;
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
}
