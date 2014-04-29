package uos.codingsroom.ddmgroup.comm;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import uos.codingsroom.ddmgroup.MainActivity;
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
	private int kind;
	private int marker_num;
	private int board_num;
	private String fb_code;
	private String text;
	private Double x;
	private Double y;
	private Boolean name_public;

	private String name;
	private String thumbNailURL;
	private Long kakaoCode;

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

	public Connect_Thread(Context context, int menu, String name, String thumbNailURL, Long kakaoCode) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; // 액티비티 객체
		this.menu = menu; // 어떤 작업을 할 것인가
		this.name = name;
		this.thumbNailURL = thumbNailURL;
		this.kakaoCode = kakaoCode;
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
			// + URLEncoder.encode(textmode.toString(), "UTF-8");
			if (menu == 10) {
				url += "&name=" + URLEncoder.encode(name, "UTF-8") + "&tnURL=" + URLEncoder.encode(thumbNailURL, "UTF-8") + "&kakaoCode="
						+ kakaoCode;
				Log.i("mytag", ">>>>"+ kakaoCode);
				// 회원 가입
			} else if (menu == 11) { // 마커 등록하기
				
			}
			// Log.i("MyTag","url >> " + url);

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
			} else if (menu == 12) { // 마커 등록하기
			// connect_insert_marker(xpp);
			}

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
						if (ret.equals(0)) { // 마커가 하나도 없을 경우
							msg.what = -10;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else { // 마커가 하나라도 있을 경우
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

}
