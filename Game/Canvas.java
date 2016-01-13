import javax.swing.JPanel;

import java.awt.*;

public class Canvas extends JPanel{

	private int width, height;
	private Ship ship;
	private Backround backround;
	private Ground ground;

	public Canvas(Dimension d, Ship s, Ground g, Backround b){

		width = d.width;
		height = d.height;
		ship = s;
		backround = b;
		ground = g;
	}

	public void paint(Graphics g){

		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		backround.paint(g2);
		
		ship.paint(g2);
		
		ground.paint(g2);
		
		//dev help
		g2.setColor(Color.YELLOW);
		g2.drawString("X: "+ship.getX(), width-150, 15);
		g2.drawString("Y: "+ship.getY(), width-150, 30);
		g2.drawString("dy: "+-ship.getVector().y, width-150, 45);
		g2.drawString("dx: "+ship.getVector().x, width-150, 60);

	}
}
