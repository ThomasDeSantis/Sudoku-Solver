package com;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

class Sudoku extends Frame implements WindowListener{

	public SudokuBox boxes[];//Holds the sub boxes of the sudoku grid
	public int[][] sudokuArray;//The internal sudoku grid.
	boolean diagonal = false;//Stores whether or not we are checking a diagonal sudoku
	
	Sudoku(){
		addWindowListener(this);
		
		
		//Create the sub boxes
		//Holds the sudoku grid
		//Each box holds 9 slots to represent a single box on the sudoku grid.
		//They are arranged like this
		//0 1 2
		//3 4 5
		//6 7 8
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
		
		//The size of the window can be a flat 500x500
		setSize(500,500);
		setLayout(null);
		setVisible(true);
		
		
		setArray(testSudoku);//Set array to the test problem for now
		updateGraphics();//Update the GUI to represent the changes
		
		//The reset button will blank out the GUI and set all numbers to -1.
		Button blank = new Button("Reset");
		blank.setBounds(50,450,70,30);
		blank.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				blank();
			}
		});
		add(blank);
		
		//The backtracking button will solve the sudoku
		Button backtrack = new Button("Backtrack");
		backtrack.setBounds(150,450,70,30);
		backtrack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				backtrack();
			}
		});
		add(backtrack);

		//Add a button that will change whether or not the program is evaluating a diagonal sudoku
		Checkbox diagonalCheckbox = new Checkbox("Diagonal");
		diagonalCheckbox.setBounds(400,450,100,30);
		diagonalCheckbox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				diagonal = !diagonal;//Changes the checkbox to the opposite value Checked = checking for diagonals, not checked = not checking for diagonals
				
			}
			
		});
		add(diagonalCheckbox);
		


	}
	
	//A test sudoku array used to test the backtracking algorithm
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
	
	//setArray will assign the internal array to be a given array
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
	
	
	//Update the GUI to represent the internal array
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
        g2.setStroke(new BasicStroke(10)); //Set the size of the lines
        
        //Draw the vertical lines
        g2.draw(new Line2D.Float(175, 50, 175, 440));
        g2.draw(new Line2D.Float(315, 50, 315, 440));
        
        //Draw the horizontal lines
        g2.draw(new Line2D.Float(50, 175, 440, 175));
        g2.draw(new Line2D.Float(50, 315, 440, 315));
	}
	
	public static void main(String args[]){
		Sudoku f=new Sudoku();//Create a sudoku box
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
	
	//Complete blanks out the entire sudoku grid
	private void blank() {
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9; j++) {
				sudokuArray[i][j] = -1;//Turn everything to -1(which represents an unfilled square)
			}
		}
		updateGraphics();//Update the graphics to represent this
	}
	
	//Do the backtracking algorithm
	private void backtrack() {
		assignArray();//Update the internal 2d array to represent what the user has entered in the GUI
		//Reset the color to black for all places
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				changeRowColor(i,j,Color.black);
			}
		}
		if(verify()) {//First ensure the user entered a valid sudoku
			Backtracking.Backtrack(this);//Run the backtracking algorithm
			updateGraphics();//It is now solved, update the array to reflect it as such.
			return;
		}
		else {
			System.err.println("Error: Invalid sudoku");
			return;
		}
	}
	
	//This updates the internal 2d array to be consistent with what the user has entered in the GUI
	private void assignArray() {
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				sudokuArray[i][j] = retrieve(i,j);
			}
		}
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
		//A single sub box of sudoku refers to the group of 9 slots for numbers to be entered into
		//In sudoku, each one must hold one number (1 to 9), and this number must be unique among the numbers in the box.
		//The index is stored like this
		//012
		//345
		//678
		if(boxRow == 0 && boxColumn == 0) { //If the row in the box is 0, and the column is 0, then the index you are in the box is 0.
			boxIndex = 0;
		}
		else if(boxRow == 0 && boxColumn == 1) {//If the row in the box is 0, and the column is 1, then the index you are in the box is 1.
			boxIndex = 1;
		}
		else if(boxRow == 0 && boxColumn == 2) {//If the row in the box is 0, and the column is 2, then the index you are in the box is 2.
			boxIndex = 2;
		}
		else if(boxRow == 1 && boxColumn == 0) {//If the row in the box is 1, and the column is 0, then the index you are in the box is 3.
			boxIndex = 3;
		}
		else if(boxRow == 1 && boxColumn == 1) {//If the row in the box is 1, and the column is 1, then the index you are in the box is 4.
			boxIndex = 4;
		}
		else if(boxRow == 1 && boxColumn == 2) {//If the row in the box is 1, and the column is 2, then the index you are in the box is 5.
			boxIndex = 5;
		}
		else if(boxRow == 2 && boxColumn == 0) {//If the row in the box is 2, and the column is 0, then the index you are in the box is 6.
			boxIndex = 6;
		}
		else if(boxRow == 2 && boxColumn == 1) {//If the row in the box is 2, and the column is 1, then the index you are in the box is 7.
			boxIndex = 7;
		}
		else if(boxRow == 2 && boxColumn == 2) {//If the row in the box is 2, and the column is 2, then the index you are in the box is 2.
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
	
	//Retrieves the element at the row and column of the sudoku table
	//For information about how the sudoku table is stored read the prior insert function
	public void changeRowColor(int row, int column,Color c) {
		if(row >= 9 || column >= 9) {
			return;//Will cause a null pointer exception if you pass it greater than 9, so return
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
		//A single sub box of sudoku refers to the group of 9 slots for numbers to be entered into
		//In sudoku, each one must hold one number (1 to 9), and this number must be unique among the numbers in the box.
		//The index is stored like this
		//012
		//345
		//678
		if(boxRow == 0 && boxColumn == 0) { //If the row in the box is 0, and the column is 0, then the index you are in the box is 0.
			boxIndex = 0;
		}
		else if(boxRow == 0 && boxColumn == 1) {//If the row in the box is 0, and the column is 1, then the index you are in the box is 1.
			boxIndex = 1;
		}
		else if(boxRow == 0 && boxColumn == 2) {//If the row in the box is 0, and the column is 2, then the index you are in the box is 2.
			boxIndex = 2;
		}
		else if(boxRow == 1 && boxColumn == 0) {//If the row in the box is 1, and the column is 0, then the index you are in the box is 3.
			boxIndex = 3;
		}
		else if(boxRow == 1 && boxColumn == 1) {//If the row in the box is 1, and the column is 1, then the index you are in the box is 4.
			boxIndex = 4;
		}
		else if(boxRow == 1 && boxColumn == 2) {//If the row in the box is 1, and the column is 2, then the index you are in the box is 5.
			boxIndex = 5;
		}
		else if(boxRow == 2 && boxColumn == 0) {//If the row in the box is 2, and the column is 0, then the index you are in the box is 6.
			boxIndex = 6;
		}
		else if(boxRow == 2 && boxColumn == 1) {//If the row in the box is 2, and the column is 1, then the index you are in the box is 7.
			boxIndex = 7;
		}
		else if(boxRow == 2 && boxColumn == 2) {//If the row in the box is 2, and the column is 2, then the index you are in the box is 2.
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
		
		boxes[boxIndex].changeColor(finalIndex,c);//Then get the value
	}
	
	//This function returns the lowest number that could fit in the box at index [row][column] that is greater than min.
	public int getNextValidBox(int sudoku[][],int row, int column,int min ) {
		boolean[] valid = new boolean[9];//Create an array that stores boolean. Each one says its index+1 is a valid number for the box.
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
				for(int k = 8;k != -1; k--) {//TODO:Proper logic. Also maybe == 8 on the if statement.
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
		
		//Return the smallest valid number greater than the min.
		for(int i = min-1; i < 9;i++) {
			if(i <= -1) {
				i = 0;//If the min is -1, then assume you actually want to start at 0 or else you will error.
			}
			if(valid[i]) {//Once you find the first valid number you will return it
			return i + 1;//Return +1 because this indexes at 0(0 to 8), but sudoku indexes at 1 (1 to 9)
			}
		}
		
		return -1;//If nothing was valid, return -1.
	}
	
	//Function verifies that a sudoku is valid
	//It does this by making sure there are no slots that by default return no possible value
	//TODO: Indicate index that failed
	//TODO: Make sure no index has the same value in the same row/column/box
	public boolean verify() {
		boolean verified = true;//Assume the verification will return true
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(sudokuArray[i][j] == -1) {//Only check indices without a value
					if(getNextValidBox(sudokuArray,i,j,1) == -1) {
						verified =  false;//If there is no possible valid box you entered an invalid sudoku
						changeRowColor(i,j,Color.red);//Change the color to red
					}
				}
				else {
					if(!checkEnteredInvalid(i,j)) {
						
						verified = false;
						changeRowColor(i,j,Color.red);//Change the color to red
					}
				}
			}
		}
		//Create a dialog box informing the user they have set an invalid sudoku
		if(!verified) {
			Frame temp = new Frame();
			final Dialog sError = new Dialog(temp,"Sudoku Error",true);
			sError.setLayout(new FlowLayout());
			Button close = new Button("OK!");
			close.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					sError.dispose();
				}
			});
			sError.add(new Label("Warning:Invalid sudoku."));
			sError.add(new Label("Ensure you have entered it correctly."));
			sError.add(close);
			sError.setSize(250,130);
			sError.setVisible(true);
		}
		return verified;
	}
	
	//This checks if a known index has a matching value in its own row/column/box/or diagonal (if applicable)
	//Used in the verification function to make sure none of those errors exist at any point
	public boolean checkEnteredInvalid(int row, int column){
		int check = sudokuArray[row][column];
		
		//Check for all numbers in column that are not you
		for(int i = 0;i < 9; i++) {
			if(i != row && sudokuArray[i][column] == check) {//If you are in a different row and same column and your number is present
				return false;
			}
		}
		
		//Check for all numbers in row that are not you
		for(int j = 0;j < 9; j++) {
			if(j != column && sudokuArray[row][j] == check) {//If you are in a different column and same row and your number is present
				return false;
			}
		}
		
		
		//This section checks for diagonals
		//Only do this if the user has entered a diagonal sudoku and checked the diagonal box
		if(diagonal) {
			//Check for all numbers along the diagonal, if you are across the diagonal
			//This one checks for the left diagonal
			if(row == column) {//The row equaling the column implies you are along the diagonal
				for(int k = 0;k < 9; k++) {
					if(k != column && sudokuArray[k][k] == check) {//If something not you in the left diagonal equals you then you are false
						return false;
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
					if(k != column && sudokuArray[tempRow][k] == -1) {//Check for the right diagonal
						return false;
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
				if(sudokuArray[i][j] == check) {//Make sure it is a valid, set box
					if(i != row || j != column) {//Make sure you are not comparing it to yourself, at your coordinates
						return false;
					}
				}
			}
		}
		
		return true;//If you find no errors, then you can return true
	}
}