package com.mch.registry.ccs.data.entities;

/**
 * Created by Isa on 18.07.2014.
 */
public class Recommendation {

	private int _id;
	private String _recommendationText;
	private int _recommendationDay;
	private String _receivedDate;
	private int _pregnancyWeek;

	public Recommendation() {
	}

	public Recommendation(int _id, String _recommendationText, int _recommendationDay, String _receivedDate, int _pregnancyWeek) {
		this._id = _id;
		this._recommendationText = _recommendationText;
		this._recommendationDay = _recommendationDay;
		this._receivedDate = _receivedDate;
		this._pregnancyWeek = _pregnancyWeek;
	}

	public Recommendation(String _recommendationText, int _recommendationDay, String _receivedDate, int _pregnancyWeek) {
		this._recommendationText = _recommendationText;
		this._recommendationDay = _recommendationDay;
		this._receivedDate = _receivedDate;
		this._pregnancyWeek = _pregnancyWeek;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_recommendationText() {
		return _recommendationText;
	}

	public void set_recommendationText(String _recommendationText) {
		this._recommendationText = _recommendationText;
	}

	public int get_recommendationDay() {
		return _recommendationDay;
	}

	public void set_recommendationDay(int _recommendationDay) {
		this._recommendationDay = _recommendationDay;
	}

	public String get_receivedDate() {
		return _receivedDate;
	}

	public void set_receivedDate(String _receivedDate) {
		this._receivedDate = _receivedDate;
	}

	public int get_pregnancyWeek() {
		return _pregnancyWeek;
	}

	public void set_pregnancyWeek(int _pregnancyWeek) {
		this._pregnancyWeek = _pregnancyWeek;
	}

	public String toString(){
		return this.get_recommendationText();
	}
}