package uos.codingsroom.ddmgroup;


import static uos.codingsroom.ddmgroup.BasicInfo.PROJECT_ID;
import static uos.codingsroom.ddmgroup.BasicInfo.TOAST_MESSAGE_ACTION;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;


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

	@Override
    public void onMessage(Context context, Intent intent) {
    	Log.i(TAG, "onMessage called.");

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String msg = (String) extras.get("msg");
            String from = (String) extras.get("from");
            String action = (String) extras.get("action");

            Log.d(TAG, "DATA : " + from + ", " + action + ", " + msg);
            Log.d(TAG, "[" + from + "]로부터 온 메세지 : " + msg);
            
            notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.i(TAG, "1");
    		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    		Log.i(TAG, "2");
            //----------알림설정----------//
            Notification noti;
			noti = new Notification(R.drawable.ic_launcher,"새 글이 등록되었습니다.", System.currentTimeMillis());
			Log.i(TAG, "3");
			noti.defaults = Notification.DEFAULT_SOUND;
			noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
			noti.flags = Notification.FLAG_AUTO_CANCEL;
			Log.i(TAG, "4");
			Intent newIntent = new Intent(getBaseContext(), ContentsActivity.class);
			PendingIntent pendingI = PendingIntent.getActivity(GCMIntentService.this, 0, newIntent, newIntent.FLAG_ACTIVITY_NEW_TASK);
			Log.i(TAG, "5");
			noti.setLatestEventInfo(GCMIntentService.this, "동대문구청","새글이 등록되었습니다.", pendingI);
			Log.i(TAG, "6");
			notiManager.notify(MyNoti, noti);
			Log.i(TAG, "7");
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