package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.GroupItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupView extends LinearLayout {

	private TextView mText01;

	public GroupView(Context context, GroupItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_group, this, true);

		// Set Text 01
		mText01 = (TextView) findViewById(R.id.group_title);
		mText01.setText(aItem.getTitle());
	}

	/**
	 * set Text
	 * 
	 * @param index
	 * @param data
	 */
	public void setText(int index, String data) {
		if (index == 0) {
			mText01.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * set Icon
	 * 
	 * @param icon
	 */
	// public void setIcon(Drawable icon) {
	// mIcon.setImageDrawable(icon);
	// }
}
