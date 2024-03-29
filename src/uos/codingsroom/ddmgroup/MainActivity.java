package uos.codingsroom.ddmgroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import uos.codingsroom.ddmgroup.comm.Delete_Notice_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Groups_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Newsfeed_Thread;
import uos.codingsroom.ddmgroup.comm.Login_Profile_Thread;
import uos.codingsroom.ddmgroup.fragments.ContentsFragment;
import uos.codingsroom.ddmgroup.fragments.NewsfeedFragment;
import uos.codingsroom.ddmgroup.fragments.RegisterFragment;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.MyInfoItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.GroupListAdapter;
import uos.codingsroom.ddmgroup.util.LoadingProgressDialog;
import uos.codingsroom.ddmgroup.util.MakeMenu;
import uos.codingsroom.ddmgroup.util.MakePreferences;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
	private static final int menus = 6;
	private static int currentCategory = 0;

	private UserProfile userProfile;
	private TextView myNameText;
	private ImageView settingButton;
	private ImageView favoriteButton;
	private ImageView noticeButton;
	private ImageView menuBackButton;
	private ImageView menuOpener;

	private RelativeLayout menuHelperLayout;

	private LinearLayout menuLayout;
	private RelativeLayout[] menuButtons = new RelativeLayout[menus];
	private Integer[] menuButtonView = { R.id.menu_1, R.id.menu_2, R.id.menu_3, R.id.menu_4, R.id.menu_5, R.id.menu_6 };

	private ListView groupListView;
	private GroupListAdapter groupAdapter;

	private EditText searchBox;
	private LinearLayout headerLayout;

	MakeMenu menu;
	MakePreferences myPreference;

	GroupItem groupItem;

	private static MyInfoItem myInfoItem;
	private ArrayList<Integer> board = new ArrayList<Integer>(); // 권한 게시판 번호
	private ArrayList<Integer> level = new ArrayList<Integer>(); // 권한 게시판 레벨
	
	PackageInfo packageinfo;
	private AlertDialog mainDialog = null;

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

	public static LoadingProgressDialog progressDialog;

	public static Boolean isAdmin = false; // 관리자인지
	public static Boolean isBlocked = false; // 블록당했는지

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = new LoadingProgressDialog(this);
		initializeView(); // 뷰를 초기화
		readProfile(this);
		// setProfile();
		// setBigListView();

		myPreference = new MakePreferences(this);

		showFragment(NEWSFEED, false);
	}

	protected void onResume() {
		super.onResume();
		userProfile = UserProfile.loadFromCache();
		// if (userProfile != null) {
		// setProfileURL(userProfile.getThumbnailImagePath());
		// }
	}

	public static MyInfoItem getMyInfoItem() {
		return myInfoItem;
	}

	public void setMyMemberNum(int myMemberNumber) {
		myMemNum = myMemberNumber;
		myInfoItem = new MyInfoItem(myMemNum, profileImageURL, nickName);
	}

	public void setBoardNum(int num) {
		board.add(num);
	}

	public void setLevel(int num) {
		level.add(num);
	}

	public void setVersion(String str) {
		SystemValue.Version = str;
	}
	
	public void setState(String str) {
		SystemValue.State = str;
	}
	
	public void setAppNotice(String str) {
		SystemValue.AppNotice = str;
	}

	public void setTutorialWebsite(String str) {
		SystemValue.TutorialWebsite = str;
	}
	
	public void setWebsite(String str) {
		SystemValue.Website = str;
	}
	
	// 초기 앱 실행 시 체크
	public void setMainActivity(){	
		try {
			// 앱 버전 확인
			packageinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			// 앱 버전이 다를 경우 업데이트 상태로 바꿔준다.
			if(packageinfo.versionCode < Integer.parseInt(SystemValue.Version)){
				SystemValue.State = "update";
			}
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(SystemValue.State.equals("normal")){
			// 평상시
			setPermission();
			setProfile();
			setGroupButtonClickListener();
		} else{
			progressDialog.dismissProgressDialog();
			
			// 그 외의 상황시 다이얼로그 출력
			new AlertDialog.Builder(this)
			.setTitle("앱 알림")
			.setMessage(SystemValue.AppNotice)
			.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// State에 따른 행동
					// normal 	평상시
					// update 	업데이트 - 업데이트 안내 및 앱 종료
					// check 	점검 중	- 점검 안내 및 앱 종료
					// end 		서비스 종료 - 앱 종료
					if(SystemValue.State.equals("normal")){
						// 평상시
					} else if(SystemValue.State.equals("update")){
						// 업데이트 중
						finish();
					} else if(SystemValue.State.equals("check")){
						// 점검 중
						finish();
					} else if(SystemValue.State.equals("end")){
						// 서비스 종료
						finish();
					} else{
						// error
						finish();
					}
					
					dialog.cancel();
				}
			}).show();
		}
		
		return;
	}
	
	// 게시판 번호와 권한 레벨 객체에 저장하기
	public void setPermission() {
		myInfoItem.setMyboard(board);
		myInfoItem.setMylevel(level);

		for (int i = 0; i < board.size(); i++) {
			if (board.get(i) == 0) { // 관리자일 경우
				if (level.get(i) == SystemValue.ADMIN) {
					isAdmin = true;
				} else if (level.get(i) == SystemValue.BLACK) { // 블랙리스트 유저
					isBlocked = true;
					isAdmin = false;
				} else { // 일반 유저
					isBlocked = false;
					isAdmin = false;
				}
			}
		}
		// Log.i("MyTag","board >> " + myInfoItem.getMyboard().get(0) + ", level >> " + myInfoItem.getMylevel().get(0));
		// Log.i("MyTag","board >> " + myInfoItem.getMyboard().get(1) + ", level >> " + myInfoItem.getMylevel().get(1));
		// Log.i("MyTag","board >> " + myInfoItem.getMyboard().get(2) + ", level >> " + myInfoItem.getMylevel().get(2));
		// myInfoItem.getMylevel().get(0) == 관리자인지 정지여부 확인하기
	}

	// 공지
	// 공지 아이템 추가
	public void setNotice(int index, String title, int num) {
		((NewsfeedFragment) fragments[NEWSFEED]).setNotice(index, title, num);
	}

	// 공지 아이템 갱신
	public void setNoticeTitle() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNoticeTitle();
	}

	// 뉴스피드
	// 뉴스피드 아이템 추가
	public void setNewsFeed(NewsFeedItem newsFeedItem) {
		
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeed(newsFeedItem);
	}

	// 뉴스피드 아이템 갱신
	public void setNewsFeedList() {
		((NewsfeedFragment) fragments[NEWSFEED]).setNewsFeedList();
	}

	// 게시글
	// 게시글 리스트 아이템 추가
	public void addContent(ContentItem contentItem) {
		((ContentsFragment) fragments[BOARD]).addListview(contentItem);

	}

	// 게시글 리스트 실행
	public void setContent() {
		((ContentsFragment) fragments[BOARD]).setListView();
	}

	public void addGroupItem(GroupItem mItem) {
		groupAdapter.addItem(mItem);
	}

	// 글 등록 후 프레그먼트 클리어
	public void clearRegisterFragment() {
		((RegisterFragment) fragments[REGISTER]).clearContent();
		showFragment(BOARD, false);
		((ContentsFragment) fragments[BOARD]).contentFragmentStart();
		progressDialog.dismissProgressDialog();
	}
	
	public void setLittleListView() {
		// 그룹 게시판 보기 터치했을때
		groupAdapter.addItem(0, new GroupItem("돌아가기", "전체 카테고리로 돌아갑니다."));

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
					progressDialog.startProgressDialog();
					((ContentsFragment) fragments[BOARD]).setCurrentGroupNum(curItem.getIndexNum());
					((ContentsFragment) fragments[BOARD]).setTitleLabel(curItem.getTitle(), curItem.getDescription());
					((ContentsFragment) fragments[BOARD]).setPageNum(0);
					((ContentsFragment) fragments[BOARD]).setKeyWord("");
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

				Login_Profile_Thread mThread = new Login_Profile_Thread(context, 10, nickName, profileImageURL, kakaoCode, SystemValue.RegistrationId);
				mThread.start();
				progressDialog.startProgressDialog();
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

		headerLayout = (LinearLayout) findViewById(R.id.header_layout);
		menuHelperLayout = (RelativeLayout) findViewById(R.id.main_menu_helper_layout);
		menuLayout = (LinearLayout) findViewById(R.id.layout_menu);
		searchBox = (EditText) findViewById(R.id.searchBox);
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if ((actionId == EditorInfo.IME_ACTION_DONE) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					((ContentsFragment) fragments[BOARD]).setPageNum(0);
					((ContentsFragment) fragments[BOARD]).setKeyWord(searchBox.getText().toString());
					((ContentsFragment) fragments[BOARD]).contentFragmentStart();
					searchBox.setText("");
				}

				return false;
			}
		});

		menu.getMenu().setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				menuHelperLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
				menuHelperLayout.setVisibility(View.GONE);
			}
		});
		menu.getMenu().setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				menuHelperLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
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
		if (fragmentIndex == 1) {
			searchBox.setVisibility(View.VISIBLE);
		} else {
			searchBox.setVisibility(View.GONE);
		}
	}

	private void initializeProfileView() {
		myNameText = (TextView) findViewById(R.id.my_name);
	}

	public void setGroupButtonClickListener() {
		for (int i = 0; i < menus; i++) {
			final int position = i;
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
		Get_Newsfeed_Thread mThread1 = new Get_Newsfeed_Thread(this, 12);
		mThread1.start();
//		progressDialog.dismissProgressDialog();
	}

	public void initializeButtons() {
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

		menuOpener = (ImageView) findViewById(R.id.main_menu_open_icon);
		menuOpener.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.getMenu().showMenu();
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
			menuButtons[i] = (RelativeLayout) findViewById(menuButtonView[i]);
		}

	}

	public void openFavorite() {
		groupAdapter.clearItem();
		
		favoriteStringSet = myPreference.getMyPreference().getStringSet("favoriteName", new HashSet<String>());

		for (String string : favoriteStringSet) {
			String[] dataSet = new String(string).split("\\|");
			groupItem = new GroupItem();
			groupItem.setIndexNum((Integer.parseInt(dataSet[0])));
			groupItem.setTitle(dataSet[1]);
			groupItem.setDescription(dataSet[2]);
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
		intent.putExtra("myNum", myMemNum);
		startActivity(intent);
	}

	private void moveToNoticeActivity() {
		final Intent intent = new Intent(this, NoticeActivity.class);
		startActivity(intent);
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하는 함수
	public void viewMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		progressDialog.dismissProgressDialog();
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하고 액티비티를 종료하는 함수
	public void viewMessage(String message, int reaction) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		progressDialog.dismissProgressDialog();
		finish();
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
