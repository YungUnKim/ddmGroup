package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.item.ContentItem;
import android.content.Context;
import android.util.Log;

public class Get_Content_Thread extends Communication_Thread {

	ContentItem conItem;
	
	// 생성자
	public Get_Content_Thread(Context context, int menu, int board_num, int content_num, int mem_num) {
		super(context,menu);
		url += "&board_num=" + board_num + "&content_num=" + content_num + "&mem_num=" + mem_num;
//		Log.i("MyTag", "url >> " + url);
	}

	//최신글 20개 받아오는 함수
	public void xmlParser(XmlPullParser xpp) {		
		  // ------------------------------------- xml 파서 ------------------------------------//
		  try {
		     eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
		     while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
		        if (eventType == XmlPullParser.START_TAG) {
		           tagname = xpp.getName(); // 태그를 받아온다.
		        } 
		        else if (eventType == XmlPullParser.TEXT) {
		           if (tagname.equals("CONTENT_NUM") 
		        		   || tagname.equals("CONTENT_BOARD") 
		        		   || tagname.equals("CONTENT_TITLE") 
		        		   || tagname.equals("CONTENT_MEM") 
		        		   || tagname.equals("CONTENT_ARTICLE") 
		        		   || tagname.equals("CONTENT_IMG") 
		        		   || tagname.equals("CONTENT_REPLY") 
		        		   || tagname.equals("CONTENT_DATE") 
		        		   || tagname.equals("CONTENT_NOTICE") 
		        		   || tagname.equals("CONTENT_READ")
		        		   || tagname.equals("MEM_NAME")
		        		   || tagname.equals("MEM_THUMBNAIL")) {
		              ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
		           }
		        } 
		        else if (eventType == XmlPullParser.END_TAG) {
		           // 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
		           tagname = xpp.getName();
		           if (tagname.equals("CONTENT_NUM")) {
		        	   conItem = new ContentItem();
		        	   conItem.setIndexNum(Integer.parseInt(ret));
		           }
		           else if (tagname.equals("CONTENT_BOARD")) {
		        	   conItem.setBoardCategory(Integer.parseInt(ret));
		           } 
		           else if (tagname.equals("CONTENT_TITLE")) {
		        	   conItem.setTitle(ret);
		           } 
		           else if (tagname.equals("CONTENT_MEM")) {
		        	   conItem.setMemberNum(Integer.parseInt(ret));
		           } 
		           else if (tagname.equals("CONTENT_ARTICLE")) {
		        	   conItem.setArticle(ret);
		           }
		           else if (tagname.equals("CONTENT_IMG")) {
		        	   conItem.setImgUrl(ret);
		           }
		           else if (tagname.equals("CONTENT_REPLY")) {
		        	   conItem.setReplyCount(Integer.parseInt(ret));
		           }
		           else if (tagname.equals("CONTENT_DATE")) {
		        	   conItem.setDate(ret);   
		           }
		           else if (tagname.equals("CONTENT_NOTICE")) {
		        	   conItem.setNoticeCheck(Boolean.parseBoolean(ret));
		           }
		           else if (tagname.equals("CONTENT_READ")) {
		        	   conItem.setReadCount(Integer.parseInt(ret));
		           }
		           else if (tagname.equals("MEM_NAME")) {
		        	   conItem.setName(ret);
		           }
		           else if (tagname.equals("MEM_THUMBNAIL")) {
		        	   conItem.setThumbnail(ret);
		        	   ((ContentsActivity) mcontext).setContentItem(conItem);
		        	   Log.i("MyTag", "Thread title : " + conItem.getTitle() + " / num : " + conItem.getIndexNum());
		           }
		        }
		        eventType = xpp.next();
		     } // end while		     
		     msg.what = 24;
		     mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		  } 
		  catch (Exception e) {
		     e.getMessage();
		  }		
	}

}
