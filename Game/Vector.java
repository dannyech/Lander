
public class Vector {

	public double x, y;

	public Vector(){
		this(90, 0);
	}

	public Vector(int angle, int speed){

		x = speed*Math.cos(Math.toRadians(angle));
		y = speed*Math.sin(Math.toRadians(angle));
	}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getMagnitude(){
		return Math.sqrt((x*x)+(y*y));
	}
	
	public int getDirection(){
		
		return (int) Math.toDegrees(Math.tan(y/x));
	}

}
