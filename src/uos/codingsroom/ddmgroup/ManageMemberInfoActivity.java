package uos.codingsroom.ddmgroup;

import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Delete_Per_Thread;
import uos.codingsroom.ddmgroup.comm.Get_MemberInfo_Thread;
import uos.codingsroom.ddmgroup.comm.Insert_Per_Thread;
import uos.codingsroom.ddmgroup.item.MemberItem;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.GlobalApplication;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

public class ManageMemberInfoActivity extends Activity implements OnClickListener {

	KakaoLink kakaoLink;
	KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

	private ImageView backButton;
	private Button blacklistButton;
	private Button managerButton;
	private NetworkImageView profilePictureLayout;

	private TextView NameText;
	private TextView DateText;
	private TextView ConditionText;
	private TextView ContentCntText;
	private TextView ReplyCntText;
	private TextView CodeText;

	private MemberItem mItem = new MemberItem();
	private static Integer mem_num;

	private Boolean isAdmin = false;
	private Boolean isBlocked = false;
	private ArrayList<Integer> board = new ArrayList<Integer>(); // 권한 게시판 번호
	private ArrayList<Integer> level = new ArrayList<Integer>(); // 권한 게시판 레벨

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);

		Bundle bundle = getIntent().getExtras();
		mem_num = bundle.getInt("myNum");

		profilePictureLayout = (NetworkImageView) findViewById(R.id.profile_member_image);

		NameText = (TextView) findViewById(R.id.text_member_name);
		DateText = (TextView) findViewById(R.id.text_member_date);
		ConditionText = (TextView) findViewById(R.id.text_member_condition);
		ContentCntText = (TextView) findViewById(R.id.text_member_content_cnt);
		ReplyCntText = (TextView) findViewById(R.id.text_member_reply_cnt);
		CodeText = (TextView) findViewById(R.id.text_member_code);

		backButton = (ImageView) findViewById(R.id.button_member_back);
		backButton.setOnClickListener(this);
		blacklistButton = (Button) findViewById(R.id.button_reg_blacklist);
		blacklistButton.setOnClickListener(this);
		managerButton = (Button) findViewById(R.id.button_reg_manager);
		managerButton.setOnClickListener(this);
		managerButton.setVisibility(View.GONE);

		// 회원정보 받아오는 스레드
		Get_MemberInfo_Thread mThread = new Get_MemberInfo_Thread(this, 111, mem_num);
		mThread.start();
	}

	// 통신 후에 뷰를 세팅하는 함수
	public void setView() {
		setProfileURL(mItem.getThumbnail());
		NameText.setText(mItem.getName());
		CodeText.setText(Long.toString(mItem.getNum()));
		DateText.setText(mItem.getDate());
		ContentCntText.setText(mItem.getContent_cnt() + " 개");
		ReplyCntText.setText(mItem.getReply_cnt() + " 개");
		ConditionText.setText("일반유저");

		for (int i = 0; i < mItem.getMylevel().size(); i++) {
			if (mItem.getMylevel().get(i) == SystemValue.ADMIN) {
				ConditionText.setText("관리자");
				blacklistButton.setVisibility(View.GONE);
				isAdmin = true;
				break;
			} else if (mItem.getMylevel().get(i) == SystemValue.BLACK) { // 블랙리스트 유저일 경우
				ConditionText.setText("블랙리스트");
				blacklistButton.setText("블랙리스트 취소");
				isBlocked = true;
				break;
			}
		}

		try {
			kakaoLink = KakaoLink.getKakaoLink();
			kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하는 함수
	public void viewMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하고 액티비티를 종료하는 함수
	public void viewMessage(String message, int reaction) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		finish();
	}

	// 객체를 세팅하는 함수
	public void setListItem(MemberItem tItem) {
		mItem = tItem;
	}

	public void setBoardNum(int num) {
		board.add(num);
	}

	public void setLevel(int num) {
		level.add(num);
	}

	// 게시판 번호와 권한 레벨 객체에 저장하기
	public void setPermission() {
		mItem.setMyboard(board);
		mItem.setMylevel(level);
	}

	// 블랙리스트 지정에 따른 버튼 변경
	public void setBlacklist(Boolean what) {
		if (what) { // 블랙리스트 지정
			ConditionText.setText("블랙리스트");
			blacklistButton.setText("블랙리스트 취소");
			isBlocked = true;
			Toast.makeText(this, "해당 유저가 블랙리스트로 지정되었습니다.", Toast.LENGTH_LONG).show();
		} else { // 블랙리스트 취소
			ConditionText.setText("일반 유저");
			blacklistButton.setText("블랙리스트로 등록");
			isBlocked = false;
			Toast.makeText(this, "해당 유저가 블랙리스트에서 취소되었습니다.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_reg_blacklist:
			if(isBlocked) { // 블랙리스트 유저
				// 블랙리스트 취소 스레드
				Delete_Per_Thread dThread = new Delete_Per_Thread(ManageMemberInfoActivity.this, 114, mItem.getNum());
				dThread.start();
			} else { // 일반 유저
				// 블랙리스트 등록 스레드
				Insert_Per_Thread iThread = new Insert_Per_Thread(ManageMemberInfoActivity.this, 112, mItem.getNum(), SystemValue.BLACK, 0);
				iThread.start();
			}

			break;
		case R.id.button_reg_manager:
			// 관리자로 등록 스레드
			break;
		case R.id.button_member_back:
			finish();
			break;

		default:
			break;
		}

	}

	/*
	 * private void alert(String message) { new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.app_name).setMessage(message) .setPositiveButton(android.R.string.ok, null).create().show(); }
	 */

	// 프로필 사진 설정하는 함수
	public void setProfileURL(final String profileImageURL) {
		if (profilePictureLayout != null && profileImageURL != null) {
			Application app = GlobalApplication.getGlobalApplicationContext();
			if (app == null)
				throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
			profilePictureLayout.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
		}
	}
}
