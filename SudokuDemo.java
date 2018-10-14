import java.util.Scanner;

public class SudokuDemo {

	public static void main(String[] args){
		
		SudokuPuzzle puzz = new SudokuPuzzle();
		puzz.addInitial();
		puzz.display();
		Scanner scan = new Scanner(System.in);
		boolean keep_playing;
		keep_playing = true;

		while ( (keep_playing) && (!puzz.isFull()) ){

			//TODO: menu option - something liek this or whatever?
			System.out.println("**1) Make a guess (Row, Col, Value) in that order and press Enter each time"
					+ "\n" + "**2) Puzzle Reset (from previous start)" 
					+ "\n" + "**3) New Puzzle  (start a brand new one)" 
			    	+ "\n" + "**4) Quit Game! ");

			int action = scan.nextInt();

			switch (action) {
				case 1: 

					while(true){
						System.out.println("**Input:Row Col Value. Value: 0 to erase. 1-9 for guess");
						int row = scan.nextInt();
						int col = scan.nextInt();
						int number = scan.nextInt();

						if (puzz.addGuess(row, col, number)){  // Only displays if valid
							puzz.display();
							break; // breaks the while
						} else {
							System.out.println("**Yo, this is not allowed!");
						}
					}
					break; // breaks the case

				case 2: 
					System.out.println("**Resetting to initial state\n");
					puzz.reset();
					puzz.display();
					break;

				case 3:
					System.out.println("**Here's a brand new puzzle\n");
					puzz = new SudokuPuzzle();
					puzz.addInitial();
					puzz.display();
					break;
				case 4:
					keep_playing = false;
					break;
			}

		} // end infinite loop

		String message = puzz.isFull() ? ("You win!"):("You suck!");
		System.out.println(message);

	} // end main
	
} // end class
