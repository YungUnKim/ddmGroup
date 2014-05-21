package uos.codingsroom.ddmgroup;


import static uos.codingsroom.ddmgroup.BasicInfo.PROJECT_ID;
import static uos.codingsroom.ddmgroup.BasicInfo.TOAST_MESSAGE_ACTION;

import java.util.StringTokenizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "PUSH";
	NotificationManager notiManager;
	Vibrator vibrator;
	final static int MyNoti = 0;
	/**
	 * Constructor
	 */
    public GCMIntentService() {
        super(PROJECT_ID);

        Log.i(TAG, "GCMIntentService() called.");
    }

    @Override
    public void onRegistered(Context context, String registrationId) {
    	Log.i(TAG, "onRegistered called : " + registrationId);

    	BasicInfo.RegistrationId = registrationId;

    	sendToastMessage(context, "기기가 등록되었습니다..");
    }

    @Override
    public void onUnregistered(Context context, String registrationId) {
    	Log.i(TAG, "onUnregistered called.");

    	sendToastMessage(context, "등록이 해지되었습니다..");
    }

    @Override
    public void onError(Context context, String errorId) {
    	Log.i(TAG, "onError called.");

    	sendToastMessage(context, "에러입니다: " + errorId);
    }

    @Override
	protected void onDeletedMessages(Context context, int total) {
    	Log.i(TAG, "onDeletedMessages called.");
    	
    	super.onDeletedMessages(context, total);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "onRecoverableError called.");
		
		return super.onRecoverableError(context, errorId);
	}

	@SuppressWarnings("deprecation")
	@Override
    public void onMessage(Context context, Intent intent) {
    	Log.i(TAG, "onMessage called.");

        Bundle extras = intent.getExtras();
        if (extras != null) {           
            String msg = (String) extras.get("message");
            String[] token = new String[4];
            StringTokenizer st = new StringTokenizer(msg);
            for(int i=0; i<3 ; i++)
            {
            	token[i] = st.nextToken(":}");
            }
            int content_num = Integer.parseInt(token[2]);
            Log.i("PUSH","글번호 = "+content_num);
            notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            //----------알림설정----------//
            Notification noti;
			noti = new Notification(R.drawable.ddmlogo3,"새 글이 등록되었습니다.", System.currentTimeMillis());
			noti.defaults = Notification.DEFAULT_SOUND;
			noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
			noti.flags = Notification.FLAG_AUTO_CANCEL;
			Intent newIntent = new Intent(getBaseContext(), ContentsActivity.class);
			newIntent.putExtra("mode", true);
            newIntent.putExtra("group_name", "공지사항");
            newIntent.putExtra("content_num", content_num);
			PendingIntent pendingI = PendingIntent.getActivity(GCMIntentService.this, 0, newIntent, newIntent.FLAG_ACTIVITY_NEW_TASK);
			noti.setLatestEventInfo(GCMIntentService.this, "동대문구청","새글이 등록되었습니다.", pendingI);
			notiManager.notify(MyNoti, noti);
			vibrator.vibrate(1000); 

            
            
            Log.i(TAG, "8");
            /*
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            newIntent.putExtra("msg", msg);
            newIntent.putExtra("from", from);
            newIntent.putExtra("action", action);
            */
             
            sendToastMessage(context, "메시지도착했습니다.");
            Log.i(TAG, "9");
        }
    }

	/**
	 * Send status messages for toast display
	 * 
	 * @param context
	 * @param message
	 */
	static void sendToastMessage(Context context, String message) {
        Intent intent = new Intent(TOAST_MESSAGE_ACTION);
        intent.putExtra("message", message);
        context.sendBroadcast(intent);
    }
    
}