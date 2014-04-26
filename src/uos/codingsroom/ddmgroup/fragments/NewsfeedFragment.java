package uos.codingsroom.ddmgroup.fragments;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.NewsFeedListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewsfeedFragment extends Fragment {

	private ListView newsfeedListView;
	private NewsFeedListAdapter newsfeedAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

		newsfeedListView = (ListView) view.findViewById(R.id.listview_newsfeed);
		newsfeedAdapter = new NewsFeedListAdapter(this.getActivity());

		setNewsfeedListView();
		return view;
	}
	

	public void setNewsfeedListView() {
		newsfeedAdapter.addItem(new NewsFeedItem(10, 1, "여기는 제목입니다.1", "Codingsroom1", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(9, 2, "여기는 제목입니다.2", "Codingsroom2", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(8, 3, "여기는 제목입니다.3", "Codingsroom3", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(7, 4, "여기는 제목입니다.4", "Codingsroom4", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(6, 5, "여기는 제목입니다.5", "Codingsroom5", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(5, 6, "여기는 제목입니다.6", "Codingsroom6", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(4, 7, "여기는 제목입니다.7", "Codingsroom7", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(3, 8, "여기는 제목입니다.8", "Codingsroom8", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(2, 9, "여기는 제목입니다.9", "Codingsroom9", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(1, 10, "여기는 제목입니다.10", "Codingsroom10", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(10, 1, "여기는 제목입니다.11", "Codingsroom1", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(9, 2, "여기는 제목입니다.12", "Codingsroom2", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(8, 3, "여기는 제목입니다.13", "Codingsroom3", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(7, 4, "여기는 제목입니다.14", "Codingsroom4", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(6, 5, "여기는 제목입니다.15", "Codingsroom5", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(5, 6, "여기는 제목입니다.16", "Codingsroom6", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(4, 7, "여기는 제목입니다.17", "Codingsroom7", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(3, 8, "여기는 제목입니다.18", "Codingsroom8", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(2, 9, "여기는 제목입니다.19", "Codingsroom9", "1988/08/03"));
		newsfeedAdapter.addItem(new NewsFeedItem(1, 10, "여기는 제목입니다.20", "Codingsroom10", "1988/08/03"));

		newsfeedListView.setAdapter(newsfeedAdapter);

		// newsfeedListView.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// // TODO Auto-generated method stub
		// if (firstVisibleItem == 1) {
		// if (layoutHideFlag == false) {
		// layoutHideFlag = true;
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// layoutHideFlag = false;
		// }
		// }, 800);
		// noticeLayout.setVisibility(View.GONE);
		// }
		// } else if (firstVisibleItem == 0) {
		// if (layoutHideFlag == false) {
		// noticeLayout.setVisibility(View.VISIBLE);
		// }
		// }
		// }
		// });
	}

}