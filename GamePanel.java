/*Grace Mao
 * 5/23/19
 * GamePanel.java
 * 
 * 	This class is where the animation and the actual game happens.
 * 	It has three main classes: DataPanel, RampPanel, and BottomPanel.
 *  DataPanel is where the data is shown for the game (ex. slope of 
 * 	ramp, speed, etc). RampPanel is where the animation occurs and
 *  where a JTextArea shows up for the work space. BottomPanel has
 * 	two buttons, one to start the simulation, one to open or close 
 * 	the work space.

*/
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

//the actual game panel
class GamePanel extends JPanel
{
	private GameCardPanel gCPan; //stores instance of GameCardPanel
	//so that this class can access GameCardPanel
	
	private DataPanel dataPan; //shows the data of the simulation
		//(ex. speed, coefficient for friction, etc), also has
		//a help button that opens the instructions when clicked
	public RampPanel rampPan; //where the actual simulation takes
		//place, interactive with the user 
	private BottomPanel botPan; //has two buttons, one to open
		//a work area for the user and another to start the
		//simulation

	//the following variables are accessed by AfterGamePanel(more 
	//specifically ExplanationPanel for the explanation after each
	//round)
	public double slope, frictCoef, massOfBlock, acce_Ramp, 
		acce_Ground, speed;
		//data used to run animation and to also show the user in
		//DataPanel
	public final int LENGTHOFSLOPE; //the length of the slope which
		//stays constant whatever the slope
	public double cosAngle, sinAngle; //stores cos and sin of
		//the right-most angle of the ramp
	public double widRamp, heightRamp; //stores the width and 
		//height of the ramp (changed when slope of ramp changes)
	public double parForce, perForce, friction, NForce; //Gforces
		//acting on block (see diagram for Week 2)
	public double timeOnRamp, timeOnGround; //stores total time 
		//block is on ramp and total time block is on ground
		//respectively
	public double initialSpeedOnGround; //initialSpeedOnGround stores 
		//the speed of the block when it first meets the ground;
		
	private double time; //stores total time that the simulation has ran
	private final double GRAVFORCE; //force of gravity (on Earth)
	
	public GamePanel(GameCardPanel gCP)
	{
		gCPan = gCP; //stores instance of GameCardPanel sent in as
			//parameter into gCPan
		setLayout(new BorderLayout());
		
		//initialize variables
		slope = frictCoef = massOfBlock = acce_Ramp = acce_Ground = 
			speed = 0.0;
		time = 0.0;
		cosAngle = sinAngle = 0.0;
		widRamp = heightRamp = 0.0;
		parForce = perForce = friction = NForce = 0.0;
		
		LENGTHOFSLOPE = 200;
		GRAVFORCE = 9.8;
		
		dataPan = new DataPanel();
		dataPan.setPreferredSize(new Dimension(100,150));
		rampPan = new RampPanel();
		botPan = new BottomPanel();
		botPan.setPreferredSize(new Dimension(100,60));
		add(dataPan, BorderLayout.NORTH);
		add(rampPan, BorderLayout.CENTER);
		add(botPan, BorderLayout.SOUTH);
	}
	
	//draws strings to show data of the simulation to the user
	class DataPanel extends JPanel
	{
		private JButton helpButton; //button that shows PhysicsPan when
		//pressed
			
		public DataPanel()
		{
			setBackground(Color.PINK);
			setLayout(null);
			
			helpButton = new JButton("Help?");
			helpButton.setBounds(950,15,150,50);
			helpButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
			helpButton.addActionListener(new HelpButtonHandler());
			add(helpButton);
		}
		
		//draws strings to show data of the simulation
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(Color.RED); //red for given values
			Font font = new Font("Arial", Font.BOLD, 25);
			setFont(font);
			g.drawString("Slope = " + roundData(slope,5), 5, 30);
			g.drawString("Friction Coefficient = " + roundData
				(frictCoef,5), 5, 55);
			g.drawString("Mass of block = " + massOfBlock + " kg",
				5, 80);
			g.drawString("Length of ramp = " + LENGTHOFSLOPE + 
				" m", 5, 105);
			g.setColor(Color.BLACK); //black for values that change 
				//during simulation
			g.drawString("Time = " + roundData(time,1) + " s", 450, 30);
			g.drawString("Speed = " + roundData(speed,2) + " m/s", 
				450, 55);
		}
		
