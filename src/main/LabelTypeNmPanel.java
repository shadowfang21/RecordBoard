package main;
import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class LabelTypeNmPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel[] labelType = { new JLabel("一勝"), new JLabel("半勝"),
			new JLabel("有效"), new JLabel("指導")};
	
	public LabelTypeNmPanel() {
		double t = TableLayout.FILL;
		double size[][] = { { t, 3, t, 3, t, 3, t, 3, t, 3, t },
						    { t } };
		super.setOpaque(true);
		super.setBackground(Color.BLACK);
		super.setLayout(new TableLayout(size));
		
		for (int i = 0 ; i < labelType.length ; i++) {
			labelType[i].setOpaque(true);
			labelType[i].setHorizontalAlignment(JLabel.CENTER);
			labelType[i].setVerticalAlignment(JLabel.CENTER);
			labelType[i].setBackground(Color.WHITE);
			labelType[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
			labelType[i].setFont(new Font("標楷體", 0, 80));
			this.add(labelType[i], Integer.toString(i * 2 + 2) + ", 0");
		}
		
		super.setVisible(true);
	}
}
