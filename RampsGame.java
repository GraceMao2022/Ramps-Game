/* Grace Mao
 * 5/23/19
 * RampsGame.java
 * 
 * 	This file contains two classes: RampsGame and GameCardPanel.
 * 	RampsGame has the main method, and creates the JFrame where the
 * 	game is shown. GameCardPanel is the JPanel that has many other
 * 	JPanels added to it using CardLayout, like StartPanel, InstruPanel,
 * 	etc.
 * 
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

public class RampsGame
{
	public RampsGame()
	{}
	
	public static void main(String [] args)
	{
		RampsGame rGame = new RampsGame();
		rGame.run();
	}
	
	public void run()
	{
		JFrame rampFrame = new JFrame();
		rampFrame.setSize(1150,700);
		rampFrame.setLocation(50,10);
		rampFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rampFrame.setResizable(false);
		GameCardPanel gCP = new GameCardPanel();
		rampFrame.getContentPane().add(gCP);
		rampFrame.setVisible(true);
	}
}

class GameCardPanel extends JPanel
{
	private CardLayout gameCards; //the layout for GameCardPanel, used
		//to show different cards added onto GameCardPanel
	
	public StartPanel startPanel; //cards added
	public InstruPanel instPanel; //onto GameCardPanel:
	public GamePanel gamePanel; //the start panel, instructions panel,
	public SettingsPanel settPanel; //game panel, after game panel, and 
	public AfterGamePanel aGPanel; // setting panel 

	public boolean startShown, instruShown, gameShown, settShown, 
		aGShown; //used to show different panels depending on if boolean 
		//values are true or false
	
	public GameCardPanel()
	{
		gameCards = new CardLayout();
		setLayout(gameCards);
		
		startPanel = new StartPanel(this);
		instPanel = new InstruPanel(this);
		settPanel = new SettingsPanel(this);
		gamePanel = new GamePanel(this);
		aGPanel = new AfterGamePanel(this);
		
		add(startPanel, "Start");
		add(instPanel, "Instru");
		add(gamePanel, "Game");
		add(settPanel, "Settings");
		add(aGPanel, "After Game");
		
		startShown = instruShown = gameShown = aGShown = 
			settShown = false;
	}
	
	//if the boolean value is true for a respective card, that card is
	//shown
	public void showCard()
	{
		if(startShown)
			gameCards.show(this, "Start");
		else if(instruShown)
			gameCards.show(this, "Instru");
		else if(gameShown)
			gameCards.show(this, "Game");
		else if(settShown)
			gameCards.show(this, "Settings");
		else if(aGShown)
			gameCards.show(this,  "After Game");
	}
}
