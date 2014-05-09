package uos.codingsroom.ddmgroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MakePreferences {
	static SharedPreferences preferences;
	Editor prefEdit;
	
	public MakePreferences(Context context){
		preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
		prefEdit = preferences.edit();
	}
	
	public SharedPreferences getMyPreference(){
		return preferences;
	}
	
	public Editor getMyPrefEditor(){
		return prefEdit;
	}
	
	public void commitMyPref(){
		prefEdit.commit();
	}
}
