package uos.codingsroom.ddmgroup;

import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Get_BoardList_Thread;
import uos.codingsroom.ddmgroup.item.AdminItem;
import uos.codingsroom.ddmgroup.item.BoardItem;
import uos.codingsroom.ddmgroup.listview.AdminListAdapter;
import uos.codingsroom.ddmgroup.listview.BoardListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBoardActivity extends Activity implements OnClickListener {

	private ListView boardListView;
	private AdminListAdapter boardListAdapter;

	private ImageView backButton;
	private TextView title_text;
	private TextView title_text2;

	private ArrayList<AdminItem> boardItem = new ArrayList<AdminItem>(); // 공지사항 리스트 배열
	private int board_cnt = 0; // 게시판수

	private int SELECT_BOARD_NUM = 0; // 선택한 게시판 위치

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_list);

		boardListView = (ListView) findViewById(R.id.listview_admin);
		boardListAdapter = new AdminListAdapter(this);

		backButton = (ImageView) findViewById(R.id.admin_list_back);
		backButton.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.admin_list_title);
		title_text.setText("게시판 관리");
		title_text2 = (TextView) findViewById(R.id.admin_list_header);
		title_text2.setText("게시판 목록");

		Get_BoardList_Thread mThread = new Get_BoardList_Thread(this, 120);
		mThread.start();

		setListView();

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
	public void setListItem(AdminItem mItem) {
		Log.i("MyTag", ">> " + mItem.getTitle());
		boardItem.add(board_cnt++, mItem);
	}

	// 초기에 액티비티 뷰 정의하는 함수
	public void setListView() {

		for (int i = 0; i < board_cnt; i++) {
			boardListAdapter.addItem(new AdminItem(boardItem.get(i).getNum(), // 인덱스 번호
					boardItem.get(i).getTitle(),
					boardItem.get(i).getSubData() + " 그룹"
					));
		}

		boardListView.setAdapter(boardListAdapter);
		boardListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SELECT_BOARD_NUM = arg2; // 댓글의 인덱스
				Log.i("MyTag", "선택 >> " + SELECT_BOARD_NUM);
				/*
				 * Intent intent = new Intent(ManageBoardActivity.this, ContentsActivity.class); intent.putExtra("content_num", boardItem.get(SELECT_BOARD_NUM).getNum());
				 * intent.putExtra("group_name", "공지사항"); intent.putExtra("mode", true); startActivity(intent);
				 */
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.admin_list_back:
			finish();
			break;

		default:
			break;
		}

	}
}