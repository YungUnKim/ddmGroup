package uos.codingsroom.ddmgroup.fragments;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.BoardItem;
import uos.codingsroom.ddmgroup.listview.BoardListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BoardFragment extends Fragment {
	
	private static Integer currentGroup = 0;

	private ListView boardListView;
	private BoardListAdapter boardListAdapter;
	
	private TextView boardLabelTitle;

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
		final View view = inflater.inflate(R.layout.fragment_board, container, false);

		boardListView = (ListView) view.findViewById(R.id.listview_board);
		boardListAdapter = new BoardListAdapter(this.getActivity());
		
		boardLabelTitle = (TextView) view.findViewById(R.id.board_label_title);

		return view;
	}
	
	public void setTitleLabel(String title){
		boardLabelTitle.setText(title);
	}

	public void setCurrentGroupNum(Integer num){
		currentGroup = num;
	}
	
	public void setListView() {
		Toast.makeText(getActivity(), "현재 그룹 번호 : " + currentGroup, Toast.LENGTH_SHORT).show();
		
		boardListAdapter.addItem(new BoardItem(0, 10, 10, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 9, 11, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 8, 12, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 7, 13, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 6, 14, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 5, 15, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 4, 16, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 3, 17, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 2, 18, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 1, 19, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 0, 20, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(0, 11, 22, "제목 입니다.", "재영박", "08/03/1988"));
		
		
		boardListView.setAdapter(boardListAdapter);		
	}
}