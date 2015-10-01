import java.awt.Color;

public class LogEntry{
	private String text;
	private Color color;
	private long duration;
	
	public LogEntry(String text, Color color){
		this.text = text;
		this.color = color;
		this.duration = Util.millisToFrames(6000);
	}
	
	public void update(){
		duration--;
	}
	
	public boolean isExpired(){
		return duration <= 0;
	}
	
	public String getText(){
		return text;
	}
	
	public Color getColor(){
		return color;
	}
}
