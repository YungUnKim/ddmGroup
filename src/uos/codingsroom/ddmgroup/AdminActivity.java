package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Get_Manage_Thread;
import uos.codingsroom.ddmgroup.fragments.RegisterFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminActivity extends Activity implements OnClickListener {

	private ImageView backButton;
	private TextView member_count_Text;
	private TextView board_count_Text;
	private TextView contents_count_Text;
	private Button member_manage_btn;
	private Button board_manage_btn;
	private Button notice_manage_btn;
	private Button notice_register_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);

		initializeView();
	}

	// 뷰 초기화하는 함수
	private void initializeView() {
		backButton = (ImageView) findViewById(R.id.button_notice_back_manage);
		backButton.setOnClickListener(this);
		member_count_Text = (TextView) findViewById(R.id.text_member_count);
		board_count_Text = (TextView) findViewById(R.id.text_board_count);
		contents_count_Text = (TextView) findViewById(R.id.text_contents_count);
		member_manage_btn = (Button) findViewById(R.id.button_member_manage);
		member_manage_btn.setOnClickListener(this);
		board_manage_btn = (Button) findViewById(R.id.button_board_manage);
		board_manage_btn.setOnClickListener(this);
		notice_manage_btn = (Button) findViewById(R.id.button_notice_manage);
		notice_manage_btn.setOnClickListener(this);
		notice_register_btn = (Button) findViewById(R.id.button_notice_register_manage);
		notice_register_btn.setOnClickListener(this);

		// 스레드 실행
		Get_Manage_Thread mThread = new Get_Manage_Thread(this, 100);
		mThread.start();
	}

	// 서버에서 받아온 count를 텍스트뷰에 세팅하는 함수
	public void setTextview(String key, int value) {
		if (key.equals("member")) {
			member_count_Text.setText(value + " 명");
		} else if (key.equals("board")) {
			board_count_Text.setText(value + " 개");
		} else if (key.equals("contents")) {
			contents_count_Text.setText(value + " 개");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_notice_back_manage: // 뒤로가기
			finish();
			break;
		case R.id.button_member_manage: // 회원 관리
			Intent intent1 = new Intent(this, ManageMemberActivity.class);		
			startActivity(intent1);
			break;
		case R.id.button_board_manage: // 게시판 관리
			Intent intent2 = new Intent(this, ManageBoardActivity.class);		
			startActivity(intent2);
			break;
		case R.id.button_notice_manage: // 공지사항 보기
			Intent intent3 = new Intent(this, NoticeActivity.class);		
			startActivity(intent3);
			break;
		case R.id.button_notice_register_manage: // 공지사항 작성
			Intent intent4 = new Intent(this, NoticeRegisterActivity.class);
			startActivity(intent4);
			break;

		default:
			break;
		}
	}
}
