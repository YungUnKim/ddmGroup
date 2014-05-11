package uos.codingsroom.ddmgroup.push;


import static uos.codingsroom.ddmgroup.push.BasicInfo.PROJECT_ID;
import static uos.codingsroom.ddmgroup.push.BasicInfo.TOAST_MESSAGE_ACTION;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	
	/**
	 * Constructor
	 */
    public GCMIntentService() {
        super(PROJECT_ID);

        Log.d(TAG, "GCMIntentService() called.");
    }

    @Override
    public void onRegistered(Context context, String registrationId) {
    	Log.d(TAG, "onRegistered called : " + registrationId);

    	BasicInfo.RegistrationId = registrationId;

    	sendToastMessage(context, "�ܸ��� ��ϵǾ� ��� ID�� �޾ҽ��ϴ�.");
    }

    @Override
    public void onUnregistered(Context context, String registrationId) {
    	Log.d(TAG, "onUnregistered called.");

    	sendToastMessage(context, "��������Ǿ���ϴ�.");
    }

    @Override
    public void onError(Context context, String errorId) {
    	Log.d(TAG, "onError called.");

    	sendToastMessage(context, "�����Դϴ� : " + errorId);
    }

    @Override
	protected void onDeletedMessages(Context context, int total) {
    	Log.d(TAG, "onDeletedMessages called.");
    	
    	super.onDeletedMessages(context, total);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(TAG, "onRecoverableError called.");
		
		return super.onRecoverableError(context, errorId);
	}

	@Override
    public void onMessage(Context context, Intent intent) {
    	Log.d(TAG, "onMessage called.");

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String msg = (String) extras.get("msg");
            String from = (String) extras.get("from");
            String action = (String) extras.get("action");

            Log.d(TAG, "DATA : " + from + ", " + action + ", " + msg);
            Log.d(TAG, "[" + from + "]�κ��� ������ �޽��� : " + msg);

            Intent newIntent = new Intent(getBaseContext(), TempPushService.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            newIntent.putExtra("msg", msg);
            newIntent.putExtra("from", from);
            newIntent.putExtra("action", action);
            context.startActivity(newIntent);

            sendToastMessage(context, "�����κ��� �޽����� �޾ҽ��ϴ�.");
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