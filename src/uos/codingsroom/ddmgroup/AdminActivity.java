package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Get_Manage_Thread;
import uos.codingsroom.ddmgroup.item.BoardItem;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity implements OnClickListener {

	private ImageView backButton;
	private TextView member_count_Text;
	private TextView board_count_Text;
	private TextView contents_count_Text;
	private Button member_manage_btn;
	private Button board_manage_btn;
	private Button board_register_btn;
	private Button notice_register_btn;

	private EditText groupNameEditText;
	private EditText groupDscrEditText;
	private BoardItem mItem = new BoardItem();

	private LinearLayout boardRegisterLayout;
	private Button setCategoryBtn;
	private Button makeBoardBtn;

	private int member_cnt = 0;
	private int board_cnt = 0;
	private int content_cnt = 0;

	private AlertDialog categoryDialog = null;
	private Boolean initFlag = false;
	private InputMethodManager imm;

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

		groupNameEditText = (EditText) findViewById(R.id.admin_group_name_edit);
		groupDscrEditText = (EditText) findViewById(R.id.admin_group_dscr_edit);

		boardRegisterLayout = (LinearLayout) findViewById(R.id.layout_board_register);
		boardRegisterLayout.setOnClickListener(this);
		setCategoryBtn = (Button) findViewById(R.id.button_register_category);
		setCategoryBtn.setOnClickListener(this);
		makeBoardBtn = (Button) findViewById(R.id.button_make_board);
		makeBoardBtn.setOnClickListener(this);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// 스레드 실행
		Get_Manage_Thread mThread = new Get_Manage_Thread(this, 100);
		mThread.start();
	}

	// 서버에서 받아온 count를 저장하는 함수
	public void setText(String key, int value) {
		if (key.equals("member")) {
			member_cnt = value;
		} else if (key.equals("board")) {
			board_cnt = value - 1;
		} else if (key.equals("contents")) {
			content_cnt = value;
		}
	}

	private AlertDialog createCategoryDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("카테고리");

		ab.setItems(SystemValue.kinds, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mItem.setCategory(which);
				setCategoryBtn.setText(SystemValue.kinds[which]);
				initFlag = true;
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

	// 서버에서 받아온 데이터를 뷰에 세팅하는 함수
	public void setView() {
		member_count_Text.setText(member_cnt + " 명");
		board_count_Text.setText(board_cnt + " 개");
		contents_count_Text.setText(content_cnt + " 개");
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
				board_register_btn.setSelected(true);
			} else {
				boardRegisterLayout.setVisibility(View.GONE);
				board_register_btn.setSelected(false);
			}
			break;
		case R.id.button_register_category:
			categoryDialog = createCategoryDialog();
			categoryDialog.show();
			break;
		case R.id.button_make_board:
			if (groupNameEditText.getText().toString().equals("") || groupDscrEditText.getText().toString().equals("") || initFlag == false) {
				Toast.makeText(getApplicationContext(), "빈칸을 입력해주세요!", Toast.LENGTH_SHORT).show();
			} else {
				boardRegisterLayout.setVisibility(View.GONE);
				board_register_btn.setSelected(false);
				mItem.setTitle(groupNameEditText.getText().toString());
				mItem.setDscr(groupDscrEditText.getText().toString());
				imm.hideSoftInputFromWindow(groupDscrEditText.getWindowToken(), 0);
				// 여기서 모임 등록 통신시작 -- 조인행
				initForm();
				// 스레드에서 위 함수를 호출할 것
			}
			break;
		case R.id.button_notice_register_manage: // 공지사항 작성
			Intent intent4 = new Intent(this, NoticeRegisterActivity.class);
			startActivity(intent4);
			break;
		default:
			break;
		}
	}

	public void initForm() {
		Toast.makeText(getApplicationContext(), "모임이 등록되었습니다.", Toast.LENGTH_SHORT).show();
		groupNameEditText.setText("");
		groupDscrEditText.setText("");
		setCategoryBtn.setText("카테고리");
		initFlag = false;
	}
}
