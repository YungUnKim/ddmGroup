package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContentView extends LinearLayout {

	private TextView mText01;
	private TextView mText02;
	private TextView mText03;
	private TextView mText04;
	private TextView mText05;

	public ContentView(Context context, ContentItem aItem) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_board, this, true);

		mText01 = (TextView) findViewById(R.id.board_read_count);
		mText01.setText(Integer.toString(aItem.getReadCount()));

		mText02 = (TextView) findViewById(R.id.board_title);
		mText02.setText(aItem.getTitle());

		mText03 = (TextView) findViewById(R.id.board_name);
		mText03.setText(aItem.getName());

		mText04 = (TextView) findViewById(R.id.board_date);
		mText04.setText(aItem.getDate());

		mText05 = (TextView) findViewById(R.id.board_reply_count);
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

}
