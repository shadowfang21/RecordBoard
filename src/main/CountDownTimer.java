package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.Timer;


public class CountDownTimer implements ActionListener,RecordObserver {

	private static final Font TIME_FONT_SIZE = new Font("Serif", 0, 380);
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int DEFAULT_INITTIME = 240 * SECOND;
	private static final int TIME_THREADHOLD = 10;
	
	private JLabel timerLabel = new JLabel("");
	
	private int initTime = DEFAULT_INITTIME; 
	private int currentTime; 
	
	private RecordState state;
	private Timer clockDown;

	public CountDownTimer() {
		
		clockDown = new Timer(TIME_THREADHOLD, this);
		
		timerLabel.setFont(TIME_FONT_SIZE);
		timerLabel.setOpaque(true);
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setVerticalAlignment(JLabel.CENTER);
		timerLabel.setForeground(Color.RED);
		timerLabel.setBackground(Color.BLACK);
		timerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		this.setState(RecordState.INACTIVE);
		timerLabel.addMouseListener(new CountDownListener());
	}
	
	public void reset() {
		this.setState(RecordState.INACTIVE);
	}
	
	public void clickSpace() {
		if (this.getState() != RecordState.FINISH) {
			if (clockDown.isRunning()) {
				this.setState(RecordState.PAUSE);
			} else {
				this.setState(RecordState.ACTIVE);
			}
		}
	}
	
	
	/**
	 * return true if count down to 0
	 * @return
	 */
	private boolean countDown() {
		
		if (currentTime > 0) {
			currentTime -= TIME_THREADHOLD;
			this.setTimeLabel(currentTime);
		} else {
			
			RecordBoard.playBeep();
			
			return true;
		}
		
		return false;
	}
	

	public void setTimeLabel(int milliSecond) {
		if (milliSecond < SECOND) {
			timerLabel.setText(String.format("0.%02d", milliSecond / TIME_THREADHOLD));
		} else {
			final int innMilliSecond = milliSecond / SECOND;
			
			final int min = (innMilliSecond / 60);
			final int sec1 = (innMilliSecond % 60 / 10);
			final int sec2 = (innMilliSecond % 60 % 10);
			final String text = (Integer.toString(min) + ":"
					+ Integer.toString(sec1) + Integer.toString(sec2));
			timerLabel.setText(text);
		}
	}

	/**
	 * @return the timerLabel
	 */
	public JLabel getTimerLabel() {
		return timerLabel;
	}
	
	class CountDownListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (state == RecordState.INACTIVE) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.isShiftDown()) {
						initTime += SECOND;
					} else {
						initTime += MINUTE;
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {

					if (e.isShiftDown()) {
						if (initTime > SECOND) {
							initTime -= SECOND;
						}
					} else {
						if (initTime > MINUTE) {
							initTime -= MINUTE;
						}
					}
				}
				currentTime = initTime;
				setTimeLabel(currentTime);
			}
		}
	}

	/**
	 * @return the state
	 */
	public RecordState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(RecordState state) {
		this.state = state;
		
		if (state == RecordState.INACTIVE) {
			currentTime = initTime;
			this.setTimeLabel(currentTime);
			this.clockDown.setInitialDelay(SECOND);
			this.timerLabel.setForeground(Color.red);
			this.timerLabel.setEnabled(true);
		} else if (state == RecordState.ACTIVE) {
			this.clockDown.start();
			this.timerLabel.setForeground(Color.yellow);
		} else if (state == RecordState.PAUSE) {
			this.clockDown.stop();
			this.clockDown.setInitialDelay(0);
			this.timerLabel.setForeground(Color.red);
		} else if (state == RecordState.FINISH) {
			this.timerLabel.setEnabled(false);
			this.clockDown.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (this.countDown()) {
			this.setState(RecordState.FINISH);
		}
	}

}


