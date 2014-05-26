package uos.codingsroom.ddmgroup.item;

import java.util.ArrayList;

public class MemberItem {
	private int num;
	private String name;
	private String date;
	private String thumbnail;
	private int content_cnt;
	private int reply_cnt;
	private ArrayList<Integer> myboard = new ArrayList<Integer>();
	private ArrayList<Integer> mylevel = new ArrayList<Integer>();
	
	public MemberItem(){
		
	}
	
	// 리스트 용도
	public MemberItem(int num, String name, String date) {
		this.num = num;
		this.name = name;
		this.date = date;
	}
	
	// 정보 열람 용도
	public MemberItem(int num, String name, String date, String thumbnail, int content_cnt, int reply_cnt) {
		this.num = num;
		this.name = name;
		this.date = date;
		this.thumbnail = thumbnail;
		this.content_cnt = content_cnt;
		this.reply_cnt = reply_cnt;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getContent_cnt() {
		return content_cnt;
	}

	public void setContent_cnt(int content_cnt) {
		this.content_cnt = content_cnt;
	}

	public int getReply_cnt() {
		return reply_cnt;
	}

	public void setReply_cnt(int reply_cnt) {
		this.reply_cnt = reply_cnt;
	}

	public ArrayList<Integer> getMyboard() {
		return myboard;
	}

	public void setMyboard(ArrayList<Integer> myboard) {
		this.myboard = myboard;
	}

	public ArrayList<Integer> getMylevel() {
		return mylevel;
	}

	public void setMylevel(ArrayList<Integer> mylevel) {
		this.mylevel = mylevel;
	}

}
