package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import android.content.Context;
import android.util.Log;

public class Get_Notice_Three_Thread extends Communication_Thread {
	private int index;
	private String title;
	private int num;

	NewsFeedItem newsFeedItem;

	// 생성자
	public Get_Notice_Three_Thread(Context context, int menu) {
		super(context,menu);
	}

	// 공지사항 3개 얻어오는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			index = 0;// 3개중 몇번째 공지사항인지 구분해줌
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("NOTICE_NUM") || tagname.equals("NOTICE_TITLE") || tagname.equals("NOTICE_ARTICLE") || tagname.equals("NOTICE_IMG") || tagname.equals("NOTICE_DATE")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("NOTICE_NUM")) {
						num = Integer.parseInt(ret);
					} else if (tagname.equals("NOTICE_TITLE")) {
						title = ret;
						msg.what = 11;
						((MainActivity) mcontext).setNotice(index, title, num);
						index++;
					} else if (tagname.equals("NOTICE_ARTICLE")) {
					} else if (tagname.equals("NOTICE_IMG")) {
					} else if (tagname.equals("NOTICE_DATE")) {

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
