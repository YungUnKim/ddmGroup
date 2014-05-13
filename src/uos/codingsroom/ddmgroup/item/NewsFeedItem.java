package uos.codingsroom.ddmgroup.item;

public class NewsFeedItem {
	private int index_num;
	private int read_count;
	private int reply_count;
	private String title;
	private String group_name;
	private String date;
	
	public NewsFeedItem() {
	}

	public NewsFeedItem(int index_num, int read_count, int reply_count, String title, String group_name, String date) {
		this.index_num = index_num;
		this.read_count = read_count;
		this.reply_count = reply_count;
		this.title = title;
		this.group_name = group_name;
		this.date = date;
	}
	
	public int getIndexNum(){
		return index_num;
	}
	
	public void setIndexNum(int index_num){
		this.index_num = index_num;
	}

	public int getReadCount() {
		return read_count;
	}

	public void setReadCount(int read_count) {
		this.read_count = read_count;
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

	public String getGroupName() {
		return group_name;
	}

	public void setGroupName(String group_name) {
		this.group_name = group_name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
