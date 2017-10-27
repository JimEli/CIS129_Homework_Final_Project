/*************************************************************************
 * Title: Play Magic Triangle Puzzle Game 
 * File: CIS129_JamesEli_FP.java
 * Author: James Eli
 * Date: 7/7/2016
 *
 * This program plays the "magic triangle" puzzle game. It includes a 
 * nested MagicTriangle class which constructs, displays and solves the 
 * "magic triangle" puzzle game. The magic triangle is a diagrammatic 
 * representation of a math problem involving a system of 3 linear 
 * equations. The triangle has 3 numbers in squares, and three unknown 
 * numbers in circles. The 3 numbers in the squares (left-side, right-side, 
 * and bottom) mid span between the triangle vertices are the sums of the 
 * numbers in the circles (top, left-bottom, and right-bottom) to which 
 * they are connected. The user enters the numbers for the squares and the 
 * program attempts to calculate the numbers in the circles. Here is an 
 * example of an unsolved magic triangle, and its solution:
 * 
 *           (  )                        ( 6)
 *          /    \                      /    \
 *        [14]  [20]                  [14]  [20]
 *        /        \                  /        \
 *     (  )--[22]--(  )            ( 8)--[22]--(14)
 * 
 * To determine the numbers in the circles, the program must solve 
 * the following system of 3 linear equations. Using Cramer's Rule 
 * and optimizing for the constraints of this problem, yields the 
 * following 3 equations:
 * (1) TopCircle = (BottomSquare - RightSquare - LeftSquare)/-2 
 * (2) LeftBottomCircle = (RigthSquare - LeftSquare - BottomSquare)/-2
 * (3) RightBottomCircle = (LeftSquare - RightSquare - BottomSquare)/-2
 * It is important to note, there may be 0 or more solutions for the 
 * combination of numbers selected for the squares. If solvable, the 
 * program only determines one solution. The input for the squares 
 * is limited between -40 to 40, inclusively.
 *
 * Uses Scanner class, so is not safe for multi-threaded use.
 * 
 * Submitted in partial fulfillment of the requirements of PCC CIS-219.
 *************************************************************************/

// Scanner class used for user input.
import java.util.Scanner;

public class CIS129_JamesEli_FP {
  // Create a class-level Scanner object for all keyboard input.
  private static Scanner scanner = new Scanner( System.in );

  /*********************************************************************
   * Nested MagicTriangle class. This is where all the magic happens.
   *********************************************************************/
  class MagicTriangle {
    // Constants.
    public static final int LEFT=0;        // Represents left square & circle array index(s).
    public static final int RIGHT=1;       // Represents right square & circle array index(s). 
    public static final int BOTTOM=2;      // Represents bottom square array index.
    public static final int TOP=2;         // Represents top circle array index.
    public static final int NUM_SQUARES=3; // Number of squares.
    public static final int NUM_CIRCLES=3; // Number of circles.
    // Minimum and maximum input values.
    public static final int MIN_NUMBER = -40;
    public static final int MAX_NUMBER = 40;
    // Sentinel value representing unsolved problem or no solution found.
    public static final int NO_SOLUTION = 99;
    // Sentinel value representing no input received.
    public static final int NO_INPUT = 99;
    // Instance values for the triangle's squares and circles.
    private int[] squares = new int[NUM_SQUARES];
    private int[] circles = new int[NUM_CIRCLES];

    /*********************************************************************
     * Constructors.
     *********************************************************************/
    public MagicTriangle( final int[] s, final int[] c ) {
      // Assert good parameters.
      assert ( c != null && s != null ) : "Constructor: param invalid";
      // Validate and initialize instance fields.
      for ( int i=0; i<NUM_SQUARES; i++ ) {
        if ( i < s.length && s[i] >= MIN_NUMBER && s[i] <= MAX_NUMBER )
          this.squares[i] = s[i];
        else 
          this.squares[i] = NO_INPUT;
        if ( i < c.length && c[i] >= MIN_NUMBER && c[i] <= MAX_NUMBER )
          this.circles[i] = c[i];
        else
          this.circles[i] = NO_SOLUTION;
      }
    } 
    // Naked constructor.
    public MagicTriangle() {
      // Initialize instance fields.
      for ( int i=0; i<NUM_SQUARES; i++ ) {
          this.squares[i] = NO_INPUT;
          this.circles[i] = NO_SOLUTION;
      }
    } 

