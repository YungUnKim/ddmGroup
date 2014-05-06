package uos.codingsroom.ddmgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ContentsActivity extends Activity implements OnClickListener{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);
		
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
