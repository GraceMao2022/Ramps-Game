/* Grace Mao
 * 5/23/29
 * AfterGamePanel.java
 * 
 * 	This class creates a JPanel that shows up after a simulation ends.
 * 	This class has two nested classes added onto its CardLayout:
 * 	MainAfterGamePan and ExplanationPanel. MainAfterGamePan tells
 * 	the user what score they got and has 3 buttons that redirect the 
 * 	user. ExplanationPanel has the same things as the physics 
 * 	instructions in InstruPanel, but also has values from the previous
 * 	round of the game.
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
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class AfterGamePanel extends JPanel
{
	private GameCardPanel gCPan; //instance of GameCardPanel used
		//to change between the different panels and change variables
		//in different files

	private CardLayout aGCards; //layout of AfterGamePanel
	private MainAfterGamePan mainAGPan; //instance of MainAfterGamePan
		//that is added to CardLayout
	public ExplanationPanel expPan; //instance of ExplanationPanel
		//that is added to CardLayout
	
	public boolean win; //set to true by showAfterGamePanel() in
		//RampPanel if the user's score is equal to or above 90%

	private boolean mainShown, expShown; //used to change between
		//the panels in this class's CardLayout: if mainShown is true
		//mainAGPan is shown, if expShown is true, expPan is shown
	
	public AfterGamePanel(GameCardPanel gCP)
	{
		gCPan = gCP;
		
		aGCards = new CardLayout();
		setLayout(aGCards);
		
		mainAGPan = new MainAfterGamePan();
		expPan = new ExplanationPanel();
		
		add(mainAGPan, "Main After Game Panel");
		add(expPan, "Explanation Panel");
	}
	
	//shows the cards in this class's CardLayout depending on which
	//corresponding boolean value is set to true 
	public void showAfterGameCards()
	{
		if(mainShown)
			aGCards.show(this, "Main After Game Panel");
		else if(expShown)
			aGCards.show(this, "Explanation Panel");
	}
	
	//panel that shows right after the simulation ends, contains
	//3 buttons to direct the user to different panels 
	class MainAfterGamePan extends JPanel
	{
		private JButton goHome, playAnotherRound, seeExp; //buttons
			//that when pressed, shows different panels on the screen:
			//when goHome is pressed, StartPanel is shown; when 
			//playAnotherRound is pressed, GamePanel is shown and the
			//game restarts; if seeExp is pressed, ExplanationPanel
			//is shown
			
		public MainAfterGamePan()
		{
			setBackground(Color.BLUE);
			setLayout(null);
			goHome = new JButton("Go To Home Page");
			goHome.setBounds(500,300,200,50);
			goHome.addActionListener(new AGButtonHandler());
			add(goHome);
			
			playAnotherRound = new JButton("Play Another Round");
			playAnotherRound.setBounds(500,375,200,50);
			playAnotherRound.addActionListener(new AGButtonHandler());
			add(playAnotherRound);
			
			seeExp = new JButton("See Explanation");
			seeExp.setBounds(500,450,200,50);
			seeExp.addActionListener(new AGButtonHandler());
			add(seeExp);
			
		}
		
		//tells user if they win or lose, and shows the user's score
		//on the panel
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Font aGFont = new Font("Arial", Font.BOLD, 50);
			g.setFont(aGFont);
			g.setColor(Color.WHITE);
			if(win)
			{
				if(gCPan.settPanel.seInPan.username.equals(""))
					g.drawString("Great Job!", 100, 100);
				else
					g.drawString("Great Job, " + 
						gCPan.settPanel.seInPan.username + "!", 100, 
						100);
			}
			else
			{
				if(gCPan.settPanel.seInPan.username.equals(""))
					g.drawString("Try Harder.", 100, 100);
				else
					g.drawString("Try Harder, " + 
						gCPan.settPanel.seInPan.username + ".", 100, 
						100);
			}
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("You got "+(int)gCPan.gamePanel.rampPan.score +
				" points.", 100, 190); //write score
			g.drawString("Total score: " + 
				gCPan.settPanel.seInPan.totalScore, 100, 220);
		}
		
		//handler class for buttons on MainAfterGamePanel
		class AGButtonHandler implements ActionListener
		{
			public AGButtonHandler()
			{}
			
			//detects which button is pressed and directs user to
			//corresponding panel
			public void actionPerformed(ActionEvent evt)
			{
				String aGComm = evt.getActionCommand();
				if(aGComm.equals("Go To Home Page"))
				{
					//reset variables in GamePanel so that another
					//game can be played later
					gCPan.gamePanel.rampPan.getInitialData();
					
					gCPan.startShown = true;
					gCPan.settShown = gCPan.gameShown = 
						gCPan.instruShown = gCPan.aGShown = false;
					gCPan.showCard(); //used to show StartPanel
				}
				else if(aGComm.equals("Play Another Round"))
				{
					//reset variables in GamePanel so that another
					//game can be played 
					gCPan.gamePanel.rampPan.getInitialData();
					
					gCPan.gameShown = true;
					gCPan.settShown = gCPan.startShown = 
						gCPan.instruShown = gCPan.aGShown = false;
					gCPan.showCard(); //used to show GamePanel
				}
				else if(aGComm.equals("See Explanation"))
				{
					expShown = true;
					mainShown = false;
					showAfterGameCards(); //used to show 
						//ExplanationPanel
				}
			}
		}
	}
	
	class ExplanationPanel extends JPanel
	{
		private JButton goBackToMainAGPan; //shows mainAGPan when 
			//pressed
		public ExpCenterPan expCenPan; //instance of ExpCenterPan
		//added onto BorderLayout.CENTER
		private JScrollBar expScrollBar; //used to show different
			//pages of ExpCenPan
		
		public ExplanationPanel()
		{
			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			
			goBackToMainAGPan = new JButton("Go Back");
			goBackToMainAGPan.setFont(new Font("Tahoma", Font.PLAIN, 
				30));
			goBackToMainAGPan.setPreferredSize(new Dimension(100,50));
			goBackToMainAGPan.addActionListener(new ExpButtonHandler());
			add(goBackToMainAGPan, BorderLayout.SOUTH);
			
			JLabel expLabel = new JLabel("Explanation", JLabel.CENTER);
			expLabel.setFont(new Font("Tahoma", Font.BOLD, 70));
			expLabel.setPreferredSize(new Dimension(400,100));
			
			expScrollBar = new JScrollBar(JScrollBar.VERTICAL, 0,
				20,0,100);
			expScrollBar.setPreferredSize(new Dimension(30,100));
			expScrollBar.addAdjustmentListener(new 
				ExpScrollBarHandler());
			
			expCenPan = new ExpCenterPan();
			
			add(expLabel, BorderLayout.NORTH);
			add(expScrollBar, BorderLayout.EAST);
			add(expCenPan, BorderLayout.CENTER);
		}
		
		class ExpCenterPan extends JPanel
		{
			private JTextArea expArea1, expArea2, expArea3, expArea4;
				//these text areas are added onto the page corresponding
				//to the number they have in their variable name. They
				//display the information that should be shown on each
				//page of the explanation
			private String exp1, exp2, exp3, exp4; //stores explanation
				//for corresponding page
			private CardLayout expCards; //layout of this class, has
				//4 cards (the 4 pages)
			private ExpPage1 expPg1; //page 1 of the explanation
			private ExpPage2 expPg2; //page 2 of the explanation
			private ExpPage3 expPg3; //page 3 of the explanation
			private ExpPage4 expPg4; //page 4 of the explanation
			private boolean showPg1, showPg2, showPg3, showPg4;
				//these variables are used to switch between the
				//different pages of the explanation
			private Scanner expInput; //used to read in from 
				//Instructions.txt
			private String expFileName; //name of file that is read
				//from
			
			public ExpCenterPan()
			{
				expCards = new CardLayout();
				setLayout(expCards);
				
				expPg1 = new ExpPage1();
				expPg2 = new ExpPage2();
				expPg3 = new ExpPage3();
				expPg4 = new ExpPage4();
				
				add(expPg1, "Exp Page 1");
				add(expPg2, "Exp Page 2");
				add(expPg3, "Exp Page 3");
				add(expPg4, "Exp Page 4");
				
				exp1 = new String("");
				exp2 = new String("");
				exp3 = new String("");
				exp4 = new String("");
				
				expFileName = new String("Instructions.txt");
				expInput = null;
				openInstruFile();
			
				getExpPg1();
			}
			
			//shows the different cards added onto ExplanationPanel/
			//This method uses if-else statements to determine which
			//card should be shown
			public void showExpCards()
			{
				if(showPg1)
					expCards.show(this,"Exp Page 1");
				else if(showPg2)
					expCards.show(this,"Exp Page 2");
				else if(showPg3)
					expCards.show(this,"Exp Page 3");
				else if(showPg4)
					expCards.show(this,"Exp Page 4");
			}
			
			//uses try-catch block to instantiate expInput so that
			//the file can be read from
			public void openInstruFile()
			{
				File expInFile = new File(expFileName);
				try
				{
					expInput = new Scanner(expInFile);
				}
				catch(FileNotFoundException e)
				{
					System.err.printf("ERROR: Cannot find/open file %s."+
						"\n\n\n", expFileName);
					System.exit(1);
				}
			}
			
			//rounds time and speed to one and two decimal places 
			//respectively
			public double roundExpData(double dataValue, 
				int decimalPlaces)
			{
				int tempData = 0;
				int multiplier = 0;
				double roundedData = dataValue;
				if(decimalPlaces == 5)
					multiplier = 100000;
				else if(decimalPlaces == 2)
					multiplier = 100;
				else 
					multiplier = 10;
				roundedData *= multiplier;
				roundedData = (double)Math.round(roundedData)/multiplier;
				return roundedData;
			}
			
			//reads from Instructions.txt and takes portions of it
			//to put on page 1 of the explanation. 
			public void getExpPg1()
			{
				openInstruFile();
				String expLine = new String("");
				while(expInput.hasNext())
				{
					expLine = expInput.nextLine();
					if(expLine.indexOf("PhyInfo:")!=-1)
					{
						expLine = expInput.nextLine();
						while(expLine.indexOf("+++++")
							==-1)
						{
							exp1+=expLine+"\n";
							expLine = expInput.nextLine();
						}
					}
				}
				expInput.close();
				setTextToExpArea();
			}
			
			//reads from Instructions.txt and stores only a section
			//of it into the variable exp2, which is the string that
			//will be shown on page 2 of the explanation. This also 
			//adds values from the previous round of the game to
			//the explanation
			public void getExpPg2()
			{
				openInstruFile();
				String expLine = new String("");
				while(expInput.hasNext())
				{
					expLine = expInput.nextLine();
					if(expLine.indexOf("+++++") != -1)
					{
						while(expLine.indexOf("sin@ = slope*cos@")
							== -1)
						{
							expLine = expInput.nextLine();
							exp2 += expLine + "\n";
						}
						//The following lines add the sin and cos
						//of the angle from the previous round of the
						//game to the explanation
						exp2 += "\n\tIn this round:\n";
						exp2 += "\tsin @ = " + roundExpData(
							gCPan.gamePanel.sinAngle, 5) +
							"\n";
						exp2 += "\tcos @ = " + roundExpData(
							gCPan.gamePanel.cosAngle, 5) + 
							"\n\n";
					}
					if(expLine.indexOf("2) Find the forces on the " +
						"ramp") != -1)
					{
						exp2 += expLine + "\n";
						while(expLine.indexOf
							("friction = NForce*frictCoef") == -1)
						{
							expLine = expInput.nextLine();
							exp2 += expLine+"\n";
						}
						//the following lines adds the forces from 
						//the previous round of the game to the 
						//explanation
						exp2 += "\n\tIn this round:\n";
						exp2 += "\tparGForce = " + 
							roundExpData(gCPan.gamePanel.parForce, 5)
							+ "\n";
						exp2 += "\tperGForce = " + 
							roundExpData(gCPan.gamePanel.perForce, 5)
							+ "\n";
						exp2 += "\tNForce = " + roundExpData(
							gCPan.gamePanel.NForce, 5) + "\n";
						exp2 += "\tFriction = " + 
							roundExpData(gCPan.gamePanel.friction, 5)
							+ "\n";
					}
				}
				expInput.close();
				setTextToExpArea();
			}
			
			//reads from Instructions.txt and stores only a section
			//of it into the variable exp3, which is the string that
			//will be shown on page 3 of the explanation. This also 
			//adds values from the previous round of the game to
			//the explanation
			public void getExpPg3()
			{
				openInstruFile();
				String expLine = new String("");
				while(expInput.hasNext())
				{
					expLine = expInput.nextLine();
					if(expLine.indexOf("3) Find the acceleratio") != -1)
					{
						exp3 += expLine + "\n";
						while(expLine.indexOf("acce_Ramp = (parForce -")
							== -1)
						{
							expLine = expInput.nextLine();
							exp3 += expLine+"\n";
						}
						//the following lines adds acceleration on the
						//ramp from the previous round of the game to
						//the explanation
						exp3 += "\n\tIn this round:\n";
						exp3 += "\tAcceleration on ramp = " + 
							roundExpData(gCPan.gamePanel.acce_Ramp,  2)
							+ " m/s^2\n\n";
					}
					if(expLine.indexOf("4) Find the total time") != -1)
					{
						exp3 += expLine + "\n";
						while(expLine.indexOf("timeOnRamp = sqrt(2*")
							== -1)
						{
							expLine = expInput.nextLine();
							exp3 += expLine+"\n";
						}
						//the following lines add the total time on
						//the ramp from the previous round of the game
						//to the explanation
						exp3 += "\n\tIn this round:\n";
						exp3 += "\tTotal time on ramp = " + 
							roundExpData(gCPan.gamePanel.timeOnRamp,  2)
							+ " sec\n\n";
					}	
					if(expLine.indexOf("5) Find the forces") != -1)
					{
						exp3 += expLine + "\n";
						while(expLine.indexOf("friction_Ground = NForc")
							== -1)
						{
							expLine = expInput.nextLine();
							exp3 += expLine+"\n";
						}
						//the following lines add the forces on the 
						//ground from the previous round of the game
						//to the explanation
						exp3 += "\n\tIn this round:\n";
						exp3 += "\tperForce = " + 
							roundExpData(gCPan.gamePanel.perForce,  5)
							+ "\n";
						exp3 += "\tNForce = " +
							roundExpData(gCPan.gamePanel.NForce,  5)
							+ "\n";
						exp3 += "\tfriction = " +
							roundExpData(gCPan.gamePanel.friction,  5)
							+ "\n\n";
					}
				}
				expInput.close();
				setTextToExpArea();
			}
			
			//reads from Instructions.txt and stores only a section
			//of it into the variable exp4, which is the string that
			//will be shown on page 4 of the explanation. This also 
			//adds values from the previous round of the game to
			//the explanation
			public void getExpPg4()
			{
				openInstruFile();
				String expLine = new String("");
				while(expInput.hasNext())
				{
					expLine = expInput.nextLine();
					if(expLine.indexOf("6) Find the accelerati") != -1)
					{
						exp4 += expLine + "\n";
						while(expLine.indexOf("acce_Ground = -fric")
							== -1)
						{
							expLine = expInput.nextLine();
							exp4 += expLine+"\n";
						}
						//the following lines add the acceleration on the 
						//ground from the previous round of the game
						//to the explanation
						exp4 += "\n\tIn this round:\n";
						exp4 += "\tAcceleration on the ground = " + 
							roundExpData(gCPan.gamePanel.acce_Ground,  
							2) + " m/s^2\n\n";
					}
					if(expLine.indexOf("7) Get the initial spee") != -1)
					{
						exp4 += expLine + "\n";
						while(expLine.indexOf("initialSpeedOnGround = ")
							== -1)
						{
							expLine = expInput.nextLine();
							exp4 += expLine+"\n";
						}
						//the following lines add the initial speed on the 
						//ground from the previous round of the game
						//to the explanation
						exp4 += "\n\tIn this round:\n";
						exp4 += "\tinitialSpeedOnGround = " + 
							roundExpData(gCPan.gamePanel.
							initialSpeedOnGround,  2) + " m/s\n\n";
					}
					if(expLine.indexOf("8) Find total tim") != -1)
					{
						exp4 += expLine + "\n";
						expLine = expInput.nextLine();
						exp4 += expLine + "\n";
						//the following lines add the total time on the 
						//ground from the previous round of the game
						//to the explanation
						exp4 += "\n\tIn this round:\n";
						exp4 += "\tTotal time on ground = " + 
							roundExpData(gCPan.gamePanel.
							timeOnGround,  2) + " sec\n\n";
					}
					if(expLine.indexOf("9) Find distance travel") != -1)
					{
						exp4 += expLine + "\n";
						expLine = expInput.nextLine();
						exp4 += expLine + "\n";
						//the following lines add the distance traveled
						//on the ground from the previous round of the 
						//game to the explanation
						exp4 += "\n\tIn this round:\n";
						exp4 += "\tdistance = " + 
							roundExpData((gCPan.gamePanel.rampPan.midX -
							gCPan.gamePanel.widRamp),  2) + " m\n\n";
						exp4 += "\tYou put " + gCPan.gamePanel.rampPan.
							userSetDist + " m as the distance.";
					}
				}
				expInput.close();
				setTextToExpArea();
			}
			
			//this method sets the strings exp1,exp2,exp3,exp4 to the
			//text areas corresponding to them
			public void setTextToExpArea()
			{
				expArea1.setText(exp1);
				expArea2.setText(exp2);
				expArea3.setText(exp3);
				expArea4.setText(exp4);
			}
			
			//panel for page 1 of the explanation
			class ExpPage1 extends JPanel
			{
				private Image resolvedForces; //image shown on the 
				//ExplanationPanel to help user understand the physics
				//behind the game
				private String rlvForcImgName; //name of image above
				
				public ExpPage1()
				{
					setLayout(null);
					expArea1 = new JTextArea();
					expArea1.setEditable(false);
					expArea1.setBounds(240,325,500,175);
					expArea1.setWrapStyleWord(true);
					expArea1.setLineWrap(true);
					expArea1.setFont(new Font("Arial",
						Font.PLAIN,15));
					add(expArea1);
					rlvForcImgName = new String
						("ResolvedForces.jpg");
					resolvedForces = null;
					getMyImage();
				}
				
				//uses try-catch block to get image to put on panel
				public void getMyImage()
				{
					File rlvImgFile = new File(rlvForcImgName);
					try
					{
						resolvedForces = ImageIO.read(rlvImgFile);
					}
					catch(IOException e)
					{
						System.err.println("\n\n"+ rlvForcImgName+
							" can't be found.\n\n");
						e.printStackTrace();
					}
				}
				
				//draws image of ResolvedForces.jpg onto panel
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
					g.drawImage(resolvedForces, 240, 10, 500, 300, 
						this);
				}
			}
			
			//panel for page 2 of the explanation
			class ExpPage2 extends JPanel
			{
				public ExpPage2()
				{
					setLayout(null);
					expArea2 = new JTextArea();
					expArea2.setEditable(false);
					expArea2.setBounds(200,25,600,500);
					expArea2.setWrapStyleWord(true);
					expArea2.setLineWrap(true);
					expArea2.setFont(new Font("Arial",
						Font.PLAIN,15));
					expArea2.setVisible(true);
					add(expArea2);
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
			}
			
			//panel for page 3 of the explanation
			class ExpPage3 extends JPanel
			{
				public ExpPage3()
				{
					setLayout(null);
					expArea3 = new JTextArea();
					expArea3.setEditable(false);
					expArea3.setBounds(200,25,600,500);
					expArea3.setWrapStyleWord(true);
					expArea3.setLineWrap(true);
					expArea3.setFont(new Font("Arial",
						Font.PLAIN,15));
					expArea3.setVisible(true);
					add(expArea3);
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
			}	
			
			//panel for page 4 of the explanation
			class ExpPage4 extends JPanel
			{
				public ExpPage4()
				{
					setLayout(null);
					expArea4 = new JTextArea();
					expArea4.setEditable(false);
					expArea4.setBounds(200,25,600,500);
					expArea4.setWrapStyleWord(true);
					expArea4.setLineWrap(true);
					expArea4.setFont(new Font("Arial",
						Font.PLAIN,15));
					expArea4.setVisible(true);
					add(expArea4);
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
			}	
		}
		
		class ExpScrollBarHandler implements AdjustmentListener
		{
			public ExpScrollBarHandler()
			{}
			
			//checks to see if the value of the scroll bar at a 
			//certain place to show the page that corresponds with
			//the position of the scroll bar
			public void adjustmentValueChanged(AdjustmentEvent evt)
			{
				if(expScrollBar.getValue()>75)
				{
					expCenPan.showPg4 = true;
					expCenPan.showPg1 = expCenPan.showPg2 = 
						expCenPan.showPg3 = false;
				}
				else if(expScrollBar.getValue()>50)
				{
					expCenPan.showPg3 = true;
					expCenPan.showPg1 = expCenPan.showPg2 = 
						expCenPan.showPg4 = false;
				}
				else if(expScrollBar.getValue()>25)
				{
					expCenPan.showPg2 = true;
					expCenPan.showPg1 = expCenPan.showPg3 = 
						expCenPan.showPg4 = false;
				}
				else
				{
					expCenPan.showPg1 = true;
					expCenPan.showPg2 = expCenPan.showPg3 = 
						expCenPan.showPg4 = false;
				}
				expCenPan.showExpCards();
			}
		}
		
		//handler class for the "Go Back" button on ExplanationPanel
		class ExpButtonHandler implements ActionListener
		{
			//handler method for the go back button so that the
			//screen will fo back to MainAfterGamePan when the button
			//is pressed
			public void actionPerformed(ActionEvent evt)
			{
				String expComm = evt.getActionCommand();
				if(expComm.equals("Go Back"))
				{
					mainShown = true;
					expShown = false;
				}
				showAfterGameCards(); //used to show MainAfterGamePan
			}
		}
	}
}
