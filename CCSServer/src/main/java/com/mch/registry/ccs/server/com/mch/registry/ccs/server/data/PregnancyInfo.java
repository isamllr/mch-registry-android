package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */
public class PregnancyInfo {

	int pregnancyID;
	int mobilePhoneNumber;
	String gcmRegistrationID;
	String patientSurName;
	String patientLastName;
	String expectedDelivery;
	String facilityName;
	String facilityPhoneNumber;

	public PregnancyInfo() {
	}

	public PregnancyInfo(int pregnancyID, int mobilePhoneNumber, String facilityPhoneNumber, String facilityName, String expectedDelivery, String patientLastName, String gcmRegistrationID, String patientSurName) {
		this.pregnancyID = pregnancyID;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.facilityPhoneNumber = facilityPhoneNumber;
		this.facilityName = facilityName;
		this.expectedDelivery = expectedDelivery;
		this.patientLastName = patientLastName;
		this.gcmRegistrationID = gcmRegistrationID;
		this.patientSurName = patientSurName;
	}

	public int getPregnancyID() {
		return pregnancyID;
	}

	public void setPregnancyID(int pregnancyID) {
		this.pregnancyID = pregnancyID;
	}

	public int getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(int mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public String getGcmRegistrationID() {
		return gcmRegistrationID;
	}

	public void setGcmRegistrationID(String gcmRegistrationID) {
		this.gcmRegistrationID = gcmRegistrationID;
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
}
