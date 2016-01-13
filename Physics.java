
public class Physics {
	
	public final static double GRAV= .01;
	public final static double FRICTION = .5;
	public final static double V_RETAINED = .8;
	
	
	
	public static double computeDX(double acceleration, int orientation){
		return acceleration*Math.cos(Math.toRadians(orientation));
	}
	
	public static double computeDY(double acceleration, int orientation){
		return acceleration*Math.sin(Math.toRadians(orientation));
	}
}
