package uos.codingsroom.ddmgroup.item;

public class GroupItem {
	private int index_num;			
	private int cat_num;			
	private int master_num;	
	private String title;			
	private String description;
	
	public GroupItem() {
		
	}
	
	public GroupItem(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public GroupItem(int index_num, int cat_num, int master_num, String title, String description) {
		this.index_num = index_num;
		this.cat_num = cat_num;
		this.master_num = master_num;
		this.title = title;
		this.description = description;
	}
	
	
	public int getIndexNum(){
		return index_num;
	}
	public void setIndexNum(int index_num){
		this.index_num = index_num;
	}
	
	public int getCategoryNum(){
		return cat_num;
	}
	public void setCategoryNum(int cat_num){
		this.cat_num = cat_num;
	}
	
	public int getMasterNum(){
		return master_num;
	}
	public void setMasterNum(int master_num){	
		this.master_num = master_num;
	}
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
}
