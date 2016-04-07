package com.example.studentmanager;

import java.util.ArrayList;
import loadOperation.loadPicture;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ScreenTool.delatTime;
import com.ScreenTool.pullRfreshUpdate;
import com.modle.connectMessage;

@SuppressLint("InflateParams")
public class right_connectPeople extends Fragment {

	public int border = 25 + 40; //dp
	
	private View v;
	private ArrayList<connectMessage> lists = loginPage.getLoginPageOBJ().messageList;
	private ListView listView;
	private GestureDetector gesture;
	private boolean scrollTop = false;
	private boolean isAddFriendsWaysDisplay = false;
	private boolean isDelete = false;
	
	private int[] addFriendWays = {R.id.add_robot,R.id.delete_makefriend,
		R.id.SID_makefriend, R.id.Name_makefriend, R.id.realName_makefriend,R.id.beside_people};
	private TextView[] addFriendsTextView = new TextView[addFriendWays.length];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Log.v("myTag", lists.size()+"");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gesture = new GestureDetector(getActivity(), new MyGestureDecoter());
		final Adapter adapter = new Adapter();
		
		v = inflater.inflate(R.layout.right_connectpeople, null);
		listView = (ListView) v.findViewById(R.id.listView_connect);
		pullView = v.findViewById(R.id.pullRefresh);
		Button back_login = (Button) v.findViewById(R.id.back_login);
		Button addNewFriendsTextView = (Button) v.findViewById(R.id.add_newFriends);
		final TextView StudentList = (TextView) v.findViewById(R.id.student_list); //联系人
		final TextView dynamisc_MSG = (TextView) v.findViewById(R.id.dynamsic_MSG); //动态
		final TextView MSG = (TextView) v.findViewById(R.id.MSG); //消息
		TextView add_robot = (TextView) v.findViewById(R.id.add_robot);
		TextView delete_robot = (TextView) v.findViewById(R.id.delete_makefriend);
		
		dynamisc_MSG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isDelete = false;
				dynamisc_MSG.setText("动态");
				StudentList.setText("联系人");
				StudentList.setVisibility(View.VISIBLE);
				MSG.setText("消息");
				MSG.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}
		});
		
		delete_robot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isDelete = true;
				StudentList.setVisibility(View.GONE);
				MSG.setVisibility(View.GONE);
				dynamisc_MSG.setText("取消删除");
				disPerenceAddF();
			}
		});
		
		add_robot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectMessage MSG = new connectMessage();
				MSG.setName("Robot");
				MSG.setSrc(new loadPicture(getActivity()).getBitmap());
				MSG.setLeastMsg("");
				loginPage.getLoginPageOBJ().messageList.add(MSG);
				lists = loginPage.getLoginPageOBJ().messageList;
				adapter.notifyDataSetChanged();
				disPerenceAddF();
			}
		});
		
		
		StudentList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, new StudentList());
				ft.commit();
			}
		});
		
		back_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, loginPage.getLoginPageOBJ()).commit();
			}
		});
		
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				disPerenceAddF();
				return gesture.onTouchEvent(event);
			}
		});		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(isDelete) {
					loginPage.getLoginPageOBJ().messageList.remove(position);
					adapter.notifyDataSetChanged();
				}else {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					Bundle bundle = new Bundle();
					bundle.putString("name", lists.get(position).getName());
					bundle.putString("page", "right_connectPeople");
					chatWithPeople chat = new chatWithPeople();
					ft.replace(R.id.container, chat);
					chat.setArguments(bundle);
					ft.commit();
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				disPerenceAddF();
				return gesture.onTouchEvent(event);
			}
		}); 
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem == 0) {
					scrollTop = true;
				}else {
					scrollTop = false;
				}
				
			}
		});
		listView.setAdapter(adapter);
		
		addNewFriendsTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isAddFriendsWaysDisplay) {
					for(int i = 0; i < addFriendWays.length; i++) {
						addFriendsTextView[i] = (TextView) v.findViewById(addFriendWays[i]);
						Handler handler = new Handler() {
							@Override
							public void handleMessage(Message msg) {
								int count = msg.what;
								if(count < addFriendWays.length) {
									addFriendsTextView[count].setVisibility(View.VISIBLE);
								}
							}
						};
						new delatTime(handler,i).start();
					}
					isAddFriendsWaysDisplay = true;
				}else {
					isAddFriendsWaysDisplay = false;
					for(TextView t : addFriendsTextView) {
						t.setVisibility(View.GONE);
					}
				}
			}
		});
		
		return v;
	} 
	
	public void disPerenceAddF() {
		if(isAddFriendsWaysDisplay) {
			isAddFriendsWaysDisplay = false;
			for(TextView t : addFriendsTextView) {
				t.setVisibility(View.GONE);
			}
		}
	}
	
	private class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().
						getLayoutInflater().inflate(R.layout.connectlistview, null);
			}			
			connectMessage msg = lists.get(position);
			ImageView imageHead = (ImageView) convertView.findViewById(R.id.id_connectpeoplehead);
			imageHead.setImageBitmap(msg.getSrc());
			TextView textView_Name = (TextView) convertView.findViewById(R.id.id_connectPeopleusername);
			textView_Name.setText(msg.getName());
			TextView textView_Msg = (TextView) convertView.findViewById(R.id.id_connectPeopleMSG);
			textView_Msg.setText(msg.getLeastMsg());
			return convertView;
		}
		
	}
	
	private View pullView ;
	
	private class MyGestureDecoter extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(e2.getY() - e1.getY() > 200 && scrollTop) {
				pullView.setVisibility(View.VISIBLE);
				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if(msg.what == pullRfreshUpdate.SUCCESS_UPDATE) {
							pullView.setVisibility(View.GONE);
						}
					}
				};
				new pullRfreshUpdate(handler).start();
			} 
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
	}
}
