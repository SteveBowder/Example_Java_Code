//*MAX***************************************************************************************
// Steve Bowder
// 5/5/2013
// Java A7

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
   The Student Calculator uses a GUI to allow
   the user to input names and grades, then
   view different statistics regarding the data.
*/

public class StudentCalculator extends JFrame
{
   private JPanel infoPanel;     		// Student information panel
   private JPanel statsPanel; 			// Statistic options panel
   private JPanel buttonPanel;    		// Hold the buttons

   //For the infoPanel
   private JTextField studentName;		//Student name textfield
   private JTextField studentGrade;		//Student grade textfield
   private JButton addButton;				//Add student button

   //For the statsPanel
   private JRadioButton avg;  			// To select average (regular)
   private JRadioButton avgWithDrop;  	// To select average (drops lowest)
   private ButtonGroup statsBg;      	// Radio button group
   private JCheckBox maxGrade;  			// To select display maximum
   private JCheckBox minGrade;       	// To select display minimum
   private JCheckBox letterGrades;   	// To select letter grades
   private JCheckBox listRoster; 		// To select listing the roster

   //For the buttonPanel
	private JButton calcButton;    		// To calculate the statistics
   private JButton exitButton;    		// To exit the application

   private int counter = 0;						//Array counter
   private final int MAX_STUDENTS = 5; // Maximum students in class

   //Create array of Student objects
	private Student[] studentsArray = new Student[MAX_STUDENTS];

   /**
      Constructor
   */

   public StudentCalculator()
   {
      // Display a title.
      setTitle("Steve Bowder");

      // Specify an action for the close button.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create a BorderLayout manager.
      setLayout(new BorderLayout());

      // Create the panels.
      buildInfoPanel();
      buildStatsPanel();
      buildButtonPanel();

      // Add the components to the content pane.
      add(infoPanel, BorderLayout.NORTH);
      add(statsPanel, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);

      // Pack the contents of the window and display it.
      pack();
      setVisible(true);
   }

   /**
      The buildButtonPanel method builds the button panel.
   */

	private void buildInfoPanel()
	{
		// Create a panel for toppings
		infoPanel = new JPanel();

		// Create a GridLayout manager with
		// four rows and one column.
		//infoPanel.setLayout(new GridLayout(1, 3));

		studentName = new JTextField(15);
		studentGrade = new JTextField(5);
		addButton = new JButton("Add Student");

		// Register the action listener.
		addButton.addActionListener(new AddStudentButtonListener());

		// Add a border around the panel.
		infoPanel.setBorder(
			BorderFactory.createTitledBorder("Student Info"));

		// Add the text boxes to the panel.
		infoPanel.add(studentName);
		infoPanel.add(studentGrade);

		//Add the button to the panel
		infoPanel.add(addButton);
	}

   private void buildStatsPanel()
   {
		// Create a panel for student info
		statsPanel = new JPanel();

      // Create a GridLayout manager with
      // two rows and one column.
      statsPanel.setLayout(new GridLayout(6, 1));

      // Create the radio buttons.
      avg = new JRadioButton("Average", true);
      avgWithDrop = new JRadioButton("Average Drops Lowest Test");

      // Group the radio buttons.
      statsBg = new ButtonGroup();
      statsBg.add(avg);
      statsBg.add(avgWithDrop);

		// Create the check boxes.
		maxGrade = new JCheckBox("Maximum");
		minGrade = new JCheckBox("Minimum");
		letterGrades = new JCheckBox("Letter Grades");
		listRoster = new JCheckBox("List Roster");

      // Add a border around the panel.
      statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

      // Add the radio buttons to the panel.
      statsPanel.add(avg);
      statsPanel.add(avgWithDrop);

      //Add the check boxes to the panel
      statsPanel.add(maxGrade);
      statsPanel.add(minGrade);
      statsPanel.add(letterGrades);
      statsPanel.add(listRoster);

	}

	private void buildButtonPanel()
	{
		// Create a panel for the buttons.
		buttonPanel = new JPanel();

		// Create the buttons.
		calcButton = new JButton("Calculate Statistics");
		exitButton = new JButton("Exit");

		//Disable the calcButton
		calcButton.setEnabled(false);

		// Register the action listeners.
		calcButton.addActionListener(new CalcButtonListener());
		exitButton.addActionListener(new ExitButtonListener());

		// Add the buttons to the button panel.
		buttonPanel.add(calcButton);
		buttonPanel.add(exitButton);
   }

   /**
      Private inner class that handles the event when
      the user clicks the Add Student button.
   */
   private class AddStudentButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
			//Variables
			String name;
			int grade;

