package uos.codingsroom.ddmgroup.item;

public class NoticeItem {
	private int num;
	private int reply_count;
	private String title;
	private String date;

	public NoticeItem(int num, int reply_count, String title, String date) {
		this.num = num;
		this.reply_count = reply_count;
		this.title = title;
		this.date = date;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getReplyCount() {
		return reply_count;
	}

	public void setReplyCount(int reply_count) {
		this.reply_count = reply_count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
