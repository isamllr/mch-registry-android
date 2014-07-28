package app.registry.mch.mchpregnancyguide.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Patient {
    private int _id;
	private String _regID;
    private int _pregnancyID;
    private String _mobileNumber;
    private String _patientSurName;
	private String _patientLastName;
    private String _expectedDelivery;
    private String _facilityName;
    private String _facilityPhoneNumber;
    private String _latestMessageID;

    public Patient() {
    }

	public Patient(int _pregnancyID, String _regID, String _mobileNumber, String _patientSurName, String _patientLastName, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, String _latestMessageID) {
		this._pregnancyID = _pregnancyID;
		this._regID = _regID;
		this._mobileNumber = _mobileNumber;
		this._patientSurName = _patientSurName;
		this._patientLastName = _patientLastName;
		this._expectedDelivery = _expectedDelivery;
		this._facilityName = _facilityName;
		this._facilityPhoneNumber = _facilityPhoneNumber;
		this._latestMessageID = _latestMessageID;
	}

	public Patient(String _regID, int _pregnancyID, String _mobileNumber, String _patientSurName, String _patientLastName, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, String _latestMessageID) {
		this._regID = _regID;
		this._pregnancyID = _pregnancyID;
		this._mobileNumber = _mobileNumber;
		this._patientSurName = _patientSurName;
		this._patientLastName = _patientLastName;
		this._expectedDelivery = _expectedDelivery;
		this._facilityName = _facilityName;
		this._facilityPhoneNumber = _facilityPhoneNumber;
		this._latestMessageID = _latestMessageID;
	}

	public String get_regID() {
		return _regID;
	}

	public void set_regID(String _regID) {
		this._regID = _regID;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_mobileNumber() {
		return _mobileNumber;
	}

	public void set_mobileNumber(String _mobileNumber) {
		this._mobileNumber = _mobileNumber;
	}

	public int get_pregnancyID() {
		return _pregnancyID;
	}

	public void set_pregnancyID(int _pregnancyID) {
		this._pregnancyID = _pregnancyID;
	}

	public String get_patientSurName() {
		return _patientSurName;
	}

	public void set_patientSurName(String _patientSurName) {
		this._patientSurName = _patientSurName;
	}

	public String get_patientLastName() {
		return _patientLastName;
	}

	public void set_patientLastName(String _patientLastName) {
		this._patientLastName = _patientLastName;
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

	public String get_latestMessageID() {
		return _latestMessageID;
	}

	public void set_latestMessageID(String _latestMessageID) {
		this._latestMessageID = _latestMessageID;
	}
}

