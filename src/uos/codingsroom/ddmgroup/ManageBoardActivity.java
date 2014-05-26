package uos.codingsroom.ddmgroup;
import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Get_NoticeList_Thread;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import uos.codingsroom.ddmgroup.listview.NoticeListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBoardActivity extends Activity implements OnClickListener {

	private ListView noticeListView;
	private NoticeListAdapter noticeListAdapter;

	private ImageView backButton;
	private TextView title_text;
	private TextView title_text2;
	
	private ArrayList<NoticeItem> noticeItem = new ArrayList<NoticeItem>(); // 공지사항 리스트 배열
	private int board_cnt = 0; // 게시판수

	private int SELECT_BOARD_NUM = 0; // 선택한 게시판 위치
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		noticeListView = (ListView) findViewById(R.id.listview_notice);
		noticeListAdapter = new NoticeListAdapter(this);

		backButton = (ImageView) findViewById(R.id.button_notice_back);
		backButton.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.text_title);
		title_text.setText("게시판 관리");
		title_text2 = (TextView) findViewById(R.id.text_title2);
		title_text2.setText("게시판 목록");
		
		// new UrlImageDownloadTask(testImage).execute("http://joongangdaily.joins.com/_data/photo/2010/01/25080514.jpg");

//		Get_NoticeList_Thread mThread = new Get_NoticeList_Thread(this, 11);
//		mThread.start();

		setListView();
		
	}

	// 서버에서 받아온 count를 텍스트뷰에 세팅하는 함수
	public void setTextview(String key, int value) {
		if (key.equals("boardber")) {
//			boardber_count_Text.setText(value + " 명");
		} else if (key.equals("board")) {
//			board_count_Text.setText(value + " 개");
		} else if (key.equals("contents")) {
//			contents_count_Text.setText(value + " 개");
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

	// 공지사항 객체를 세팅하는 함수
	public void setNoticeListItem(NoticeItem mItem) {
		noticeItem.add(board_cnt++, mItem);
	}
	
	// 초기에 액티비티 뷰 정의하는 함수
	public void setListView() {

		for (int i = 0; i < board_cnt; i++) {
			noticeListAdapter.addItem(new NoticeItem(noticeItem.get(i).getNum(), // 인덱스 번호
					noticeItem.get(i).getReplyCount(), // 댓글 개수
					noticeItem.get(i).getTitle(), // 제목
					noticeItem.get(i).getDate() // 날짜
			));
		}

		noticeListView.setAdapter(noticeListAdapter);
		noticeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SELECT_BOARD_NUM = arg2; // 댓글의 인덱스
				
				Intent intent = new Intent(ManageBoardActivity.this, ContentsActivity.class);
				intent.putExtra("content_num", noticeItem.get(SELECT_BOARD_NUM).getNum());
				intent.putExtra("group_name", "공지사항");
				intent.putExtra("mode", true);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_notice_back:
			finish();
			break;

		default:
			break;
		}

	}
}
