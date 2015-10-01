import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Util{
	public static final int SIDE_BAR_WIDTH = 128;
	public static int INTERWAVE_FRAMES = 1; //placeholder value, setable
	public static final int MIN_REPRODUCTION_RADIUS = 26, MIN_ASTEROID_RADIUS = 16;

	public static double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	public static double distance(Point a, Point b){
		return Math.sqrt(Math.pow(b.getX()-a.getX(), 2) + Math.pow(b.getY()-a.getY(), 2));
	}
	
	public static void sleep(long millis){
		try{
			Thread.sleep(millis);
		}catch(Exception e){}
	}
	
	public static int randomIntInclusive(int lower, int upper){
		assert upper >= lower;
		return (int)(Math.random()*(1+upper-lower))+lower;
	}
	
	public static double randomDouble(double lower, double upper){
		assert upper >= lower;
		return (Math.random()*(upper-lower))+lower;
	}
	
	/**
	 * in a list of events, return whether or not the list contains a specified type.
	 */
	public static boolean contains(ArrayList<Event> list, int type){
		for(Event e : list)
			if(e.getType() == type)
				return true;
		return false;
	}
	
	/**
	 * in a list of entities, return whether or not the list contains a specified type.
	 */
	public static boolean contains(ArrayList<Entity> list, Class type){
		for(Entity e : list)
			if(e.getClass() == type)
				return true;
		return false;
	}
	
	/**
	 * in a list of events, remove all the instances of a specified type.
	 */
	public static void remove(ArrayList<Event> list, int type){
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).getType() == type){
				list.remove(i);
				i--;
			}
	}
	
	/**
	 * in a list of events, return the first instance of a given type.
	 * @return first instance of a given type.
	 */
	public static Event get(ArrayList<Event> list, int type){
		for(Event e : list)
			if(e.getType() == type)
				return e;
		return null;
	}
	
	/**
	 * DEPRECATED
	 */
	/*
	public static void paintHealth(Player p, Graphics g){
		//TODO DO THIS
		g.setColor(Color.RED);
		g.fillRect(p.getX()-p.getRadius(), p.getY()-p.getRadius()-10,
				2*p.getRadius(), 8);
		g.setColor(Color.GREEN);
		g.fillRect(p.getX()-p.getRadius(), p.getY()-p.getRadius()-10,
				(int)(2*p.getRadius()*p.getHP()), 8);
	}
	*/
	
	public static void paintLives(Player p, Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 15));
		g.drawString("x"+p.getLives(), p.getX()-(int)(1.5*p.getRadius()), p.getY()-p.getRadius());
	}
	
	public static void paintShield(Player p, Graphics g){
		g.setColor(Color.BLUE);
		g.drawOval(p.getX()-p.getRadius(), p.getY()-p.getRadius(),
				2*p.getRadius(), 2*p.getRadius());
	}
	
	public static void paintDynamicShield(Player p, Graphics g){
		g.setColor(Color.BLUE);
		if(contains(p.getEvents(), Event.SHIELD))
			g.fillArc(p.getX()-(int)(1.5*p.getRadius()), p.getY()-(int)(1.5*p.getRadius()),
					3*p.getRadius(), 3*p.getRadius(), 90,
					(int)(360*((double)Util.get(p.getEvents(), Event.SHIELD).getDuration() /
					(double)Util.get(p.getEvents(), Event.SHIELD).getInitialDuration())));
		else if(contains(p.getEvents(), Event.SPAWN_SHIELD))
				g.fillArc(p.getX()-(int)(1.5*p.getRadius()), p.getY()-(int)(1.5*p.getRadius()),
						3*p.getRadius(), 3*p.getRadius(), 90,
						(int)(360*((double)Util.get(p.getEvents(), Event.SPAWN_SHIELD).getDuration() /
						(double)Util.get(p.getEvents(), Event.SPAWN_SHIELD).getInitialDuration())));
	}
	
	public static void paintElectrodeTimer(Electrode e, Graphics g){
		g.setColor(Color.YELLOW);
		g.fillArc(e.getX()-(int)(.75*e.getInnerRadius()), e.getY()-(int)(.75*e.getInnerRadius()),
					(int)(1.5*e.getInnerRadius()), (int)(1.5*e.getInnerRadius()), 90,
					(int)(360*((double)e.getInactiveDuration() /
					(double)e.getInitialInactiveDuration())));
	}
	
	public static double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	public static boolean isCollisionType(Entity a, Entity b, Class aC, Class bC){
		return (a.getClass() == aC && b.getClass() == bC) ||
				(a.getClass() == bC && b.getClass() == aC);
	}
	
	public static boolean colliding(Player a, Missile b, Class missileType){
		final int NUM_CHECKS = 3;
		//between a PLAYER and a MISSILE
		if(missileType == Missile.class){
			for(int i = 0; i < NUM_CHECKS; i++)
				if(a.getPolygon().contains(b.getPoint(((double)i) / ((double)NUM_CHECKS-1))))
					return true;
			return false;
		//between a PLAYER and a SHARD
		}else if(missileType == Shard.class){
			//check spine of shard
			for(int i = 0; i < NUM_CHECKS; i++)
				if(a.getPolygon().contains(b.getPoint(((double)i) / ((double)NUM_CHECKS-1))))
					return true;
			//check side vertices of shard
			Point check = getDisplacedPoint(b.getX(), b.getY(), b.getAngle()+Math.PI/6, .5*b.getLength());
			if(a.getPolygon().contains(check))
				return true;
			check = getDisplacedPoint(b.getX(), b.getY(), b.getAngle()-Math.PI/6, .5*b.getLength());
			if(a.getPolygon().contains(check))
				return true;
			return false;
		}
		return false;
	}
	
	public static boolean colliding(Player a, Player b){
		Point[] aPts = a.getPoints();
		Point[] bPts = b.getPoints();
		//checks for collision between points in a and polygon of b
		for(Point p : aPts)
			if(b.getPolygon().contains(p))
				return true;
		//checks for collision between points in b and polygon of a
		for(Point p : bPts)
			if(a.getPolygon().contains(p))
				return true;
		return false;
	}
	
	public static boolean colliding(Player a, EventEntity b){
		Point[] aPts = a.getPoints();
		//checks for collision between points in a and circle of b
		for(Point p : aPts)
			if(distance(p.getX(), p.getY(), b.getX(), b.getY()) < b.getRadius())
				return true;
		return false;
	}
	
	public static boolean colliding(Player a, ItemEntity b){
		Point[] aPts = a.getPoints();
		//checks for collision between points in a and circle of b
		for(Point p : aPts)
			if(distance(p.getX(), p.getY(), b.getX(), b.getY()) < b.getRadius())
				return true;
		return false;
	}
	
	public static boolean colliding(Player a, Asteroid b){
		Point[] aPts = a.getPoints();
		Point[] bPts = b.getPoints();
		//checks for collision between points in a and polygon of b
		for(Point p : aPts)
			if(b.getPolygon().contains(p))
				return true;
		//checks for collision between points in b and polygon of a
		for(Point p : bPts)
			if(a.getPolygon().contains(p))
				return true;
		return false;
	}
	
	public static boolean colliding(Missile a, Asteroid b){
		//between MISSILE and ELECTRODE
		if(b instanceof Electrode){
			final int NUM_CHECKS = 3;
			for(int i = 0; i < NUM_CHECKS; i++)
				if(distance(a.getPoint(((double)i) / ((double)NUM_CHECKS-1)),
						new Point(b.getX(), b.getY())) < ((Electrode)b).getInnerRadius())
					return true;
			return false;
		//between MISSILE and ASTEROID, COMET
		}else{
			final int NUM_CHECKS = 3;
			for(int i = 0; i < NUM_CHECKS; i++)
				if(b.getPolygon().contains(a.getPoint(((double)i) / ((double)NUM_CHECKS-1))))
					return true;
			return false;
		}
	}
	
	public static boolean withinRange(Player a, Player b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY()) 
				<= a.getRadius() + b.getRadius();
	}
	
	public static boolean withinRange(Player a, EventEntity b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY()) 
				<= a.getRadius() + b.getRadius();
	}
	
	public static boolean withinRange(Player a, ItemEntity b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY()) 
				<= a.getRadius() + b.getRadius();
	}
	
	public static boolean withinRange(Player a, Asteroid b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY()) 
				<= a.getRadius() + b.getRadius();
	}
	
	public static boolean withinRange(Missile a, Asteroid b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY()) 
				<= b.getRadius();
	}
	
	public static boolean withinRange(Asteroid a, Asteroid b){
		return Util.distance(a.getX(), a.getY(), b.getX(), b.getY())
				<= a.getRadius() + b.getRadius();
	}
	
	public static int millisToFrames(long millis){
		return (Refs.game.getFrameRate()*(int)millis)/1000;
	}
	
	public static Object returnRandom(Object...o){
		return o[(int)(Math.random()*o.length)];
	}
	
	/**
	 * return polar coordinates for the default ship.
	 * [0][] return the angle
	 * [1][] returns the radii
	 */
	public static double[][] getDefaultShipPolars(){
		double[][] temp = {{0, 0, (3*Math.PI)/4, Math.PI},
							{1, 1, 1, .5}};
		return temp;
	}
	
	/**
	 * returns a random point on the border of the screen; typically for spawning
	 * @return point on random part of screen border
	 */
	public static Point getRandomBorderPoint(){
		int x, y;
		//1/2 chance; point is on top or bottom
		if(Util.randomIntInclusive(0, 1) == 0){
			x = Util.randomIntInclusive(0, Refs.canvas.getPlayableWidth());
			y = Util.randomIntInclusive(0, 1)*Refs.canvas.getHeight();
		//1/2 chance; point is on left or right
		}else{
			x = Util.randomIntInclusive(0, 1)*Refs.canvas.getPlayableWidth();
			y = Util.randomIntInclusive(0, Refs.canvas.getHeight());
		}
		return new Point(x, y);
	}
	
	/**
	 * returns an array of angles uniformly distributed about a full revolution.
	 * @param angles number of angles
	 * @return an array of angles uniformly distributed about a full revolution
	 */
	public static double[] getUniformTh(int angles){
		double[] th = new double[angles];
		for(int i = 0; i < angles; i++)
			th[i] = ((Math.PI*2)/angles)*i;
		return th;
	}
	
	/**
	 * returns an array of random radius coefficients within the given limits.
	 * @param num number of radii in the circle
	 * @param lower lowest possible radius coefficient
	 * @param upper highest possible radius coefficient
	 * @return
	 */
	public static double[] getRandomR(int num, double lower, double upper){
		double[] r = new double[num];
		for(int i = 0; i < num; i++)
			r[i] = Util.randomDouble(lower, upper);
		return r;
	}
	
	public static void alterR(double[] r, double min, double max, double dr){
		for(int i = 0; i < r.length; i++)
			r[i] = trimDouble(r[i]+randomDouble((-1)*dr, dr), min, max);
	}
	
	public static String framesToTime(long frames){
		long millis = 1000*frames/Refs.game.getFrameRate();
		long seconds = millis/1000;
		millis %= 1000;
		long minutes = seconds/60;
		seconds %= 60;
		long hours = minutes/60;
		minutes %= 60;
		return hours+":"+minutes+":"+seconds+"."+millis;
	}
	
	public static int trimInt(int x, int a, int b){
		if(x < a)
			return a;
		if(x > b)
			return b;
		return x;
	}
	
	public static double trimDouble(double x, double a, double b){
		if(x < a)
			return a;
		if(x > b)
			return b;
		return x;
	}
	
	public static double correctBoundaryBreachesX(double x){
		if(x < 0)
			return Refs.canvas.getPlayableWidth()-1+x;
		else if(x > Refs.canvas.getPlayableWidth()-1)
			return x%Refs.canvas.getPlayableWidth();
		return x;
	}
	
	public static double correctBoundaryBreachesY(double y){
		if(y < 0)
			return Refs.canvas.getHeight()-1+y;
		else if(y > Refs.canvas.getHeight()-1)
			return y%Refs.canvas.getHeight();
		return y;
	}
	
	public static int randomXInCircle(int radius, double angle){
		return (int)(Math.cos(angle)*radius*randomDouble(0, 1));
	}
	
	public static int randomYInCircle(int radius, double angle){
		return (int)(Math.sin(angle)*radius*randomDouble(0, 1));
	}
	
	public static void setInterwaveFrames(int frames){
		INTERWAVE_FRAMES = frames;
	}
	
	public static boolean emitsInterwaveParticles(Entity e){
		return e instanceof Player ||
			   e instanceof Asteroid ||
			   e instanceof EventEntity ||
			   e instanceof ItemEntity;
	}
	
	public static Point getDisplacedPoint(int x, int y, double th, double disp){
		return new Point((int)(x-(disp*Math.cos(disp+th))),
				(int)(y+(disp*th)));
	}
}
