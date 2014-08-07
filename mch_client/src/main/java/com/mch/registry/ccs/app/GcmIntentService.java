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

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mch.registry.ccs.app.Constants.EventbusMessageType;
import com.mch.registry.ccs.app.Constants.State;
import com.mch.registry.ccs.data.Pregnancy;
import com.mch.registry.ccs.data.PregnancyDataHandler;
import com.mch.registry.ccs.data.RecommendationDataHandler;
import com.mch.registry.ccs.data.VisitDataHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class GcmIntentService extends IntentService {

   private NotificationManager mNotificationManager;
   private String mSenderId = null;

   public GcmIntentService() {
      super("GcmIntentService");
   }

   @Override
   ///New: Mueller
   protected void onHandleIntent(Intent intent) {
      mSenderId = Constants.PROJECT_ID;
      GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

      // action handling for actions of the activity
      String action = intent.getAction();
      Log.v("PregnancyGuide", "action: " + action);
      if (action.equals(Constants.ACTION_REGISTER)) {
         register(gcm, intent);
      } else if (action.equals(Constants.ACTION_UNREGISTER)) {
         unregister(gcm, intent);
      } else if (action.equals(Constants.ACTION_ECHO)) {
         sendMessage(gcm, intent);
      }

      // handling of stuff as described on
      // http://developer.android.com/google/gcm/client.html
      try {
         Bundle extras = intent.getExtras();
         // The getMessageType() intent parameter must be the intent you
         // received in your BroadcastReceiver.
         String messageType = gcm.getMessageType(intent);

         if (extras != null && !extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	            sendPhoneActivityNotification("Send error: " + extras.toString(), "Error");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
	            sendPhoneActivityNotification("Deleted messages on server: " + extras.toString(), "Deleted");
               // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
               // Post notification of received message.
               String msg = extras.getString("message");
	           PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(),"fn received", null, 1);
	           Pregnancy preg = pdh.getPregnancy();

	                if (TextUtils.isEmpty(msg)){
	                }else if(msg.contains("_R: ")){
		                final String recommendationMessage = msg.replaceAll("_R: ","");
		                Handler mHandler = new Handler(getMainLooper());
		                mHandler.post(new Runnable() {
			                @Override
			                public void run() {
				                RecommendationDataHandler rdh = new RecommendationDataHandler(getApplicationContext(),"Msg received",null, 1);
				                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
				                Calendar cal = Calendar.getInstance();
				                int pregnancyDay = calculateRecommendationDay(cal.getTime());
				                rdh.addRecommendation(recommendationMessage, pregnancyDay , cal.getTime(), (int)Math.floor(pregnancyDay / 7));
				                sendNotification("Pregnancy Guide: Recommendation received!", "New recommendation");
			                }
		                });
		            }else if(msg.contains("_V: ")){
		                final String visitMessage = msg.replaceAll("_V: ","");
		                Handler mHandler = new Handler(getMainLooper());
		                mHandler.post(new Runnable() {
			                @Override
			                public void run() {
				                VisitDataHandler vdh = new VisitDataHandler(getApplicationContext(),"Msg received",null, 1);
				                vdh.addVisit(visitMessage);
				                sendNotification("Pregnancy Guide: Reminder received!", "New visit reminder");
			                }
		                });
		            }else if(msg.contains("_Verified")){
		                Handler mHandler = new Handler(getMainLooper());
		                mHandler.post(new Runnable() {
			                @Override
			                public void run() {
				                PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(),"Msg received", null, 1);
				                Toast.makeText(getApplicationContext(),getString(R.string.number_verified), Toast.LENGTH_LONG).show();
				                pdh.setVerified(true);
				                pdh.setLoadingProgress(pdh.getPregnancy().get_loadingProgress()+1);
			                }
		                });
	                }else if(msg.contains("_NotVerified")){
		                Handler mHandler = new Handler(getMainLooper());
		                mHandler.post(new Runnable() {
			                @Override
			                public void run() {
				                PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(),"not verified", null, 1);
				                pdh.setVerified(false);
				                Toast.makeText(getApplicationContext(), getString(R.string.number_not_verified), Toast.LENGTH_SHORT).show();
			                }
		                });
	                }else if(msg.contains("_PregnancyNotFound")){
		                Handler mHandler = new Handler(getMainLooper());
		                mHandler.post(new Runnable() {
			                @Override
			                public void run() {
				                PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(),"Msg received", null, 1);
				                pdh.setVerified(false);
				                Toast.makeText(getApplicationContext(),getString(R.string.number_not_found), Toast.LENGTH_LONG).show();
			                }
		                });
	                }else if(msg.contains("_PregnancyInfosFacilityName")){
		                String pInfoFN = msg.replaceAll("_PregnancyInfosFacilityName: ","");
		                pdh.updateFacilityName(pInfoFN);
		                pdh.setLoadingProgress(preg.get_loadingProgress() + 1);
	                }else if(msg.contains("_PregnancyInfosFacilityPhone")){
		                String pInfoFP = msg.replaceAll("_PregnancyInfosFacilityPhone: ","");
		                pdh.updateFacilityPhone(pInfoFP);
		                pdh.setLoadingProgress(preg.get_loadingProgress() + 1);
	                }else if(msg.contains("_PregnancyInfosExpectedDelivery")){
		                String pInfoED = msg.replaceAll("_PregnancyInfosExpectedDelivery: ","");
		                pdh.updateExpectedDelivery(pInfoED);
		                pdh.setLoadingProgress(preg.get_loadingProgress() + 1);
	                }else if(msg.contains("_PregnancyInfosPatientName")){
		                String pInfoPN = msg.replaceAll("_PregnancyInfosPatientName: ","");
		                pdh.updatePatientName(pInfoPN);
		                pdh.setLoadingProgress(preg.get_loadingProgress() + 1);
	                }

               Log.i("PregnancyGuide", "Received: " + extras.toString());
            }
         }
      } finally {
         // Release the wake lock provided by the WakefulBroadcastReceiver.
         GcmBroadcastReceiver.completeWakefulIntent(intent);
      }
   }

   private void unregister(GoogleCloudMessaging gcm, Intent intent) {
      try {
         Log.v("PregnancyGuide", "About to unregister...");
         gcm.unregister();
         Log.v("PregnancyGuide", "Device unregistered.");

         removeRegistrationId();
         Bundle bundle = new Bundle();
         bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.UNREGISTRATION_SUCCEEDED.ordinal());
         EventBus.getDefault().post(bundle);
      } catch (IOException e) {
         Bundle bundle = new Bundle();
         bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.UNREGISTRATION_FAILED.ordinal());
         EventBus.getDefault().post(bundle);
         Log.e("PregnancyGuide", "Unregistration failed", e);
      }
   }

   private void register(GoogleCloudMessaging gcm, Intent intent) {
      try {
         Log.v("PregnancyGuide", "About to register...");
         String regid = gcm.register(mSenderId);
         Log.v("PregnancyGuide", "Device registered: " + regid);

         String account = intent.getStringExtra(Constants.KEY_ACCOUNT);
         sendRegistrationIdToBackend(gcm, regid, account);

         // Persist the regID - no need to register again.
         storeRegistrationId(regid);
         Bundle bundle = new Bundle();
         bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.REGISTRATION_SUCCEEDED.ordinal());
         bundle.putString(Constants.KEY_REG_ID, regid);

         EventBus.getDefault().post(bundle);
      } catch (IOException e) {
         // If there is an error, don't just keep trying to register.
         // Require the user to click a button again, or perform
         // exponential back-off.

         // Simply notify the user:
         Bundle bundle = new Bundle();
         bundle.putInt(Constants.KEY_EVENT_TYPE,
               EventbusMessageType.REGISTRATION_FAILED.ordinal());
         EventBus.getDefault().post(bundle);
         Log.e("PregnancyGuide", "Registration failed.", e);
      }
   }

   private void storeRegistrationId(String regId) {
	  PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(), null, null, 1);
	  pdh.updateRegId(regId);
      final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      Log.i("PregnancyGuide", "Saving regId to prefs: " + regId);
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString(Constants.KEY_REG_ID, regId);
      editor.putInt(Constants.KEY_STATE, State.REGISTERED.ordinal());
      editor.commit();
   }

   private void removeRegistrationId() {
	   //TODO account
	  PregnancyDataHandler pdh = new PregnancyDataHandler(getApplicationContext(), null, null, 1);
	  pdh.updateRegId("null");
      final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      Log.i("PregnancyGuide", "Removing regId from prefs");
      SharedPreferences.Editor editor = prefs.edit();
      editor.remove(Constants.KEY_REG_ID);
      editor.putInt(Constants.KEY_STATE, State.UNREGISTERED.ordinal());
      editor.commit();
   }

   private void sendRegistrationIdToBackend(GoogleCloudMessaging gcm,String regId, String account) {
      try {
         Bundle data = new Bundle();
         // the name is used for keeping track of user notifications
         // if you use the same name everywhere, the notifications will
         // be cancelled
         data.putString("account", account);
         data.putString("action", Constants.ACTION_REGISTER);
         String msgId = Integer.toString(getNextMsgId());
         gcm.send(mSenderId + "@gcm.googleapis.com", msgId, Constants.GCM_DEFAULT_TTL, data);
         Log.v("PregnancyGuide", "regId sent: " + regId);
      } catch (IOException e) {
         Log.e("PregnancyGuide", "IOException while sending registration to backend...", e);
      }
   }

   private void sendMessage(GoogleCloudMessaging gcm, Intent intent) {
      try {
         String msg = intent.getStringExtra(Constants.KEY_MESSAGE_TXT);
         Bundle data = new Bundle();
         data.putString(Constants.ACTION, Constants.ACTION_ECHO);
         data.putString("message", msg);
         String id = Integer.toString(getNextMsgId());
         gcm.send(mSenderId + "@gcm.googleapis.com", id, data);
         Log.v("PregnancyGuide", "Sent message: " + msg);
      } catch (IOException e) {
         Log.e("PregnancyGuide", "Error while sending a message", e);
      }
   }

   // Put the message into a notification and post it.
   // This is just one simple example of what you might choose to do with
   // a GCM message.

	///New: Mueller
	private void sendPhoneActivityNotification(String msg, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, MobileNumberActivity.class);
		notificationIntent.setAction(Constants.NOTIFICATION_ACTION);
		notificationIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_collections_cloud)
				.setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(Constants.NOTIFICATION_NR, mBuilder.build());
	}

	///New: Mueller
	private void sendNotification(String msg, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setAction(Constants.NOTIFICATION_ACTION);
		notificationIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_collections_cloud)
				.setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(Constants.NOTIFICATION_NR, mBuilder.build());
	}

   private int getNextMsgId() {
      SharedPreferences prefs = getPrefs();
      int id = prefs.getInt(Constants.KEY_MSG_ID, 0);
      Editor editor = prefs.edit();
      editor.putInt(Constants.KEY_MSG_ID, ++id);
      editor.commit();
      return id;
   }

   private SharedPreferences getPrefs() {
      return PreferenceManager.getDefaultSharedPreferences(this);
   }

	private int calculateRecommendationDay(Date today) {

		Date truncatedDate1 = null;
		Date truncatedDate2 = null;

		long timeDifference = 0;
		long daysInBetween = 0;

		try {
			PregnancyDataHandler pdh = new PregnancyDataHandler(this, "Rec", null, 1);
			Pregnancy pregnancy = pdh.getPregnancy();

			DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");

			String truncatedDateString1 = formatter.format(today);
			String truncatedDateString2 = pregnancy.get_expectedDelivery().toString();
			truncatedDate1 = formatter.parse(truncatedDateString1);
			truncatedDate2 = formatter.parse(truncatedDateString2);

			timeDifference = truncatedDate2.getTime()- truncatedDate1.getTime();
			daysInBetween = timeDifference / (24*60*60*1000);

		} catch (ParseException e) {
			Log.i("Pregnancy Guide", "error parsing dates" + e.getMessage());
		}


		return ((int) daysInBetween);
	}
}
