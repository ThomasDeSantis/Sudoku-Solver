package com;
import java.util.*;
public class LogicSolve {
	
	public enum currentMethod{
		SINGLE,
		PAIR
	}
	
	public static void LogicSolve(Sudoku s) {
		currentMethod current = currentMethod.SINGLE;//Current indicates what method is currently being tested
		
		
		
		while(!solved(s.sudokuArray)) {//TODO: Implement check more efficient that checking every box
			boolean[][][] possibilities = new boolean[9][9][9];
			for(int i = 0;i < 9;i++) {
				for(int j = 0;j < 9; j++) {
					possibilities[i][j] = getStartingProbs(s.sudokuArray,i,j,s.diagonal);
				}
			}
			switch(current) {
				case SINGLE:
					if(checkForSingle(s.sudokuArray,possibilities,s.diagonal)) {//If you had found a single
						current = currentMethod.SINGLE;
					}
					else {
						current = currentMethod.PAIR;
					}
					break;
				case PAIR:
					if(checkForPair(s.sudokuArray,possibilities,s.diagonal)) {//If you had found a single
						current = currentMethod.SINGLE;
					}
					else {
						System.err.printf("Could not solve with logic. Exiting.");
						System.exit(-1);
					}
					break;
				default:
					break;
			}
			
			
			
		}
	}
	
	//Run this to check all slots for singles
	//A single (sometimes called a naked single) is a slot on a sudoku table where the is only one valid element availible to that slot
	//Since there is only one possible spot you can declare it to be that remaining element
	private static boolean checkForSingle(int[][] sudokuArray,boolean[][][]possibilities, boolean diagonal) {
		//Check for singles
		//Singles are unsolved slots in a sudoku problem that have only one possible value
		//When checked by eliminating elements that are in its row/column/box/diagonal (if applicable)
		for(int row = 0;row < 9;row++) {
			for(int column = 0;column < 9; column++) {
				if(sudokuArray[row][column] == -1) {
					int single = checkSingle(row,column,possibilities,diagonal);
					if(single != -1) {
						setSingle(sudokuArray,possibilities,row,column,single,diagonal);
						log("Row " + Integer.toString(row+1) + " Column " + Integer.toString(column+1) + " is " + Integer.toString(single)
						+ ", and was solved by being a single.",sudokuArray);
						//s.updateGraphics();
						
						return true;
					}
				}
			}
		}
		return false;//If you didnt find any then return false
	}
	
	//Check a specific location to be a single
	//A single is a slot on a sudoku table where the is only one valid element availible to that slot
	//Since there is only one possible spot you can declare it to be that remaining element
	private static int checkSingle(int row,int column,boolean[][][]possibilities, boolean diagonal) {
		int single = -1;
		for(int i = 0;i < 9;i++) {
			if(possibilities[row][column][i] == true) {
				if(single == -1) {
					single = i+1;
				}
				else {
					return -1;
				}
			}
		}
		return single;
	}
	
	//Sets all elements to be aware that a single exists, update the possibilities
	private static void setSingle(int[][]sudoku,boolean[][][]possibilities,int row,int column,int single,boolean diagonal){
		sudoku[row][column] = single;
		
		for(int i = 0;i < 9;i++) {
			possibilities[row][column][i] = false;
		}
		
		
		//Check for all numbers in your column that are not you
		for(int i = 0;i < 9; i++) {
			possibilities[i][column][single-1] = false;
		}
		
		//Check for all numbers in your row that are not you
		for(int j = 0;j < 9; j++) {
			possibilities[row][j][single-1] = false;
		}
		
		
		//This section checks for diagonals
		//Only do this if the user has entered a diagonal sudoku and checked the diagonal box
		if(diagonal) {
			//Check for all numbers along the diagonal, if you are across the diagonal
			//This one checks for the left diagonal
			if(row == column) {//The row equaling the column implies you are along the diagonal
				for(int k = 0;k < 9; k++) {
					possibilities[k][k][single-1] = false;
				}
			}
			
			
			//Check for all numbers along the diagonal, if you are across the diagonal
			//This one checks for the right diagonal
			if((row+1) + (column+1) == 10) {//If you look at a traditional sudoku board, adding the row+column (while indexing by one)
											//equaling 10 implies it is on the right diagonal
				for(int k = 8;k != -1; k--) {
					int tempRow = 8 - k;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
										//If k, the column, was say 2, the row would have to be 6.
					possibilities[tempRow][k][single-1] = false;
				}
			}
		}
		
		
		
		//Check the box you are in
		//First find the top left position of the sub box
		int originX = (row/3)*3;//Dividing and then multiplying by three will shrink then truncate it to get the x and y
		int originY = (column/3)*3;//Of the top left of the sub box you are in
		
		for(int i = originX;i < originX + 3;i++) {
			for(int j = originY;j < originY + 3;j++) {
				possibilities[i][j][single-1] = false;
			}
		}
		
		return;
	}
	
