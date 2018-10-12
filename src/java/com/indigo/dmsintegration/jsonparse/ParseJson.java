/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.jsonparse;

import com.indigo.dmsintegration.conf.ConfProperty;
import com.indigo.dmsintegration.conf.LogProcessing;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Neel
 */
public class ParseJson {

    private UploadDetails ud = new UploadDetails();
    private DownloadDetails dd = new DownloadDetails();
    private FlightStatusDetails sd = new FlightStatusDetails();

    public UploadDetails parseUploadDetails(String jsonData) {

        try {
            JSONObject uploadJson = new org.json.JSONObject(jsonData);

            ud.setFlightNo(uploadJson.optString("flightNo"));
            ud.setFlightDate(uploadJson.optString("flightDate"));
            ud.setStation(uploadJson.optString("station"));
            ud.setCreationDateTime(uploadJson.optString("creationDateTime"));
            ud.setFileName(uploadJson.optString("fileName"));
            ud.setDocData(uploadJson.optString("docData"));
            ud.setDocType(uploadJson.optString("docType"));
            ud.setUserName(uploadJson.optString("username"));
            ud.setPassWord(uploadJson.optString("password"));

        } catch (JSONException ex) {
            LogProcessing.errorLogs.info("Unable to parse JSON");
            LogProcessing.errorLogs.info("Exception at class ParseJson => parseUploadDetails() => " + ex);
        }

        return ud;

    }

    public DownloadDetails parseDownloadDetails(String jsonData) {

        try {
            JSONObject downloadJson = new org.json.JSONObject(jsonData);

            dd.setFlightNo(downloadJson.optString("flightNo"));
            dd.setFlightDate(downloadJson.optString("flightDate"));
            dd.setStation(downloadJson.optString("station"));
            dd.setFileName(downloadJson.optString("fileName"));
            dd.setFileType(downloadJson.optString("fileType"));
           
        } catch (JSONException ex) {
            LogProcessing.errorLogs.info("Unable to parse JSON");
            LogProcessing.errorLogs.info("Exception at class ParseJson => parseDownloadDetails() => " + ex);
        }
        
        return dd;
    }
    
    public FlightStatusDetails parseStatusDetails(String jsonData) {
        
        try {
            JSONObject statusJson = new org.json.JSONObject(jsonData);
            sd.setFlightNo(statusJson.optString("flightNo"));
            sd.setFlightDate(statusJson.optString("flightDate"));
            sd.setStation(statusJson.optString("station"));
            
        } catch (JSONException ex) {
            LogProcessing.errorLogs.info("Unable to parse JSON");
            LogProcessing.errorLogs.info("Exception at class ParseJson => parseStatusDetails() => " + ex);
        }
        
        return sd;
    }
    
    public FlightStatusDetails parseAllStatusDetails(String jsonData) {
        
        try {
            JSONObject statusJson = new org.json.JSONObject(jsonData);
            sd.setFlightNo("");
            sd.setFlightDate(statusJson.optString("flightDate"));
            sd.setStation(statusJson.optString("station"));
            
        } catch (JSONException ex) {
            LogProcessing.errorLogs.info("Unable to parse JSON");
            LogProcessing.errorLogs.info("Exception at class ParseJson => parseAllStatusDetails() => " + ex);
        }
        
        return sd;
    }
    
    

}
