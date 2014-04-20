package uos.codingsroom.ddmgroup.item;

public class ContentItem {
	private int index_num; 			// �� ��ȣ
	private int board_category; 	// ���� ��ȣ
	private int mem_num; 			// �۾��� ��ȣ
	private int reply_count; 		// ��� ��
	private String title; 			// �� ����
	private String article; 		// �� ����
	private String img_url; 		// ���� �ּ�
	private String date; 			// �� �Խ���
	private Boolean notice_check; 	// �������� ����

	// ����
	public ContentItem(int index_num, int board_category, int mem_num, int reply_count, 
			String title, String article, String img_url, String date, Boolean notice_check) {

		this.index_num = index_num;
		this.board_category = board_category;
		this.mem_num = mem_num;
		this.reply_count = reply_count;
		this.title = title;
		this.article = article;
		this.img_url = img_url;
		this.date = date;
		this.notice_check = notice_check;
	}

	public int getIndexNum() {
		return index_num;
	}
	public void setIndexNum(int index_num) {
		this.index_num = index_num;
	}

	public int getBoardCategory() {
		return board_category;
	}
	public void setBoardCategory(int board_category) {
		this.board_category = board_category;
	}

	public int getMemberNum() {
		return mem_num;
	}
	public void setMemberNum(int mem_num) {
		this.mem_num = mem_num;
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

	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}

	public String getImgUrl() {
		return img_url;
	}
	public void setImgUrl(String img_url) {
		this.img_url = img_url;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getNoticeCheck() {
		return notice_check;
	}
	public void setNoticeCheck(Boolean notice_check) {
		this.notice_check = notice_check;
	}

}
