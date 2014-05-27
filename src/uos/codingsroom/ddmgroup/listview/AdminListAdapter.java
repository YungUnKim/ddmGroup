package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.AdminItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdminListAdapter extends BaseAdapter {

	private Context mContext;

	private List<AdminItem> mItems = new ArrayList<AdminItem>();

	public AdminListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(AdminItem it) {
		mItems.add(it);
	}

	public void clearItem() {
		mItems.clear();
	}

	public void addItem(int position, AdminItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<AdminItem> lit) {
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
		AdminView itemView;
		if (convertView == null) {
			itemView = new AdminView(mContext, mItems.get(position));
		} else {
			itemView = (AdminView) convertView;

			itemView.setText(0, mItems.get(position).getTitle());
			itemView.setText(1, mItems.get(position).getSubData());
		}

		return itemView;
	}

}
