import java.awt.Color;


public class Event{
	public static final int SHOW_LIVES = 0, NO_FIRE = 1, SPAWN_SHIELD = 2, TURBO = 3, TRIPLE_SHOT = 4, SHIELD = 5, EVENT_NO_FIRE = 6, RAPID_FIRE = 7, PERMATHRUST = 8, IMMOBILE = 9, SHRINK = 10, GROW = 11, OSC_MISSILES = 12, EXTRA_LIFE = 13;
	//                                    for chance events
	public static final int[] durations = {-1, //SHOW_LIVES
										   -1,//NO_FIRE
										   -1,//SPAWN_SHIELD
					Util.millisToFrames(10000), //TURBO
					Util.millisToFrames(13000), //TRIPLE_SHOT
					Util.millisToFrames(10000), //SHIELD
					Util.millisToFrames(8000), //EVENT_NO_FIRE
					Util.millisToFrames(13000), //RAPID_FIRE
					Util.millisToFrames(10000), //PERMATHRUST
					Util.millisToFrames(8000), //IMMOBILE
					Util.millisToFrames(20000), //SHRINK
					Util.millisToFrames(20000), //GROW
					Util.millisToFrames(13000), //OSC_MISSILES
					1}; //EXTRA_LIFE
	
	private int type, initialDur, duration;
	private Player parent;
	
	public Event(int type, int duration, Player parent){
		this.type = type;
		this.initialDur = duration;
		this.duration = duration;
		this.parent = parent;
	}
	
	public void update(){
		duration--;
		switch(type){
		case SHRINK:
			if(duration%10 == 0){
				if(duration > durations[SHRINK]*.75)
					parent.changeRadius(-1);
				else if(duration < durations[SHRINK]*.25)
					parent.changeRadius(1);
			}else if(duration <= 1)
				parent.setRadius(Player.STANDARD_RADIUS);
			break;
		case GROW:
			if(duration%10 == 0){
				if(duration > durations[GROW]*.75)
					parent.changeRadius(1);
				else if(duration < durations[GROW]*.25)
					parent.changeRadius(-1);
			}else if(duration <= 1)
				parent.setRadius(Player.STANDARD_RADIUS);
			break;
		case EXTRA_LIFE:
			if(duration == 0)
				parent.changeLives(1);
			break;
		}
	}
	
	public boolean isDead(){
		return duration <= 0;
	}
	
	public int getType(){
		return type;
	}
	
	public long getInitialDuration(){
		return initialDur;
	}
	
	public long getDuration(){
		return duration;
	}
	
