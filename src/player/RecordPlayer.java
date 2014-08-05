package player;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import main.RecordBoard;
import main.RecordObserver;
import main.RecordState;

public class RecordPlayer implements ActionListener {
	
	private static final int SCORE_YUKO_SECOND = 10;
	private static final int SCORE_WAZA_SECOND = 15;
	private static final int SCORE_IPPON_SECOND = 20;
	
	private JPanel playerPanel = new JPanel();
	private JLabel timerLabel = new JLabel("00");
	private JLabel winnerLabel = new JLabel("WINNER");
	
	private int countTime = 0;
	
	private JLabel playerLabel = new JLabel("");
	private ScoreLabel[] scoreLabel = { new ScoreLabel(LabelType.IPPON), 
									    new ScoreLabel(LabelType.WAZA),
									    new ScoreLabel(LabelType.YUKO), 
									    new ScoreLabel(LabelType.SHIDO)};

	private PlayerColorType colorType;
	private RecordState state;
	private Timer clockUp;
	private Timer winnerTimer;
	
	private RecordObserver observer;
	
	public RecordPlayer(PlayerColorType colorType, RecordObserver observer) {
		
		this.observer = observer;
		this.colorType = colorType;
		this.setState(RecordState.INACTIVE);
		
		this.clockUp = new Timer(1000, this);
		this.winnerTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (winnerLabel.isVisible()) {
					winnerLabel.setVisible(false);
				} else {
					winnerLabel.setVisible(true);
				}
			}
		});
		
		double t = TableLayout.FILL;
		double size[][] = { { t, 3, t, 3, t, 3, t, 3, t, 3, t },
						    { t } };
		
		timerLabel.setOpaque(true);
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setVerticalAlignment(JLabel.CENTER);
		timerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		timerLabel.setFont(ScoreLabel.SCORE_FONT_SIZE);
		
		winnerLabel.setOpaque(true);
		winnerLabel.setHorizontalAlignment(JLabel.CENTER);
		winnerLabel.setVerticalAlignment(JLabel.CENTER);
		winnerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		winnerLabel.setFont(new Font("新細名體", 0, 280));
		winnerLabel.setBackground(Color.YELLOW);
		winnerLabel.setVisible(false);
		
		playerLabel.setText(colorType.name());
		playerPanel.setOpaque(true);
		playerPanel.setBackground(Color.BLACK);
		playerPanel.setLayout(new TableLayout(size));
		playerLabel.setFont(ScoreLabel.SCORE_FONT_SIZE);
		playerLabel.setOpaque(true);
		playerLabel.setHorizontalAlignment(JLabel.CENTER);
		playerLabel.setVerticalAlignment(JLabel.CENTER);
		playerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		playerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				changeColor();
			}
		});
		this.changeBackGround(colorType.getColor());
		playerPanel.add(winnerLabel, "0, 0, 10, 0");
		playerPanel.add(playerLabel, "0, 0");
		
		for (int i = 0; i < scoreLabel.length ; i++) {
			playerPanel.add(scoreLabel[i], Integer.toString((i+1) * 2) + ", 0");
		}
		playerPanel.add(timerLabel, "10, 0");
		
		playerPanel.setVisible(true);
	}
	
	private void setTimeLabel(int TimeSec) {

		final int sec1 = TimeSec / 10;
		final int sec2 = TimeSec % 10;

		final String text = Integer.toString(sec1) + Integer.toString(sec2);

		timerLabel.setText(text);
	}

	private void changeBackGround(Color c) {
		playerLabel.setBackground(c);
		for (ScoreLabel score : scoreLabel) {
			score.setBackground(c);
		}
		timerLabel.setBackground(c);
	}
	
	private void changeColor() {
		colorType = colorType.next();
		playerLabel.setText(colorType.name());
		this.changeBackGround(colorType.getColor());
	}
	
	public void reset() {
		for (ScoreLabel score : scoreLabel) {
			score.reset();
		}
		this.setState(RecordState.INACTIVE);
		winnerLabel.setVisible(false);
		if (winnerTimer.isRunning()) {
			winnerTimer.stop();
		}
	}
	
	private void resetTimer() {
		this.countTime = 0;
		timerLabel.setText("00");
	}

	/**
	 * @return the playerPanel
	 */
	public JPanel getPlayerPanel() {
		return playerPanel;
	}

	
	public RecordState getState() {
		return state;
	}

	public void setState(RecordState state) {
		this.state = state;
		
		if (state == RecordState.INACTIVE) {
			this.resetTimer();
			this.timerLabel.setEnabled(true);
//			if (clockUp.isRunning()) {
//				clockUp.stop();
//			}
		} else if (state == RecordState.ACTIVE) {
			this.clockUp.start();
		} else if (state == RecordState.PAUSE) {
			this.clockUp.stop();
		} else if (state == RecordState.FINISH) {
			this.timerLabel.setEnabled(false);
			this.clockUp.stop();
			RecordBoard.playBeep();
		}
		
	}

	public boolean isOperatable() {
		
		if (state != RecordState.FINISH && state != RecordState.INACTIVE) {
			return true;
		}
		return true;
	}
	
	public void clickWinnerButton() {
		if (this.winnerTimer.isRunning()) {
			this.winnerTimer.stop();
			winnerLabel.setVisible(false);
		} else {
			this.winnerTimer.start();
		}
	}
	
	public void clickStartButton() {
		
		if (state == RecordState.INACTIVE) {
			this.setState(RecordState.ACTIVE);
		} else if (state == RecordState.ACTIVE) {
			this.setState(RecordState.PAUSE);
		} else if (state == RecordState.PAUSE) {
			if (countTime >= SCORE_WAZA_SECOND) {
				scoreLabel[LabelType.WAZA.ordinal()].add();
			} else if (countTime >= SCORE_YUKO_SECOND) {
				scoreLabel[LabelType.YUKO.ordinal()].add();
			}
			this.setState(RecordState.INACTIVE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		countTime++;
		this.setTimeLabel(countTime);
		
		//WAZA
		if ((scoreLabel[LabelType.WAZA.ordinal()].getScore() == 1 && 
				countTime >= SCORE_WAZA_SECOND) || 
				countTime >= SCORE_IPPON_SECOND) {
			this.setState(RecordState.FINISH);
			observer.setState(RecordState.FINISH);
		}
	}

	/**
	 * @return the scoreLabel
	 */
	public ScoreLabel[] getScoreLabel() {
		return scoreLabel;
	}
}
