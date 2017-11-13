package grapher.ui;

import static java.lang.Math.*;

import java.util.Vector;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.converter.DoubleStringConverter;

import javafx.application.Application.Parameters;

import javafx.geometry.Point2D;


import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;


import grapher.fc.*;


public class GrapherCanvas extends Canvas {


	static final double MARGIN = 40;
	static final double STEP = 5;

	static final double WIDTH = 600;
	static final double HEIGHT = 400;
	
	private static DoubleStringConverter d2s = new DoubleStringConverter();
	
	protected double W = WIDTH;
	protected double H = HEIGHT;

	protected double xmin, xmax;
	protected double ymin, ymax;

	
	public GrapherCanvas(Parameters params) {
		super(WIDTH, HEIGHT);
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;


        this.addEventHandler(MouseEvent.ANY, new Grapher_Handler(this));
		this.addEventHandler(ScrollEvent.ANY, new ScrollerHandler(this));
	}

	
	public double minHeight(double width)  { return HEIGHT;}
	public double maxHeight(double width)  { return Double.MAX_VALUE; }
	public double minWidth(double height)  { return WIDTH;}
	public double maxWidth(double height)  { return Double.MAX_VALUE; }

	public boolean isResizable() { return true; }
	public void resize(double width, double height) {
		super.setWidth(width);
		super.setHeight(height);
		redraw();
	}	
	
	public void redraw() {
		GraphicsContext gc = getGraphicsContext2D();
		W = getWidth();
		H = getHeight();
		
		// background
		gc.clearRect(0, 0, W, H);

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		// box
		gc.save();
		gc.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) {
			return;
		}
		
		gc.strokeRect(0, 0, W, H);
		
		gc.fillText("x", W, H+10);
		gc.fillText("y", -10, 0);
		
		gc.beginPath();
		gc.rect(0, 0, W, H);
		gc.closePath();
		gc.clip();

	
		// plot		
		gc.translate(-MARGIN, -MARGIN);

		gc.setLineWidth(1);

		// x values
		final int N = (int)(W/STEP + 1);
		final double dx = dx(STEP);
		double xs[] = new double[N];
		double Xs[] = new double[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
		}

		for(Grapher_Type f: Main.list_function) {
			// y values
			double Ys[] = new double[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.getFunction().y(xs[i]));
			}

            gc.setLineWidth(f.getLineWidth());
            gc.setStroke(f.getColor_picker().getValue());
			gc.strokePolyline(Xs, Ys, N);
		}

		gc.setLineWidth(1);
		gc.setFill(Color.BLACK);
		gc.restore(); // restoring no clipping
		
		
		// axes
		drawXTick(gc, 0);
		drawYTick(gc, 0);
		
		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		gc.setLineDashes(new double[]{ 4.f, 4.f });
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(gc, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(gc, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(gc, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(gc, y); }
		
		gc.setLineDashes(null);
	}
	
	protected double dx(double dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(double dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(double X) { return xmin+dx(X-MARGIN); }
	protected double y(double Y) { return ymin+dy((Y-MARGIN)-H); }
	
	protected double X(double x) {
		double Xs = (x-xmin)/(xmax-xmin)*W;
		return Xs + MARGIN;
	}
	protected double Y(double y) {
		double Ys = (y-ymin)/(ymax-ymin)*H;
		return (H - Ys) + MARGIN;
	}
		
	protected void drawXTick(GraphicsContext gc, double x) {
		if(x > xmin && x < xmax) {
			final double X0 = X(x);
			gc.strokeLine(X0, MARGIN, X0, H+MARGIN);
			gc.fillText(d2s.toString(x), X0, H+MARGIN+15);
		}
	}
	
	protected void drawYTick(GraphicsContext gc, double y) {
		if(y > ymin && y < ymax) {
			final double Y0 = Y(y);
			gc.strokeLine(0+MARGIN, Y0, W+MARGIN, Y0);
			gc.fillText(d2s.toString(y), 5, Y0);
		}
	}
	
	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; }
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}
	
	
	protected void translate(double dX, double dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		redraw();
	}
	
	protected void zoom(Point2D center, double dz) {
		double x = x(center.getX());
		double y = y(center.getY());
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		redraw();
	}

    protected void zoom_from_center(double dz) {
        double x = x(WIDTH/2);
        double y = y(HEIGHT/2);
        double ds = exp(dz*.01);
        xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
        ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
        redraw();
    }
	
	protected void zoom(Point2D p0, Point2D p1) {
		double x0 = x(p0.getX());
		double y0 = y(p0.getY());
		double x1 = x(p1.getX());
		double y1 = y(p1.getY());
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		redraw();
	}

	public double getMARGIN() {
		return MARGIN;
	}

	public double getWIDTH() {
		return WIDTH;
	}

	public double getHEIGHT() {
		return HEIGHT;
	}
}
