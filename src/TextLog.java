import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class TextLog{
	private final int FONT_SIZE = 15;
	ArrayList<LogEntry> entries;
	
	public TextLog(){
		entries = new ArrayList<LogEntry>();
	}
	
	public void update(){
		//update each logEntry
		for(LogEntry l : entries)
			l.update();
	}
	
	public void cleanUp(){
		//remove expired entries
		for(int i = 0; i < entries.size(); i ++)
			if(entries.get(i).isExpired()){
				entries.remove(i);
				i--;
			}
	}
	
	public void paint(Graphics g){
		for(int i = 0; i < entries.size(); i++){
			g.setColor(entries.get(i).getColor());
			g.setFont(new Font("Impact", Font.PLAIN, 15));
			g.drawString(entries.get(i).getText(),
					4, 4+(i+1)*(FONT_SIZE+2));
		}
	}
	
	public void add(LogEntry entry){
		entries.add(0, entry);
	}
}
