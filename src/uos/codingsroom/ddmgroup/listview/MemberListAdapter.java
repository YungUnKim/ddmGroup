package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.MemberItem;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MemberListAdapter extends BaseAdapter {

	private Context mContext;

	private List<MemberItem> mItems = new ArrayList<MemberItem>();

	public MemberListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(MemberItem it) {
		mItems.add(it);
	}

	public void clearItem() {
		mItems.clear();
	}

	public void addItem(int position, MemberItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<MemberItem> lit) {
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
		MemberView itemView;
		if (convertView == null) {
			itemView = new MemberView(mContext, mItems.get(position));
		} else {
			itemView = (MemberView) convertView;

			// itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getName());
			itemView.setText(1, mItems.get(position).getDate());
			itemView.setText(2, "0");

		}

		return itemView;
	}

}
