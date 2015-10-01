
public class Refs{
	public static Game game;
	public static EntityHandler ents;
	public static GameCanvas canvas;
	public static TextLog log;
	
	public static void setGame(Game g){
		game = g;
	}
	
	public static void setEntityHandler(EntityHandler e){
		ents = e;
	}
	
	public static void setGameCanvas(GameCanvas g){
		canvas = g;
	}
	
	public static void setTextLog(TextLog t){
		log = t;
	}
}
