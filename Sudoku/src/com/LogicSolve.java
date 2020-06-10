package com;

public class LogicSolve {
	
	public static void LogicSolve(Sudoku s) {
		while(!solved(s.sudokuArray)) {//TODO: Implement check more efficient that checking every box
			
			//Check for naked singles
			//Naked singles are unsolved slots in a sudoku problem that have only one possible value
			//When checked by eliminating elements that are in its row/column/box/di
			for(int row = 0;row < 9;row++) {
				for(int column = 0;column < 9; column++) {
					if(s.sudokuArray[row][column] == -1) {
						int nakedSingle = checkNakedSingle(row,column,s.sudokuArray,s.diagonal);
						if(nakedSingle != -1) {
							s.sudokuArray[row][column] = nakedSingle;
							//log("Row " + Integer.toString(row+1) + " Column " + Integer.toString(column+1) + " is " + Integer.toString(nakedSingle)
							//+ ", and was solved by being a naked single.",s.sudokuArray);
							//s.updateGraphics();
							
							row = 0;//Return to beginning
							column = 0;
							
						}
					}
				}
			}
			
			
			
		}
	}
	
	//Test if it is solved or not (all slots filled in)
	private static boolean solved(int[][] sudokuArray) {
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(sudokuArray[i][j] == -1) { //-1 indicates a slot is not filled, meaning it is not solved
					return false;
				}
			}
		}
		//TODO: Verify that the filled in table satisfies all sudoku checks (Row,column,box,diagonal(if enabled))
		return true;//If it passed all checks then it is solved, return true
	}
	
	private static void log(String logString,int[][] sudoku) {
		//TODO: Create UI or log to file or something
		System.out.println(logString);//Print out the log string
		System.out.printf("\n\n-------------------\n");
		for(int i = 0;i < 9;i++) {
			System.out.printf("|");
			for(int j = 0;j < 9;j++) {
				String numString;
				if(sudoku[i][j] == -1) {
					numString = " ";
				}
				else {
					numString = Integer.toString(sudoku[i][j]);
				}
				System.out.printf("%s|",numString);
			}
			System.out.printf("\n-------------------\n");
		}
		System.out.printf("\n\n");
	}
	
	//A naked single is a slot on a sudoku table where the is only one valid element availible to that slot
	//Since there is only one possible spot you can declare it to be that remaining element
	private static int checkNakedSingle(int row,int column,int[][] sudoku,boolean diagonal) {
		boolean[] valid = new boolean[9];//Create an array that stores boolean. Each one says its index+1 is a valid number for the box.
		java.util.Arrays.fill(valid, true);//Set all to be valid at first
		
		//Check for all numbers in your column that are not you
		for(int i = 0;i < 9; i++) {
			int temp = sudoku[i][column];
			if(i != row && temp != -1) {//As long as youre not comparing it to yourself or an invalid box
				valid[temp - 1] = false;//It has seen it, and thus is false.
			}
		}
		
		//Check for all numbers in your row that are not you
		for(int j = 0;j < 9; j++) {
			int temp = sudoku[row][j];
			if(j != column && temp != -1) {//As long as youre not comparing it to yourself
				valid[temp - 1] = false;//It has seen it, and thus is false.
			}
		}
		
		
		//This section checks for diagonals
		//Only do this if the user has entered a diagonal sudoku and checked the diagonal box
		if(diagonal) {
			//Check for all numbers along the diagonal, if you are across the diagonal
			//This one checks for the left diagonal
			if(row == column) {//The row equaling the column implies you are along the diagonal
				for(int k = 0;k < 9; k++) {
					int temp = sudoku[k][k];
					if(k != column && temp != -1) {//As long as youre not comparing it to yourself
						valid[temp - 1] = false;//It has seen it, and thus is false.
					}
				}
			}
			
			
			//Check for all numbers along the diagonal, if you are across the diagonal
			//This one checks for the right diagonal
			if((row+1) + (column+1) == 10) {//If you look at a traditional sudoku board, adding the row+column (while indexing by one)
											//equaling 10 implies it is on the right diagonal
				for(int k = 8;k != -1; k--) {
					int tempRow = 8 - k;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
										//If k, the column, was say 2, the row would have to be 6.
					int temp = sudoku[tempRow][k];
					if(k != column && temp != -1) {//As long as youre not comparing it to yourself
						valid[temp - 1] = false;//It has seen it, and thus is false.
					}
				}
			}
		}
		
		
		
		//Check the box you are in
		//First find the top left position of the sub box
		int originX = (row/3)*3;//Dividing and then multiplying by three will shrink then truncate it to get the x and y
		int originY = (column/3)*3;//Of the top left of the sub box you are in
		
		for(int i = originX;i < originX + 3;i++) {
			for(int j = originY;j < originY + 3;j++) {
				int temp = sudoku[i][j];
				if(temp != -1) {//Make sure it is a valid, set box
					if(i != row || j != column) {//Make sure you are not comparing it to yourself, at your coordinates
						valid[temp - 1] = false;//It has seen it, and thus is false.
					}
				}
			}
		}
		
		int nakedSingle = -1;//Assume there are no valid elements
		
		//Iterate through the list of valid elements
		//If you find multiple valid elements then you have not found a naked single
		//Return -1 to indicate that these coordinates do not hold a naked single
		for(int i = 0; i < 9;i++) {
			if(valid[i] == true) {
				if(nakedSingle != -1) {//If you have already recorded a number, and found another valid number, then it is not a naked single
					return -1;
				}
				nakedSingle = i + 1;
			}
		}
		
		return nakedSingle;//If you did not have multiple valid elements then you may return the value of the naked single
	}

}
