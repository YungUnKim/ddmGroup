package uos.codingsroom.ddmgroup.comm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import android.content.Context;
import android.util.Log;

public class Login_Profile_Thread extends Communication_Thread {

	// 생성자 (로그인)
	public Login_Profile_Thread(Context context, int menu, String name, String thumbNailURL, Long kakaoCode) {
		super(context,menu);
		try {
			url += "&name=" + URLEncoder.encode(name, "UTF-8") + "&tnURL=" + URLEncoder.encode(thumbNailURL, "UTF-8") + "&kakaoCode=" + kakaoCode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.i("MyTag", "url >> " + url);
	}

	// XML 파싱 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("member_num") || tagname.equals("board") || tagname.equals("level")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("member_num")) {
						if (ret.equals(0)) {
							msg.what = -10;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else {
							msg.what = 10;
							((MainActivity) mcontext).setMyMemberNum(Integer.parseInt(ret));
						}
					}
					else if (tagname.equals("board")) {
						((MainActivity) mcontext).setBoardNum(Integer.parseInt(ret));
					}
					else if (tagname.equals("level")) {
						((MainActivity) mcontext).setLevel(Integer.parseInt(ret));
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
