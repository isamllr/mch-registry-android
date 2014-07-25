package app.registry.mch.mchpregnancyguide.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Patient {
    private int _id;
    private int _patientID;
    private String _regID;
    private String _projectID;
    private String _mobileNumber;
    private String _patientName;
    private String _expectedDelivery;
    private String _facilityName;
    private String _facilityPhoneNumber;
    private String _latestMessageID;

    public Patient() {
    }

    public Patient(int _patientID, String _regID, String _projectID, String _patientName, String _mobileNumber, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, String _latestMessageID) {
        this._patientID = _patientID;
        this._regID = _regID;
        this._projectID = _projectID;
        this._patientName = _patientName;
        this._mobileNumber = _mobileNumber;
        this._expectedDelivery = _expectedDelivery;
        this._facilityName = _facilityName;
        this._facilityPhoneNumber = _facilityPhoneNumber;
        this._latestMessageID = _latestMessageID;
    }

    public Patient(int _id, int _patientID, String _regID, String _mobileNumber, String _projectID, String _patientName, String _expectedDelivery, String _facilityName, String _facilityPhoneNumber, String _latestMessageID) {
        this._id = _id;
        this._patientID = _patientID;
        this._regID = _regID;
        this._mobileNumber = _mobileNumber;
        this._projectID = _projectID;
        this._patientName = _patientName;
        this._expectedDelivery = _expectedDelivery;
        this._facilityName = _facilityName;
        this._facilityPhoneNumber = _facilityPhoneNumber;
        this._latestMessageID = _latestMessageID;
    }

    public String get_projectID() {
        return _projectID;
    }

    public void set_projectID(String _projectID) {
        this._projectID = _projectID;
    }

    public String get_latestMessageID() {
        return _latestMessageID;
    }

    public void set_latestMessageID(String _latestMessageID) {
        this._latestMessageID = _latestMessageID;
    }

    public String get_regID() {
        return _regID;
    }

    public void set_regID(String _regID) {
        this._regID = _regID;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public int get_patientID() {
        return _patientID;
    }

    public void set_patientID(int _patientID) {
        this._patientID = _patientID;
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
}

