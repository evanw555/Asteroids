import java.awt.Color;


public class Item{
	public static final int CLUSTER = 0, SPLITTING = 1, GRAVITATING = 2;
	private int type;
	
	public Item(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public String toString(){
		switch(type){
		case CLUSTER:
			return "CLUSTER";
		case SPLITTING:
			return "SPLITTING";
		case GRAVITATING:
			return "GRAVITATING";
		default:
			return "ITEM";
		}
	}
	
	public void execute(Player parent){
		switch(type){
		case CLUSTER:
			Refs.ents.add(new ClusterMissile(parent.getX(), parent.getY(), parent.getAngle(), parent));
			break;
		case SPLITTING:
			parent.fireMissile(parent.getAngle()+.01, Item.SPLITTING);
			parent.fireMissile(parent.getAngle()-.01, Item.SPLITTING);
			break;
		case GRAVITATING:
			for(int i = 0; i < 6; i++)
				parent.fireMissile(parent.getAngle()-.25+(.1*i), Item.GRAVITATING, Util.millisToFrames(14000));
			break;
		}
	}
	
	public static void displayItemMessage(Player p, int type){
		switch(type){
		case CLUSTER:
			Refs.log.add(new LogEntry(p.getName()+" has acquired a cluster missile!", Color.LIGHT_GRAY));
			break;
		case SPLITTING:
			Refs.log.add(new LogEntry(p.getName()+" has acquired a splitting missile!", Color.LIGHT_GRAY));
			break;
		case GRAVITATING:
			Refs.log.add(new LogEntry(p.getName()+" has acquired gravitating missiles!", Color.LIGHT_GRAY));
		}
	}
	
	public static void solicitRandomItem(Player p){
		if(p.hasItem()){
			Refs.log.add(new LogEntry(p.getName()+" already has an item", Color.PINK));
			return;
		}
		int chance = Util.randomIntInclusive(0, 2); //CLUSTER, SPLITTING, GRAVITATING
		p.giveItem(chance);
		displayItemMessage(p, chance);
	}
}