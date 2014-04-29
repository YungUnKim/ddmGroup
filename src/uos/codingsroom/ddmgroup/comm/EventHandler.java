package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.MainActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class EventHandler extends Handler {
	Context mcontext;
	Context mcontext2;

	// 기본 생성자
	public EventHandler() {
	}

	// 생성자
	public EventHandler(Context context) {
		this.mcontext = context; // 액티비티 객체
	}

	public void handleMessage(Message msg) {
		try {
			if (msg.what == 0) { 
			
			}
			else if (msg.what == -10) { 
			
			} 
			else if (msg.what == 10) { 
			// Log.i("MyTag","Handler 10 >> " + msg);
				((MainActivity) mcontext).showMyMemNumber();
			}
			else if (msg.what == 11) { 
			// Log.i("MyTag","Handler 10 >> " + msg);
				((MainActivity) mcontext).setNoticeTitle();
				Log.i("MyTag", "핸들러 접근 성공");
			}
		} catch (Exception e) {
		}
		return;
	}
}
