package com.mch.registry.ccs.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
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
	}

	@Override
	public void onCreate() {
		Context.registerReceiver(myBroadcast, new IntentFilter("android.provider.Telephony.SMS_RECEIVED").setPriority(999));

	}
}
