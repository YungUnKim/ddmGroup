package uos.codingsroom.ddmgroup.item;

public class CommentItem {
	private int index_num;
	private String article;
	private String date;
	private String kakaoName;
	private String kakaoUrl;

	public CommentItem(int index_num, String article, String date, String kakaoName, String kakaoUrl) {
		this.index_num = index_num;
		this.article = article;
		this.date = date;
		this.kakaoName = kakaoName;
		this.kakaoUrl = kakaoUrl;
	}

	public int getIndexNum() {
		return index_num;
	}

	public void setIndexNum(int index_num) {
		this.index_num = index_num;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKakaoName() {
		return kakaoName;
	}

	public void setKakaoName(String kakaoName) {
		this.kakaoName = kakaoName;
	}

	public String getKakaoUrl() {
		return kakaoUrl;
	}

	public void setKakaoUrl(String kakaoUrl) {
		this.kakaoUrl = kakaoUrl;
	}

}
