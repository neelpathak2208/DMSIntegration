/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import org.json.JSONArray;

/**
 *
 * @author Neel
 */
public class ConfProperty {

    public static Properties prop = new Properties();
    public static Connection newgenConn = null;
    public static String fileName = "null";
    public static String status = "Failure";
    public static String remarks = "null";
    public static String docData = "null";
    public static String docType = "null";
    public static String fltNo = "null";
    public static String fltDate = "null";
    public static String station = "null";
    public static String isCompleted = "null";
    public static JSONArray data;
    
    static {
        String path = "D:" + File.separatorChar + "Indigo" + File.separatorChar + "jboss-eap-6.2" + File.separatorChar + "bin" + File.separatorChar + "indigoConf" + File.separator + "conf.properties";
        LogProcessing.errorLogs.info("Inside static block path => " + path);
        try {
            FileInputStream fis = new FileInputStream(path);
            prop.load(fis);
            Enumeration e = prop.propertyNames();
            LogProcessing.errorLogs.info("poperties enum: " + e);
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String val = prop.get(key) != null ? prop.get(key).toString() : null;

                if (val == null || val.trim().equals("")) {
                    //System.out.print("Invalid conf file entry for key :" + key);
                    LogProcessing.errorLogs.info("Invalid conf file entry for key :" + key);
                } else {
                    prop.setProperty(key.trim().toLowerCase(), val.trim());
                }
            }
            createConnection();
        } catch (IOException ioe) {
            LogProcessing.errorLogs.info("Error in reading config fle: " + ioe);
        }

    }

    public static String getProperty(String key) throws Exception {
        String val = null;
        val = prop.getProperty(key.toLowerCase().trim());

        if (val == null) {
            throw new Exception("No value found for Key:" + key.toLowerCase().trim());
        } else {
            return val;
        }
    }

    public static void createConnection() {
        Connection NewgentempCon = null;
        String sid = "";
        String dbIP = "";
        String dbDriverClass = "";
        String dbDriverSource = "";
        String username = "";
        String password = "";

        try {
            NewgentempCon = newgenConn;

            //newgen DB details
            sid = ConfProperty.getProperty("DatabaseName");
            dbIP = ConfProperty.getProperty("DatabaseIP");
            dbDriverClass = ConfProperty.getProperty("DatabaseDriverClass");
            dbDriverSource = ConfProperty.getProperty("DatabaseDriverSource");
            username = ConfProperty.getProperty("UserName");
            password = ConfProperty.getProperty("Password");

            Class.forName(dbDriverClass);

            /*
             try {
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.130.100.91:52333;databaseName=indigobpm",
             "bpm.uat.user", "B1PmU#aT!uS41");
             JOptionPane.showMessageDialog(null, "connection created " + conn);
             } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "connection not created " + e);
             }
             */
            //reset newgen connection if exists
            try {
                if (NewgentempCon != null) {
                    NewgentempCon.close();
                    NewgentempCon = null;
                }

            } catch (SQLException se) {
                LogProcessing.printConsoleLog("Exception in reseting Newgen connection  :" + se.getMessage());

                status = "Failure";
                remarks = "Exception in reseting Newgen connection";
                return;
            }

            //creating Newgen connection
            do {
                try {
                    NewgentempCon = DriverManager.getConnection(dbDriverSource, username, password);
                    LogProcessing.errorLogs.info("Connection has been established");

                } catch (SQLException sqle) {
                    LogProcessing.errorLogs.info("Error in connecting newgen database. " + sqle.getMessage());
                    NewgentempCon = null;
                }

                if (NewgentempCon == null) {
                    //System.out.println("Waiting for newgen connection...");
                    Thread.sleep(5000);
                } else {
                    LogProcessing.errorLogs.debug("Re-connected with newgen database.");
                }
            } while (NewgentempCon == null);
            newgenConn = NewgentempCon;

        } catch (ClassNotFoundException cnfe) {
            LogProcessing.errorLogs.info("Error While making connection with database 1.", cnfe);
            status = "Failure";
            remarks = "Error While making connection with database.";

        } catch (Exception e) {
            LogProcessing.errorLogs.info("Error While making connection with database 2.", e);
            status = "Failure";
            remarks = "Error While making connection with database.";

        }
    }
}