    /*********************************************************************
     * Square's setter function.
     *********************************************************************/
    public void setSquares( final int[] s ) {
      // Assert good parameter.
      assert ( s != null ) : "setSquares: square invalid";
      // Validate parameters.
      for ( int i=0; i<NUM_SQUARES; i++ ) 
        if ( i < s.length && s[i] >= MIN_NUMBER && s[i] <= MAX_NUMBER )
          this.squares[i] = s[i];
        else 
          this.squares[i] = NO_INPUT;
    }

    /*********************************************************************
     * Display program instructions and sample triangle.
     *********************************************************************/
    public void displayInstructions() {
      System.out.println( "Magic Triangle: The numbers in the squares below are the" );
      System.out.println( "sums of the numbers in the circles to which they are connected." );
      System.out.println( "Enter numbers between -40 and 40 for the squares, and the" );
      System.out.println( "program will find the numbers in the circles. For example:" );
      // Display an example triangle.
      displayTriangle();
    }

    /*********************************************************************
    * Output a diagram of the magic triangle filled appropriately with 
    * numbers or blank characters.
     *********************************************************************/
    public void displayTriangle() {
      // Display triangle diagram with field data.
      System.out.format( "       (%1$3s)%n", (circles[TOP]==NO_SOLUTION) ? "" : String.valueOf(circles[TOP]) );
      System.out.format( "      /     \\%n   [%1$3s]", (squares[LEFT]==NO_INPUT) ? "" : String.valueOf(squares[LEFT]) );
      System.out.format( "   [%1$3s]%n", (squares[RIGHT]==NO_INPUT) ? "" : String.valueOf(squares[RIGHT]) );
      System.out.format( "   /           \\%n (%1$3s)", (circles[LEFT]==NO_SOLUTION) ? "" : String.valueOf(circles[LEFT]) );
      System.out.format( "-[%1$3s]-", (squares[BOTTOM]==NO_INPUT) ? "" : String.valueOf(squares[BOTTOM]) );
      System.out.format( "(%1$3s)%n", (circles[RIGHT]==NO_SOLUTION) ? "" : String.valueOf(circles[RIGHT]) );
    }

    /*********************************************************************
     * Attempt to solve the magic triangle via Cramer's Rule. If solution
     * is valid, the circles array is populated with answers and the 
     * method returns true. If not valid, the circles array is filled with 
     * the NO_SOLUTION value, and the method returns false.
     *********************************************************************/
    public boolean solveTriangle() {
      // Check for valid square data.
      if ( squares.length != NUM_SQUARES ) 
        return false;
      if ( squares[LEFT] == NO_INPUT || squares[RIGHT] == NO_INPUT || squares[BOTTOM] == NO_INPUT )
        return false;
      // Use optimized Cramer's Rule to find a possible solution. Values computed 
      // here can be wrong, and are checked below for valid solution.
      circles[TOP] = ( squares[BOTTOM] - squares[RIGHT] - squares[LEFT] ) / -2; 
      circles[LEFT] = ( squares[RIGHT] - squares[LEFT] - squares[BOTTOM] ) / -2; 
      circles[RIGHT] = ( squares[LEFT] - squares[RIGHT] - squares[BOTTOM] ) / -2; 
      // Check and return true if we found a solution.
      if ( ( squares[LEFT] == circles[TOP] + circles[LEFT] ) &&
           ( squares[RIGHT] == circles[TOP] + circles[RIGHT] ) &&
           ( squares[BOTTOM] == circles[LEFT] + circles[RIGHT] ) ) {
        return true;
     } else {
        // We didn't find a solution, so erase the incorrect data.
        for ( int i=0; i<NUM_CIRCLES; i++ )
          circles[i] = NO_SOLUTION;
      }
      // Return no solution found.
      return false;
    }
    
  } // End of MagicTriangle class
    
