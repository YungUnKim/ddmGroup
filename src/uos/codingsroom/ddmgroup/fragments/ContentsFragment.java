package uos.codingsroom.ddmgroup.fragments;

import java.util.HashSet;
import java.util.Set;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.MakePreferences;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.listview.ContentListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentsFragment extends Fragment implements OnClickListener {

	private static Integer currentGroup = 0;
	private static String currentGroupName;

	private ListView boardListView;
	private ContentListAdapter boardListAdapter;
	private ImageView favoriteStar;
	private TextView boardLabelTitle;
	private LinearLayout boardMenuLayout;

	private TextView boardBackButton;
	private TextView boardRegisterButton;
	private ImageView boardPrevButton;
	private ImageView boardNextButton;

	private Boolean favoriteThisGroup = false;

	MakePreferences myPreference;

	private int mLastFirstVisibleItem;
	private boolean mIsScrollingUp;

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
		boardMenuLayout = (LinearLayout) view.findViewById(R.id.layout_board_menu);

		boardBackButton = (TextView) view.findViewById(R.id.button_board_home);
		boardRegisterButton = (TextView) view.findViewById(R.id.button_board_register);
		boardPrevButton = (ImageView) view.findViewById(R.id.button_board_prev);
		boardNextButton = (ImageView) view.findViewById(R.id.button_board_next);

		boardBackButton.setOnClickListener(this);
		boardRegisterButton.setOnClickListener(this);
		boardPrevButton.setOnClickListener(this);
		boardNextButton.setOnClickListener(this);

		favoriteStar = (ImageView) view.findViewById(R.id.board_star_favorite);
		favoriteStar.setOnClickListener(this);

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
		// Toast.makeText(getActivity(), "현재 그룹 번호는 " + currentGroup, Toast.LENGTH_SHORT).show();
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

		boardListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				final ListView lw = (ListView) view;

				if (view.getId() == lw.getId()) {
					final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
						mIsScrollingUp = false;
					} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
						mIsScrollingUp = true;
					}

					mLastFirstVisibleItem = currentFirstVisibleItem;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					boardMenuLayout.setVisibility(View.VISIBLE);
				} else if (mIsScrollingUp) {
					boardMenuLayout.setVisibility(View.VISIBLE);
				} else {
					boardMenuLayout.setVisibility(View.GONE);
				}
			}
		});

		boardListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final Intent intent = new Intent(getActivity(), ContentsActivity.class);
				intent.putExtra("board_name", "테스트");
				intent.putExtra("board_num", 1);
				intent.putExtra("content_num", 9);
				intent.putExtra("mem_num", 16);
				
				startActivity(intent);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_board_home:
			((MainActivity) getActivity()).showFragment(0, false);
			break;
		case R.id.button_board_register:
			((MainActivity) getActivity()).showFragment(2, false);
			break;
		case R.id.button_board_prev:
			
			break;
		case R.id.button_board_next:

			break;
		case R.id.board_star_favorite:
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
			break;

		default:
			break;
		}

	}
}