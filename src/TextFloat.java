import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TextFloat extends TimedParticle{
	private String message;
	
	public TextFloat(String message, int x, int y, Color color, int duration){
		super(-1, x, y, 0, -2, color, duration);
		this.message = message;
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 15));
		g.drawString(message, getX(), getY());
	}
}