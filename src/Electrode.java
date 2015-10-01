import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Electrode extends Asteroid{
	private int innerRadius, initInactiveDur, inactiveDur;
	private double resilience;
	private boolean active, deactivator;
	
	public Electrode(){
		this.radius = Util.randomIntInclusive(40, 70);
		this.innerRadius = (int)(this.radius*Util.randomDouble(.2, .6));
		this.initInactiveDur = 1;
		this.inactiveDur = 0;
		this.resilience = Util.randomDouble(0, 1);
		this.active = true;
		this.deactivator = Refs.game.getWave().isWaveType(Wave.ELECTRIC_FIELD) &&
							Util.randomDouble(0, 1) < .05;
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
		if(deactivator)
			this.th = Util.getUniformTh(Util.randomIntInclusive(20, 30));
		else
			this.th = Util.getUniformTh(Util.randomIntInclusive(12, 20));
		this.r = Util.getRandomR(th.length, .8, 1);
		this.color = Color.YELLOW;
	}
	
	public Electrode(int radius, int x, int y, double vx, double vy){
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(12, 20));
		this.r = Util.getRandomR(th.length, .8, 1);
		this.color = Color.YELLOW;
	}
	
	public void update(){
		if(inactiveDur <= 0)
			active = true;
		else{
			active = false;
			inactiveDur--;
		}
		vx += Util.randomDouble(-.01, .01);
		vy += Util.randomDouble(-.01, .01);
		x += vx;
		y += vy;
		angle += angleV;
		makeCorrections();
		if(active && Util.randomDouble(0, 1) < .7)
			if(deactivator)
				this.r = Util.getRandomR(th.length, .65, 1);
			else
				this.r = Util.getRandomR(th.length, .8, 1);
	}
	
	public void paint(Graphics g){
		if(active){
			g.setColor(color);
			g.drawPolygon(getPolygon());
			if(deactivator)
				g.setColor(Color.BLUE);
			else
				g.setColor(Color.GRAY);
			g.fillOval(getX()-innerRadius, getY()-innerRadius, 2*innerRadius, 2*innerRadius);
		}else{
			g.setColor(Color.GRAY);
			g.fillOval(getX()-innerRadius, getY()-innerRadius, 2*innerRadius, 2*innerRadius);
			Util.paintElectrodeTimer(this, g);
		}
	}
	
	public int getInnerRadius(){
		return innerRadius;
	}
	
	public void attemptToDeactivate(double charge){
		if(!deactivator && resilience < charge){
			initInactiveDur = Util.millisToFrames(Util.randomIntInclusive(9000, 25000));
			inactiveDur = initInactiveDur;
			Refs.ents.addParticles(this, innerRadius/2, TimedParticle.ELECTRIC_DEBRIS);
		}
	}
	
	public boolean isHostile(){
		return active;
	}
	
	public int getInactiveDuration(){
		return inactiveDur;
	}
	
	public int getInitialInactiveDuration(){
		return initInactiveDur;
	}
	
	public boolean isDeactivator(){
		return deactivator;
	}
}