package uos.codingsroom.ddmgroup;

import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Get_NoticeList_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Notice_Thread;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import uos.codingsroom.ddmgroup.listview.NoticeListAdapter;
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
import android.widget.Toast;

public class NoticeActivity extends Activity implements OnClickListener {

	private ListView noticeListView;
	private NoticeListAdapter noticeListAdapter;

	private ImageView backButton;

	private ArrayList<NoticeItem> noticeItem = new ArrayList<NoticeItem>(); // 공지사항 리스트 배열
	private int noti_cnt = 0; // 공지사항 개수

	private int SELECT_NOTICE_NUM = 0; // 선택한 공지사항 위치

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		noticeListView = (ListView) findViewById(R.id.listview_notice);
		noticeListAdapter = new NoticeListAdapter(this);

		backButton = (ImageView) findViewById(R.id.button_notice_back);
		backButton.setOnClickListener(this);

		// new UrlImageDownloadTask(testImage).execute("http://joongangdaily.joins.com/_data/photo/2010/01/25080514.jpg");

		Get_NoticeList_Thread mThread = new Get_NoticeList_Thread(this, 11);
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

	// 공지사항 객체를 세팅하는 함수
	public void setNoticeListItem(NoticeItem mItem) {
		noticeItem.add(noti_cnt++, mItem);
	}

	public void setListView() {

		for (int i = 0; i < noti_cnt; i++) {
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
				SELECT_NOTICE_NUM = arg2; // 댓글의 인덱스
				
				Intent intent = new Intent(NoticeActivity.this, ContentsActivity.class);
				intent.putExtra("content_num", noticeItem.get(SELECT_NOTICE_NUM).getNum());
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
