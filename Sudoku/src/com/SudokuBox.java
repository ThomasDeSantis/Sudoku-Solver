package com;

import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.Color;
import java.awt.Font;

public class SudokuBox{
	private TextField box[];
	static Font textFont = new Font("TimesNewRoman",Font.PLAIN,20);
	SudokuBox(int x, int y,Sudoku s){
		//Create a matrix of 9 boxes
		//This will represent one sudoku box
		
		
		
		box = new TextField[9];
		
		//Intialize all boxes
		for(int i = 0;i < 9;i++) {
			box[i] = new TextField("");
			box[i].setFont(textFont);
			final int temp = i;//Must use final variable inside of enclosed scope (below)
			//Text listener used to reset color if it was set to red because of an error
			box[i].addTextListener( new TextListener()
			{	
				@Override
				public void textValueChanged(TextEvent e) {
					if ( e.getID() == TextEvent.TEXT_VALUE_CHANGED )
					{
						box[temp].setForeground(Color.black);//if you change the color, set the color back to black
					}
					
				}
			});
		}
		
		//Set box boundaries
		//9x9 matrix seperated by 40 pixels of difference
		box[0].setBounds(x, y, 30, 30);
		box[1].setBounds(x+40, y, 30, 30);
		box[2].setBounds(x+80, y, 30, 30);
		box[3].setBounds(x, y+40, 30, 30);
		box[4].setBounds(x+40, y+40, 30, 30);
		box[5].setBounds(x+80, y+40, 30, 30);
		box[6].setBounds(x, y+80, 30, 30);
		box[7].setBounds(x+40, y+80, 30, 30);
		box[8].setBounds(x+80, y+80, 30, 30);
		
		for(int i = 0;i < 9;i++) {
			
			s.add(box[i]);
		}
	}
	
	//Given the index of a box and a value,
	//Set the index to that value
	public void set(int boxI, int val) {
		if(val == -1) {
			box[boxI].setText("");//-1 represents the null number, so set the text to nothing
		}
		else {
			box[boxI].setText(Integer.toString(val));//Otherwise just set the text to the given value
		}
		
		
	}
	
	//Get, given an index, will return the value inside the slot
	public int get(int boxI) {
		if(box[boxI].getText().isBlank()) {
			return -1;
		}
		else {
			return Integer.valueOf(box[boxI].getText());
		}
	}
	
	public void errorColor(int boxI) {
		box[boxI].setForeground(Color.red);
	}
}
