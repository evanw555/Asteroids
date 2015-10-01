
public class Wave{
	public static final int REGULAR = 0, ICE_FLOE = 1, ELECTRIC_FIELD = 2, SWARM = 3;
	private int stage, killsNeeded, type;
	private boolean[] typeSeen = {true, false, false, false};
	private double[] spawnChances = {.7, .8, .9, 1};
	private int[] nextWaves;
	
	public Wave(){
		stage = 1;
		killsNeeded = getKillsNeeded(1);
		type = REGULAR;
		spawnChances = new double[4];
		nextWaves = new int[4];
	}
	
	public void advance(){
		stage++;
		killsNeeded = getKillsNeeded(stage);
		type = nextWaves[0];
		moveNextWavesForward();
		typeSeen[type] = true;
		updateSpawnChances();
	}
	
	public int getStage(){
		return stage;
	}
	
	public int getKillsNeeded(int stage){
		int sum = 10;
		for(int i = 0; i < stage; i++)
			sum += i;
		return sum;
	}
	
	public int getKillsNeeded(){
		return killsNeeded;
	}
	
	public String getTitle(){
		return "Wave "+stage;
	}
	
	public String getSubtitle(){
		switch(type){
		case ICE_FLOE:
			return "Ice Floe";
		case ELECTRIC_FIELD:
			return "Electric Field";
		case SWARM:
			return "Swarm";
		}
		return "";
	}
	
	public String getSubtitleAt(int ahead){
		if(ahead == 0)
			return getSubtitle();
		int tempType = nextWaves[ahead-1];
		switch(tempType){
		case ICE_FLOE:
			return "Ice Floe";
		case ELECTRIC_FIELD:
			return "Electric Field";
		case SWARM:
			return "Swarm";
		}
		return "";
	}
	
	public int getKillsRemaining(){
		//if 1 player
		if(Refs.ents.getNumPlayers() == 0)
			return killsNeeded - Refs.ents.getPlayer(0).getRoundKills();
		//if 2 players
		return killsNeeded - Refs.ents.getPlayer(0).getRoundKills()
						   - Refs.ents.getPlayer(1).getRoundKills();
	}
	
	public void moveNextWavesForward(){
		for(int i = 0; i < nextWaves.length-1; i++)
			nextWaves[i] = nextWaves[i+1];
		nextWaves[nextWaves.length-1] = chooseWaveType();
	}
	
	public int chooseWaveType(){
		
		//TODO TEMP
		//return Util.randomIntInclusive(ICE_FLOE, SWARM);
		
		double chance = Util.randomDouble(0, 1);
		//REGULAR
		if(chance < .7)
			return REGULAR;
		//ICE_FLOE
		else if(chance < .8)
			return ICE_FLOE;
		//ELECTRIC_FIELD
		else if(chance < .9)
			return ELECTRIC_FIELD;
		//SWARM
		else
			return SWARM;
	}
	
	public void updateSpawnChances(){
		double[] result = new double[4];
		//REGULAR
		if(type == REGULAR){
			result[0] = .7;
			result[1] = .8;
			result[2] = .9;
			result[3] = 1;
		//ICE_FLOE
		}else if(type == ICE_FLOE){
			result[0] = .05;
			result[1] = 1;
			result[2] = 0;
			result[3] = 0;
		//ELECTRIC_FIELD
		}else if(type == ELECTRIC_FIELD){
			result[0] = .05;
			result[1] = 0;
			result[2] = 1;
			result[3] = 0;
		//SWARM
		}else if(type == SWARM){
			result[0] = .05;
			result[1] = 0;
			result[2] = 0;
			result[3] = 1;
		}
		//if wave of a certain type hasn't been encountered, nullify that type's chances
		for(int i = ICE_FLOE; i < SWARM+1; i++)
			if(!typeSeen[i])
				result[i] = 0;
		//return chances
		spawnChances = result;
	}
	
	public int getAsteroidType(double chance){
		for(int i = 0; i < spawnChances.length; i++)
			if(chance < spawnChances[i])
				return i;
		return REGULAR;
	}
	
	public boolean isWaveType(int type){
		return this.type == type;
	}
}
