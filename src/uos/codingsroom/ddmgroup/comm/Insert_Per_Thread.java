package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class Insert_Per_Thread extends Manage_Communication_Thread {
	int where = 0;

	// 생성자
	public Insert_Per_Thread(Context context, int menu, int mem_num, int level, int where) {
		super(context, menu);
		this.where = where;
		url += "&mem_num=" + mem_num + "&level=" + level;
		Log.i("MyTag", "url >> " + url);
	}

	// 모임 생성 여부 결과받는 부분
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("MODE") || tagname.equals("PER")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("MODE")) {
						if (ret.equals("fail")) {
							if (this.where == 0) { // 관리자 메뉴
								msg.what = -1120;
							} else if (this.where == 1) { // 글
								msg.what = -1121;
							}
						} else {
							if (this.where == 0) { // 관리자 메뉴
								msg.what = 1120;
							} else if (this.where == 1) { // 글
								msg.what = 1121;
							}
						}
					} else if (tagname.equals("PER")) {

					}
				}
				eventType = xpp.next();
			} // end while

			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
