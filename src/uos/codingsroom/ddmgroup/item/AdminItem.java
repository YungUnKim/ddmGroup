package uos.codingsroom.ddmgroup.item;

public class AdminItem {
	private int num;
	private String title;
	private String subData;

	public AdminItem() {

	}

	public AdminItem(int num, String title, String subData) {
		this.num = num;
		this.title = title;
		this.subData = subData;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubData() {
		return subData;
	}

	public void setSubData(String date) {
		this.subData = date;
	}
}
