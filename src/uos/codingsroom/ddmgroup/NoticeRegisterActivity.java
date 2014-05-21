package uos.codingsroom.ddmgroup;

import java.io.File;

import uos.codingsroom.ddmgroup.comm.Insert_Notice_Thread;
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

public class NoticeRegisterActivity extends Activity implements OnClickListener {

	private static String currentGroupName = "공지사항 작성";

	private String ImgPath;

	EditText EditTitle;
	EditText EditMemo;
	Button BtnUpload;
	Button BtnRegister;
	Button BtnBack;
	ImageView temp_img;
	TextView title_text;
	
	int REQUEST_CODE_IMAGE = 1;

	TextView groupTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);

		EditTitle = (EditText) findViewById(R.id.editTitle);
		EditMemo = (EditText) findViewById(R.id.editMemo);
		groupTitle = (TextView) findViewById(R.id.text_register_groupname);
		groupTitle.setText(currentGroupName);
		title_text = (TextView) findViewById(R.id.text_register_groupname_textview);
		title_text.setText("");
		BtnUpload = (Button) findViewById(R.id.button_img_upload);
		temp_img = (ImageView) findViewById(R.id.temp_img);
		BtnUpload.setOnClickListener(this);
		BtnRegister = (Button) findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(this);
		BtnBack = (Button) findViewById(R.id.button_content_back);
		BtnBack.setOnClickListener(this);
	}

	// 글 업로드 하는 함수
	public void registerContent() {
		String Title = EditTitle.getText().toString();
		String Memo = EditMemo.getText().toString();

		if (Title.equals("") || Memo.equals("")) {
			Toast.makeText(this, "필수입력사항을 입력하세요!", Toast.LENGTH_LONG).show();
			// EditText가 비워있으면 null 오류가 뜨기 때문에 리턴해줘서 다시 입력하게 해야한다.
			return;
		}

		Insert_Notice_Thread iThread = new Insert_Notice_Thread(this, 140, Title, Memo, ImgPath);
		iThread.start(); // 글 업로드하는 스레드

	}

	// 글 업로드 성공 후 수행하는 함수
	public void clearContent() {
		temp_img.setImageBitmap(null);
		EditTitle.setText("");
		EditMemo.setText("");
		ImgPath = null;

		finish();
	}

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

			// temp_img.setImageURI(data.getData());
			temp_img.setImageBitmap(b);
			temp_img.setVisibility(View.VISIBLE);
		} catch (Exception e) {

		}

	}// end onActivityResult Method

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_img_upload: // 이미지 업로드
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_CODE_IMAGE);
			break;
		case R.id.button_content_register: // 글 업로드 버튼
			registerContent();
			break;
		case R.id.button_content_back:
			finish();
			break;

		default:
			break;
		}

	}
}
