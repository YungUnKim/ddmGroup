package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Delete_Member_Thread;
import uos.codingsroom.ddmgroup.comm.Delete_Notice_Thread;
import uos.codingsroom.ddmgroup.util.LoadingProgressDialog;
import uos.codingsroom.ddmgroup.util.MakePreferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.APIErrorResult;
import com.kakao.AppActionBuilder;
import com.kakao.GlobalApplication;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.LogoutResponseCallback;
import com.kakao.UnlinkResponseCallback;
import com.kakao.UserManagement;

public class SettingActivity extends Activity implements OnClickListener {
	public LoadingProgressDialog progressDialog;

	KakaoLink kakaoLink;
	KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
	MakePreferences myPreference;

	private ImageView backButton;
	private Button logoutButton;
	private Button adminButton;
	private Button resignButton;
	private Button contactButton;
	private Button kakaoLinkButton;
	private NetworkImageView profilePictureLayout;

	private final String mailAddress = "codingsroom@gmail.com";

	private TextView myNameText;
	private TextView myCodeText;

	private static String nickName;
	private static String profileBigImageURL;
	private static Long kakaoCode;
	private static Integer myNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		progressDialog = new LoadingProgressDialog(this, true);

		Bundle bundle = getIntent().getExtras();
		nickName = bundle.getString("myName");
		profileBigImageURL = bundle.getString("myProfileUrl");
		kakaoCode = bundle.getLong("myCode");
		myNum = bundle.getInt("myNum");

		profilePictureLayout = (NetworkImageView) findViewById(R.id.profile_setting_image);

		myNameText = (TextView) findViewById(R.id.text_setting_myname);
		myCodeText = (TextView) findViewById(R.id.text_setting_mycode);

		backButton = (ImageView) findViewById(R.id.button_setting_back);
		backButton.setOnClickListener(this);

		logoutButton = (Button) findViewById(R.id.button_logout);
		logoutButton.setOnClickListener(this);
		adminButton = (Button) findViewById(R.id.button_admin);
		adminButton.setOnClickListener(this);
		resignButton = (Button) findViewById(R.id.button_resign);
		resignButton.setOnClickListener(this);
		contactButton = (Button) findViewById(R.id.button_contact);
		contactButton.setOnClickListener(this);
		kakaoLinkButton = (Button) findViewById(R.id.kakaolink);
		kakaoLinkButton.setOnClickListener(this);

		if (MainActivity.isAdmin == true) {
			adminButton.setVisibility(View.VISIBLE);
		}

		setProfileURL(profileBigImageURL);
		myNameText.setText(nickName);
		myCodeText.setText(Long.toString(kakaoCode));

		try {
			kakaoLink = KakaoLink.getKakaoLink();
			kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onClickLogout() {
		UserManagement.requestLogout(new LogoutResponseCallback() {
			@Override
			protected void onSuccess(final long userId) {
				redirectLoginActivity("", 1);
			}

			@Override
			protected void onFailure(final APIErrorResult apiErrorResult) {
				redirectLoginActivity("", 1);
			}
		});
	}

	// 회원탈퇴하는 스레드 함수
	private void resignMember(){
//		progressDialog.startProgressDialog();
		// 회원탈퇴하는 스레드
		Delete_Member_Thread dThread = new Delete_Member_Thread(SettingActivity.this, 9, myNum);
		dThread.start();
	}
	
	// 첫 로그인 화면으로 돌아가는 함수
	public void redirectLoginActivity(String message, int where) {
		if(where == 0){	// 회원 탈퇴 후
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//			progressDialog.dismissProgressDialog();
		} else if(where == 1){ // 로그아웃
			
		}
		removePreference();
		Intent intent = new Intent(this, KakaoLoginActivity.class);
		startActivity(intent);
		finish();		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_logout: // 로그아웃
			new AlertDialog.Builder(this).setTitle("로그아웃 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onClickLogout();
					MainActivity.preActivity.finish();
				}
			}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();

			break;
		case R.id.button_admin: // 관리자 메뉴
			final Intent intent = new Intent(SettingActivity.this, AdminActivity.class);
			startActivity(intent);
			break;
		case R.id.button_contact: // 문의사항
			sendMail();
			break;
		case R.id.button_setting_back:
			finish();
			break;
		case R.id.button_resign: // 탈퇴하기
			onClickUnlink();	// 카톡 연동 삭제
			break;
		case R.id.kakaolink:
			sendKakaoLink();
			kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			break;
		default:
			break;
		}

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

	private void sendKakaoLink() {
		try {
			kakaoTalkLinkMessageBuilder.addText("동대문 그룹에 초대합니다! \n 카카오톡으로 간편하게 가입하시고 모임에 대한 이야기를 나눠보세요!");

			kakaoTalkLinkMessageBuilder.addAppButton(
					"앱으로 이동",
					new AppActionBuilder().setAndroidExecuteURLParam("target=main")
							.setIOSExecuteURLParam("target=main", AppActionBuilder.DEVICE_TYPE.PHONE).build());

			kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
		} catch (KakaoParameterException e) {
			alert(e.getMessage());
			Log.i("setTag", "lol");
		}
	}

	private void alert(String message) {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.app_name).setMessage(message)
				.setPositiveButton(android.R.string.ok, null).create().show();
	}

	private void sendMail() {
		Uri emailUri = Uri.parse("mailto:" + mailAddress);

		Intent i = new Intent(Intent.ACTION_SENDTO, emailUri);
		startActivity(i);
	}
	
	private void removePreference(){
		myPreference = new MakePreferences(this);
		
		myPreference.getMyPrefEditor().remove("favoriteName");
		myPreference.commitMyPref();
	}

	public void onClickUnlink() {
		final String appendMessage = getString(com.kakao.sdk.R.string.com_kakao_confirm_unlink);
		new AlertDialog.Builder(this).setMessage(appendMessage)
				.setPositiveButton(getString(com.kakao.sdk.R.string.com_kakao_ok_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UserManagement.requestUnlink(new UnlinkResponseCallback() {
							@Override
							protected void onSuccess(final long userId) {
								resignMember();	// 통신 스레드
							}

							@Override
							protected void onSessionClosedFailure(final APIErrorResult errorResult) {
								resignMember();	// 통신 스레드
							}

							@Override
							protected void onFailure(final APIErrorResult errorResult) {
								// Logger.getInstance().d("failed to unlink. msg=" + ErrorResult);
								resignMember();	// 통신 스레드
							}
						});
						dialog.dismiss();
					}
				}).setNegativeButton(getString(com.kakao.sdk.R.string.com_kakao_cancel_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	public void setProfileURL(final String profileImageURL) {
		if (profilePictureLayout != null && profileImageURL != null) {
			Application app = GlobalApplication.getGlobalApplicationContext();
			if (app == null)
				throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
			profilePictureLayout.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
		}
	}
}
