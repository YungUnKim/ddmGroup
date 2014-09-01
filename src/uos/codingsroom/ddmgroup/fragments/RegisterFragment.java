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

	EditText EditTitle;
	EditText EditMemo;
	Button BtnRegister;
	Button BtnBack;
	
	String[] ImgPath = new String[5];
	ImageView[] img = new ImageView[5];
	int[] img_id = { R.id.edit_img_01, R.id.edit_img_02, R.id.edit_img_03, R.id.edit_img_04, R.id.edit_img_05 };

	final int REQUEST_CODE_IMAGE_01 = 1;
	final int REQUEST_CODE_IMAGE_02 = 2;
	final int REQUEST_CODE_IMAGE_03 = 3;
	final int REQUEST_CODE_IMAGE_04 = 4;
	final int REQUEST_CODE_IMAGE_05 = 5;

	// GCM 메세지를 보낼 변수
	AsyncTask<Void, Void, Void> mSendTask;
	Sender sender;

	TextView groupTitle;
	Intent img_intent;

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

		for (int i = 0; i < 5; i++) {
			img[i] = (ImageView) view.findViewById(img_id[i]);
			img[i].setOnClickListener(click);
		}

		BtnRegister = (Button) view.findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(click);
		BtnBack = (Button) view.findViewById(R.id.button_content_back);
		BtnBack.setOnClickListener(click);

		img_intent = new Intent(Intent.ACTION_PICK);

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
			case R.id.button_content_register: // 글 업로드 버튼
				registerContent();
				break;
			case R.id.button_content_back:
				((MainActivity) getActivity()).showFragment(1, false);
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
			}
		}
	}

	public void clickImgButton(int position, int requestCode) {
		if (ImgPath[position] == null) {
			img_intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			img_intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(img_intent, requestCode);
		} else {
			ImgPath[position] = null;
			img[position].setImageResource(R.drawable.icon_img_plus);
		}
	}

	// 글 업로드 하는 함수
	public void registerContent() {
		String Title = EditTitle.getText().toString();
		String Memo = EditMemo.getText().toString();

		if (Title.equals("") || Memo.equals("")) {
			Toast.makeText(getActivity(), "필수입력사항을 입력하세요!", Toast.LENGTH_LONG).show();
			return;
		}
		Log.i("MyTag", Title + " >> " + Memo);

		Insert_Content_Thread iThread = new Insert_Content_Thread(this.getActivity(), 22, MainActivity.getMyInfoItem().getMyMemNum(),
				currentGroup, Title, Memo,
				"prevent Error");

		iThread.start(); // 글 업로드하는 스레드
		((MainActivity) getActivity()).progressDialog.startProgressDialog();
	}

	// 글 업로드 성공 후 수행하는 함수
	public void clearContent() {
		EditTitle.setText("");
		EditMemo.setText("");
		for (int i=0; i<5;i++){
			img[i].setImageResource(R.drawable.icon_img_plus);
			ImgPath[i] = null;
		}
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
			String tempPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			Uri uri = Uri.fromFile(new File(tempPath));
			Log.e("flag", uri.toString() + "\n" + tempPath);
			c.close();

			final Bitmap b = BitmapFactory.decodeFile(tempPath, options);

			// temp_img.setImageURI(data.getData());
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

	}

}