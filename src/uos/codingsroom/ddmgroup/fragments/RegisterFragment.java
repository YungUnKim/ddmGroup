package uos.codingsroom.ddmgroup.fragments;

import java.io.File;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Insert_Content_Thread;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.server.Sender;

public class RegisterFragment extends Fragment {

	private static Integer currentGroup = 0;
	private static String currentGroupName;

	private Uri ImgUrl;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_register, container, false);

		EditTitle = (EditText) view.findViewById(R.id.editTitle);
		EditMemo = (EditText) view.findViewById(R.id.editMemo);
		sender = new Sender(SystemValue.GOOGLE_API_KEY);
		groupTitle = (TextView) view.findViewById(R.id.text_register_groupname);

		clickListener click = new clickListener();
		BtnUpload = (Button) view.findViewById(R.id.button_img_upload);
		temp_img = (ImageView) view.findViewById(R.id.temp_img);
		BtnUpload.setOnClickListener(click);
		BtnRegister = (Button) view.findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(click);
		BtnBack = (Button) view.findViewById(R.id.button_content_back);
		BtnBack.setOnClickListener(click);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void setTitleLabel(String title) {
		currentGroupName = title;
		groupTitle.setText(title);
	}

	public void setCurrentGroupNum(Integer num) {
		currentGroup = num;
	}

	// 버튼 클릭 이벤트
	class clickListener implements View.OnClickListener {

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
				((MainActivity) getActivity()).showFragment(1, false);
				break;
			}

		}

	}

	// 글 업로드 하는 함수
	public void registerContent() {
		String Title = EditTitle.getText().toString();
		String Memo = EditMemo.getText().toString();

		if (Title.equals("") || Memo.equals("")) {
			Toast.makeText(getActivity(), "필수입력사항을 입력하세요!", Toast.LENGTH_LONG).show();
			// EditText가 비워있으면 null 오류가 뜨기 때문에 리턴해줘서 다시 입력하게 해야한다.
			return;
		}
		Log.i("MyTag", Title + " >> " + Memo);

		Insert_Content_Thread iThread = new Insert_Content_Thread(this.getActivity(), 22, MainActivity.getMyInfoItem().getMyMemNum(), currentGroup, Title, Memo,
				ImgPath);
		
		iThread.start(); // 글 업로드하는 스레드

		// rDialog = createRegisterDialog();
		// rDialog.show();

	}

	// 글 업로드 성공 후 수행하는 함수
	public void clearContent(){
		temp_img.setImageBitmap(null);
		EditTitle.setText("");
		EditMemo.setText("");
		ImgPath = null;
	}
	


	public void setNoticeTitle(String title, int index) {

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;

			Cursor c = getActivity().getContentResolver().query(Uri.parse(data.getDataString()), null, null, null, null);
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

}