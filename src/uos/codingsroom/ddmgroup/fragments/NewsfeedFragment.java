package uos.codingsroom.ddmgroup.fragments;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Get_Notice_Three_Thread;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.NewsFeedListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NewsfeedFragment extends Fragment implements OnClickListener {

	private ListView newsfeedListView;
	private NewsFeedListAdapter newsfeedAdapter;
	private LinearLayout noticeLayout;

	private TextView[] noticeTitleText = new TextView[3];
	private int noticeNum[] = new int[3];
	private String noticeTitle[] = new String[3];

	private int noticeCount = 0;
	
	private int mLastFirstVisibleItem;
	private boolean mIsScrollingUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();

		Get_Notice_Three_Thread mThread = new Get_Notice_Three_Thread(this.getActivity(), 11);
		mThread.start();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		
		noticeLayout = (LinearLayout) view.findViewById(R.id.notice_layout);

		noticeTitleText[0] = (TextView) view.findViewById(R.id.notice_1);
		noticeTitleText[1] = (TextView) view.findViewById(R.id.notice_2);
		noticeTitleText[2] = (TextView) view.findViewById(R.id.notice_3);

		for (int i = 0; i < 3; i++) {
			noticeTitleText[i].setOnClickListener(this);
		}

		newsfeedListView = (ListView) view.findViewById(R.id.listview_newsfeed);
		newsfeedAdapter = new NewsFeedListAdapter(this.getActivity());

		// setNewsfeedListView();

		return view;
	}

	// 공지사항 1개씩 저장
	public void setNotice(int index, String title, int num) {
		noticeTitle[index] = title;
		noticeNum[index] = num;
		noticeCount++;
	}

	// 공지사항 출력 (3개)
	public void setNoticeTitle() {
		Log.i("MyTag", "공지사항 입력된다." + noticeCount);
		for (int i = 0; i < noticeCount; i++) {
			noticeTitleText[i].setText(noticeTitle[i]);
			noticeTitleText[i].setVisibility(View.VISIBLE);
		}
	}

	// 뉴스피드 1개씩 입력
	public void setNewsFeed(NewsFeedItem newsFeedItem) {
		newsfeedAdapter.addItem(newsFeedItem);

	}

	// 뉴스피드 출력(20개)
	public void setNewsFeedTitle() {
		// Log.i("MyTag", "뉴스피드입력된다." + noticeCount);
		newsfeedListView.setAdapter(newsfeedAdapter);
		newsfeedListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				moveToConetentsActivity();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notice_1:
			moveToConetentsActivity();
			break;
		case R.id.notice_2:
			moveToConetentsActivity();
			break;
		case R.id.notice_3:
			moveToConetentsActivity();
			break;

		default:
			break;
		}
	}
	
	public void moveToConetentsActivity(){
		final Intent intent = new Intent(getActivity(), ContentsActivity.class);
		startActivity(intent);
	}

}