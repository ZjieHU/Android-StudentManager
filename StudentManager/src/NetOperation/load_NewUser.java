package NetOperation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.studentmanager.loginPage;

public class load_NewUser extends Thread {
	
	private Handler handler;
	
	public load_NewUser(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL(loginPage.HTTPURL+"/Adduser.action");
			Log.v("myTag", "OJ");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.connect();
			InputStream is = http.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String str = "";
			String s = "";
			while((str = bf.readLine()) != null) {
				s += str;
			}
			Message msg = new Message();
			msg.obj = s;
			handler.sendMessage(msg);
		}catch(Exception e) {
			Log.v("myTag", e.toString());
		}
		
	}
	
}
