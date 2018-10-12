/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.jsonparse;

import com.indigo.dmsintegration.conf.ConfProperty;
import com.indigo.dmsintegration.conf.LogProcessing;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/*import java.util.ArrayList;
 import java.util.List;
 import org.json.JSONArray;*/

/**
 *
 * @author Neel
 */
@XmlRootElement
public class jsonResponse {

    public jsonResponse() {
    } // JAXB needs this

    public String uploadJsonResponse() {

        JSONObject uploadJson = new JSONObject();

        try {

            uploadJson.put("remarks", ConfProperty.remarks);
            uploadJson.put("status", ConfProperty.status);

        } catch (JSONException e) {
            LogProcessing.errorLogs.info("Unable to generate response JSON");
            LogProcessing.errorLogs.info("Exception at class jsonResponse => uploadJsonResponse() => " + e);
        }
        return uploadJson.toString();
    }

    public String downloadJsonResponse() {

        JSONObject downloadJson = new JSONObject();

        try {
            downloadJson.put("status", ConfProperty.status);
            downloadJson.put("remarks", ConfProperty.remarks);
            downloadJson.put("docData", ConfProperty.docData);
            downloadJson.put("docType", ConfProperty.docType);

        } catch (JSONException e) {
            LogProcessing.errorLogs.info("Unable to generate response JSON");
            LogProcessing.errorLogs.info("Exception at class jsonResponse => uploadJsonResponse() => " + e);

        }

        return downloadJson.toString();
    }

    public String statusJsonResponse() {

        JSONObject statusJson = new JSONObject();

        try {
            
            statusJson.put("flightNo", ConfProperty.fltNo);
            statusJson.put("flightDate", ConfProperty.fltDate);
            statusJson.put("station", ConfProperty.station);
            statusJson.put("isCompleted", ConfProperty.isCompleted);

        } catch (JSONException e) {
            LogProcessing.errorLogs.info("Unable to generate response JSON");
            LogProcessing.errorLogs.info("Exception at class jsonResponse => uploadJsonResponse() => " + e);

        }

        return statusJson.toString();
    }
    
     public String allStatusJsonResponse() {

        JSONObject statusJson = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        
        try {
            
            statusJson.put("fltDate", ConfProperty.fltDate);
            statusJson.put("station", ConfProperty.station);
            statusJson.put("data", ConfProperty.data);

        } catch (JSONException e) {
            LogProcessing.errorLogs.info("Unable to generate response JSON");
            LogProcessing.errorLogs.info("Exception at class jsonResponse => uploadJsonResponse() => " + e);

        }

        return statusJson.toString();
    }

}
