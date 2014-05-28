package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.GroupItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupView extends LinearLayout {

	private TextView mText01;
	private TextView mText02;

	public GroupView(Context context, GroupItem aItem) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_group, this, true);

		mText01 = (TextView) findViewById(R.id.group_title);
		mText01.setText(aItem.getTitle());
		mText02 = (TextView) findViewById(R.id.group_description);
		mText02.setText(aItem.getDescription());

	}

	public void setText(int index, String data) {
		if (index == 0) {
			mText01.setText(data);
		} else if (index == 1) {
			mText02.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
