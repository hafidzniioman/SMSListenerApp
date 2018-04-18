package com.example.cetak1.smslistenerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();
    public SmsReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = getIncomingMessage(pdusObj[i], bundle);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getMessageBody();
                    Log.i("Sms Receiver", "Sender Num : " + senderNum +"; message : " +message);
                    Intent showSmsIntent = new Intent(context, SMSReceiverActivity.class);
                    showSmsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showSmsIntent.putExtra(SMSReceiverActivity.EXTRA_SMS_NO, phoneNumber);
                    showSmsIntent.putExtra(SMSReceiverActivity.EXTRA_SMS_MESSAGE, message);
                    context.startActivity(showSmsIntent);
                }
            }
        }catch(Exception e){
            Log.e("SMS RECEIVER", "Exception SMS RECEIVER" +e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle){
        SmsMessage currentSMS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String format = bundle.getString("Format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        }else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }
}
