package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */

// Do not import com.mysql.jdbc.*!

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MySqlHandler{

	private static Connection conn = null;
	private SecureRandom random = new SecureRandom();

	public static void MySqlHandler(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void connect(){
		try {
			//conn =	DriverManager.getConnection("jdbc:mysql://localhost?" + "user=root&password=");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registry", "root", "");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private static void close(){
		//end
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Notification> getNotificationQueue(int notificationTypeID){
	// notificationTypeID 1: Visit Reminder,  notificationTypeID 2: Recommendations

		Notification notification = new Notification();
		ArrayList<Notification> notificationQueue = new ArrayList<Notification>();

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String lastFullHalfHour = getLastFullHalfHour();
			String statement = "SELECT "
					+ "nq.NotificationQueueID, "
					+ "nq.NotificationText, "
					+ "nar.GCMRegistrationID "
					+ "FROM notificationqueue nq "
					+ "JOIN notificationappregistration nar on nq.MobilePhone = nar.MobilePhone "
					+ "WHERE nq.MobileApp = 1 "
					+ "AND Date(nq.LatestBy) >= curdate() "
					+ "AND nq.NotificationTypeID = " + notificationTypeID + " "
					+ "AND ((nq.DateTimeToSend >= '" + lastFullHalfHour + "' AND  nq.DateTimeToSend <=  ADDTIME('" + lastFullHalfHour + "', '00:30:00')) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 1 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 1 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 2 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 2 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 3 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 3 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 4 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 4 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 5 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 5 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 6 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 6 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 7 DAY) AND nq.DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 7 DAY))) "
					+ "ORDER BY DateTimeToSend ASC;";

			if (stmt.execute(statement)) {
				rs = stmt.getResultSet();
			}

			while(rs.next()){
				//Retrieve by column name
				notification.setGcmRegID(rs.getString("GCMRegistrationID"));
				notification.setNotificationQueueID(rs.getInt("NotificationQueueID"));
				notification.setNotificationText(rs.getString("NotificationText"));
				notificationQueue.add(notification);
			}
		}
		catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore

				stmt = null;
			}
		}

		this.close();
		return notificationQueue;
	}

	public boolean moveNotificationToHistory(int notificationQueueID){
		boolean status = false;

		String statementReplace ="REPLACE INTO notificationqueuehistory "
			+ "SELECT 	NotificationQueueID, "
				+ "MobilePhone, "
				+ "NotificationText, "
				+ "DateTimeToSend, "
				+ "1 as SucessfullySent, "
				+ "NOW() as DateTimeSent, "
				+ "NotificationTypeID, "
				+ "MobileApp "
				+ "FROM notificationqueue "
				+ "WHERE NotificationQueueID = " + notificationQueueID + ";";

		String statementDelete = "DELETE FROM notificationqueue "
				+ "WHERE NotificationQueueID =" + notificationQueueID + ";";

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(statementReplace);
			stmt.execute(statementDelete);
			status = true;
		}
		catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore

				stmt = null;
			}
		}

		this.close();
		return status;
	}

	public boolean prepareNotificationForTheNextDay(int notificationQueueID){
		boolean status = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		String statement = 	"UPDATE notificationqueue SET"
		+ "DateTimeToSend = ADDDATE('" + dateFormat.format(cal.getTime()).toString() + "', INTERVAL 1 DAY) "
		+ "WHERE NotificationQueueID = " + notificationQueueID + ";";

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(statement);
			status = true;
		}
		catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore

				stmt = null;
			}
		}

		this.close();
		return status;
	}

	public boolean saveNewRegID(String gcmRegId){
		boolean status = false;

		String statement = "INSERT INTO notificationappregistration"
		+ " (NotificationAppRegistrationID,"
				+ " GCMRegistrationID,"
				+ " PregnancyID,"
				+ " MobilePhoneVerified,"
				+ " ActivationCode)"
				+ " VALUES"
				+ " (null,"
				+ " " + gcmRegId + ","
				+ " null,"
				+ " 0,"
				+ " '" + this.createActivationCode() + "';";

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(statement);
			status = true;
		}
		catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore

				stmt = null;
			}
		}

		this.close();
		return status;
	}

	public Pregnancy getPregnancyInfo(String mobilePhone){
		Pregnancy pregnancy = new Pregnancy();

		String statement = "SELECT nar.NotificationAppRegistrationID, "
				+ " nar.GCMRegistrationID,"
				+ " nar.PregnancyID,"
				+ " nar.ActivationCode,"
				+ " preg.MobileApp,"
				+ " pat.MobilePhone,"
				+ " pat.GivenName as SurName,"
				+ " pat.FamilyName as LastName,"
				+ " preg.Calc_DeliveryDate,"
				+ " f.FacilityName,"
				+ " f.Phone as FacilityPhoneNumber"
				+ " FROM notificationappregistration nar"
				+ " JOIN pregnancy preg on preg.PregnancyID = nar.PregnancyID"
				+ " JOIN patient pat on pat.PatientID = preg.PatientID"
				+ " JOIN facilities f on preg.FacilityID = f.FacilityID"
				+ " WHERE pat.MobilePhone = '" + mobilePhone + "';";


		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();

			if (stmt.execute(statement)) {
				rs = stmt.getResultSet();
			}

			while(rs.next()){
				//Retrieve by column name
				pregnancy.setPregnancyId(rs.getInt("PregnancyID"));
				pregnancy.setNotificationAppRegistrationId(rs.getInt("NotificationAppRegistrationID"));
				pregnancy.setgcmRegistrationId(rs.getString("GCMRegistrationID"));
				pregnancy.setMobileApp(rs.getInt("MobileApp"));
				pregnancy.setActivationCode(rs.getString("ActivationCode"));
				pregnancy.setMobilePhone(rs.getString("MobilePhone"));
				pregnancy.setPatientSurName(rs.getString("SurName"));
				pregnancy.setPatientLastName(rs.getString("LastName"));
				pregnancy.setExpectedDelivery(rs.getString("Calc_DeliveryDate"));
				pregnancy.setFacilityName(rs.getString("FacilityName"));
				pregnancy.setFacilityPhoneNumber(rs.getString("FacilityPhoneNumber"));
			}
		}
		catch (SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore

				stmt = null;
			}
		}

		this.close();
		return pregnancy;
	}

	private static String getLastFullHalfHour() {

		String lastFullHalfHour;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		String currentDate =  dateFormat.format(cal.getTime()).toString();
		Integer hour = cal.get(Calendar.HOUR_OF_DAY);
		Integer minute = cal.get(Calendar.MINUTE);

		if (minute < 30) {
			lastFullHalfHour = currentDate + " " + hour.toString() + ":00:00";
		} else {
			lastFullHalfHour = currentDate + " " + hour.toString() + ":30:00";
			if (++hour > 23) {
				// past midnight set the hour to 00
				cal.add(Calendar.DAY_OF_MONTH, 1);
				lastFullHalfHour = dateFormat.format(cal.getTime()).toString() + " 00:30:00";
			}
		}

		return lastFullHalfHour;
	}

	private String createActivationCode() {
		return new BigInteger(130, random).toString(32);
	}
}