import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;

public class Backround {
	
	private final int STAR_NUM = 50;
	private int width, height, x ,y;
	private Star[] stars;
	
	public Backround(int x, int y, int width, int height){
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		stars = new Star[STAR_NUM];
		for(int i=0; i<stars.length; i++)
			stars[i] = new Star();
	}
	
	public void paint(Graphics2D g2){
		
		g2.setPaint(new GradientPaint(width/2,0,Color.BLACK, width/2, height, new Color(0,191,255)));
		g2.fillRect(x, y, width, height);
		for(Star s : stars)
			s.paint(g2);
	}
	
	private class Star {
		
		private final int DIAMETER = 10;
		private int starX , starY;
		
		public Star(){
			starX = (int) (Math.random()*width);
			starY = (int) (Math.random()*height/2);
		}
		
		protected void paint(Graphics2D g2){
			
			g2.setColor(Color.WHITE);
			g2.fillOval(starX, starY, DIAMETER, DIAMETER);
		}
	}

}