	public static void eventBeginMessage(int type, Player parent){
		switch(type){
		case -1:
			Refs.log.add(new LogEntry(parent.getName()+" gets nothing!", Color.PINK));
			break;
		case TURBO:
			Refs.log.add(new LogEntry(parent.getName()+" has acquired turbo!", Color.CYAN));
			Refs.ents.add(new TextFloat("TURBO", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case TRIPLE_SHOT:
			Refs.log.add(new LogEntry(parent.getName()+" has acquired triple-shot!", Color.CYAN));
			Refs.ents.add(new TextFloat("TRIPLE SHOT", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case SHIELD:
			Refs.log.add(new LogEntry(parent.getName()+" has acquired a shield!", Color.CYAN));
			Refs.ents.add(new TextFloat("SHIELD", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case EVENT_NO_FIRE:
			Refs.log.add(new LogEntry(parent.getName()+"'s gun has broken!", Color.CYAN));
			Refs.ents.add(new TextFloat("NO FIRE", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case RAPID_FIRE:
			Refs.log.add(new LogEntry(parent.getName()+" has acquired rapid fire!", Color.CYAN));
			Refs.ents.add(new TextFloat("RAPID FIRE", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case PERMATHRUST:
			Refs.log.add(new LogEntry(parent.getName()+"'s thrust is stuck on!", Color.CYAN));
			Refs.ents.add(new TextFloat("PERMATHRUST", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case IMMOBILE:
			Refs.log.add(new LogEntry(parent.getName()+" is now immobile!", Color.CYAN));
			Refs.ents.add(new TextFloat("IMMOBILE", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case SHRINK:
			Refs.log.add(new LogEntry(parent.getName()+" has been shrunk!", Color.CYAN));
			Refs.ents.add(new TextFloat("SHRINK", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case GROW:
			Refs.log.add(new LogEntry(parent.getName()+" is growing!", Color.CYAN));
			Refs.ents.add(new TextFloat("GROW", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case OSC_MISSILES:
			Refs.log.add(new LogEntry(parent.getName()+" has acquired oscillating missiles!", Color.CYAN));
			Refs.ents.add(new TextFloat("OSCILLATING MISSILES", parent.getX(), parent.getY(), Color.WHITE, Util.millisToFrames(2000)));
			break;
		case EXTRA_LIFE:
			Refs.log.add(new LogEntry(parent.getName()+" has gained an extra life!", Color.CYAN));
			break;
		}
	}
	
	public static void eventEndMessage(int type, Player parent){
		switch(type){
		case TURBO:
			Refs.log.add(new LogEntry(parent.getName()+" has run out of turbo", Color.GREEN));
			break;
		case TRIPLE_SHOT:
			Refs.log.add(new LogEntry(parent.getName()+" has run out of triple-shot", Color.GREEN));
			break;
		case SHIELD:
			Refs.log.add(new LogEntry(parent.getName()+" has run out of shield", Color.GREEN));
			break;
		case EVENT_NO_FIRE:
			Refs.log.add(new LogEntry(parent.getName()+"'s gun has been fixed", Color.GREEN));
			break;
		case RAPID_FIRE:
			Refs.log.add(new LogEntry(parent.getName()+" has run out of rapid fire", Color.GREEN));
			break;
		case PERMATHRUST:
			Refs.log.add(new LogEntry(parent.getName()+"'s thrust has been fixed", Color.GREEN));
			break;
		case IMMOBILE:
			Refs.log.add(new LogEntry(parent.getName()+" is now mobile", Color.GREEN));
			break;
		case SHRINK:
		case GROW:
			Refs.log.add(new LogEntry(parent.getName()+" has returned to normal size", Color.GREEN));
			break;
		case OSC_MISSILES:
			Refs.log.add(new LogEntry(parent.getName()+" now has normal missiles", Color.GREEN));
			break;
		case EXTRA_LIFE:
			break;
		}
	}
	
	public static void solicitRandomEvent(Player p){
		boolean done = false;
		int count = 0;
		//try to give random events until one that is not already equipped is selected
		do{
			int chance = Util.randomIntInclusive(3, 13); //TURBO, TRIPLE_SHOT, SHIELD, EVENT_NO_FIRE, RAPID_FIRE, PERMATHRUST, IMMOBILE, SHRINK, GROW, OSC_MISSILES, EXTRA_LIFE
			//int chance = OSC_MISSILES; //TODO DEBUG
			if(!Util.contains(p.getEvents(), chance)){
				p.getEvents().add(new Event(chance, durations[chance], p));
				Event.eventBeginMessage(chance, p);
				done = true;
			//if tried 10 times, give up; get nothing
			}else if(count > 10){
				Event.eventBeginMessage(-1, p);
				done = true;
			}
			count++;
		}while(!done);
	}
	
	public String toString(){
		switch(type){
		case SHOW_LIVES:
			return "SHOW LIVES";
		case NO_FIRE:
			return "NO FIRE";
		case SPAWN_SHIELD:
			return "SPAWN SHIELD";
		case TURBO:
			return "TURBO";
		case TRIPLE_SHOT:
			return "TRIPLE SHOT";
		case SHIELD:
			return "SHIELD";
		case EVENT_NO_FIRE:
			return "NO FIRE";
		case RAPID_FIRE:
			return "RAPID FIRE";
		case PERMATHRUST:
			return "PERMATHRUST";
		case IMMOBILE:
			return "IMMOBILE";
		case SHRINK:
			return "SHRINK";
		case GROW:
			return "GROW";
		case OSC_MISSILES:
			return "OSCILLATING MISSILES";
		case EXTRA_LIFE:
			return "EXTRA LIFE";
		}
		return "EVENT";
	}
}
