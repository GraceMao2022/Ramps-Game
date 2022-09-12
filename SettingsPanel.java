/* Grace Mao
 * 5/23/19
 * SettingsPanel.java
 * 
 * 	This class creates a JPanel that contains many components, all
 * 	of which control the settings of the game. For example, there
 * 	is a JTextField in which the user can change their username, or 
 * 	JRadioButtons in which the user can change the color of the ramp
 * 	or block. The settings are written to and read from the text file
 * 	Settings.txt, so that if the user runs the program again, the
 * 	settings will be saved.

*/
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JSlider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//shows the settings
class SettingsPanel extends JPanel
{
	public SettInfoPanel seInPan; //largest panel in SettingsPanel,
		//has components in which the user can change the settings
	private GameCardPanel gCPan; //instance of GameCardPanel used
		//to change between the different panels and change variables
		//in different files
	
	public SettingsPanel(GameCardPanel gCP)
	{
		gCPan = gCP;
		setBackground(Color.CYAN);
		setLayout(new BorderLayout(5,5));
		
		JLabel settLabel = new JLabel("Settings", JLabel.CENTER);
		settLabel.setPreferredSize(new Dimension(100,50));
		Font settLabFont = new Font("Arial", Font.BOLD, 30);
		settLabel.setFont(settLabFont);
		add(settLabel, BorderLayout.NORTH);
		
		seInPan = new SettInfoPanel();
		add(seInPan, BorderLayout.CENTER);
		
		SettBotPanel seBoPan = new SettBotPanel();
		seBoPan.setPreferredSize(new Dimension(100,60));
		add(seBoPan, BorderLayout.SOUTH);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	class SettInfoPanel extends JPanel
	{	
		//the following variables are accessed by GamePanel
		public String username; //stores username that user sets
		public Color rampColor, blockColor; //store colors of ramp 
			//and block
		public double slopeMin, slopeMax, friCoefMin, friCoefMax; 
			//ranges that the slope and friction coefficient can be 
		public int mass; //stores the mass of the block from the 
			//settings
		public int totalScore; //stores the total score that the 
			//user has, including from previous games
			
		private int numOfRColorUlck, numOfBColorUlck; //stores
			//the number of ramp colors and the number of block
			//colors unlocked from previous games
		private String rampColorName, blockColorName; //names of 
			//color of ramp and block
			
		private JTextField typeUsername; //used to get username 
			//that user sets
		private JButton slopeMinUp, slopeMinDown, slopeMaxUp,
			slopeMaxDown; //buttons to change slopeMin and 
			//slopeMax
		private JButton friCoefMinUp, friCoefMinDown, 
			friCoefMaxUp, friCoefMaxDown; //buttons used to 
			//change friCoefMin, friCoefMax
		private JSlider massChanger;
		private JRadioButton rampRed, rampBlue, rampGreen, 
			rampCyan, rampPink, rampBlack, rampYellow, rampOrange, 
			rampMagenta, rampGray; //used to change color of ramp
		private JRadioButton blockRed, blockBlue, blockGreen, 
			blockCyan, blockPink, blockBlack, blockYellow, 
			blockOrange, blockMagenta, blockGray; //used to change 
			//color of block
		private JButton unlockRampColor, unlockBlockColor; //used
			//to unlock new colors for the ramp and block
		
		private PrintWriter output; //used to print text to file
		private Scanner input; //used to read text from file
		private String fileName; //stores name of file read and 
			//written to
		private File settFile; //file that is read and written to

		public SettInfoPanel()
		{
			setBackground(Color.WHITE);
			setLayout(null);
			
			numOfRColorUlck = 1;
			numOfBColorUlck = 1;
			
			typeUsername = new JTextField("Type in your username");
			typeUsername.setBounds(175,30,850,35);
			typeUsername.addActionListener(new SettTextFieldHandler());
			add(typeUsername);
			
			massChanger = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
			massChanger.setBounds(200,85,825,35);
			massChanger.addChangeListener(new SettSliderHandler());
			add(massChanger);
			
			slopeMinUp = new JButton(">");
			slopeMinUp.setBounds(400,140,50,50);
			slopeMinDown = new JButton("<");
			slopeMinDown.setBounds(300,140,50,50);
			slopeMaxUp = new JButton(">");
			slopeMaxUp.setBounds(700,140,50,50);
			slopeMaxDown = new JButton("<");
			slopeMaxDown.setBounds(600,140,50,50);
			friCoefMinUp = new JButton(">");
			friCoefMinUp.setBounds(400,215,50,50);
			friCoefMinDown = new JButton("<");
			friCoefMinDown.setBounds(300,215,50,50);
			friCoefMaxUp = new JButton(">");
			friCoefMaxUp.setBounds(700,215,50,50);
			friCoefMaxDown = new JButton("<");
			friCoefMaxDown.setBounds(600,215,50,50);
			
			slopeMinUp.addActionListener(new SettButtonHandler());
			slopeMinDown.addActionListener(new SettButtonHandler());
			slopeMaxUp.addActionListener(new SettButtonHandler());
			slopeMaxDown.addActionListener(new SettButtonHandler());
			friCoefMinUp.addActionListener(new SettButtonHandler());
			friCoefMinDown.addActionListener(new SettButtonHandler());
			friCoefMaxUp.addActionListener(new SettButtonHandler());
			friCoefMaxDown.addActionListener(new SettButtonHandler());
			
			add(slopeMinUp);
			add(slopeMinDown);
			add(slopeMaxUp);
			add(slopeMaxDown);
			add(friCoefMinUp);
			add(friCoefMinDown);
			add(friCoefMaxUp);
			add(friCoefMaxDown);
			
			ButtonGroup rampColorGroup = new ButtonGroup();
			ButtonGroup blockColorGroup = new ButtonGroup();
			
			rampRed = new JRadioButton("Red");
			rampRed.setBounds(300,290,100,25);
			rampBlue = new JRadioButton("Blue");
			rampBlue.setBounds(300,315,100,25);
			rampGreen = new JRadioButton("Green");
			rampGreen.setBounds(300,340,100,25);
			rampCyan = new JRadioButton("Cyan");
			rampCyan.setBounds(300,365,100,25);
			rampPink = new JRadioButton("Pink");
			rampPink.setBounds(300,390,100,25);
			rampBlack = new JRadioButton("Black");
			rampBlack.setBounds(300,415,100,25);
			rampYellow = new JRadioButton("Yellow");
			rampYellow.setBounds(300,440,100,25);
			rampOrange = new JRadioButton("Orange");
			rampOrange.setBounds(300,465,100,25);
			rampMagenta = new JRadioButton("Magenta");
			rampMagenta.setBounds(300,490,100,25);
			rampGray = new JRadioButton("Gray");
			rampGray.setBounds(300,515,100,25);
			
			rampRed.addActionListener(new RadioButtonHandler());
			rampBlue.addActionListener(new RadioButtonHandler());
			rampGreen.addActionListener(new RadioButtonHandler());
			rampCyan.addActionListener(new RadioButtonHandler());
			rampPink.addActionListener(new RadioButtonHandler());
			rampBlack.addActionListener(new RadioButtonHandler());
			rampYellow.addActionListener(new RadioButtonHandler());
			rampOrange.addActionListener(new RadioButtonHandler());
			rampMagenta.addActionListener(new RadioButtonHandler());
			rampGray.addActionListener(new RadioButtonHandler());
			
			rampColorGroup.add(rampRed);
			rampColorGroup.add(rampBlue);
			rampColorGroup.add(rampGreen);
			rampColorGroup.add(rampCyan);
			rampColorGroup.add(rampPink);
			rampColorGroup.add(rampBlack);
			rampColorGroup.add(rampYellow);
			rampColorGroup.add(rampOrange);
			rampColorGroup.add(rampMagenta);
			rampColorGroup.add(rampGray);
			
			add(rampRed);
			add(rampBlue);
			add(rampGreen);
			add(rampCyan);
			add(rampPink);
			add(rampBlack);
			add(rampYellow);
			add(rampOrange);
			add(rampMagenta);
			add(rampGray);
			
			rampBlue.setVisible(false);
			rampGreen.setVisible(false);
			rampCyan.setVisible(false);
			rampPink.setVisible(false);
			rampBlack.setVisible(false);
			rampYellow.setVisible(false);
			rampOrange.setVisible(false);
			rampMagenta.setVisible(false);
			rampGray.setVisible(false);
			
			unlockRampColor = new JButton("Unlock");
			unlockRampColor.setBounds(300,315,100,25);
			unlockRampColor.addActionListener(new 
				SettButtonHandler());
			add(unlockRampColor);
			
			blockBlue = new JRadioButton("Blue");
			blockBlue.setBounds(750,290,100,25);
			blockRed = new JRadioButton("Red");
			blockRed.setBounds(750,315,100,25);
			blockGreen = new JRadioButton("Green");
			blockGreen.setBounds(750,340,100,25);
			blockCyan = new JRadioButton("Cyan");
			blockCyan.setBounds(750,365,100,25);
			blockPink = new JRadioButton("Pink");
			blockPink.setBounds(750,390,100,25);
			blockBlack = new JRadioButton("Black");
			blockBlack.setBounds(750,415,100,25);
			blockYellow = new JRadioButton("Yellow");
			blockYellow.setBounds(750,440,100,25);
			blockOrange = new JRadioButton("Orange");
			blockOrange.setBounds(750,465,100,25);
			blockMagenta = new JRadioButton("Magenta");
			blockMagenta.setBounds(750,490,100,25);
			blockGray = new JRadioButton("Gray");
			blockGray.setBounds(750,515,100,25);
			
			blockRed.addActionListener(new RadioButtonHandler());
			blockBlue.addActionListener(new RadioButtonHandler());
			blockGreen.addActionListener(new RadioButtonHandler());
			blockCyan.addActionListener(new RadioButtonHandler());
			blockPink.addActionListener(new RadioButtonHandler());
			blockBlack.addActionListener(new RadioButtonHandler());
			blockYellow.addActionListener(new RadioButtonHandler());
			blockOrange.addActionListener(new RadioButtonHandler());
			blockMagenta.addActionListener(new RadioButtonHandler());
			blockGray.addActionListener(new RadioButtonHandler());
			
			blockColorGroup.add(blockRed);
			blockColorGroup.add(blockBlue);
			blockColorGroup.add(blockGreen);
			blockColorGroup.add(blockCyan);
			blockColorGroup.add(blockPink);
			blockColorGroup.add(blockBlack);
			blockColorGroup.add(blockYellow);
			blockColorGroup.add(blockOrange);
			blockColorGroup.add(blockMagenta);
			blockColorGroup.add(blockGray);
			
			add(blockRed);
			add(blockBlue);
			add(blockGreen);
			add(blockCyan);
			add(blockPink);
			add(blockBlack);
			add(blockYellow);
			add(blockOrange);
			add(blockMagenta);
			add(blockGray);
			
			blockRed.setVisible(false);
			blockGreen.setVisible(false);
			blockCyan.setVisible(false);
			blockPink.setVisible(false);
			blockBlack.setVisible(false);
			blockYellow.setVisible(false);
			blockOrange.setVisible(false);
			blockMagenta.setVisible(false);
			blockGray.setVisible(false);
			
			unlockBlockColor = new JButton("Unlock");
			unlockBlockColor.setBounds(750,315,100,25);
			unlockBlockColor.addActionListener(new 
				SettButtonHandler());
			add(unlockBlockColor);
			
			output = null;
			input = null;
			fileName = new String("Settings.txt");
			settFile = new File(fileName);
		
			username = new String("");
			blockColorName = new String("Red");
			rampColorName = new String("Blue");
		
			openFile();
		}
		
		//draws labels for the settings
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			Font settFont = new Font("Arial", Font.PLAIN, 15);
			g.setFont(settFont);
			g.drawString("Username:", 80, 50);
			g.drawString("Mass of block:", 80, 110);
			g.drawString(mass+" kg", (int)(mass*8.35) + 175, 80);
			g.drawString("Range of Slope:", 80, 175);
			g.drawString("" + roundNum(slopeMin, 1), 360, 175);
			g.drawString("to", 540, 175);
			g.drawString("" + roundNum(slopeMax, 1), 660, 175);
			g.drawString("Range of Friction Coefficient:", 80, 
					250);
			g.drawString("" + roundNum(friCoefMin, 2), 360, 250);
			g.drawString("to", 540, 250);
			g.drawString("" + roundNum(friCoefMax, 2), 660, 250);
			g.drawString("Color of Ramp", 300, 285);
			g.drawString("Color of Block", 750, 285);
			g.setFont(new Font("Tahoma", Font.PLAIN, 25));
			g.drawString("Total points: ", 450, 300);
			g.setColor(Color.RED);
			g.drawString("" + totalScore, 600, 300);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.PLAIN, 20));
			g.drawString("You need 1000 points to unlock ", 425,325);
			g.drawString("a new color.", 425, 345);
		}
		
		//this is used to round a double to a set number of decimal
		//places using parameters
		public double roundNum(double numIn, int numDecimal)
		{
			int temp = 0;
			numIn = numIn + 0.000001;
			int multFactor = 0;
			if(numDecimal == 1)
				multFactor = 10;
			else
				multFactor = 100;
			temp = (int)Math.round(numIn*multFactor);
			numIn = (double)temp/multFactor;
			return numIn;
		}
		
		//uses try-catch block to open file
		public void openFile()
		{
			try
			{
				input = new Scanner(settFile);
			}
			catch(FileNotFoundException e)
			{
				System.err.printf("ERROR: Cannot find/open file %s."+
					"\n\n\n", fileName);
				System.exit(1);
			}
			readFile();
			input.close();
			evaluateSettData();
		}
		
		//reads in text in "Settings.txt" and stores values 
		//to corresponding variables
		public void readFile()
		{
			String line = new String("");
			while(input.hasNext())
			{
				line = input.nextLine();
				if(line.indexOf("Username")!=-1) //used to check
					//if line of file contains the username
					
					//save text after "Username:_" into the 
					//field variable username
					username = line.substring(line.indexOf("_")+1);
				else if(line.indexOf("Mass") != -1) //similar to
					//above
					mass = Integer.parseInt
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Slope Max")!=-1)
					slopeMax = Double.parseDouble
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Slope Min")!=-1)
					slopeMin = Double.parseDouble
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Frict Coef Max")!=-1)
					friCoefMax = Double.parseDouble
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Frict Coef Min")!=-1)
					friCoefMin = Double.parseDouble
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Ramp Color")!=-1)
					rampColorName = line.substring(line.indexOf("_")+1);
				else if(line.indexOf("Block Color")!=-1)
					blockColorName = line.substring
						(line.indexOf("_")+1);
				else if(line.indexOf("Total Score")!=-1)
					totalScore = Integer.parseInt
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Num of Rp Color")!=-1)
					numOfRColorUlck = Integer.parseInt
						(line.substring(line.indexOf("_")+1));
				else if(line.indexOf("Num of Blck Color")!=-1)
					numOfBColorUlck = Integer.parseInt
						(line.substring(line.indexOf("_")+1));
			}
		}
		
		//gets name of colors of ramp and block and sets rampColor
		//and blockColor to corresponding color. Sets text of
		//typeUsername to username and the value of mass to 
		//the JSlider massChanger
		public void evaluateSettData()
		{
			typeUsername.setText(username); //sets text of 
				//JTextField to username so that user can
				//see the current username
			massChanger.setValue(mass); //sets the value
				//of the JSlider to mass so that user
				//can see the current mass of the block
			
			//sets the JRadioButton ramp colors to visible
			//in correspondence to the value of the variable
			//numOfRColorUlck, which stores the number of 
			//ramp colors that have already been unlocked from
			//previous rounds
			if(numOfRColorUlck >= 2) //if the number of
				//ramp colors unlocked is greater than or
				//equal to 2, then the blue color for
				//ramp is unlocked
			{
				rampBlue.setVisible(true);
				unlockRampColor.setBounds(300, 340, 
					100, 25); //move "Unlock" button down
			}
			if(numOfRColorUlck >= 3) //similar to above
			{
				rampGreen.setVisible(true);
				unlockRampColor.setBounds(300, 365, 
					100, 25);
			}
			if(numOfRColorUlck >= 4)
			{
				rampCyan.setVisible(true);
				unlockRampColor.setBounds(300, 390, 
					100, 25);
			}
			if(numOfRColorUlck >= 5)
			{
				rampPink.setVisible(true);
				unlockRampColor.setBounds(300, 415, 
					100, 25);
			}
			if(numOfRColorUlck >= 6)
			{
				rampBlack.setVisible(true);
				unlockRampColor.setBounds(300, 440, 
					100, 25);
			}
			if(numOfRColorUlck >= 7)
			{
				rampYellow.setVisible(true);
				unlockRampColor.setBounds(300, 465,
					100, 25);
			}
			if(numOfRColorUlck >= 8)
			{
				rampOrange.setVisible(true);
				unlockRampColor.setBounds(300, 490,
					100, 25);
			}
			if(numOfRColorUlck >= 9)
			{
				rampMagenta.setVisible(true);
				unlockRampColor.setBounds(300, 515, 
					100, 25);
			}
			if(numOfRColorUlck == 10)
			{
				rampGray.setVisible(true);
				
				//set "Unlock" button to invisible
				//because all 10 colors are unlocked
				unlockRampColor.setVisible(false);
			}
			
			//same as above, just for block colors instead
			//of ramp colors
			if(numOfBColorUlck >= 2)
			{
				blockRed.setVisible(true);
				unlockBlockColor.setBounds(750, 340, 
					100, 25);
			}
			if(numOfBColorUlck >= 3)
			{
				blockGreen.setVisible(true);
				unlockBlockColor.setBounds(750, 365, 
					100, 25);
			}
			if(numOfBColorUlck >= 4)
			{
				blockCyan.setVisible(true);
				unlockBlockColor.setBounds(750, 390,
					100, 25);
			}
			if(numOfBColorUlck >= 5)
			{
				blockPink.setVisible(true);
				unlockBlockColor.setBounds(750, 415,
					100, 25);
			}
			if(numOfBColorUlck >= 6)
			{
				blockBlack.setVisible(true);
				unlockBlockColor.setBounds(750, 440,
					100, 25);
			}
			if(numOfBColorUlck >= 7)
			{
				blockYellow.setVisible(true);
				unlockBlockColor.setBounds(750, 465,
					100, 25);
			}
			if(numOfBColorUlck >= 8)
			{
				blockOrange.setVisible(true);
				unlockBlockColor.setBounds(750, 490, 
					100, 25);
			}
			if(numOfBColorUlck >= 9)
			{
				blockMagenta.setVisible(true);
				unlockBlockColor.setBounds(750, 515, 
					100, 25);
			}
			if(numOfBColorUlck == 10)
			{
				blockGray.setVisible(true);
				unlockBlockColor.setVisible(false);
			}
			
			//sets ramp color in correspondence to the ramp
			//color name stored in Settings.txt
			if(rampColorName.equals("Red"))
			{
				rampColor = Color.RED;
				rampRed.setSelected(true);
			}
			else if(rampColorName.equals("Blue"))
			{
				rampColor = Color.BLUE;
				rampBlue.setSelected(true);
			}
			else if(rampColorName.equals("Green"))
			{
				rampColor = Color.GREEN;
				rampGreen.setSelected(true);
			}
			else if(rampColorName.equals("Cyan"))
			{
				rampColor = Color.CYAN;
				rampCyan.setSelected(true);
			}
			else if(rampColorName.equals("Pink"))
			{
				rampColor = Color.PINK;
				rampPink.setSelected(true);
			}
			else if(rampColorName.equals("Black"))
			{
				rampColor = Color.BLACK;
				rampBlack.setSelected(true);
			}
			else if(rampColorName.equals("Yellow"))
			{
				rampColor = Color.YELLOW;
				rampYellow.setSelected(true);
			}
			else if(rampColorName.equals("Orange"))
			{
				rampColor = Color.ORANGE;
				rampOrange.setSelected(true);
			}
			else if(rampColorName.equals("Magenta"))
			{
				rampColor = Color.MAGENTA;
				rampMagenta.setSelected(true);
			}
			else if(rampColorName.equals("Gray"))
			{
				rampColor = Color.GRAY;
				rampGray.setSelected(true);
			}
			
			//sets block color in correspondence to block
			//color name stored in Settings.txt
			if(blockColorName.equals("Red"))
			{
				blockColor = Color.RED;
				blockRed.setSelected(true);
			}
			else if(blockColorName.equals("Blue"))
			{
				blockColor = Color.BLUE;
				blockBlue.setSelected(true);
			}
			else if(blockColorName.equals("Green"))
			{
				blockColor = Color.GREEN;
				blockGreen.setSelected(true);
			}
			else if(blockColorName.equals("Cyan"))
			{
				blockColor = Color.CYAN;
				blockCyan.setSelected(true);
			}
			else if(blockColorName.equals("Pink"))
			{
				blockColor = Color.PINK;
				blockPink.setSelected(true);
			}
			else if(blockColorName.equals("Black"))
			{
				blockColor = Color.BLACK;
				blockBlack.setSelected(true);
			}
			else if(blockColorName.equals("Yellow"))
			{
				blockColor = Color.YELLOW;
				blockYellow.setSelected(true);
			}
			else if(blockColorName.equals("Orange"))
			{
				blockColor = Color.ORANGE;
				blockOrange.setSelected(true);
			}
			else if(blockColorName.equals("Magenta"))
			{
				blockColor = Color.MAGENTA;
				blockMagenta.setSelected(true);
			}
			else if(blockColorName.equals("Gray"))
			{
				blockColor = Color.GRAY;
				blockGray.setSelected(true);
			}
		}
		
		//prints settings into "Settings.txt" to save the settings.
		//This method is called every time a component in 
		//SettingsInfoPanel is interacted with
		public void saveToFile()
		{
			output.println("Username:_" + username);
			output.println("Mass:_" + mass);
			output.println("Slope Max:_" + slopeMax);
			output.println("Slope Min:_" + slopeMin);
			output.println("Frict Coef Max:_" + friCoefMax);
			output.println("Frict Coef Min:_" + friCoefMin);
			output.println("Ramp Color:_" + rampColorName);
			output.println("Block Color:_" + blockColorName);
			output.println("Total Score:_" + totalScore);
			output.println("Num of Rp Color:_" + numOfRColorUlck);
			output.println("Num of Blck Color:_" + numOfBColorUlck);
		}
		
		//checks to see if the user's total scores is over 1000
		//so that a new color can be unlocked. Receives parameter
		//object to determine if the user wants to unlock a ramp
		//or block color
		public void unlockColor(String object)
		{
			if(totalScore >= 1000)
			{
				totalScore -= 1000;
				if(object.equals("Ramp"))
				{
					numOfRColorUlck += 1;
					evaluateSettData();
				}
				else if(object.equals("Block"))	
				{
					numOfBColorUlck += 1;
					evaluateSettData();
				}
			}
			repaint();
		}
		
		//uses try-catch block to instantiate output
		public void writeFile()
		{
			try
			{
				output = new PrintWriter(settFile);
			}
			catch(IOException e)
			{
				System.err.printf("ERROR: Cannot create file %s."+
					"\n\n\n", fileName);
				System.exit(2);
			}
			saveToFile();
			output.close();
		}
		
		//adds score that user gets from each round to total score
		public void addToScore(int scoreIn)
		{
			totalScore += scoreIn;
			writeFile();
		}
		
		class SettTextFieldHandler implements ActionListener
		{
			public SettTextFieldHandler()
			{}
			
			//detects if user changed username in text field
			public void actionPerformed(ActionEvent evt)
			{
				username = typeUsername.getText();
				evaluateSettData();
				writeFile();
			}
		}
		
		class SettSliderHandler implements ChangeListener
		{
			public SettSliderHandler()
			{}
			
			//sets value of slider to the mass of the block
			public void stateChanged(ChangeEvent evt)
			{
				int sliderValue = massChanger.getValue();
				mass = sliderValue;
				repaint();
				writeFile();
			}
		}
		
		class SettButtonHandler implements ActionListener
		{
			public SettButtonHandler()
			{}
			
			//Detects if the buttons on the panel are pressed to 
			//increase or decrease the minimum or maximum of slope
			//and friction coefficient. Also, this method prevents the
			//user from setting the minimum and maximum for slope and
			//friction coefficient above or below a certain range, and
			//prevents user from setting the minimum greater than the
			//maximum
			public void actionPerformed(ActionEvent evt)
			{
				if(evt.getSource() == slopeMinUp && 
					roundNum(slopeMin,1) + 0.1 < roundNum(slopeMax,1))
					slopeMin += 0.1;
				else if(evt.getSource() == slopeMinDown)
				{
					slopeMin -= 0.1;
					if(slopeMin < -5.6)
						slopeMin = -5.6;
				}
				else if(evt.getSource() == slopeMaxUp)
				{
					slopeMax += 0.1;
					if(slopeMax > -0.5)
						slopeMax = -0.5;
				}
				else if(evt.getSource() == slopeMaxDown && 
					roundNum(slopeMax,1) - 0.1 > roundNum(slopeMin,1))
					slopeMax -= 0.1;
				else if(evt.getSource() == friCoefMinDown)
				{
					friCoefMin -= 0.02;
					if(friCoefMin < 0.18)
						friCoefMin = 0.18;
				}
				else if(evt.getSource() == friCoefMinUp && 
					friCoefMin + 0.02 < friCoefMax)
					friCoefMin += 0.02;
				else if(evt.getSource() == friCoefMaxUp)
				{
					friCoefMax += 0.02;
					if(friCoefMax > 0.4)
						friCoefMax = 0.4;
				}
				else if(evt.getSource() == friCoefMaxDown && 
					friCoefMax - 0.02 > friCoefMin)
					friCoefMax -= 0.02;
				else if(evt.getSource() == unlockRampColor)
					unlockColor("Ramp");
				else if(evt.getSource() == unlockBlockColor)
					unlockColor("Block");
				repaint();
				writeFile();
			}
		}
		
		class RadioButtonHandler implements ActionListener
		{
			public RadioButtonHandler()
			{}
			
			//detects if user has selected a radio button to change
			//the color of the ramp and block
			public void actionPerformed(ActionEvent evt)
			{
				if(rampRed.isSelected())
					rampColorName = "Red";
				else if(rampBlue.isSelected())
					rampColorName = "Blue";
				else if(rampGreen.isSelected())
					rampColorName = "Green";
				else if(rampCyan.isSelected())
					rampColorName = "Cyan";
				else if(rampPink.isSelected())
					rampColorName = "Pink";
				else if(rampBlack.isSelected())
					rampColorName = "Black";
				else if(rampYellow.isSelected())
					rampColorName = "Yellow";
				else if(rampOrange.isSelected())
					rampColorName = "Orange";
				else if(rampMagenta.isSelected())
					rampColorName = "Magenta";
				else if(rampGray.isSelected())
					rampColorName = "Gray";
					
				if(blockRed.isSelected())
					blockColorName = "Red";
				else if(blockBlue.isSelected())
					blockColorName = "Blue";
				else if(blockGreen.isSelected())
					blockColorName = "Green";
				else if(blockCyan.isSelected())
					blockColorName = "Cyan";
				else if(blockPink.isSelected())
					blockColorName = "Pink";
				else if(blockBlack.isSelected())
					blockColorName = "Black";
				else if(blockYellow.isSelected())
					blockColorName = "Yellow";
				else if(blockOrange.isSelected())
					blockColorName = "Orange";
				else if(blockMagenta.isSelected())
					blockColorName = "Magenta";
				else if(blockGray.isSelected())
					blockColorName = "Gray";
					
				evaluateSettData();
				writeFile();
			}
		}
		
	}
	
	//has a button that makes screen go back to the start page
	//when clicked
	class SettBotPanel extends JPanel
	{
		private JButton goBack2; //button to let user go back to 
			//StartPanel when pressed
			
		public SettBotPanel()
		{
			setBackground(Color.CYAN);
			setLayout(null);
			goBack2 = new JButton("Go Back To Main Screen");
			goBack2.addActionListener(new BackButtonHandler());
			goBack2.setBounds(50, 10, 500, 30);
			add(goBack2);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}
		
		class BackButtonHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				String command = evt.getActionCommand();
				//if button is clicked, screen goes to start page
				if(command.equals("Go Back To Main Screen"))
				{
					gCPan.startShown = true;
					gCPan.instruShown = gCPan.gameShown 
						= gCPan.settShown = false;
				}
				gCPan.showCard();
			}
		}
	}
}
