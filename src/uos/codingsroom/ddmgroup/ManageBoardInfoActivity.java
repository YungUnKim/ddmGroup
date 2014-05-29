package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.comm.Delete_Board_Thread;
import uos.codingsroom.ddmgroup.comm.Get_BoardInfo_Thread;
import uos.codingsroom.ddmgroup.item.BoardItem;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBoardInfoActivity extends Activity implements OnClickListener{

	private ImageView backButton;
	private Button modifyButton;
	private Button deleteButton;

	ArrayAdapter<String> categorylist;
	private Button categoryBtn;

	private TextView BoardName; // 게시판 이름
	private EditText BoardDSCR; // 게시판 설명
	private TextView BoardNum; // 게시판 번호
	private TextView ContentCnt; // 게시판 글개수

	private BoardItem mItem = new BoardItem();
	private static Integer board_num;
	private AlertDialog categoryDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board_info);

		Bundle bundle = getIntent().getExtras();
		board_num = bundle.getInt("board_num");

		BoardName = (TextView) findViewById(R.id.board_info_title_text);
		BoardDSCR = (EditText) findViewById(R.id.board_info_dscr_edit);
		BoardNum = (TextView) findViewById(R.id.board_info_index_text);
		ContentCnt = (TextView) findViewById(R.id.board_info_cnt_text);

		categoryBtn = (Button) findViewById(R.id.board_info_category_button);
		categoryBtn.setOnClickListener(this);

		backButton = (ImageView) findViewById(R.id.button_board_back);
		backButton.setOnClickListener(this);
		modifyButton = (Button) findViewById(R.id.button_modify_board);
		modifyButton.setOnClickListener(this);
		deleteButton = (Button) findViewById(R.id.button_delete_board);
		deleteButton.setOnClickListener(this);
		
		Get_BoardInfo_Thread mThread = new Get_BoardInfo_Thread(this, 121, board_num);
		mThread.start();
		
	}
	
	private AlertDialog createCategoryDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("카테고리");

		ab.setItems(SystemValue.kinds, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mItem.setCategory(which);
				categoryBtn.setText(SystemValue.kinds[which]);
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

	// 통신 후에 뷰를 세팅하는 함수
	public void setView() {
		categoryBtn.setText(SystemValue.kinds[mItem.getCategory()]);
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
		case R.id.board_info_category_button:
			categoryDialog = createCategoryDialog();
			categoryDialog.show();
			break;
		case R.id.button_modify_board:
			mItem.setDscr(BoardDSCR.getText().toString());
			
			//여기서 mItem으로 통신 시작
			break;
		case R.id.button_delete_board:
			// 게시판 삭제하기
			Delete_Board_Thread dThread = new Delete_Board_Thread(ManageBoardInfoActivity.this,124,mItem.getNum());
			dThread.start();
			break;
		case R.id.button_board_back:
			finish();
			break;

		default:
			break;
		}

	}
}
