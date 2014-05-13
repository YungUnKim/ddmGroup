package uos.codingsroom.ddmgroup.comm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import uos.codingsroom.ddmgroup.util.SystemValue;
import android.content.Context;
import android.util.Log;

public class Insert_Image_Thread extends Communication_Thread {
	String upLoadServerUri = null;
	     
	/**********  File Path *************/
//	private String uploadFilePath = "/mnt/sdcard/";
//	private String uploadFileName = "service_lifecycle.png";
	private String uploadFilePath;
	
	int serverResponseCode = 0;
	
	// 테스트 생성자
	public Insert_Image_Thread(Context context, int menu, String path) {
		super(context,menu);
		this.uploadFilePath = path;
		Log.i("MyTag", "url >> " + url);
	}
	// 생성자
	public Insert_Image_Thread(Context context, int menu, int board_num, int content_num, int mem_num) {
		super(context,menu);
		url += "&board_num=" + board_num + "&content_num=" + content_num + "&mem_num=" + mem_num;
		Log.i("MyTag", "url >> " + url);
	}

	// 스레드 기본 함수
	@Override
	public void run() {
		try {
//			uploadFile(uploadFilePath + "" + uploadFileName);
			uploadFile(uploadFilePath);
//			xmlParser(connect(url));	// XML 파싱 함수
			
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public int uploadFile(String sourceFileUri) {
	          String fileName = sourceFileUri;
	          Log.i("MyTag","Path >> " + fileName);
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
//	          	Log.i("MyTag", "Source File not exist :" +uploadFilePath + "" + uploadFileName);
	          	Log.i("MyTag", "Source File not exist :" +uploadFilePath);
	          	return 0;
	          }
	          else
	          {
	          	try {   
	          		/************* php script path ****************/
	          		upLoadServerUri = "http://14.63.199.182/ddmgroup/UploadToServer.php";
	          		
	          		// open a URL connection to the Servlet
	          		FileInputStream fileInputStream = new FileInputStream(sourceFile);
	          		URL url = new URL(upLoadServerUri);
	                    
	          		// Open a HTTP  connection to  the URL
	          		conn = (HttpURLConnection) url.openConnection(); 
	          		conn.setDoInput(true); // Allow Inputs
	          		conn.setDoOutput(true); // Allow Outputs
	          		conn.setUseCaches(false); // Don't use a Cached Copy
	          		conn.setRequestMethod("POST");
	          		conn.setRequestProperty("Connection", "Keep-Alive");
	          		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	          		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	          		conn.setRequestProperty("uploaded_file", fileName); 
	                    
	          		dos = new DataOutputStream(conn.getOutputStream());
	          
	          		dos.writeBytes(twoHyphens + boundary + lineEnd); 
	          		dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename=" + fileName + "" + lineEnd);
	                    
	          		dos.writeBytes(lineEnd);
	          
		                   // create a buffer of  maximum size
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
	          
		                   // send multipart form data necesssary after file data...
		                   dos.writeBytes(lineEnd);
		                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	          
		                   // Responses from the server (code and message)
		                   serverResponseCode = conn.getResponseCode();
		                   String serverResponseMessage = conn.getResponseMessage();
	                     
		                   Log.i("MyTag", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
	                    
		                   if(serverResponseCode == 200){
		          	         // 이미지 전송 완료
		          	         Log.i("MyTag", "이미지 전송 완료");       
		                   }    
	                    
		                   //close the streams //
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

	/*
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
	*/
	
	// 그림 서버에 올린 결과 받는 함수
	public void xmlParser(XmlPullParser xpp) {		
		// ------------------------------------- xml 파서 ------------------------------------//
		try {
			eventType = xpp.getEventType(); // 이벤트 타입 얻어오기 예를들어 <start> 인지 </start> 인지 구분하기 위한.
			while (eventType != XmlPullParser.END_DOCUMENT) { // xml이 끝날때까지 계속 돌린다.
				if (eventType == XmlPullParser.START_TAG) {
					tagname = xpp.getName(); // 태그를 받아온다.
				} 
				else if (eventType == XmlPullParser.TEXT) {
					if ( tagname.equals("mode") ) {
						ret = xpp.getText(); // id 태그에 해당되는 TEXT를 임시로 저장
					}
				} 
				else if (eventType == XmlPullParser.END_TAG) {
					// 태그가 닫히는 부분에서 임시 저장된 TEXT를 Array에 저장한다.
					tagname = xpp.getName();
					if (tagname.equals("mode")) {
						if(ret.equals("success")){
							msg.what = 100;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						}
						else{
							msg.what = -100;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						}
					}
					else if (tagname.equals("MEM_THUMBNAIL")) {
//						conItem.setThumbnail(ret);
//						((ContentsActivity) mcontext).setContentItem(conItem);
//						Log.i("MyTag", "Thread title : " + conItem.getTitle() + " / num : " + conItem.getIndexNum());
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
