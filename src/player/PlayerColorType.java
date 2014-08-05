package player;

import java.awt.Color;


public enum PlayerColorType {
	
	R(Color.RED), W(Color.WHITE), B(new Color(70, 80, 255));
	
	private Color c;
	
	PlayerColorType(Color c) {
		this.c = c;
	}
	
	public Color getColor() {
		return c;
	}
	
	public PlayerColorType next() {
		
		PlayerColorType[] colorTypes = PlayerColorType.values();
		
		int idx = this.ordinal();
		
		idx++;
		if (idx == colorTypes.length) {
			idx = 0;
		}
		
		return colorTypes[idx];
	}
	
}
