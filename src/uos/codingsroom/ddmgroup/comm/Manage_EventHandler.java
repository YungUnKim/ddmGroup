package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.AdminActivity;
import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.ManageBoardActivity;
import uos.codingsroom.ddmgroup.ManageBoardInfoActivity;
import uos.codingsroom.ddmgroup.ManageMemberActivity;
import uos.codingsroom.ddmgroup.ManageMemberInfoActivity;
import uos.codingsroom.ddmgroup.NoticeActivity;
import uos.codingsroom.ddmgroup.NoticeRegisterActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Manage_EventHandler extends Handler {
	Context mcontext;
	Context mcontext2;

	// 기본 생성자
	public Manage_EventHandler() {
	}

	// 생성자
	public Manage_EventHandler(Context context) {
		this.mcontext = context; // 액티비티 객체
	}

	public void handleMessage(Message msg) {
		try {
			if (msg.what == 0) {

			} else if (msg.what == -1000) { // 전체 정보 받아오기 실패
//				Log.i("MyTag", "Handler -1000 >> ");
			} else if (msg.what == 1000) { // 전체 정보 받아오기 성공
//				Log.i("MyTag", "Handler 1000 >> ");
				((AdminActivity) mcontext).setView();
			} else if (msg.what == -1100) { // 회원 리스트 받아오기 실패
//				Log.i("MyTag", "Handler -1100 >> ");
				((ManageMemberActivity) mcontext).viewMessage("회원목록 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1100) { // 회원 리스트 받아오기 성공
//				Log.i("MyTag", "Handler 1100 >> ");
				((ManageMemberActivity) mcontext).setListView();
			} else if (msg.what == -1110) { // 회원 정보 받아오기 실패
//				Log.i("MyTag", "Handler -1110 >> ");
				((ManageMemberInfoActivity) mcontext).viewMessage("회원 정보 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1110) { // 회원 정보 받아오기 성공
//				Log.i("MyTag", "Handler 1110 >> ");
				((ManageMemberInfoActivity) mcontext).setView();
			} else if (msg.what == -1200) { // 게시판 리스트 받아오기 실패
				((ManageBoardActivity) mcontext).viewMessage("게시판 목록을 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1200) { // 게시판 리스트 받아오기 성공
				((ManageBoardActivity) mcontext).setListView();
			} else if (msg.what == -1210) { // 게시판 정보 받아오기 실패
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판 정보를 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1210) { // 게시판 정보 받아오기 성공
				((ManageBoardInfoActivity) mcontext).setView();
			} else if (msg.what == -1300) { // 공지사항 리스트 받아오기 실패
				((NoticeActivity) mcontext).viewMessage("공지사항 목록을 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1300) { // 공지사항 리스트 받아오기 성공
				((NoticeActivity) mcontext).setListView();
			} else if (msg.what == -1400) { // 공지사항 올리기 실패
				Log.i("MyTag", "글 올리기 핸들러 실패");
			} else if (msg.what == 1400) { // 공지사항 올리기 성공
				((NoticeRegisterActivity) mcontext).clearContent();
				Log.i("MyTag", "글 올리기 핸들러 성공");
			} else if (msg.what == 24) {
				// Log.i("MyTag", "글 얻어오기 핸들러 성공");
				((ContentsActivity) mcontext).setContentView();
			} else if (msg.what == -24) {
				// Log.i("MyTag", "글 얻어오기 핸들러 실패");
				((ContentsActivity) mcontext).viewMessage("글 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == -25) {
				Log.i("MyTag", "글 수정하기 핸들러 실패");
			} else if (msg.what == 25) {
				Log.i("MyTag", "글 수정하기 핸들러 성공");
			} else if (msg.what == -26) {
				// Log.i("MyTag", "글 삭제 핸들러 실패");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 실패하였습니다.");
			} else if (msg.what == 26) {
				// Log.i("MyTag", "글 삭제 핸들러 성공");
				((ContentsActivity) mcontext).viewMessage("글 삭제에 성공하였습니다.", 0);
			}

		} catch (Exception e) {
		}
		return;
	}
}
