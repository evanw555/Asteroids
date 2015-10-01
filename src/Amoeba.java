import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Amoeba extends Asteroid{
	public static final int DEATH_RADIUS = 80;
	private int radiusAggregate = 0;
	private boolean infected;
	
	public Amoeba(){
		this.infected = Refs.game.getWave().isWaveType(Wave.SWARM) &&
							Util.randomDouble(0, 1) < .1;
		
		this.radius = Util.randomIntInclusive(25, 40);
		Point spawn = Util.getRandomBorderPoint();
		this.x = spawn.getX();
		this.y = spawn.getY();
		double tempAngle = Util.randomDouble(0, Math.PI*2);
		final double VELOCITY = Util.randomDouble(1, 2);
		this.vx = VELOCITY*Math.cos(tempAngle);
		this.vy = VELOCITY*Math.sin(tempAngle);
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(10, 18));
		this.r = Util.getRandomR(th.length, .8, 1);
		if(infected)
			this.color = Color.MAGENTA; //TODO CHANGE
		else
			this.color = Color.GREEN; //TODO CHANGE
	}
	
	public Amoeba(int radius, int x, int y, double vx, double vy){
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(10, 18));
		this.r = Util.getRandomR(th.length, .8, 1);
		this.color = Color.GREEN; //TODO CHANGE
	}
	
	public void update(){
		//if infected, begin to swell
		if(infected)
			radiusAggregate += (int)(Util.randomDouble(0, 1.3)); // 3/13 chance of increasing radius by 1
		//add the aggregate to the real radius, then reset the aggregate
		radius += radiusAggregate;
		radiusAggregate = 0;
		//if radius lethal, disinfect
		if(hasLethalRadius())
			disinfect();
		//trim radius so it is never larger than the death radius
		radius = Util.trimInt(radius, 0, DEATH_RADIUS);
		
		x += vx;
		y += vy;
		angle += angleV;
		makeCorrections();
		Util.alterR(r, .8, 1, .04);
	}
	
	public void changeRadius(int dr){
		radiusAggregate += dr;
	}
	
	public boolean hasLethalRadius(){
		return radius >= DEATH_RADIUS;
	}
	
	public boolean isInfected(){
		return infected;
	}
	
	public void infect(){
		infected = true;
		color = Color.MAGENTA;
	}
	
	public void disinfect(){
		infected = false;
		color = Color.green;
	}
}