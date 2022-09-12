/*Grace Mao
 * 5/23/19
 * InstruPanel.java
 * 
 * 	This class has a menu that is used to switch between the different
 * 	sections of the instructions, like the basic instructions, how
 * 	to win instructions, etc. This class reads from the text file
 * 	Instructions.txt to get the instructions onto the panels.
 * 
 * 	Testing:
 * 	- The How To Win instructions should not be on the Basics
 * 	instructions panel, and vice versa
 * 	- Clicking on a menu item should change the screen to the 
 * 	corresponding panel, not other panels

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
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

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

class InstruPanel extends JPanel
{
	private GameCardPanel gCPan; //instance of GameCardPanel
		//used to allow this class to access other classes
		
	public InstruMenuPanel instMenuPan; //one of the panels added
		//onto InstruPanel, contains the menuBar and the goBack 
		//button
	public InstruMainPanel instMainPan; //the other panel added
		//onto InstruPanel, has a CardLayout to show different
		//instruction pages
	
	public boolean gameIsGoingOn; //set to true when a round
		//of the game is going on, so that when the back
		//button is pressed the screen goes back to GamePanel
		//and not StartPanel --- this variable is accessed by
		//a nested class in GamePanel
	
	public InstruPanel(GameCardPanel gCP)
	{
		gCPan = gCP;
		
		setLayout(new BorderLayout(5,5));
		setBackground(Color.CYAN);
		
		JLabel instruLabel = new JLabel("Instructions", 
								JLabel.CENTER);
		instruLabel.setPreferredSize(new Dimension(100,50));
		Font instLabFont = new Font("Arial", Font.BOLD, 30);
		instruLabel.setFont(instLabFont);
		add(instruLabel, BorderLayout.NORTH);
		
		instMenuPan = new InstruMenuPanel();
		instMainPan = new InstruMainPanel();
		instMenuPan.setPreferredSize(new Dimension(200,600));
		add(instMenuPan, BorderLayout.WEST);
		add(instMainPan, BorderLayout.CENTER);
		
		gameIsGoingOn = false;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	class InstruMenuPanel extends JPanel
	{
		private JMenuItem basicItem, hTWItem, imgGame, phyGame,
			settInstru; //when clicked, the instructions page
				//shows a different set of instructions
		private JButton goBack1; //when pressed, screen returns to
			//start page or the game page, depending on if a round
			//of the game is going on or not
		
		public InstruMenuPanel()
		{
			setBackground(Color.PINK);
			setLayout(null);
			
			basicItem = new JMenuItem("Basics");
			hTWItem = new JMenuItem("How to Win");
			imgGame = new JMenuItem("Image of Game");
			phyGame = new JMenuItem("Physics Behind Game");
			settInstru = new JMenuItem("Settings Instructions");
			
			basicItem.addActionListener(new InstruMenuHandler());
			hTWItem.addActionListener(new InstruMenuHandler());
			imgGame.addActionListener(new InstruMenuHandler());
			phyGame.addActionListener(new InstruMenuHandler());
			settInstru.addActionListener(new InstruMenuHandler());
			
			basicItem.setPreferredSize(new Dimension(150,40));
			hTWItem.setPreferredSize(new Dimension(150,40));
			imgGame.setPreferredSize(new Dimension(150,40));
			phyGame.setPreferredSize(new Dimension(150,40));
			settInstru.setPreferredSize(new Dimension(150,40));
			
			JMenu instruMenu = new JMenu("Menu");
			instruMenu.setPreferredSize(new Dimension(175,50));
			
			instruMenu.add(basicItem);
			instruMenu.add(hTWItem);
			instruMenu.add(imgGame);
			instruMenu.add(phyGame);
			instruMenu.add(settInstru);
			
			//add menu bar to InstruMenuPanel
			JMenuBar instMenBar = new JMenuBar();
			instMenBar.setBounds(10,10,175,50);
			instMenBar.add(instruMenu);
			add(instMenBar); 
			
			//add back button to InstruMenuPanel
			goBack1 = new JButton("Go Back");
			goBack1.addActionListener(new BackButtonHandler());
			goBack1.setBounds(25, 550, 150, 50);
			add(goBack1);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}
		
		//changes text shown on goBack1 depending on if game is going on 
		//or not
		public void changeBackButton()
		{
			if(gameIsGoingOn)
				goBack1.setText("Back to Game");
			else
				goBack1.setText("Go Back");
		}
		
		class InstruMenuHandler implements ActionListener
		{
			public InstruMenuHandler()
			{}
			
			//changes booleans in InstruMainPanel depending on which
			//menu item the user clicks
			public void actionPerformed(ActionEvent evt)
			{
				String instruMenuCommand = evt.getActionCommand();
				if(instruMenuCommand.equals("Basics"))
				{
					instMainPan.basicPanShown = true;
					instMainPan.hTWPanShown = instMainPan.imgPanShown = 
						instMainPan.phyPanShown = 
						instMainPan.settInstPanShown = false;
				}
				else if(instruMenuCommand.equals("How to Win"))
				{
					instMainPan.hTWPanShown = true;
					instMainPan.basicPanShown = instMainPan.imgPanShown 
						= instMainPan.phyPanShown = 
						instMainPan.settInstPanShown = false;
				}
				else if(instruMenuCommand.equals("Image of Game"))
				{
					instMainPan.imgPanShown = true;
					instMainPan.hTWPanShown = instMainPan.basicPanShown 
						= instMainPan.phyPanShown = 
						instMainPan.settInstPanShown = false;
				}
				else if(instruMenuCommand.equals("Physics Behind Game"))
				{
					instMainPan.phyPanShown = true;
					instMainPan.hTWPanShown = instMainPan.imgPanShown = 
						instMainPan.basicPanShown = 
						instMainPan.settInstPanShown = false;
				}
				else if(instruMenuCommand.equals
					("Settings Instructions"))
				{
					instMainPan.settInstPanShown = true;
					instMainPan.hTWPanShown = instMainPan.imgPanShown = 
						instMainPan.phyPanShown = 
						instMainPan.basicPanShown = false;
				}
				instMainPan.showInstruCards(); //calls method to show 
					//different panel in InstruMainPan
			}
		}
		
		class BackButtonHandler implements ActionListener
		{
			public BackButtonHandler()
			{}
			
			//handler method for back button on InstruMenuPanel
			public void actionPerformed(ActionEvent evt)
			{
				String backButtonCommand = evt.getActionCommand();
				
				//if the button goBack1 is pressed, boolean for
				//StartPanel becomes true, others become false
				if(backButtonCommand.equals("Go Back"))
				{
					gCPan.startShown = true;
					gCPan.instruShown = gCPan.gameShown 
						= gCPan.settShown = false;
				}
				//if button goBack1 has text "Back to Game", meaning
				//a round of the game is going on, then the boolean for
				//GamePanel becomes true, others become false
				else if(backButtonCommand.equals("Back to Game"))
				{
					gCPan.gameShown = true;
					gCPan.instruShown = gCPan.startShown 
							= gCPan.settShown = false;
				}
				gCPan.showCard(); //called to change panel shown
			}
		}
	}
	
	//has CardLayout which shows different panels for the 
	//instructions
	class InstruMainPanel extends JPanel
	{
		private CardLayout instruCards; //CardLayout for InstruMainPanel
		private JPanel basicPan, hTWPan, imgPan, phyPan, 
			settInstPan; //panels added as cards onto InstruMainPanel
			
		public boolean basicPanShown, hTWPanShown, imgPanShown, 
			phyPanShown, settInstPanShown; //used to show certain card
				//depending on if boolean value is true or false; the
				//boolean values change depending on what menu item
				//the user presses in InstruMenuPanel ---- accessed by
				//InstruMenuPanel
				
		private Scanner instruInput; //reads text from Instructions.txt
		private String instruFileName; //stores name for file that will
			//be read
		
		public InstruMainPanel()
		{
			instruCards = new CardLayout();
			setBackground(Color.WHITE);
			setLayout(instruCards);
			
			instruFileName = new String("Instructions.txt");
			instruInput = null;
			openInstruFile();
			
			basicPan = new BasicPan();
			hTWPan = new HowToWinPan();
			imgPan = new ImagePan();
			phyPan = new PhysicsPan();
			settInstPan = new SettInstruPan();
			
			add(basicPan, "Basics Card");
			add(hTWPan, "HTW Card");
			add(imgPan, "Image Card");
			add(phyPan, "Physics Card");
			add(settInstPan, "SettInstru Card");
			
			basicPanShown = hTWPanShown = imgPanShown = phyPanShown = 
				settInstPanShown = false;
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}
		
		//shows cards in CardLayout depending on their corresponding
		//boolean value
		public void showInstruCards()
		{
			if(basicPanShown)
				instruCards.show(this, "Basics Card");
			else if(hTWPanShown)
				instruCards.show(this, "HTW Card");
			else if(imgPanShown)
				instruCards.show(this, "Image Card");
			else if(phyPanShown)
				instruCards.show(this, "Physics Card");
			else if(settInstPanShown)
				instruCards.show(this, "SettInstru Card");
		}
		
		//uses try-catch block to create instance of a Scanner that
		//reads from Instructions.txt
		public void openInstruFile()
		{
			File instruInFile = new File(instruFileName);
			try
			{
				instruInput = new Scanner(instruInFile);
			}
			catch(FileNotFoundException e)
			{
				System.err.printf("ERROR: Cannot find/open file %s."+
					"\n\n\n", instruFileName);
				System.exit(1);
			}
		}
		
		//contains basic instructions
		class BasicPan extends JPanel
		{
			public BasicPan()
			{
				setLayout(new BorderLayout());
				setBackground(Color.RED);
				
				JLabel basicLabel = new JLabel("Basics",JLabel.CENTER);
				basicLabel.setPreferredSize(new Dimension(100,50));
				basicLabel.setFont(new Font("Arial",Font.BOLD,30));
				add(basicLabel, BorderLayout.NORTH);
				
				BasicCenterPanel basicCenPan = new BasicCenterPanel();
				add(basicCenPan, BorderLayout.CENTER);
			}
			
			//panel located BorderLayout.CENTER with text area that
			//shows the basic instructions
			class BasicCenterPanel extends JPanel
			{
				private JTextArea basicInstruArea; //displays basic 
					//instructions for game
				private String basicInstru; //stores basic instructions 
					//for game
				
				public BasicCenterPanel()
				{
					setBackground(Color.RED);
					basicInstru = new String("");
					
					basicInstruArea = new JTextArea();
					basicInstruArea.setEditable(false);
					basicInstruArea.setBounds(0,0,800,600);
					basicInstruArea.setWrapStyleWord(true);
					basicInstruArea.setLineWrap(true);
					basicInstruArea.setFont(new Font("Arial",Font.PLAIN,
						30));
					add(basicInstruArea);
					
					getBasicInstru();
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
				
				//uses a while-loop to read from Instructions.txt 
				//using the Scanner instruLine. If-else statements
				//are used so that only parts of the text file are
				//stored into a variable 
				public void getBasicInstru()
				{
					openInstruFile(); //to be able to read from 
						//Instructions.txt
					
					String instruLine = new String("");
					while(instruInput.hasNext())
					{
						instruLine = instruInput.nextLine();
						
						//the code below is used to only store the 
						//needed portion of the instructions into the 
						//variable basicInstru
						if(instruLine.indexOf("Basics:")!=-1)
						{
							instruLine = instruInput.nextLine();
							while(instruLine.indexOf("BasicsInstruEnd") 
								== -1)
							{
								basicInstru += instruLine+"\n";
								instruLine = instruInput.nextLine();
							}
						}
					}
					instruInput.close(); //so that Instructions.txt can 
						//be reread later on
					setTextToBasicInstruArea();
				}
				
				//sets the text of basicInstruArea to basicInstru
				public void setTextToBasicInstruArea()
				{
					basicInstruArea.setText(basicInstru);
				}
			}
		}
		
		//contains more detailed instructions on how score is calculated,
		//etc.
		class HowToWinPan extends JPanel
		{
			public HowToWinPan()
			{
				setLayout(new BorderLayout());
				setBackground(Color.ORANGE);
				
				JLabel hTWPanLabel = new JLabel("How to Win",
					JLabel.CENTER);
				hTWPanLabel.setPreferredSize(new Dimension(100,50));
				hTWPanLabel.setFont(new Font("Arial",Font.BOLD,30));
				add(hTWPanLabel, BorderLayout.NORTH);
				
				HTWCenterPanel hTWCenPan = new HTWCenterPanel();
				add(hTWCenPan, BorderLayout.CENTER);
			}
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
			
			//panel located BorderLayout.CENTER
			class HTWCenterPanel extends JPanel
			{
				private JTextArea hTWInstruArea; //displays how to win 
					//instructions for game
				private String hTWInstru; //stores how to win 
					//instructions for game
			
				public HTWCenterPanel()
				{
					setBackground(Color.ORANGE);
					
					hTWInstru = new String("");
					
					hTWInstruArea = new JTextArea();
					hTWInstruArea.setEditable(false);
					hTWInstruArea.setBounds(0,0,800,600);
					hTWInstruArea.setWrapStyleWord(true);
					hTWInstruArea.setLineWrap(true);
					hTWInstruArea.setFont(new Font("Arial",Font.PLAIN,
						30));
					add(hTWInstruArea);
					
					getHTWInstru();
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
				
				//reads from Instructions.txt and stores a portion
				//of the text file into the variable hTWInstru
				public void getHTWInstru()
				{
					openInstruFile();
					String instruLine = new String("");
					while(instruInput.hasNext())
					{
						instruLine = instruInput.nextLine();
						if(instruLine.indexOf("How To Win")!=-1)
						{
							instruLine = instruInput.nextLine();
							while(instruLine.indexOf
								("HowToWinInstruEnd") == -1)
							{
								hTWInstru += instruLine + "\n";
								instruLine = instruInput.nextLine();
							}
						}
					}
					instruInput.close();
					setTextToHTWInstruArea();
				}
				
				//sets the text of hTWInstruArea to hTWInstru
				public void setTextToHTWInstruArea()
				{
					hTWInstruArea.setText(hTWInstru);
				}
			}
		}
		
		//has screen shot of game with labeled parts to help user 
		//understand set up of the game
		class ImagePan extends JPanel
		{
			public ImagePan()
			{
				setLayout(new BorderLayout());
				setBackground(Color.YELLOW);
				
				JLabel imgPanLabel = new JLabel("Image of Game",
					JLabel.CENTER);
				imgPanLabel.setPreferredSize(new Dimension(100,50));
				imgPanLabel.setFont(new Font("Arial",Font.BOLD,30));
				add(imgPanLabel, BorderLayout.NORTH);
				
				ImageCenterPan imgCenPan = new ImageCenterPan();
				add(imgCenPan, BorderLayout.CENTER);
			}
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
			
			//panel located BorderLayout.CENTER and has the image
			//of the game drawn on it
			class ImageCenterPan extends JPanel
			{
				private Image imgForGame; //image that will be shown
					//on "Image of Game" part of instructions
				private String gameImgName; //name of the image file
				
				public ImageCenterPan()
				{
					setBackground(Color.YELLOW);
					imgForGame = null;
					gameImgName = new String("ImgForGame.jpg");
					getMyImage();
				}
				
				//draws the image of the game onto the panel
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
					g.drawImage(imgForGame, 10,10,900,500, this);
				}
				
				//uses try-catch block to open file for the image
				public void getMyImage()
				{
					File gameImgFile = new File(gameImgName);
					try
					{
						imgForGame = ImageIO.read(gameImgFile);
					}
					catch(IOException e)
					{
						System.err.println("\n\n" + gameImgName +
							" can't be found.\n\n");
						e.printStackTrace();
					}
				}
			}
		}
		
		//has image and information on the physics behind the game
		class PhysicsPan extends JPanel
		{
			private JScrollBar phyScrollBar; //a scroll bar used to
				//switch between the different pages of the physics
				//instructions
			private PhyCenterPanel phyCenPan; //instance of panel 
				//located BorderLayout.CENTER and has CardLayout
				//with the different pages added onto it as cards
			
			public PhysicsPan()
			{
				setLayout(new BorderLayout());
				setBackground(Color.GREEN);
				
				JLabel phyPanLabel = new JLabel("Physics Behind Game",
					JLabel.CENTER);
				phyPanLabel.setPreferredSize(new Dimension(100,50));
				phyPanLabel.setFont(new Font("Arial",Font.BOLD,30));
				add(phyPanLabel, BorderLayout.NORTH);
				
				phyCenPan = new PhyCenterPanel();
				add(phyCenPan, BorderLayout.CENTER);
				
				phyScrollBar = new JScrollBar(JScrollBar.VERTICAL, 0,
					30,0,100);
				phyScrollBar.setPreferredSize(new Dimension(25,100));
				phyScrollBar.addAdjustmentListener(new 
					PhyInstruScrollBarHandler());
				add(phyScrollBar, BorderLayout.EAST);
			}
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
	
			//panel located BorderLayout.CENTER
			class PhyCenterPanel extends JPanel
			{
				private JTextArea phyInstruArea1, phyInstruArea2,
					phyInstruArea3;
				//displays physics instructions, one for each different
				//page
				
				private String phyInstru1, phyInstru2, phyInstru3;
				//store physics instructions for game, one for each
				//different page
				
				private CardLayout phyInstruCards; //layout for this
					//panel
				private PhyInstruPage1 phyPg1; //instance of page 1
					//added onto the CardLayout
				private PhyInstruPage2 phyPg2; //instance of page 2
					//added onto the CardLayout
				private PhyInstruPage3 phyPg3; //instance of page 3
					//added onto the CardLayout
				private boolean pg1Shown, pg2Shown, pg3Shown; //used
					//to switch between the different pages of the
					//physics instructions
				
				public PhyCenterPanel()
				{
					setBackground(Color.GREEN);
						
					phyInstruCards = new CardLayout();
					setLayout(phyInstruCards);
					
					phyPg1 = new PhyInstruPage1();
					phyPg2 = new PhyInstruPage2();
					phyPg3 = new PhyInstruPage3();
					
					add(phyPg1, "Page 1");
					add(phyPg2, "Page 2");
					add(phyPg3, "Page 3");
					
					phyInstru1 = new String("");
					phyInstru2 = new String("");
					phyInstru3 = new String("");
					
					getPhyInstru();
				}
				
				//uses the values of the boolean field variables
				//to show the different pages of the physics
				//instructions
				public void showPhyInstruCards()
				{
					if(pg1Shown)
						phyInstruCards.show(this,"Page 1");
					else if(pg2Shown)
						phyInstruCards.show(this,"Page 2");
					else if(pg3Shown)
						phyInstruCards.show(this,"Page 3");
				}
				
				//reads Instructions.txt and stores the needed sections
				//of the text file into the String field variables
				//phyInstru1, phyInstru2, and phyInstru3
				public void getPhyInstru()
				{
					openInstruFile();
					String phyInstruLine = new String("");
					while(instruInput.hasNext())
					{
						phyInstruLine = instruInput.nextLine();
						//read text for first page
						if(phyInstruLine.indexOf("PhyInfo") != -1)
						{
							phyInstruLine = instruInput.nextLine();
							while(phyInstruLine.indexOf("+++++")
								== -1)
							{
								phyInstru1 += phyInstruLine + "\n";
								phyInstruLine = instruInput.nextLine();
							}
						}
						//read text for second page
						if(phyInstruLine.indexOf("+++++") != -1)
						{
							phyInstruLine = instruInput.nextLine();
							while(phyInstruLine.indexOf("------")
								== -1)
							{
								phyInstru2 += phyInstruLine + "\n";
								phyInstruLine = instruInput.nextLine();
							}
						}
						//read text for third page
						if(phyInstruLine.indexOf("------")!=-1)
						{
							phyInstruLine = instruInput.nextLine();
							while(phyInstruLine.indexOf("PhyInfoEnd")
								==-1)
							{
								phyInstru3+=phyInstruLine + "\n";
								phyInstruLine = instruInput.nextLine();
							}
						}
					}
					instruInput.close();
					setTextToPhyInstruArea();
				}
				
				//sets the text of the JTextAreas for physics 
				//instructions to corresponding String variables
				public void setTextToPhyInstruArea()
				{
					phyInstruArea1.setText(phyInstru1);
					phyInstruArea2.setText(phyInstru2);
					phyInstruArea3.setText(phyInstru3);
				}
				
				//page 1 for physics instructions ---- has one image
				//and text area
				class PhyInstruPage1 extends JPanel
				{
					private Image resolvedForces; //image shown on the 
					//ExplanationPanel to help user understand the physics
					//behind the game
					private String rlvForcImgName; //name of image above
					
					public PhyInstruPage1()
					{	
						setLayout(null);
						phyInstruArea1 = new JTextArea();
						phyInstruArea1.setEditable(false);
						phyInstruArea1.setBounds(240,325,500,175);
						phyInstruArea1.setWrapStyleWord(true);
						phyInstruArea1.setLineWrap(true);
						phyInstruArea1.setFont(new Font("Arial",
							Font.PLAIN,15));
						add(phyInstruArea1);
						
						rlvForcImgName = new String
							("ResolvedForces.jpg");
						resolvedForces = null;
						getMyImage();
					}
					
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
					
					//draws image of the resolved forces diagram onto
					//panel
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						g.drawImage(resolvedForces, 240, 10, 500, 300, 
							this);
					}
				}
				
				//page 2 for physics instructions ---- has one text 
				//area
				class PhyInstruPage2 extends JPanel
				{
					public PhyInstruPage2()
					{
						setLayout(null);
						phyInstruArea2 = new JTextArea();
						phyInstruArea2.setEditable(false);
						phyInstruArea2.setBounds(150,25,600,500);
						phyInstruArea2.setWrapStyleWord(true);
						phyInstruArea2.setLineWrap(true);
						phyInstruArea2.setFont(new Font("Arial",
							Font.PLAIN,15));
						phyInstruArea2.setVisible(true);
						add(phyInstruArea2);
					}
					
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
					}
				}
				
				//page 3 for physics instructions ---- has one text
				//area
				class PhyInstruPage3 extends JPanel
				{
					public PhyInstruPage3()
					{
						setLayout(null);
						phyInstruArea3 = new JTextArea();
						phyInstruArea3.setEditable(false);
						phyInstruArea3.setBounds(150,25,600,450);
						phyInstruArea3.setWrapStyleWord(true);
						phyInstruArea3.setLineWrap(true);
						phyInstruArea3.setFont(new Font("Arial",
							Font.PLAIN,15));
						phyInstruArea3.setVisible(true);
						add(phyInstruArea3);
					}
					
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
					}
				}
			}
			
			class PhyInstruScrollBarHandler implements 
				AdjustmentListener
			{
				public PhyInstruScrollBarHandler()
				{}
				
				//gets the value of the scroll bar and changes
				//boolean variables that determine which page of
				//the physics instructions is shown
				public void adjustmentValueChanged(AdjustmentEvent evt)
				{
					if(phyScrollBar.getValue()>65)
					{
						phyCenPan.pg3Shown = true;
						phyCenPan.pg1Shown = phyCenPan.pg2Shown = false;
					}
					else if(phyScrollBar.getValue()>32)
					{
						phyCenPan.pg2Shown = true;
						phyCenPan.pg1Shown = phyCenPan.pg3Shown = false;
					}
					else
					{
						phyCenPan.pg1Shown = true;
						phyCenPan.pg2Shown = phyCenPan.pg3Shown = false;
					}
					phyCenPan.showPhyInstruCards();
				}
			}
		}
		
		//has instructions on how the settings is set up
		class SettInstruPan extends JPanel
		{
			public SettInstruPan()
			{
				setLayout(new BorderLayout());
				setBackground(Color.BLUE);
				JLabel settInstLabel = new JLabel("Settings "+
					"Instructions", JLabel.CENTER);
				settInstLabel.setPreferredSize(new Dimension(100,50));
				settInstLabel.setFont(new Font("Arial",Font.BOLD,30));
				add(settInstLabel, BorderLayout.NORTH);
				SettInstruCenterPanel settInstCenPan = 
					new SettInstruCenterPanel();
				add(settInstCenPan, BorderLayout.CENTER);
			}
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
			
			//panel that contains actual instructions for 
			//"Settings Instructions"
			class SettInstruCenterPanel extends JPanel
			{
				private JTextArea settInstruArea; //displays settings
				//instructions for game
				private String settInstru; //stores settings 
				//instructions for game
				
				public SettInstruCenterPanel()
				{
					setLayout(null);
					setBackground(Color.BLUE);
					
					settInstru = new String("");
					
					settInstruArea = new JTextArea();
					settInstruArea.setEditable(false);
					settInstruArea.setBounds(100,0,700,525);
					settInstruArea.setWrapStyleWord(true);
					settInstruArea.setLineWrap(true);
					settInstruArea.setFont(new Font("Arial",Font.PLAIN,
						20));
					add(settInstruArea);
					
					getSettInstru();
				}
				
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
				}
				
				//reads portion of Instructions.txt that has 
				//settings instructions
				public void getSettInstru()
				{
					openInstruFile();
					String instruLine = new String("");
					while(instruInput.hasNext())
					{
						instruLine = instruInput.nextLine();
						if(instruLine.indexOf("SettInstru")!=-1)
						{
							instruLine = instruInput.nextLine();
							while(instruLine.indexOf("SettInstruEnd") 
								== -1)
							{
								settInstru+=instruLine+"\n";
								instruLine = instruInput.nextLine();
							}
						}
					}
					instruInput.close();
					setTextToSettInstruArea();
				}
				
				//sets the text of settInstruArea to settInstru
				public void setTextToSettInstruArea()
				{
					settInstruArea.setText(settInstru);
				}
			}
		}
	}
}
