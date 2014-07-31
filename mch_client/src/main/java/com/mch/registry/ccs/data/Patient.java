package com.mch.registry.ccs.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Patient {
    private int _id;
    private int _patientID;
    private String _regID;
    private String _mobileNumber;
    private String _patientName;
    private String _expectedDelivery;
    private String _facilityName;
    private String _facilityPhoneNumber;
    private int _latestMessageID;
	private int _isVerified;

    public Patient() {
    }

	public Patient(int _patientID, String _regID, String _mobileNumber, String _patientName, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, int _latestMessageID, int _isVerified) {
		this._patientID = _patientID;
		this._regID = _regID;
		this._mobileNumber = _mobileNumber;
		this._patientName = _patientName;
		this._expectedDelivery = _expectedDelivery;
		this._facilityName = _facilityName;
		this._facilityPhoneNumber = _facilityPhoneNumber;
		this._latestMessageID = _latestMessageID;
		this._isVerified = _isVerified;
	}

	public Patient(int _id, int _patientID, String _mobileNumber, String _regID, String _patientName, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, int _latestMessageID, int _isVerified) {
		this._id = _id;
		this._patientID = _patientID;
		this._mobileNumber = _mobileNumber;
		this._regID = _regID;
		this._patientName = _patientName;
		this._expectedDelivery = _expectedDelivery;
		this._facilityName = _facilityName;
		this._facilityPhoneNumber = _facilityPhoneNumber;
		this._latestMessageID = _latestMessageID;
		this._isVerified = _isVerified;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_patientID() {
		return _patientID;
	}

	public void set_patientID(int _patientID) {
		this._patientID = _patientID;
	}

	public String get_regID() {
		return _regID;
	}

	public void set_regID(String _regID) {
		this._regID = _regID;
	}

	public String get_mobileNumber() {
		return _mobileNumber;
	}

	public void set_mobileNumber(String _mobileNumber) {
		this._mobileNumber = _mobileNumber;
	}

	public String get_patientName() {
		return _patientName;
	}

	public void set_patientName(String _patientName) {
		this._patientName = _patientName;
	}

	public String get_expectedDelivery() {
		return _expectedDelivery;
	}

	public void set_expectedDelivery(String _expectedDelivery) {
		this._expectedDelivery = _expectedDelivery;
	}

	public String get_facilityName() {
		return _facilityName;
	}

	public void set_facilityName(String _facilityName) {
		this._facilityName = _facilityName;
	}

	public String get_facilityPhoneNumber() {
		return _facilityPhoneNumber;
	}

	public void set_facilityPhoneNumber(String _facilityPhoneNumber) {
		this._facilityPhoneNumber = _facilityPhoneNumber;
	}

	public int get_latestMessageID() {
		return _latestMessageID;
	}

	public void set_latestMessageID(int _latestMessageID) {
		this._latestMessageID = _latestMessageID;
	}

	public int get_isVerified() {
		return _isVerified;
	}

	public void set_isVerified(int _isVerified) {
		this._isVerified = _isVerified;
	}
}

