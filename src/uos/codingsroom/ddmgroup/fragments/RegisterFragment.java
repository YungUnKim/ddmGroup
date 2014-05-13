package uos.codingsroom.ddmgroup.fragments;

import java.io.File;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Insert_Image_Thread;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

	// 사진 경로 구하는 클래스
	class innerforURI extends Activity {
		public String getRealPathFromURI(Uri contentUri) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Log.i("MyTag", "contentUri = " + contentUri.toString() + "proj = " + proj.toString());
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
	}

	public void registerContent() {
		String Title = EditTitle.getText().toString();
		String Memo = EditMemo.getText().toString();

		if (Title.equals("") || Memo.equals("")) {
			Toast.makeText(getActivity(), "필수입력사항을 입력하세요!", Toast.LENGTH_LONG).show();
			// EditText가 비워있으면 null 오류가 뜨기 때문에 리턴해줘서 다시 입력하게 해야한다.
			return;
		}
		Log.i("MyTag", Title + " >> " + Memo);
		
		Insert_Image_Thread iThread = new Insert_Image_Thread(this.getActivity(),22,
								MainActivity.getMyInfoItem().getMyMemNum(),
								currentGroup,
								Title,
								Memo,
								ImgPath);
		
		iThread.start();	// 이미지 업로드하는 스레드
		
//		Insert_Content_Thread mThread = new Insert_Content_Thread(this.getActivity(), 22,
//				16, 1, Title, Memo); // 회원번호, 소분류 번호 임시로 넣음
//		mThread.start();

		// rDialog = createRegisterDialog();
		// rDialog.show();

	}

	public void setNoticeTitle(String title, int index) {

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SetImage setImage = new SetImage();
//		Bitmap bitmap;
//		try {
//			bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		
		Cursor c = getActivity().getContentResolver().query(Uri.parse(data.getDataString()), null,null,null,null);
		c.moveToNext();
		ImgPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		Uri uri = Uri.fromFile(new File(ImgPath));
		Log.e("flag", uri.toString() + "\n" + ImgPath);
		c.close();

		
		final Bitmap b = BitmapFactory.decodeFile(ImgPath, options);
//		Log.i("flag", data.getData().toString());
		
//		temp_img.setImageURI(data.getData());
		temp_img.setImageBitmap(b);
		temp_img.setVisibility(View.VISIBLE);
		
		ImgUrl = data.getData();
		Log.i("MyTag","result url >>" + data.getData());
		/*
		 * if (requestCode == REQUEST_CODE_IMAGE && resultCode == -1 && null != data) { Uri currImageURI = data.getData(); Log.i("img",currImageURI.toString()); innerforURI inner = new
		 * innerforURI(); String imagePath = inner.getRealPathFromURI(currImageURI) ; // 찍은 사진을 이미지뷰에 보여준다.
		 * 
		 * final Bitmap bitmap = BitmapFactory.decodeFile(imagePath); setImage.setAlbumImageDrawble(imagePath, temp_img) ;
		 * 
		 * 
		 * }//end if
		 */
	}// end onActivityResult Method

}