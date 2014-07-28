package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */
public class Pregnancy {
	int NotificationAppRegistrationID;
	String GCMRegistrationID;
	int PregnancyID;
	String MobilePhoneNumber;
	String Account;
	String Password;

	public Pregnancy(int notificationAppRegistrationID, String GCMRegistrationID, int pregnancyID, String mobilePhoneNumber, String account, String password) {
		NotificationAppRegistrationID = notificationAppRegistrationID;
		this.GCMRegistrationID = GCMRegistrationID;
		PregnancyID = pregnancyID;
		MobilePhoneNumber = mobilePhoneNumber;
		Account = account;
		Password = password;
	}

	public Pregnancy(){
	}

	public int getNotificationAppRegistrationID() {
		return NotificationAppRegistrationID;
	}

	public void setNotificationAppRegistrationID(int notificationAppRegistrationID) {
		NotificationAppRegistrationID = notificationAppRegistrationID;
	}

	public String getGCMRegistrationID() {
		return GCMRegistrationID;
	}

	public void setGCMRegistrationID(String GCMRegistrationID) {
		this.GCMRegistrationID = GCMRegistrationID;
	}

	public int getPregnancyID() {
		return PregnancyID;
	}

	public void setPregnancyID(int pregnancyID) {
		PregnancyID = pregnancyID;
	}

	public String getMobilePhoneNumber() {
		return MobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		MobilePhoneNumber = mobilePhoneNumber;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
}
