package uos.codingsroom.ddmgroup;

import static uos.codingsroom.ddmgroup.BasicInfo.TOAST_MESSAGE_ACTION;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Sender;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;

public class KakaoLoginActivity extends Activity {

	private int DELAY_TIME = 1500;

	AsyncTask<Void, Void, Void> mSendTask;
	private LoginButton loginButton;
	private final SessionCallback mySessionCallback = new MySessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kakaologin);

		// ---------------------GCM Device register--------------------//
		registerReceiver(mToastMessageReceiver, new IntentFilter(TOAST_MESSAGE_ACTION));

		registerDevice();

		loginButton = (LoginButton) findViewById(R.id.com_kakao_login);
		loginButton.setLoginSessionCallback(mySessionCallback);

	}

	private void registerDevice() {

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.i("PUSH", "regId = " + regId);
		if (regId.equals("")) {
			Log.i("PUSH", "regID = 없음");
			DELAY_TIME = 5000;
			GCMRegistrar.register(getBaseContext(), BasicInfo.PROJECT_ID);

		} else {

			if (GCMRegistrar.isRegisteredOnServer(this)) {
				DELAY_TIME = 5000;
			} else {
				GCMRegistrar.register(getBaseContext(), BasicInfo.PROJECT_ID);
			}

		}

	}

	private final BroadcastReceiver mToastMessageReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String message = intent.getExtras().getString("message");
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	};

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