		//rounds time and speed to one and two decimal places 
		//respectively
		public double roundData(double dataValue, int decimalPlaces)
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
		
		//handler class for the "Help?" button in DataPanel
		class HelpButtonHandler implements ActionListener
		{
			public HelpButtonHandler()
			{}
			
			//Detects if the user presses the "Help?" button, and opens 
			//InstruPanel with PhysicsPan shown to help user. If the 
			//simulation is running, the button does not respond
			public void actionPerformed(ActionEvent evt)
			{
				String helpComm = evt.getActionCommand();
				
				//if user presses "Help?" button and if simulation is
				//not running
				if(helpComm.equals("Help?")&&!rampPan.simRunning)
				{
					gCPan.instPanel.gameIsGoingOn = true; //to allow
						//the goBack1 button in InstruPanel to change
						//to "Back to Game" instead of "Go Back"
					
					//changes boolean values to show InstruPanel
					gCPan.instruShown = true;
					gCPan.gameShown = gCPan.startShown 
							= gCPan.settShown = false;
							
					//changes boolean values to show PhysicsPan inside
					//InstruPanel
					gCPan.instPanel.instMainPan.phyPanShown = true;
					gCPan.instPanel.instMainPan.hTWPanShown = 
						gCPan.instPanel.instMainPan.imgPanShown = 
						gCPan.instPanel.instMainPan.basicPanShown = 
						gCPan.instPanel.instMainPan.settInstPanShown 
						= false;
						
					//calls method to change cards in InstruPanel
					//to PhysicsPan
					gCPan.instPanel.instMainPan.showInstruCards();
					
					//calls method to change the text on the goBack1
					//button in InstruPanel
					gCPan.instPanel.instMenuPan.changeBackButton();
					gCPan.showCard(); //calls method to show InstruPanel
				}
			}
		}
	}
	
	//where the animation is drawn and where data in the simulation
	//is calculated
	class RampPanel extends JPanel implements MouseListener,
		MouseMotionListener
	{
		private int [] sqrX, sqrY; //stores x and y coordinates 
			//respectively to draw the square on the computer screen
		private int [] nonCompSqrX, nonCompSqrY; //coordinates of 
			//square on basic xy-axis, not converted to computer
			//coordinates
		private final int LENGTHOFBLOCK, FRAMEHEIGHT, FRAMEWID;
			//constants that store the length of a side of the block and 
			//the frame height of RampPanel
		private Timer timer; //used for animation
		private int [] rampX, rampY; //stores the coord of the ramp
			//(already converted into computer coordinates)
		private boolean markPlaced, showMark;//markPlaced is set to true 
			//if user places down the mark in which they think the block 
			//will stop; showMark is set to true if user moves their
			//mouse into a region to set their mark
		private double timeSinceOnGround; //timeSinceOnGround is the 
			//time since the block met the ground
		private int mouseXCoor, markXCoor; //mouseXCoor stores x-coord
			//of mouse when it moves, markXCoor stores x-coord when
			//mouse clicks in the region to set the mark
		private boolean simRunning; //set to true when timer starts
		
		private JTextArea workSpace; //a text area that allows user
			//to write down their work if they need to
		
		//the following variables are either accessed by InstruPanel
		//or AfterGamePanel
		public double midX, midY; //stores the coord of the bottom
			//midpoint of the square. It is not converted into computer
			//coordinates. Used to find other coordinates of square
		public int score; //stores score that user gets depending
			//on how far their mark is from actual distance that block
			//moves
		public boolean simOver; //set to true if the simulation ends
		public boolean workSpaceOpen; //set to true if user presses
			//"Open Work Space" button
		public int userSetDist; //the distance of the mark from the
			//lower left corner of the ramp
		
		public RampPanel()
		{
			setBackground(Color.WHITE);
			setLayout(null);
			sqrX = new int[4];
			sqrY = new int[4];
			nonCompSqrX = new int[4];
			nonCompSqrY = new int[4];
			rampX = new int[3];
			rampY = new int[3];
			time = 0.0;
			markPlaced = false;
		
			LENGTHOFBLOCK = 50;
			FRAMEHEIGHT = 463;
			FRAMEWID = 1150;
			
			workSpace = new JTextArea("Your workspace: ");
			workSpace.setBounds(700,50,300,300);
			workSpace.setBackground(Color.LIGHT_GRAY);
			workSpace.setLineWrap(true);
			workSpace.setWrapStyleWord(true);
			add(workSpace);
			workSpace.setVisible(false);
			
			Mover move = new Mover(); //ActionListener object
			timer = new Timer(25, move);

			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		//draws the animation and directions on the center panel
		//of GamePanel
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Font font = new Font("Arial", Font.BOLD, 20);
			setFont(font);
			g.setColor(gCPan.settPanel.seInPan.blockColor); //sets the color
				//of block to what user wants
			g.fillPolygon(sqrX,sqrY,4); //draw square
			g.setColor(gCPan.settPanel.seInPan.rampColor); //sets the color
				//of ramp to what user wants
			g.fillPolygon(rampX,rampY,3); //draw ramp
			
			//if simulation is not running and if user has mouse in 
			//region to set the mark
			if(showMark&&!simRunning) 
			{
				g.setColor(Color.PINK);
				g.fillRect(mouseXCoor, 413, 5, 50);
				g.setColor(Color.GRAY);
				g.drawString((int)(mouseXCoor-widRamp)+" m",
					mouseXCoor-8,411);
			}
			//if user has clicked to mark where they think square will 
			//end up
			if(markPlaced) 
			{
				g.setColor(Color.RED);
				g.fillRect(markXCoor, 413, 5, 50); //draw mark
				g.setColor(Color.BLACK);
				//draw distance of mark from lower right corner of ramp
				g.drawString(userSetDist+" m",markXCoor-8,403);
			}
			else //mark is not placed
			{
				g.setColor(Color.BLACK);
				g.drawString("Please click below to set your mark.", 
					300,390); //prompt user to set mark
			}
			if(time>timeOnRamp) //if square is on ground
			{
				g.setColor(Color.BLACK);
				g.drawString((int)(midX-widRamp)+" m",
					(int)midX-8,414); //show distance square is from
					//lower right corner of ramp
			}
		}
		
		//calls methods that calculate data in simulation and coord
		//of initial ramp and block
		public void getInitialData()
		{
			resetVariables();
			showWorkSpace(true);
			getRandomValues();
			
			//get initial set up for new round of the game
			getSinCosAngle();
			getHeightWidthOfRamp();
			midX = 0; //initial value of x-coord
			midY = heightRamp; //initial value of y-coord(not converted
				//to computer coord)
			calcForcesAndAcceOnRamp();
			calcTimeWhenBlockReachesGround();
			calcForcesAndAcceOnGround();
			calcInitialSpeedAndTimeOnGround();
			getCoorOfRamp();
			getCoorOfSquare();
			convertToCompCoor();
			
			repaint(); //to draw initial square and ramp
			gCPan.aGPanel.expPan.expCenPan.getExpPg2(); //calls method 
				//that will write the explanation for page two in 
				//ExpCenPan
		}
		
		//resets variables for each round of the game, called by 
		//getInitialData
		public void resetVariables()
		{
			markPlaced = showMark = simOver = simRunning = false;
			time = speed = timeSinceOnGround = 0;
			gCPan.instPanel.gameIsGoingOn = false;
			workSpaceOpen = false;
		}
		
		//sets visibility of workSpace to true if workSpaceOpen is true
		public void showWorkSpace(boolean clear)
		{
			if(clear)
			{
				workSpace.setVisible(false);
				workSpace.setText("Your workspace: ");
				botPan.openWorkSpace.setText("Open Work Space");
			}
			else if(workSpaceOpen)
				workSpace.setVisible(true);
			else if(!workSpaceOpen)
			{
				workSpace.setVisible(false);
			}
		}
		
		//generates random values for slope and friction coefficient in
		//the user's given ranges. Also sets mass of the block to the 
		//mass from the settings
		public void getRandomValues()
		{
			slope = Math.random()*(gCPan.settPanel.seInPan.slopeMax -
				gCPan.settPanel.seInPan.slopeMin)+gCPan.settPanel.
				seInPan.slopeMin;
			frictCoef = Math.random()*(gCPan.settPanel.seInPan.
				friCoefMax - gCPan.settPanel.seInPan.friCoefMin) + 
				gCPan.settPanel.seInPan.friCoefMin;
			massOfBlock = gCPan.settPanel.seInPan.mass;
		}
				
		//calculates cosAngle and sinAngle (angle is right-most angle
		//of ramp)
		public void getSinCosAngle()
		{
			cosAngle = 1/Math.sqrt(slope*slope+1);
			sinAngle = Math.abs(slope)*cosAngle;
		}
		
		//calculates heightRamp and widRamp
		public void getHeightWidthOfRamp()
		{
			heightRamp = LENGTHOFSLOPE*sinAngle;
			widRamp = LENGTHOFSLOPE*cosAngle;
		}
		
		//gets coordinates of ramp and stores into rampX[] and rampY[]
		public void getCoorOfRamp()
		{
			rampX[0] = 0; //rampX[0] and rampY[0] stores top left coor
				//of ramp (in computer coordinates)
			rampY[0] = (int)(FRAMEHEIGHT-heightRamp);
			rampX[1] = 0; //rampX[1] and rampY[1] stores bottom left 
				//coord of ramp (in computer coordinates)
			rampY[1] = FRAMEHEIGHT;
			rampX[2] = (int)widRamp; //rampX[2] and rampY[2] stores 
				//bottom right coord of ramp (in computer coordinates)
			rampY[2] = FRAMEHEIGHT;
		}
		
		//calculates forces acting on block when it is on the ramp 
		//and then uses the forces to get the acce of the block on the
		//ramp
		public void calcForcesAndAcceOnRamp()
		{
			perForce = cosAngle*massOfBlock*GRAVFORCE;
			parForce = sinAngle*massOfBlock*GRAVFORCE;
			NForce = perForce; //NForce is force opposite of the per
				//GForce
			friction = NForce*frictCoef;
			acce_Ramp = (parForce-friction)/massOfBlock;
		}
		
		//calculates timeOnRamp
		public void calcTimeWhenBlockReachesGround()
		{
			timeOnRamp = Math.sqrt(2*LENGTHOFSLOPE/acce_Ramp);
		}
		
		//calculates initial speed and time on ground for animating
		//square on ground
		public void calcInitialSpeedAndTimeOnGround()
		{
			initialSpeedOnGround = acce_Ramp*timeOnRamp;
			timeOnGround = -initialSpeedOnGround/acce_Ground;
		}
		
		//calculates forces acting on block when it is on the ground 
		//and then uses the forces to get the acce of the block on the
		//ground
		public void calcForcesAndAcceOnGround()
		{
			//when on ground, parForce is 0
			perForce = massOfBlock*GRAVFORCE;
			NForce = perForce;
			friction = NForce*frictCoef;
			acce_Ground = -friction/massOfBlock;
		}
		
		//Starts the time and sets the delay of the timer
		public void startSimulation()
		{
			timer.start();
			simRunning = true;
			timer.setDelay(25);
		}
		
		//ActionListener object for timer
		class Mover implements ActionListener
		{
			public Mover()
			{}
			
			//calls methods for animation
			public void actionPerformed(ActionEvent evt)
			{
				if(time<timeOnRamp) //square is on ramp
				{
					moveMidPointOnRamp();
					getCoorOfSquare();
					convertToCompCoor();
					getSpeedOnRamp();
				}
				else if(timeSinceOnGround<timeOnGround) //square is
					//on ground but not static
				{
					moveMidPointOnGround();
					getCoorOfSquare();
					convertToCompCoor();
					getSpeedOnGround();
				}
				else //square has stopped
				{
					timer.stop();
					simOver = true;
					speed = 0;
					time = timeOnRamp+timeOnGround;
					//reseting variables in InstruPanel
					gCPan.instPanel.gameIsGoingOn = false;
					gCPan.instPanel.instMainPan.basicPanShown = true;
					gCPan.instPanel.instMainPan.hTWPanShown = 
						gCPan.instPanel.instMainPan.imgPanShown = 
						gCPan.instPanel.instMainPan.phyPanShown = 
						gCPan.instPanel.instMainPan.settInstPanShown 
						= false;
					gCPan.instPanel.instMainPan.showInstruCards();
					gCPan.instPanel.instMenuPan.changeBackButton();
					
					getScore();
				}
				repaint(); //draw next frame
				dataPan.repaint(); //change data like time and speed
				time+=0.025;
			}
		}
		
		//finds the distance from the top-left corner of the ramp
		//that the midpoint of the bottom side of the square has moved
		//from, given the current time that the simulation has ran
		public void moveMidPointOnRamp()
		{
			double distanceMoved_ramp, xDistMoved_ramp, yDistMoved_ramp;
			distanceMoved_ramp = xDistMoved_ramp = yDistMoved_ramp 
				= 0.0;
			distanceMoved_ramp = 0.5*acce_Ramp*time*time;
			xDistMoved_ramp = cosAngle*distanceMoved_ramp;
			yDistMoved_ramp = sinAngle*distanceMoved_ramp;
			midX = xDistMoved_ramp;
			midY = heightRamp-yDistMoved_ramp;
		}
		
		//used to move mid point of square on ground for each frame
		//of animation
		public void moveMidPointOnGround()
		{
			double distanceMoved_ground = 0.0;
			timeSinceOnGround = time - timeOnRamp;
			distanceMoved_ground = initialSpeedOnGround*
				timeSinceOnGround+0.5*acce_Ground*timeSinceOnGround*
				timeSinceOnGround;
			midX = widRamp+distanceMoved_ground;
			midY = 0;
			cosAngle = 1; //because angle of slope is 0 (flat ground)
			sinAngle = 0; //because angle of slope is 0
		}
		
		//uses midX and midY to find other coordinates of the square
		public void getCoorOfSquare()
		{
			nonCompSqrX[0]=(int)(midX+(double)LENGTHOFBLOCK/2*cosAngle);
			nonCompSqrY[0]=(int)(midY-(double)LENGTHOFBLOCK/2*sinAngle);
			nonCompSqrX[1]=(int)(midX-(double)LENGTHOFBLOCK/2*cosAngle);
			nonCompSqrY[1]=(int)(midY+(double)LENGTHOFBLOCK/2*sinAngle);
			nonCompSqrX[2]=(int)(nonCompSqrX[1]+LENGTHOFBLOCK*sinAngle);
			nonCompSqrY[2]=(int)(nonCompSqrY[1]+LENGTHOFBLOCK*cosAngle);
			nonCompSqrX[3]=(int)(nonCompSqrX[0]+LENGTHOFBLOCK*sinAngle);
			nonCompSqrY[3]=(int)(nonCompSqrY[0]+LENGTHOFBLOCK*cosAngle);
		}
		
		//converts the y-coor of the square into computer y-coor, where
		//the top left corner of the panel is (0,0)
		public void convertToCompCoor()
		{
			for(int i = 0; i<nonCompSqrY.length; i++)
				sqrY[i] = FRAMEHEIGHT-nonCompSqrY[i];
			for(int j = 0; j<nonCompSqrX.length; j++)
				sqrX[j] = nonCompSqrX[j];
		}
		
		//gets speed of square on ramp
		public void getSpeedOnRamp()
		{
			speed = acce_Ramp*time;
		}
		
		//gets speed of square on flat ground
		public void getSpeedOnGround()
		{
			speed = initialSpeedOnGround+acce_Ground*timeSinceOnGround;
		}
		
		//calculates score user gets based on distance between their
		//mark and actual distance traveled
		public void getScore()
		{
			double distTraveledOnGround = midX-widRamp;
			score = (int)Math.round((1 - Math.abs(distTraveledOnGround -
				userSetDist)/distTraveledOnGround)*100);
			if(score <= 0)
				score = 0;
			gCPan.settPanel.seInPan.addToScore(score);
			showAfterGamePanel();
		}
		
		//sets win variable in AfterGamePanel to true is score is 
		//greater than 90 (90%), or to false if it is less. Then
		//it calls showCard() in GameCardPanel to show AfterGamePanel
		public void showAfterGamePanel()
		{
			if(score>=90)
				gCPan.aGPanel.win = true;
			else
				gCPan.aGPanel.win = false;
			gCPan.aGShown = true;
			gCPan.startShown = gCPan.gameShown = gCPan.settShown 
				= gCPan.instruShown = false;
			gCPan.aGPanel.expPan.expCenPan.getExpPg3();
			gCPan.aGPanel.expPan.expCenPan.getExpPg4();
			gCPan.showCard();
		}
		
		public void mousePressed(MouseEvent evt)
		{}
		public void mouseReleased(MouseEvent evt)
		{}
		//used to detect if user clicks in region to set mark, and 
		//changes variables and calls methods to show their mark
		public void mouseClicked(MouseEvent evt)
		{
			//if click is inside region to set the mark
			//and simulation is not running
			if(showMark&&!simRunning)
			{
				markXCoor = mouseXCoor;
				userSetDist = (int)Math.round(mouseXCoor-widRamp);
				markPlaced = true;
				repaint();
			}
		}
		public void mouseEntered(MouseEvent evt)
		{}
		public void mouseExited(MouseEvent evt)
		{}
		public void mouseDragged(MouseEvent evt)
		{}
		//used to detect if mouse has moved into region to set the mark
		public void mouseMoved(MouseEvent evt)
		{
			mouseXCoor = evt.getX();
			int mouseYCoor = evt.getY();
			//if user moves mouse inside of region to set mark
			if(mouseXCoor>=rampX[2]&&mouseXCoor<=FRAMEWID)
			{
				if(mouseYCoor>=423&&mouseYCoor<=FRAMEHEIGHT)
				{
					showMark = true;
				}
				else
					showMark = false;
			}
			else
				showMark = false;
			repaint();
		}
	}
	
	class BottomPanel extends JPanel implements ActionListener
	{
		private JButton startSimButton; //starts the simulation in
			//RampPanel when clicked
		private JButton openWorkSpace; //button that opens JTextArea 
		//in RampPanel when pressed

		public BottomPanel()
		{
			setBackground(Color.PINK);
			setLayout(null);
			
			startSimButton = new JButton("Start Simulation");
			startSimButton.setBounds(800,5,200,50);
			startSimButton.setFont(new Font("Tahoma", Font.PLAIN,
				20));
			startSimButton.addActionListener(this);
			add(startSimButton);
			
			openWorkSpace = new JButton("Open Work Space");
			openWorkSpace.setBounds(200,5,150,50);
			openWorkSpace.setFont(new Font("Tahoma", Font.PLAIN,
				14));
			openWorkSpace.addActionListener(this);
			add(openWorkSpace);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}
		
		//if startSimButton is clicked, class startSimulation() in
		//RampPanel to start simulation
		public void actionPerformed(ActionEvent evt)
		{
			String botButtonComm = evt.getActionCommand();
			if(botButtonComm.equals("Start Simulation")&&
				rampPan.markPlaced)
			{
				rampPan.startSimulation();
			}
			else if(botButtonComm.equals("Open Work Space"))
			{
				rampPan.workSpaceOpen = true;
				openWorkSpace.setText("Close Work Space");
				rampPan.showWorkSpace(false);
			}
			else if(botButtonComm.equals("Close Work Space"))
			{
				rampPan.workSpaceOpen = false;
				openWorkSpace.setText("Open Work Space");
				rampPan.showWorkSpace(false);
			}
		}
	}
}
