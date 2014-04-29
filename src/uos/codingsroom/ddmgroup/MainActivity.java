package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Connect_Thread;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.listview.GroupListAdapter;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.APIErrorResult;
import com.kakao.GlobalApplication;
import com.kakao.KakaoTalkHttpResponseHandler;
import com.kakao.KakaoTalkProfile;
import com.kakao.KakaoTalkService;
import com.kakao.UserProfile;

public class MainActivity extends FragmentActivity {
	public static MainActivity preActivity;

	private UserProfile userProfile;
	private NetworkImageView profilePictureLayout;
	private TextView myNameText;
	private ImageView settingButton;

	private ListView groupListView;
	private GroupListAdapter groupAdapter;

	MakeMenu menu;

	private static Integer myMemNum;
	private static String nickName;
	private static String profileImageURL;
	private static Long kakaoCode;

	private static final int NEWSFEED = 0;
	private static final int BOARD = 1;
	private static final int REGISTER = 2;
	private static final int FRAGMENT_COUNT = REGISTER + 1;

	private static Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeView();								// 뷰를 초기화 

		readProfile();
		setProfile();
		setBigListView();
		
		showFragment(NEWSFEED, false);

		Connect_Thread mThread = new Connect_Thread(this, 10, nickName, profileImageURL, kakaoCode);
		mThread.start();
	}

	protected void onResume() {
		super.onResume();
		userProfile = UserProfile.loadFromCache();
		// if (userProfile != null) {
		// setProfileURL(userProfile.getThumbnailImagePath());
		// }
	}

	public void setMyMemberNum(int myMemberNumber) {
		myMemNum = myMemberNumber;
	}

	public void showMyMemNumber() {
		Toast.makeText(getApplication(), "당신의 회원 번호는 " + myMemNum + " 입니다.", Toast.LENGTH_SHORT).show();
	}

	public void setBigListView() {
		groupAdapter.addItem(new GroupItem("대분류 1"));
		groupAdapter.addItem(new GroupItem("대분류 2"));
		groupAdapter.addItem(new GroupItem("대분류 3"));
		groupAdapter.addItem(new GroupItem("대분류 4"));
		groupAdapter.addItem(new GroupItem("대분류 5"));

		groupListView.setAdapter(groupAdapter);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				GroupItem curItem = (GroupItem) groupAdapter.getItem(position);
				Toast.makeText(getApplicationContext(), curItem.getTitle(), Toast.LENGTH_SHORT).show();
				groupAdapter.clearItem();
				Connect_Thread mThread = new Connect_Thread(MainActivity.this, 20, position);
				mThread.start();
				//setLittleListView();
			}
		});
	}

	public void setLittleListView() {
		groupAdapter.addItem(new GroupItem("/뒤로가기"));
		groupAdapter.addItem(new GroupItem("소분류 1"));
		groupAdapter.addItem(new GroupItem("소분류 2"));
		groupAdapter.addItem(new GroupItem("소분류 3"));
		groupAdapter.addItem(new GroupItem("소분류 4"));
		groupAdapter.addItem(new GroupItem("소분류 5"));
		groupAdapter.addItem(new GroupItem("소분류 6"));
		groupAdapter.addItem(new GroupItem("소분류 7"));

		groupListView.setAdapter(groupAdapter);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position == 0) {
					Toast.makeText(getApplicationContext(), "대분류로 이동합니다.", Toast.LENGTH_SHORT).show();
					groupAdapter.clearItem();
					setBigListView();
				} else {
					GroupItem curItem = (GroupItem) groupAdapter.getItem(position);
					Toast.makeText(getApplicationContext(), curItem.getTitle(), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	public void setProfileURL(final String profileImageURL) {
		if (profilePictureLayout != null && profileImageURL != null) {
			Application app = GlobalApplication.getGlobalApplicationContext();
			if (app == null)
				throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
			profilePictureLayout.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
		}
	}

	private abstract class MyTalkHttpResponseHandler<T> extends KakaoTalkHttpResponseHandler<T> {
		@Override
		protected void onHttpSessionClosedFailure(final APIErrorResult errorResult) {
			redirectLoginActivity();
		}

		@Override
		protected void onNotKakaoTalkUser() {
			Toast.makeText(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onFailure(final APIErrorResult errorResult) {
			Toast.makeText(getApplicationContext(), "failed : " + errorResult, Toast.LENGTH_SHORT).show();
		}
	}

	public void readProfile() {
		KakaoTalkService.requestProfile(new MyTalkHttpResponseHandler<KakaoTalkProfile>() {
			@Override
			protected void onHttpSuccess(final KakaoTalkProfile talkProfile) {
				nickName = talkProfile.getNickName();
				profileImageURL = talkProfile.getThumbnailURL();
				kakaoCode = userProfile.getId();
			}
		});
	}

	public void setProfile() {
		myNameText.setText(nickName + "님 안녕하세요!");
		setProfileURL(profileImageURL);
	}

	private void initializeView() {
		setContentView(R.layout.activity_main);
		preActivity = this;
		menu = new MakeMenu(this);

		FragmentManager fm = getSupportFragmentManager();
		fragments[NEWSFEED] = fm.findFragmentById(R.id.newsfeedFragment);
		fragments[BOARD] = fm.findFragmentById(R.id.boardFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();

		initializeButtons();
		initializeProfileView();
		initializeListView();
	}

	public void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	private void initializeProfileView() {
		myNameText = (TextView) findViewById(R.id.my_name);
		profilePictureLayout = (NetworkImageView) findViewById(R.id.profile_image);
	}

	private void initializeButtons() {
		settingButton = (ImageView) findViewById(R.id.button_setting);
		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToSettingActivity();
			}
		});
	}

	private void initializeListView() {
		groupListView = (ListView) findViewById(R.id.listview_group);
		groupAdapter = new GroupListAdapter(this);
	}

	private void redirectLoginActivity() {
		Intent intent = new Intent(this, KakaoLoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void moveToSettingActivity() {
		final Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		startActivity(intent);
	}
}
