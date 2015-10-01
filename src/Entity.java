import java.awt.Color;
import java.awt.Graphics;

public abstract class Entity{

	public abstract void update();
	
	public abstract void paint(Graphics g);
	
	public abstract int getX();
	
	public abstract int getY();
	
	public abstract Color getColor();
	
	public abstract void kill();
	
	public abstract boolean isDead();
	
	//public abstract void changeHP(double dhp);
	
	//public abstract Entity getParent();
}
