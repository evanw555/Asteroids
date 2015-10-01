import javax.swing.JOptionPane;

public class ADriver{
	public static final boolean DEBUG = true;
	public static GameSetup setup;
	public static Game game;
	
	public static void main(String[] args){
		
		
		String[] options = {"1", "2"};
		int numPlayers = 0;
		try{
			numPlayers = Integer.parseInt((String)JOptionPane.showInputDialog(null,
				"Welcome to Evan Williams' world famous Asteroids!\n" +
				"Please select number of players:", "Asteroids",
				JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]));   
		}catch(Exception e){
			if(DEBUG) System.out.println("ERROR");
			System.exit(0);
		}
		if(DEBUG) System.out.println(numPlayers);
		setup = new GameSetup(numPlayers);
		
		
		//TODO TEMP
		//setup = new GameSetup();
		//TODO END TEMP
		 
		game = setup.getGame();
		game.run();
	}
}
 