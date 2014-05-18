package uos.codingsroom.ddmgroup.comm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.ContentsActivity;
import android.content.Context;
import android.util.Log;

public class Insert_Reply_Thread extends Communication_Thread {
	String article;
	// 생성자
	public Insert_Reply_Thread(Context context, int menu, int content_num, int mem_num, String article, boolean kind) {
		super(context,menu);
		this.article = article;
		try {
			url += "&content_num=" + content_num + "&mem_num=" + mem_num + "&article=" + URLEncoder.encode(article, "UTF-8") + "&kind=" + kind;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("MyTag", "url >> " + url);
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
		           if (tagname.equals("mode")
		          	|| tagname.equals("reply_num")) {
		              ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
		           }
		        } 
		        else if (eventType == XmlPullParser.END_TAG) {
		           // 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
		           tagname = xpp.getName();
		           if (tagname.equals("mode")) {
		        	   if(ret.equals("fail")){
		        		   msg.what = -27;
		        		   mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		        	   }
		           }
		           else if(tagname.equals("reply_num")){
		          	 ((ContentsActivity) mcontext).setCommentNum(Integer.parseInt(ret));
		          	 msg.what = 27;
		        		 mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		        		 break;
		           }
		        }
		        eventType = xpp.next();
		     } // end while
		  
		  } 
		  catch (Exception e) {
		     e.getMessage();
		  }		
	}

}
