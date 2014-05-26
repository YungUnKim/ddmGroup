package uos.codingsroom.ddmgroup.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class LoadingProgressDialog {

	ProgressDialog progressDialog;

	public LoadingProgressDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("로딩중입니다.");
		progressDialog.setCancelable(false);
	}

	public void startProgressDialog() {
		progressDialog.show();
	}

	public void dismissProgressDialog() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
			}
		}, 500);

	}
}
