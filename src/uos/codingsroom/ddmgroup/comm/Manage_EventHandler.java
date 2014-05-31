package uos.codingsroom.ddmgroup.comm;

import uos.codingsroom.ddmgroup.AdminActivity;
import uos.codingsroom.ddmgroup.ContentsActivity;
import uos.codingsroom.ddmgroup.ManageBoardActivity;
import uos.codingsroom.ddmgroup.ManageBoardInfoActivity;
import uos.codingsroom.ddmgroup.ManageMemberActivity;
import uos.codingsroom.ddmgroup.ManageMemberInfoActivity;
import uos.codingsroom.ddmgroup.ModifyActivity;
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
				
			} else if (msg.what == 1000) { // 전체 정보 받아오기 성공
				((AdminActivity) mcontext).setView();
			}
			
			else if (msg.what == -1100) { // 회원 리스트 받아오기 실패
				((ManageMemberActivity) mcontext).viewMessage("회원목록 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1100) { // 회원 리스트 받아오기 성공
				((ManageMemberActivity) mcontext).setListView();
			} else if (msg.what == -1110) { // 회원 정보 받아오기 실패
				((ManageMemberInfoActivity) mcontext).viewMessage("회원 정보 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1110) { // 회원 정보 받아오기 성공
				((ManageMemberInfoActivity) mcontext).setView();
			} else if (msg.what == -1120) { // 권한 추가 실패(블랙리스트) - 관리자메뉴
				((ManageMemberInfoActivity) mcontext).viewMessage("블랙리스트 지정 실패");
			} else if (msg.what == 1120) { // 권한 추가 성공(블랙리스트) - 관리자메뉴
				((ManageMemberInfoActivity) mcontext).setBlacklist(true);
			} else if (msg.what == -1121) { // 권한 추가 실패(블랙리스트) - 글
				((ContentsActivity) mcontext).viewMessage("블랙리스트 지정 실패");
			} else if (msg.what == 1121) { // 권한 추가 성공(블랙리스트) - 글
				((ContentsActivity) mcontext).setBlacklist(true);
			} else if (msg.what == -1140) { // 권한 삭제 실패(블랙리스트)
				((ManageMemberInfoActivity) mcontext).viewMessage("블랙리스트 취소 실패");
			} else if (msg.what == 1140) { // 권한 삭제 성공(블랙리스트)
				((ManageMemberInfoActivity) mcontext).setBlacklist(false);
			}
			
			else if (msg.what == -1200) { // 게시판 리스트 받아오기 실패
				((ManageBoardActivity) mcontext).viewMessage("게시판 목록을 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1200) { // 게시판 리스트 받아오기 성공
				((ManageBoardActivity) mcontext).setListView();
			} else if (msg.what == -1210) { // 게시판 정보 받아오기 실패
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판 정보를 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1210) { // 게시판 정보 받아오기 성공
				((ManageBoardInfoActivity) mcontext).setView();
			} else if (msg.what == -1220) { // 게시판 생성하기 실패
				((AdminActivity) mcontext).viewMessage("게시판 생성하는데 실패하였습니다.");
			} else if (msg.what == 1220) { // 게시판 생성하기 성공
				((AdminActivity) mcontext).initForm();
			} else if (msg.what == -1230) { // 게시판 수정 실패
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판 수정하는데 실패하였습니다.");
			} else if (msg.what == 1230) { // 게시판 수정 성공
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판을 수정하였습니다.",0);
			} else if (msg.what == -1240) { // 게시판 삭제 실패
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판 삭제하는데 실패하였습니다.");
			} else if (msg.what == 1240) { // 게시판 삭제 성공
				((ManageBoardInfoActivity) mcontext).viewMessage("게시판을 삭제하였습니다.",1);
			}
			
			else if (msg.what == -1300) { // 공지사항 리스트 받아오기 실패
				((NoticeActivity) mcontext).viewMessage("공지사항 목록을 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == 1300) { // 공지사항 리스트 받아오기 성공
				((NoticeActivity) mcontext).setListView();
			}
			
			else if (msg.what == -1400) { // 공지사항 올리기 실패
				Log.i("MyTag", "글 올리기 핸들러 실패");
			} else if (msg.what == 1400) { // 공지사항 올리기 성공
				((NoticeRegisterActivity) mcontext).clearContent();
			} else if (msg.what == -1420) { // 공지사항 수정 실패
				((ModifyActivity) mcontext).viewMessage("공지사항 수정하는데 실패하였습니다.");
			} else if (msg.what == 1420) { // 공지사항 수정 성공
				((ModifyActivity) mcontext).viewMessage("공지사항을 수정하였습니다.", 2);
			} else if (msg.what == -1430) { // 공지사항 삭제 실패
				((ContentsActivity) mcontext).viewMessage("공지사항 삭제에 실패하였습니다.");
			} else if (msg.what == 1430) { // 공지사항 삭제 성공
				((ContentsActivity) mcontext).viewMessage("공지사항을 삭제하였습니다.", 1);
			}
			
			else if (msg.what == 24) {
				((ContentsActivity) mcontext).setContentView();
			} else if (msg.what == -24) {
				((ContentsActivity) mcontext).viewMessage("글 얻어오는데 실패하였습니다.", 0);
			} else if (msg.what == -25) {
				Log.i("MyTag", "글 수정하기 핸들러 실패");
			} else if (msg.what == 25) {
				Log.i("MyTag", "글 수정하기 핸들러 성공");
			} else if (msg.what == -26) {
				((ContentsActivity) mcontext).viewMessage("글 삭제에 실패하였습니다.");
			} else if (msg.what == 26) {
				((ContentsActivity) mcontext).viewMessage("글 삭제에 성공하였습니다.", 0);
			}

		} catch (Exception e) {
		}
		return;
	}
}
