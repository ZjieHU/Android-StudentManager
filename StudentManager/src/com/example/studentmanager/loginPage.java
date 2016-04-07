package com.example.studentmanager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import loadOperation.loadPicture;

import org.json.JSONArray;
import org.json.JSONObject;

import NetOperation.load_NewUser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.modle.connectMessage;

public class loginPage extends Fragment {
	
	public final static int REQUEST_ALBUM_PICTURE = 1;
	public final static int REQUEST_ALBUM_SUCCESS = 0;
	public final static String HTTPURL = "http://192.168.1.101:8080/StudentManager";
	public static loginPage login;
	
	private Button login_btn ;
	private EditText username,password;
	private ImageView picture_Header;
	private loadPicture load_Picture ;
	private TextView new_user;
	
	public ArrayList<connectMessage> messageList = new ArrayList<connectMessage>();
	
	
	public static loginPage getLoginPageOBJ() {
		if(login == null)
			login = new loginPage();
		return login;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		load_Picture = new loadPicture(getActivity());
		getLoginPageOBJ().messageList.clear();
	}
	
	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login, null);
		login_btn = (Button) v.findViewById(R.id.id_login);
		username = (EditText) v.findViewById(R.id.id_username);
		password = (EditText) v.findViewById(R.id.id_password);
		picture_Header = (ImageView) v.findViewById(R.id.id_headpicture);
		new_user = (TextView) v.findViewById(R.id.new_user);
		
		String[] loginInfo = load_Picture.getLoginInfo();
		username.setText(loginInfo[0]);
		password.setText(loginInfo[1]);
		
		new_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						String str = msg.obj.toString();
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("申请成功");
						builder.setMessage("帐号："+str.split(",")[0]+"\n密码："+str.split(",")[1]);
						builder.show();
					}
				};
				new load_NewUser(handler).start();
			}
		});
		
		picture_Header.setImageBitmap(load_Picture.getBitmap());
		picture_Header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_ALBUM_PICTURE);
			}
		});
		
		login_btn.setOnTouchListener(new OnTouchListener() {
			@SuppressWarnings("static-access")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == event.ACTION_DOWN) {
					load_Picture.saveLoginInfo(username.getText().toString(),
							password.getText().toString());
					login_btn.setBackgroundColor(Color.rgb(134, 255, 255));
					String username_txt = username.getText().toString();
					String password_txt = password.getText().toString();
					new login_thread().execute(username_txt,password_txt);
				}else {
					login_btn.setBackgroundColor(Color.rgb(154, 255, 255));
				}
				return false;
			}
		});
		getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.login_title);
		return v;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_ALBUM_PICTURE && resultCode == getActivity().RESULT_OK) {
			Uri uri = data.getData();
			try {
				if(uri != null) {
					Bitmap bitmap = MediaStore.Images.Media.
							getBitmap(getActivity().getContentResolver(), uri);
					load_Picture.saveBitmap(bitmap);
					picture_Header.setImageBitmap(load_Picture.getBitmap());
				}else {
					Bundle bundle = data.getExtras();
					if(bundle != null) {
						Bitmap bitmap = (Bitmap) bundle.get("data");
						load_Picture.saveBitmap(bitmap);
						picture_Header.setImageBitmap(load_Picture.getBitmap());
					}
				}
			}catch(Exception e) {
				Log.v("myTag", e.toString()); 
			}
		}
	}
	
	/*
	 * 登录验证成功则返回true
	 * 失败则返回false
	 */
	private class login_thread extends AsyncTask<String, Integer, Boolean> {
		
		private ProgressDialog progressBar;
		String str= "";
				
		@Override
		protected Boolean doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			try {
				String url = HTTPURL+"/login.action?username="+username.trim()+"&password="+password;
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedReader bf = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String t = new String();
				while((t = bf.readLine()) != null)
					str += t; 
				Log.v("myTag", str);
				if(str.equals("0")) return false;
				else {
					JSONArray jsonArray = new JSONArray(str);
					for(int i = 0; i < jsonArray.length(); i++) {
						//Log.v("myTag", jsonArray.length()+"");
						JSONObject jsonOBJ = jsonArray.getJSONObject(i);
						connectMessage MSG = new connectMessage();
						MSG.setName(jsonOBJ.getString("name"));
						MSG.setLeastMsg(jsonOBJ.getString("least"));
						MSG.setUrl(jsonOBJ.getString("url"));
						getLoginPageOBJ().messageList.add(MSG); 
					}
					return true;
				}
			}catch(Exception e) {
				Log.v("myTag", e.toString());
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setMessage(values[0]+"");
		}

		@Override
		protected void onPreExecute() {
			progressBar = new ProgressDialog(getActivity()); 
			progressBar.setMax(100);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressBar.cancel();
			if(!result) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, new connectPeople()).commit();
			}else {
				Toast.makeText(getActivity(), "用户名或密码错误", 1).show();
			}
		}
	}
}
