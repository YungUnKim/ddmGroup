package uos.codingsroom.ddmgroup.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Connect_Thread;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

	EditText EditTitle;
	EditText EditMemo;
	Button BtnUpload;
	Button BtnRegister;
	ImageView temp_img;
	int REQUEST_CODE_IMAGE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_register, container, false);

		EditTitle = (EditText) view.findViewById(R.id.editTitle);
		EditMemo = (EditText) view.findViewById(R.id.editMemo);
		
		clickListener click = new clickListener();
		BtnUpload = (Button) view.findViewById(R.id.button_img_upload);
		temp_img = (ImageView) view.findViewById(R.id.temp_img);
		BtnUpload.setOnClickListener(click);
		BtnRegister = (Button) view.findViewById(R.id.button_content_register);
		BtnRegister.setOnClickListener(click);
		
		return view;
	}

	// 버튼 클릭 이벤트
	class clickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button_img_upload:	// 이미지 업로드
			{
				Intent intent = new Intent (Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent,REQUEST_CODE_IMAGE);
				
			}	
				break;
			case R.id.button_content_register:	// 글 업로드 버튼
				registerContent();
				break;
			}
			
		}
		
	}
	//사진 경로 구하는 클래스
	class innerforURI extends Activity{
		public String getRealPathFromURI(Uri contentUri) {
			String [] proj={MediaStore.Images.Media.DATA};
			Log.i("img","contentUri = "+contentUri.toString() + "proj = " + proj.toString());
			Cursor cursor = managedQuery(contentUri,proj,null,null,null);
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
		Connect_Thread mThread = new Connect_Thread(this.getActivity(), 22, 
				16 , 1, Title, Memo);	// 회원번호, 소분류 번호 임시로 넣음
		mThread.start();
		
//		rDialog = createRegisterDialog();
//		rDialog.show();

	}
	
	public void setNoticeTitle(String title, int index) {

	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SetImage setImage = new SetImage();
		temp_img.setImageURI(data.getData()); 
		temp_img.setVisibility(temp_img.VISIBLE);
		/*
	    if (requestCode == REQUEST_CODE_IMAGE && resultCode == -1 && null != data) {
	    	Uri currImageURI = data.getData();
	    	Log.i("img",currImageURI.toString());
	    	innerforURI inner = new innerforURI();
	    	String imagePath = inner.getRealPathFromURI(currImageURI) ;
	    	// 찍은 사진을 이미지뷰에 보여준다.
	    	
	    	final Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
	    	setImage.setAlbumImageDrawble(imagePath, temp_img) ;
	    	
	    	
	    }//end if
	     */
	 }//end onActivityResult Method
	

	

}