package loadOperation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

@SuppressLint("SdCardPath")
public class loadPicture {
	
	@SuppressLint("SdCardPath")
	private String path = "/sdcard/image/header.jpg";
	private Activity activity;
	
	public loadPicture(Activity activity) {
		this.activity = activity;
	}
	
	public Bitmap getBitmap() {
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			if(file.exists()) {
				bitmap = BitmapFactory.decodeFile(path);
			}
		}catch(Exception e) {
			Log.v("myTag", e.toString());
		}
		return bitmap;
	}
	
	@SuppressLint("SdCardPath")
	public void saveBitmap(Bitmap bitmap) {
		File file = new File("/sdcard/image");
		try {
			if(!file.exists()) {
				file.mkdir();
			}
			file = new File("/sdcard/image/header.jpg");
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.v("myTag", e.toString()+"  saveBitmap"); 
		}
	}
	
	
	public String[] getLoginInfo() {
		String[] loginInfo = {"",""};
		try {
			FileInputStream fis = activity.openFileInput("username.txt");
			byte[] b = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while(fis.read(b) > 0) {
				baos.write(b);
			}
			baos.close();
			fis.close();
			loginInfo[0] = baos.toString();
			fis = activity.openFileInput("password.txt");
			b = new byte[1024];
			baos = new ByteArrayOutputStream();
			while(fis.read(b) > 0) {
				baos.write(b);
			}
			baos.close();
			fis.close();
			loginInfo[1] = baos.toString();
		}catch(Exception e) {
			Log.v("myTag", e.toString()+"getLogin");
		}
		return loginInfo;
	}
	
	public void saveLoginInfo(String username, String password ) {
		try {
			FileOutputStream fis = activity.openFileOutput("username.txt",
					Activity.MODE_PRIVATE);
			fis.write(username.getBytes());
			fis.close();
			fis = activity.openFileOutput("password.txt",
					Activity.MODE_PRIVATE);
			fis.write(password.getBytes());
			fis.close();
		}catch(Exception e) {
			Log.v("myTag", e.toString() +"saveLogin");
		}
	}
}
