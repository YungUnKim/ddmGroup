package uos.codingsroom.ddmgroup;

import android.content.Context;
import android.content.Intent;

public class Content_intent extends Intent{
	public Context mcontext;		// 액티비티 객체
	public String GroupName;
	public int board_num;
	public int content_num;
	public int mem_num;
	public boolean mode;
	
	// 생성자
	public Content_intent(Context context,String GroupName, int board_num, int content_num, int mem_num, boolean mode){
		this.mcontext = context;
		this.GroupName = GroupName;
		this.board_num = board_num;
		this.content_num = content_num;
		this.mem_num = mem_num;
		this.mode = mode;
	}
	
	public Intent put_intent(){
		final Intent intent = new Intent(mcontext, ContentsActivity.class);
		
		intent.putExtra("board_name", GroupName);
		intent.putExtra("board_num", board_num);
		intent.putExtra("content_num", content_num);
		intent.putExtra("mem_num", mem_num);
		intent.putExtra("mode",mode);
		
		return intent;
	}
}
