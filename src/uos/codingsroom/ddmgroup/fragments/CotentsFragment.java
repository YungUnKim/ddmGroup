package uos.codingsroom.ddmgroup.fragments;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MakePreferences;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.listview.ContentListAdapter;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CotentsFragment extends Fragment {

	private static Integer currentGroup = 0;
	private static String currentGroupName;

	private ListView boardListView;
	private ContentListAdapter boardListAdapter;
	private ImageView favoriteStar;
	private TextView boardLabelTitle;

	private Boolean favoriteThisGroup = false;

	MakePreferences myPreference;

	Set<String> favoriteStringSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myPreference = new MakePreferences(getActivity());
		favoriteStringSet = myPreference.getMyPreference().getStringSet("favoriteName", new HashSet<String>());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_board, container, false);

		boardListView = (ListView) view.findViewById(R.id.listview_board);
		boardListAdapter = new ContentListAdapter(this.getActivity());

		boardLabelTitle = (TextView) view.findViewById(R.id.board_label_title);
		favoriteStar = (ImageView) view.findViewById(R.id.board_star_favorite);
		favoriteStar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!favoriteThisGroup) {
					Toast.makeText(getActivity(), "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
					favoriteStar.setSelected(true);
					favoriteThisGroup = true;
					favoriteStringSet.add(Integer.toString(currentGroup) + " " + currentGroupName);
					// for (String string : favoriteStringSet) {
					// Log.i("setTag", string);
					// }
					myPreference.getMyPrefEditor().putStringSet("favoriteName", favoriteStringSet);
					myPreference.getMyPrefEditor().commit();

				} else {
					Toast.makeText(getActivity(), "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
					favoriteStar.setSelected(false);
					favoriteThisGroup = false;
					favoriteStringSet.remove(Integer.toString(currentGroup) + " " + currentGroupName);
					// for (String string : favoriteStringSet) {
					// Log.i("setTag", string);
					// }
					myPreference.getMyPrefEditor().putStringSet("favoriteName", favoriteStringSet);
					myPreference.getMyPrefEditor().commit();
				}
			}
		});

		return view;
	}

	public void setTitleLabel(String title) {
		currentGroupName = title;
		boardLabelTitle.setText(title);
	}

	public void setCurrentGroupNum(Integer num) {
		currentGroup = num;
	}

	public void setListView() {
//		Toast.makeText(getActivity(), "현재 그룹 번호는 " + currentGroup, Toast.LENGTH_SHORT).show();
		favoriteStar.setSelected(false);
		favoriteThisGroup = false;

		favoriteStringSet = myPreference.getMyPreference().getStringSet("favoriteName", new HashSet<String>());
		for (String string : favoriteStringSet) {
			String[] dataSet = new String(string).split(" ");
			if (Integer.parseInt(dataSet[0]) == currentGroup) {
				favoriteStar.setSelected(true);
				favoriteThisGroup = true;
				break;
			}
		}

		boardListAdapter.clearItem();

		boardListAdapter.addItem(new ContentItem(0, 10, 10, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 9, 11, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 8, 12, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 7, 13, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 6, 14, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 5, 15, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 4, 16, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 3, 17, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 2, 18, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 1, 19, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 0, 20, "제목 입니다.", "재영박", "08/03/1988"));
		boardListAdapter.addItem(new ContentItem(0, 11, 22, "제목 입니다.", "재영박", "08/03/1988"));

		boardListView.setAdapter(boardListAdapter);

		boardListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final Intent intent = new Intent(getActivity(), ContentsActivity.class);
				startActivity(intent);
			}
		});

	}
}