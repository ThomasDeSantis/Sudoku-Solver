package com;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

class Sudoku extends Frame implements WindowListener{

	public SudokuBox boxes[];
	public int[][] sudokuArray;
	
	
	Sudoku(){
		addWindowListener(this);
		
		
		boxes = new SudokuBox[9];
		boxes[0] = new SudokuBox(50,50,this);
		boxes[1] = new SudokuBox(190,50,this);
		boxes[2] = new SudokuBox(330,50,this);
		
		boxes[3] = new SudokuBox(50,190,this);
		boxes[4] = new SudokuBox(190,190,this);
		boxes[5] = new SudokuBox(330,190,this);
		
		boxes[6] = new SudokuBox(50,330,this);
		boxes[7] = new SudokuBox(190,330,this);
		boxes[8] = new SudokuBox(330,330,this);
		
		setSize(500,500);
		setLayout(null);
		setVisible(true);
		
		
		/*
		int current = 0;
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j <9;j++) {
				insert(current,i,j);
				current++;
			}
		}
		*/
		
		setArray(testSudoku);
		updateGraphics();
		//getNextValidBox(testSudoku,3,3);
		Backtracking.Backtrack(this);
		


	}
	
	int[][] testSudoku = {
			{  3, -1, -1,  8, -1,  1, -1, -1 , 2},
			{  2, -1,  1, -1,  3, -1,  6, -1,  4},
			{ -1, -1, -1,  2, -1,  4, -1, -1, -1},
			{  8, -1,  9, -1, -1, -1,  1, -1,  6},
			{ -1,  6, -1, -1, -1, -1, -1,  5, -1},
			{  7, -1,  2, -1, -1, -1,  4, -1,  9},
			{ -1, -1, -1,  5, -1,  9, -1, -1, -1},
			{  9, -1,  4, -1,  8, -1,  7, -1,  5},
			{  6, -1, -1,  1, -1,  7, -1, -1,  3}
			
	};
	
	private void setArray(int[][] sudokuToBeSolved) {
		//First ensure the given array is valid
		if(sudokuToBeSolved.length != 9) {
			return;//This must be a 9x9 cube
		}
		else {
			for(int i = 0;i < 9;i++) {
				if (sudokuToBeSolved[i].length != 9) {
					return;//Check all index of array to make sure the length is 9
				}
			}
		}
		
		sudokuArray = sudokuToBeSolved.clone();
	}
	
