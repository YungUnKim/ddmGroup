package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsFeedView extends LinearLayout {

	private TextView mText01;
	private TextView mText02;
	private TextView mText03;
	private TextView mText04;
	private TextView mText05;

	public NewsFeedView(Context context, NewsFeedItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_newsfeed, this, true);

		// Set Text 01
		mText01 = (TextView) findViewById(R.id.newsfeed_title);
		mText01.setText(aItem.getTitle());

		mText02 = (TextView) findViewById(R.id.newsfeed_group_name);
		mText02.setText(aItem.getGroupName());

		mText03 = (TextView) findViewById(R.id.newsfeed_date);
		mText03.setText(aItem.getDate());

		mText04 = (TextView) findViewById(R.id.newsfeed_read_count);
		mText04.setText("조회 " + Integer.toString(aItem.getReadCount()));

		mText05 = (TextView) findViewById(R.id.newsfeed_reply_count);
		mText05.setText(Integer.toString(aItem.getReplyCount()));
	}

	public void setText(int index, String data) {
		if (index == 0) {
			mText01.setText(data);
		} else if (index == 1) {
			mText02.setText(data);
		} else if (index == 2) {
			mText03.setText(data);
		} else if (index == 3) {
			mText04.setText(data);
		} else if (index == 4) {
			mText05.setText(data);
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
