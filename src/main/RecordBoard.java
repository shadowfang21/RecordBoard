package main;
import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import player.LabelType;
import player.PlayerColorType;
import player.RecordPlayer;

public class RecordBoard {
	
	private JFrame mainFrame = new JFrame();
	private RecordPlayer playerE;
	private RecordPlayer playerD;
	private CountDownTimer clock;
	
	private static Clip clip;
	
	private RecordBoard() {
		clock = new CountDownTimer();
		playerE = new RecordPlayer(PlayerColorType.B, clock);
		playerD = new RecordPlayer(PlayerColorType.W, clock);
		
		setUpClip();
		makeGUI();
	}
	
	public static void playBeep() {
		clip.setFramePosition(0);
		clip.start();
	}
	
	private void setUpClip() {
		try {
			// Open an audio input stream.
			URL url = RecordBoard.class.getResource("beep.wav");

			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void makeGUI() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Container con = new Container();
		con = mainFrame.getContentPane();

		mainFrame.setUndecorated(true);
		mainFrame.setBounds(0, 0, screenSize.width, screenSize.height);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		double t = TableLayout.FILL;
		double size[][] = { { t },
				{ 0.4, 3, 0.1, 3,  t, 3, t } };
		mainFrame.setLayout(new TableLayout(size));
		mainFrame.addKeyListener(new mainFrameKeyListener());
		con.setBackground(Color.BLACK);
		con.add(clock.getTimerLabel(), "0, 0");
		con.add(new LabelTypeNmPanel(), "0, 2");
		con.add(playerE.getPlayerPanel(), "0, 4");
		con.add(playerD.getPlayerPanel(), "0, 6");
		
		mainFrame.setVisible(true);
	}
	
	public static void main(String... args) {
		JOptionPane.showMessageDialog(null,"清華大學柔道社‧柔道計分程式_2013年規則版\n版本別：v3\n" +
				"設計人：方琮凱\n" +
					"ChangeLog : \n" + 
					"1. 增加分數類別文字\n2. 指導不使對手增加分數，並改以數字顯示\n3. 壓制時間修改10,15,20\n" + 
					"--------------\n" + 
					"4. 滑鼠點擊R、W可改變顏色\n" +
					"5. shift + 點選可以秒為單位改變倒數時間\n6. 最後一秒時以微秒顯示\n" + 
					"7. 壓制時間啟動按鈕修改為E和D\n8. 增加S顯示勝利者功能"); 
		new RecordBoard();
	}
	
	private void resetAll() {
		clock.reset();
		playerE.reset();
		playerD.reset();
	}
	
	private RecordPlayer checkWinner() {
		RecordPlayer winner = null;
		
		//check shido first, 
		if (playerE.getScoreLabel()[LabelType.SHIDO.ordinal()].getScore() ==
				LabelType.SHIDO.getMaxScore()) {
			return playerD;
		}
		if (playerD.getScoreLabel()[LabelType.SHIDO.ordinal()].getScore() ==
				LabelType.SHIDO.getMaxScore()) {
			return playerE;
		}
		
		//會按照順序判斷 IPPON > WAZA, YUKO
		for (int i = 0 ; i < playerE.getScoreLabel().length ; i++) {
			final int scoreE = playerE.getScoreLabel()[i].getScore();
			final int scoreD = playerD.getScoreLabel()[i].getScore();
			
			if (scoreE > scoreD) {
				return playerE;
			} else if (scoreE < scoreD) {
				return playerD;
			}
		}
		return winner;
	}
	
	private boolean isResetAble() {
		
		if (clock.getState() != RecordState.ACTIVE &&
				playerE.getState() != RecordState.ACTIVE &&
				playerD.getState() != RecordState.ACTIVE) {
			return true;
		}
		
		return false;
	}
	
	class mainFrameKeyListener extends KeyAdapter {
		public void keyTyped(KeyEvent e) { 
			if (e.getKeyChar() == ' ') { //start count down
				clock.clickSpace();
				if (clock.getState() == RecordState.PAUSE) { //暫停時要讓壓制計時也停止
					//若已經啟動就讓他暫停
					if (playerE.getState() == RecordState.ACTIVE) {
						playerE.setState(RecordState.PAUSE);
					}
					if (playerD.getState() == RecordState.ACTIVE) {
						playerD.setState(RecordState.PAUSE);
					}
				} else if (clock.getState() == RecordState.ACTIVE) { //重啟時就要一並讓下面的重啟
					if (playerE.getState() == RecordState.PAUSE) {
						playerE.setState(RecordState.ACTIVE);
					}
					if (playerD.getState() == RecordState.PAUSE) {
						playerD.setState(RecordState.ACTIVE);
					}
				}
			} else if (e.getKeyChar() == 'e') { //start count up
				if ((clock.getState() == RecordState.ACTIVE && 
						playerD.getState() != RecordState.ACTIVE) || 
						playerE.getState() == RecordState.PAUSE ||
						(clock.getState() == RecordState.FINISH && 
						playerE.getState() == RecordState.ACTIVE)) {
					playerE.clickStartButton();
				}
			} else if (e.getKeyChar() == 'd') { //start count up
				if ((clock.getState() == RecordState.ACTIVE &&
						playerE.getState() != RecordState.ACTIVE) || 
						playerD.getState() == RecordState.PAUSE ||
						(clock.getState() == RecordState.FINISH && 
								playerD.getState() == RecordState.ACTIVE)) {
					playerD.clickStartButton();
				}
			} else if (e.getKeyChar() == '\n') { //reset
				if (isResetAble()) {
					resetAll();
				}
			} else if (e.getKeyChar() == 's') { //show winner
				if (isResetAble()) {
					final RecordPlayer player = checkWinner();
					if (player != null) { //如果有勝利者的話
						player.clickWinnerButton();
					}
				}
			}
		}

		
	}
}
