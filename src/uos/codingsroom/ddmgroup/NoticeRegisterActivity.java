package uos.codingsroom.ddmgroup;

import java.io.File;

import uos.codingsroom.ddmgroup.comm.Insert_Notice_Thread;
import uos.codingsroom.ddmgroup.util.LoadingProgressDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

	EditText EditTitle;
	EditText EditMemo;
	Button BtnRegister;
	Button BtnBack;
	TextView title_text;

	String[] ImgPath = new String[5];
	ImageView[] img = new ImageView[5];
	int ImgNum = 0;
	int[] img_id = { R.id.edit_img_01, R.id.edit_img_02, R.id.edit_img_03, R.id.edit_img_04, R.id.edit_img_05 };
	Intent img_intent;

	LoadingProgressDialog progressDialog;
	
	final int REQUEST_CODE_IMAGE_01 = 1;
	final int REQUEST_CODE_IMAGE_02 = 2;
	final int REQUEST_CODE_IMAGE_03 = 3;
	final int REQUEST_CODE_IMAGE_04 = 4;
	final int REQUEST_CODE_IMAGE_05 = 5;

	int REQUEST_CODE_IMAGE = 1;

	TextView groupTitle;
	
	private AlertDialog uploadDialog = null;
	
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

		for (int i = 0; i < 5; i++) {
			img[i] = (ImageView) findViewById(img_id[i]);
			img[i].setOnClickListener(this);
		}

		BtnRegister = (Button) findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(this);
		BtnBack = (Button) findViewById(R.id.button_content_back);
		BtnBack.setOnClickListener(this);
		
		img_intent = new Intent(Intent.ACTION_PICK);
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

		Insert_Notice_Thread iThread = new Insert_Notice_Thread(this, 140, Title, Memo, ImgPath, ImgNum);
		iThread.start(); // 글 업로드하는 스레드

		progressDialog = new LoadingProgressDialog(this);
		progressDialog.startProgressDialog();
	}

	// 글 업로드 성공 후 수행하는 함수
	public void clearContent() {
		for (int i=0; i<5;i++){
			img[i].setImageResource(R.drawable.icon_img_plus);
			ImgPath[i] = null;
		}
		EditTitle.setText("");
		EditMemo.setText("");
		ImgNum = 0;
		
		progressDialog.dismissProgressDialog();
		
		viewMessage("공지사항 작성이 완료되었습니다.",0);
//		finish();
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하는 함수
	public void viewMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	// 핸들러에서 보낸 메시지를 토스트로 출력하고 액티비티를 종료하는 함수
	public void viewMessage(String message, int reaction) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		finish();
	}
		
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;

			Cursor c = this.getContentResolver().query(Uri.parse(data.getDataString()), null, null, null, null);
			c.moveToNext();
			String tempPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			Uri uri = Uri.fromFile(new File(tempPath));
			Log.e("flag", uri.toString() + "\n" + ImgPath);
			c.close();

			final Bitmap b = BitmapFactory.decodeFile(tempPath, options);

			switch (requestCode) {
			case REQUEST_CODE_IMAGE_01:
				img[0].setImageBitmap(b);
				ImgPath[0] = tempPath;
				break;
			case REQUEST_CODE_IMAGE_02:
				img[1].setImageBitmap(b);
				ImgPath[1] = tempPath;
				break;
			case REQUEST_CODE_IMAGE_03:
				img[2].setImageBitmap(b);
				ImgPath[2] = tempPath;
				break;
			case REQUEST_CODE_IMAGE_04:
				img[3].setImageBitmap(b);
				ImgPath[3] = tempPath;
				break;
			case REQUEST_CODE_IMAGE_05:
				img[4].setImageBitmap(b);
				ImgPath[4] = tempPath;
				break;
			default:
				break;
			}
		} catch (Exception e) {

		}

	}// end onActivityResult Method

	private AlertDialog createUploadDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this).setTitle("공지사항 올리기")
				.setMessage("공지사항을 올리시겠습니까?\n그림은 수정 및 삭제가 불가능합니다.\n(그림을 수정하시거나 삭제하시려면 글을 삭제하셔야 합니다.)")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 공지사항 업로드하기
						registerContent();
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});

		return ab.create();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_content_register: // 글 업로드 버튼
			uploadDialog = createUploadDialog();
			uploadDialog.show();
			
			break;
		case R.id.button_content_back:
			finish();
			break;
		case R.id.edit_img_01:
			clickImgButton(0, REQUEST_CODE_IMAGE_01);
			break;
		case R.id.edit_img_02:
			clickImgButton(1, REQUEST_CODE_IMAGE_02);
			break;
		case R.id.edit_img_03:
			clickImgButton(2, REQUEST_CODE_IMAGE_03);
			break;
		case R.id.edit_img_04:
			clickImgButton(3, REQUEST_CODE_IMAGE_04);
			break;
		case R.id.edit_img_05:
			clickImgButton(4, REQUEST_CODE_IMAGE_05);
			break;
		default:
			break;
		}
	}
	
	public void clickImgButton(int position, int requestCode) {
		if (ImgPath[position] == null) {
			ImgNum++;
			img_intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			img_intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(img_intent, requestCode);
		} else {
			ImgNum--;
			ImgPath[position] = null;
			img[position].setImageResource(R.drawable.icon_img_plus);
		}
	}
}
