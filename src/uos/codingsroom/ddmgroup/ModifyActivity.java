package uos.codingsroom.ddmgroup;

import java.io.File;

import uos.codingsroom.ddmgroup.comm.Modify_Content_Thread;
import uos.codingsroom.ddmgroup.comm.Modify_Notice_Thread;
import uos.codingsroom.ddmgroup.util.LoadingProgressDialog;
import uos.codingsroom.ddmgroup.util.SystemValue;
import uos.codingsroom.ddmgroup.util.UrlImageDownloadTask;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyActivity extends Activity implements OnClickListener {

	public LoadingProgressDialog progressDialog;
	
	ImageView backButton;

	TextView groupNameText;
	EditText titleText;
	EditText articleText;
	ImageView myImage;

	Button imageRemoveButton;
	Button imageModifyButton;
	Button backButton2;
	Button modifyButton;

	private boolean title_change = false;
	private boolean article_change = false;
	private boolean img_change = false; // 그림이 바뀌었는지 체크하는 변수

	int REQUEST_CODE_IMAGE = 1;
	private String ImgPath = null; // 이미지 절대 경로

	// 수정할 글의 정보 변수
	private Integer currentContentNum;
	private String title;
	private String article;
	private String imageUrl;
	private String group_name;
	private int group_num;
	private boolean kind; // 공지사항, 일반 글 여부

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressDialog = new LoadingProgressDialog(this);
		
		Bundle bundle = getIntent().getExtras();
		currentContentNum = bundle.getInt("content_num");
		title = bundle.getString("title");
		article = bundle.getString("article");
		imageUrl = bundle.getString("img_url");
		group_name = bundle.getString("group_name");
		group_num = bundle.getInt("group_num");
		kind = bundle.getBoolean("mode"); // 공지사항 여부

		initializeView();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_modify_back:
			finish();
			break;
		case R.id.button_content_back_modify:
			finish();
			break;
		case R.id.button_img_remove:
			if (myImage != null) {
				myImage.setImageBitmap(null);
				img_change = true;
			}
			break;
		case R.id.button_img_modify:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_CODE_IMAGE);
			break;
		case R.id.button_content_modify:
			if (modifycheck()) { // 글 수정 통신하기 전에 수정 여부 확인하기
				modify();
			} else {
				Toast.makeText(getApplicationContext(), "수정된 내용이 없습니다.", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	// 수정하기 위한 통신을 시작하는 함수
	public void modify(){
		String Title = null;
		String Article = null;

		// 글수정 통신 스레드
		Title = titleText.getText().toString();
		Article = articleText.getText().toString();

		if (!img_change) {
			ImgPath = null;
			imageUrl = null;
		} else if (img_change && ImgPath == null) {
			ImgPath = "del";
		}
		
		progressDialog.startProgressDialog();
		if (kind) {
			// 공지사항 수정 스레드
			Modify_Notice_Thread nThread = new Modify_Notice_Thread(ModifyActivity.this, 142, currentContentNum, Title, Article, ImgPath,
					imageUrl);
			nThread.start();
		} else {
			// 글 수정 스레드
			Modify_Content_Thread mThread = new Modify_Content_Thread(ModifyActivity.this, 25, MainActivity.getMyInfoItem().getMyMemNum(),
					group_num, currentContentNum, Title, Article, ImgPath, imageUrl);
			mThread.start();
		}
	}
	
	private void initializeView() {
		setContentView(R.layout.activity_modify);

		backButton = (ImageView) findViewById(R.id.button_modify_back);
		backButton.setOnClickListener(this);
		backButton2 = (Button) findViewById(R.id.button_content_back_modify);
		backButton2.setOnClickListener(this);

		groupNameText = (TextView) findViewById(R.id.text_register_groupname_modify);
		groupNameText.setText(group_name);
		titleText = (EditText) findViewById(R.id.editTitle_modify);
		titleText.setText(title);
		articleText = (EditText) findViewById(R.id.editMemo_modify);
		articleText.setText(article);
		myImage = (ImageView) findViewById(R.id.img_need_modify);

		if (!imageUrl.equals("")) {
			new UrlImageDownloadTask(myImage).execute(SystemValue.imageConn + imageUrl);
			myImage.setVisibility(View.VISIBLE);
		}

		imageRemoveButton = (Button) findViewById(R.id.button_img_remove);
		imageRemoveButton.setOnClickListener(this);
		imageModifyButton = (Button) findViewById(R.id.button_img_modify);
		imageModifyButton.setOnClickListener(this);
		modifyButton = (Button) findViewById(R.id.button_content_modify);
		modifyButton.setOnClickListener(this);
	}

	// 글의 내용이 바뀌었는지 체크하는 함수
	private boolean modifycheck() {
		if (!titleText.getText().toString().equals(title)) {
			title_change = true;
			return true;
		}
		if (!articleText.getText().toString().equals(article)) {
			article_change = true;
			return true;
		}
		if (img_change) { // 그림이 바뀌었을 경우
			return true;
		}

		// 그림 변경 여부도 체크해야함
		return false;
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하는 함수
	public void viewMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		progressDialog.dismissProgressDialog();
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하고 액티비티를 종료하는 함수
	public void viewMessage(String message, int reaction) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		setResult(reaction);
		progressDialog.dismissProgressDialog();
		finish();
	}

	// 그림 선택하는 뷰 이후의 함수
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;

			Cursor c = this.getContentResolver().query(Uri.parse(data.getDataString()), null, null, null, null);
			c.moveToNext();
			ImgPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			Uri uri = Uri.fromFile(new File(ImgPath));
			Log.e("flag", uri.toString() + "\n" + ImgPath);
			c.close();

			final Bitmap b = BitmapFactory.decodeFile(ImgPath, options);
			// Log.i("flag", data.getData().toString());

			// temp_img.setImageURI(data.getData());
			myImage.setImageBitmap(b);
			myImage.setVisibility(View.VISIBLE);

			img_change = true;
		} catch (Exception e) {

		}

	}// end onActivityResult Method
}
