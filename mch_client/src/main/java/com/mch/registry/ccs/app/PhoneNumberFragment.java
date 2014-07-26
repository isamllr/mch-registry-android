/*
 * Copyright (C) 2014 Wolfram Rittmeyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mch.registry.ccs.app;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mch.registry.ccs.app.Constants.EventbusMessageType;
import com.mch.registry.ccs.app.Constants.State;
import com.mch.registry.ccs.data.PatientDataHandler;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PhoneNumberFragment extends Fragment implements View.OnClickListener {

	private Button btnOK;
	private EditText mobilePhoneNumber;
	private Constants.State mState = Constants.State.UNREGISTERED;
	private static final int RC_RES_REQUEST = 100;
	private static final int RC_SELECT_ACCOUNT = 200;

	public static GcmDemoFragment newInstance() {
		return new GcmDemoFragment();
	}

	public void onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v("PregnancyGuide", "onCreateContentView " + container);
		if (!checkPlayServices()) {
			inflater.inflate(
					R.layout.container_content_no_play_services, container, true);
			return;
		}
		// get current state from prefs
		mState = getCurrState();

		View root = inflater.inflate(R.layout.container_content_gcm_demo, container, true);

		/* Load phone input field */
		mobilePhoneNumber = (EditText) root.findViewById(R.id.input_phone);
		mobilePhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		mobilePhoneNumber.requestFocus();

		PatientDataHandler pdh = new PatientDataHandler(getActivity(), null, null, 1);

		if(pdh.getPatient().get_mobileNumber().length()>0){
			mobilePhoneNumber.setText(pdh.getPatient().get_mobileNumber());
		}else{
			TelephonyManager tMgr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
			mobilePhoneNumber.setText(tMgr.getLine1Number());
		}

        /* Load OK Button */
		btnOK = (Button) root.findViewById(R.id.btnOK);

		if (mState != Constants.State.REGISTERED) {
			btnOK.setEnabled(false);
		}
		else {
			btnOK.setOnClickListener(this);
		}
		Log.v("PregnancyGuide", "onCreateContentView");
	}

	private State getCurrState() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		int stateAsInt = prefs.getInt(Constants.KEY_STATE,
				State.UNREGISTERED.ordinal());
		return State.values()[stateAsInt];
	}

	private String getRegId() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
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
					Log.e("PregnancyGuide", "default click event, don't know what to do");
					break;
			}

		if(validatePhoneNumber(mobilePhoneNumber.getText().toString())){
			PatientDataHandler pdh = new PatientDataHandler(getActivity(), null, null, 1);
			//Save phone number
			pdh.updateMobilePhoneNumber(mobilePhoneNumber.getText().toString());
			//Send new phone number to server
			sendMessage();
		}else{
			Crouton.showText(getActivity(), getString(R.string.number_invalid), Style.ALERT);
		}
		// else if (view.getId() == R.id.btn) {
		//	sendMessage();
	}

	private boolean validatePhoneNumber(String mobilePhoneNumber) {
		return mobilePhoneNumber.matches("[+]\\d{11,12}");
	}

	@TargetApi(value=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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

	private void sendMessage() {
		Intent msgIntent = new Intent(getActivity(), GcmIntentService.class);
		msgIntent.setAction(Constants.ACTION_ECHO);
		String msg = mobilePhoneNumber.getText().toString();

		String msgTxt = getString(R.string.msg_sent, msg);
		Crouton.showText(getActivity(), msgTxt, Style.INFO);
		msgIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		getActivity().startService(msgIntent);
	}

	/**
	 * EventBus messages.
	 */
	public void onEventMainThread(Bundle bundle) {
		int typeOrdinal = bundle.getInt(Constants.KEY_EVENT_TYPE);
		EventbusMessageType type = EventbusMessageType.values()[typeOrdinal];
		switch (type) {
			case REGISTRATION_FAILED:
				btnOK.setEnabled(false);
				break;
			case REGISTRATION_SUCCEEDED:
				mState = State.REGISTERED;
				break;
			case UNREGISTRATION_FAILED:
				break;
			case UNREGISTRATION_SUCCEEDED:
				mState = State.UNREGISTERED;
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
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), RC_RES_REQUEST).show();
			} else {
				Log.i("PregnancyGuide", "This device is not supported.");
			}
			return false;
		}
		return true;
	}

	private void registerDevice() {
		Intent regIntent = new Intent(getActivity(), GcmIntentService.class);
		regIntent.putExtra(Constants.KEY_ACCOUNT, Constants.DEFAULT_USER);
		regIntent.setAction(Constants.ACTION_REGISTER);
		getActivity().startService(regIntent);
	}

	private static final String ARG_SECTION_NUMBER = "3";

	public static PhoneNumberFragment newInstance(int sectionNumber) {
		PhoneNumberFragment fragment = new PhoneNumberFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_informed_pregnancy, container, false);
		return rootView;
	}

}
