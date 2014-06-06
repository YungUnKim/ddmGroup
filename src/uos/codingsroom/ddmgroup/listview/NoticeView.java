package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoticeView extends LinearLayout {

	private TextView mText01;
	private TextView mText02;
	private TextView mText03;

	public NoticeView(Context context, NoticeItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_notice, this, true);

		// Set Text 01
		mText01 = (TextView) findViewById(R.id.notice_title);
		mText01.setText(aItem.getTitle());

		mText02 = (TextView) findViewById(R.id.notice_date);
		mText02.setText(aItem.getDate());

		mText03 = (TextView) findViewById(R.id.notice_reply_count);
		mText03.setText(Integer.toString(aItem.getReplyCount()));

	}

	public void setText(int index, String data) {
		if (index == 0) {
			mText01.setText(data);
		} else if (index == 1) {
			mText02.setText(data);
		} else if (index == 2) {
			mText03.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
