package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */

// Do not import com.mysql.jdbc.*!
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
			String statement = "SELECT NotificationQueueID, "
					+ "MobilePhone, "
					+ "NotificationText, "
					+ "DateTimeToSend, "
					+ "LatestBy "
					+ "FROM notificationqueue WHERE "
					+ "MobileApp = 1 "
					+ "AND Date(LatestBY) >= curdate()"
					+ "AND NotificationTypeID = " + notificationTypeID + " "
					+ "AND ((DateTimeToSend >= '" + lastFullHalfHour + "' AND  DateTimeToSend <=  ADDTIME('" + lastFullHalfHour + "', '00:30:00')) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 1 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 1 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 2 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 2 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 3 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 3 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 4 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 4 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 5 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 5 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 6 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 6 DAY)) "
					+ "OR (DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 7 DAY) AND DateTimeToSend <=  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 7 DAY))) "
					+ "ORDER BY DateTimeToSend ASC;";

			if (stmt.execute(statement)) {
				rs = stmt.getResultSet();
			}

			while(rs.next()){
				//Retrieve by column name
				notification.setNotificationQueueID(rs.getInt("NotificationQueueID"));
				notification.setMobilePhone(rs.getString("MobilePhone"));
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

	private boolean moveNotificationToHistory(int notificationQueueID){
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

	private boolean prepareNotificationForTheNextDay(int notificationQueueID){
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


}