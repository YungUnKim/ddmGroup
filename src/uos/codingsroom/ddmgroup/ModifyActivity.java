package uos.codingsroom.ddmgroup;

import uos.codingsroom.ddmgroup.util.UrlImageDownloadTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.APIErrorResult;
import com.kakao.AppActionBuilder;
import com.kakao.GlobalApplication;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.LogoutResponseCallback;
import com.kakao.UnlinkResponseCallback;
import com.kakao.UserManagement;

public class ModifyActivity extends Activity implements OnClickListener {

	ImageView backButton;

	TextView groupNameText;
	EditText titleText;
	EditText articleText;
	ImageView myImage;

	Button imageRemoveButton;
	Button imageModifyButton;
	Button backButton2;
	Button modifyButton;

	private String imageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeView();

		imageUrl = "http://codingsroom.com/ddmgroup/image/sshot.png";

		if (!imageUrl.equals("")) {
			new UrlImageDownloadTask(myImage).execute(imageUrl);
			myImage.setVisibility(View.VISIBLE);
		}
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
			myImage.setImageBitmap(null);
			break;
		case R.id.button_img_modify:

			break;
		case R.id.button_content_modify:

			break;

		default:
			break;
		}
	}

	private void initializeView() {
		setContentView(R.layout.activity_modify);

		backButton = (ImageView) findViewById(R.id.button_modify_back);
		backButton.setOnClickListener(this);
		backButton2 = (Button) findViewById(R.id.button_content_back_modify);
		backButton2.setOnClickListener(this);

		groupNameText = (TextView) findViewById(R.id.text_register_groupname_modify);
		titleText = (EditText) findViewById(R.id.editTitle_modify);
		articleText = (EditText) findViewById(R.id.editMemo_modify);
		myImage = (ImageView) findViewById(R.id.img_need_modify);

		imageRemoveButton = (Button) findViewById(R.id.button_img_remove);
		imageRemoveButton.setOnClickListener(this);
		imageModifyButton = (Button) findViewById(R.id.button_img_modify);
		imageModifyButton.setOnClickListener(this);
		modifyButton = (Button) findViewById(R.id.button_content_modify);
		modifyButton.setOnClickListener(this);
	}

}
