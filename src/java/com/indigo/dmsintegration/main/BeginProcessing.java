/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.main;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISIsIndex;
import com.indigo.dmsintegration.conf.ConfProperty;
import com.indigo.dmsintegration.conf.LogProcessing;
import com.indigo.dmsintegration.jsonparse.DownloadDetails;
import com.indigo.dmsintegration.jsonparse.FlightStatusDetails;
import com.indigo.dmsintegration.jsonparse.ParseJson;
import com.indigo.dmsintegration.jsonparse.UploadDetails;
import com.indigo.dmsintegration.methods.CabinetApi;
import com.indigo.dmsintegration.services.CompletedStatus;
import com.indigo.dmsintegration.services.DownloadDocument;
import com.indigo.dmsintegration.services.UploadDocument;
import com.newgen.dmsapi.DMSXmlResponse;

/*import com.newgen.dmsapi.DMSCallBroker;
 import com.newgen.dmsapi.DMSCallBroker;
 import java.io.File;
 import java.io.IOException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import ISPack.ISUtil.JPISException;
 import com.indigo.dmsintegration.conf.LogProcessing;
 import com.indigo.dmsintegration.methods.FileEncodeDecode;
 import com.indigo.dmsintegration.methods.FileIO;*/
public class BeginProcessing {

    private UploadDetails uploadDetails = new UploadDetails();
    private DownloadDetails downloadDetails = new DownloadDetails();
    private FlightStatusDetails statusDetails = new FlightStatusDetails(); 
    private CabinetApi api = null;
    public static String fileLocation = "";
    private JPISIsIndex newIsIndex;
    private DMSXmlResponse resp;
    private CPISDocumentTxn txn;
    private JPDBRecoverDocData docDBData;

    private int docIndex;
    private String isIndex = null;
    private String isIndexNew = "";

    public void uploadDocument(String jsonData) {

        LogProcessing.settingLogFiles();
        ConfProperty.createConnection();

        ParseJson parseObj = new ParseJson();

        uploadDetails = parseObj.parseUploadDetails(jsonData);

        UploadDocument objUploadDoc = new UploadDocument();

        objUploadDoc.createFolders_StoreFiles(uploadDetails);

        objUploadDoc.documentUploadCallInDMS(uploadDetails);

    }

    public void downloadDocument(String jsonData) {

        LogProcessing.settingLogFiles();
        ConfProperty.createConnection();

        ParseJson parseObj = new ParseJson();

        downloadDetails = parseObj.parseDownloadDetails(jsonData);

        LogProcessing.errorLogs.info("Download details: " + downloadDetails);

        DownloadDocument objDownloadDoc = new DownloadDocument();

        objDownloadDoc.documentDownload(downloadDetails);

    }

    public void getStatus(String jsonData) {
        
        LogProcessing.settingLogFiles();
        ConfProperty.createConnection();

        ParseJson parseObj = new ParseJson();
        
        statusDetails = parseObj.parseStatusDetails(jsonData);
        
        CompletedStatus statusObj = new CompletedStatus();
        
        statusObj.getFlightStatus(statusDetails);
        
    }
    
    public void getAllStatus(String jsonData) {
        
        LogProcessing.settingLogFiles();
        ConfProperty.createConnection();

        ParseJson parseObj = new ParseJson();
        
        statusDetails = parseObj.parseAllStatusDetails(jsonData);
        
        CompletedStatus statusObj = new CompletedStatus();
        
        statusObj.getAllFlightStatus(statusDetails);
        
        
    }
    

}
