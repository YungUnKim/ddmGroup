package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MainActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class EventHandler extends Handler {
	Context mcontext;
	Context mcontext2;

	// 기본 생성자
	public EventHandler() {
	}

	// 생성자
	public EventHandler(Context context) {
		this.mcontext = context; // 액티비티 객체
	}

	public void handleMessage(Message msg) {
		try {
			if (msg.what == 0) { 
			
			}
			else if (msg.what == -10) { 
			
			}
			else if (msg.what == 10) { 
				((MainActivity) mcontext).showMyMemNumber();
				((MainActivity) mcontext).setProfile();
			}
			else if (msg.what == 11) { //공지사항
				((MainActivity) mcontext).setNoticeTitle();
			//	Log.i("MyTag", "핸들러 접근 성공");
			}
			else if (msg.what == 12) { //뉴스피드
				((MainActivity) mcontext).setNewsFeedList();
			//	Log.i("MyTag", "핸들러 접근 성공");
			}
			else if (msg.what == 13) { //게시글
				((MainActivity) mcontext).setContent();
			}
			else if (msg.what == 20) { 
			// Log.i("MyTag","Handler 10 >> " + msg);
				((MainActivity) mcontext).setLittleListView();
			//	Log.i("MyTag", "핸들러 접근 성공");
			}
			else if (msg.what == 22){
				Log.i("MyTag", "글 올리기 핸들러 성공");
			}
			else if (msg.what == -22){
				Log.i("MyTag", "글 올리기 핸들러 실패");
			}
			else if (msg.what == 24){
//				Log.i("MyTag", "글 얻어오기 핸들러 성공");
				((ContentsActivity) mcontext).setContentView();
			}
			else if (msg.what == -25){
				Log.i("MyTag", "글 수정하기 핸들러 실패");
			}
			else if (msg.what == 25){
				Log.i("MyTag", "글 수정하기 핸들러 성공");
//				((ContentsActivity) mcontext).setContentView();
			}
			else if (msg.what == -26){
				Log.i("MyTag", "글 삭제 핸들러 실패");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 실패하였습니다.");
			}
			else if (msg.what == 26){
				Log.i("MyTag", "글 삭제 핸들러 성공");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 성공하였습니다.");
			}
			else if (msg.what == 27){
				 ((ContentsActivity) mcontext).addComment();
//				Log.i("MyTag", "댓글 삽입 핸들러 성공");
			}
			else if (msg.what == -27){
				((ContentsActivity) mcontext).viewMessage("댓글 추가에 실패하였습니다.");
//				Log.i("MyTag", "댓글 삽입 핸들러 실패");
			}
			else if (msg.what == 28){
//				Log.i("MyTag", "댓글 얻어오기 핸들러");
				((ContentsActivity) mcontext).setListView();
			}
			else if (msg.what == 29){
				 ((ContentsActivity) mcontext).modifyComment();
//				Log.i("MyTag", "댓글 수정 핸들러 성공");
			}
			else if (msg.what == -29){
				((ContentsActivity) mcontext).viewMessage("댓글 수정에 실패하였습니다.");
//				Log.i("MyTag", "댓글 수정 핸들러 실패");
			}
			else if (msg.what == 30){
				((ContentsActivity) mcontext).deleteComment();
//				Log.i("MyTag", "댓글 삭제 핸들러 성공");
			}
			else if (msg.what == -30){
				((ContentsActivity) mcontext).viewMessage("댓글 삭제에 실패하였습니다.");
//				Log.i("MyTag", "댓글 삭제 핸들러 실패");
			}
		
		} catch (Exception e) {
		}
		return;
	}
}
