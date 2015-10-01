import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;


public class ItemEntity extends Entity{
	private final int RADIUS = 16;
	private double x, y, vx, vy;
	private long duration;
	
	public ItemEntity(){
		Point spawn = Util.getRandomBorderPoint();
		this.x = spawn.getX();
		this.y = spawn.getY();
		double tempAngle = Util.randomDouble(0, Math.PI*2);
		this.vx = 2*Math.cos(tempAngle);
		this.vy = 2*Math.sin(tempAngle);
		this.duration = Util.millisToFrames(Util.randomIntInclusive(10000, 20000));
	}
	
	//TODO ADD PARTICLE EFFECT WHEN DURATION EXPIRES AND WHEN KILLED
	
	public void update(){
		duration--;
		vx += Util.randomDouble(-.2, .2);
		vx = Util.trimDouble(vx, -2, 2);
		vy += Util.randomDouble(-.2, .2);
		vy = Util.trimDouble(vy, -2, 2);
		x += vx;
		y += vy;
		correctBoundaryBreaches();
		if(duration == 0) //so that particles show on expiration and forced death
			kill();
	}

	public void paint(Graphics g){
		g.setColor(Color.DARK_GRAY);
		g.fillOval(getX()-RADIUS, getY()-RADIUS, RADIUS*2, RADIUS*2);
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Impact", Font.BOLD, RADIUS*2));
		g.drawString("+", getX()-RADIUS/2, getY()+RADIUS-4);
	}

	public int getX(){
		return (int)x;
	}

	public int getY(){
		return (int)y;
	}

	public Color getColor(){
		return Color.DARK_GRAY;
	}

	public void kill(){
		duration = 0;
		Refs.ents.addParticles(this, 15, TimedParticle.ITEM_BALL);
	}

	public boolean isDead(){
		return duration <= 0;
	}
	
	public int getRadius(){
		return RADIUS;
	}
	
	public void correctBoundaryBreaches(){
		x = Util.correctBoundaryBreachesX(x);
		y = Util.correctBoundaryBreachesY(y);
	}
}