package uos.codingsroom.ddmgroup.item;

public class MyInfoItem {
	private Integer myMemNum;
	private String myProfileUrl;
	private String myName;
	
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
	
}
