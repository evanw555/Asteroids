import java.awt.event.KeyEvent;


public class KeyHandler{
	private static boolean[] keysPressed = new boolean[1000];
	
	public static void setKeyPressed(int key, boolean pressed){
		keysPressed[key] = pressed;
	}
	
	public static boolean isPressed(int key){
		return keysPressed[key];
	}
}