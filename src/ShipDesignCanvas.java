import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class ShipDesignCanvas extends JPanel implements MouseListener{
	private int width, height;
	private PlayerSettings parent;
	private final double PI = Math.PI;
	private double[] th = {0, 0, (3*PI)/4, PI};
	private double[] r = {1, 1, 1, .5};
	
	public ShipDesignCanvas(int width, int height, PlayerSettings parent){
		this.width = width;
		this.height = height;
		this.parent = parent;
		this.addMouseListener(this);
	}
	
	public boolean isFocusable(){
		return true;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(width, height);
	}
	
	public Dimension getMinimumSize(){
		return new Dimension(width, height);
	}
	
	public Dimension getMaximumSize(){
		return new Dimension(width, height);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawOval(0, 0, width, height);
		g.drawLine(width/2, 0, width/2, height);
		g.drawLine(0, height/2, width, height/2);
		//draw points
		g.setColor(parent.getColor());
		Polygon proto;
		int[] x = {width,
				(width/2)+(int)(r[0]*(width/2)*Math.cos(th[0])),
				(width/2)+(int)(r[1]*(width/2)*Math.cos(th[1])),
				(width/2)+(int)(r[2]*(width/2)*Math.cos(th[2])),
				(width/2)+(int)(r[3]*(width/2)*Math.cos(th[3])),
				(width/2)+(int)(r[3]*(width/2)*Math.cos((-1)*th[3])),
				(width/2)+(int)(r[2]*(width/2)*Math.cos((-1)*th[2])),
				(width/2)+(int)(r[1]*(width/2)*Math.cos((-1)*th[1])),
				(width/2)+(int)(r[0]*(width/2)*Math.cos((-1)*th[0]))};
		int[] y = {height/2,
				(height/2)-(int)(r[0]*(height/2)*Math.sin(th[0])),
				(height/2)-(int)(r[1]*(height/2)*Math.sin(th[1])),
				(height/2)-(int)(r[2]*(height/2)*Math.sin(th[2])),
				(height/2)-(int)(r[3]*(height/2)*Math.sin(th[3])),
				(height/2)-(int)(r[3]*(height/2)*Math.sin((-1)*th[3])),
				(height/2)-(int)(r[2]*(height/2)*Math.sin((-1)*th[2])),
				(height/2)-(int)(r[1]*(height/2)*Math.sin((-1)*th[1])),
				(height/2)-(int)(r[0]*(height/2)*Math.sin((-1)*th[0]))};
		proto = new Polygon(x, y, x.length);
		g.fillPolygon(proto);
		g.setColor(Color.BLACK);
		g.drawPolygon(proto);
		//draw pointer over selected point
		int index = parent.getSelectedPointIndex();
		g.fillOval((width/2)+(int)(r[index]*(width/2)*Math.cos(th[index]))-4,
				(height/2)-(int)(r[index]*(height/2)*Math.sin(th[index]))-4,
				8, 8);
	}
	
	public double[] getR(){
		return r;
	}
	
	public double[] getTh(){
		return th;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e){
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e){
		//find the r (distance ratio from center) for this point
		double rTemp = Util.distance(width/2, height/2, e.getX(), e.getY())/(width/2);
		//if mouse clicked in top semicircle
		if(e.getY() < height/2 && rTemp <= 1){
			//find angle for this point
			double thTemp;
			try{
				thTemp = Math.atan((((double)height/2)-(double)e.getY()) /
						((double)e.getX()-((double)width/2)));
				if(e.getX() < width/2)
					thTemp += PI;
				//if x = 0 (divide by 0) then manually set angle
			}catch(ArithmeticException ex){ thTemp = PI/2; }
			//alter correct point
			int pointIndex = parent.getSelectedPointIndex();
			r[pointIndex] = rTemp;
			th[pointIndex] = thTemp;
			//repaint canvas
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
