package uos.codingsroom.ddmgroup;

import static uos.codingsroom.ddmgroup.BasicInfo.TOAST_MESSAGE_ACTION;

import java.io.File;

import uos.codingsroom.ddmgroup.comm.Insert_Notice_Thread;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class NoticeRegisterActivity extends Activity implements OnClickListener {

	private static Integer currentGroup = 0;
	private static String currentGroupName = "공지사항 작성";

	private String ImgPath;

	EditText EditTitle;
	EditText EditMemo;
	Button BtnUpload;
	Button BtnRegister;
	Button BtnBack;
	ImageView temp_img;
	int REQUEST_CODE_IMAGE = 1;

	// GCM 메세지를 보낼 변수
	AsyncTask<Void, Void, Void> mSendTask;
	Sender sender;

	TextView groupTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("MyTag","NoticeRegisterActivity >> ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);

		EditTitle = (EditText) findViewById(R.id.editTitle);
		EditMemo = (EditText) findViewById(R.id.editMemo);
		sender = new Sender(BasicInfo.GOOGLE_API_KEY);
		groupTitle = (TextView) findViewById(R.id.text_register_groupname);
		groupTitle.setText(currentGroupName);

		BtnUpload = (Button) findViewById(R.id.button_img_upload);
		temp_img = (ImageView) findViewById(R.id.temp_img);
		BtnUpload.setOnClickListener(this);
		BtnRegister = (Button) findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(this);
		BtnBack = (Button) findViewById(R.id.button_content_back);
		BtnBack.setOnClickListener(this);
		Log.i("MyTag","NoticeRegisterActivity >>>> ");
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
		Log.i("MyTag", Title + " >> " + Memo);

		Insert_Notice_Thread iThread = new Insert_Notice_Thread(this, 140, Title, Memo, ImgPath);
		innerforActivity inner = new innerforActivity();

		inner.sendToDevice("새 글이 등록되었습니다.");
		iThread.start(); // 글 업로드하는 스레드

	}

	// 글 업로드 성공 후 수행하는 함수
	public void clearContent() {
//		temp_img.setImageBitmap(null);
//		EditTitle.setText("");
//		EditMemo.setText("");
//		ImgPath = null;
		Log.i("MyTag","Good!");
	}

	class innerforActivity extends Activity {
		private void sendToDevice(final String msg) {
			mSendTask = new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) {
					Message.Builder messageBuilder = new Message.Builder();
					messageBuilder.addData("msg", msg);
					messageBuilder.addData("action", "show");
					Message message = messageBuilder.build();

					try {
						Result result = sender.send(message, BasicInfo.RegistrationId, 5);
						Log.i("PUSH", "Message sent. Result : " + result);

						String statusMessage = "현재상태 : " + result;
						Intent intent = new Intent(TOAST_MESSAGE_ACTION);
						intent.putExtra("message", statusMessage);
						intent.putExtra("mode", false);
						intent.putExtra("group_name", currentGroupName);
						intent.putExtra("content_num", 3);
						sendBroadcast(intent);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					return null;
				}

				protected void onPostExecute(Void result) {
					mSendTask = null;
				}

			};
			mSendTask.execute(null, null, null);
		}
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
			// Log.i("flag", data.getData().toString());

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
