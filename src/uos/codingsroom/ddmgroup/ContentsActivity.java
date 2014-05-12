package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.item.CommentItem;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.listview.CommentListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentsActivity extends Activity implements OnClickListener {

	private ListView commentsListView;
	private CommentListAdapter commentsListAdapter;
	private View header;

	private String dongHayaHI = "http://th-p.talk.kakao.co.kr/th/talkp/wkfTeGMwVl/KkvewuhCSWa6cKhx9v8mb0/lsfb88_110x110_c.jpg";

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

	private ContentItem conItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);

		initializeView();
		setListView();

	}

	public void setListView() {
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 1", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 2", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 3", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 4", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 5", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 6", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 7", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 8", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 9", "방금 전", "진동하", dongHayaHI));
		commentsListAdapter.addItem(new CommentItem(0, "댓글입니다. 10", "방금 전", "진동하", dongHayaHI));

		commentsListView.setAdapter(commentsListAdapter);
	}

	public void initializeView() {
		commentsListView = (ListView) findViewById(R.id.listview_contents);
		commentsListAdapter = new CommentListAdapter(this);
		header = header();

		commentsListView.addHeaderView(header, null, false);

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

	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header_contents, null);
		return header;
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

	// contentItem 세팅하는 함수
	public void setContentItem(ContentItem mItem) {
		conItem = new ContentItem();
		conItem = mItem;
	}

	// View에 contentItem 집어넣는 함수
	public void setContentView() {
		/*
		 * contentsReadCount; contentsReplyCount; contentsGroupName; contentsTitle; contentsName; contentsDate; contentsArticle;
		 */
		// contentsImage;
	}
}
