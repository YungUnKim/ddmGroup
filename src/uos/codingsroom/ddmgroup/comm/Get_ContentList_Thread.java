package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.fragments.ContentsFragment;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.util.TimeFormat;
import android.content.Context;
import android.util.Log;

public class Get_ContentList_Thread extends Communication_Thread {

	ContentItem contentItem;

	// 생성자
	public Get_ContentList_Thread(Context context, int menu, int group_num) {
		super(context, menu);
		url += "&list_num=15" + "&key_word=&board=" + group_num;
		Log.i("MyTag2", "url 게시글 리스트 >> " + url);
	}

	// 게시글 읽어오는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			Log.i("MyTag", "xml 파싱 리스트");
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("CONTENT_NUM") || tagname.equals("CONTENT_BOARD")
							|| tagname.equals("CONTENT_TITLE") || tagname.equals("CONTENT_MEM")
							|| tagname.equals("CONTENT_ARTICLE") || tagname.equals("CONTENT_IMG")
							|| tagname.equals("CONTENT_REPLY") || tagname.equals("CONTENT_DATE")
							|| tagname.equals("CONTENT_NOTICE") || tagname.equals("CONTENT_READ")
							|| tagname.equals("MEM_NAME")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("CONTENT_NUM")) {
						contentItem = new ContentItem();
						contentItem.setIndexNum(Integer.parseInt(ret));
						
					} else if (tagname.equals("CONTENT_TITLE")) {
						contentItem.setTitle(ret);
					} else if (tagname.equals("CONTENT_MEM")) {
						contentItem.setMemberNum(Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_ARTICLE")) {

					} else if (tagname.equals("CONTENT_IMG")) {
					} else if (tagname.equals("CONTENT_REPLY")) {
						contentItem.setReplyCount(Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_DATE")) {
						contentItem.setDate(new TimeFormat().timeDelay(ret));

					} else if (tagname.equals("CONTENT_NOTICE")) {

					} else if (tagname.equals("CONTENT_READ")) {
						contentItem.setReadCount(Integer.parseInt(ret));
					} else if (tagname.equals("MEM_NAME")) {
						contentItem.setName(ret);
						((MainActivity) mcontext).addContent(contentItem);
					}
				}
				eventType = xpp.next();
			} // end while

			Log.i("MyTag", "content_list -> xml파싱 끝");
			msg.what = 13;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
