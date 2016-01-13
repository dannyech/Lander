import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Timer;

public class Game extends JFrame{

	private JFrame f;
	private final Dimension D = new Dimension(960, 740);
	Ship ship;
	Ground ground;
	Backround backround;
	Canvas canvas;
	int GROUND_Y;
	private boolean leftHeld, rightHeld, upHeld;


	public Game() throws InterruptedException{

		f = new JFrame();
		ship = new Ship(D);
		GROUND_Y = D.height-(D.height/5);
		ground = new Ground(0, GROUND_Y, D.width, 
				D.height-GROUND_Y);
		backround = new Backround(0, 0, D.width, GROUND_Y);
		canvas = new Canvas(D, ship, ground, backround);
		leftHeld = false; rightHeld = false; upHeld = false;
		makeFrame();
		makeListener();
		runGame();
	}

	private void makeFrame(){

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Lander");
		f.setSize(D);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.add(canvas);
		f.setVisible(true);
	}

	private void makeListener(){
		KeyListener listener = new KeyListener(){

			public void keyTyped(KeyEvent e) {

			}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP)
					upHeld = true;
				if(e.getKeyCode() == KeyEvent.VK_LEFT)
					leftHeld = true;
				if(e.getKeyCode() == KeyEvent.VK_RIGHT)
					rightHeld = true;
			}

			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP)
					upHeld = false;
				if(e.getKeyCode() == KeyEvent.VK_LEFT)
					leftHeld = false;
				if(e.getKeyCode() == KeyEvent.VK_RIGHT)
					rightHeld = false;
			}
		};
		f.addKeyListener(listener);
		f.setFocusable(true);
	}

	private void runGame() throws InterruptedException{

		final int FPS = 60;
		final int SKIP_TICKS = 1000/FPS;
		long nextTick = System.currentTimeMillis();

		long sleepTime = 0;

		boolean isRunning = true;

		while(isRunning){
			update();
			display();

			nextTick+=SKIP_TICKS;
			sleepTime = nextTick - System.currentTimeMillis();

			if(sleepTime>=0)

				Thread.sleep(sleepTime);
		}
	}

	private void update(){

		ship.updateLocation(leftHeld, rightHeld, upHeld);

		if(ship.getY()>D.height/2)
			if(checkCollision(ship, ground))
				ship.collision();

		//loop round screen
		if(ship.getX()<0)
			ship.setX(D.width);
		else if(ship.getX()>D.width)
			ship.setX(0);

	}

	private boolean checkCollision(Ship s, Ground g){

		boolean collided = false;

		for(Line2D.Double line : s.getBoundLines()){
			for(Line2D.Double l : g.getBoundsArray()){
				if(line.intersectsLine((l)))
				{collided = true; 
				Line2D.Double t = l;
				System.out.println("("+t.x1+","+t.y1+"), ("+t.x2+","+t.y2+")");}
			}
		}
		return collided;

	}

	private void display(){
		canvas.repaint();
	}

	public static void main(String[] args){
		try{
			new Game();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
