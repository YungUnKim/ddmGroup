package uos.codingsroom.ddmgroup.listview;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.AdminItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminView extends LinearLayout {

	private TextView mText01;
	private TextView mText02;

	public AdminView(Context context, AdminItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_admin, this, true);

		// Set Text 01
		mText01 = (TextView) findViewById(R.id.admin_title);
		mText01.setText(aItem.getTitle());

		mText02 = (TextView) findViewById(R.id.admin_subdata);
		mText02.setText(aItem.getSubData());

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
