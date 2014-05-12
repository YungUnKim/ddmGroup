package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.item.NoticeItem;
import uos.codingsroom.ddmgroup.listview.NoticeListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class NoticeActivity extends Activity implements OnClickListener {
	
	private ListView noticeListView;
	private NoticeListAdapter noticeListAdapter;
	
	private ImageView backButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		
		noticeListView = (ListView) findViewById(R.id.listview_notice);
		noticeListAdapter = new NoticeListAdapter(this);
				
		backButton = (ImageView) findViewById(R.id.button_notice_back);
		backButton.setOnClickListener(this);
		
		setListView();
	}
	
	public void setListView(){
		noticeListAdapter.addItem(new NoticeItem(0, 1, "아아 공지사항 말씀드립니다. 1", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 3, "아아 공지사항 말씀드립니다. 2", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 12, "아아 공지사항 말씀드립니다. 3", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 10, "아아 공지사항 말씀드립니다. 4", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 9, "아아 공지사항 말씀드립니다. 5", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 7, "아아 공지사항 말씀드립니다. 6", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 6, "아아 공지사항 말씀드립니다. 7", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 10, "아아 공지사항 말씀드립니다. 4", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 9, "아아 공지사항 말씀드립니다. 5", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 7, "아아 공지사항 말씀드립니다. 6", "880803"));
		noticeListAdapter.addItem(new NoticeItem(0, 6, "아아 공지사항 말씀드립니다. 7", "880803"));
		
		
		noticeListView.setAdapter(noticeListAdapter);
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
