import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Game{
	private final int FRAME_RATE = 24; //24 FPS
	private final int FRAME_DELAY = 1000/FRAME_RATE;
	private int interwaveCountdown;
	private long updateStartMillis, updateEndMillis, updateDelayMillis;
	private boolean paused;
	private EntityHandler ents;
	private GameCanvas canvas;
	private TextLog log;
	private JFrame frame;
	private Wave wave;
	
	public Game(int width, int height, Player...players){
		ents = new EntityHandler(players);
		Refs.setEntityHandler(ents);
		
		log = new TextLog();
		Refs.setTextLog(log);
		
		canvas = new GameCanvas(width-64, height-64);
		Refs.setGameCanvas(canvas);
		for(Player p : players)
			p.placeCentered(true);
		
		frame = new JFrame();
		frame.setTitle("Asteroids");
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.toFront();
		canvas.requestFocus();
		frame.setVisible(true);
	}
	
	public void run(){
		Refs.setGame(this);
		ents.openingActions();
		wave = new Wave();
		paused = false;
		Util.setInterwaveFrames(Util.millisToFrames(2000));
		interwaveCountdown = Util.INTERWAVE_FRAMES;
		ents.addPlayersToInterwaveShells();
		updateStartMillis = updateEndMillis = updateDelayMillis = 0;
		while(true){
			while(paused){
				canvas.repaint();
				Util.sleep(200);
			}
			updateStartMillis = System.currentTimeMillis();
			this.update();
			canvas.repaint();
			updateEndMillis = System.currentTimeMillis();
			updateDelayMillis = updateEndMillis - updateStartMillis;
			Util.sleep(FRAME_DELAY - ((int)updateDelayMillis));
		}
	}
	
	public void update(){
		if(interwaveCountdown > 0)
			interwaveCountdown--;
		if(killGoalReached()){
			interwaveCountdown = Util.INTERWAVE_FRAMES;
			wave.advance();
			ents.resetPlayersForWave();
			ents.resetForWave();
		}
		ents.cleanUp();
		ents.update();
		log.cleanUp();
		log.update();
	}
	
	public int getFrameRate(){
		return FRAME_RATE;
	}
	
	public Wave getWave(){
		return wave;
	}
	
	public boolean killGoalReached(){
		if(ents.getNumPlayers() == 1)
			return ents.getPlayer(0).getRoundKills() >= wave.getKillsNeeded();
		else
			return ents.getPlayer(0).getRoundKills() +
					ents.getPlayer(1).getRoundKills() >= wave.getKillsNeeded();
	}
	
	public boolean isInterwavePhase(){
		return interwaveCountdown > 0;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void setPaused(boolean b){
		paused = b;
	}
	
	public void switchPauseState(){
		paused = !paused;
	}
}