	public void updateGraphics() {
		
		//For every index of the 9x9 sudoku 2d array
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				insert(sudokuArray[i][j],i,j);//Insert it into the visualized array
			}
		}
		return;
	}
	
	
	//Draw grid lines for the board
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.draw(new Line2D.Float(175, 50, 175, 440));
        g2.draw(new Line2D.Float(315, 50, 315, 440));
        
        g2.draw(new Line2D.Float(50, 175, 440, 175));
        g2.draw(new Line2D.Float(50, 315, 440, 315));
	}
	
	public static void main(String args[]){
	
		Sudoku f=new Sudoku();
		
	}
	
	public void windowActivated(WindowEvent e) {}  
	public void windowClosed(WindowEvent e) {}  

	public void windowClosing(WindowEvent e) {  
	    dispose();  
	}  
	
	public void windowDeactivated(WindowEvent e) {}  
	public void windowDeiconified(WindowEvent e) {}  
	public void windowIconified(WindowEvent e) {}  
	public void windowOpened(WindowEvent arg0) {}  
	
	public void initTable(String[][] table) {
		
	}
	
	
	//The row and column passed to this function represent how you would look at a sudoku puzzle as if it were a [9][9] array
	//For example, passing row = 0 and column = 0 would set the value of the top left box to the val
	//Another example, passing row = 8 and column = 8 would set the value of the bottom right box to the val
	//A visual representation would be
	//ROW INDEX:                 0      1        2
	//                        _______________________
	//COLUMN INDEX:     0     |  0  |   1   |    2  |
	//                        |_____|_______|_______|
	//                  1     |  3  |   4   |    5  |
	//                        |_____|_______|_______|
	//                  2     |  6  |   7   |    8  |
	//                        |_____|_______|_______|
	public void insert(int val,int row, int column) {
		if(row >= 9 || column >= 9) {
			return;//Will cause a null pointer exception if you pass it greater than 9
		}
		int boxRow;//For box row and column imagine instead of 9 tables, they are instead arranged in a [3][3] array
		int boxColumn;//An element inside the top left box would be at 0,0, an element at the bottom right would be 3,3
		int boxIndex = 0;//These will be converted to get the index of the box that it should be in when used with boxes[]
		
		if(row < 3) {//If row is less than three, then it is within the top row of the sudoku puzzle
			boxRow = 0;
		}
		else if(row < 6) {//If it is 3 <= x < 6 then it is in the middle row
			boxRow = 1;
		}
		else {
			boxRow = 2; //Otherwise it is in the bottom row
		}
		
		if(column < 3) {//If the column is less than three it is within the leftmost column column
			boxColumn = 0;
		}
		else if(column < 6) {//If it is 3 <= x < 6 then it is in the middle column
			boxColumn = 1;
		}
		else {//Otherwise it is in the rightmost column
			boxColumn = 2;
		}
		
		//Referencing the chart, match the box row and column to get the correct index
		if(boxRow == 0 && boxColumn == 0) {
			boxIndex = 0;
		}
		else if(boxRow == 0 && boxColumn == 1) {
			boxIndex = 1;
		}
		else if(boxRow == 0 && boxColumn == 2) {
			boxIndex = 2;
		}
		else if(boxRow == 1 && boxColumn == 0) {
			boxIndex = 3;
		}
		else if(boxRow == 1 && boxColumn == 1) {
			boxIndex = 4;
		}
		else if(boxRow == 1 && boxColumn == 2) {
			boxIndex = 5;
		}
		else if(boxRow == 2 && boxColumn == 0) {
			boxIndex = 6;
		}
		else if(boxRow == 2 && boxColumn == 1) {
			boxIndex = 7;
		}
		else if(boxRow == 2 && boxColumn == 2) {
			boxIndex = 8;
		}
		
		//Now you must find the correct index within the sudokuBox class
		//It is stored like
		// 0 1 2
		// 3 4 5
		// 6 7 8
		int localR = row % 3;//The local row will represent which row the element is on
		int localC = column % 3;//The local column will represent which column it is in
		int finalIndex = (localR * 3) + localC;//The final index is calculated by multiplying the local row by three (as there are three rows)
		//And the adding the local column
		//That will give you the index within the sudokuBox where the element you requested is stored
		
		boxes[boxIndex].set(finalIndex, val);//Then get the value
	}
	
	//Retrieves the element at the row and column of the sudoku table
	//For information about how the sudoku table is stored read the prior insert function
	public int retrieve(int row, int column) {
		if(row >= 9 || column >= 9) {
			return -1;//Will cause a null pointer exception if you pass it greater than 9
		}
		int boxRow;//For box row and column imagine instead of 9 tables, they are instead arranged in a [3][3] array
		int boxColumn;//An element inside the top left box would be at 0,0, an element at the bottom right would be 3,3
		int boxIndex = 0;//These will be converted to get the index of the box that it should be in when used with boxes[]
		
		if(row < 3) {//If row is less than three, then it is within the top row of the sudoku puzzle
			boxRow = 0;
		}
		else if(row < 6) {//If it is 3 <= x < 6 then it is in the middle row
			boxRow = 1;
		}
		else {
			boxRow = 2; //Otherwise it is in the bottom row
		}
		
		if(column < 3) {//If the column is less than three it is within the leftmost column column
			boxColumn = 0;
		}
		else if(column < 6) {//If it is 3 <= x < 6 then it is in the middle column
			boxColumn = 1;
		}
		else {//Otherwise it is in the rightmost column
			boxColumn = 2;
		}
		
		//Referencing the chart, match the box row and column to get the correct index
		if(boxRow == 0 && boxColumn == 0) {
			boxIndex = 0;
		}
		else if(boxRow == 0 && boxColumn == 1) {
			boxIndex = 1;
		}
		else if(boxRow == 0 && boxColumn == 2) {
			boxIndex = 2;
		}
		else if(boxRow == 1 && boxColumn == 0) {
			boxIndex = 3;
		}
		else if(boxRow == 1 && boxColumn == 1) {
			boxIndex = 4;
		}
		else if(boxRow == 1 && boxColumn == 2) {
			boxIndex = 5;
		}
		else if(boxRow == 2 && boxColumn == 0) {
			boxIndex = 6;
		}
		else if(boxRow == 2 && boxColumn == 1) {
			boxIndex = 7;
		}
		else if(boxRow == 2 && boxColumn == 2) {
			boxIndex = 8;
		}
		
		//Now you must find the correct index within the sudokuBox class
		//It is stored like
		// 0 1 2
		// 3 4 5
		// 6 7 8
		int localR = row % 3;//The local row will represent which row the element is on
		int localC = column % 3;//The local column will represent which column it is in
		int finalIndex = (localR * 3) + localC;//The final index is calculated by multiplying the local row by three (as there are three rows)
		//And the adding the local column
		//That will give you the index within the sudokuBox where the element you requested is stored
		
		return boxes[boxIndex].get(finalIndex);//Then get the value
	}
	
	public int getNextValidBox(int sudoku[][],int row, int column,int min ) {
		boolean[] valid = new boolean[9];
		java.util.Arrays.fill(valid, true);//Set all to be valid at first
		
		//Check for all numbers in column that are not you
		for(int i = 0;i < 9; i++) {
			int temp = sudoku[i][column];
			if(i != row && temp != -1) {//As long as youre not comparing it to yourself or an invalid box
				valid[temp - 1] = false;//It has seen it, and thus is false.
			}
		}
		
		//Check for all numbers in row that are not you
		for(int j = 0;j < 9; j++) {
			int temp = sudoku[row][j];
			if(j != column && temp != -1) {//As long as youre not comparing it to yourself
				valid[temp - 1] = false;//It has seen it, and thus is false.
			}
		}
		
		
		//These commented out sections only apply to diagonal sudoku
		/*
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
			for(int k = 8;k != -1; k--) {//TODO:Proper logic. Also maybe == 8 on the if statement.
				int tempRow = 8 - k;//The row of the right diagonal is on the opposite side as far. 8 - k yields this result.
									//If k, the column, was say 2, the row would have to be 6.
				int temp = sudoku[tempRow][k];
				if(k != column && temp != -1) {//As long as youre not comparing it to yourself
					valid[temp - 1] = false;//It has seen it, and thus is false.
				}
			}
		}
		*/
		
		
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
		//Return the smallest valid position
		System.out.print("(");
		for(int i = 0; i < 9;i++) {
			if(valid[i]) {
				System.out.print(Integer.toString(i+1));
			}
		}
		System.out.print(")\n");

		
		for(int i = min-1; i < 9;i++) {
			if(i <= -1) {
				i = 0;
			}
			if(valid[i]) {
			return i + 1;
			}
		}
		
		return -1;
	}
	

}