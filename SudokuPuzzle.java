import java.util.*;
import java.util.Random;

// Version 2018-10-10 - Wednesday

public class SudokuPuzzle {

	private int board[][];
	private boolean start[][];

	// 2-Dimensional Map:   Key: String, Value: Set<Integer> 
	// The key is a String which concatenates the row and column:
	// i.e. A key of 1 means row 0, col 1.  A key of 74 is row 7, col 4.
	// The value mapped to the key is a set of Integers that can be played: {1,2,3,4,5,6,7,8,9}
	// Sets have the faster get methods to test for membership compared to an Array
	// This initial map will be populated in the Constructor

	public Map <Integer, Set<Integer>> allowed_values = new HashMap<Integer, Set <Integer>>();

	// Holds all used members - using set for O(1) -- THIS IS ACTUALLY PROBABLY USELESS
	private List<Set<Integer>> row_seen = new ArrayList<>();
	private List<Set<Integer>> col_seen = new ArrayList<>();
	private List<Set<Integer>> box_seen = new ArrayList<>(); 

	// This is a set of numbers from 0 to 88  (excluding 9, 19, 29, 39, 49...)
	// This will be populated in the constructor
	private Set<Integer> vacancies;
 
 	//CONSTRUCTOR:
	public SudokuPuzzle() {
		/* 

		Constructs Four groups of things. This operator allows us to create multiple instances of puzzles.
		
		1) this.board  -- Actual State of the 9x9 board. Empty values are 0  

		2) this.start  --  9 x 9 - Default numbers placed at the beginning of the game that cannot be changed
						-- True if it is default & cannot be changed  , False if it was originally blank

		3) this.allowed_values -- A mapping of all the possible (allowed) numbers that can be played in a given row/col coordinate
						-- We initialize every row/col coordinate with the set {1,2,3,4,5,6,7,8,9}
						-- This initialization is done in the private method  populateSet()		
		*/

		this.board = new int[9][9]; // 1) 
		this.start = new boolean[9][9]; // 2)  

		// 3) this.possible:
		// (row,col) coordinate to be stored as a map with the key as a concatenated string 
		// e.g. row 1, col 3, is a string of "13"
		// Value stores a HashSet {1 .... 9} at default for valid choices

		for (int i = 0; i < 9; i ++){ // Each row
			
			for (int j = 0; j < 9 ; j++) // Each column
			{

				Set<Integer> set = new HashSet<Integer>();
				populateSet(set, 10, false); // Populate set from 0 (inclusive) until 10-1
				this.allowed_values.put( (i*10) + j , set ); // String concatenation to build the key
			}
		}

		// 4)
		this.vacancies = new HashSet<Integer>();
		populateSet(this.vacancies,89, true);
	}


	//@Not to be used outside of constructor
	private void populateSet(Set<Integer> set, int end, boolean removeNines){
		// Populate set with {1,2,3,4,5,6,7,8 ... end-1 } 
		if (removeNines == true){
			for (int num = 1; num < end; num++ ){ 
					if (num%10 != 9) { // don't add 9, 19, 29, 39, 49 ... etc
						set.add(num); 
					}
			}
		} else {// removeNines == false 
			for (int num = 1; num < end; num++ ){ 
						set.add(num); 
			}
		}
	}

	//@Not to be used outside. Just for picking a random number from the set
	private int produceValue(Set<Integer> set){
		// Pass in a set as a parameter.
		// this function will randomly pick one of the numbers in the set to spit out
		int size = set.size(); // Get the size of the set 
		int value = new Random().nextInt(size); // Get a number from 0 to the size of set
		int i = 0; // Start counting as you iterate through the set, until we spit oone
		for (int number : set)
		{
			if (i == value)
				return number;
			i++;
		}
		return -1; // to make compiler shut up "Must have a return because it can't see inside the for loop"

	} 

	//@For Debug
	public static void main(String[] args){
		SudokuPuzzle puzz = new SudokuPuzzle();
		// System.out.println(puzz.allowed_values.get(1));
		//Set<Integer> numbers = new HashSet<Integer>();
		//System.out.println(puzz.produceValue(numbers));	
		puzz.addInitial();
		puzz.display();
	}
	
