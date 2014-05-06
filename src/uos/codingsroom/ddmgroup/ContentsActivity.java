package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentsActivity extends Activity implements OnClickListener {

	private TextView contentsReadCount;
	private TextView contentsReplyCount;
	private TextView contentsGroupName;
	private TextView contentsTitle;
	private TextView contentsName;
	private TextView contentsDate;
	private TextView contentsArticle;

	private ImageView contentsImage;

	private ImageView contentsLogo;
	private ImageView contentsMenuButton;

	private LinearLayout menuLayout;
	private RelativeLayout menuHelperLayout;

	private TextView menuBackButton;
	private TextView menuEditButton;
	private TextView menuDeleteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);

		initializeView();

	}

	public void initializeView() {
		contentsReadCount = (TextView) findViewById(R.id.contents_read_count);
		contentsReplyCount = (TextView) findViewById(R.id.contents_reply_count);
		contentsGroupName = (TextView) findViewById(R.id.contents_group_name);
		contentsTitle = (TextView) findViewById(R.id.contents_title);
		contentsName = (TextView) findViewById(R.id.contents_name);
		contentsDate = (TextView) findViewById(R.id.contents_date);
		contentsArticle = (TextView) findViewById(R.id.contents_article);

		contentsImage = (ImageView) findViewById(R.id.contents_image);

		contentsLogo = (ImageView) findViewById(R.id.contents_logo_img);
		contentsLogo.setOnClickListener(this);
		contentsMenuButton = (ImageView) findViewById(R.id.contents_menu_btn);
		contentsMenuButton.setOnClickListener(this);

		menuLayout = (LinearLayout) findViewById(R.id.contents_menu_layout);
		menuHelperLayout = (RelativeLayout) findViewById(R.id.contents_menu_helper_layout);
		menuHelperLayout.setOnClickListener(this);

		menuBackButton = (TextView) findViewById(R.id.contents_return_btn);
		menuBackButton.setOnClickListener(this);
		menuEditButton = (TextView) findViewById(R.id.contents_edit_btn);
		menuEditButton.setOnClickListener(this);
		menuDeleteButton = (TextView) findViewById(R.id.contents_delete_btn);
		menuDeleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contents_logo_img:
			menuLayout.setVisibility(View.VISIBLE);
			menuHelperLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.contents_menu_btn:
			menuLayout.setVisibility(View.VISIBLE);
			menuHelperLayout.setVisibility(View.VISIBLE);
			break;
			
		case R.id.contents_return_btn:
			menuLayout.setVisibility(View.GONE);
			menuHelperLayout.setVisibility(View.GONE);
			break;
			
		case R.id.contents_menu_helper_layout:
			menuLayout.setVisibility(View.GONE);
			menuHelperLayout.setVisibility(View.GONE);
			break;
			
		case R.id.contents_edit_btn:

			break;
			
		case R.id.contents_delete_btn:

			break;

		default:
			break;
		}

	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.button_logout:
	// onClickLogout();
	// MainActivity.preActivity.finish();
	// break;
	//
	// default:
	// break;
	// }
	// }
}