	private static boolean checkForPair(int[][] sudokuArray,boolean[][][]possibilities,boolean diagonal) {
		int[] doubles = new int[2];

		//Check for each box
		for(int row = 0;row < 9;row++) {
			for(int column = 0;column < 9;column++) {
				//System.out.printf("%d\n",possibilitiesSize(row,column,possibilities));
				if(possibilitiesSize(row,column,possibilities) == 2 && sudokuArray[row][column] == -1) {
					getDoublesPair(row,column,possibilities,doubles,diagonal);
					System.out.printf("Doubles %d %d",doubles[0],doubles[1]);
					if(checkForMatchingPair(row,column,possibilities,sudokuArray,doubles,diagonal)) {
						return true;
					}
				}
			}
			
		}
		return false;//If you didnt find any then return false
	}
	
	//This function will return a 2 element array filled with the two valid possibilities
	//In order for this to work correctly, you must ensure you check with possibilitiesSize() that only 2 possibilities are true for the indices
	//The information will be saved in the 2 element double array you have passed the function 
	private static void getDoublesPair(int row,int column,boolean[][][]possibilities,int[] doubles,boolean diagonal) {
		doubles[0] = -1;//Store both as -1 for now
		doubles[1] = -1;
		for(int i = 0;i < 9;i++) {//Iterate through the array
			if(possibilities[row][column][i] == true) {//If you find a true possibility
				if(doubles[0] == -1) {//If [0] is not set then set it there
					doubles[0] = i+1;
				}
				else {//Otherwise set it at [1] and finish
					doubles[1] = i+1;
					return;
				}
			}
		}
	}
	
