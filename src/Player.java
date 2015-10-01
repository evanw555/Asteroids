import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Entity{
	public static final int STANDARD_RADIUS = 16;
	private String name;
	private Color color;
	private Controls controls;
	private double[] r, th;
	private boolean dead, newWave;
	private double x, y, vx, vy;
	private int radius, lives, roundKills, kills;
	private double angle;
	private ArrayList<Event> events, eventPurg;
	private Item item;
	
	public Player(String name, Color color, Controls controls, double[] r, double[] th){
		this.name = name;
		this.color = color;
		this.controls = controls;
		this.r = r;
		this.th = th;
		this.dead = false;
		this.newWave = false;
		this.x = 0;
		this.y = 0;
		this.vx = this.vy = 0;
		this.radius = STANDARD_RADIUS;
		this.angle = Math.PI/2;
		this.lives = 3;
		this.roundKills = 0;
		this.kills = 0;
		this.events = new ArrayList<Event>();
		this.eventPurg = new ArrayList<Event>();
	}

	public void update(){
		if(dead)
			return;
		//TODO
		
		//events
		
		//	cleanup
		for(int i = 0; i < events.size(); i++)
			if(events.get(i).isDead()){
				Event.eventEndMessage(events.get(i).getType(), this);
				events.remove(i);
				i--;
			}
		//	prepare for purgatory transfer
		if(Util.contains(eventPurg, Event.SHOW_LIVES))
			Util.remove(events, Event.SHOW_LIVES); //remove so the duration can be reset
		//	add new events from event purgatory
		for(Event e : eventPurg)
			events.add(e);
		eventPurg.clear();
		//	update
		for(Event e: events)
			e.update();
		//	application
		//		TURBO
		double VELOCITY = .3;
		if(Util.contains(events, Event.TURBO))
			VELOCITY = .8;
		
		//controls
		
		if((controls.thrustPressed() && !Util.contains(events, Event.IMMOBILE)) ||
				Util.contains(events, Event.PERMATHRUST)){
			vx += VELOCITY*Math.cos(angle);
			vy -= VELOCITY*Math.sin(angle);
			if(Util.contains(events, Event.TURBO))
				Refs.ents.addParticles(this, 1, TimedParticle.THRUST);
		}
		if(controls.brakePressed()){
			vx *= .95;
			vy *= .95;
		}
		if(controls.rightPressed())
			angle -= .09;
		if(controls.leftPressed())
			angle += .09;
		if(controls.firePressed() && !Util.contains(events, Event.NO_FIRE)
				&& !Util.contains(events, Event.EVENT_NO_FIRE)){
			fireMissile(angle, -1);
			//if has triple shot event, fire two other shots
			if(Util.contains(events, Event.TRIPLE_SHOT)){
				fireMissile(angle+Util.randomDouble(.1, .3), -1);
				fireMissile(angle-Util.randomDouble(.1, .3), -1);
			}
			//if has rapid fire event
			if(Util.contains(events, Event.RAPID_FIRE))
				events.add(new Event(Event.NO_FIRE, Util.millisToFrames(250), this));
			else
				events.add(new Event(Event.NO_FIRE, Util.millisToFrames(500), this));
		}
		if(controls.itemPressed()){
			if(item != null)
				item.execute(this);
			item = null;
		}
		//if fire key released, remove natural NO_FIRE restriction
		//if(!controls.firePressed()){
		//	Util.remove(events, Event.NO_FIRE);
		//}
		//TODO TEMP
//			if(KeyHandler.isPressed(KeyEvent.VK_F1))
//				radius -= 1;
//			if(KeyHandler.isPressed(KeyEvent.VK_F2))
//				radius += 1;
//			if(KeyHandler.isPressed(KeyEvent.VK_F3))
//				changeLives(-1);
//			if(KeyHandler.isPressed(KeyEvent.VK_F4))
//				changeLives(1);
//			if(KeyHandler.isPressed(KeyEvent.VK_F5))
//				Refs.ents.add(new EventEntity());
			if(KeyHandler.isPressed(KeyEvent.VK_F6))
				item = new Item(Item.GRAVITATING);
//			if(KeyHandler.isPressed(KeyEvent.VK_F7))
//				Refs.ents.administerEMPCharge();
		//TODO END TEMP
		x += vx;
		y += vy;
		correctBoundaryBreaches();
		dampenVelocity();
		
		//hp functions
		
		if(lives < 0){
			this.kill();
			lives = 0;
		}
	}

	public void paint(Graphics g){
		if(dead)
			return;
		//paint shield
		if(Util.contains(events, Event.SPAWN_SHIELD) || Util.contains(events, Event.SHIELD)){
			Util.paintDynamicShield(this, g);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.BOLD, 16));
			//paint SHIELD
			/*
			if(Util.contains(events, Event.SHIELD))
				g.drawString(""+Util.get(events, Event.SHIELD).getDuration(), getX()+radius, getY()+radius);
			//paint SPAWN_SHIELD
			if(Util.contains(events, Event.SPAWN_SHIELD))
				g.drawString(""+Util.get(events, Event.SPAWN_SHIELD).getDuration(), getX()+radius, getY()+radius);
				*/
		}
		//paint ship
		g.setColor(color);
		Polygon poly = getPolygon();
		g.fillPolygon(poly);
		//showing lives
		if(Util.contains(events, Event.SHOW_LIVES))
			Util.paintLives(this, g);
		//paint item
		if(item != null){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.BOLD, 13));
			g.drawString(""+item, getX()-radius, getY()+radius+13);
		}
	}
	
	public int getX(){
		return (int)x;
	}
	
	public double getVX(){
		return vx;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public double getVY(){
		return vy;
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
	
	public int getRadius(){
		return radius;
	}
	
	public double getAngle(){
		return angle;
	}
	
	public int getLives(){
		return lives;
	}
	
	public boolean outOfLives(){
		return lives < 0;
	}
	
	public boolean hasItem(){
		return item != null;
	}
	
	public void resurrect(){
		dead = false;
		angle = Math.PI/2;
		vx = vy = 0;
		radius = STANDARD_RADIUS;
		placeCentered(true);
		events.clear();
		addEvent(new Event(Event.SHOW_LIVES, Util.millisToFrames(3000), this));
		addEvent(new Event(Event.SPAWN_SHIELD, Util.millisToFrames(4000), this));
		item = null;
		if(newWave) //if first resurrection of a wave, show textfloat "+1"
			Refs.ents.add(new TextFloat("+1",
					getX()-(int)(1.5*getRadius()), getY()-(int)(1.5*getRadius()),
					Color.WHITE, Util.millisToFrames(1500)));
		newWave = false;
	}
	
	public void placeCentered(boolean displacement){
		this.x = Refs.canvas.getPlayableWidth()/2;
		this.y = Refs.canvas.getHeight()/2;
		if(displacement){
			this.x += Util.randomIntInclusive(-75, 75);
			this.y += Util.randomIntInclusive(-25, 25);
		}
	}
	
	/**
	 * have movement wrap around map/canvas
	 */
	public void correctBoundaryBreaches(){
		x = Util.correctBoundaryBreachesX(x);
		y = Util.correctBoundaryBreachesY(y);
	}
	
	public void dampenVelocity(){
		vx *= .97;
		vy *= .97;
	}
	
	public void changeLives(int dl){
		lives += dl;
		addEvent(new Event(Event.SHOW_LIVES, Util.millisToFrames(2000), this));
		if(dl > 0) //if adding lives, add textfloat "+1" or "+dl"
			Refs.ents.add(new TextFloat("+"+dl, getX()-(int)(1.5*getRadius()),
					getY()-(int)(1.5*getRadius()), Color.WHITE, Util.millisToFrames(1500)));
	}
	
	public Polygon getPolygon(){
		int x = (int)this.x;
		int y = (int)this.y;
		Polygon poly;
		int[] xA = {x+(int)(radius*Math.cos(angle)),
				x+(int)(r[0]*radius*Math.cos(th[0]+angle)),
				x+(int)(r[1]*radius*Math.cos(th[1]+angle)),
				x+(int)(r[2]*radius*Math.cos(th[2]+angle)),
				x+(int)(r[3]*radius*Math.cos(th[3]+angle)),
				x+(int)(r[3]*radius*Math.cos((-1)*th[3]+angle)),
				x+(int)(r[2]*radius*Math.cos((-1)*th[2]+angle)),
				x+(int)(r[1]*radius*Math.cos((-1)*th[1]+angle)),
				x+(int)(r[0]*radius*Math.cos((-1)*th[0]+angle))};
		int[] yA = {y-(int)(radius*Math.sin(angle)),
				y-(int)(r[0]*radius*Math.sin(th[0]+angle)),
				y-(int)(r[1]*radius*Math.sin(th[1]+angle)),
				y-(int)(r[2]*radius*Math.sin(th[2]+angle)),
				y-(int)(r[3]*radius*Math.sin(th[3]+angle)),
				y-(int)(r[3]*radius*Math.sin((-1)*th[3]+angle)),
				y-(int)(r[2]*radius*Math.sin((-1)*th[2]+angle)),
				y-(int)(r[1]*radius*Math.sin((-1)*th[1]+angle)),
				y-(int)(r[0]*radius*Math.sin((-1)*th[0]+angle))};
		poly = new Polygon(xA, yA, xA.length);
		return poly;
	}
	
	public Point[] getPoints(){
		Point[] pts = {
				new Point((int)(x+(radius*Math.cos(angle))), (int)(y-(radius*Math.sin(angle)))),
				new Point((int)(x+(r[0]*radius*Math.cos(th[0]+angle))), (int)(y-(r[0]*radius*Math.sin(th[0]+angle)))),
				new Point((int)(x+(r[1]*radius*Math.cos(th[1]+angle))), (int)(y-(r[1]*radius*Math.sin(th[1]+angle)))),
				new Point((int)(x+(r[2]*radius*Math.cos(th[2]+angle))), (int)(y-(r[2]*radius*Math.sin(th[2]+angle)))),
				new Point((int)(x+(r[3]*radius*Math.cos(th[3]+angle))), (int)(y-(r[3]*radius*Math.sin(th[3]+angle)))),
				new Point((int)(x+(r[3]*radius*Math.cos((-1)*th[3]+angle))), (int)(y-(r[3]*radius*Math.sin((-1)*th[3]+angle)))),
				new Point((int)(x+(r[2]*radius*Math.cos((-1)*th[2]+angle))), (int)(y-(r[2]*radius*Math.sin((-1)*th[2]+angle)))),
				new Point((int)(x+(r[1]*radius*Math.cos((-1)*th[1]+angle))), (int)(y-(r[1]*radius*Math.sin((-1)*th[1]+angle)))),
				new Point((int)(x+(r[0]*radius*Math.cos((-1)*th[0]+angle))), (int)(y-(r[0]*radius*Math.sin((-1)*th[0]+angle)))),
		};
		return pts;
	}
	
	public void addEvent(Event e){
		eventPurg.add(e);
	}
	
	public boolean isVulnerable(){
		for(Event e : events)
			if(e.getType() == Event.SPAWN_SHIELD ||
					e.getType() == Event.SHIELD)
				return false;
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	public void fireMissile(double theta, int itemEffect){
		Refs.ents.add(new Missile((int)(x+radius*Math.cos(angle)), 
				(int)(y-radius*Math.sin(angle)), color, theta, 8,
				Util.millisToFrames(4000), this, Util.contains(events, Event.OSC_MISSILES), itemEffect));
	}
	
	public void fireMissile(double theta, int itemEffect, int frames){
		Refs.ents.add(new Missile((int)(x+radius*Math.cos(angle)), 
				(int)(y-radius*Math.sin(angle)), color, theta, 8,
				frames, this, Util.contains(events, Event.OSC_MISSILES), itemEffect));
	}
	
	public ArrayList<Event> getEvents(){
		return events;
	}
	
	public void giveItem(int type){
		item = new Item(type);
	}
	
	public void resetForWave(){
		newWave = true;
		roundKills = 0;
		radius = STANDARD_RADIUS;
		changeLives(1);
	}
	
	public void addKill(){
		roundKills++;
		kills++;
	}
	
	public int getRoundKills(){
		return roundKills;
	}
	
	public int getKills(){
		return kills;
	}
	
	public void denegateLives(){
		if(lives < 0)
			lives = 0;
	}
	
	public void changeRadius(int dr){
		radius += dr;
	}
	
	public void setRadius(int r){
		radius = r;
	}
}