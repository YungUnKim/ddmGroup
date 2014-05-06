package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.UserManagement;

public class ContentsActivity extends Activity implements OnClickListener{
	
	private Button logoutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		logoutButton = (Button) findViewById(R.id.button_logout);
		logoutButton.setOnClickListener(this);
	}

	private void onClickLogout() {
		UserManagement.requestLogout(new LogoutResponseCallback() {
			@Override
			protected void onSuccess(final long userId) {
				redirectLoginActivity();
			}

			@Override
			protected void onFailure(final APIErrorResult apiErrorResult) {
				redirectLoginActivity();
			}
		});
	}

	private void redirectLoginActivity() {
		Intent intent = new Intent(this, KakaoLoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_logout:
			onClickLogout();
			MainActivity.preActivity.finish();
			break;

		default:
			break;
		}

	}
}
