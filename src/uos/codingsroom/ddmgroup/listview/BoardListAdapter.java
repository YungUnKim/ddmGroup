package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.BoardItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BoardListAdapter extends BaseAdapter {

	private Context mContext;

	private List<BoardItem> mItems = new ArrayList<BoardItem>();

	public BoardListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(BoardItem it) {
		mItems.add(it);
	}

	public void clearItem() {
		mItems.clear();
	}

	public void addItem(int position, BoardItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<BoardItem> lit) {
		mItems = lit;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	// public boolean isSelectable(int position) {
	// try {
	// return mItems.get(position).isSelectable();
	// } catch (IndexOutOfBoundsException ex) {
	// return false;
	// }
	// }

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		BoardView itemView;
		if (convertView == null) {
			itemView = new BoardView(mContext, mItems.get(position));
		} else {
			itemView = (BoardView) convertView;

			// itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getTitle());
			itemView.setText(1, mItems.get(position).getCategory());
			itemView.setText(2, "0");

		}

		return itemView;
	}

}
