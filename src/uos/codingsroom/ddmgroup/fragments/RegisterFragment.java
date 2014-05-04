package uos.codingsroom.ddmgroup.fragments;

import uos.codingsroom.ddmgroup.R;
import uos.codingsroom.ddmgroup.comm.Connect_Thread;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.listview.NewsFeedListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

	EditText EditTitle;
	EditText EditMemo;
	Button BtnUpload;
	Button BtnRegister;
	
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
				break;
			case R.id.button_content_register:	// 글 업로드 버튼
				registerContent();
				break;
			}
			
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

}