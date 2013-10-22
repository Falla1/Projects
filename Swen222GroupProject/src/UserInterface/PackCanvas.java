package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ClientServer.Slave;
import GameParts.Coin;
import GameParts.GameObject;
/**
 * The Inventory for the player. Displays the shapes the player can change into and visually indicate 
 * when they can pick up chest items.
 *@author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class PackCanvas extends JPanel implements MouseMotionListener,
MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2991927083188678777L;
	private PackCanvas p = this;
	private int x = 70;

	private int mouseX;
	@SuppressWarnings("unused")
	private int mouseY;
	private Insets insets;
	private Dimension size;
	private List<Image> images;
	private Set<GameObject> inventory;
	private List<GameObject> pending;
	private Slave slave;
	private JButton triangleButton;
	private JButton sphereButton;
	private JButton squareButton;
	private JButton takeAllButton;
	private boolean hasSquare = false;
	private boolean hasPyramid = false;
	private boolean hasSphere = false;
	
	


	public PackCanvas(final Slave slave) {

		this.slave = slave;
		inventory = new HashSet<GameObject>();
		pending = new ArrayList<GameObject>();
		// removes the layout manager
		this.setLayout(null);
		this.setFocusable(false);
		images = new ArrayList<Image>();
		
		images.add(loadImage("src/images/squareYELLOW.png"));
		images.add(loadImage("src/images/pyramidYELLOW.png"));
		images.add(loadImage("src/images/sphereYELLOW.png"));
		images.add(loadImage("src/images/chestRight.png"));
		images.add(loadImage("src/images/inventoryPanel.png"));
		images.add(loadImage("src/images/panelBorder.png"));
		images.add(loadImage("src/images/coinGold.png"));
		images.add(loadImage("src/images/panelDrag.png"));
		images.add(loadImage("src/images/takeAll.png"));
		images.add(loadImage("src/images/backPackBLUE.png"));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		setButtons();
		this.repaint();

	}

	public void setButtons(){
		insets = this.getInsets();

		//Puts a button over the location of square image 
		//That appears in the inventory by using the insets location
		squareButton = new JButton();
		squareButton.setVisible(true);
		squareButton.setEnabled(true);
		squareButton.setFocusable(false);

		squareButton.setPreferredSize(new Dimension(50, 50));
		squareButton.setLocation(0, 0);

		this.add(squareButton);
		size = squareButton.getPreferredSize();
		squareButton.setBounds(-100 + x + insets.left, 0 + insets.top, size.width,
				size.height);

		//Puts a button over the location of pyramid image 
		//That appears in the inventory by using the insets location
		triangleButton = new JButton();
		triangleButton.setVisible(true);
		triangleButton.setEnabled(true);
		triangleButton.setFocusable(false);

		triangleButton.setPreferredSize(new Dimension(50, 50));
		triangleButton.setLocation(0, 0);

		this.add(triangleButton);
		size = triangleButton.getPreferredSize();
		triangleButton.setBounds(-40 + x + insets.left, 0 + insets.top, size.width,
				size.height);

		//Puts a button over the location of sphere image 
		//That appears in the inventory by using the insets location
		sphereButton = new JButton();
		sphereButton.setVisible(true);
		sphereButton.setEnabled(true);
		sphereButton.setFocusable(false);

		sphereButton.setPreferredSize(new Dimension(50, 50));
		sphereButton.setLocation(0, 0);
		this.add(sphereButton);

		size = sphereButton.getPreferredSize();
		sphereButton.setBounds(20 + x + insets.left, 0 + insets.top, size.width,
				size.height);

		takeAllButton = new JButton();
		takeAllButton.setVisible(true);
		takeAllButton.setEnabled(true);
		takeAllButton.setFocusable(false);

		takeAllButton.setPreferredSize(new Dimension(60,60));
		takeAllButton.setLocation(0, 0);
		this.add(takeAllButton);

		size = takeAllButton.getPreferredSize();
		takeAllButton.setBounds(0 + insets.left, 135 + insets.top, size.width,
				size.height);

		repaint();
		squareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//If player has the shape, tell the slave the user changed shape
				if(hasSquare){
					slave.updateShape(1);
					p.repaint();}
			}
		});
		triangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//If player has the shape, tell the slave the user changed shape
				if(hasPyramid){
					slave.updateShape(2);
					p.repaint();}
			}
		});

		sphereButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//If player has the shape, tell the slave the user changed shape
				if(hasSphere){
					slave.updateShape(3);
					p.repaint();}
			}
		});
		
		takeAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//If player has the shape, tell the slave the user changed shape
				if(pending.size()>0){
					pending.clear();
					slave.addAllPending();
					p.repaint();}
			}
		});
		
	}

	public Set<GameObject> getInventory() {
		return inventory;
	}

	public void setInventory(Set<GameObject> inv) {
		this.inventory = new HashSet<GameObject>(inv);
	}

	/**
	 * Loads an image for drawing
	 *
	 * @param filenamefile
	 *            to load
	 * @return Image to draw
	 */
	private Image loadImage(String filename) { //TODO CHANGE THIS
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(220, 220);
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		Color back = new Color(210, 210, 210);
		Color itemBack = new Color(95,158,160);
		Color select = new Color(175,238,238);

		g2.setColor(back);

		g2.setColor(itemBack);

		g2.fillRect(-100 + x, 11, 59, 58);
		g2.fillRect(-40 + x, 11, 59, 58);
		g2.fillRect(20 + x, 11, 59, 58);
		g2.fillRect(80 + x, 11, 59, 58);
		g2.fillRect(140 + x, 11, 59, 58);

		//Iterate through every object in players inventory
		for(GameObject i : inventory){
			//If player has a certain object
			//Set the value to true and then draw that image
			if(i.getType().equals("+")){
				hasSquare = true;
				g2.setColor(select);
				g2.fillRect(-100 + x, 11, 59, 58);
				g2.drawImage(images.get(0), -92 + x, -12, null, null);
			}
			if(i.getType().equals("^")){
				hasPyramid = true;
				g2.setColor(select);
				g2.fillRect(-40 + x, 11, 59, 58);
				g2.drawImage(images.get(1), -32 + x, -10, null, null);
			}
			if(i.getType().equals("o")){
				hasSphere = true;
				g2.setColor(select);
				g2.fillRect(20 + x, 11, 59, 58);
				g2.drawImage(images.get(2), 30 + x, -12, null, null);
			}
			if(i.getType().equals("chest")){
				g2.setColor(select);
				g2.fillRect(80 + x, 11, 59, 58);
				g2.drawImage(images.get(3), 90 + x, -12, null, null);
			}
		}
		//Draw the frame of the inventory
		g2.drawImage(images.get(4), -101 + x, 10, null, null);
		g2.drawImage(images.get(5), 0, 0, null, null);
		g2.drawImage(images.get(7), 0, 80, null, null);
		if(pending != null){
		if(pending.size()>=1){
			g2.setColor(itemBack);
			g2.drawImage(images.get(8), 0, 140, null, null);
			g2.setColor(new Color(255,255,0,125));
			g2.fillRect(1, 160, 218, 58);

			for(int i =0;i< pending.size(); i++){
				if(pending.get(i) instanceof Coin){
					g2.drawImage(images.get(6), 55 + 60*i,135, null, null);
				}
				else if(pending.get(i).getType().equals("+")){
					g2.drawImage(images.get(0), 55 + 60*i,135, null, null);
				}
				else if(pending.get(i).getType().equals("^")){
					g2.drawImage(images.get(1), 55 + 60*i,135, null, null);
				}
				else if(pending.get(i).getType().equals("o")){
					g2.drawImage(images.get(2), 55 + 60*i,135, null, null);
				}
				else if(pending.get(i).getType().equals("%")){
					g2.drawImage(images.get(9), 55 + 60*i,135, null, null);
				}
				
			}
			g2.drawImage(images.get(3), 8,135, null, null);
		}}
		
		//Set the bounds of the buttons and validate them
		squareButton.setBounds(-100 + x + insets.left, 0 + insets.top, size.width,
				size.height);
		squareButton.validate();
		triangleButton.setBounds(-40 + x + insets.left, 0 + insets.top, size.width,
				size.height);
		triangleButton.validate();
		sphereButton.setBounds(20 + x + insets.left, 0 + insets.top, size.width,
				size.height);
		sphereButton.validate();


	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//If user drags the inventory across, change the position of the inventory
		if (SwingUtilities.isLeftMouseButton(e)) {
			x = x + (e.getX() - mouseX);
			if (x > 61 && x < 112) {
				repaint();
			} else if (x <= 61) {
				x = 62;
				repaint();
			} else if (x >= 112) {
				x = 111;
				repaint();
			}
			this.mouseX = e.getX();
			this.mouseY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void addPendingItems(List<GameObject> pending) {
		this.pending.clear();
		this.pending = pending;
	}
	public List<GameObject> getPending() {
		return pending;
	}

}
