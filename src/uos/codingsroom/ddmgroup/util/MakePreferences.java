package uos.codingsroom.ddmgroup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MakePreferences {
	static SharedPreferences preferences;
	Editor prefEdit;
	
	public MakePreferences(Context context){
		preferences = context.getSharedPreferences("prefs", 0);
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
