import java.util.*;
import java.util.Random;

// Version 2018-10-08 - Monday 4:00 PM

public class SudokuPuzzle {

	private int board[][];
	private boolean start[][];

	// holds possible/valid values - this will be used to check . HashMaps have O(1) access 
	public Map <String, Set<Integer>> possibles = new HashMap<String, Set <Integer>>();

	// Holds all used numbers - using set for O(1)
	private List<Set<Integer>> row_seen = new ArrayList();
	private List<Set<Integer>> col_seen = new ArrayList();
	private List<Set<Integer>> box_seen = new ArrayList(); 
 
	public SudokuPuzzle() {
		/* Constructs three things:
		
		1) this.board  -- Actual State of the 9x9 board 

		2) this.start  --  9 x 9Default numbers placed at the beginning of the game that cannot be changed
						-- True if it is default & cannot be changed  , False if it was originally blank

		3) this.possible -- A mapping of all the possible numbers that can be played in a given row/col coordinate
						-- In the constructor, this just means we initialize every row/col coordinate with the set {1,2,3,4,5,6,7,8,9}
						-- This initialization is done in the private method  populateSet()

		4)  this.row_seen   -- This shows all the numbers seen in that given row
			this.col_seen  -- This shows all the numbers seen in that given col
			this.box_seen  -- This shows all the numbers seen in that given box 

		*/

		this.board = new int[9][9]; // Actual State of the Board
		this.start = new boolean[9][9]; // 9 * 9 

		// Populate HashMap - 
		// (row,col) coordinate to be stored as a map with the key as a concatenated string 
		// e.g. row 1, col 3, is a string of "13"
		// Value stores a HashSet {1 .... 9} at default for valid choices

		for (int i = 0; i < 9; i ++){ // Each row
			
			for (int j = 0; j < 9 ; j++) // Each column
			{

				Set<Integer> set = new HashSet<Integer>();
				populateSet(set);
				this.possibles.put( Integer.toString(i) + j , set );
			}
		}
	}

	//@For Debug
	public static void main(String[] args){
		SudokuPuzzle puzz = new SudokuPuzzle();
		System.out.println(puzz.possibles.get("01"));
	}

	//@Not to be used outside of constructor
	private void populateSet(Set<Integer> set){
		// Populate set with {1,2,3,4,5,6,7,8,9} 
		for (int num = 1; num < 10; num++ )
				{ 
					set.add(num); 
				}
	}

	//@Not to be used outside of addInitial -- Just for creating random number
	private 
	
	public void addInitial(row,col,value){
		// sets the given square to the given value as an initial value 
		// that cannot be changed by the puzzle solver
		
		


		;
		

/*
	}

	public void addGuess(row,col, value){
		// sets the given square to the given value; 
		// the value can be changed later by another call to addGuess
		;
	}
*/
	public boolean checkPuzzle(){
		// returns true if the values in the puzzle do not violate the restrictions
		
		//  0  1  2   <--
		//  3  4  5   <--
		//  6  7  8   <-- for the box index. 
		
		// Given i, j, and since we are going row and column, just integer divide by 3, mult by  3 and add horizontaly

		// 0+0	1+0 2+0
		// 3+0	3+1	3+2
		// 6+0	6+1	6+2

		for (int row = 0; row < 9; row ++ ){

			for (int col = 0; col < 9; col ++) {

				int box =  (row/3)*3 + (col/3) + 1; // Compute which box the coordinate belongs to based on its row/col position

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
		
	}

	public boolean[] getAllowedValues(row, col){
		// returns a one-dimensional array of nine booleans, 
		// each of which corresponds to a digit 
		// and is true if the digit can be placed in the given square 
		// without violating the restrictions

		// this is stupid

		return this.allowed[row][col];
	}

	public boolean isFull(){
		// Easy
		// isFull—returns true if every square has a value
		;
	}	

	public boolean reset() {
		// Easy 
		// Changes all of the nonpermanent squares back to blanks (0s)
		;
	}
*/
}