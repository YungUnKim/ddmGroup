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

public class Insert_Content_Thread extends Communication_Thread {
//	private String uploadFilePath;	// 이미지 경로
	String[] ImgPath = new String[5];		// 이미지 경로
	private int img_num = 0;
	private int menu;
	private int mem_num;
	private int board_num;
	private String title;
	private String article;
	
	private static String CRLF = "\r\n";
	private static String twoHyphens = "--";
	private static String boundary = "*****mgd*****";

	private HttpURLConnection conn = null;
	private DataOutputStream dos = null;

	private int serverResponseCode = 0;
	
	int i = 0;
	
	// 글 등록하기
	public Insert_Content_Thread(Context context, int menu,int mem_num, int board_num, String title, String article, String[] path, int img_num) {
		super(context,menu);
		this.menu = menu;
		this.mem_num = mem_num;
		this.board_num = board_num;
		this.title = title;
		this.article = article;
//		this.uploadFilePath = path;
		this.ImgPath = path;
		this.img_num = img_num;
		Log.i("MyTag","Insert_Conent_Thread >> " + this.ImgPath[0] + " >> " + this.ImgPath[1] + " >> " + this.ImgPath[2] + " >> " + this.ImgPath[3] + " >> " + this.ImgPath[4]);
	}

	// 스레드 기본 함수
	@Override
	public void run() {
		try {
			if(this.img_num == 0){	// 그림이 없는 경우
				try {
					url += "&mem_num=" + mem_num + "&board_num=" + board_num +
							"&title=" + URLEncoder.encode(title, "UTF-8") + "&article=" + URLEncoder.encode(article, "UTF-8") + "&img_num=" + img_num;
					Log.i("MyTag", "url >> " + url);
					xmlParser(connect(url));	// XML 파싱 함수
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{			// 그림이 있는 경우
//				Log.i("MyTag","Insert_Conent_Thread Image Upload ");
				
				uploadFile();	// 서버에 파일 및 정보 전송
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public int uploadFile() {
//		String sourceFileUri1 = "test1";
//		String sourceFileUri2 = "test2";
//		File sourceFile1 = new File(sourceFileUri1);
//		File sourceFile2 = new File(sourceFileUri2);
//		FileInputStream fileInputStream1 = new FileInputStream(sourceFile1);
//		FileInputStream fileInputStream2 = new FileInputStream(sourceFile2);
		
		String[] sourceFileUri = new String[5];		// 이미지 경로
		File[] sourceFile = new File[5];
		FileInputStream[] fileInputStream = new FileInputStream[5];
		
		try {
			int j = 0;
			for(int i=0; i<5; i++){
				if(this.ImgPath[i] == null){	// 이미지 경로가 없을 경우
//					Log.i("MyTag","Insert_Conent_Thread Image >> " + i);
				}
				else{		// 이미지 경로가 있을 경우
//					Log.i("MyTag","Insert_Conent_Thread Image exist >> " + i);
					sourceFileUri[j] = this.ImgPath[i];
					sourceFile[j] = new File(sourceFileUri[j]);
					fileInputStream[j] = new FileInputStream(sourceFile[j]);
					j++;
				}
			}

			// open a URL connection to the Servlet
			String imgurl = SystemValue.conn_public;
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
//			conn.connect();
//	      		conn.setRequestProperty("uploaded_file", fileName);			// 파일명
			
			dos = new DataOutputStream(conn.getOutputStream());
          		
			// 파일 이외에 파라미터로 넘길 값들
			writeFormField("menu", String.valueOf(menu));
			writeFormField("img_num", String.valueOf(img_num));
			writeFormField("mem_num", String.valueOf(mem_num));
			writeFormField("board_num", String.valueOf(board_num));
			writeFormField("title", title);
			writeFormField("article", article);
			
			// 올릴 파일
			for(i=0; i<this.img_num; i++){
				writeFileField("InputFile_" + i, sourceFileUri[i], fileInputStream[i]);
			}
			dos.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("MyTag", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

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
			for(i=0; i<this.img_num; i++){
				fileInputStream[i].close();
			}
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

	}

	private void writeFormField(String fieldName, String fieldValue) {
		try {
			dos.writeBytes(twoHyphens + boundary + CRLF);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
			dos.writeBytes(CRLF);
			dos.write(fieldValue.getBytes("utf-8"));
			dos.writeBytes(CRLF);
		} catch (Exception e) {
			Log.i("MyTag", "GeoPictureUploader.writeFormField: got: " + e.getMessage());
		}
	}

	private void writeFileField(String fieldName, String fieldValue, FileInputStream fis) {
		try {
			// opening boundary line
			dos.writeBytes(twoHyphens + boundary + CRLF);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"");
			dos.write(fieldValue.getBytes("utf-8"));
			dos.writeBytes("\"" + CRLF);
			dos.writeBytes(CRLF);

			// create a buffer of maximum size
			int bytesAvailable = fis.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// read file and write it into form...
			int bytesRead = fis.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}
			
			// closing CRLF
			dos.writeBytes(CRLF);
		} catch (Exception e) {
			Log.i("MyTag", "GeoPictureUploader.writeFormField: got: " + e.getMessage());
		}
	}
	
	/*
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
	          	Log.i("MyTag", "Source File not exist :" +uploadFilePath);
	          	return 0;
	          }
	          else
	          {
	          	try {
	          		// open a URL connection to the Servlet
	          		String imgurl = SystemValue.conn_public;
	          		FileInputStream fileInputStream = new FileInputStream(sourceFile);
	          		URL Httpurl = new URL(imgurl);
	          		
	          		// Open a HTTP  connection to  the URL
	          		conn = (HttpURLConnection) Httpurl.openConnection(); 
	          		conn.setDoInput(true); // Allow Inputs
	          		conn.setDoOutput(true); // Allow Outputs
	          		conn.setUseCaches(false); // Don't use a Cached Copy
	          		conn.setRequestMethod("POST");
	          		conn.setRequestProperty("Connection", "Keep-Alive");
//	          		conn.setRequestProperty("Charset", "UTF-8");
//	          		conn.setRequestProperty("Content-Language", "UTF-8");  
	          		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	          		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	          		conn.setRequestProperty("uploaded_file", fileName);			// 파일명
	          		
	          		dos = new DataOutputStream(conn.getOutputStream());
	          		
	          		dos.writeBytes(twoHyphens + boundary + lineEnd);
	          		
	          		dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename=");
	          		dos.write(fileName.getBytes("utf-8"));
	          		dos.writeBytes("" + lineEnd);
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
	           
	          		// 파일 이외에 파라미터로 넘길 값들
		                   	dos.writeBytes(lineEnd+twoHyphens+boundary+lineEnd);
	          		dos.writeBytes("Content-Disposition: form-data; name='menu';" + lineEnd + lineEnd + this.menu);
	          		dos.writeBytes(lineEnd+twoHyphens+boundary+lineEnd);
	          		dos.writeBytes("Content-Disposition: form-data; name='mem_num';" + lineEnd + lineEnd + this.mem_num);
	          		dos.writeBytes(lineEnd+twoHyphens+boundary+lineEnd);
	          		dos.writeBytes("Content-Disposition: form-data; name='board_num';" + lineEnd + lineEnd + this.board_num);
	          		dos.writeBytes(lineEnd+twoHyphens+boundary+lineEnd);
	          		dos.writeBytes("Content-Disposition: form-data; name='title';" + lineEnd + lineEnd);
	          		dos.write(title.getBytes("utf-8"));
	          		dos.writeBytes(lineEnd+twoHyphens+boundary+lineEnd);
	          		dos.writeBytes("Content-Disposition: form-data; name='article';" + lineEnd + lineEnd);
	          		dos.write(article.getBytes("utf-8"));
	          		dos.writeBytes(lineEnd);
	          		dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		                  
		                   // Responses from the server (code and message)
		                   serverResponseCode = conn.getResponseCode();
		                   String serverResponseMessage = conn.getResponseMessage();
	                     
//		                   Log.i("MyTag", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
	                    
		                   if(serverResponseCode == 200){
		          	         // 이미지 전송 완료
		          	         Log.i("MyTag", "이미지 전송 완료");
		          	         msg.what = 22;
		          	         mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
		                   }
		                   else{
		          	         Log.i("MyTag", "이미지 전송 실패");
		          	         msg.what = -22;
		          	         mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
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
	*/
	
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
							msg.what = -22;
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
						} else {
							msg.what = 22;
							Log.i("MyTag","test >> 333");
							mHandler.sendMessage(msg); // Handler에 다음 수행할 작업을 넘긴다
//							((MainActivity) mcontext).setMyMemberNum(Integer.parseInt(ret));
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
