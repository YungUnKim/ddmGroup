package uos.codingsroom.ddmgroup.item;

public class ReplyItem {
	private int index_num;			
	private int mem_num;			
	private int board_num;			
	private String article;		
	private String date;			


	public ReplyItem(int index_num, int mem_num, int board_num, String article, String date) {
		this.index_num = index_num;
		this.mem_num = mem_num;
		this.board_num = board_num;
		this.article = article;
		this.date = date;
	}

	public int getIndexNum() {
		return index_num;
	}
	public void setIndexNum(int index_num) {
		this.index_num = index_num;
	}
	
	public int getMemberNum(){
		return mem_num;
	}
	public void setMemberNum(int mem_num){
		this.mem_num = mem_num;	
	}
	
	public int getBoardNum(){
		return board_num;
	}
	public void setBoardNum(int board_num){
		this.board_num = board_num;
	}
	
	public String getArticle(){
		return article;
	}
	public void setArticle(String article){
		this.article = article;
	}
	
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}
}
