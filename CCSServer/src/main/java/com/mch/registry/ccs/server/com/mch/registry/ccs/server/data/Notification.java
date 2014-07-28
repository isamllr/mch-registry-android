package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */
public class Notification {

	private int notificationQueueID;
	private String notificationText;
	private String gcmRegID;


	public Notification(){
	}

	public Notification(String gcmRegID, String notificationText, int notificationQueueID) {
		this.gcmRegID = gcmRegID;
		this.notificationText = notificationText;
		this.notificationQueueID = notificationQueueID;
	}

	public String getGcmRegID() {
		return gcmRegID;
	}

	public void setGcmRegID(String gcmRegID) {
		this.gcmRegID = gcmRegID;
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
