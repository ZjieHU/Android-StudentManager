package com.example.studentmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.modle.connectMessage;

import loadOperation.load_MSGRecord;
import NetOperation.TuringRobot;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class chatWithPeople extends Fragment {
	
	private Map<Integer,Boolean> isGetView = new TreeMap<Integer,Boolean>();
	private View v;
	private ListView listView;
	private ArrayList<String> lists = new ArrayList<String>();
	private static chatWithPeople mChatWithPeople;
	private Bundle bundle;
	private load_MSGRecord loadmsg;
	private String name;
	private boolean isDeleate = false;
	private TuringRobot robot ;
	private String send_txt ;
	private String page;
	private boolean isSend = false;
	
	public int mPosition;
	
	public static chatWithPeople getChatWithPeople() {
		mChatWithPeople = mChatWithPeople == null ? new chatWithPeople() : mChatWithPeople;
		return mChatWithPeople;
	}
	public Map<Integer,Boolean> getIsGetView() {
		return isGetView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getArguments();
		loadmsg = new load_MSGRecord(getActivity());
		name = bundle.getString("name");
		page = bundle.getString("page");
	}
	
	@SuppressWarnings("unused")
	private void InitIsGetView() {
		for(int i = 0; i < lists.size(); i++) {
			isGetView.put(i, false);
		}
	}
	
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.chatwindow, null);
		final TextView back = (TextView) v.findViewById(R.id.back_rightconnect);
		listView = (ListView) v.findViewById(R.id.messageList);
		Button send_Message = (Button) v.findViewById(R.id.send_message);
		final EditText send_Text = (EditText) v.findViewById(R.id.send_text);
		TextView chatwithPeople_Name = (TextView) v.findViewById(R.id.chatwithPeople_Name);
		Button delete_robot = (Button) v.findViewById(R.id.delete);
		
		delete_robot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isDeleate = true;
				back.setText("取消");
			}
		});
		
		chatwithPeople_Name.setText(name);

		lists = loadmsg.getMSGRecored(name);
		
		final Adapter adapter = new Adapter(getActivity(),0,lists);
		
		send_Message.setOnClickListener(new OnClickListener() {
			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View v) {
				if(!send_Text.getText().toString().equals("")) {
					send_txt = send_Text.getText().toString();
					lists.add(send_txt);
					loadmsg.saveMSGRecord(lists, name);
					lists = loadmsg.getMSGRecored(name);
					isGetView.clear();
					send_Text.setText("");
					if(name.equals("Robot")) {
						Handler handler = new Handler() {
							public void handleMessage(android.os.Message msg) {
								lists.add(msg.obj.toString());
								loadmsg.saveMSGRecord(lists, name);
								lists = loadmsg.getMSGRecored(name);
								isGetView.clear();
								Adapter adapter = new Adapter(getActivity(),0,lists);
								listView.setAdapter(adapter);
							};
						};
						robot = new TuringRobot(handler, send_txt); 
						robot.start();
					}
					Adapter adapter = new Adapter(getActivity(),0,lists);
					listView.setAdapter(adapter);
					isSend = true;
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(isDeleate) {
					loadmsg.delete_MSGRecord(lists.get(position), name);
					lists = loadmsg.getMSGRecored(name);
					Log.v("myTag", lists.size()+"delete after size");
					Adapter adapter = new Adapter(getActivity(),0,lists);
					isGetView.clear();
					listView.setAdapter(adapter);
				}
			}
		});
		listView.setAdapter(adapter);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isDeleate) {
					isDeleate = false;
					back.setText("返回"); 
				}else {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					if(page.equals("right_connectPeople"))
						ft.replace(R.id.container, new right_connectPeople()).commit();
					else
						ft.replace(R.id.container, new StudentList()).commit();
					if(!isExists(name) && isSend) {
						connectMessage c = new connectMessage();
						if(lists.size() > 1)
							c.setLeastMsg(lists.get(lists.size() - 1));
						c.setName(name);
						loginPage.getLoginPageOBJ().messageList.add(c);
					}
				}
			}
		});
		return v;
	}
	
	public boolean isExists(String name) {
		for(connectMessage conn : loginPage.getLoginPageOBJ().messageList) {
			if(conn.getName().equals(name)) return true;
		}
		return false;
	}
	
	private class Adapter extends ArrayAdapter<Object> {
		
		private ArrayList<String> lists ;
		
		@SuppressWarnings("unchecked")
		public Adapter(Context context, int resource, @SuppressWarnings("rawtypes") List objects) {
			super(context, resource, objects);
			this.lists = (ArrayList<String>) objects;
		}
		
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.messagelist, null);
			}
			Log.v("myTag", lists.size()+"");
			if(isGetView.containsKey(position)) {
				return convertView;
			}else {
				String str = lists.get(position);
				int count = str.length();
				String str1 = str.split("")[count];
				if(str1.equals("1")) { //接收
					TextView textView = (TextView) convertView.findViewById(R.id.left_message);
					textView.setText(str.subSequence(0, count-1));
					textView.setVisibility(View.VISIBLE);
				}else {//发送
					TextView textView = (TextView) convertView.findViewById(R.id.right_message);
					textView.setText(str);
					textView.setVisibility(View.VISIBLE);
				} 
				isGetView.put(position, true);
			}
			
			return convertView;
		}
		
	}
}
