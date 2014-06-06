package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NewsFeedListAdapter extends BaseAdapter {

	private Context mContext;

	private List<NewsFeedItem> mItems = new ArrayList<NewsFeedItem>();

	public NewsFeedListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(NewsFeedItem it) {
		mItems.add(it);
	}

	public void clearItem() {
		mItems.clear();
	}

	public void addItem(int position, NewsFeedItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<NewsFeedItem> lit) {
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
		NewsFeedView itemView;
		if (convertView == null) {
			itemView = new NewsFeedView(mContext, mItems.get(position));
		} else {
			itemView = (NewsFeedView) convertView;

			// itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getTitle());
			itemView.setText(1, mItems.get(position).getGroupName());
			itemView.setText(2, mItems.get(position).getDate());
			itemView.setText(3, "조회 " + Integer.toString(mItems.get(position).getReadCount()));
			itemView.setText(4, Integer.toString(mItems.get(position).getReplyCount()));

		}

		return itemView;
	}

}
