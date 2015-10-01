
public class TimedEntityShell{
	private Entity ent;
	private long duration;
	
	public TimedEntityShell(Entity ent, long duration){
		this.ent = ent;
		this.duration = duration;
	}
	
	public void update(){
		duration--;
	}
	
	public Entity getEntity(){
		if(ent instanceof Player)
			((Player)ent).resurrect();
		return ent;
	}
	
	public boolean isExpired(){
		return duration <= 0;
	}
}
