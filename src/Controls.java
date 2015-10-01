
public class Controls{
	private int thrust, brake, right, left, fire, item;
	
	public Controls(int thrust, int brake, int right, int left, int fire, int item){
		this.thrust = thrust;
		this.brake = brake;
		this.right = right;
		this.left = left;
		this.fire = fire;
		this.item = item;
	}
	
	public boolean thrustPressed(){
		return KeyHandler.isPressed(thrust);
	}
	
	public boolean brakePressed(){
		return KeyHandler.isPressed(brake);
	}
	
	public boolean rightPressed(){
		return KeyHandler.isPressed(right);
	}
	
	public boolean leftPressed(){
		return KeyHandler.isPressed(left);
	}
	
	public boolean firePressed(){
		return KeyHandler.isPressed(fire);
	}
	
	public boolean itemPressed(){
		return KeyHandler.isPressed(item);
	}
}
