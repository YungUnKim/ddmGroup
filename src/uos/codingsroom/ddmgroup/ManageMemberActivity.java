package uos.codingsroom.ddmgroup;
import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Get_MemberList_Thread;
import uos.codingsroom.ddmgroup.item.MemberItem;
import uos.codingsroom.ddmgroup.listview.MemberListAdapter;
import android.app.Activity;
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

public class ManageMemberActivity extends Activity implements OnClickListener {

	private ListView memberListView;
	private MemberListAdapter memberListAdapter;

	private ImageView backButton;
	private TextView title_text;
	private TextView title_text2;
	
	private ArrayList<MemberItem> memberItem = new ArrayList<MemberItem>(); // 공지사항 리스트 배열
	private int mem_cnt = 0; // 회원수

	private int SELECT_MEM_NUM = 0; // 선택한 회원번호 위치
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		memberListView = (ListView) findViewById(R.id.listview_notice);
		memberListAdapter = new MemberListAdapter(this);

		backButton = (ImageView) findViewById(R.id.button_notice_back);
		backButton.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.text_title);
		title_text.setText("회원 관리");
		title_text2 = (TextView) findViewById(R.id.text_title2);
		title_text2.setText("회원 목록");
		
		// new UrlImageDownloadTask(testImage).execute("http://joongangdaily.joins.com/_data/photo/2010/01/25080514.jpg");

		Get_MemberList_Thread mThread = new Get_MemberList_Thread(this, 110);
		mThread.start();
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
	public void setListItem(MemberItem mItem) {
		Log.i("MyTag",">> " + mItem.getName());
		memberItem.add(mem_cnt++, mItem);
	}
	
	// 초기에 액티비티 뷰 정의하는 함수
	public void setListView() {

		for (int i = 0; i < mem_cnt; i++) {
			memberListAdapter.addItem(new MemberItem(memberItem.get(i).getNum(), // 인덱스 번호
					memberItem.get(i).getName(),	// 이름
					memberItem.get(i).getDate()	// 날짜
			));
		}

		memberListView.setAdapter(memberListAdapter);
		memberListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SELECT_MEM_NUM = arg2; // 댓글의 인덱스
				Log.i("MyTag","선택 >> " + SELECT_MEM_NUM);
				/*
				Intent intent = new Intent(ManageMemberActivity.this, ContentsActivity.class);
				intent.putExtra("content_num", memberItem.get(SELECT_MEM_NUM).getNum());
				intent.putExtra("group_name", "공지사항");
				intent.putExtra("mode", true);
				startActivity(intent);
				*/
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
