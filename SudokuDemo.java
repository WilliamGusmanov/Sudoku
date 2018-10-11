import java.util.Scanner;

public class SudokuDemo {

	public static void main(String[] args){
		
		SudokuPuzzle puzz = new SudokuPuzzle();
		puzz.addInitial();
		puzz.display();
		Scanner scan = new Scanner(System.in);

		while (true) {

			int row = scan.nextInt();
			int col = scan.nextInt();
			int number = scan.nextInt(); // get number to put in

			puzz.addGuess(row, col, number); // this function only allows valid input
			puzz.display();

			System.out.println("You wanna restart from beginning?");
			// process input, invoke puzz.reset();
			System.out.println("You wanna just get a brand new puzzle?");
			// puzz = new SudokuPuzzle(); // remakes 
			System.out.println("You wanna quit game?");
			// continue = false;

		}

	}
	
}