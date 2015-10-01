import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PlayerSettings extends JFrame{
	private Container cont;
	private ControlsComponent[] cc;
	private JColorChooser colorSelect;
	private JPanel masterPanel, superPanel, controlsPanel, designPanel, pointPanel, bottomPanel;
	private ShipDesignCanvas designCanvas;
	private ButtonGroup pbGroup;
	private JRadioButton[] pb;
	private JTextField name;
	private JButton doneButton;
	private boolean done;
	
	public PlayerSettings(int player){
		this.setTitle("Player "+player+" Settings");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		cont = this.getContentPane();
		cont.setLayout(new BorderLayout());
		
		masterPanel = new JPanel();
		masterPanel.setLayout(new GridLayout(1, 2));
		
		colorSelect = new JColorChooser();
		masterPanel.add(colorSelect);
		
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(2, 1));
		addControlsPanel();
		addDesignPanel();
		masterPanel.add(superPanel);
		
		cont.add(masterPanel, BorderLayout.CENTER);
		
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		
		name = new JTextField("enter your name here (less than 11 characters)");
		name.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(name.getText().equals("enter your name here (less than 11 characters)"))
					name.selectAll();
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		bottomPanel.add(name);
		
		doneButton = new JButton("Done (hover mouse over to refresh button)");
		doneButton.setEnabled(false);
		doneButton.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(doneButton.isEnabled())
					done = true;
			}
			public void mouseEntered(MouseEvent e){
				boolean result = true;
				for(ControlsComponent c : cc)
					if(!c.isReady())
						result = false;
				if(name.getText().length() > 10)
					result = false;
				doneButton.setEnabled(result);
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		bottomPanel.add(doneButton);
		
		cont.add(bottomPanel, BorderLayout.SOUTH);
		
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.toFront();
		this.setVisible(true);
		done = false;
	}
	
	public void addControlsPanel(){
		controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(6, 2));
		controlsPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Controls"));
		
		cc = new ControlsComponent[6];
		String[] captions = {"Thrust", "Brake", "Turn Right", "Turn Left", "Fire", "Use Item"};
		for(int i = 0; i < cc.length; i++){
			JTextField tempField = new JTextField(captions[i]);
			tempField.setEditable(false);
			controlsPanel.add(tempField);
			cc[i] = new ControlsComponent();
			controlsPanel.add(cc[i]);
		}
		
		superPanel.add(controlsPanel);
	}
	
	public void addDesignPanel(){
		designPanel = new JPanel();
		designPanel.setLayout(new GridLayout(1, 2));
		designPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Ship Design"));
		
		designCanvas = new ShipDesignCanvas(128, 128, this);
		designPanel.add(designCanvas);
		
		pointPanel = new JPanel();
		pointPanel.setLayout(new GridLayout(4, 1));
		pbGroup = new ButtonGroup();
		pb = new JRadioButton[4];
		for(int i = 0; i < pb.length; i++){
			pb[i] = new JRadioButton("Edit Point "+(i+1));
			pb[i].addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					designCanvas.repaint();
				}
			});
			pbGroup.add(pb[i]);
			pointPanel.add(pb[i]);
		}
		pb[0].setSelected(true);
		designPanel.add(pointPanel);
		
		
		superPanel.add(designPanel);
	}
	
	public Color getColor(){
		return colorSelect.getColor();
	}
	
	public int getSelectedPointIndex(){
		for(int i = 0; i < pb.length; i++)
			if(pb[i].isSelected())
				return i;
		//if none, then return first index
		return 0;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public ShipDesignCanvas getDesignCanvas(){
		return designCanvas;
	}
	
	public Controls getControls(){
		return new Controls(cc[0].getKeyEvent().getKeyCode(),
				cc[1].getKeyEvent().getKeyCode(),
				cc[2].getKeyEvent().getKeyCode(),
				cc[3].getKeyEvent().getKeyCode(),
				cc[4].getKeyEvent().getKeyCode(),
				cc[5].getKeyEvent().getKeyCode());
	}
	
	public String getName(){
		return name.getText().trim();
	}
}