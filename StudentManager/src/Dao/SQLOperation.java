package Dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.modle.Student;

public class SQLOperation {
	
	private static Context context;
	private static SQLOperation sqlOperation;
	private SQLiteDatabase db;
	
	public static SQLOperation getSQLOperationOBJ(Context c ) {
		if(sqlOperation == null) {
			sqlOperation = new SQLOperation();
			sqlOperation.context = c;
		}
		return sqlOperation;
	}
	
	public void CreateDatabase() {
		db = context.openOrCreateDatabase("StudentManager", context.MODE_PRIVATE, null);
	}
	
	public void CreateTable() {
		Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='stu'"
				, null);
		if(c.moveToNext()) {
			if(c.getInt(0) == 0) {
				db.execSQL("CREATE TABLE stu (id INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ "name VARCHAR, introduce VARCHAR, stu_id VARCHAR, Array_Name VARCHAR)");
			}
		}
		c.close();
	}
	
	public void add(Student s) {
		Cursor c = db.rawQuery("SELECT * FROM stu WHERE stu_id = ?",new String[]{s.getStu_id()});
		if(c.moveToNext()) {
			ContentValues cv = new ContentValues();
			cv.put("stu_id", s.getStu_id());
			cv.put("introduce", s.getIntroduce());
			cv.put("name", s.getName());
			db.update("stu", cv, "name=?,stu_id=?,introduce=?,Array_Name=?", new String[] {
					s.getName(), s.getStu_id(), s.getIntroduce(), s.getArray_Name()
			});
		}else {
			db.execSQL("INSERT INTO stu VALUES (NULL,?,?,?,?)",new String[]{
					s.getName(),s.getIntroduce(),s.getStu_id(),s.getArray_Name()
			});
		}
		c.close();
	}
	
	public int getCount () {
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM stu",null);
		if(c.moveToNext()) {
			return c.getInt(0);
		}else {
			c.close();
			return 0;
		}
		
	}
	
	public ArrayList<Student> getStudentList() {
		ArrayList<Student> student = new ArrayList<Student>();
		
		Cursor c = db.rawQuery("SELECT * FROM stu", null);
		while(c.moveToNext()) {
			Student s = new Student();
			s.setIntroduce(c.getString(c.getColumnIndex("introduce")));
			s.setArray_Name(c.getString(c.getColumnIndex("Array_Name")));
			s.setName(c.getString(c.getColumnIndex("name")));
			s.setStu_id(c.getString(c.getColumnIndex("stu_id")));
			student.add(s);
		}
		
		c.close();
		return student;
	}
	
	public ArrayList<String> getArrayName() {
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT * FROM stu",null);
		while(c.moveToNext()) {
			list.add(c.getString(c.getColumnIndex("Array_Name")));
		}
		return list;
	}
	
	public void closeResource() {
		if(db.isOpen()) {
			db.close();
		}
	}
}
