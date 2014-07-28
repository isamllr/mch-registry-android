package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */
public class Notification {

	private int notificationQueueID;
	private String notificationText;
	private String mobilePhone;

	public void NotificationQueue(){
	}

	public void NotificationQueue(int notificationQueueID, String notificationText, String mobilePhone) {
		this.notificationQueueID = notificationQueueID;
		this.notificationText = notificationText;
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public int getNotificationQueueID() {
		return notificationQueueID;
	}

	public void setNotificationQueueID(int notificationQueueID) {
		this.notificationQueueID = notificationQueueID;
	}

}