	//Check for matching pairs
	//This means you check for any pair of doubles
	//(A double is when a slot in the sudoku table can only be one of two numbers)
	//(If two slots match their possible doubles then they are a double pair in a row/column/box/diagonal(if a diagonal sudoku), sometimes called a naked double)
	//(This means that those two elements can only be in 2 configurations, with double 1 being in box 1 and double 2 being in box 2/vice versa)
	//(Thus you can eliminate those two elements from any other unfilled box in the row/column/box/diagonal respectively)
	private static boolean checkForMatchingPair(int row,int column,boolean[][][]possibilities,int sudoku[][],int[] doubles,boolean diagonal) {
		int doublesTemp[] = new int[2];//Stores any doubles you want to compare to your given double pair
		
		//Check for matching double pairs in the possibilities of all other elements in your column
		for(int i = 0;i < 9; i++) {
			if(i != row && sudoku[i][column] == -1) {//As long as youre not comparing it to yourself or a filled in box
				if(possibilitiesSize(i,column,possibilities) == 2) {//Check if its a valid double
					getDoublesPair(i,column,possibilities,doublesTemp,diagonal);//If its a valid double get its double pair
					if(doublesPairMatch(doubles,doublesTemp)) {//Then check if the double pairs match
						if(doublesPairColumn(row,i,column,possibilities,sudoku,doubles,diagonal)) {//If they do match, any elements in the column can not possibly either of the doubles so eliminate them in the possibilties array
							return true;//If you solved a single from finding a double, return true to see if you can now access any new singles.
						}
					}
				}
			}
		}
		
		//Check for matching double pairs in the possibilities of all other elements in your row
		for(int j = 0;j < 9; j++) {
			if(j != column && sudoku[row][j] == -1) {//As long as youre not comparing it to yourself or a filled in box
				if(possibilitiesSize(row,j,possibilities) == 2) {//Check if its a valid double
					getDoublesPair(row,j,possibilities,doublesTemp,diagonal);//If its a valid double get its double pair
					if(doublesPairMatch(doubles,doublesTemp)) {//Then check if the double pairs match
						if(doublesPairRow(column,j,row,possibilities,sudoku,doubles,diagonal)) {//If they do match, any elements in the row can not possibly be either of the doubles so eliminate them in the possibilties array
							return true;//If you solved a single from finding a double, return true to see if you can now access any new singles.
						}
					}
				}
			}
		}
		
		
		
		//This section checks for diagonals
		//Only do this if the user has entered a diagonal sudoku and checked the diagonal box
		if(diagonal) {
			
			//Check for matching double pairs in the possibilities of all other elements in your top left diagonal
			if(row == column) {//The row equaling the column implies you are along the diagonal
				for(int k = 0;k < 9; k++) {
					if(k != column && sudoku[k][k] == -1) {//As long as youre not comparing it to yourself or a filled in box
						if(possibilitiesSize(k,k,possibilities) == 2) {//Check if its a valid double
							getDoublesPair(k,k,possibilities,doublesTemp,diagonal);//If its a valid double get its double pair
							if(doublesPairMatch(doubles,doublesTemp)) {//Then check if the double pairs match
								if(doublesPairLeftDiagonal(column,k,possibilities,sudoku,doubles,diagonal)) {//If they do match, any elements in the left diagonal can not possibly be either of the doubles so eliminate them in the possibilties array
									return true;//If you solved a single from finding a double, return true to see if you can now access any new singles.
								}
							}
						}
					}
				}
			}
			
			if((row+1) + (column+1) == 10) {//If you look at a traditional sudoku board, adding the row+column (while indexing by one)
				for(int k = 8;k != -1; k--) {
					if(k != column && sudoku[k][k] == -1) {//As long as youre not comparing it to yourself or a filled in box
						int tempRow = 8 - k;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
						//If k, the column, was say 2, the row would have to be 6.
						if(possibilitiesSize(tempRow,k,possibilities) == 2) {//Check if its a valid double
							getDoublesPair(tempRow,k,possibilities,doublesTemp,diagonal);//If its a valid double get its double pair
							if(doublesPairMatch(doubles,doublesTemp)) {//Then check if the double pairs match
								if(doublesPairRightDiagonal(column,k,possibilities,sudoku,doubles,diagonal)) {//If they do match, any elements in the left diagonal can not possibly be either of the doubles so eliminate them in the possibilties array
									return true;//If you solved a single from finding a double, return true to see if you can now access any new singles.
								}
							}
						}
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
				if(possibilitiesSize(i,j,possibilities) == 2) {//Check if its a valid double
					getDoublesPair(i,j,possibilities,doublesTemp,diagonal);//If its a valid double get its double pair
					if(doublesPairMatch(doubles,doublesTemp)) {//Then check if the double pairs match
						if(doublesPairBox(originX,originY,row,column,i,j, possibilities,sudoku,doubles,diagonal)) {//If they do match, any elements in the row can not possibly be either of the doubles so eliminate them in the possibilties array
							return true;//If you solved a single from finding a double, return true to see if you can now access any new singles.
						}
					}
				}
			}
		}
		
		
		return false;
		
	}
	
	//Given two arrays of size 2 each, check if they match
	//Alternative way is to just do a deep comparison, but would require conversion to object array
	private static boolean doublesPairMatch(int[]doubles,int[] doubles2){
		if(doubles[0] == doubles2[0] && doubles[1] == doubles2[1]) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	
	private static boolean doublesPairColumn(int r1,int r2,int column,boolean[][][]possibilities,int[][]sudoku,int[] doubles,boolean diagonal) {
		boolean AtLeastOneFound = false;
		for(int i = 0;i < 9;i++) {
			if(i != r1 && i != r2) {
				possibilities[i][column][doubles[0]-1] = false;
				possibilities[i][column][doubles[1]-1] = false;
				int single = checkSingle(i,column,possibilities,diagonal);
				if(single != -1) {
					setSingle(sudoku,possibilities,i,column,single,diagonal);
					AtLeastOneFound = true;
					log(String.format("Row %d Column %d was solved to be %d due to being the in the same column as the"
							+ "double pair %d & %d found in Row %d and Row %d within the column %d",i+1,column+1,single,doubles[0],doubles[1],
							r1+1,r2+1,column+1),sudoku);
				}
			}
		}
		
		if(!AtLeastOneFound) {
			log(String.format("Column %d found a double pair of %d & %d, but it did not immediately solve any box.",column+1,doubles[0],doubles[1]),sudoku);
		}
		
		return AtLeastOneFound;
	}
	
	private static boolean doublesPairLeftDiagonal(int k1,int k2,boolean[][][]possibilities,int[][]sudoku,int[] doubles,boolean diagonal) {
		boolean AtLeastOneFound = false;
		for(int k = 0;k < 9;k++) {
			if(k != k1 && k != k2) {
				possibilities[k][k][doubles[0]-1] = false;
				possibilities[k][k][doubles[1]-1] = false;
				int single = checkSingle(k,k,possibilities,diagonal);
				if(single != -1) {
					setSingle(sudoku,possibilities,k,k,single,diagonal);
					AtLeastOneFound = true;
					log(String.format("Row %d Column %d was solved to be %d due to being the in the same left diagonal as the"
							+ "double pair %d & %d found in Row/Column %d and Row/Column %d within the left diagonal.",k+1,k+1,single,doubles[0],doubles[1],
							k1+1,k2+1),sudoku);
				}
			}
		}
		
		if(!AtLeastOneFound) {
			log(String.format("The left diagonal found a double pair of %d & %d, but it did not immediately solve any box.",doubles[0],doubles[1]),sudoku);
		}
		
		return AtLeastOneFound;
	}
	
	//Double check/test this one
	private static boolean doublesPairRightDiagonal(int c1,int c2,boolean[][][]possibilities,int[][]sudoku,int[] doubles,boolean diagonal) {
		boolean AtLeastOneFound = false;
		int tempRow1 = 8 - c1;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
		int tempRow2 = 8 - c2;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
		for(int k = 8;k != -1; k--) {
			if(k != c1 && k != c2) {
				int tempRow3 = 8 - k;
				possibilities[tempRow3][k][doubles[0]-1] = false;
				possibilities[tempRow3][k][doubles[0]-1] = false;
				int single = checkSingle(tempRow3,k,possibilities,diagonal);
				if(single != -1) {
					setSingle(sudoku,possibilities,tempRow3,k,single,diagonal);
					AtLeastOneFound = true;
					log(String.format("Row %d Column %d was solved to be %d due to being the in the same right diagonal as the"
							+ "double pair %d & %d found in Row %d Column %d and Row %d Column %d within the left diagonal.",tempRow3+1,k+1,single,doubles[0],doubles[1],
							tempRow1+1,c1+1,tempRow2+1,c2+1),sudoku);
				}
			}
		}
		
		if(!AtLeastOneFound) {
			log(String.format("The right diagonal found a double pair of %d & %d, but it did not immediately solve any box.",doubles[0],doubles[1]),sudoku);
		}
		
		return AtLeastOneFound;
	}
	
	private static boolean doublesPairRow(int c1,int c2,int row,boolean[][][]possibilities,int[][]sudoku,int[] doubles,boolean diagonal) {
		boolean AtLeastOneFound = false;
		for(int j = 0;j < 9;j++) {
			if(j != c1 && j != c2) {
				possibilities[row][j][doubles[0]-1] = false;
				possibilities[row][j][doubles[1]-1] = false;
				int single = checkSingle(row,j,possibilities,diagonal);
				if(single != -1) {
					setSingle(sudoku,possibilities,row,j,single,diagonal);
					AtLeastOneFound = true;
					log(String.format("Row %d Column %d was solved to be %d due to being the in the same row as the"
							+ "double pair %d & %d found in Column %d and Column %d within the row %d",row+1,j+1,single,doubles[0],doubles[1],
							c1+1,c2+1,row+1),sudoku);
				}
			}
		}
		
		if(!AtLeastOneFound) {
			log(String.format("Row %d found a double pair of %d & %d, but it did not immediately solve any box.",row+1,doubles[0],doubles[1]),sudoku);
		}
		
		return AtLeastOneFound;
	}
	
	private static boolean doublesPairBox(int or,int oc,int r1,int c1,int r2,int c2,boolean[][][]possibilities,int[][]sudoku,int[] doubles,boolean diagonal) {
		boolean AtLeastOneFound = false;
		for(int i = or;i < or + 3;i++) {
			for(int j = oc;j < oc + 3;j++) {
				if((i != r1 && j != c1) && (i != r2 && j != c2) && (sudoku[i][j] == -1)) {
					possibilities[i][j][doubles[0]-1] = false;
					possibilities[i][j][doubles[1]-1] = false;
					int single = checkSingle(i,j,possibilities,diagonal);
					if(single != -1) {
						setSingle(sudoku,possibilities,i,j,single,diagonal);
						AtLeastOneFound = true;
						log(String.format("Row %d Column %d was solved to be %d due to being the in the same box as the"
								+ "double pair %d & %d found in Row %d Column %d and Row %d Column %d within the box.",i+1,j+1,single,doubles[0],doubles[1],
								r1+1,c1+1,r2+1,c2+1),sudoku);
					}
				}
			}
		}
		
		if(!AtLeastOneFound) {
			log(String.format("Row %d Column %d found a double pair of %d & %d inside its box, but it did not immediately solve any box.",r1+1,c1+1,doubles[0],doubles[1]),sudoku);
		}
		
		return AtLeastOneFound;
	}
	
	private static int possibilitiesSize(int row,int column,boolean[][][]possibilities){
		int size = 0;
		for(int i = 0;i < 9;i++) if(possibilities[row][column][i] == true) size++;
		return size;
	}
	
	
	private static boolean[] getStartingProbs(int[][] sudoku,int row,int column,boolean diagonal) {
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
		
		return valid;
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
	
	

}
