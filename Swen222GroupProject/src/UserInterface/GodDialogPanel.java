package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ClientServer.Slave;
import java.util.*;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class GodDialogPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7418137296809892269L;
	Date dNow = new Date();
	private int numDaysPlaying = 1;
	private int diagCount = 0;// counter for going through the hint dialogs
	private JLabel dayLabel = new JLabel();
	private Insets insets;
	private Dimension size;
	private Font dayCountFont = new Font("Verdana", Font.BOLD, 20);
	private Font godDialogtFont = new Font("Verdana", Font.BOLD, 14);
	private ArrayList<String> godDialog = new ArrayList<String>();
	private int diagLimit;
	private String dialogFileName = "src/instructionsFromGod.txt";
	private JLabel godDialogLab;
	
	private JLabel playerScoreLab;
	private JLabel gameOver;

	public GodDialogPanel(final Slave slave) {
		loadGodDialog(dialogFileName);
		diagLimit = godDialog.size(); 
		
		// removes the layout manager
		this.setLayout(null);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		insets = this.getInsets();

		dayLabel = new JLabel();
		dayLabel.setFont(dayCountFont);
		dayLabel.setText("Day : 1");
		dayLabel.setPreferredSize(new Dimension(100, 100));
		dayLabel.setLocation(0, 0);

		this.add(dayLabel);
		size = dayLabel.getPreferredSize();
		dayLabel.setBounds(0 + insets.left, 0 + insets.top, size.width,
				size.height);

		godDialogLab = new JLabel();
		godDialogLab.setFont(godDialogtFont);
		godDialogLab.setText(godDialog.get(diagCount));
		godDialogLab.setPreferredSize(new Dimension(500, 100));
		godDialogLab.setLocation(50, 0);

		this.add(godDialogLab);
		size = godDialogLab.getPreferredSize();
		godDialogLab.setBounds(0 + insets.left, 60 + insets.top, size.width,
				size.height);
		
		playerScoreLab = new JLabel();
		playerScoreLab.setFont(dayCountFont);
		playerScoreLab.setText("Player Score:  0");
		playerScoreLab.setPreferredSize(new Dimension(200, 100));
		playerScoreLab.setLocation(0, 0);

		this.add(playerScoreLab);
		size = playerScoreLab.getPreferredSize();
		playerScoreLab.setBounds(0 + insets.left, 30 + insets.top, size.width,
				size.height);
		
		gameOver = new JLabel();
		gameOver.setFont(dayCountFont);
		gameOver.setText("GAMEOVER");
		gameOver.setPreferredSize(new Dimension(200, 100));
		gameOver.setLocation(0, 0);
		gameOver.setVisible(false);

		this.add(gameOver);
		size = gameOver.getPreferredSize();
		gameOver.setBounds(50 + insets.left, 450 + insets.top, size.width,
				size.height);

		//
		this.setFocusable(false);
		this.setVisible(true);
		this.repaint();

	}

	/**
	 * Loads the file of player hints into the dialog array
	 * */
	private void loadGodDialog(String dialogFileName2) {
		try {
			Scanner scan = new Scanner(new File(dialogFileName2));
			while (scan.hasNextLine()) {
				godDialog.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
 * 
 * */
	public void updateDialog() {
		diagCount++;
		godDialogLab.setText(godDialog.get(diagCount));

	}

	@Override
	public Dimension getPreferredSize() {
		// return new Dimension(1000, 1000);
		return new Dimension(500, 820);
	}

	public void updateDay() {
		numDaysPlaying++;
		dayLabel.setText("Day : " + numDaysPlaying);
	}
	public void endGame(){
		dayLabel.setVisible(false);
		godDialogLab.setVisible(false);
		gameOver.setVisible(true);
		playerScoreLab.setBounds(50 + insets.left, 500 + insets.top, size.width,
			size.height);
	}

	public boolean hasDiags() {
		if (diagCount < diagLimit-1) {
			return true;
		}
		godDialogLab.setText("");
		return false;
	}

	public void updatePlayerScore(int j) {
		playerScoreLab.setText( "Score: "+j);
		
	}

}
