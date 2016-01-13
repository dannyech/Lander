import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.Shape;

public class Ship {

	private boolean isAccelerating;

	private final double THRUST_ACCELERATION = .025;
	private final int ROTATION_SPEED = 2;

	private final int SPAN = 20;
	private final int HEIGHT = 24;

	private double x, y; 
	private int orientation;
	private Vector velVec;
	private ArrayList<Smoke> smokeList;

	private AffineTransform rotate;

	public Ship(Dimension d){

		isAccelerating = false;
		x = d.width/2;
		y = d.height/2;
		orientation = 90;
		velVec = new Vector();
		smokeList = new ArrayList<Smoke>();
	}

	private Polygon getShipPoly(){

		int x = (int) this.x;
		int y = (int) this.y;

		int[] xPoints = {x-SPAN/2 , x, x+SPAN/2};
		int[] yPoints = {y, y-HEIGHT,y};

		//{left leg start, left leg bottom, top, right leg bottom
		// right leg start}

		/*
		int[] xPoints = {x-(SPAN/4), x-SPAN, x, x+SPAN,
				x+(SPAN/4)};
		int[] yPoints = {y, y+(HEIGHT/2), y-HEIGHT, y+(HEIGHT/2),
				y};
		 */
		return new Polygon(xPoints, yPoints, xPoints.length);
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	private void thrust(){
		isAccelerating = true;

		double dx = Physics.computeDX(THRUST_ACCELERATION, orientation);
		double dy = Physics.computeDY(THRUST_ACCELERATION, orientation);

		velVec.x+=dx;
		velVec.y-=dy;
	}

	private void rotate(String direction){
		if(direction.equals("right")){
			if(orientation-ROTATION_SPEED<0)
				orientation = 360+orientation-ROTATION_SPEED;
			else
				orientation-=ROTATION_SPEED;
		}
		else if(direction.equals("left")){
			if(orientation+ROTATION_SPEED>360)
				orientation = orientation+ROTATION_SPEED-360;
			else
				orientation+=ROTATION_SPEED;
		}
	}

	public void updateLocation(boolean left, boolean right, boolean up){

		if(up)
			thrust();
		if(left)
			rotate("left");
		if(right)
			rotate("right");

		velVec.y+=Physics.GRAV;
		x+=velVec.x;
		y+=velVec.y;
	}

	public void collision(){

		y -=.05;
		velVec.y*=-Physics.V_RETAINED;
		velVec.x*=Physics.FRICTION;
		
	}

	public void paint(Graphics2D g2){

		g2.setColor(new Color(178,34,34));

		//set transform
		AffineTransform original = g2.getTransform();
		rotate = AffineTransform
				.getRotateInstance(Math.toRadians(90-orientation),x, y);
		g2.transform(rotate);

		//draw ship
		g2.fillPolygon(getShipPoly());

		//draw flames
		if(isAccelerating){
			g2.setColor(Color.ORANGE);
			g2.fillPolygon(getFlamesPoly(2));
			g2.setColor(Color.RED);
			g2.fillPolygon(getFlamesPoly(1));
			isAccelerating = false;

			smokeList.add(new Smoke(x,y,Physics.computeDY(10*THRUST_ACCELERATION, orientation),
					-Physics.computeDX(10*THRUST_ACCELERATION, orientation)));
		}

		//ship cockpit
		g2.setColor(Color.CYAN);
		g2.fill(new Ellipse2D.Double(x-(SPAN/8.0),y-(HEIGHT/2.0), SPAN/4.0, 
				SPAN/2.0));

		//reset transform
		g2.setTransform(original);

		//paint smoke
		long currTime = System.currentTimeMillis();
		ListIterator<Smoke> smokeIterator = smokeList.listIterator();

		while(smokeIterator.hasNext()){
			Smoke temp = smokeIterator.next();
			if(currTime-temp.timeDrawn>temp.removeTime)
				smokeIterator.remove();
			temp.paint(g2);
		}
	}

	private class Smoke {

		private double sX,sY;
		private Vector v;
		private final int DIAMETER = 4;
		private long timeDrawn;
		private int removeTime;

		public Smoke(double x, double y, double yA, double xA){	

			v = new Vector();
			v.x = xA;
			v.y = yA;
			int xOffset = (int) (Math.random()*8)-4;
			int yOffset = (int) (Math.random()*16);

			Point2D p = getTransformedXY(xOffset, yOffset);
			sX = p.getX();
			sY = p.getY();
			timeDrawn = System.currentTimeMillis();
			removeTime = (int)(Math.random()*6000)+1;
		}

		public void paint(Graphics2D g2){
			g2.setColor(new Color(239, 239,239));
			g2.fillOval((int)sX, (int)sY, DIAMETER, DIAMETER);
			v.y += Physics.GRAV*.2; //.2 = air resistance
			sY+=v.y;
			sX +=v.x;
		}
	}

	private Point2D getTransformedXY(){

		return getTransformedXY(0,0);
	}

	private Point2D getTransformedXY(double xOffset, double yOffset){

		Point2D xy = new Point2D.Double(x+xOffset,y+yOffset);
		Point2D xyTrans = new Point2D.Double();

		xyTrans = rotate.transform(xy, xyTrans);

		return xyTrans;

	}

	private Polygon getFlamesPoly(int sizeFactor){

		int x = (int) this.x;
		int y = (int) this.y;

		int[] xPoints = {x - (SPAN/4), x + (SPAN/4), x};
		int[] yPoints = {y, y, y + sizeFactor*HEIGHT};

		return new Polygon(xPoints, yPoints, xPoints.length);
	}

	public Line2D.Double[] getBoundLines(){

		PathIterator path = getShipPoly().getPathIterator(rotate);
		double[] coords = new double[6];

		Line2D.Double[] lines = new Line2D.Double[3];
		int i = 0;

		while(!path.isDone() && i<lines.length){
			int type = path.currentSegment(coords);
			lines[i++] = new Line2D.Double(coords[0],coords[1],coords[2],coords[3]);
			path.next();
		}

		return lines;
	}

	public Vector getVector(){
		return velVec;
	}
}
