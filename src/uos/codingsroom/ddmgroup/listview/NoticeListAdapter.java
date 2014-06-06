package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.NoticeItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoticeListAdapter extends BaseAdapter {

	private Context mContext;

	private List<NoticeItem> mItems = new ArrayList<NoticeItem>();

	public NoticeListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(NoticeItem it) {
		mItems.add(it);
	}

	public void clearItem() {
		mItems.clear();
	}
	
	public void removeItem(int position){
		mItems.remove(position);
	}

	public void addItem(int position, NoticeItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<NoticeItem> lit) {
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
		NoticeView itemView;
		if (convertView == null) {
			itemView = new NoticeView(mContext, mItems.get(position));
		} else {
			itemView = (NoticeView) convertView;

			// itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getTitle());
			itemView.setText(1, mItems.get(position).getDate());
			itemView.setText(2, Integer.toString(mItems.get(position).getReplyCount()));

		}

		return itemView;
	}

}
