package uos.codingsroom.ddmgroup.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;

public class LoadingProgressDialog {

	ProgressDialog progressDialog;

	public LoadingProgressDialog(final Context context, Boolean cancelFlag) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("로딩중입니다.");
		progressDialog.setCancelable(cancelFlag);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((Activity) context).finish();
			}
		});
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
