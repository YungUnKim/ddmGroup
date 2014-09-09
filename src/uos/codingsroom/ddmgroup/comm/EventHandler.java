package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.MainActivity;
import uos.codingsroom.ddmgroup.ModifyActivity;
import uos.codingsroom.ddmgroup.NoticeActivity;
import uos.codingsroom.ddmgroup.SettingActivity;
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
//			Log.i("MyTag","EventHandler >> " + msg.what);
			if (msg.what == 0) {

			} else if (msg.what == 9) { // 회원탈퇴 성공
				((SettingActivity) mcontext).redirectLoginActivity("회원 탈퇴하였습니다.", 0);
			} else if (msg.what == -9) { // 회원탈퇴 실패
				((SettingActivity) mcontext).viewMessage("회원탈퇴에 실패하였습니다.");
			} else if (msg.what == -10) { // 로그인 실패

			} else if (msg.what == 10) { // 로그인 성공
//				((MainActivity) mcontext).setPermission();
//				((MainActivity) mcontext).setProfile();
//				((MainActivity) mcontext).setGroupButtonClickListener();
				((MainActivity) mcontext).setMainActivity();
			} else if (msg.what == 11) { // 공지사항 3개 얻어오기 성공
				((MainActivity) mcontext).setNoticeTitle();
			} else if (msg.what == 12) { // 뉴스피드 목록 얻어오기 성공
				((MainActivity) mcontext).setNewsFeedList();
			} else if (msg.what == 13) { // 게시글 목록 얻어오기 성공
				((MainActivity) mcontext).setContent();
			} else if (msg.what == 14) { // 공지사항 목록 얻어오기 성공
				((NoticeActivity) mcontext).setListView();
			} else if (msg.what == -14) { // 공지사항 목록 얻어오기 실패
				((NoticeActivity) mcontext).viewMessage("공지사항 목록을 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 20) { // 소분류 얻어오기 성공
				((MainActivity) mcontext).setLittleListView();
			} else if (msg.what == 22) { // 글 올리기 성공
				((MainActivity) mcontext).clearRegisterFragment();
			} else if (msg.what == -22) { // 글 올리기 실패
				((MainActivity) mcontext).viewMessage("글 올리기에 실패하였습니다.");
			} else if (msg.what == 24) { // 글 얻어오기 성공
				((ContentsActivity) mcontext).setContentView();
			} else if (msg.what == -24) { // 글 얻어오기 실패
				((ContentsActivity) mcontext).viewMessage("글 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == -25) { // 글 수정 실패
				((ModifyActivity) mcontext).viewMessage("글 수정하는데 실패하였습니다.");
			} else if (msg.what == 25) { // 글 수정 성공
				((ModifyActivity) mcontext).viewMessage("글을 수정하였습니다.", 2);
			} else if (msg.what == -26) { // 글 삭제 실패
				((ContentsActivity) mcontext).viewMessage("글 삭제에 실패하였습니다.");
			} else if (msg.what == 26) { // 글 삭제 성공
				((ContentsActivity) mcontext).viewMessage("글 삭제에 성공하였습니다.", 1);
			} else if (msg.what == 27) { // 댓글 삽입 성공
				((ContentsActivity) mcontext).addComment();
			} else if (msg.what == -27) { // 댓글 삽입 실패
				((ContentsActivity) mcontext).viewMessage("댓글 추가에 실패하였습니다.");
			} else if (msg.what == 28) { // 댓글 얻어오기 성공
				((ContentsActivity) mcontext).setListView();
			} else if (msg.what == 29) { // 댓글 수정 성공
				((ContentsActivity) mcontext).modifyComment();
			} else if (msg.what == -29) { // 댓글 수정 실패
				((ContentsActivity) mcontext).viewMessage("댓글 수정에 실패하였습니다.");
			} else if (msg.what == 30) { // 댓글 삭제 성공
				((ContentsActivity) mcontext).deleteComment();
			} else if (msg.what == -30) { // 댓글 삭제 실패
				((ContentsActivity) mcontext).viewMessage("댓글 삭제에 실패하였습니다.");
			} else if (msg.what == 34) { // 공지사항 내용 얻어오기
				((ContentsActivity) mcontext).setNoticeView();
			} else if (msg.what == -34) { // 공지사항 내용 얻어오기
				((ContentsActivity) mcontext).viewMessage("공지사항 내용을 얻어오는데 실패하였습니다.", 0);
			}

		} catch (Exception e) {
		}
		return;
	}
}
