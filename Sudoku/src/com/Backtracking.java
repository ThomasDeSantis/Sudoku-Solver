package com;

import java.util.Stack;

public class Backtracking {

	
	public static void Backtrack(Sudoku s) {
		//Will represent in bools which indices are known at the start
		//True means it is already filled in
		//False means it is not filled in
		boolean[][] knownSpaces = new boolean[9][9];
		getKnown(knownSpaces,s.sudokuArray);
		
		//Stores the last index with an unknown [row,column]
		int[] lastIndex = new int[2];
		getLastUnknown(lastIndex,knownSpaces);
		
		Stack<int[]> priorActions = new Stack<int[]>();
		int i = 0, j = 0;
		while(i <= 8 && j <= 8) {//TODO: Proper last index
			//System.out.println("I J:" + Integer.toString(i) + " " + Integer.toString(j));
			if(knownSpaces[i][j] == false) {
				//s.updateGraphics();
				if(s.sudokuArray[i][j] < 10) {
					//System.out.println("Viewing:" + Integer.toString(s.sudokuArray[i][j]) + "@" +Integer.toString(i) + " " + Integer.toString(j));
					int newElement = s.getNextValidBox(s.sudokuArray, i, j,s.sudokuArray[i][j]+1);
					//System.out.println("Considering:" + Integer.toString(newElement));
			
					if(newElement == -1) {
						int tempArray[] = priorActions.pop();
						reset(tempArray[0],i,tempArray[1],j,s.sudokuArray,knownSpaces);
						s.sudokuArray[tempArray[0]][tempArray[1]] = tempArray[2];
						//s.updateGraphics();
						i = tempArray[0];
						j = tempArray[1];
						//System.out.println("Popped:" + Integer.toString(tempArray[0]) + " " +Integer.toString(tempArray[1]) + " " + Integer.toString(tempArray[2]));
					}
					else if(newElement == 10) {
						while (!priorActions.isEmpty())
						{
							int tempArray[] = priorActions.pop();
							//System.err.println(Integer.toString(tempArray[0]) + " " +Integer.toString(tempArray[1]) + " " + Integer.toString(tempArray[2]));
						}
						System.exit(-1);
					}
					else  {
						s.sudokuArray[i][j] = newElement;
						//System.out.println("Pushed:" + Integer.toString(i) + " " +Integer.toString(j) + " " + Integer.toString(newElement));
						priorActions.push(new int[] {i,j,newElement});
						if(j == 8) {
							j = 0;
							i++;
						}
						else {
							j++;
						}
					}
				}
				else {
					/*System.out.println("Error: indices Row:" + Integer.toString(i) + " Column: " + Integer.toString(j)+
							" Reached above 9.");*/
					return;
				}
			}
			else {
				if(j == 8) {
					j = 0;
					i++;
				}
				else {
					j++;
				}
			}
		}
		//s.updateGraphics();
		//System.out.println(s.sudokuArray[8][8]);

	}
	
	//Given a blank 9x9 array of booleans, and a filled in array of ints
	//Insert a bool 'false' where ever  the matching index on the int array is -1
	//-1 represents a box that is not filled in
	//Otherwise represent it as true, meaning that index was known to be that element from the start.
	private static void getKnown(boolean[][] knownSpaces,int[][] sudokuArray) {
		for(int i =0; i < 9; i++) {
			for(int j = 0; j < 9;j++) {
				if(sudokuArray[i][j] == -1) {
					knownSpaces[i][j] = false;
				}
				else {
					knownSpaces[i][j] = true;
				}
			}
		}
	}
	
	private static void getLastUnknown(int[] lastIndex,boolean[][] knownSpaces) {
		for(int i =8; i >= 0; i--) {
			for(int j = 8; j >= 0;j--) {
				if(knownSpaces[i][j] == false) {
					lastIndex[0] = i;
					lastIndex[1] = j;
					return;
				}
			}
		}
		//TODO: Condition for if it is already solved (IE no falses)
	}
	
	private static void reset(int row1,int row2, int column1, int column2,int[][]sudokuArray,boolean[][]knownSpaces) {
		//System.out.println("Reset:" + Integer.toString(row1) + " " +Integer.toString(column1) + " " + Integer.toString(row2) + " " +Integer.toString(column2));
		for(int i = row1;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(i == row1 && j == 0) {
					j = column1;
				}
				else if(!knownSpaces[i][j]) {
					sudokuArray[i][j] = -1;
					//System.out.println("Reset:" + Integer.toString(i) + " " +Integer.toString(j));
				}
				if(i == row2 && j == column2) {
					return;
				}
				
			}
		}
	}
	
}

