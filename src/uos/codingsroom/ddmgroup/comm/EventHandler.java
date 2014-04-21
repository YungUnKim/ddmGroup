package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.MainActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
			if (msg.what == 0) { // 맵에서 현재위치 주소 받기
			
			}
			else if (msg.what == -10) { // 마커정보가 하나도 없을 경우
			// Log.i("MyTag","Handler -10 >> 마커가 하나도 없음 >> " + msg);
			} else if (msg.what == 10) { // 마커정보 얻어오기
			// Log.i("MyTag","Handler 10 >> " + msg);
				((MainActivity) mcontext).showMyMemNumber();
			}
		} catch (Exception e) {
		}
		return;
	}
}
