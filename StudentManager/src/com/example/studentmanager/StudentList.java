package com.example.studentmanager;

import java.util.ArrayList;

import loadOperation.load_MSGRecord;
import Dao.SQLOperation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.ScreenTool.delatTime;
import com.ScreenTool.pullRfreshUpdate;
import com.modle.Student;
import com.modle.connectMessage;

public class StudentList extends Fragment {
	
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> studentArray = new ArrayList<ArrayList>();
	private String[] ArrayName ;
	private int[] addFriendWays = {R.id.add_robot,R.id.delete_makefriend,
			R.id.SID_makefriend, R.id.Name_makefriend, R.id.realName_makefriend,R.id.beside_people};
	private TextView[] addFriendsTextView = new TextView[addFriendWays.length];
	
	private ArrayList<Student> student;
	private ArrayList<String> mArrayName;
	private ExpandableListView listView ;
	private boolean isAddFriendsWaysDisplay = false;
	private View v;
	private GestureDetector gesture;
	private boolean scrollTop = false;
	private SQLOperation sql;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Init();
		ArrayName = new String[mArrayName.size()];
		for(int i = 0; i < mArrayName.size(); i++) {
			ArrayList<Student> list = new ArrayList<Student>();
			for(int j = 0; j < student.size(); j++) {
				Student s = student.get(j);
				if(s.getArray_Name().equals(mArrayName.get(i))) {
					list.add(s);
				}
			}
			studentArray.add(list);
			ArrayName[i] = mArrayName.get(i);
		}
	}
	
	public void Init() {
		sql = SQLOperation.getSQLOperationOBJ(getActivity());
		sql.CreateDatabase();
		sql.CreateTable();
		student = sql.getStudentList();
		mArrayName = sql.getArrayName();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gesture = new GestureDetector(getActivity(), new MyGestureDecoter());
		v = inflater.inflate(R.layout.right_connectpeople, null);
		listView = (ExpandableListView) v.findViewById(R.id.listView_Array);
		listView.setAdapter(new Adapter());
		TextView MSG_people = (TextView) v.findViewById(R.id.MSG);
		Button back_login = (Button) v.findViewById(R.id.back_login);
		Button addNewFriendsTextView = (Button) v.findViewById(R.id.add_newFriends);
		pullView = v.findViewById(R.id.pullRefresh);
		
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isAddFriendsWaysDisplay) {
					isAddFriendsWaysDisplay = false;
					for(TextView t : addFriendsTextView) {
						t.setVisibility(View.GONE);
					}
				}
				return gesture.onTouchEvent(event);
			}
		});
		
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isAddFriendsWaysDisplay) {
					isAddFriendsWaysDisplay = false;
					for(TextView t : addFriendsTextView) {
						t.setVisibility(View.GONE);
					}
				}
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
		
		back_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, new loginPage()).commit();
			}
		});
		
		MSG_people.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, new right_connectPeople());
				ft.commit();
			}
		});
		
		return v;
	}
	
	private class Adapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return studentArray.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return studentArray.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return studentArray.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return studentArray.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.studentarray, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.group_Name);
			TextView peopleNumber = (TextView) convertView.findViewById(R.id.people_number);
			textView.setText(ArrayName[groupPosition]);
			peopleNumber.setText(studentArray.get(groupPosition).size()+"");
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.studentschild, null);
			}
			TextView Name = (TextView) convertView.findViewById(R.id.people_Name);
			Name.setText(((Student)(studentArray.get(groupPosition).get(childPosition)))
					.getName());
			TextView Introduce = (TextView) convertView.findViewById(R.id.introduce);
			Introduce.setText(((Student)(studentArray.get(groupPosition).get(childPosition)))
					.getIntroduce());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			FragmentManager fm = getActivity().getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			Student s = ((Student)(studentArray.get(groupPosition).get(childPosition)));
			bundle.putString("name", s.getName());
			bundle.putString("page", "StudentList");
			chatWithPeople chat = new chatWithPeople();
			ft.replace(R.id.container, chat);
			chat.setArguments(bundle);
			ft.commit();
			return true;
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
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
	}
}
