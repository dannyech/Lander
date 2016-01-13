import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.Polygon;
import java.awt.Shape;

	public class Ground {
	private final int MOUNTAIN_NUM = 5;
	private final int PEBBLE_NUM = 50;
	private int x, y, width, height;
	private Mountain[] mountains;
	private Pebble[] pebbles;
	
	public Ground(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		mountains = new Mountain[MOUNTAIN_NUM];
		for(int i=0; i<mountains.length; i++)
			mountains[i] = new Mountain();
		pebbles = new Pebble[PEBBLE_NUM];
		for(int i=0; i<pebbles.length; i++)
			pebbles[i] = new Pebble();
	}
	
	public void paint(Graphics2D g2){
		
		g2.setColor(new Color(34,139,34));
		g2.fillRect(x, y, width, height);
		for(Mountain m : mountains)
			m.paint(g2);
		for(Pebble p : pebbles)
			p.paint(g2);
	}
	
	public Line2D.Double[] getBoundsArray(){
		
		Line2D.Double[] arr = new Line2D.Double[2*mountains.length+1];
		
		int i = 0;
		
		arr[i++] = new Line2D.Double(x,y,width,y);
		
		for(Mountain m : mountains){
			Line2D.Double[] lines = m.getBoundLines();
			for(Line2D.Double line : lines)
				arr[i++] = line;
		}
		
		return arr;
	}
	
	private class Mountain {
		
		private int leftCorner, base, peak;
		private int[] xPoints, yPoints;
		
		public Mountain(){
			peak = (int) (Math.random()*height)+5;
			base = (int) (Math.random()*width/4)+10;
			leftCorner = (int) (Math.random()*(width-base));
			xPoints = new int[3]; 
			xPoints[0] = leftCorner; xPoints[1] = leftCorner+base; xPoints[2] = leftCorner +
					(int) (Math.round(base/2.0));
			yPoints = new int[3];
			yPoints[0] = y; yPoints[1] = y; yPoints[2] = y-peak;
			
		}
		
		protected void paint(Graphics2D g2){
			
			g2.setColor(new Color(131,139,131));
			g2.fill(getPoly());
		}
		
		private Polygon getPoly(){
			
			return new Polygon(xPoints, yPoints, xPoints.length);
		}
		
		public Line2D.Double[] getBoundLines(){
			
			Line2D.Double[] lines = {new Line2D.Double(xPoints[1], yPoints[1], xPoints[2], yPoints[2]),
					new Line2D.Double(xPoints[0], yPoints[0], xPoints[2], yPoints[2]),
			};
			return lines;
		}
	}

	private class Pebble{
		
		private final int DIAMETER = 5;
		private int pebbleX, pebbleY;
		
		public Pebble(){
			pebbleX = (int) (Math.random()*width);
			pebbleY = (int) (Math.random()*height) + y;
		}
		
		protected void paint(Graphics2D g2){
			
			g2.setColor(Color.GRAY);
			g2.fillOval(pebbleX, pebbleY, DIAMETER, DIAMETER);
		}
	}
}
