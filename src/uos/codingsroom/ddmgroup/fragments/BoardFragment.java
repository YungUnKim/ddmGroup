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

public class BoardFragment extends Fragment {

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

		return view;
	}

}