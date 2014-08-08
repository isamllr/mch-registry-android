package com.mch.registry.ccs.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.mch.registry.ccs.app.Constants;
import com.mch.registry.ccs.app.GcmIntentService;
import com.mch.registry.ccs.app.R;

/**
 * Created by Isa
 */
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

		if (origNumber.contains(Constants.SMS_SENDER_PART)){
			sendVerificationCodeBackToServer(msgBody, context, intent);
		}
	}

	private void sendVerificationCodeBackToServer(String verificationCode, Context context, Intent intent) {
		Toast.makeText(context.getApplicationContext(), R.string.activation_code_received, Toast.LENGTH_LONG).show();
		Intent msgIntent = new Intent(context.getApplicationContext(), GcmIntentService.class);
		msgIntent.setAction(Constants.ACTION_ECHO);
		String msg = "_Verify: " + verificationCode;
		msgIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		context.getApplicationContext().startService(msgIntent);
	}
}
