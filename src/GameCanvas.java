import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class GameCanvas extends JPanel implements KeyListener{
	private int width, height;
	
	public GameCanvas(int width, int height){
		this.width = width;
		this.height = height;
		this.addKeyListener(this);
	}
	
	public boolean isFocusable(){
		return true;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(width, height);
	}
	
	public Dimension getMinimumSize(){
		return new Dimension(width, height);
	}
	
	public Dimension getMaximumSize(){
		return new Dimension(width, height);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		//TODO do
		Refs.ents.paint(g);
		Refs.log.paint(g);
		drawSideBar(g);
		if(Refs.game.isInterwavePhase())
			drawWaveTitle(g);
		if(Refs.game.isPaused())
			drawPause(g);
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getPlayableWidth(){
		return width - Util.SIDE_BAR_WIDTH;
	}
	
	public void drawSideBar(Graphics g){
		try
		{
			int sixteenth = Util.SIDE_BAR_WIDTH/16;
			//draw side bar
			g.setColor(Color.GRAY);
			g.fillRect(getPlayableWidth(), 0, Util.SIDE_BAR_WIDTH, height);
			//write wave number
			g.setColor(Color.DARK_GRAY);
			g.setFont(new Font("Impact", Font.BOLD, 22));
			g.drawString("Wave "+Refs.game.getWave().getStage(),
					getPlayableWidth()+(3*sixteenth), 3*sixteenth);
			//draw kill graph circle
			g.setColor(Color.LIGHT_GRAY);
			g.fillOval(getPlayableWidth()+sixteenth, 4*sixteenth,
					Util.SIDE_BAR_WIDTH-(2*sixteenth), Util.SIDE_BAR_WIDTH-(2*sixteenth));
			//draw player 1 kill count arc
			drawPlayer1Arc(Refs.ents.getPlayer(0), sixteenth, g);
			//draw player 2 kill count arc
			if(Refs.ents.getNumPlayers() >= 2)
				drawPlayer2Arc(Refs.ents.getPlayer(1), sixteenth, g);
			//write kills remaining on circle
			String tempString = Refs.game.getWave().getKillsRemaining()+"";
			int tempX = getPlayableWidth()+(int)(Util.SIDE_BAR_WIDTH*.45);
			int tempY = (int)(3.5*sixteenth)+(Util.SIDE_BAR_WIDTH/2);
			g.setFont(new Font("Impact", Font.BOLD, 25));
			g.setColor(Color.WHITE);
			g.drawString(tempString, tempX+1, tempY+1);
			g.setColor(Color.DARK_GRAY);
			g.drawString(tempString, tempX, tempY);
			//draw upcoming waves
			g.setFont(new Font("Impact", Font.BOLD, 14));
			for(int i = 0; i < 4; i++){
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(getPlayableWidth()+sixteenth, Util.SIDE_BAR_WIDTH+(8*sixteenth)+(i*5*sixteenth),
					Util.SIDE_BAR_WIDTH-(2*sixteenth), sixteenth*4);
				g.setColor(Color.BLACK);
				int stage = Refs.game.getWave().getStage();
				g.drawString(stage+i+1+" "+Refs.game.getWave().getSubtitleAt(i+1),
						getPlayableWidth()+(2*sixteenth),
						Util.SIDE_BAR_WIDTH+(11*sixteenth)+(i*5*sixteenth));
		}
		}catch(Exception e){}
	}
	
	public void drawPlayer1Arc(Player p, int sixteenth, Graphics g){
		double killShare = (double)p.getRoundKills()/
							(double)Refs.game.getWave().getKillsNeeded();
		g.setColor(p.getColor());
		g.fillArc(getPlayableWidth()+sixteenth, 4*sixteenth,
				Util.SIDE_BAR_WIDTH-(2*sixteenth), Util.SIDE_BAR_WIDTH-(2*sixteenth),
				90, (int)(-360*killShare));
	}
	
	public void drawPlayer2Arc(Player p, int sixteenth, Graphics g){
		double killShare = (double)p.getRoundKills()/
							(double)Refs.game.getWave().getKillsNeeded();
		g.setColor(p.getColor());
		g.fillArc(getPlayableWidth()+sixteenth, 4*sixteenth,
				Util.SIDE_BAR_WIDTH-(2*sixteenth), Util.SIDE_BAR_WIDTH-(2*sixteenth),
				90, (int)(360*killShare));
	}
	
	public void drawWaveTitle(Graphics g){
		g.setColor(Color.GRAY);
		g.setFont(new Font("Impact", Font.BOLD, 128));
		g.drawString(Refs.game.getWave().getTitle(), getWidth()/4, getHeight()/2);
		g.setFont(new Font("Impact", Font.BOLD, 80));
		g.drawString(Refs.game.getWave().getSubtitle(), getWidth()/4, (int)(getHeight()*.75));
	}
	
	public void drawPause(Graphics g){
		g.setFont(new Font("Impact", Font.BOLD, 128));
		g.setColor(Color.BLACK);
		g.drawString("PAUSED", getWidth()/4-4, getHeight()/4-4);
		g.setColor(Color.RED);
		g.drawString("PAUSED", getWidth()/4, getHeight()/4);
	}
	
	// KEY LISTENER METHODS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void keyPressed(KeyEvent e){
		KeyHandler.setKeyPressed(e.getKeyCode(), true);
		//if escape pressed, switch pause state
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			if(Refs.game.isInterwavePhase())
				Refs.game.setPaused(false);
			else
				Refs.game.switchPauseState();
	}

	public void keyReleased(KeyEvent e){
		KeyHandler.setKeyPressed(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
