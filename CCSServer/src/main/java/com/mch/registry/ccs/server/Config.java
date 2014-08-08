package com.mch.registry.ccs.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Isa on 05.08.2014.
 */
public class Config {

	String databaseurl;
	String dbuser;
	String dbpassword;
	String senderphone;
	String dbport;
	String dbname;

	String clickatellUsername;
	String clickatellPassword;
	String clickatellAPIID;

	String projectId;
	String key;

	public String getProjectId() {
		return projectId;
	}

	public String getKey() {
		return key;
	}

	public String getClickatellUsername() {
		return clickatellUsername;
	}

	public String getClickatellAPIID() {
		return clickatellAPIID;
	}

	public String getClickatellPassword() {
		return clickatellPassword;
	}

	public String getSenderphone() {
		return senderphone;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public String getDbuser() {
		return dbuser;
	}

	public String getDbname() {
		return dbname;
	}

	public String getDbport() {
		return dbport;
	}

	public String getDatabaseurl() {
		return databaseurl;
	}

	public Config() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);

			if (input == null) {
				System.out.println("Sorry, unable to find path.");
				return;
			}

			prop.load(input);
			databaseurl = prop.getProperty("databaseurl");
			dbport = prop.getProperty("dbport");
			dbname = prop.getProperty("dbname");
			dbuser = prop.getProperty("dbuser");
			dbpassword = prop.getProperty("dbpassword");
			senderphone = prop.getProperty("senderphone");
			clickatellUsername = prop.getProperty("clickatellUsername");
			clickatellPassword = prop.getProperty("clickatellPassword");
			clickatellAPIID = prop.getProperty("clickatellAPIID");
			projectId = prop.getProperty("projectId");
			key = prop.getProperty("key");

			input.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
