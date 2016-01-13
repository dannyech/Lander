import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

public class Triangle /*implements Shape*/{
	
	//a,b,c represent the three corners of the triangle
	
	private Point2D.Double a,b,c;
	private Line2D.Double ab,bc,ac;
	
	public Triangle(double x, double y, double base, double height){
		
		a = new Point2D.Double(x, y);
		b = new Point2D.Double(x+(base/2), y-height);
		c = new Point2D.Double(x+base, y);
		ab = new Line2D.Double(a,b);
		bc = new Line2D.Double(b,c);
		ac = new Line2D.Double(a,c);
		
	}
	
	
	
	
}
