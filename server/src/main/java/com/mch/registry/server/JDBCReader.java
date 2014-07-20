package com.mch.registry.server;

/**
 * Created by Isa on 18.07.2014.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
/**
 * Created by Isa on 17.07.2014.
 */
public class JDBCReader {

    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    public void readData() {

        log.warning("-MySQL JDBC Connection Testing-");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.warning("JDBC Class not found:  " + e.getMessage() + " " + e.getException());
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://mysql.isabelmueller.net:3306/mchregistry","mchregistry", "swisstphukraine");

        } catch (SQLException e) {
            log.warning("Connection Failed! Check output console " + e.getMessage());
            return;
        }

        if (connection != null) {
            log.info("You made it, take control your database now!");
        } else {
            log.warning("JDBC Failed to make connection!");
        }
    }
}
