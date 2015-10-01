import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Shard extends Missile{
	private final int LENGTH = 16;
	
	public Shard(int x, int y, Color color, double angle, double velocity, int duration, Player parent){
		super(x, y, color, angle, velocity, duration, parent, false, -1);
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		//find the points
		int x = this.getX();
		int y = this.getY();
		int[] xA = {x,
				x - (int)(.5*LENGTH*Math.cos(angle+Math.PI/6)),
				x - (int)(LENGTH*Math.cos(angle)),
				x - (int)(.5*LENGTH*Math.cos(angle-Math.PI/6))};
		int[] yA = {y,
				y + (int)(.5*LENGTH*Math.sin(angle+Math.PI/6)),
				y + (int)(LENGTH*Math.sin(angle)),
				y + (int)(.5*LENGTH*Math.sin(angle-Math.PI/6))};
		Polygon p = new Polygon(xA, yA, 4);
		g.fillPolygon(p);
	}
}
