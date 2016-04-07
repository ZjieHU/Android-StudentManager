package loadOperation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class load_MSGRecord {
	
	private Activity activity;
	public load_MSGRecord(Activity activity) {
		this.activity = activity;
	}
	
	public ArrayList<String> getMSGRecored(String name) {
		ArrayList<String> msg = new ArrayList<String>();
		SharedPreferences sharedPreferences = ((Context)activity).getSharedPreferences(name, 0);
		//0是默认PRIVATE
		String str = "";
		int i = 0;
		for(; i < 5; i++) {
			str = sharedPreferences.getString(i+"", null);
			if(str != null){
				Log.v("myTag", "取出的值是"+i);
				msg.add(str);
			}
		}
		return msg;
	}
	
	public void saveMSGRecord(ArrayList<String> txt, String name) {
		SharedPreferences sharedPreferences = ((Context)activity).getSharedPreferences(name, 0);
		SharedPreferences.Editor edit = sharedPreferences.edit();
		int i = 0;
		int count = txt.size() - 1;
		for(;i < 5 && count >= 0; i++) {
			if(count > 4) {
				edit.putString(i+"", txt.get((count++)-4));
			}
			else {
				edit.putString(i+"", txt.get(i));
				count--;
			}
		}
		Log.v("myTag", "save i的值"+i);
		
		edit.commit();
	}
	
	public void delete_MSGRecord(String key, String name) {
		SharedPreferences sharedPreferences = ((Context)activity).getSharedPreferences(name, 0);
		SharedPreferences.Editor edit = sharedPreferences.edit();
		for(int i = 0; i <  5; i++) {
			String str = sharedPreferences.getString(i+"", null);
			if(str != null && str.equals(key) ) {
				edit.remove(i+"");
				Log.v("myTag", "delete 的值是"+i);
				break;
			}
		}
		edit.commit();
	}
}
