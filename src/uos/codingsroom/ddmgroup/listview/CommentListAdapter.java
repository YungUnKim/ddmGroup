package uos.codingsroom.ddmgroup.listview;

import java.util.ArrayList;
import java.util.List;

import uos.codingsroom.ddmgroup.item.CommentItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentListAdapter extends BaseAdapter {

	private Context mContext;

	private List<CommentItem> mItems = new ArrayList<CommentItem>();

	public CommentListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(CommentItem it) {
		mItems.add(it);
	}
	
	public void removeItem(int what){
		mItems.remove(what);
	}
	
	public void clearItem(){
		mItems.clear();
	}

	public void addItem(int position, CommentItem it) {
		mItems.add(position, it);
	}

	public void setListItems(List<CommentItem> lit) {
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
		CommentView itemView;
		if (convertView == null) {
			itemView = new CommentView(mContext, mItems.get(position));
		} else {
			itemView = (CommentView) convertView;

//			itemView.setIcon(mItems.get(position).getIcon());
			itemView.setProfileURL(mItems.get(position).getKakaoUrl());
			itemView.setText(0, mItems.get(position).getKakaoName());
			itemView.setText(1, mItems.get(position).getArticle());
			itemView.setText(2, mItems.get(position).getDate());

		}

		return itemView;
	}

}
