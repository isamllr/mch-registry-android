package com.mch.registry.ccs.app;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mch.registry.ccs.data.Patient;
import com.mch.registry.ccs.data.PatientDataHandler;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MobileNumberActivity extends Activity implements View.OnClickListener {

	private Button btnOK;
	private EditText mobilePhoneNumber;
	private Constants.State mState = Constants.State.UNREGISTERED;
	private static final int RC_RES_REQUEST = 100;
	private static final int RC_SELECT_ACCOUNT = 200;

	private BroadcastReceiver smsReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

		//TODO:check play services
	    mState = getCurrState();

	    mobilePhoneNumber = (EditText) findViewById(R.id.input_phone);
	    mobilePhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
	    mobilePhoneNumber.requestFocus();

	    btnOK = (Button) findViewById(R.id.btnOK);
	    if (mState != Constants.State.REGISTERED) {
		    registerDevice();
		    btnOK.setOnClickListener(this);
		    //btnOK.setEnabled(false);
	    }
	    else {
		    btnOK.setOnClickListener(this);
	    }

	    Handler hand = new Handler();
	    hand.post(new Runnable() {
		    public void run() {
			    loadMobilePhone();
		    }
	    });

	    Log.v("PregnancyGuide", "MobilePhoneActivity");
    }

	private void loadMobilePhone(){
		PatientDataHandler pdh = new PatientDataHandler(getApplicationContext(), null, null, 1);
		Patient patient = new Patient();
		patient = pdh.getPatient();
		String mobileNumber = patient.get_mobileNumber();

		if(!mobileNumber.isEmpty()){
			mobilePhoneNumber.setText(mobileNumber);
		}else{
			TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			mobilePhoneNumber.setText(tMgr.getLine1Number());
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private Constants.State getCurrState() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		int stateAsInt = prefs.getInt(Constants.KEY_STATE,
				Constants.State.UNREGISTERED.ordinal());
		return Constants.State.values()[stateAsInt];
	}

	private String getRegId() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(Constants.KEY_REG_ID, null);
	}

	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onClick(View view) {
		Log.v("PregnancyGuide", "onClick: " + view.getId());
		switch (mState) {
			case UNREGISTERED:
				registerDevice();
				break;
			default:
				break;
		}

		PatientDataHandler pdh = new PatientDataHandler(this, null, null, 1);
		Patient patient = new Patient();
		patient = pdh.getPatient();
		String mobileNumber = mobilePhoneNumber.getText().toString().replaceAll("\\s","");

		if (patient.get_mobileNumber().compareTo(mobileNumber)==0){
			Crouton.showText(this, getString(R.string.number_same), Style.INFO);
		}else{
			if(validatePhoneNumber(mobileNumber)){

			//Save phone number
			pdh.updateMobilePhoneNumber(mobilePhoneNumber.getText().toString());
			//Send new phone number to server
			this.sendPhoneMessage();
			smsReceiver = new SMSReceiver();
			getApplicationContext().registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
			//wait until sms has arrived (new STATE!!)
			getApplicationContext().unregisterReceiver(smsReceiver);

			}else{
				Crouton.showText(this, getString(R.string.number_invalid), Style.ALERT);
			}
		}
	}

	private boolean validatePhoneNumber(String mobilePhoneNumber) {
		return mobilePhoneNumber.matches("[+]\\d{11,12}");
	}

	@TargetApi(value= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void startAccountSelector() {
		Intent selectAccount =
				AccountManager.
						newChooseAccountIntent(
								null,
								null,
								new String[]{"com.google"},
								false,
								null,
								null,
								null,
								null);
		startActivityForResult(selectAccount, RC_SELECT_ACCOUNT);
	}

	private void sendPhoneMessage() {
		Intent msgIntent = new Intent(this, GcmIntentService.class);
		msgIntent.setAction(Constants.ACTION_ECHO);
		String msg = "Phone: " + mobilePhoneNumber.getText().toString();

		String msgTxt = getString(R.string.phone_number_sent, msg);
		Crouton.showText(this, msgTxt, Style.INFO);
		msgIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		this.startService(msgIntent);
	}

	/**
	 * EventBus messages.
	 */
	public void onEventMainThread(Bundle bundle) {
		int typeOrdinal = bundle.getInt(Constants.KEY_EVENT_TYPE);
		Constants.EventbusMessageType type = Constants.EventbusMessageType.values()[typeOrdinal];
		switch (type) {
			case REGISTRATION_FAILED:
				btnOK.setEnabled(false);
				break;
			case REGISTRATION_SUCCEEDED:
				mState = Constants.State.REGISTERED;
				break;
			case UNREGISTRATION_FAILED:
				break;
			case UNREGISTRATION_SUCCEEDED:
				mState = Constants.State.UNREGISTERED;
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SELECT_ACCOUNT) {
			if (resultCode == Activity.RESULT_OK) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			}
			else {
				Log.v("PregnancyGuide", "couldn't select account: " + resultCode);
			}
		}
	}

	// taken more or less verbatim from the documentation:
	//
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, RC_RES_REQUEST).show();
			} else {
				Log.i("PregnancyGuide", "This device is not supported.");
			}
			return false;
		}
		return true;
	}

	private void registerDevice() {
		Intent regIntent = new Intent(getApplicationContext(), GcmIntentService.class);
		regIntent.putExtra(Constants.KEY_ACCOUNT, Constants.DEFAULT_USER);
		regIntent.setAction(Constants.ACTION_REGISTER);
		getApplicationContext().startService(regIntent);
	}
}
