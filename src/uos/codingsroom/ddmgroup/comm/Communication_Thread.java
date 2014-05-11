package uos.codingsroom.ddmgroup.comm;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import uos.codingsroom.ddmgroup.util.SystemValue;
import android.content.Context;
import android.os.Message;

public class Communication_Thread extends Thread {
	protected String ret = ""; 		// xml에서 받아온 TEXT를 임시로 받는 변수
	protected String tagname = ""; 	// xml의 태그네임을 위한 변수
	protected String url;				// url
	protected int eventType;			//

	protected EventHandler mHandler;	// 이벤트 핸들러
	protected Message msg;			// 메시지
	protected Context mcontext;		// 액티비티 객체
	
	protected int menu; 		// 어떤 정보를 처리할지 위한 변수

	// 기본 생성자
	public Communication_Thread(Context context, int menu) {
		this.mHandler = new EventHandler(context);
		this.msg = mHandler.obtainMessage();
		this.mcontext = context; 	// 액티비티 객체
		this.menu = menu; 			// 어떤 작업을 할 것인가
		this.url = SystemValue.conn + menu;
	}

	// 스레드 기본 함수
	@Override
	public void run() {
		try {
			xmlParser(connect(url));	// XML 파싱 함수
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	// 서버와 통신하는 함수
	public XmlPullParser connect(String url){
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpMethod = new HttpGet(url); // url
			HttpResponse response = client.execute(httpMethod); // 서버와 통신
			InputStream is = response.getEntity().getContent();
	
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // xmlPullparser를 위한 준비과정.
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(is, "UTF-8"); // 인코딩 방식 설정
			
			return xpp;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
		
	}
	
	// 소뷴류 받아오는 함수
	public void xmlParser(XmlPullParser xpp) {
		
	}
	
}
