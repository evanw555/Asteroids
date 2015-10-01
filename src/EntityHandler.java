import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class EntityHandler{
	private ArrayList<Entity> ents, purgatory;
	private ArrayList<TimedEntityShell> timedPurgatory;
	private Player[] players;
	private int asteroidsDestroyed;
	private long framesSurpassed;
	
	public EntityHandler(Player...players){
		ents = new ArrayList<Entity>();
		purgatory = new ArrayList<Entity>();
		timedPurgatory = new ArrayList<TimedEntityShell>();
		this.players = players;
		this.asteroidsDestroyed = 0;
		this.framesSurpassed = 0;
	}
	
	public void update(){
		framesSurpassed++;
		//update each timedEntityShell on timedPurgatory
		for(TimedEntityShell e : timedPurgatory)
			e.update();
		//unpack entities from expired timedEntityShells
		for(int i = 0; i < timedPurgatory.size(); i++)
			if(timedPurgatory.get(i).isExpired()){
				purgatory.add(timedPurgatory.remove(i).getEntity());
				i--;
			}
		//remove duplicates of players
		removePlayerDuplicates();
		//transfer each entity from purgatory to ents
		for(Entity e : purgatory)
			ents.add(e);
		purgatory.clear();
		//update each entity on ents
		for(Entity e : ents)
			e.update();
		//check collisions
		checkCollisions();
		//handle random entity addition if not between waves
		if(!Refs.game.isInterwavePhase())
			addRandomEntities();
	}
	
	public void cleanUp(){
		for(int i = 0; i < ents.size(); i++)
			if(ents.get(i).isDead()){
				ents.remove(i);
				i--;
			}
	}
	
	public void paint(Graphics g){
		for(Entity e : ents)
			e.paint(g);
		//game over text
		if(!Util.contains(ents, Player.class) && timedPurgatory.isEmpty()){
			g.setColor(Color.RED);
			g.setFont(new Font("Impact", Font.BOLD, 100));
			g.drawString("GAME OVER", Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2);
			g.setFont(new Font("Impact", Font.BOLD, 30));
			g.drawString("ASTEROIDS DESTROYED: "+asteroidsDestroyed,
					Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2+70);
			g.drawString("YOU LASTED "+Util.framesToTime(framesSurpassed),
					Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2+100);
			g.setFont(new Font("Impact", Font.BOLD, 20));
			g.drawString("Created by Evan Williams",
					Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2+150);
			g.drawString("Began 23 April 2012",
					Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2+180);
			g.drawString("Finished 11 June 2012",
					Refs.canvas.getPlayableWidth()/4, Refs.canvas.getHeight()/2+210);
		}
	}
	
	public void add(Entity e){
		purgatory.add(e);
	}
	
	public void checkCollisions(){
		for(int i = 0; i < ents.size(); i++)
			for(int k = i+1; k < ents.size(); k++){
				Entity a = ents.get(i);
				Entity b = ents.get(k);
				if(a != b && !(a instanceof TimedParticle) && !(b instanceof TimedParticle)){
					//between player and missile/shard ===============================================================================================
					if(Util.isCollisionType(a, b, Player.class, Missile.class) ||
							Util.isCollisionType(a, b, Player.class, Shard.class)){
						//assign references and save original (precasted) class type
						Player player;
						Missile missile;
						Class type;
						if(a instanceof Player){
							type = b.getClass();
							player = (Player)a;
							missile = (Missile)b;
						}else{
							type = a.getClass();
							player = (Player)b;
							missile = (Missile)a;
						}
						//if missile within player's polygon but isn't of parent ship, and if both entities are alive
						if(missile.getParent() != player && Util.colliding(player, missile, type) &&
								!missile.isDead() && !player.isDead()){
							//kill missile
							missile.kill();
							//if player is vulnerable, kill player
							if(player.isVulnerable()){
								player.kill();
								player.changeLives(-1);
								addParticles(player, 20, TimedParticle.EXPLOSION);
								if(missile.getParent() == null)
									Refs.log.add(new LogEntry(
											player.getName()+" was hit by a comet shard",
											Color.RED));
								else
									Refs.log.add(new LogEntry(
											player.getName()+" was killed by "+
											missile.getParent().getName(),
											Color.RED));
								respawnProcedure(player);
							}
						}
					//between two players ======================================================================================================
					}else if(Util.isCollisionType(a, b, Player.class, Player.class)){
						//assign references
						Player p1 = (Player)a;
						Player p2 = (Player)b;
						//if both players' polygons are intersecting
						if(Util.withinRange(p1, p2) 
								&& Util.colliding(p1, p2)){
							//if both players are vulnerable, kill both players
							if(p1.isVulnerable() && p2.isVulnerable()){
								p1.kill();
								p2.kill();
								p1.changeLives(-1);
								p2.changeLives(-1);
								addParticles(p1, 30, TimedParticle.EXPLOSION);
								addParticles(p2, 30, TimedParticle.EXPLOSION);
								Refs.log.add(new LogEntry(
										p1.getName()+" and "+
										p2.getName()+" crashed into each other",
										Color.RED));
								respawnProcedure(p1);
								respawnProcedure(p2);
							}
						}
					//between a player and an event ball =======================================================================================
					}else if(Util.isCollisionType(a, b, Player.class, EventEntity.class)){
						//assign references
						Player player;
						EventEntity event;
						if(a instanceof Player){
							player = (Player)a;
							event = (EventEntity)b;
						}else{
							player = (Player)b;
							event = (EventEntity)a;
						}
						//if both are within range, and if points of ship in circle
						if(Util.withinRange(player, event) && Util.colliding(player, event)){
							event.kill();
							Event.solicitRandomEvent(player);
						}
					//between a player and an item ball ========================================================================================
					}else if(Util.isCollisionType(a, b, Player.class, ItemEntity.class)){
						//assign references
						Player player;
						ItemEntity item;
						if(a instanceof Player){
							player = (Player)a;
							item = (ItemEntity)b;
						}else{
							player = (Player)b;
							item = (ItemEntity)a;
						}
						//if both are within range, and if points of ship in circle
						if(Util.withinRange(player, item) && Util.colliding(player, item)){
							item.kill();
							Item.solicitRandomItem(player);
						}
					//between a player and an asteroid/comet/electrode/amoeba =========================================================================================
					}else if(Util.isCollisionType(a, b, Player.class, Asteroid.class) ||
							Util.isCollisionType(a, b, Player.class, Comet.class) ||
							Util.isCollisionType(a, b, Player.class, Electrode.class) ||
							Util.isCollisionType(a, b, Player.class, Amoeba.class)){
						//assign references and save original (precasted) type
						Player player;
						Asteroid asteroid;
						Class type;
						if(a instanceof Player){
							type = b.getClass();
							player = (Player)a;
							asteroid = (Asteroid)b;
						}else{
							type = a.getClass();
							player = (Player)b;
							asteroid = (Asteroid)a;
						}
						//if asteroid isn't an electrode, or it's an electrode and it's active
						if(type != Electrode.class || ((Electrode)asteroid).isHostile())
							//if both are within range, and if points of ship in points of asteroid
							if(Util.withinRange(player, asteroid) && Util.colliding(player, asteroid)){
								//if player is vulnerable, kill player and react to to momentum
								if(player.isVulnerable()){
									//react to momentum if not of type electrode
									if(type != Electrode.class)
										asteroid.reactToForeignMomentum(.5*Math.pow(player.getRadius(), 2),
											player.getVX(), player.getVY());
									//kill player
									player.kill();
									player.changeLives(-1);
									addParticles(player, 25, TimedParticle.EXPLOSION);
									if(type == Asteroid.class)
										Refs.log.add(new LogEntry(
												player.getName()+" has crashed into an asteroid",
												Color.RED));
									else if(type == Comet.class)
										Refs.log.add(new LogEntry(
											player.getName()+" has crashed into a comet",
											Color.RED));
									else if(type == Electrode.class)
										Refs.log.add(new LogEntry(
											player.getName()+" has collided with an electrode's current",
											Color.RED));
									else if(type == Amoeba.class)
										Refs.log.add(new LogEntry(
											player.getName()+" has collided with an amoeba",
											Color.RED));
									respawnProcedure(player);
								}
							}
					//between a missile and an asteroid =========================================================================================
					}else if(Util.isCollisionType(a, b, Missile.class, Asteroid.class)){
						//assign references
						Missile missile;
						Asteroid asteroid;
						if(a instanceof Missile){
							missile = (Missile)a;
							asteroid = (Asteroid)b;
						}else{
							missile = (Missile)b;
							asteroid = (Asteroid)a;
						}
						//if both entities alive, if missile within range, and if points of missile within polygon of the asteroid
						if(!missile.isDead() && !asteroid.isDead() &&
								Util.withinRange(missile, asteroid) && Util.colliding(missile, asteroid)){
							//if asteroid's radius is bigger than requirement, create fragments
							if(asteroid.getRadius() >= Util.MIN_REPRODUCTION_RADIUS)
								createAsteroidFragments(asteroid);
							//add number of particles based on asteroid's radius
							addParticles(asteroid, asteroid.getRadius(), TimedParticle.ROCK_DEBRIS);
							//kill missile and asteroid
							missile.kill();
							asteroid.kill();
							missile.getParent().addKill();
							asteroidsDestroyed++;
						}
					//between a missile and a comet =========================================================================================
					}else if(Util.isCollisionType(a, b, Missile.class, Comet.class)){
						//assign references
						Missile missile;
						Comet comet;
						if(a instanceof Missile){
							missile = (Missile)a;
							comet = (Comet)b;
						}else{
							missile = (Missile)b;
							comet = (Comet)a;
						}
						//if both entities alive, if missile within range, and if points of missile within polygon of the asteroid
						if(!missile.isDead() && !comet.isDead() &&
								Util.withinRange(missile, comet) && Util.colliding(missile, comet)){
							//create comet shards
							createCometShards(comet);
							//add number of particles based on asteroid's radius
							addParticles(comet, comet.getRadius(), TimedParticle.ROCK_DEBRIS);
							//kill missile and asteroid
							missile.kill();
							comet.kill();
							missile.getParent().addKill();
							asteroidsDestroyed++;
						}
					//between a missile and an electrode =========================================================================================
					}else if(Util.isCollisionType(a, b, Missile.class, Electrode.class)){
						//assign references
						Missile missile;
						Electrode elec;
						if(a instanceof Missile){
							missile = (Missile)a;
							elec = (Electrode)b;
						}else{
							missile = (Missile)b;
							elec = (Electrode)a;
						}
						//if both entities alive, if missile within range, and if points of missile within oval of the electrode
						if(!missile.isDead() && !elec.isDead() &&
								Util.withinRange(missile, elec) && Util.colliding(missile, elec)){
							if(elec.isDeactivator()){
								//if electrode is a deactivator, administer charge
								Refs.ents.administerEMPCharge();
								//add number of EMP particles based on electrode's radius
								addParticles(elec, 5*elec.getInnerRadius(), TimedParticle.EMP);
							}else{
								//add number of particles based on electrode's radius
								addParticles(elec, elec.getInnerRadius(), TimedParticle.ELECTRIC_DEBRIS);
							}
							//kill missile and electrode
							missile.kill();
							elec.kill();
							missile.getParent().addKill();
							asteroidsDestroyed++;
						}
					//between a missile and an amoeba =========================================================================================
					}else if(Util.isCollisionType(a, b, Missile.class, Amoeba.class)){
						//assign references
						Missile missile;
						Amoeba amoeba;
						if(a instanceof Missile){
							missile = (Missile)a;
							amoeba = (Amoeba)b;
						}else{
							missile = (Missile)b;
							amoeba = (Amoeba)a;
						}
						//if both entities alive, if missile within range, and if points of missile within polygon of the amoeba
						if(!missile.isDead() && !amoeba.isDead() &&
								Util.withinRange(missile, amoeba) && Util.colliding(missile, amoeba)){
							//increase amoeba's radius
							amoeba.changeRadius(Util.randomIntInclusive(4, 8));
							//kill missile
							missile.kill();
							//if amoeba's radius is too big, kill it
							if(amoeba.hasLethalRadius()){
								//add number of particles based on asteroid's radius
								addParticles(amoeba, amoeba.getRadius(), TimedParticle.ROCK_DEBRIS);
								//kill amoeba
								amoeba.kill();
								//add kills
								missile.getParent().addKill();
								asteroidsDestroyed++;
							}
						}
					//between two amoebas =========================================================================================
					}else if(Util.isCollisionType(a, b, Amoeba.class, Amoeba.class)){
						//only execute if ONE is infected
						if((((Amoeba)a).isInfected() && !((Amoeba)b).isInfected()) ||
								(!((Amoeba)a).isInfected() && ((Amoeba)b).isInfected())){
							//assign references
							Amoeba inf;
							Amoeba uninf;
							if(((Amoeba)a).isInfected()){
								inf = (Amoeba)a;
								uninf = (Amoeba)b;
							}else{
								inf = (Amoeba)b;
								uninf = (Amoeba)a;
							}
							//if both entities alive, if amoebas within range of each other,
							//and if the uninfected amoeba isn't larger than the death radius
							if(!inf.isDead() && !uninf.isDead() && Util.withinRange(inf, uninf) &&
									!uninf.hasLethalRadius()){
								//infect the uninfected amoeba
								uninf.infect();
							}
						}
						/*
						//if both entities alive, if missile within range, and if points of missile within polygon of the amoeba
						if(!missile.isDead() && !amoeba.isDead() &&
								Util.withinRange(missile, amoeba) && Util.colliding(missile, amoeba)){
							//increase amoeba's radius
							amoeba.changeRadius(Util.randomIntInclusive(3, 6));
							//kill missile
							missile.kill();
							//if amoeba's radius is too big, kill it
							if(amoeba.hasLethalRadius()){
								//add number of particles based on asteroid's radius
								addParticles(amoeba, amoeba.getRadius(), TimedParticle.ROCK_DEBRIS);
								//kill amoeba
								amoeba.kill();
								//add kills
								missile.getParent().addKill();
								asteroidsDestroyed++;
							}
						}
						*/
					}
				}
			}
	}
	
	public void addRandomEntities(){
		int chance;
		//events
		chance = Util.randomIntInclusive(0, 16*Refs.game.getFrameRate()); //0-16FR
		if(chance == 0)
			add(new EventEntity());
		//items
		chance = Util.randomIntInclusive(0, 14*Refs.game.getFrameRate()); //0-14FR
		if(chance == 0)
			add(new ItemEntity());
		//asteroids
		chance = Util.randomIntInclusive(0, 40*Refs.game.getFrameRate()); //0-4FR
		//TODO TEMP
			if(KeyHandler.isPressed(KeyEvent.VK_F8))
				add(new Asteroid());
			if(KeyHandler.isPressed(KeyEvent.VK_F9))
				add(new Stalker());
		//TODO END TEMP
		if(chance <= 14)
			addAsteroidOfType(Refs.game.getWave().getAsteroidType(Util.randomDouble(0, 1)));
	}
	
	public void respawnProcedure(Player p){
		if(!p.outOfLives())
			timedPurgatory.add(new TimedEntityShell(p, Util.millisToFrames(2500)));
		else
			Refs.log.add(new LogEntry(p.getName()+" has run out of lives", Color.RED));
	}
	
	public void addParticles(Entity e, int amount, int type){
		//radius of initial displacement of particles
		int radius = getParticleDisplacementRadius(e, type);
		for(int i = 0; i < amount; i++){
			double rdmAngle = Util.randomDouble(0, Math.PI*2);
			purgatory.add(new TimedParticle(type,
					e.getX()+Util.randomXInCircle(radius, rdmAngle), 
					e.getY()+Util.randomYInCircle(radius, rdmAngle), 
					e, Util.millisToFrames(Util.randomIntInclusive(500, 2500))));
		}
	}
	
	public int getParticleDisplacementRadius(Entity e, int type){
		//if it's rock debris from an asteroid/comet, set to half radius of asteroid
		if(e instanceof Asteroid && type == TimedParticle.ROCK_DEBRIS)
			return ((Asteroid)e).getRadius()/2;
		//if it's electric debris from an electrode, set to half inner-radius of electrode
		if(e instanceof Electrode && type == TimedParticle.ELECTRIC_DEBRIS)
			return ((Electrode)e).getInnerRadius()/2;
		//if it's the explosion from a player, set to half radius of player
		if(e instanceof Player && type == TimedParticle.EXPLOSION)
			return ((Player)e).getRadius()/2;
		//default is zero
		return 0;
	}
	
	public void createAsteroidFragments(Asteroid a){
		//declare spawn asteroids
		Asteroid s1, s2;
		//figure out radii of each spawn asteroid
		int radius1 = (int)(a.getRadius()*Util.randomDouble(.25, .75));
		int radius2 = a.getRadius()-radius1;
		//figure out x-velocities of each spawn asteroid
		double vx1 = a.getVX()*Util.randomDouble(.25, .75);
		double vx2 = a.getVX()-vx1;
		//figure out y-velocities of each spawn asteroid
		double vy1 = a.getVY()*Util.randomDouble(.25, .75);
		double vy2 = a.getVY()-vx1;
		//initialize each asteroid
		s1 = new Asteroid(radius1,
				a.getX()+Util.randomIntInclusive((-1)*a.getRadius()/2, a.getRadius())/2,
				a.getY()+Util.randomIntInclusive((-1)*a.getRadius()/2, a.getRadius())/2,
				vx1, vy1);
		s2 = new Asteroid(radius2,
				a.getX()+Util.randomIntInclusive((-1)*a.getRadius()/2, a.getRadius())/2,
				a.getY()+Util.randomIntInclusive((-1)*a.getRadius()/2, a.getRadius())/2,
				vx2, vy2);
		//add asteroids to world if their radii are larger than 12
		if(s1.getRadius() >= Util.MIN_ASTEROID_RADIUS)
			purgatory.add(s1);
		if(s2.getRadius() >= Util.MIN_ASTEROID_RADIUS)
			purgatory.add(s2);
	}
	
	public void createCometShards(Comet c){
		int numShards = c.getRadius()/4;
		for(int i = 0; i < numShards; i++){
			purgatory.add(new Shard(c.getX(), c.getY(), 
					c.getColor(), Util.randomDouble(-.4, .4)+((2*Math.PI)/numShards)*i,
					Util.randomDouble(4.5, 6), 
					Util.millisToFrames(Util.randomIntInclusive(1200, 2200)), null));
		}
	}
	
	public void openingActions(){
		//show all players' lives
		for(Entity e : purgatory)
			if(e instanceof Player){
				((Player)e).addEvent(new Event(Event.SHOW_LIVES, Util.millisToFrames(3000),(Player)e));
				((Player)e).addEvent(new Event(Event.SPAWN_SHIELD, Util.millisToFrames(4000),(Player)e));
				
			}
	}
	
	public Player getPlayer(int index){
		try{
			return players[index];
		}catch(Exception e){
			return null;
		}
	}
	
	public int getNumPlayers(){
		return players.length;
	}
	
	public void resetPlayersForWave(){
		for(Player p : players)
			p.resetForWave();
	}
	
	public void resetForWave(){
		purgatory.clear();
		for(Entity e : ents){
			if(Util.emitsInterwaveParticles(e))
				addParticles(e, 10, TimedParticle.WAVE_END);
			e.kill();
		}
		timedPurgatory.clear();
		for(Player p : players){
			p.denegateLives();
			timedPurgatory.add(new TimedEntityShell(p, Util.INTERWAVE_FRAMES));
		}
	}
	
	public void addPlayersToInterwaveShells(){
		for(Player p : players)
			timedPurgatory.add(new TimedEntityShell(p, Util.INTERWAVE_FRAMES));
	}
	
	public void removePlayerDuplicates(){
		int count;
		for(Player p : players){
			count = 0;
			for(int i = 0; i < ents.size(); i++)
				if(ents.get(i) == p){
					count++;
					if(count > 1){
						ents.remove(i);
						i--;
					}
				}
		}
	}
	
	public void addTimedEntityShell(TimedEntityShell e){
		timedPurgatory.add(e);
	}
	
	public void administerEMPCharge(){
		Refs.log.add(new LogEntry("An EMP shockwave has disabled some electrodes!", Color.LIGHT_GRAY));
		double charge = Util.randomDouble(.6, 1);
		for(Entity e : ents)
			if(e instanceof Electrode)
				((Electrode) e).attemptToDeactivate(charge);
	}
	
	public void addAsteroidOfType(int type){
		switch(type){
		case Asteroid.ASTEROID:
			add(new Asteroid());
			break;
		case Asteroid.COMET:
			add(new Comet());
			break;
		case Asteroid.ELECTRODE:
			add(new Electrode());
			break;
		case Asteroid.AMOEBA:
			add(new Amoeba());
			break;
		}
	}
	
	public Asteroid getRandomAsteroid(){
		ArrayList<Asteroid> result = new ArrayList<Asteroid>();
		for(Entity e : ents)
			if(e instanceof Asteroid)
				result.add((Asteroid)e);
		if(result.isEmpty())
			return null;
		return result.get(Util.randomIntInclusive(0, result.size()-1));
	}
	
	/**
	 * MAY RETURN NULL, EVEN IF VALID PLAYERS EXIST
	 * @return a random player from the existing living entities
	 */
	public Player getRandomPlayer(){
		for(Player p : players)
			if(!p.isDead())
				if(Util.randomDouble(0, 1) < .5)
					return p;
		return null;
	}
}