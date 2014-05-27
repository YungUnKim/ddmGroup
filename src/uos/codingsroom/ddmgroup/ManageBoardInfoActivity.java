package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Get_BoardInfo_Thread;
import uos.codingsroom.ddmgroup.item.BoardItem;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBoardInfoActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private ImageView backButton;
	private Button modifyButton;
	private Button deleteButton;
	private Button insertButton;

	ArrayAdapter<String> categorylist;
	private Spinner spinner; // 대분류 스피너
	private Integer SELECT_CATEGORY_NUM;

	private EditText BoardName; // 게시판 이름
	private EditText BoardDSCR; // 게시판 설명
	private TextView BoardNum; // 게시판 번호
	private TextView ContentCnt; // 게시판 글개수

	private Boolean mode = true; // 게시판 수정,삽입 모드
	private BoardItem mItem = new BoardItem();
	private static Integer board_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board_info);

		Bundle bundle = getIntent().getExtras();
		board_num = bundle.getInt("board_num");
		mode = bundle.getBoolean("mode");

		BoardName = (EditText) findViewById(R.id.edit_board_name);
		BoardDSCR = (EditText) findViewById(R.id.edit_board_dscr);
		BoardNum = (TextView) findViewById(R.id.text_board_num);
		ContentCnt = (TextView) findViewById(R.id.text_board_cnt);

		spinner = (Spinner) findViewById(R.id.spinner_board_category);
		spinner.setPrompt("카테고리를 선택하세요.");

		categorylist = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SystemValue.kinds);
		spinner.setAdapter(categorylist);
		spinner.setOnItemSelectedListener(this);

		backButton = (ImageView) findViewById(R.id.button_board_back);
		backButton.setOnClickListener(this);
		modifyButton = (Button) findViewById(R.id.button_modify_board);
		modifyButton.setOnClickListener(this);
		deleteButton = (Button) findViewById(R.id.button_delete_board);
		deleteButton.setOnClickListener(this);
		insertButton = (Button) findViewById(R.id.button_insert_board);
		insertButton.setOnClickListener(this);

		if (mode) { // 게시판 수정모드
			insertButton.setVisibility(View.GONE);
			// 회원정보 받아오는 스레드
			Get_BoardInfo_Thread mThread = new Get_BoardInfo_Thread(this, 121, board_num);
			mThread.start();
		} else { // 게시판 삽입모드
			modifyButton.setVisibility(View.GONE);
			deleteButton.setVisibility(View.GONE);
			spinner.setSelection(0);
		}
	}

	// 통신 후에 뷰를 세팅하는 함수
	public void setView() {
		spinner.setSelection(Integer.parseInt(mItem.getCategory()));
		BoardName.setText(mItem.getTitle());
		BoardDSCR.setText(mItem.getDscr());
		BoardNum.setText(mItem.getNum() + " 번");
		ContentCnt.setText(mItem.getContent_cnt() + " 개");
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
	public void setListItem(BoardItem tItem) {
		mItem = tItem;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_modify_board:
			// 게시판 수정하기
			break;
		case R.id.button_delete_board:
			// 게시판 삭제하기
			break;
		case R.id.button_insert_board:
			// 게시판 삽입하기
			break;
		case R.id.button_board_back:
			finish();
			break;

		default:
			break;
		}

	}

	// 스피너 선택
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView selected = (TextView) arg1;
		SELECT_CATEGORY_NUM = arg2;
		Log.i("MyTag", "선택>>" + selected.getText() + ">>" + SELECT_CATEGORY_NUM);
	}

	// 스피너 미선택
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
