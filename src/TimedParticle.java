import java.awt.Color;
import java.awt.Graphics;


public class TimedParticle extends Entity{
	public static final int EXPLOSION = 0, THRUST = 1, EVENT_BALL = 2, ROCK_DEBRIS = 3, ITEM_BALL = 4, WAVE_END = 5, ELECTRIC_DEBRIS = 6, EMP = 7;
	private double x, y, vx, vy;
	private int type, duration;
	private Color color;
	private Entity parent;
	
	public TimedParticle(int type, int x, int y, Entity parent, int duration){
		this.type = type;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.duration = duration;
		this.color = parent.getColor();
		this.vx = this.vy = 0;
		switch(type){
		case EXPLOSION:
		case EVENT_BALL:
		case ROCK_DEBRIS:
		case ITEM_BALL:
		case WAVE_END:
		case ELECTRIC_DEBRIS:
			this.vx = Util.randomDouble(-1, 1);
			this.vy = Util.randomDouble(-1, 1);
			break;
		case THRUST:
			this.vx = Util.randomDouble(-1, 1);
			this.vy = Util.randomDouble(-1, 1);
			if(parent instanceof Player){
				//make thrust particles spawn at rear of ship
				this.x = parent.getX()+((Player)parent).getRadius()*.5*Math.cos(((Player)parent).getAngle()+Math.PI);
				this.y = parent.getY()-((Player)parent).getRadius()*.5*Math.sin(((Player)parent).getAngle()+Math.PI);
			}
			break;
		case EMP:
			this.vx = Util.randomDouble(-10, 10);
			this.vy = Util.randomDouble(-10, 10);
		}
	}
	
	public TimedParticle(int type, int x, int y, double vx, double vy, Color color, int duration){
		this.type = type;
		this.x = x;
		this.y = y;
		this.parent = null;
		this.duration = duration;
		this.color = color;
		this.vx = vx;
		this.vy = vy;
	}
	
	public void update(){
		x += vx;
		y += vy;
		if (duration > 0) {
			duration--;
		}
		switch(type){
		case EMP:
			vx *= .99;
			vy *= .99;
		}
	}

	public void paint(Graphics g){
		switch(type){
		case EXPLOSION:
			//TODO make sizes more random, and maybe color more random too
			g.setColor(new Color(255, Util.randomIntInclusive(0, 255), 0));
			g.fillOval((int)x-3, (int)y-3, 6, 6);
			break;
		case THRUST:
			//TODO make sizes more random, and maybe color more random too
			g.setColor(new Color(255, Util.randomIntInclusive(64, 255), 0));
			g.fillOval((int)x-2, (int)y-2, 4, 4);
			break;
		case EVENT_BALL:
			//TODO make sizes more random, and maybe color more random too
			g.setColor((Color)Util.returnRandom(Color.WHITE, Color.GREEN));
			g.fillOval((int)x-3, (int)y-3, 6, 6);
			break;
		case ROCK_DEBRIS:
			//TODO make sizes more random, and maybe color more random too
			g.setColor(parent.getColor());
			g.fillOval((int)x-3, (int)y-3, 6, 6);
			break;
		case ITEM_BALL:
			//TODO make sizes more random, and maybe color more random too
			g.setColor((Color)Util.returnRandom(Color.LIGHT_GRAY, Color.DARK_GRAY));
			g.fillOval((int)x-3, (int)y-3, 6, 6);
			break;
		case WAVE_END:
			//TODO make sizes more random, and maybe color more random too
			g.setColor(parent.getColor());
			g.fillOval((int)x-3, (int)y-3, 6, 6);
			break;
		case ELECTRIC_DEBRIS:
			if(Util.randomDouble(0, 1) < .3){
				//TODO make sizes more random, and maybe color more random too
				g.setColor(parent.getColor());
				g.fillOval((int)x-2, (int)y-2, 4, 4);
			}
			break;
		case EMP:
			if(Util.randomDouble(0, 1) < .6){
				//TODO make sizes more random, and maybe color more random too
				g.setColor(parent.getColor());
				g.fillOval((int)x-2, (int)y-2, 4, 4);
			}
			break;
		}
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
		duration = 0;
	}

	public boolean isDead(){
		return duration <= 0;
	}
}