package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentsActivity extends Activity implements OnClickListener{
	
	private TextView contentsReadCount;
	private TextView contentsReplyCount;
	private TextView contentsGroupName;
	private TextView contentsTitle;
	private TextView contentsName;
	private TextView contentsDate;
	private TextView contentsArticle;
	
	private ImageView contentsImage;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);
		
		initializeView();
		
	}
	
	public void initializeView(){
		contentsReadCount = (TextView) findViewById(R.id.contents_read_count);
		contentsReplyCount = (TextView) findViewById(R.id.contents_reply_count);
		contentsGroupName = (TextView) findViewById(R.id.contents_group_name);
		contentsTitle = (TextView) findViewById(R.id.contents_title);
		contentsName = (TextView) findViewById(R.id.contents_name);
		contentsDate = (TextView) findViewById(R.id.contents_date);
		contentsArticle = (TextView) findViewById(R.id.contents_article);
		
		contentsImage = (ImageView) findViewById(R.id.contents_image);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}



//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.button_logout:
//			onClickLogout();
//			MainActivity.preActivity.finish();
//			break;
//
//		default:
//			break;
//		}
//	}
}
