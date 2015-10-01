import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Missile extends Entity{
	private final int LENGTH = 10;
	protected Color color;
	protected int duration, itemEffect;
	protected double x, y, angle, velocity;
	protected Player parent;
	protected Asteroid target;
	protected boolean osc, dead;
	
	public Missile(int x, int y, Color color, double angle, double velocity, int duration, Player parent, boolean osc, int itemEffect){
		this.x = x;
		this.y = y;
		this.color = color;
		this.angle = angle;
		this.velocity = velocity;
		this.duration = duration;
		this.parent = parent;
		this.osc = osc;
		this.itemEffect = itemEffect;
		this.dead = false;
		if(osc)
			this.angle -= Util.randomDouble(Math.PI/8., (3.*Math.PI)/8.);
		switch(itemEffect){
		case Item.GRAVITATING:
			target = Refs.ents.getRandomAsteroid();
			break;
		}
	}

	public void update(){
		if(osc)
			angle += .6*Math.sin((double)duration*.75+.2);
		switch(itemEffect){
		case Item.SPLITTING:
			if(Util.randomDouble(0, 1) < .04)
				splitMissile(Util.randomDouble(-.15, .15));
			break;
		case Item.GRAVITATING:
			gravitateToTarget();
			break;
		}
		x += velocity*Math.cos(angle);
		y -= velocity*Math.sin(angle);
		duration--;
		if(duration == 0)
			kill();
	}

	public void paint(Graphics g){
		g.setColor(color);
		g.drawLine((int)x, (int)y,
				(int)(x-LENGTH*Math.cos(angle)), 
				(int)(y+LENGTH*Math.sin(angle)));
	}

	public int getX(){
		return (int)x;
	}

	public int getY(){
		return (int)y;
	}
	
	public Color getColor(){
		return color;
	}

	public void kill(){
		dead = true;
	}

	public boolean isDead(){
		return dead;
	}
	
	public Player getParent(){
		return parent;
	}
	
	public int getLength(){
		return LENGTH;
	}
	
	public double getAngle(){
		return angle;
	}
	
	/**
	 * returns a point on the missile for checking collisions.
	 * @param distance = location of point from from of missile; 0 = front, 1 = rear
	 * @return point at specified location on missile
	 */
	public Point getPoint(double distance){
		return new Point((int)(x-LENGTH*distance*Math.cos(angle)), 
							(int)(y+LENGTH*distance*Math.sin(angle)));
	}
	
	public void splitMissile(double thOffset){
		Refs.ents.add(new Missile(getX(), 
				getY(), color, angle+thOffset, 8,
				duration, parent,
				osc, itemEffect));
	}
	
	public void gravitateToTarget(){
		if(target == null || target.isDead())
			target = Refs.ents.getRandomAsteroid();
		try{
			double vx = velocity*Math.cos(angle);
			double vy = velocity*Math.sin(angle);
			double distance = Util.distance(target.getX(), target.getY(), x, y);
			double gConstant = 40;
			//X calculations
			vx += (gConstant*Math.pow(target.getRadius(), 2)*(target.getX()-x))/
				(distance*Math.pow(distance, 2));
			//Y calculations
			vy -= (gConstant*Math.pow(target.getRadius(), 2)*(target.getY()-y))/
				(distance*Math.pow(distance, 2));
			//find new angle
			if(vx <= 0)
				angle = Math.atan(vy/vx)+Math.PI;
			else
				angle = Math.atan(vy/vx);
			//change velocity
			velocity = Util.trimDouble(Util.distance(vx, vy, 0, 0), 0 ,8);
		}catch(Exception e){}
	}
}
