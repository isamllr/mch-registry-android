package com.mch.registry.ccs.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Visit {
    private int _id;
    private String _visitText;
    private String _receivedDate;
    private String _visitDate;

    public Visit() {
    }

    public Visit(int _id, String _visitText, String _receivedDate, String _visitDate) {
        this._visitText = _visitText;
        this._receivedDate = _receivedDate;
        this._id = _id;
        this._visitDate = _visitDate;
    }

    public Visit(String _visitText, String _receivedDate, String _visitDate) {
        this._visitText = _visitText;
        this._receivedDate = _receivedDate;
        this._visitDate = _visitDate;
    }

    public String get_receivedDate() {
        return _receivedDate;
    }

    public void set_receivedDate(String _receivedDate) {
        this._receivedDate = _receivedDate;
    }

    public String get_visitText() {
        return _visitText;
    }

    public void set_visitText(String _visitText) {
        this._visitText = _visitText;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

	public String get_visitDate() {
		return _visitDate;
	}

	public void set_visitDate(String _visitDate) {
		this._visitDate = _visitDate;
	}
}
