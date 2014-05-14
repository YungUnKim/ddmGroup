package uos.codingsroom.ddmgroup;

import java.util.HashSet;
import java.util.Set;

import uos.codingsroom.ddmgroup.comm.Get_Groups_Thread;
import uos.codingsroom.ddmgroup.comm.Login_Profile_Thread;
import uos.codingsroom.ddmgroup.fragments.ContentsFragment;
import uos.codingsroom.ddmgroup.fragments.NewsfeedFragment;
import uos.codingsroom.ddmgroup.fragments.RegisterFragment;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.MyInfoItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.GroupListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.kakao.APIErrorResult;
import com.kakao.KakaoTalkHttpResponseHandler;
import com.kakao.KakaoTalkProfile;
import com.kakao.KakaoTalkService;
import com.kakao.UserProfile;

public class MainActivity extends FragmentActivity {
	public static MainActivity preActivity;
	private static final int menus = 7;
	private static int currentCategory = 0;

	private UserProfile userProfile;
	private TextView myNameText;
	private ImageView settingButton;
	private ImageView favoriteButton;
	private ImageView noticeButton;
	private ImageView ddmLogo;
	private ImageView menuBackButton;

	private RelativeLayout menuHelperLayout;

	private LinearLayout menuLayout;
	private RelativeLayout[] menuButtons = new RelativeLayout[menus];
	private Integer[] menuButtonView = { R.id.menu_1, R.id.menu_2, R.id.menu_3, R.id.menu_4, R.id.menu_5, R.id.menu_6, R.id.menu_7 };

	private ListView groupListView;
	private GroupListAdapter groupAdapter;

	MakeMenu menu;
	MakePreferences myPreference;

	GroupItem groupItem;
	
	private static MyInfoItem myInfoItem;

	Set<String> favoriteStringSet;

	private static Integer myMemNum;
	private static String nickName;
	private static String profileImageURL;
	private static String profileBigImageURL;
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
	
	public static MyInfoItem getMyInfoItem(){
		return myInfoItem;
	}

	public void setMyMemberNum(int myMemberNumber) {
		myMemNum = myMemberNumber;
		myInfoItem = new MyInfoItem(myMemNum, profileImageURL, nickName);		
	}

	public void showMyMemNumber() {
		Toast.makeText(getApplication(), "당신의 회원 번호는 " + myMemNum + " 입니다.", Toast.LENGTH_SHORT).show();
	}
	
	//공지
	//공지 아이템 추가
	public void setNotice(int index, String title, int num) {
		((NewsfeedFragment) fragments[NEWSFEED]).setNotice(index, title, num);
	}
	//공지 아이템 갱신
	public void setNoticeTitle() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNoticeTitle();
	}

	//뉴스피드
	//뉴스피드 아이템 추가
	public void setNewsFeed(NewsFeedItem newsFeedItem) {
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeed(newsFeedItem);
	}
	//뉴스피드 아이템 갱신
	public void setNewsFeedList() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeedList();
	}

	//게시글
	//게시글 리스트 아이템 추가
	public void addContent(ContentItem contentItem) {
		((ContentsFragment) fragments[BOARD]).addListview(contentItem);

	}
	//게시글 리스트 실행
	public void setContent() {
		((ContentsFragment) fragments[BOARD]).setListView();
	}

	//페이지 넘버 조작
	//페이지 넘버 가져오기
	public int getPageNum() {
		return ((ContentsFragment) fragments[BOARD]).getPageNum();
	}
	//페이지 넘버 저장하기
	public void setPageNum(int num) {
		((ContentsFragment) fragments[BOARD]).setPageNum(num);
	}
	
	public void addGroupItem(GroupItem mItem) {
		groupAdapter.addItem(mItem);
	}

	public void setLittleListView() {
		//그룹 게시판 보기 터치했을때
		groupAdapter.addItem(0, new GroupItem("돌아가기"));

		groupListView.setAdapter(groupAdapter);
		menuLayout.setVisibility(View.GONE);
		groupListView.setVisibility(View.VISIBLE);
		menuButtons[currentCategory].setClickable(true);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position == 0) {
					menuLayout.setVisibility(View.VISIBLE);
					groupListView.setVisibility(View.GONE);
					groupAdapter.clearItem();
					if (favoriteFlag)
						closeFavorite();

					showFragment(NEWSFEED, false);
					// setBigListView();
				} else {
					GroupItem curItem = (GroupItem) groupAdapter.getItem(position);
					((ContentsFragment) fragments[BOARD]).setCurrentGroupNum(curItem.getIndexNum());
					((ContentsFragment) fragments[BOARD]).setTitleLabel(curItem.getTitle());
					((ContentsFragment) fragments[BOARD]).contentFragmentStart();
					((RegisterFragment) fragments[REGISTER]).setCurrentGroupNum(curItem.getIndexNum());
					((RegisterFragment) fragments[REGISTER]).setTitleLabel(curItem.getTitle());
					showFragment(BOARD, false);
					menu.getMenu().showContent();
				}

			}
		});
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
				profileBigImageURL = talkProfile.getProfileImageURL();
				kakaoCode = userProfile.getId();

				Login_Profile_Thread mThread = new Login_Profile_Thread(context, 10, nickName, profileImageURL, kakaoCode);
				mThread.start();
			}
		});
	}

	public void setProfile() {
		myNameText.setText(nickName + "님 안녕하세요!");
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

		menuHelperLayout = (RelativeLayout) findViewById(R.id.main_menu_helper_layout);
		menuLayout = (LinearLayout) findViewById(R.id.layout_menu);

		menu.getMenu().setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				menuHelperLayout.setVisibility(View.GONE);
			}
		});
		menu.getMenu().setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				menuHelperLayout.setVisibility(View.VISIBLE);
			}
		});

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
		noticeButton = (ImageView) findViewById(R.id.button_notice);
		noticeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNoticeActivity();
			}
		});

		ddmLogo = (ImageView) findViewById(R.id.ddm_logo);
		ddmLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentFragment != 0) {
					showFragment(0, false);
					Toast.makeText(getBaseContext(), "조인행 코딩좀 해", Toast.LENGTH_SHORT).show();
				}

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
					menuButtons[position].setClickable(false);
					currentCategory = position;
					Get_Groups_Thread mThread = new Get_Groups_Thread(MainActivity.this, 20, position);
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
		final Intent intent = new Intent(this, SettingActivity.class);
		intent.putExtra("myName", nickName);
		intent.putExtra("myProfileUrl", profileBigImageURL);
		intent.putExtra("myCode", kakaoCode);
		startActivity(intent);
	}
	
	private void moveToNoticeActivity() {
		final Intent intent = new Intent(this, NoticeActivity.class);		
		startActivity(intent);
	}

	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_MENU:
			menu.getMenu().showMenu();
			return true;
		}

		return super.onKeyDown(keycode, e);
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
