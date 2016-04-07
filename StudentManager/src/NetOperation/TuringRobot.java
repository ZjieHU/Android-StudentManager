package NetOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TuringRobot extends  Thread {
	
	private String url = "http://www.tuling123.com/openapi/api";
	private String key = "592ada5a5a680f59c51d82b34274d4b1";
	private Handler handler;
	private String info;
	
	public TuringRobot(Handler h, String i) {
		this.handler = h;
		this.info = i;
	}
	
	public String getIngo(String info) {
		String answer = new String();
		try {
			url = url +"?key=" + key +"&info=" + new String(info.getBytes("UTF-8"),"ISO-8859-1");
			URL u = new URL(url);
			HttpURLConnection http = (HttpURLConnection) u.openConnection();
			http.setRequestMethod("GET");
			http.connect();
			InputStream is = http.getInputStream();	
			BufferedReader bf = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String str = "";
			while((str = bf.readLine()) != null) {
				answer += str;
			}
			JSONObject json = new JSONObject(answer);
			answer = json.getString("text");
		}catch(ProtocolException e) {
			Log.v("myTag", e.toString()+" Protocol");
		}catch(IOException e) {
			Log.v("myTag", e.toString()+" IO");
		}catch(JSONException e) {
			Log.v("myTag", e.toString()+" JSON");
		}
		Log.v("myTag", answer);
		return answer + "1";
	}
	
	@Override
	public void run() {
		String msg = getIngo(info);
		Message m = new Message();
		m.obj = msg;
		handler.sendMessage(m);
	}
}
