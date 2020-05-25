package com;

import java.awt.TextField;
import java.awt.Frame;

public class SudokuBox{
	private TextField box[];
	SudokuBox(int x, int y,Sudoku s){
		//Create a matrix of 9 boxes
		//This will represent one sudoku box
		box = new TextField[9];
		box[0] = new TextField("");
		box[0].setBounds(x, y, 30, 30);
		box[1] = new TextField("");
		box[1].setBounds(x+40, y, 30, 30);
		box[2] = new TextField("");
		box[2].setBounds(x+80, y, 30, 30);
		box[3] = new TextField("");
		box[3].setBounds(x, y+40, 30, 30);
		box[4] = new TextField("");
		box[4].setBounds(x+40, y+40, 30, 30);
		box[5] = new TextField("");
		box[5].setBounds(x+80, y+40, 30, 30);
		box[6] = new TextField("");
		box[6].setBounds(x, y+80, 30, 30);
		box[7] = new TextField("");
		box[7].setBounds(x+40, y+80, 30, 30);
		box[8] = new TextField("");
		box[8].setBounds(x+80, y+80, 30, 30);
		
		for(int i = 0;i < 9;i++) {
			s.add(box[i]);
		}
	}
	
	public void set(int boxI, int val) {
		box[boxI].setText(Integer.toString(val));
	}
	
	public int get(int boxI) {
		return Integer.valueOf(box[boxI].getText());
	}
}
