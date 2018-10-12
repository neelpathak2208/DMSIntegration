/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.services;

import com.indigo.dmsintegration.conf.ConfProperty;
import static com.indigo.dmsintegration.conf.ConfProperty.newgenConn;
import com.indigo.dmsintegration.conf.LogProcessing;
import com.indigo.dmsintegration.jsonparse.FlightStatusDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Neel
 */
public class CompletedStatus {

    public void getFlightStatus(FlightStatusDetails fd) {

        ConfProperty.fltNo = "null";
        ConfProperty.fltDate = "null";
        ConfProperty.station = "null";
        ConfProperty.isCompleted = "null";

        String query = "";

        String flightNo = fd.getFlightNo();
        String flightDate = fd.getFlightDate();
        String station = fd.getStation();
        String status = "";
        String isCompleted = "";

        try {

            query = "SELECT status FROM indigobpm.dbo.flight_docs_data WHERE flightNo = '" + flightNo + "' AND station = '" + station + "' "
                    + "AND CONVERT(char(10), atd,126) = '" + flightDate + "'";
            LogProcessing.errorLogs.info("Completion status query : " + query);
            Statement stmt = newgenConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {

                status = rs.getString(1);

                if (status.equalsIgnoreCase("Completed")) {
                    isCompleted = "1";
                } else {
                    isCompleted = "0";

                }
                ConfProperty.fltNo = flightNo;
                ConfProperty.fltDate = flightDate;
                ConfProperty.station = station;
                ConfProperty.isCompleted = isCompleted;

            } else {

                ConfProperty.fltNo = flightNo;
                ConfProperty.fltDate = flightDate;
                ConfProperty.station = station;
                ConfProperty.isCompleted = "0";

            }

        } catch (SQLException ex) {
            LogProcessing.errorLogs.info("Exception in getFlightStatus()--> " + ex);

            ConfProperty.fltNo = flightNo;
            ConfProperty.fltDate = flightDate;
            ConfProperty.station = station;
            ConfProperty.isCompleted = "Exception: " + ex;
        }

    }

    public void getAllFlightStatus(FlightStatusDetails sd) {
        JSONArray ja = new JSONArray();

        ConfProperty.fltNo = "null";
        ConfProperty.fltDate = "null";
        ConfProperty.station = "null";
        ConfProperty.isCompleted = "null";

        String query = "";

        String flightNo = sd.getFlightNo();
        String flightDate = sd.getFlightDate();
        String station = sd.getStation();
        String std = "";
        String status = "";
        String isCompleted = "";

        try {

            ConfProperty.fltDate = flightDate;
            ConfProperty.station = station;

            query = "select flightNo, std, status from indigobpm.dbo.flight_docs_data WHERE station = '" + station + "' "
                    + "AND CONVERT(char(10), atd,126) = '" + flightDate + "'";
            LogProcessing.errorLogs.info("All flights status query : " + query);
            Statement stmt = newgenConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                JSONObject jo = new JSONObject();

                flightNo = rs.getString(1);
                std = rs.getString(2);
                status = rs.getString(3);
                if (status.equalsIgnoreCase("Completed")) {
                    isCompleted = "1";
                } else {
                    isCompleted = "0";

                }
                jo.put("flightNo", flightNo);
                jo.put("std", std);
                jo.put("isCompleted", isCompleted);
                ja.put(jo);
            }

            ConfProperty.data = ja;
            
        } catch (Exception ex) {
            
            LogProcessing.errorLogs.info("Exception in getAllFlightStatus()--> " + ex);
                        ConfProperty.fltDate = flightDate;
            ConfProperty.station = station;
            ConfProperty.data.put("null");

            
        }
        
    }

}
