package uos.codingsroom.ddmgroup.listview;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.GlobalApplication;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.item.CommentItem;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView extends LinearLayout {

	private NetworkImageView mIcon;
	private TextView mText01;
	private TextView mText02;
	private TextView mText03;

	public CommentView(Context context, CommentItem aItem) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_comment, this, true);

		mIcon = (NetworkImageView) findViewById(R.id.comment_profile);
		setProfileURL(aItem.getKakaoUrl());

		mText01 = (TextView) findViewById(R.id.comment_name);
		mText01.setText(aItem.getKakaoName());

		mText02 = (TextView) findViewById(R.id.comment_article);
		mText02.setText(aItem.getArticle());

		mText03 = (TextView) findViewById(R.id.comment_date);
		mText03.setText(aItem.getDate());

	}

	public void setProfileURL(final String profileImageURL) {
		if (mIcon != null && profileImageURL != null) {
			Application app = GlobalApplication.getGlobalApplicationContext();
			if (app == null)
				throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
			mIcon.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
		}
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
