package uos.codingsroom.ddmgroup.comm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.util.SystemValue;
import android.content.Context;
import android.util.Log;

public class Modify_Content_Thread extends Communication_Thread {
	private String uploadFilePath = null; // 이미지 경로
	private int menu;
	private int mem_num;
	private int board_num;
	private int content_num;
	private String title;
	private String article;
	private String deleteFilePath = null; // 이전의 이미지 경로

	int serverResponseCode = 0;

	// 글 등록하기
	public Modify_Content_Thread(Context context, int menu, int mem_num, int board_num, int content_num, String title, String article, String path, String del_path) {
		super(context, menu);
		this.menu = menu;
		this.mem_num = mem_num;
		this.board_num = board_num;
		this.content_num = content_num;
		this.title = title;
		this.article = article;
		this.uploadFilePath = path;
		this.deleteFilePath = del_path;
	}

	// 스레드 기본 함수
	@Override
	public void run() {
		try {
			Log.i("MyTag", "test [" + uploadFilePath + "][" + deleteFilePath + "]");
			if (deleteFilePath == null && uploadFilePath == null) { // 그림이 없거나 수정하지 않은 경우
				try {
					url += "&mem_num=" + mem_num + "&board_num=" + board_num + "&content_num=" + content_num + "&title="
							+ URLEncoder.encode(title, "UTF-8") + "&article=" + URLEncoder.encode(article, "UTF-8") + "&del_img=" + ""
							+ "&del_mode=not";
					Log.i("MyTag", "url [" + url + "]");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				xmlParser(connect(url)); // XML 파싱 함수
			} else if (uploadFilePath.equals("del")) { // 그림을 삭제할 경우
				try {
					url += "&mem_num=" + mem_num + "&board_num=" + board_num + "&content_num=" + content_num + "&title="
							+ URLEncoder.encode(title, "UTF-8") + "&article=" + URLEncoder.encode(article, "UTF-8") + "&del_img="
							+ URLEncoder.encode(deleteFilePath, "UTF-8") + "&del_mode=del";;
					Log.i("MyTag", "url [" + url + "]");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				xmlParser(connect(url)); // XML 파싱 함수
			} else { // 그림이 있는 경우
				uploadFile(this.uploadFilePath);
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public int uploadFile(String sourceFileUri) {
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			// 파일이 없을 경우
			Log.i("MyTag", "Source File not exist :" + uploadFilePath);
			return 0;
		} else {
			try {
				// open a URL connection to the Servlet
				String imgurl = "http://14.63.199.182/ddmgroup/ddmgroup.php";
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL Httpurl = new URL(imgurl);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) Httpurl.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				// conn.setRequestProperty("Charset", "UTF-8");
				// conn.setRequestProperty("Content-Language", "UTF-8");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName); // 파일명

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);

				dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename=");
				dos.write(fileName.getBytes("utf-8"));
				dos.writeBytes("" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// 파일 이외에 파라미터로 넘길 값들
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='menu';" + lineEnd + lineEnd + this.menu);
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='mem_num';" + lineEnd + lineEnd + this.mem_num);
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='board_num';" + lineEnd + lineEnd + this.board_num);
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='content_num';" + lineEnd + lineEnd + this.content_num);
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='title';" + lineEnd + lineEnd);
				dos.write(title.getBytes("utf-8"));
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='article';" + lineEnd + lineEnd);
				dos.write(article.getBytes("utf-8"));
				dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
				if (deleteFilePath != null) {
					dos.writeBytes("Content-Disposition: form-data; name='del_img';" + lineEnd + lineEnd);
					dos.write(deleteFilePath.getBytes("utf-8"));
				} else {
					dos.writeBytes("Content-Disposition: form-data; name='del_img';" + lineEnd + lineEnd + "");
				}
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				// Log.i("MyTag", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					// 이미지 전송 완료
					Log.i("MyTag", "이미지 전송 완료");
					msg.what = 22;
					mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
				} else {
					Log.i("MyTag", "이미지 전송 실패");
					msg.what = -22;
					mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {
				// url 실패
				ex.printStackTrace();
				Log.i("MyTag", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				// 기타 이상
				e.printStackTrace();
				Log.i("MyTag", "error: " + e.getMessage(), e);
			}
			return serverResponseCode;

		} // End else block
	}

	// 글 등록하는 함수
	public void xmlParser(XmlPullParser xpp) {
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
							msg.what = -25;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else {
							msg.what = 25;
							Log.i("MyTag", "test >> 333");
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						}
					}
				}
				eventType = xpp.next();
			} // end while
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
