package uos.codingsroom.ddmgroup.fragments;

import java.util.HashSet;
import java.util.Set;

import uos.codingsroom.ddmgroup.ContentIntent;
import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.MakePreferences;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Get_ContentList_Thread;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.listview.ContentListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private static Integer end = -1;
	private static Integer start = -1;
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
	
	public void contentFragmentStart(){
		Get_ContentList_Thread mThread = new Get_ContentList_Thread(this.getActivity(), 13);
		mThread.start();
	}

	public void setTitleLabel(String title) {
		currentGroupName = title;
		boardLabelTitle.setText(title);
	}

	public void setCurrentGroupNum(Integer num) {
		currentGroup = num;
	}
	public int getCurrentGroupNum() {
		return currentGroup;
	}
	//게시글 끝, 시작 넘버 알아오고 저장하는 함수들
	public int getEndNum() {
		return end;
	}
	public void setEndNum(int num){
		end = num;
	}
	public int getStartNum() {
		return start;
	}
	public void setStartNum(int num){
		start = num;
	}
	
	//게시글 아이템 삭제
	public void cleanListview() {
		boardListAdapter.clearItem();		
	}	

	//게시글 1개씩 입력	
	public void addListview(ContentItem contentItem) {
		boardListAdapter.addItem(contentItem);	
	}
	
	//내용 출력
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
/*
		boardListAdapter.clearItem();
		boardListAdapter.addItem(new ContentItem(0, 10, 10, "제목 입니다.", "재영박", "08/03/1988"));
*/
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
				ContentItem curItem = (ContentItem) boardListAdapter.getItem(position);
				moveToContentsActivity(curItem.getIndexNum());				
			}
		});

	}
	
	public void moveToContentsActivity(Integer contentNum){
		Intent intent = new Intent(this.getActivity(),ContentsActivity.class);
		intent.putExtra("content_num", contentNum);
		intent.putExtra("group_name", currentGroupName);
		intent.putExtra("mode", false);
		startActivity(intent);
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
			//이전
			break;
		case R.id.button_board_next:
			//이후
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