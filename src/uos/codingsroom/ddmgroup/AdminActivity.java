package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Get_Manage_Thread;
import uos.codingsroom.ddmgroup.fragments.RegisterFragment;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends Activity implements OnClickListener {

	private ImageView backButton;
	private TextView member_count_Text;
	private TextView board_count_Text;
	private TextView contents_count_Text;
	private Button member_manage_btn;
	private Button board_manage_btn;
	private Button board_register_btn;
	private Button notice_register_btn;

	private LinearLayout boardRegisterLayout;
	private Button setCategoryBtn;
	private Button makeBoardBtn;
	
	private AlertDialog categoryDialog = null;
	private Integer setMakeCategory;

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
		board_register_btn = (Button) findViewById(R.id.button_board_register);
		board_register_btn.setOnClickListener(this);
		notice_register_btn = (Button) findViewById(R.id.button_notice_register_manage);
		notice_register_btn.setOnClickListener(this);

		boardRegisterLayout = (LinearLayout) findViewById(R.id.layout_board_register);
		boardRegisterLayout.setOnClickListener(this);
		setCategoryBtn = (Button) findViewById(R.id.button_register_category);
		setCategoryBtn.setOnClickListener(this);
		makeBoardBtn = (Button) findViewById(R.id.button_make_board);
		makeBoardBtn.setOnClickListener(this);

		// 스레드 실행
		Get_Manage_Thread mThread = new Get_Manage_Thread(this, 100);
		mThread.start();
	}

	// 서버에서 받아온 count를 텍스트뷰에 세팅하는 함수
	public void setTextview(String key, int value) {
		if (key.equals("member")) {
			member_count_Text.setText(value + " 명");
		} else if (key.equals("board")) {
			board_count_Text.setText(value - 1 + " 개");
		} else if (key.equals("contents")) {
			contents_count_Text.setText(value + " 개");
		}
	}
	
	private AlertDialog createCategoryDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("카테고리");

		ab.setItems(SystemValue.kinds, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setMakeCategory = which;
				setCategoryBtn.setText(SystemValue.kinds[which]);
			}
		});

		ab.setNeutralButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setDismiss(categoryDialog);
			}
		});

		return ab.create();
	}
	
	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
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
		case R.id.button_board_register:
			if (boardRegisterLayout.getVisibility() == View.GONE) {
				boardRegisterLayout.setVisibility(View.VISIBLE);
			} else {
				boardRegisterLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.button_register_category:
			categoryDialog = createCategoryDialog();
			categoryDialog.show();
			break;
		case R.id.button_make_board:
			boardRegisterLayout.setVisibility(View.GONE);
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
