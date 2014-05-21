package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.NoticeRegisterActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Manage_EventHandler extends Handler {
	Context mcontext;
	Context mcontext2;

	// 기본 생성자
	public Manage_EventHandler() {
	}

	// 생성자
	public Manage_EventHandler(Context context) {
		this.mcontext = context; // 액티비티 객체
	}

	public void handleMessage(Message msg) {
		try {
			if (msg.what == 0) {

			} else if (msg.what == -1000) {
				Log.i("MyTag", "Handler -1000 >> ");
			} else if (msg.what == 1000) {
				Log.i("MyTag", "Handler 1000 >> ");
			} else if (msg.what == 1400) {
				Log.i("MyTag", "글 올리기 핸들러 성공");
				((NoticeRegisterActivity) mcontext).clearContent();
			} else if (msg.what == -1400) {
				Log.i("MyTag", "글 올리기 핸들러 실패");
			} else if (msg.what == 24) {
				// Log.i("MyTag", "글 얻어오기 핸들러 성공");
				((ContentsActivity) mcontext).setContentView();
			} else if (msg.what == -24) {
				// Log.i("MyTag", "글 얻어오기 핸들러 실패");
				((ContentsActivity) mcontext).viewMessage("글 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == -25) {
				Log.i("MyTag", "글 수정하기 핸들러 실패");
			} else if (msg.what == 25) {
				Log.i("MyTag", "글 수정하기 핸들러 성공");
			} else if (msg.what == -26) {
				// Log.i("MyTag", "글 삭제 핸들러 실패");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 실패하였습니다.");
			} else if (msg.what == 26) {
				// Log.i("MyTag", "글 삭제 핸들러 성공");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 성공하였습니다.", 0);
			} 

		} catch (Exception e) {
		}
		return;
	}
}
