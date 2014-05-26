package uos.codingsroom.ddmgroup.item;


public class BoardItem {
	private int num;
	private String title;
	private String category;
	private String dscr;
	private int content_cnt;

	public BoardItem(){
		
	}
	
	// 리스트 용도
	public BoardItem(int num, String title, String category) {
		this.num = num;
		this.setTitle(title);
		this.setCategory(category);
	}
	
	// 정보 열람 용도
	public BoardItem(int num, String title, String category, String dscr, int content_cnt) {
		this.num = num;
		this.setTitle(title);
		this.setCategory(category);
		this.setDscr(dscr);
		this.content_cnt = content_cnt;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getContent_cnt() {
		return content_cnt;
	}

	public void setContent_cnt(int content_cnt) {
		this.content_cnt = content_cnt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDscr() {
		return dscr;
	}

	public void setDscr(String dscr) {
		this.dscr = dscr;
	}

	
}
