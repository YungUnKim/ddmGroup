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

public class BoardFragment extends Fragment {
	
	private ListView boardListView;
	private BoardListAdapter boardListAdapter;

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
		
		setListView();
		
		return view;
	}

	public void setListView() {
		boardListAdapter.addItem(new BoardItem(0, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(1, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(2, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(3, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(4, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(5, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(6, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(7, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new BoardItem(8, 1, 16, 5, "제목 입니다.", "재영박", "08/03/1988"));
		
		boardListView.setAdapter(boardListAdapter);		
	}

}