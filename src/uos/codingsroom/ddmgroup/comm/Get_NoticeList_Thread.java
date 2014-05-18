package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.NoticeActivity;
import uos.codingsroom.ddmgroup.item.NoticeItem;
import android.content.Context;

public class Get_NoticeList_Thread extends Communication_Thread {
	private NoticeItem mItem;

	// 생성자
	public Get_NoticeList_Thread(Context context, int menu) {
		super(context, menu);
	}

	// 공지사항 20개 얻어오는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("total") || tagname.equals("NOTICE_NUM") || tagname.equals("NOTICE_TITLE") || tagname.equals("NOTICE_ARTICLE")
							|| tagname.equals("NOTICE_IMG") || tagname.equals("NOTICE_DATE")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if(tagname.equals("total")){
						if(ret.equals("fail")){
							msg.what = -14;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
							return;
						}
					} else if (tagname.equals("NOTICE_NUM")) {
						mItem = new NoticeItem();
						mItem.setNum(Integer.parseInt(ret));
					} else if (tagname.equals("NOTICE_TITLE")) {
						mItem.setTitle(ret);
					} else if (tagname.equals("NOTICE_ARTICLE")) {
					} else if (tagname.equals("NOTICE_IMG")) {
					} else if (tagname.equals("NOTICE_DATE")) {
						mItem.setDate(ret);
						((NoticeActivity) mcontext).setNoticeListItem(mItem);
					}
				}
				eventType = xpp.next();
			} // end while
			msg.what = 14;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
