package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.util.TimeFormat;
import android.content.Context;

public class Get_Newsfeed_Thread extends Communication_Thread {
	NewsFeedItem newsFeedItem;

	// 생성자
	public Get_Newsfeed_Thread(Context context, int menu) {
		super(context, menu);
	}

	// 최신글 20개 받아오는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("CONTENT_NUM") || tagname.equals("CONTENT_TITLE")
							|| tagname.equals("CONTENT_REPLY") || tagname.equals("CONTENT_DATE")
							|| tagname.equals("CONTENT_NOTICE") || tagname.equals("CONTENT_READ")
							|| tagname.equals("GROUP_NAME")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("CONTENT_NUM")) {
						newsFeedItem = new NewsFeedItem();
						newsFeedItem.setIndexNum(Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_TITLE")) {
						newsFeedItem.setTitle(ret);
					} else if (tagname.equals("CONTENT_REPLY")) {
						newsFeedItem.setReplyCount(Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_DATE")) {
						newsFeedItem.setDate(new TimeFormat().timeDelay(ret));
					} else if (tagname.equals("CONTENT_NOTICE")) {
					} else if (tagname.equals("CONTENT_READ")) {
						newsFeedItem.setReadCount(Integer.parseInt(ret));

					} else if (tagname.equals("GROUP_NAME")) {
						newsFeedItem.setGroupName(ret);
						((MainActivity) mcontext).setNewsFeed(newsFeedItem);
					}
				}
				eventType = xpp.next();
			} // end while
			msg.what = 12;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
