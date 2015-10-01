import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Stalker extends Entity{
	private double x, y, vx, vy, hp, angleToTarget;
	private int radius;
	private boolean dead;
	private Player target;
	
	public Stalker(){
		Point p = Util.getRandomBorderPoint();
		x = p.getX();
		y = p.getY();
		vx = Util.randomDouble(-2, 2);
		vy = Util.randomDouble(-2, 2);
		hp = 1;
		angleToTarget = 0;
		radius = Util.randomIntInclusive(15, 30);
		dead = false;
		target = null;
	}
	
	public void update(){
		//if target is dead or null, (try to) find a new one
		if(target == null || target.isDead())
			findTarget();
		//randomly switch targets
		if(Util.randomDouble(0, 1) < .01)
			findTarget();
		//update kinematics
		vx = Util.trimDouble(vx, -7, 7);
		vy = Util.trimDouble(vy, -7, 7);
		x += vx;
		y += vy;
		correctBoundaryBreaches();
		if(target != null)
			gravitateToTarget();
	}

	public void paint(Graphics g) {
		//paint body
		g.setColor(Color.GRAY);
		g.fillOval(getX()-radius, getY()-radius, radius*2, radius*2);
		//paint inner square
		if(target == null)
			g.setColor(Color.BLACK);
		else
			g.setColor(target.getColor());
		g.fillRect(getX()-(int)(radius*.5), getY()-(int)(radius*.5), radius, radius);
		//paint tick, if a target exists
		if(target != null){
			g.setColor(Color.BLACK);
			int[] xPoints = {getX(), getX()+(int)(radius*Math.cos(angleToTarget+.1)), 
					getX()+(int)(radius*Math.cos(angleToTarget-.1))};
			int[] yPoints = {getY(), getY()-(int)(radius*Math.sin(angleToTarget+.1)),
					getY()-(int)(radius*Math.sin(angleToTarget-.1))};
			g.fillPolygon(xPoints, yPoints, 3);
		}
	}

	public int getX(){
		return (int)x;
	}

	public int getY(){
		return (int)y;
	}

	public Color getColor(){
		return Color.GRAY;
	}

	public void kill(){
		dead = true;
	}

	public boolean isDead(){
		return dead;
	}
	
	public void correctBoundaryBreaches(){
		x = Util.correctBoundaryBreachesX(getX());
		y = Util.correctBoundaryBreachesY(getY());
	}
	
	public void findTarget(){
		try{
			target = Refs.ents.getRandomPlayer();
		}catch(Exception e){
			target = null;
		}
	}
	
	public void gravitateToTarget(){
		double gConstant = 5;
		double distance = Util.distance(getX(), getY(), target.getX(), target.getY());
		double xDistance = (double)(target.getX() - getX());
		double yDistance = (double)(target.getY() - getY());
		//X calculations
		vx += (gConstant*Math.pow(target.getRadius(), 2)*xDistance)/
			(distance*Math.pow(distance, 2));
		//Y calculations
		vy += (gConstant*Math.pow(target.getRadius(), 2)*yDistance)/
			(distance*Math.pow(distance, 2));
		//change angle
		if(xDistance <= 0)
			angleToTarget = Math.atan(-yDistance/xDistance)+Math.PI;
		else
			angleToTarget = Math.atan(-yDistance/xDistance);
	}
}