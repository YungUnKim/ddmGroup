package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.ManageBoardInfoActivity;
import uos.codingsroom.ddmgroup.item.BoardItem;
import android.content.Context;

public class Get_BoardInfo_Thread extends Manage_Communication_Thread {
	private BoardItem mItem;
	private Integer cnt;

	// 생성자
	public Get_BoardInfo_Thread(Context context, int menu, int board_num) {
		super(context, menu);
		url += "&board_num=" + board_num;
	}

	// 회원정보 파싱하는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("TOTAL") || tagname.equals("BOARD") || tagname.equals("BOARD_NUM") || tagname.equals("BOARD_TITLE")
							|| tagname.equals("BOARD_CAT") || tagname.equals("BOARD_DSCR") || tagname.equals("BOARD_MASTER")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("TOTAL")) {
						if (ret.equals("fail")) {
							msg.what = -1210;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
							return;
						}
						else{
							mItem.setContent_cnt(Integer.parseInt(ret));
						}
					} else if (tagname.equals("BOARD_NUM")) {
						mItem = new BoardItem();
						mItem.setNum(Integer.parseInt(ret));
					} else if (tagname.equals("BOARD_TITLE")) {
						mItem.setTitle(ret);
					} else if (tagname.equals("BOARD_CAT")) {
						mItem.setCategory(Integer.parseInt(ret));
					} else if (tagname.equals("BOARD_DSCR")) {
						mItem.setDscr(ret);
					} else if (tagname.equals("BOARD_MASTER")) {
						//
						((ManageBoardInfoActivity) mcontext).setListItem(mItem);
					} 
				}
				eventType = xpp.next();
			} // end while
			msg.what = 1210;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
