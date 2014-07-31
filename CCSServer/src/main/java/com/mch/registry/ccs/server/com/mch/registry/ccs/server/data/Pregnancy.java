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

	public Pregnancy(){}

	public int getNotificationAppRegistrationID() {
		return notificationAppRegistrationID;
	}

	public void setNotificationAppRegistrationID(int notificationAppRegistrationID) {
		this.notificationAppRegistrationID = notificationAppRegistrationID;
	}

	public String getFacilityPhoneNumber() {
		return facilityPhoneNumber;
	}

	public void setFacilityPhoneNumber(String facilityPhoneNumber) {
		this.facilityPhoneNumber = facilityPhoneNumber;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(String expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public int getMobileApp() {
		return mobileApp;
	}

	public void setMobileApp(int mobileApp) {
		this.mobileApp = mobileApp;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public int getPregnancyID() {
		return pregnancyID;
	}

	public void setPregnancyID(int pregnancyID) {
		this.pregnancyID = pregnancyID;
	}

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public String getPatientSurName() {
		return patientSurName;
	}

	public void setPatientSurName(String patientSurName) {
		this.patientSurName = patientSurName;
	}
}
