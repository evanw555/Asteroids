import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

public class ControlsComponent extends JTextField implements KeyListener{
	private KeyEvent e;
	
	public ControlsComponent(){
		this.setText("click here and press the desired key");
		this.setEditable(false);
		this.addKeyListener(this);
	}
	
	public boolean isReady(){
		return e != null;
	}
	
	public KeyEvent getKeyEvent(){
		return e;
	}

	public void keyPressed(KeyEvent e){
		this.e = e;
		this.setText(e.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
