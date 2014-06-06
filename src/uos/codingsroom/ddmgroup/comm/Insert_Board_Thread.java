package uos.codingsroom.ddmgroup.comm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.item.BoardItem;
import android.content.Context;
import android.util.Log;

public class Insert_Board_Thread extends Manage_Communication_Thread {
	// 생성자
	public Insert_Board_Thread(Context context, int menu, BoardItem mItem) {
		super(context, menu);
		try {
			url += "&title=" + URLEncoder.encode(mItem.getTitle(), "UTF-8") + "&cat=" + mItem.getCategory() + "&dscr="
					+ URLEncoder.encode(mItem.getDscr(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					if (tagname.equals("MODE") || tagname.equals("BOARD_NUM")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("MODE")) {
						if (ret.equals("fail")) {
							msg.what = -1220;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else{
							msg.what = 1220;
						}
					} else if (tagname.equals("BOARD_NUM")) {

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