	public void addInitial(){
		// Initializes the board with 10 random legal positions! 
		
		for (int pos = 0; pos < 15; pos++){
			// Generate coordinate
			int vacant_coordinate = produceValue(this.vacancies);
			int random_row = vacant_coordinate/10; 
			int random_col = vacant_coordinate%10; 
			this.vacancies.remove(vacant_coordinate); 

			// Produce valid value
			Set<Integer> numbers_set = this.allowed_values.get(vacant_coordinate); // Get all the allowed numbers it can be
			int value = produceValue(numbers_set); // Randomly pick one of the numbers
			
			// Update the board & start
			this.board[random_row][random_col] = value;
			this.start[random_row][random_col] = true; 

			
			
			// Update the allowed_values by row. j is the column. row stays constant
			for (int j = 0; j < 9; j++) { 
				int key = random_row*10 + j;
				this.allowed_values.get(key).remove(value);
			}

			// Update the allowed_values by column. i is the row. col stays constant
			for (int i = 0; i <9; i++) { 
				int key = i*10 + random_col;
				this.allowed_values.get(key).remove(value);
			}
			// Remove this value from every member in the entire box
			
			// Box 1		Box 2		...
			// 0 [00 01 02 ]	[03 04 05]
			// 1 [10 11 12 ]	[13 14 15]
			// 2 [20 21 22 ]  [23 24 25]
			// Box 4
			// 3 [30 31 32]
			// 4 [40 41 42]
			// 5 [50 51 52]




			int box_row = (random_row/3)*3 ;
			int box_col = (random_col/3)*3 ;
			for (int i = box_row ; i < box_row + 3 ; i++ ){
				for (int j = box_col ; j < box_col + 3 ; j++){

					int key = i*10 + j;
					this.allowed_values.get(key).remove(value);
				}

			}
		}

			
	}
	
	public void display(){
		// Displays board 

		// This part displays the x-coordinate (the numbering for each column)
		System.out.print("  ");
		for (int x = 0; x < 9; x ++){
			String dividerx = (x%3 == 2) ? ("   ") : (" ");	
			System.out.print(" " + x + " " + dividerx); // Label the columns
		}
		System.out.println(); // new line


		// This part just displays the actual sudoku boards
		for (int i = 0; i < 9; i ++ ){
			
			System.out.print(i + " "); // Labeling the rows 
			
			for (int j = 0; j < 9; j++){

				String divider = (j%3 == 2) ? ("   ") : (" ");	// Divides by boxes 
				if (board[i][j] == 0)  {
					System.out.print("[ ]" + divider);
				} else {
					System.out.print("[" + board[i][j] +"]" + divider); // Print the actual Value
				}			
			}
			// Produce new line, divide the boxes up
			String vertical_divider = (i%3 == 2) ? "\n" : "";
			System.out.println(vertical_divider);
		}
	}

/*
	public void addGuess(row,col, value){
		// sets the given square to the given value; 
		// the value can be changed later by another call to addGuess
		// while vacancies 
		;
	}
*/
	public boolean checkPuzzle(){
		// returns true if the values in the puzzle do not violate the restrictions
		
		// Numbering system for the 9 boxes:
		//  0  1  2   <--
		//  3  4  5   <--
		//  6  7  8   <-- for the box index. 
		
		// To compute my box formula, I just took the row and column: 

		// visulization for the box formula: 
		// 0+0	0+1 0+2
		// 3+0	3+1	3+2
		// 6+0	6+1	6+2

		// any number between 0,1,2 when you divide by 3 becomes 0

		for (int row = 0; row < 9; row ++ ){

			for (int col = 0; col < 9; col ++) {

				int box =  (row/3)*3 + (col/3); // Compute which box the coordinate belongs to based on its row/col position

				int num = this.board[row][col];
				if (!this.row_seen.get(row).contains(num)   // If the rows_seen does not have that number 
					&& !this.col_seen.get(col).contains(num) 
					&& !this.box_seen.get(box).contains(num) ) 
				{
					row_seen.get(row).add(num);
					col_seen.get(col).add(num);
					box_seen.get(box).add(num);
				} else { // There is a contradiction
					return false;
				}
			}
		}

		return true;			
	}


	/* 
	public int getValueIn(row, col){
		// returns the value in the given square
		return this.board[row][col]
		
	}

	public boolean[] getAllowedValues(row, col){
		// returns a one-dimensional array of nine booleans, 
		// each of which corresponds to a digit 
		// and is true if the digit can be placed in the given square 
		// without violating the restrictions
		// this is stupid - we don't even need this function

		return ;
	}

	public boolean isFull(){
		// Waste of computation time to check 81 booleans 
		// when you could have just kept track of the count in the beginning. Only one boolean check (81x faster)
		return this.vacancies.size() == 0;
	}	

	public boolean reset() {
		// Easy 
		// Changes all of the nonpermanent squares back to blanks (0s)
		;
	}
*/
}
