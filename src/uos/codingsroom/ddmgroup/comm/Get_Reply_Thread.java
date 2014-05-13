package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.item.CommentItem;
import uos.codingsroom.ddmgroup.util.TimeFormat;
import android.content.Context;
import android.util.Log;

public class Get_Reply_Thread extends Communication_Thread {
	CommentItem comItem;

	// 생성자
	public Get_Reply_Thread(Context context, int menu, int content_num) {
		super(context, menu);
		url += "&content_num=" + content_num;
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
					if (tagname.equals("mem_num") || tagname.equals("mem_name") || tagname.equals("mem_thumbnail") || tagname.equals("reply_num")
							|| tagname.equals("content_num") || tagname.equals("article") || tagname.equals("date")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("mem_num")) {
						comItem = new CommentItem();
						comItem.setIndexNum(Integer.parseInt(ret));
					} else if (tagname.equals("mem_name")) {
						comItem.setKakaoName(ret);
					} else if (tagname.equals("mem_thumbnail")) {
						comItem.setKakaoUrl(ret);
					} else if (tagname.equals("reply_num")) {
						comItem.setIndexNum(Integer.parseInt(ret));
					} else if (tagname.equals("content_num")) {
					} else if (tagname.equals("article")) {
						comItem.setArticle(ret);
					} else if (tagname.equals("date")) {
						comItem.setDate(new TimeFormat().timeDelay(ret));
						((ContentsActivity) mcontext).setCommentItem(comItem);
					}
				}
				eventType = xpp.next();
			} // end while
			msg.what = 28;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
