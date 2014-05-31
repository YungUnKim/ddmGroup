package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.GroupItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GroupListAdapter extends BaseAdapter {

	private Context mContext;

	private List<GroupItem> mItems = new ArrayList<GroupItem>();

	public GroupListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(GroupItem it) {
		mItems.add(it);
	}
	
	public void clearItem(){
		mItems.clear();
	}

	public void removeItem(int position){
		mItems.remove(position);
	}
	
	public void addItem(int position, GroupItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<GroupItem> lit) {
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

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		GroupView itemView;
		if (convertView == null) {
			itemView = new GroupView(mContext, mItems.get(position));
		} else {
			itemView = (GroupView) convertView;

			itemView.setText(0, mItems.get(position).getTitle());
			itemView.setText(1, mItems.get(position).getDescription());

		}

		return itemView;
	}

}
