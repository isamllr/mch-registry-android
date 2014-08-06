package com.mch.registry.ccs.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Note {

	private int _id;
	private String _noteText;
	private int _noteDay;
	private String _createdDate;

	public Note() {
	}

	public Note(int _noteDay, String _noteTex) {
		this._noteText = _noteText;
		this._noteDay = _noteDay;
	}

	public String toString(){
		return this._noteText;
	}

	public Note(int _id, String _noteText, String _createdDate, int _noteDay) {
		this._id = _id;
		this._noteText = _noteText;
		this._createdDate = _createdDate;
		this._noteDay = _noteDay;
	}

	public Note(String _noteText, String _createdDate, int _noteDay) {
		this._noteText = _noteText;
		this._createdDate = _createdDate;
		this._noteDay = _noteDay;
	}

	public void setID(int id) {
		this._id = id;
	}

	public int getID() {
		return this._id;
	}

	public String get_noteText() {
		return _noteText;
	}

	public void set_noteText(String _noteText) {
		this._noteText = _noteText;
	}

	public int get_noteDay() {
		return _noteDay;
	}

	public void set_noteDay(int _noteDay) {
		this._noteDay = _noteDay;
	}

	public String get_createdDate() {
		return _createdDate;
	}

	public void set_createdDate(String _createdDate) {
		this._createdDate = _createdDate;
	}
}

