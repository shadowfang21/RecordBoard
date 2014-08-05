package player;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class ScoreLabel extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Font SCORE_FONT_SIZE = new Font("Serif", 0, 200);
	
	private int score;
	private LabelType type;
	
	public ScoreLabel(LabelType type) {
		super("0");
		this.type = type;
		score = 0;
		
		super.setOpaque(true);
		super.setHorizontalAlignment(JLabel.CENTER);
		super.setVerticalAlignment(JLabel.CENTER);
		super.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		super.setFont(SCORE_FONT_SIZE);
		
		super.addMouseListener(new MouseAdapter() {
			public void mouseClicked( MouseEvent e ) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					add();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					sub();
				}
			}
		});
	}
	
	public void reset() {
		score = 0;
		super.setText("0");
		
	}
	
	public void add() {
		if (score < type.getMaxScore()) {
			score++;
			super.setText(Integer.toString(score));
		}
	}
	
	private void sub() {
		if (score > 0) {
			score--;
			super.setText(Integer.toString(score));
		}
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
}
