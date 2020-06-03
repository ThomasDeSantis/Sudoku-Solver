package com;

import java.util.Stack;

public class Backtracking {

	
	public static void Backtrack(Sudoku s) {
		//Will represent in bools which indices are known at the start
		//True means it is already filled in
		//False means it is not filled in
		boolean[][] knownSpaces = new boolean[9][9];
		getKnown(knownSpaces,s.sudokuArray);//Store which indices we already know the correct number for (the already set)
		
		
		Stack<int[]> priorActions = new Stack<int[]>();//This stack will store the prior actions
		int i = 0, j = 0;//These will be the iterators
		while(i <= 8 && j <= 8) {//Iterate over the array
			if(knownSpaces[i][j] == false) {//Known spaces already have a determined number, so you only have to do undetermined slots
				if(s.sudokuArray[i][j] < 10) {//Anything over 10 should never occur, will result in an error
					
					//Get the smallest valid number the slot could hold that is larger than its current value
					//You must consider only the ones smaller than the current value because otherwise it would just get its current value
					//(As long as its not -1)
					//If its not -1 it would get stuck in an infinite loop, repeating the same number.
					int newElement = s.getNextValidBox(s.sudokuArray, i, j,s.sudokuArray[i][j]+1);
					
			
					if(newElement == -1) {//If -1 is returned that means there are no valid numbers, meaning an error was made at one point
						int tempArray[] = priorActions.pop();//Pop the top state off the stack
						reset(tempArray[0],i,tempArray[1],j,s.sudokuArray,knownSpaces);//Reset all non-known numbers between the popped off state and the current state
						s.sudokuArray[tempArray[0]][tempArray[1]] = tempArray[2];//Set the value of the popped off index to its old number so we know the number on it that failed
						i = tempArray[0];//Change i and j to return to the popped of state's i and j value
						j = tempArray[1];
					}
					else  {//If the returned value from getNextValidBox is not -1, you found a valid route to continue
						s.sudokuArray[i][j] = newElement;//Set the internal array to be the new number
						priorActions.push(new int[] {i,j,newElement});//Push the state you just went into onto the stack
						if(j == 8) {//If j (the column) is 8 you are at the final column of the row
							j = 0;//Thus you should go to the beginning of the next row
							i++;
						}
						else {
							j++;//If you arent at the end of the row continue to the next column of the same row
						}
					}
				}
				else {//If you somehow got an element larger than 10 then you are in an error state, print an error message and exit
					System.out.println("Error: indices Row:" + Integer.toString(i) + " Column: " + Integer.toString(j)+
							" Reached above 9.");
					System.exit(-1);
				}
			}
			else {//If you are on an already known space just continue to the next slot
				if(j == 8) {//If j (the column) is 8 you are at the final column of the row
					j = 0;//Thus you should go to the beginning of the next row
					i++;
				}
				else {
					j++;//If you arent at the end of the row continue to the next column of the same row
				}
			}
		}
	}
	
	//Given a blank 9x9 array of booleans, and a filled in array of ints
	//Insert a bool 'false' where ever  the matching index on the int array is -1
	//-1 represents a box that is not filled in
	//Otherwise represent it as true, meaning that index was known to be that element from the start.
	private static void getKnown(boolean[][] knownSpaces,int[][] sudokuArray) {
		for(int i =0; i < 9; i++) {//Iterate through the 2d array
			for(int j = 0; j < 9;j++) {
				if(sudokuArray[i][j] == -1) {//If the initial array has -1 that means it is not known
					knownSpaces[i][j] = false;
				}
				else {
					knownSpaces[i][j] = true;//If it is anything but -1 it is already known
				}
			}
		}
	}
	
	//Turn all indices that were not already known at the outset of the array back to -1
	//This will be called if you reach a state where there are no possible way to continue
	//This means you must set all non-known indices back to -1 so you can retry with a different number
	private static void reset(int row1,int row2, int column1, int column2,int[][]sudokuArray,boolean[][]knownSpaces) {
		for(int i = row1;i < 9;i++) {//Iterate starting at row1,column1 and continue until you reach row2,column2
			for(int j = 0;j < 9;j++) {//Must start at 0 so columns past the initial rows will start at 0
				if(i == row1 && j == 0) {//This indicates you are at row1 column1, so set j to equal column1
					j = column1;
				}
				else if(!knownSpaces[i][j]) {//If its not a known space set it back to -1
					sudokuArray[i][j] = -1;
				}
				if(i == row2 && j == column2) {//Once you are at row2,column2 then you can return
					return;
				}
				
			}
		}
	}
	
}

