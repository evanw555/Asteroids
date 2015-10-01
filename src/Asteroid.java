import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;


public class Asteroid extends Entity{
	public static final int ASTEROID = 0, COMET = 1, ELECTRODE = 2, AMOEBA = 3;
	public int radius;
	public double x, y, vx, vy, angle, angleV;
	public boolean dead;
	public double[] r, th;
	protected Color color;
	
	public Asteroid(){
		this.radius = Util.randomIntInclusive(15, 70);
		Point spawn = Util.getRandomBorderPoint();
		this.x = spawn.getX();
		this.y = spawn.getY();
		double tempAngle = Util.randomDouble(0, Math.PI*2);
		final double VELOCITY = Util.randomDouble(1.5, 3);
		this.vx = VELOCITY*Math.cos(tempAngle);
		this.vy = VELOCITY*Math.sin(tempAngle);
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(8, 16));
		this.r = Util.getRandomR(th.length, .7, 1);
		this.color = new Color(156 + Util.randomIntInclusive(-10, 10),
				107 + Util.randomIntInclusive(-10, 10),
				23 + Util.randomIntInclusive(-10, 10));
	}
	
	public Asteroid(int radius, int x, int y, double vx, double vy){
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(8, 16));
		this.r = Util.getRandomR(th.length, .7, 1);
		this.color = new Color(156 + Util.randomIntInclusive(-10, 10),
				107 + Util.randomIntInclusive(-10, 10),
				23 + Util.randomIntInclusive(-10, 10));
	}

	public void update(){
		x += vx;
		y += vy;
		angle += angleV;
		makeCorrections();
	}

	public void paint(Graphics g){
		g.setColor(color);
		g.fillPolygon(getPolygon());
		//TODO TEMP
		//	g.setColor(Color.WHITE);
		//	g.setFont(new Font("Impact", Font.BOLD, 23));
		//	g.drawString(""+radius, getX(), getY());
		//TODO END TEMP
	}

	public int getX(){
		return (int)x;
	}
	
	public double getVX(){
		return vx;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public double getVY(){
		return vy;
	}

	public Color getColor() {
		//TODO TEMP
		return color;
		//TODO END TEMP
	}

	public void kill(){
		dead = true;
	}

	public boolean isDead(){
		return dead;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public void correctAngle(){
		if(angle >= Math.PI*2)
			angle = angle%(Math.PI*2);
		if(angle < 0)
			angle = angle%(Math.PI*2);
	}
	
	public void correctBoundaryBreaches(){
		x = Util.correctBoundaryBreachesX(x);
		y = Util.correctBoundaryBreachesY(y);
	}
	
	public Polygon getPolygon(){
		Polygon poly;
		//get array of x's
		int[] xA = new int[th.length];
		for(int i = 0; i < xA.length; i++)
			xA[i] = (int)(x+(r[i]*radius*Math.cos(th[i]+angle))); //TODO +angle? rotating?
		//get array of y's
		int[] yA = new int[th.length];
		for(int i = 0; i < yA.length; i++)
			yA[i] = (int)(y-(r[i]*radius*Math.sin(th[i]+angle))); //TODO +angle? rotating?
		//create polygon
		poly = new Polygon(xA, yA, xA.length);
		return poly;
	}
	
	public Point[] getPoints(){
		Point[] pts = new Point[th.length];
		for(int i = 0; i < pts.length; i++)
			pts[i] = new Point((int)(x+(r[i]*radius*Math.cos(th[i]+angle))), //TODO +angle? rotating?
					(int)(y-(r[i]*radius*Math.sin(th[i]+angle)))); //TODO +angle? rotating?
		return pts;
	}
	public void makeCorrections(){
		correctAngle();
		correctBoundaryBreaches();
		//correctBordering();
	}
	
	public void correctBordering(){
		if((vx <= .5 && vx >= -.5) || (vy <= .5 && vy >= -.5)){
			vx += Util.randomDouble(.5, -.5);
			vy += Util.randomDouble(.5, -.5);
		}
	}
	
	public void reactToForeignMomentum(double fMass, double fvx, double fvy){
		double mass = Math.pow(getRadius(), 2);
		this.vx += (fMass/mass)*fvx;
		this.vy += (fMass/mass)*fvy;
	}
}