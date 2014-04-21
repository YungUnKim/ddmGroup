package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;

public class KakaoLoginActivity extends Activity {

	private static final int DELAY_TIME = 1500;

	private LoginButton loginButton;
	private final SessionCallback mySessionCallback = new MySessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kakaologin);

		loginButton = (LoginButton) findViewById(R.id.com_kakao_login);
		loginButton.setLoginSessionCallback(mySessionCallback);

	}

	protected void showProfileButton() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loginButton.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
				loginButton.setVisibility(View.VISIBLE);
			}
		}, DELAY_TIME);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 세션을 초기화 한다
		if (Session.initializeSession(this, mySessionCallback)) {
			// 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
			loginButton.setVisibility(View.GONE);
		} else if (Session.getCurrentSession().isOpened()) {
			// 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					onSessionOpened();
				}
			}, DELAY_TIME);
		} else {
			showProfileButton();
		}
		// 3. else 로그인 창이 보인다.
	}

	private class MySessionStatusCallback implements SessionCallback {
		@Override
		public void onSessionOpened() {
			// 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
			KakaoLoginActivity.this.onSessionOpened();
		}

		@Override
		public void onSessionClosed(final KakaoException exception) {
			// 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
			loginButton.setVisibility(View.VISIBLE);
		}

	}

	protected void onSessionOpened() {
		final Intent intent = new Intent(KakaoLoginActivity.this, KakaoTalkSignupActivity.class);
		startActivity(intent);
		finish();
	}
}
