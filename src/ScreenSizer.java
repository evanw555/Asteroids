import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScreenSizer extends JFrame{
	private JButton doneButton;
	private boolean done;
	private ScreenSizer self;

	public ScreenSizer(){
		this.self = this;
		
		this.setTitle("Set Window Size");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		doneButton = new JButton("Maximize this window, then click to begin");
		doneButton.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(doneButton.isEnabled())
					done = true;
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		doneButton.setEnabled(false);
		this.getContentPane().add(doneButton);
		
		this.addWindowStateListener(new WindowStateListener(){
			public void windowStateChanged(WindowEvent e){
				doneButton.setEnabled(self.getHeight() > 128);
			}
		});
		
		done = false;
		this.pack();
		this.setLocationRelativeTo(null);
		this.toFront();
		this.setVisible(true);
	}
	
	public boolean isDone(){
		return done;
	}
	
	public int getWidth(){
		return super.getWidth();
	}
	
	public int getHeight(){
		return super.getHeight();
	}
}