package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Connect_Thread;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.GroupListAdapter;
import uos.codingsroom.ddmgroup.listview.NewsFeedListAdapter;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends Activity {
	public static MainActivity preActivity;

	private UserProfile userProfile;
	private NetworkImageView profilePictureLayout;
	private TextView myNameText;
	private ImageView settingButton;

	private LinearLayout noticeLayout;

	private ListView groupListView;
	private GroupListAdapter groupAdapter;

	private static Boolean layoutHideFlag = false;

	private ListView newsfeedListView;
	private NewsFeedListAdapter newsfeedAdapter;

	MakeMenu menu;

	private static Integer myMemNum;
	private static String nickName;
	private static String profileImageURL;
	private static Long kakaoCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeView();

		readProfile();
		setProfile();
		setBigListView();
		setNewsfeedListView();

		Connect_Thread mThread = new Connect_Thread(this, 10, nickName, profileImageURL, kakaoCode);
		mThread.start();
	}

	protected void onResume() {
		super.onResume();
		userProfile = UserProfile.loadFromCache();
//		if (userProfile != null) {
//			setProfileURL(userProfile.getThumbnailImagePath());
//		}
	}

	public void setMyMemberNum(int myMemberNumber) {
		myMemNum = myMemberNumber;
	}

	public void showMyMemNumber() {
		Toast.makeText(getApplication(), "당신의 회원 번호는 " + myMemNum + " 입니다.", Toast.LENGTH_SHORT).show();
	}

	public void setNewsfeedListView() {
		newsfeedAdapter.addItem(new NewsFeedItem(10, 1, "여기는 제목입니다.1", "Codingsroom1", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(9, 2, "여기는 제목입니다.2", "Codingsroom2", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(8, 3, "여기는 제목입니다.3", "Codingsroom3", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(7, 4, "여기는 제목입니다.4", "Codingsroom4", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(6, 5, "여기는 제목입니다.5", "Codingsroom5", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(5, 6, "여기는 제목입니다.6", "Codingsroom6", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(4, 7, "여기는 제목입니다.7", "Codingsroom7", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(3, 8, "여기는 제목입니다.8", "Codingsroom8", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(2, 9, "여기는 제목입니다.9", "Codingsroom9", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(1, 10, "여기는 제목입니다.10", "Codingsroom10", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(10, 1, "여기는 제목입니다.11", "Codingsroom1", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(9, 2, "여기는 제목입니다.12", "Codingsroom2", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(8, 3, "여기는 제목입니다.13", "Codingsroom3", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(7, 4, "여기는 제목입니다.14", "Codingsroom4", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(6, 5, "여기는 제목입니다.15", "Codingsroom5", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(5, 6, "여기는 제목입니다.16", "Codingsroom6", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(4, 7, "여기는 제목입니다.17", "Codingsroom7", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(3, 8, "여기는 제목입니다.18", "Codingsroom8", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(2, 9, "여기는 제목입니다.19", "Codingsroom9", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(1, 10, "여기는 제목입니다.20", "Codingsroom10", "1988/08/03"));

		newsfeedListView.setAdapter(newsfeedAdapter);

//		newsfeedListView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//				if (firstVisibleItem == 1) {
//					if (layoutHideFlag == false) {
//						layoutHideFlag = true;
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								layoutHideFlag = false;
//							}
//						}, 800);
//						noticeLayout.setVisibility(View.GONE);
//					}
//				} else if (firstVisibleItem == 0) {
//					if (layoutHideFlag == false) {						
//						noticeLayout.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//		});
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
				setLittleListView();
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

		initializeButtons();
		initializeProfileView();
		initializeListView();
	}

	private void initializeProfileView() {
		myNameText = (TextView) findViewById(R.id.my_name);
		profilePictureLayout = (NetworkImageView) findViewById(R.id.profile_image);
		noticeLayout = (LinearLayout) findViewById(R.id.notice_layout);
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

		newsfeedListView = (ListView) findViewById(R.id.listview_newsfeed);
		newsfeedAdapter = new NewsFeedListAdapter(this);
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
