package uos.codingsroom.ddmgroup.item;

import java.util.ArrayList;

public class MyInfoItem {
	private Integer myMemNum;
	private String myProfileUrl;
	private String myName;
	private ArrayList<Integer> myboard = new ArrayList<Integer>();
	private ArrayList<Integer> mylevel = new ArrayList<Integer>();
	
	public MyInfoItem(Integer myMemNum, String myProfileUrl, String myName){
		this.myMemNum = myMemNum;
		this.myProfileUrl = myProfileUrl;
		this.myName = myName;
	}

	public Integer getMyMemNum() {
		return myMemNum;
	}

	public void setMyMemNum(Integer myMemNum) {
		this.myMemNum = myMemNum;
	}

	public String getMyProfileUrl() {
		return myProfileUrl;
	}

	public void setMyProfileUrl(String myProfileUrl) {
		this.myProfileUrl = myProfileUrl;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
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
