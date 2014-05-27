package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.AdminActivity;
import android.content.Context;
import android.util.Log;

public class Get_Manage_Thread extends Manage_Communication_Thread {

	// 생성자
	public Get_Manage_Thread(Context context, int menu) {
		super(context, menu);
	}

	// xml 파싱 함수
	public void xmlParser(XmlPullParser xpp) {
		try {
			eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("MEMBER_COUNT") || tagname.equals("BOARD_COUNT") || tagname.equals("CONTENT_COUNT")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					tagname = xpp.getName();
					if (tagname.equals("MEMBER_COUNT")) {
						if (ret.equals("fail")) {
							msg.what = -1000;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
							break;
						} else {
							((AdminActivity) mcontext).setText("member",Integer.parseInt(ret));
						}
					} else if (tagname.equals("BOARD_COUNT")) {
						((AdminActivity) mcontext).setText("board", Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_COUNT")) {
						((AdminActivity) mcontext).setText("contents", Integer.parseInt(ret));
					}

				}
				eventType = xpp.next();
			} // end while

			msg.what = 1000;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
