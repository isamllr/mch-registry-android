package com.mch.registry.ccs.server.com.mch.registry.ccs.server.data;

/**
 * Created by Isa on 27.07.2014.
 */

import com.mch.registry.ccs.server.Config;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySqlHandler{

	public static final Logger logger = Logger.getLogger(MySqlHandler.class.getName());

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
			Config config = new Config();
			conn = DriverManager.getConnection("jdbc:mysql://" + config.getDatabaseurl() + ":" + config.getDbport() + "/" + config.getDbname(), config.getDbuser(), config.getDbpassword());

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


	// notificationTypeID 1: Visit Reminder,  notificationTypeID 2: Recommendations
	public ArrayList<Notification> getNotificationQueue(int notificationTypeID){

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
					+ "JOIN patient pat on nq.MobilePhone = pat.MobilePhone "
					+ "JOIN pregnancy preg on preg.PatientID = pat.PatientID "
					+ "JOIN notificationappregistration nar on preg.PregnancyID = nar.PregnancyID "
					+ "WHERE preg.MobileApp = 1 "
					+ "AND Date(nq.LatestBy) >= curdate() "
					+ "AND nq.NotificationTypeID = " + notificationTypeID + " "
					+ "AND ((nq.DateTimeToSend >= '" + lastFullHalfHour + "' AND  nq.DateTimeToSend <  ADDTIME('" + lastFullHalfHour + "', '00:30:00')) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 1 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 1 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 2 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 2 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 3 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 3 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 4 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 4 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 5 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 5 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 6 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 6 DAY)) "
					+ "OR (nq.DateTimeToSend >= DATE_SUB('" + lastFullHalfHour + "', INTERVAL 7 DAY) AND nq.DateTimeToSend <  DATE_SUB(ADDTIME('" + lastFullHalfHour + "', '00:30:00'), INTERVAL 7 DAY))) "
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
				} catch (SQLException sqlEx) {
					logger.log(Level.SEVERE, "getq" + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					logger.log(Level.SEVERE, "getq" + sqlEx.getMessage());
				}
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
			logger.log(Level.INFO, "moved notificationid " + notificationQueueID + " to history");
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
				} catch (SQLException sqlEx) {
					logger.log(Level.SEVERE, "movetohistory1" + sqlEx.getMessage());
				}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					logger.log(Level.SEVERE, "movetohistory2" + sqlEx.getMessage());
				}

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
			logger.log(Level.INFO, "not prepared for next day");
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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, "prepnextday" + sqlEx.getMessage()); }

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, "prepnextday" + sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();
		return status;
	}

	public boolean findRegID(String regID){
		boolean regIDFound = false;

		String statement = "SELECT GCMRegistrationID"
		+ " FROM notificationappregistration"
		+ " WHERE GCMRegistrationID = '" + regID + "';";

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();

			if (stmt.execute(statement)) {
				rs = stmt.getResultSet();
			}

			if(rs.next()){
				regIDFound = true;
				logger.log(Level.INFO, "regid found");
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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, "findregid" + sqlEx.getMessage()); }

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();
		return regIDFound;

	}

	public boolean findPregnancyIdInNarTable(int pregnancyId){
		boolean pregnancyIdFound = false;

		Pregnancy pregnancy = new Pregnancy();

		String statement = "SELECT nar.NotificationAppRegistrationID, "
				+ " FROM notificationappregistration nar"
				+ " WHERE nar.PregnancyID = " + pregnancyId + ";";


		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();

			if (stmt.execute(statement)) {
				rs = stmt.getResultSet();
			}

			if(rs.next()){
				pregnancyIdFound = true;
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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, "pregnar" + sqlEx.getMessage());}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, "pregnar" + sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();

		logger.log(Level.INFO, "pregnancy in NAR table found.");
		return pregnancyIdFound;

	}

	public boolean updateAllPregnancyInfos(String mobilePhone, String gcmRegID){
		boolean statusPregnancyFound = false;
		int pregnancyID = 0;

		String statement = "SELECT preg.PregnancyID"
				+ " FROM pregnancy preg"
				+ " JOIN patient pat on pat.PatientID = preg.PatientID"
				+ " JOIN facilities f on preg.FacilityID = f.FacilityID"
				+ " LEFT JOIN delivery d ON preg.PregnancyID = d.PregnancyID"
				+ " WHERE d.DeliveryID IS NULL"
				+ " AND DATEDIFF(DATE_ADD(preg.Calc_DeliveryDate, INTERVAL 31 DAY), curdate()) >= 0"
				+ " AND pat.discharged = 0"
				+ " AND pat.MobilePhone = '" + mobilePhone + "';";

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
				pregnancyID = rs.getInt("PregnancyID");
				logger.log(Level.INFO, "get PregnancyInfo by Mobile Phone. PregID: " + pregnancyID);
				if (pregnancyID>0){statusPregnancyFound = true;}
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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, "preginfos" + sqlEx.getMessage());}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, "preginfos2" + sqlEx.getMessage()); }

				stmt = null;
			}
		}

		String activationCode = this.createActivationCode();
		String statement2 = "UPDATE notificationappregistration"
				+ " SET "
				+ " PregnancyID = " + pregnancyID + ", "
				+ " ActivationCode = '" + activationCode + "'"
				+ " WHERE GCMRegistrationID = '" + gcmRegID + "';";

		stmt = null;
		rs = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(statement2);
			logger.log(Level.INFO, "pregnancyinfos updated in NAR table for found pregid" + pregnancyID);

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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

				stmt = null;
			}
		}

		this.close();

		//Recmove old RegIds
		String statement3 = "DELETE FROM notificationappregistration "
				+ " WHERE ActivationCode <> '" + activationCode + "'"
				+ " AND PregnancyID = " + pregnancyID +";";

		this.connect();

		try {
			stmt = conn.createStatement();
			stmt.execute(statement3);
			logger.log(Level.INFO, "deleted old regIDs");
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
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();

		return statusPregnancyFound;
	}

	public boolean saveNewRegID(String gcmRegId){
		boolean status = false;

		String statement = "INSERT INTO notificationappregistration"
		+ " (NotificationAppRegistrationID,"
				+ " GCMRegistrationID,"
				+ " PregnancyID,"
				+ " ActivationCode)"
				+ " VALUES"
				+ " (null,"
				+ " '" + gcmRegId  + "',"
				+ " 0,"
				+ " '');";

		this.connect();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(statement);
			status = true;
			logger.log(Level.INFO, "New regId saved.");

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
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();
		return status;
	}

	public Pregnancy getPregnancyInfoByGcmRegId(String gcmRegId){
		Pregnancy pregnancy = new Pregnancy();

		String statement = "SELECT nar.NotificationAppRegistrationID,"
				+ " nar.GCMRegistrationID,"
				+ " preg.PregnancyID,"
				+ " nar.ActivationCode,"
				+ " preg.MobileApp,"
				+ " pat.MobilePhone,"
				+ " pat.GivenName as SurName,"
				+ " pat.FamilyName as LastName,"
				+ " preg.Calc_DeliveryDate,"
				+ " f.FacilityName,"
				+ " f.Phone as FacilityPhoneNumber"
				+ " FROM pregnancy preg"
				+ " LEFT JOIN notificationappregistration nar on preg.PregnancyID = nar.PregnancyID"
				+ " JOIN patient pat on pat.PatientID = preg.PatientID"
				+ " JOIN facilities f on preg.FacilityID = f.FacilityID"
				+ " LEFT JOIN delivery d ON preg.PregnancyID = d.PregnancyID"
				+ " WHERE d.DeliveryID IS NULL"
				+ " AND DATEDIFF(DATE_ADD(preg.Calc_DeliveryDate, INTERVAL 31 DAY), curdate()) >= 0"
				+ " AND pat.discharged = 0"
				+ " AND nar.GCMRegistrationID = '" + gcmRegId + "';";


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
				pregnancy.setPregnancyID(rs.getInt("PregnancyID"));
				pregnancy.setNotificationAppRegistrationID(rs.getInt("NotificationAppRegistrationID"));
				pregnancy.setGcmRegistrationId(rs.getString("GCMRegistrationID"));
				pregnancy.setMobileApp(rs.getInt("MobileApp"));
				pregnancy.setActivationCode(rs.getString("ActivationCode"));
				pregnancy.setMobilePhone(rs.getString("MobilePhone"));
				pregnancy.setPatientSurName(rs.getString("SurName"));
				pregnancy.setPatientLastName(rs.getString("LastName"));
				pregnancy.setExpectedDelivery(rs.getString("Calc_DeliveryDate"));
				pregnancy.setFacilityName(rs.getString("FacilityName"));
				pregnancy.setFacilityPhoneNumber(rs.getString("FacilityPhoneNumber"));
				logger.log(Level.INFO, "get PregnancyInfo by gcmregid.");
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
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				stmt = null;
			}
		}

		this.close();

		return pregnancy;
	}

	public Pregnancy getPregnancyInfoByMobilePhone(String mobilePhone){
		Pregnancy pregnancy = new Pregnancy();

		String statement = "SELECT nar.NotificationAppRegistrationID,"
				+ " nar.GCMRegistrationID,"
				+ " preg.PregnancyID,"
				+ " nar.ActivationCode,"
				+ " preg.MobileApp,"
				+ " pat.MobilePhone,"
				+ " pat.GivenName as SurName,"
				+ " pat.FamilyName as LastName,"
				+ " preg.Calc_DeliveryDate,"
				+ " f.FacilityName,"
				+ " f.Phone as FacilityPhoneNumber"
				+ " FROM pregnancy preg"
				+ " LEFT JOIN notificationappregistration nar on preg.PregnancyID = nar.PregnancyID"
				+ " JOIN patient pat on pat.PatientID = preg.PatientID"
				+ " JOIN facilities f on preg.FacilityID = f.FacilityID"
				+ " LEFT JOIN delivery d ON preg.PregnancyID = d.PregnancyID"
				+ " WHERE d.DeliveryID IS NULL"
				+ " AND DATEDIFF(DATE_ADD(preg.Calc_DeliveryDate, INTERVAL 31 DAY), curdate()) >= 0"
				+ " AND pat.discharged = 0"
				+ " AND pat.MobilePhone = '" + mobilePhone + "';";

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
				pregnancy.setPregnancyID(rs.getInt("PregnancyID"));
				pregnancy.setNotificationAppRegistrationID(rs.getInt("NotificationAppRegistrationID"));
				pregnancy.setGcmRegistrationId(rs.getString("GCMRegistrationID"));
				pregnancy.setMobileApp(rs.getInt("MobileApp"));
				pregnancy.setActivationCode(rs.getString("ActivationCode"));
				pregnancy.setMobilePhone(rs.getString("MobilePhone"));
				pregnancy.setPatientSurName(rs.getString("SurName"));
				pregnancy.setPatientLastName(rs.getString("LastName"));
				pregnancy.setExpectedDelivery(rs.getString("Calc_DeliveryDate"));
				pregnancy.setFacilityName(rs.getString("FacilityName"));
				pregnancy.setFacilityPhoneNumber(rs.getString("FacilityPhoneNumber"));
				logger.log(Level.INFO, "get PregnancyInfo by Mobile Phone.");
				logger.log(Level.INFO, "The activation code is: " + pregnancy.getActivationCode());
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
				} catch (SQLException sqlEx) {logger.log(Level.SEVERE, sqlEx.getMessage()); }

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

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

		logger.log(Level.INFO, "lastFullHalfHour: " + lastFullHalfHour);
		return lastFullHalfHour;
	}

	private String createActivationCode() {
		String activationCode = (new BigInteger(130, random).toString(32));
		logger.log(Level.INFO, "Verification code created. " +  activationCode);
		return activationCode;
	}

	public boolean setVerified(String gcmRegId, boolean verificationAccepted) {
		Pregnancy pregnancy = new Pregnancy();
		pregnancy = getPregnancyInfoByGcmRegId(gcmRegId);
		boolean status = false;

		if (pregnancy!=null){
			int accepted = ( verificationAccepted ? 1 : 0 );

			String statement = "UPDATE pregnancy"
					+ " SET MobileApp = " + accepted
					+ " WHERE PregnancyID =" + pregnancy.getPregnancyID() + ";";

			this.connect();
			Statement stmt = null;
			ResultSet rs = null;

			try {
				stmt = conn.createStatement();
				stmt.execute(statement);
				status = true;
				logger.log(Level.INFO, "Verification " +  accepted + " for PregnancyID" + pregnancy.getPregnancyID());
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
					} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) { logger.log(Level.SEVERE, sqlEx.getMessage());}

					stmt = null;
				}
			}

			this.close();
		}
		return status;
	}
}