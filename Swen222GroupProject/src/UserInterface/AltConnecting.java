package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ClientServer.Client;
import ClientServer.Server;
/**
 * Allows the user the select what type of connection they are wanting.
 * Options being 'server' or 'connect' with connect requiring a valid host name or ip address
 * 'localhost' is used to connect to yourself
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class AltConnecting extends JFrame{
	private static final long serialVersionUID = 8770623048267860493L;
	/**
	 * 
	 */
	
	private JLabel imageLabel;
	private JTextField ip;
	@SuppressWarnings("rawtypes")
	private JComboBox option;
	private GridLayout gl = new GridLayout(1,5);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AltConnecting(){
		JLayeredPane jl = new JLayeredPane();
		jl.setLayout(gl);
		JPanel centerP = new JPanel();
		JPanel backP = new JPanel();
		imageLabel = new JLabel();
		//ImageIcon ic = makeImageIcon("connect.png");
		//imageLabel.setIcon(ic);
		File file = new File(this.getClass().getName());
		file = file.getAbsoluteFile();
		file = file.getParentFile();

		ImageIcon bckG = new ImageIcon(loadImage("src/images/connect.png"));
		imageLabel.setIcon(bckG);

		this.setPreferredSize(new Dimension(800,800));
		this.setLocation(400, 100);

		jl.setSize(new Dimension(300,50));

		ip = new JTextField("Enter Host Address");
		ip.setPreferredSize(new Dimension(200,20));

		option = new JComboBox();
		option.addItem("Server");
		option.addItem("Connect");
		ip.setEditable(false);


		JButton startButton = new JButton("Start");

		option.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(option.getSelectedItem().equals("Server")){
					ip.setEditable(false);
				}
				else{
					ip.setEditable(true);
				}
			}

		});

		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(option.getSelectedItem().equals("Server")){
					setVisible(false);
					dispose();
					new Server().start();

				}
				else{
					setVisible(false);
					dispose();
					new Client(ip.getText()).start();

				}
			}

		});
		jl.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		jl.add(ip,  new Integer(0));

		jl.add(option,  new Integer(0));
		jl.add(startButton, new Integer(0));

		//jl.add(imageLabel,new Integer(1));


	centerP.add(jl);
	backP.add(imageLabel);

	this.add(centerP,BorderLayout.NORTH);
	this.add(backP, BorderLayout.CENTER);


		//this.add(imageLabel);
		this.repaint();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}


	/**
	 * Loads an image for drawing
	 * @param filenamefile to load
	 * @return Image to draw
	 */
	protected Image loadImage(String filename) { //TODO CHANGE THIS
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
}