			name = studentName.getText();
         //If the name is valid
         if (!name.equals("") && !studentGrade.getText().equals(""))
         {
				grade = Integer.parseInt(studentGrade.getText());
				//If the grade is valid
				if (grade >= 0)
				{
					//Create new student in the array
					studentsArray[counter] = new Student(studentName.getText(), grade);

					//Clear fields after submitting
					studentName.setText("");
					studentGrade.setText("");

					//Increase counter
					counter++;

					//If the counter is at max, disable the text fields and add button
					//then enable the calculate stats button
					if (counter == MAX_STUDENTS)
					{
						studentName.setEnabled(false);
						studentGrade.setEnabled(false);
						addButton.setEnabled(false);
						calcButton.setEnabled(true);
					}
				}

				else
				{
					//Display error message, then focus on the field
					JOptionPane.showMessageDialog(null, "Please enter a valid grade!");
					studentGrade.requestFocus();
				}
			}

			else
			{
				if (name.equals(""))
				{
					//Display error message, then focus on the field
					JOptionPane.showMessageDialog(null, "Please enter a name!");
					studentGrade.requestFocus();
				}
				else
				{
					//Display error message, then focus on the field
					JOptionPane.showMessageDialog(null, "Please enter a grade!");
					studentGrade.requestFocus();
				}
			}
      }
   }

   /**
      Private inner class that handles the event when
      the user clicks the Calculate button.
   */
   private class CalcButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         // Variable to hold the information
         String results = "Statistics: \n\n";

         // Create a DecimalFormat object to format output.
         DecimalFormat average = new DecimalFormat("#0.0");

         //Build the results
         results += "Average: " + String.valueOf(average.format(findAverage())) + "\n";

         //If max grade is checked, add it to results
         if (maxGrade.isSelected())
         	results += "MAXIMUM: " + Integer.toString(findMaximum()) + "\n";

         //If min grade is checked, add it to results
         if (minGrade.isSelected())
         	results += "MINIMUM: " + Integer.toString(findMinimum()) + "\n";

         //If letter grades is checked, add it to results
         if (letterGrades.isSelected())
         	results += "LETTERGRADES:" + showLetterGrades() + "\n";

         //If list students is checked, add it to results
         if (listRoster.isSelected())
         	results += "ROSTER:" + listStudents() + "\n";

         // Display the charges.
         JOptionPane.showMessageDialog(null, results);
      }
   }

   /**
      findAverage method
      @return The average of the grades.
   */

   private double findAverage()
   {
		//Variable
		int lowest = studentsArray[0].getGrade();
		double average = lowest;	//The average
		boolean dropLowest; //Drop lowest score

		//Check to see if we want to drop the lowest grade
		if (avgWithDrop.isSelected())
			dropLowest = true;

		else
			dropLowest = false;

		//Loop throw the array
		for (int i = 1; i < counter; i++)
		{
			//Collect lowest grade if we're dropping it
			if (dropLowest && lowest > studentsArray[i].getGrade())
			{
				lowest = studentsArray[i].getGrade();
			}

			//Add to average
			average += studentsArray[i].getGrade();
		}

		//Average accordingly
		if (dropLowest)
			average = (average - lowest) / (counter - 1);
		else
			average = average / counter;

		//Return the average
      return average;
   }

	/**
      findMaximum method
      @return The maximum number.
   */

   private int findMaximum()
   {
		//Variable
		int max = studentsArray[0].getGrade();		//The highest grade

		for (int i = 1; i < counter; i++)
		{
			if (studentsArray[i].getGrade() > max)
			{
				max = studentsArray[i].getGrade();
			}
		}

      return max;
   }

	/**
		findMinimum method
		@return The minimum grade.
	*/

	private int findMinimum()
	{
		//Variable
		int min = studentsArray[0].getGrade();		//The lowest grade

		for (int i = 1; i < counter; i++)
		{
			if (studentsArray[i].getGrade() < min)
			{
				min = studentsArray[i].getGrade();
			}
		}

		return min;
	}


   /**
      showLetterGrades method
      @return The roster and corresponding letter grade.
   */

   private String showLetterGrades()
   {
		//Variables
		String roster = "\n";

		for (int i = 0; i < counter; i++)
		{
			roster += studentsArray[i].getName();
			//Find the letter grades of each score then add to the roster
			if (studentsArray[i].getGrade() < 60)
				roster += ": F\n";
			else if (studentsArray[i].getGrade() > 70)
				roster += ": D\n";
			else if (studentsArray[i].getGrade() < 80)
				roster += ": C\n";
			else if (studentsArray[i].getGrade() < 90)
				roster += ": B\n";
			else
				roster += ": A\n";
		}

      return roster;
	}

	/**
	      listStudents method
	      @return The roster and corresponding numerical grade.
	   */

	   private String listStudents()
	   {
			//Variables
			String roster = "\n";

			//Loop to populate the roster with the names and grades
			for (int i = 0; i < counter; i++)
			{
				roster += studentsArray[i].getName();
				roster += studentsArray[i].getGrade() + "\n";
			}

			//Return roster
	      return roster;
	}

   /**
      Private inner class that handles the event when
      the user clicks the Exit button.
   */
   private class ExitButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
          System.exit(0);
      }
   }

   /**
	   This method creates an instance of the StudentCalculator class
	   which displays the GUI for Professor Plum's application.
	*/

	public static void main(String[] args)
	{
		StudentCalculator gui = new StudentCalculator();
	}

}
