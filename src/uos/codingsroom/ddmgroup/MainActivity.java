package uos.codingsroom.ddmgroup;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uos.codingsroom.ddmgroup.comm.Connect_Thread;
import uos.codingsroom.ddmgroup.fragments.ContentsFragment;
import uos.codingsroom.ddmgroup.fragments.NewsfeedFragment;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.GroupListAdapter;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private static final int menus = 7;

	private UserProfile userProfile;
	private NetworkImageView profilePictureLayout;
	private TextView profileNameText;
	private TextView myNameText;
	private ImageView settingButton;
	private ImageView favoriteButton;
	private ImageView ddmLogo;
	private ImageView menuBackButton;

	private LinearLayout menuLayout;
	private RelativeLayout[] menuButtons = new RelativeLayout[menus];
	private Integer[] menuButtonView = { R.id.menu_1, R.id.menu_2, R.id.menu_3, R.id.menu_4, R.id.menu_5, R.id.menu_6, R.id.menu_7 };

	private ListView groupListView;
	private GroupListAdapter groupAdapter;

	MakeMenu menu;
	MakePreferences myPreference;

	GroupItem groupItem;

	Set<String> favoriteStringSet;

	private static Integer myMemNum;
	private static String nickName;
	private static String profileImageURL;
	private static Long kakaoCode;

	private static Integer currentFragment = 0;
	private static Boolean favoriteFlag = false;

	private static final int NEWSFEED = 0;
	private static final int BOARD = 1;
	private static final int REGISTER = 2;
	private static final int FRAGMENT_COUNT = REGISTER + 1;

	private static Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeView(); // 뷰를 초기화

		readProfile(this);
		// setProfile();
		// setBigListView();
		setNewsFeedView();// 뉴스피드 시작

		myPreference = new MakePreferences(this);
		favoriteStringSet = myPreference.getMyPreference().getStringSet("favoriteName", new HashSet<String>());

		showFragment(NEWSFEED, false);

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

	public void setNotice(int index, String title, int num) {
		((NewsfeedFragment) fragments[NEWSFEED]).setNotice(index, title, num);
	}

	public void setNoticeTitle() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNoticeTitle();
	}

	public void setNewsFeed(NewsFeedItem newsFeedItem) {
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeed(newsFeedItem);
	}

	public void setNewsFeedTitle() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeedTitle();
	}

	// 뉴스피드 스레드 실행
	public void setNewsFeedView() {
		Connect_Thread mThread = new Connect_Thread(MainActivity.this, 12);
		mThread.start();
	}

	public void addGroupItem(GroupItem mItem) {
		groupAdapter.addItem(mItem);
	}

	public void setLittleListView() {
		groupAdapter.addItem(0, new GroupItem("돌아가기"));

		groupListView.setAdapter(groupAdapter);
		menuLayout.setVisibility(View.GONE);
		groupListView.setVisibility(View.VISIBLE);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position == 0) {
					menuLayout.setVisibility(View.VISIBLE);
					groupListView.setVisibility(View.GONE);
					groupAdapter.clearItem();
					if (favoriteFlag)
						closeFavorite();
					// setBigListView();
				} else {
					GroupItem curItem = (GroupItem) groupAdapter.getItem(position);
					((ContentsFragment) fragments[BOARD]).setCurrentGroupNum(curItem.getIndexNum());
					((ContentsFragment) fragments[BOARD]).setTitleLabel(curItem.getTitle());
					((ContentsFragment) fragments[BOARD]).setListView();
					showFragment(BOARD, false);
					menu.getMenu().showContent();
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

	public void readProfile(final Context context) {
		KakaoTalkService.requestProfile(new MyTalkHttpResponseHandler<KakaoTalkProfile>() {
			@Override
			protected void onHttpSuccess(final KakaoTalkProfile talkProfile) {
				nickName = talkProfile.getNickName();
				profileImageURL = talkProfile.getThumbnailURL();
				kakaoCode = userProfile.getId();

				Connect_Thread mThread = new Connect_Thread(context, 10, nickName, profileImageURL, kakaoCode);
				mThread.start();
			}
		});
	}

	public void setProfile() {
		myNameText.setText(nickName + "님 안녕하세요!");
		profileNameText.setText(nickName);
		setProfileURL(profileImageURL);
	}

	private void initializeView() {
		setContentView(R.layout.activity_main);
		preActivity = this;
		menu = new MakeMenu(this);

		FragmentManager fm = getSupportFragmentManager();
		fragments[NEWSFEED] = fm.findFragmentById(R.id.newsfeedFragment);
		fragments[BOARD] = fm.findFragmentById(R.id.boardFragment);
		fragments[REGISTER] = fm.findFragmentById(R.id.registerFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();

		menuLayout = (LinearLayout) findViewById(R.id.layout_menu);

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

		currentFragment = fragmentIndex;
	}

	private void initializeProfileView() {
		myNameText = (TextView) findViewById(R.id.my_name);
		profilePictureLayout = (NetworkImageView) findViewById(R.id.profile_image);
		profileNameText = (TextView) findViewById(R.id.profile_name);
	}

	private void initializeButtons() {
		settingButton = (ImageView) findViewById(R.id.button_setting);
		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToSettingActivity();
			}
		});
		favoriteButton = (ImageView) findViewById(R.id.button_favorite);
		favoriteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (favoriteFlag)
					closeFavorite();
				else
					openFavorite();
			}
		});

		ddmLogo = (ImageView) findViewById(R.id.ddm_logo);
		ddmLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "조인행 코딩좀 해", Toast.LENGTH_SHORT).show();

			}
		});

		menuBackButton = (ImageView) findViewById(R.id.slidingmenu_back_button);
		menuBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.getMenu().showContent();
			}
		});

		for (int i = 0; i < menus; i++) {
			final int position = i;
			menuButtons[i] = (RelativeLayout) findViewById(menuButtonView[i]);
			menuButtons[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Connect_Thread mThread = new Connect_Thread(MainActivity.this, 20, position);
					mThread.start();
				}
			});
		}

	}

	public void openFavorite() {
		groupAdapter.clearItem();

		for (String string : favoriteStringSet) {
			String[] dataSet = new String(string).split(" ");
			groupItem = new GroupItem();
			groupItem.setIndexNum((Integer.parseInt(dataSet[0])));
			groupItem.setTitle(string.substring(dataSet[0].length()));
			addGroupItem(groupItem);
		}
		setLittleListView();
		favoriteButton.setSelected(true);
		favoriteFlag = true;
	}

	public void closeFavorite() {
		menuLayout.setVisibility(View.VISIBLE);
		groupListView.setVisibility(View.GONE);
		groupAdapter.clearItem();
		favoriteButton.setSelected(false);
		favoriteFlag = false;
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

	@Override
	public void onBackPressed() {
		if (menu.getMenu().isMenuShowing()) {
			menu.getMenu().showContent();
		} else if (currentFragment == 2) {
			showFragment(1, false);
		} else if (currentFragment == 1) {
			showFragment(0, false);
		} else {
			super.onBackPressed();
		}
	}
}
