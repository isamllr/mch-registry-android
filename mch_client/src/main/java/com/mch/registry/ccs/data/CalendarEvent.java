package com.mch.registry.ccs.data;

import android.content.ContentValues;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Isa on 05.08.2014.
 */
public class CalendarEvent {
	private String title;
	private String descr;
	private String location;
	private long startTime;
	private long endTime;
	private String idCalendar;

	public CalendarEvent(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getIdCalendar() {
		return idCalendar;
	}

	public void setIdCalendar(String idCalendar) {
		this.idCalendar = idCalendar;
	}

	public static ContentValues toContentValues(CalendarEvent evt) {
		ContentValues cv = new ContentValues();
		cv.put("calendar_id", evt.getIdCalendar());
		cv.put("title", evt.getTitle());
		cv.put("description", evt.getDescr());
		cv.put("eventLocation", evt.getLocation());
		cv.put("dtstart", evt.getStartTime());
		cv.put("dtend", evt.getEndTime());
		cv.put("eventStatus", 1);
		cv.put("visibility", 0);
		cv.put("transparency", 0);

		return cv;

	}

	public static ContentValues toICSContentValues(CalendarEvent evt) {

		ContentValues cv = new ContentValues();
		cv.put(CalendarContract.Events.CALENDAR_ID, evt.getIdCalendar());
		cv.put(CalendarContract.Events.TITLE, evt.getTitle());
		cv.put(CalendarContract.Events.DESCRIPTION, evt.getDescr());
		cv.put(CalendarContract.Events.EVENT_LOCATION, evt.getLocation());
		cv.put(CalendarContract.Events.DTSTART, evt.getStartTime());
		cv.put(CalendarContract.Events.DTEND, evt.getEndTime());

		Calendar cal = Calendar.getInstance();
		TimeZone tz = cal.getTimeZone();

		cv.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getDisplayName());

		return cv;
	}

}