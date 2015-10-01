import java.awt.Color;
import java.awt.Graphics;

public class ClusterMissile extends Entity{
	private double x, y, angle, velocity;
	private long duration;
	private Player parent;
	
	public ClusterMissile(int x, int y, double angle, Player parent){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.velocity = 6;
		this.parent = parent;
		this.duration = Util.millisToFrames(2800);
	}
	
	public void update(){
		duration--;
		if(duration > Util.millisToFrames(500)){ //moves only until first explosion
			x += velocity*Math.cos(angle);
			y -= velocity*Math.sin(angle);
			correctBoundaryBreaches();
		}else if(duration == Util.millisToFrames(500)){ //first explosion at .5 seconds left
			createMissiles(Util.randomIntInclusive(23, 27));
			Refs.ents.addParticles(this, 12, TimedParticle.EXPLOSION);
		}else if(duration <= 0){ //second explosion at 0 seconds left
			createMissiles(Util.randomIntInclusive(33, 37));
			Refs.ents.addParticles(this, 17, TimedParticle.EXPLOSION);
		}
	}

	public void paint(Graphics g){
		if(duration <= Util.millisToFrames(500)) //do not paint after first explosion
			return;
		g.setColor(getColor());
		g.fillOval(getX()-8, getY()-8, 16, 16);
		g.setColor(new Color(255, Util.randomIntInclusive(0, 255), 0));
		g.fillOval(getX()-4, getY()-4, 8, 8);
	}

	public int getX(){
		return (int)x;
	}

	public int getY(){
		return (int)y;
	}

	public Color getColor(){
		return parent.getColor();
	}

	public void kill(){
		duration = 0;
	}

	public boolean isDead(){
		return duration <= 0;
	}
	
	public void correctBoundaryBreaches(){
		x = Util.correctBoundaryBreachesX(getX());
		y = Util.correctBoundaryBreachesY(getY());
	}
	
	public void createMissiles(int num){
		for(int i = 0; i < num; i++)
			Refs.ents.add(new Missile(getX(), getY(),
					parent.getColor(), Util.randomDouble(-.1, .1)+((2*Math.PI)/num)*i,
					8, Util.millisToFrames(Util.randomIntInclusive(800, 1200)), parent, false, -1));
	}

}