  /*********************************************************************
   * Function: enterSquares(int[] squares)
   * Input:    int[] (squares), values to be place in triangle squares
   * Return:   n/a
   * 
   * Prompts user to enter valid integers to be placed inside the 
   * triangle's squares. Uses the input scanner. Note: Purposely does 
   * not close the input stream.
   *********************************************************************/
  private static void enterSquares( int[] squares ) {
    int number;               // Used to temporarily hold user input.
    boolean invalidInputFlag; // Flag value indicates a validated integer was input.
    // String array representing which square value is being requested. 
    String[] location = new String[] { "left side", "right side", "bottom" };
       
    // Loop through all 3 squares.
    for ( int i=0; i<MagicTriangle.NUM_SQUARES; i++ ) {
       // Prompt/input/validate loop.
      do {
        // Assume bad input by setting to true.
        invalidInputFlag = true;
        // Prompt user for input.
        System.out.format( "Enter the number for the %s square:%n", location[i] );
        // We catch NumberFormatException.
        try {
          number = Integer.parseInt( scanner.nextLine().trim() );
          if ( number >= MagicTriangle.MIN_NUMBER && number <= MagicTriangle.MAX_NUMBER ) {
            // Valid integer. Flag that good input received so do/while loop terminates.
            squares[i] = number;
            invalidInputFlag = false;
          } else {
            // User input an invalid integer. Alert and continue loop.
            System.out.format( "Input must be between (%d and %d).\r\n", MagicTriangle.MIN_NUMBER, MagicTriangle.MAX_NUMBER );
          }
        } catch ( NumberFormatException ex ) {
           System.out.println( "That is not a valid number!" );
        }
      //loop until valid non-negative integer is received.
      } while ( invalidInputFlag );
    }
  }
    
  /*********************************************************************
   * Function: queryYesNo(String prompt)
   * Input:    String (prompt), question asked user.
   * Return:   boolean
   * 
   * Prints a prompt and then continues in a loop until a valid input 
   * character of 'y', 'Y', 'n' or 'N' is received. The function 
   * returns boolean true for user input of 'y' or 'Y', and false 
   * for user input of 'n' or 'N'. Uses the input scanner. Note: Purposely 
   * does not close the input stream.
   *********************************************************************/
  private static boolean queryYesNo( final String prompt ) {
    String userInput; // Used to hold user input.
    
    // Assert valid parameter.
    assert ( prompt != null ) : "queryYesNo prompt nulll";
    
    do { 
      // Concatenate and display prompting message.
      System.out.format( "%s (please respond: ‘y’ or ‘n’)?%n", prompt );
      // Use scanner class to get user input, trim, and change to lower case
      userInput = scanner.nextLine().trim().toLowerCase();
      // Test for valid input (characters: y, Y, n, N), continue to loop otherwise.
    } while ( !userInput.equals( "y" ) && !userInput.equals( "n" ) );
    // Test user input character for lower case 'y'.
    return ( userInput.equals( "y" ) );
  }

  /*********************************************************************
   * Start of main program. Command line arguments are ignored.
   *********************************************************************/
  public static void main( String[] args ) {
    // Computed solution to the magic triangle, represents values in the 3 circles.
    int[] circles = new int[] { MagicTriangle.NO_SOLUTION, MagicTriangle.NO_SOLUTION, MagicTriangle.NO_SOLUTION }; 
    // Values input by user, represents values in the 3 squares of the magic triangle.
    // These initial values are used as an example in the instructions.
    int[] squares = new int[] { 14, 20, 22 }; 
    // Flag used to continue/stop play.
    boolean playMagicTriangle;
    // Instantiate program class to create instance of nested class, and assert valid.
    CIS129_JamesEli_FP myApp = new CIS129_JamesEli_FP();
    assert ( myApp != null ) : "CIS129_JamesEli_FP instantiation failed";
    // Create instance of nested Magic Triangle class, asserting it worked.
    CIS129_JamesEli_FP.MagicTriangle magicTriangle = myApp.new MagicTriangle( squares, circles );
    assert ( magicTriangle != null ) : "Magic Triangle instantiation failed";
    
    // Start by displaying the instructions.
    magicTriangle.displayInstructions();
    // Ask if user wants to play?
    if ( queryYesNo( "Are you ready to play Magic Triangle" ) )
      playMagicTriangle = true;
    else
      playMagicTriangle = false;
    
    // Main play loop, ignored if user doesn't want to play.
    while ( playMagicTriangle ) {
      enterSquares( squares );             // Ask user to enter square values.
      magicTriangle.setSquares( squares ); // Set square values of triangle.
      if ( magicTriangle.solveTriangle() ) // Attempt to solve triangle.
        System.out.println( "Here is the solution:" );
      else 
        System.out.println( "Entered values has no solution:" );
      magicTriangle.displayTriangle();     // Display results.
      // Ask if user wants to continue playing?
      if ( !queryYesNo( "Play Magic Triangle again" ) )
        playMagicTriangle = false;
    } // End of main play loop.
    
    // Normal program termination is here.
      
  } // End of Main module.

} // End of CIS129_JamesEli_FP class

