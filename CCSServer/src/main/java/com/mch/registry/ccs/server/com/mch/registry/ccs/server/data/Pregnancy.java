package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */
public class Pregnancy {
	int notificationAppRegistrationID;
	String gcmRegistrationId;
	int pregnancyID;
	String activationCode;
	int mobileApp;
	String mobilePhone;
	String patientSurName;
	String patientLastName;
	String expectedDelivery;
	String facilityName;
	String facilityPhoneNumber;

	public Pregnancy(int notificationAppRegistrationID, String facilityPhoneNumber, String facilityName, String expectedDelivery, String patientLastName, String patientSurName, String mobilePhone, int mobileApp, String activationCode, int pregnancyID, String gcmRegistrationId) {
		this.notificationAppRegistrationID = notificationAppRegistrationID;
		this.facilityPhoneNumber = facilityPhoneNumber;
		this.facilityName = facilityName;
		this.expectedDelivery = expectedDelivery;
		this.patientLastName = patientLastName;
		this.patientSurName = patientSurName;
		this.mobilePhone = mobilePhone;
		this.mobileApp = mobileApp;
		this.activationCode = activationCode;
		this.pregnancyID = pregnancyID;
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public Pregnancy(){
	}

	public int getNotificationAppRegistrationID() {
		return notificationAppRegistrationID;
	}

	public void setNotificationAppRegistrationID(int notificationAppRegistrationID) {
		this.notificationAppRegistrationID = notificationAppRegistrationID;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public int getPregnancyID() {
		return pregnancyID;
	}

	public void setPregnancyID(int pregnancyID) {
		this.pregnancyID = pregnancyID;
	}

	public String getPatientSurName() {
		return patientSurName;
	}

	public void setPatientSurName(String patientSurName) {
		this.patientSurName = patientSurName;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(String expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getFacilityPhoneNumber() {
		return facilityPhoneNumber;
	}

	public void setFacilityPhoneNumber(String facilityPhoneNumber) {
		this.facilityPhoneNumber = facilityPhoneNumber;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		activationCode = activationCode;
	}

	public int getMobileApp() {
		return mobileApp;
	}

	public void setMobileApp(int mobileApp) {
		mobileApp = mobileApp;
	}

	public int getNotificationAppRegistrationId() {
		return notificationAppRegistrationID;
	}

	public void setNotificationAppRegistrationId(int notificationAppRegistrationID) {
		notificationAppRegistrationID = notificationAppRegistrationID;
	}

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setgcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public int getPregnancyId() {
		return pregnancyID;
	}

	public void setPregnancyId(int pregnancyID) {
		pregnancyID = pregnancyID;
	}

}
