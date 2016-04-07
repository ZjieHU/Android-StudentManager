package com.modle;

import java.util.ArrayList;

public class studentArray {
	private studentArray mStudentArray;
	public studentArray getStudentArrayOBJ() {
		if(mStudentArray == null) 
			mStudentArray = new studentArray();
		return mStudentArray;
	}
	
	public ArrayList<String> getStudentArray() {
		ArrayList<String> lists = new ArrayList<String>();
		return lists;
	}
	
	public ArrayList<String> getStudents() {
		ArrayList<String> lists = new ArrayList<String>();
		return lists;
	}
}
