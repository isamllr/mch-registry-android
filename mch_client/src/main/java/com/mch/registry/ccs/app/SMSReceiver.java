package com.mch.registry.ccs.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver{

    public SMSReceiver() {
    }

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle extras = intent.getExtras();

		if (extras == null)
			return;

		Object[] pdus = (Object[]) extras.get("pdus");
		SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
		String origNumber = msg.getOriginatingAddress();
		String msgBody = msg.getMessageBody();
		sendVerificationCodeToServer(msgBody, context, intent);
	}

	private String sendVerificationCodeToServer(String verificationCode, Context context, Intent intent) {
		Intent msgIntent = new Intent(context.getApplicationContext(), GcmIntentService.class);
		msgIntent.setAction(Constants.ACTION_ECHO);
		String msg = "_Verify: " + verificationCode;
		String msgTxt = "Checking verification code: " + verificationCode + ".";
		msgIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		context.getApplicationContext().startService(msgIntent);
		return verificationCode;
	}
}
