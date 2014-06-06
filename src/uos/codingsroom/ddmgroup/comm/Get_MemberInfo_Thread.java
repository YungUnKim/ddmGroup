package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.ManageMemberInfoActivity;
import uos.codingsroom.ddmgroup.item.MemberItem;
import android.content.Context;

public class Get_MemberInfo_Thread extends Manage_Communication_Thread {
	private MemberItem mItem;
	private Integer cnt;

	// 생성자
	public Get_MemberInfo_Thread(Context context, int menu, int mem_num) {
		super(context, menu);
		url += "&mem_num=" + mem_num;
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
					if (tagname.equals("TOTAL") || tagname.equals("MEM") || tagname.equals("MEM_NUM") || tagname.equals("MEM_NAME")
							|| tagname.equals("MEM_DATE") || tagname.equals("MEM_THUMB") || tagname.equals("MEM_CODE")
							|| tagname.equals("BOARD") || tagname.equals("LEVEL") || tagname.equals("CONTENT_CNT")
							|| tagname.equals("REPLY_CNT")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("TOTAL")) {
						if (ret.equals("fail")) {
							msg.what = -1110;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
							return;
						}
					} else if (tagname.equals("MEM_NUM")) {
						mItem = new MemberItem();
						mItem.setNum(Integer.parseInt(ret));
					} else if (tagname.equals("MEM_NAME")) {
						mItem.setName(ret);
					} else if (tagname.equals("MEM_DATE")) {
						mItem.setDate(ret);
					} else if (tagname.equals("MEM_THUMB")) {
						mItem.setThumbnail(ret);
					} else if (tagname.equals("MEM_CODE")) {
						//
						((ManageMemberInfoActivity) mcontext).setListItem(mItem);
					} else if (tagname.equals("BOARD")) {
						((ManageMemberInfoActivity) mcontext).setBoardNum(Integer.parseInt(ret));
					} else if (tagname.equals("LEVEL")) {
						((ManageMemberInfoActivity) mcontext).setLevel(Integer.parseInt(ret));
					} else if (tagname.equals("CONTENT_CNT")) {
						mItem.setContent_cnt(Integer.parseInt(ret));
					} else if (tagname.equals("REPLY_CNT")) {
						mItem.setReply_cnt(Integer.parseInt(ret));
						((ManageMemberInfoActivity) mcontext).setPermission();
					}
				}
				eventType = xpp.next();
			} // end while
			msg.what = 1110;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
