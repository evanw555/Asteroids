import java.awt.Color;
import java.awt.Point;


public class Comet extends Asteroid{

	public Comet(){
		this.radius = Util.randomIntInclusive(30, 70);
		Point spawn = Util.getRandomBorderPoint();
		this.x = spawn.getX();
		this.y = spawn.getY();
		double tempAngle = Util.randomDouble(0, Math.PI*2);
		final double VELOCITY = Util.randomDouble(1, 2.5);
		this.vx = VELOCITY*Math.cos(tempAngle);
		this.vy = VELOCITY*Math.sin(tempAngle);
		this.angle = 0;
		this.angleV = Util.randomDouble(-.03, .03);
		this.dead = false;
		this.th = Util.getUniformTh(Util.randomIntInclusive(14, 22));
		this.r = Util.getRandomR(th.length, .5, 1);
		this.color = new Color(90 + Util.randomIntInclusive(-10, 10),
				200 + Util.randomIntInclusive(-10, 10),
				240 + Util.randomIntInclusive(-10, 10));
	}
	
	public Comet(int radius, int x, int y, double vx, double vy){
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
		this.color = new Color(90 + Util.randomIntInclusive(-10, 10),
				200 + Util.randomIntInclusive(-10, 10),
				240 + Util.randomIntInclusive(-10, 10));
	}
}
