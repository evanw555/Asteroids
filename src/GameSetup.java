import java.awt.Color;
import java.awt.event.KeyEvent;


public class GameSetup{
	private int numPlayers;
	private Player p1, p2;
	private Game game;
	
	public GameSetup(int numPlayers){
		this.numPlayers = numPlayers;
		//player 1
		PlayerSettings p1Settings = new PlayerSettings(1);
		do{
			Util.sleep(250);
		}while(!p1Settings.isDone());
		p1 = new Player(p1Settings.getName(),
				p1Settings.getColor(),
				p1Settings.getControls(),
				p1Settings.getDesignCanvas().getR(),
				p1Settings.getDesignCanvas().getTh());
		p1Settings.dispose();
		//if two players
		if(numPlayers > 1){
			//player 2
			PlayerSettings p2Settings = new PlayerSettings(2);
			do{
				Util.sleep(250);
			}while(!p2Settings.isDone());
			p2 = new Player(p2Settings.getName(),
					p2Settings.getColor(),
					p2Settings.getControls(),
					p2Settings.getDesignCanvas().getR(),
					p2Settings.getDesignCanvas().getTh());
			p2Settings.dispose();
		}
		//screen sizer to set game window size
		ScreenSizer ss = new ScreenSizer();
		do{
			Util.sleep(250);
		}while(!ss.isDone());
		int width, height;
		width = ss.getWidth();
		height = ss.getHeight();
		if(ADriver.DEBUG) System.out.println(width+", "+height);
		ss.dispose();
		//create game
		if(numPlayers == 1)
			game = new Game(width, height, p1);
		else
			game = new Game(width, height, p1, p2);
	}
	
	public GameSetup(){
		//default players
		p1 = new Player("Player 1",
				Color.RED,
				new Controls(KeyEvent.VK_UP,
						KeyEvent.VK_DOWN,
						KeyEvent.VK_RIGHT,
						KeyEvent.VK_LEFT,
						KeyEvent.VK_CONTROL,
						KeyEvent.VK_SHIFT),
				Util.getDefaultShipPolars()[1],
				Util.getDefaultShipPolars()[0]);
		p2 = new Player("Player 2",
				Color.GREEN,
				new Controls(KeyEvent.VK_W,
						KeyEvent.VK_S,
						KeyEvent.VK_D,
						KeyEvent.VK_A,
						KeyEvent.VK_Q,
						KeyEvent.VK_E),
				Util.getDefaultShipPolars()[1],
				Util.getDefaultShipPolars()[0]);
		//screen sizer to set game window size
		ScreenSizer ss = new ScreenSizer();
		do{
			Util.sleep(250);
		}while(!ss.isDone());
		int width, height;
		width = ss.getWidth();
		height = ss.getHeight();
		if(ADriver.DEBUG) System.out.println(width+", "+height);
		ss.dispose();
		//create game
		game = new Game(width, height, p1, p2);
	}
	
	public Game getGame(){
		return game;
	}
}