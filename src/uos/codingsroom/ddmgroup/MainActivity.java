package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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

	MakeMenu menu;

	private static String nickName;
	private static String profileImageURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeView();
	}

	protected void onResume() {
		super.onResume();
		userProfile = UserProfile.loadFromCache();
		if (userProfile != null) {
			setProfileURL(userProfile.getThumbnailImagePath());
		}
		readProfile();
		setProfile();
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
