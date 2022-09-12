/* Grace Mao
 * 5/23/19
 * StartPanel.java
 * 
 * 	This class has 3 buttons added onto it: a button to go to the
 * 	instructions, a button to play the game, and a button to go to
 * 	the settings. This is the home screen for the game, and is the
 * 	very first screen the user sees when they run the game.
 * 
 * 	Testing:
 * 	- When the user presses a button, the screen should show the
 * 	corresponding panel (ex. Pressing "Instructions" should not cause
 * 	the screen to go to the game panel)

*/
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

class StartPanel extends JPanel
{
	private GameCardPanel gCPan; //instance of GameCardPanel
		//that allows this class to access other classes
		
	private JButton instruButton, playButton, settButton;
		//buttons added onto StartPanel and change the panel shown
		//when clicked

	public StartPanel(GameCardPanel gCP)
	{
		gCPan = gCP; //instantiating the instance of GameCardPanel in 
			//this class to the instance of GameCardPanel in the file
			//RampsGame.java. Therefore, all of the different files
			//shares the same instance of GameCardPanel
		
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 50));
		
		JLabel title = new JLabel("Ramps", JLabel.CENTER);
		instruButton = new JButton("Instructions");
		instruButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		playButton = new JButton("Play Game");
		playButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		settButton = new JButton("Settings");
		settButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		Font titleFont = new Font("Tahoma", Font.BOLD, 70);
		title.setFont(titleFont); //changes font of title
		title.setPreferredSize(new Dimension(250, 125));
		instruButton.setPreferredSize(new Dimension(200, 50));
		playButton.setPreferredSize(new Dimension(240, 90));
		settButton.setPreferredSize(new Dimension(200, 50));
		
		instruButton.addActionListener(new ButtonHandler());
		playButton.addActionListener(new ButtonHandler());
		settButton.addActionListener(new ButtonHandler());
		
		add(title);
		add(instruButton);
		add(playButton);
		add(settButton);
	}
	
	//draws graphics shown on start screen (the ramps and the blocks)
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(100, 500, 1000, 500);
		
		g.setColor(Color.PINK);
		int [] xCoor1 = {100, 100, 300};
		int [] yCoor1 = {300, 500, 500};
		g.fillPolygon(xCoor1, yCoor1, 3);
		
		g.setColor(Color.RED);
		int [] xCoor2 = {140,175,210,175};
		int [] yCoor2 = {340,305,340,375};
		g.fillPolygon(xCoor2, yCoor2, 4);
		
		g.setColor(Color.ORANGE);
		g.fillRect(300, 450, 50, 50);
		
		g.setColor(Color.YELLOW);
		g.fillRect(450, 450, 50, 50);
		
		g.setColor(Color.GREEN);
		g.fillRect(600, 450, 50, 50);
		
		g.setColor(Color.BLUE);
		g.fillRect(750, 450, 50, 50);
		
		g.setColor(Color.MAGENTA);
		g.fillRect(900, 450, 50, 50);
	}
	
	class ButtonHandler implements ActionListener
	{
		//if a certain button is pressed, the corresponding panel
		//is shown by setting its corresponding boolean variable
		//to true and the others to false
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if(command.equals("Instructions"))
			{
				gCPan.instruShown = true;
				gCPan.startShown = gCPan.gameShown = gCPan.settShown 
					= gCPan.aGShown = false;
			}
			else if(command.equals("Play Game"))
			{
				gCPan.gameShown = true;
				gCPan.startShown = gCPan.instruShown = gCPan.settShown 
					= gCPan.aGShown = false;
				gCPan.gamePanel.rampPan.getInitialData();
			}
			else if(command.equals("Settings"))
			{
				gCPan.settShown = true;
				gCPan.startShown = gCPan.gameShown = gCPan.instruShown 
					= gCPan.aGShown = false;
			}
			gCPan.showCard(); //calling method to change panel shown
		}
	}
}
