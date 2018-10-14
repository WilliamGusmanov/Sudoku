

import java.util.*;
import java.util.Random;

// Version 2018-10-08 - Wednesday
public class SudokuPuzzle {

	private int board[][]; // 9x9 sudoku board of integers 
	private boolean start[][]; // True for positions that are part of the initial board and cannot be modified
	public Map <Integer, Set<Integer>> allowed_values = new HashMap<Integer, Set <Integer>>(); // Key is row*10 + col, value is the set of integers of allowed values
	private Set<Integer> vacancies; // Set of row/col combinations (00 to 88, with no 9,19,29 etc..) that have vacant spots
 
	public SudokuPuzzle() { // Constructor
		// Initialize board & Start:
		this.board = new int[9][9]; 
		this.start = new boolean[9][9];  

		// Initialize allowed_vaues
		for (int i = 0; i < 9; i ++){ 
			for (int j = 0; j < 9 ; j++) 
			{
				Set<Integer> set = new HashSet<Integer>();
				populateSet(set, 10, false); // Populate set from 0 (inclusive) until 10-1
				this.allowed_values.put( (i*10) + j , set ); // String concatenation to build the key
			}
		}

		// Initialize vacancies
		this.vacancies = new HashSet<Integer>();
		populateSet(this.vacancies,89, true);
	}

	//@Not to be used outside of constructor or class
	private void populateSet(Set<Integer> set, int end, boolean removeNines){ 	
		if (removeNines == true){ // For populating vacancies
			for (int num = 0; num < end; num++ ){ 
					if (num%10 != 9) { // don't add 9, 19, 29, 39, 49 ... etc
						set.add(num); 
					}
			}
		} else {// removeNines == false , i.e. for populating allowed_values
			for (int num = 1; num < end; num++ ){ 
						set.add(num); 
			}
		}
	}

	//@Not to be used outside of class. 
	private int produceValue(Set<Integer> set){
		// Pass in a set of all numbers. This function will randomly return a value from that set.
		int size = set.size(); 
		int value = new Random().nextInt(size); 
		int i = 0; 
		for (int number : set){
			if (i == value)
				return number;
			i++;
		}
		return -1; // To make compiler shut up - This never happens. b/c "Must have a return"

	} 
	
	public void addInitial(){ // Initializes the board with 15 random legal positions! 
		
		for (int pos = 0; pos < 15; pos++){
			// Generate coordinate
			int vacant_coordinate = produceValue(this.vacancies);
			int random_row = vacant_coordinate/10; 
			int random_col = vacant_coordinate%10; 
			this.vacancies.remove(vacant_coordinate); 

			// Produce valid value
			Set<Integer> numbers_set = this.allowed_values.get(vacant_coordinate); // Get all the allowed numbers it can be
			int value = produceValue(numbers_set); // Randomly pick one of the numbers
			
			// Update the start
			this.start[random_row][random_col] = true; 

			this.updateVacanciesAndAllowedValues(random_row,random_col, value); // fix the vacancies and allowed values
			this.board[random_row][random_col] = value;  // update the board with the value
			
		} // end 15 iterations
	} // end addInitial method

	private void updateVacanciesAndAllowedValues(int row, int col, int value) { // updates 
			
		// Update the board & start
		boolean isErasing = (value == 0); 
		int previous_value = this.board[row][col]; // remember the previous value
		
		boolean previously_Empty = false;
		if (previous_value == 0){ // if it was previously empty
			previously_Empty = true;
		}

		if (isErasing){ 
			this.vacancies.add(row*10 + col); // That spot is now vacant
		} else { // Else we're changing guess - no new vacancie
			;
		}

			// Update the allowed_values:
			int key; // to access the allowed values

			for (int j = 0; j < 9; j++) {  // updating every member of the same row
				key = row*10 + j;

				this.allowed_values.get(key).remove(value); // Remove the value we're changing
				
				if (!previously_Empty){
					this.allowed_values.get(key).add(previous_value); // Refund the previous value
				} // end if
			}

			for (int i = 0; i <9; i++) { // updating every member of the same column
				key = i*10 + col;
				
				this.allowed_values.get(key).remove(value); // remove new value
				
				if (!previously_Empty){
					this.allowed_values.get(key).add(previous_value); // refund previous
				}//end if
			}

			// updating every member of the same "box": see below diagram for derivation of the formula
			int box_row = (row/3)*3 ; // {0,1,2} --> 0 ; {3,4,5} --> 3 ; { 6,7,8} --> 6;
			int box_col = (col/3)*3 ;

			for (int i = box_row ; i < box_row + 3 ; i++ ){ // each box is 3 x 3 
				for (int j = box_col ; j < box_col + 3 ; j++){ 
					key = i*10 + j;
				
					this.allowed_values.get(key).remove(value);
				
				if (!previously_Empty){
					this.allowed_values.get(key).add(previous_value);
				} // end if
				} //end j for
			} // end i for

	}
	
	public void display(){ // board display

		// This part displays the x-coordinate (the numbering for each column)
		System.out.print("  ");
		for (int x = 0; x < 9; x ++){
			String dividerx = (x%3 == 2) ? ("   ") : (" ");	
			System.out.print(" " + x + " " + dividerx); // Labeling the columns
		}
		System.out.println(); 

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

	public boolean addGuess(int row, int col, int value){ // -> Boolean if it's a valid guess or not

		int key = row*10 + col;
		boolean isempty = this.vacancies.contains(key);
		boolean isallowed = this.allowed_values.get(key).contains(value);
		boolean iszero = (value == 0) && (!this.start[row][col]); // zero means remove entry if its not a start

		if ( (isempty && isallowed) || iszero){
			this.updateVacanciesAndAllowedValues(row,col,value); // update before adding in case they are erasing
			this.board[row][col] = value;
			return true;
		} else {
			return false;
		}
	}

	public boolean isFull(){ // checks for a full board
		return this.vacancies.size() == 0;
	}	

	public void reset() { // Changes non start square to 0
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j <9 ; j++){
				if (!this.start[i][j]){
					this.board[i][j] = 0;
				} // end if
			} // end j
		} // end i
	}// end reset method

}
