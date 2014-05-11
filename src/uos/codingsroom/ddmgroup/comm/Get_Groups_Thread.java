package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.item.GroupItem;
import android.content.Context;

public class Get_Groups_Thread extends Communication_Thread {
	private GroupItem groupItem;
	
	// 생성자 (소분류)
	public Get_Groups_Thread(Context context, int menu, int category) {
		super(context,menu);
		url += "&category=" + category;
//		Log.i("MyTag", "url >> " + url);
	}

	// 소뷴류 받아오는 함수
	public void xmlParser(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("TITLE") || tagname.equals("DSCR") || tagname.equals("MASTER")
							|| tagname.equals("NUM") || tagname.equals("total")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("total")) {
						if (ret.equals(0)) {
							msg.what = -20;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
							break;
						}
			
					}
					else if(tagname.equals("NUM")){
						groupItem = new GroupItem();
						groupItem.setIndexNum((Integer.parseInt(ret)));
					}
					else if(tagname.equals("TITLE")){
						groupItem.setTitle(ret);			
					}
					else if(tagname.equals("DSCR")){
						groupItem.setDescription(ret);
					}
					else if(tagname.equals("MASTER")){
						groupItem.setMasterNum(Integer.parseInt(ret));
						((MainActivity) mcontext).addGroupItem(groupItem);
					}
					
				}
				eventType = xpp.next();
			} // end while

			msg.what = 20;
			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
}
