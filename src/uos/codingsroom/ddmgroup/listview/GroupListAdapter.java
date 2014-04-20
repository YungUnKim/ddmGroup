package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.GroupItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ����� Ŭ���� ����
 * 
 * @author Mike
 * 
 */
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

//	public boolean isSelectable(int position) {
//		try {
//			return mItems.get(position).isSelectable();
//		} catch (IndexOutOfBoundsException ex) {
//			return false;
//		}
//	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		GroupView itemView;
		if (convertView == null) {
			itemView = new GroupView(mContext, mItems.get(position));
		} else {
			itemView = (GroupView) convertView;

//			itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getTitle());

		}

		return itemView;
	}

}
