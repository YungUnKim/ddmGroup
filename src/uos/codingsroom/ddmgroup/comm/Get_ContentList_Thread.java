package uos.codingsroom.ddmgroup.comm;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.fragments.ContentsFragment;
import uos.codingsroom.ddmgroup.item.ContentItem;
import android.content.Context;
import android.util.Log;

public class Get_ContentList_Thread extends Communication_Thread {
	private String title;
	private int num;
	private String mem;
	private int read_count;
	private String date;
	private int reply_count;

	private int group_num;
	private int end;
	private int start;

	ContentItem contentItem;
	
	// 생성자
	public Get_ContentList_Thread(Context context, int menu) {
		super(context,menu);
		((MainActivity) mcontext).cleanListview();
		group_num = ((MainActivity) mcontext).getGroupNum();
		start = ((MainActivity) mcontext).getStartNum();
		end = ((MainActivity) mcontext).getEndNum();
		url += "&list_num=10&end="+end+"&start="+start+"&key_word=&board="+group_num;
		Log.i("MyTag", "url 게시글 리스트 >> " + url);
	}
	

	//게시글 읽어오는 함수
	public void xmlParser(XmlPullParser xpp) {	
		  // ------------------------------------- xml 파서 ------------------------------------//
		  try {
       	   Log.i("MyTag", "xml 파싱 리스트");
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
		        		   || tagname.equals("CONTENT_READ") ) {
		              ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
		           }
		        } 
		        else if (eventType == XmlPullParser.END_TAG) {
		           // 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
		           tagname = xpp.getName();
		           if (tagname.equals("CONTENT_NUM")) {
		        	   num = Integer.parseInt(ret);
		        	   Log.i("MyTag", "content_list -> 1. num : " + num );
		           } 
		           else if (tagname.equals("CONTENT_TITLE")) {
		        	   title = ret;
		        	   Log.i("MyTag", "content_list -> 2. title : " + title);
		           } 
		           else if (tagname.equals("CONTENT_MEM")) {
		        	   mem = ret;		        	   
		        	   Log.i("MyTag", "content_list -> 3. mem : " + mem);
		           } 
		           else if (tagname.equals("CONTENT_ARTICLE")) {	
		        	   Log.i("MyTag", "content_list -> 4. article : " + ret);           
		           }
		           else if (tagname.equals("CONTENT_IMG")) {	
		        	   Log.i("MyTag", "content_list -> 5. img : " + ret);           
		           }
		           else if (tagname.equals("CONTENT_REPLY")) {
		        	   reply_count = Integer.parseInt(ret);
		        	   Log.i("MyTag", "content_list -> 6. reply : " + reply_count);
		           }
		           else if (tagname.equals("CONTENT_DATE")) {
		        	   date = ret;	           
		        	   Log.i("MyTag", "content_list -> 7. date : " + date);
		           }
		           else if (tagname.equals("CONTENT_NOTICE")) {	    
		        	   Log.i("MyTag", "content_list -> 8. notice"); 
		           }
		           else if (tagname.equals("CONTENT_READ")) {
		        	   read_count = Integer.parseInt(ret);
		        	   contentItem = new ContentItem(num, read_count, reply_count, title, mem, date);
		        	   ((MainActivity) mcontext).addContent(contentItem);
		        	   Log.i("MyTag", "content_list -> 9. read : " + read_count);
		           }
		        }
		        eventType = xpp.next();
		     } // end while		     

      	   	 Log.i("MyTag", "content_list -> xml파싱 끝");
		     msg.what=13;
		     mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		  } 
		  catch (Exception e) {
		     e.getMessage();
		  }		
	}


}
