package uos.codingsroom.ddmgroup.comm;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.item.GroupItem;
import uos.codingsroom.ddmgroup.item.NewsFeedItem;
import uos.codingsroom.ddmgroup.util.SystemValue;
import android.content.Context;
import android.os.Message;
import android.util.Log;

public class Connect_Thread extends Thread {
	private String ret = ""; // xml에서 받아온 TEXT를 임시로 받는 변수
	private String tagname = ""; // xml의 태그네임을 위한 변수
	private String url;
	private int eventType;

	private int menu; // 어떤 정보를 처리할지 위한 변수
	private int category; // 대분류 번호

	// 지워야할것들 안전지도의 잔제 시작
	private int kind;
	private int marker_num;
	private int board_num;
	private String fb_code;
	private String text;
	private Double x;
	private Double y;
	private Boolean name_public;
	// 지워야할것들 안전지도의 잔제 끝

	private int myNum;
	private int index;
	private String title;
	private String memo;
	private int num;
	private int read_count;
	private String group_name;
	private String date;
	private int reply_count;

	NewsFeedItem newsFeedItem;

	private String name;
	private String thumbNailURL;
	private Long kakaoCode;

	GroupItem groupItem;
	
	EventHandler mHandler;
	Message msg;
	Context mcontext;

	// 생성자 (마커 얻어올 때)
	public Connect_Thread(Context context, int menu) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; // 액티비티 객체
		this.menu = menu; // 어떤 작업을 할 것인가
	}

	// 생성자 (로그인)
	public Connect_Thread(Context context, int menu, String name, String thumbNailURL, Long kakaoCode) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; // 액티비티 객체
		this.menu = menu; // 어떤 작업을 할 것인가
		this.name = name;
		this.thumbNailURL = thumbNailURL;
		this.kakaoCode = kakaoCode;
	}

	// 생성자 (소분류)
	public Connect_Thread(Context context, int menu, int category) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; // 액티비티 객체
		this.menu = menu; // 어떤 작업을 할 것인가
		this.category = category;
	}

	// 생성자 (글 등록하기)
	public Connect_Thread(Context context, int menu,int mynum, int board_num, String title, String memo) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; // 액티비티 객체
		this.menu = menu; // 어떤 작업을 할 것인가
		this.myNum = mynum;	// 회원번호
		this.board_num = board_num;
		this.title = title; // 어떤 작업을 할 것인가
		this.memo = memo;
	}
	// // 생성자 (마커 등록할 때)
	// public Connect_Thread(Context context, int menu, MarkerItem mItem) {
	// this.mHandler = new EventHandler(context);
	// this.msg = mHandler.obtainMessage();
	// this.mcontext = context; // 액티비티 객체
	// this.menu = menu; // 어떤 작업을 할 것인가
	// this.kind = mItem.getKind(true);
	// this.name = mItem.getName();
	// this.x = mItem.getX();
	// this.y = mItem.getY();
	// this.text = mItem.getMemo();
	// }

	@Override
	public void run() {
		try {
			// ---------------------------- 서버에 값 전송하기 ----------------------------//
			url = SystemValue.conn + menu;
			if (menu == 10) {
				url += "&name=" + URLEncoder.encode(name, "UTF-8") + "&tnURL=" + URLEncoder.encode(thumbNailURL, "UTF-8") + "&kakaoCode=" + kakaoCode;
				Log.i("mytag", ">>>>" + kakaoCode);
				// 회원 가입
			} else if (menu == 11) { // 공지 받아오기

			} else if (menu == 12) { // 뉴스피드 받아오기

			} else if (menu == 20) { // 소분류 받아오기
				url += "&category=" + category;
			} else if (menu == 22) {
				url += "&mem_num=" + myNum + "&board_num=" + board_num +
						"&title=" + URLEncoder.encode(title, "UTF-8") + "&memo=" + URLEncoder.encode(memo, "UTF-8");
			}

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpMethod = new HttpGet(url); // url
			HttpResponse response = client.execute(httpMethod); // 서버와 통신
			InputStream is = response.getEntity().getContent();

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // xmlPullparser를 위한 준비과정.
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(is, "UTF-8"); // 인코딩 방식 설정

			if (menu == 10) { // 회원 가입
				connectForSignUp(xpp);
			} else if (menu == 11) { // 공지받아오기 함수 호출
				connectForGetNotice(xpp);
			} else if (menu == 12) { // 뉴스피드 받아오기 함수 호출
				connectForGetNewsfeed(xpp);
			} else if (menu == 20) { // 소분류 받아오기
				connectForGetCategory(xpp);
			} else if (menu == 22) { // 글 올리기
				connectForUploadContent(xpp);
			}
			Log.i("MyTag", "url >> " + url);
		} catch (Exception e) {
			e.getMessage();
		}

	}

	public void connectForSignUp(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("member_num")) {
						// location 태그정보는 중복되어 배열에 저장하지 않음.
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("member_num")) {
						if (ret.equals(0)) {
							msg.what = -10;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else {
							msg.what = 10;
							((MainActivity) mcontext).setMyMemberNum(Integer.parseInt(ret));
						}
					}
				}
				eventType = xpp.next();
			} // end while

			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

	// 공지사항 3개 얻어오는 함수
	public void connectForGetNotice(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			index = 0;// 3개중 몇번째 공지사항인지 구분해줌
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("NOTICE_NUM") || tagname.equals("NOTICE_TITLE") || tagname.equals("NOTICE_ARTICLE") || tagname.equals("NOTICE_IMG") || tagname.equals("NOTICE_DATE")) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("NOTICE_NUM")) {
						num = Integer.parseInt(ret);
					} else if (tagname.equals("NOTICE_TITLE")) {
						title = ret;
						msg.what = 11;
						((MainActivity) mcontext).setNotice(index, title, num);
						Log.i("MyTag", "index : " + index + " / title : " + title + " / num : " + num);
						index++;
					} else if (tagname.equals("NOTICE_ARTICLE")) {
					} else if (tagname.equals("NOTICE_IMG")) {
					} else if (tagname.equals("NOTICE_DATE")) {

					}
				}
				eventType = xpp.next();
			} // end while

			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}

	//최신글 읽어오는 함수
	public void connectForGetNewsfeed(XmlPullParser xpp) {		
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
		        		   || tagname.equals("CONTENT_READ") ) {
		              ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
		           }
		        } 
		        else if (eventType == XmlPullParser.END_TAG) {
		           // 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
		           tagname = xpp.getName();
		           if (tagname.equals("CONTENT_NUM")) {
		        	   num = Integer.parseInt(ret);
		           } 
		           else if (tagname.equals("CONTENT_TITLE")) {
		        	   title = ret;
		           } 
		           else if (tagname.equals("CONTENT_MEM")) {		        	   
		           } 
		           else if (tagname.equals("CONTENT_ARTICLE")) {	           
		           }
		           else if (tagname.equals("CONTENT_IMG")) {	           
		           }
		           else if (tagname.equals("CONTENT_REPLY")) {
		        	   reply_count = Integer.parseInt(ret);
		           }
		           else if (tagname.equals("CONTENT_DATE")) {
		        	   date = ret;	           
		           }
		           else if (tagname.equals("CONTENT_NOTICE")) {	           
		           }
		           else if (tagname.equals("CONTENT_READ")) {
		        	   read_count = Integer.parseInt(ret);
		        	   newsFeedItem = new NewsFeedItem(num, read_count, reply_count, title, group_name, date);
		        	   ((MainActivity) mcontext).setNewsFeed(newsFeedItem);
		        	   Log.i("MyTag", "newsfeed -> title : " + title + " / num : " + num );
		           }
		        }
		        eventType = xpp.next();
		     } // end while		     
		     msg.what=12;
		     mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		  } 
		  catch (Exception e) {
		     e.getMessage();
		  }		
	}
	
	// 소뷴류 받아오는 함수
	public void connectForGetCategory(XmlPullParser xpp) {
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
	
	public void connectForUploadContent(XmlPullParser xpp) {
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} else if (eventType == XmlPullParser.TEXT) {
					if (tagname.equals("mode")) {
						// location 태그정보는 중복되어 배열에 저장하지 않음.
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("mode")) {
						if (ret.equals("fail")) {
							msg.what = -22;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else {
							msg.what = 22;
//							((MainActivity) mcontext).setMyMemberNum(Integer.parseInt(ret));
						}
					}
				}
				eventType = xpp.next();
			} // end while

			mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
