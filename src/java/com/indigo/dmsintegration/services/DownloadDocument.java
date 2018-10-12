/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.services;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPISException;
import Jdts.Client.JtsConnection;
import Jdts.DataObject.JPDBString;
//import Jdts.excp.JtsException;
import com.indigo.dmsintegration.conf.ConfProperty;
import static com.indigo.dmsintegration.conf.ConfProperty.newgenConn;
import com.indigo.dmsintegration.conf.LogProcessing;
import com.indigo.dmsintegration.jsonparse.DownloadDetails;
import com.indigo.dmsintegration.methods.CabinetApi;
import com.indigo.dmsintegration.methods.FileEncodeDecode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;

/**
 *
 * @author Neel
 */
public class DownloadDocument {

    private static PreparedStatement ps;
    private ByteArrayOutputStream streamBuff;
    private JPDBString siteName;
    private JtsConnection jts;
    private CabinetApi cabinetapi;

    public void documentDownload(DownloadDetails dd) {
        try {
            
            ConfProperty.status = "Failure";
            ConfProperty.remarks = "null";
            ConfProperty.docData = "null";
            ConfProperty.docType = "null";
            
            cabinetapi = new CabinetApi();
            streamBuff = new ByteArrayOutputStream();
            siteName = new JPDBString();
            FileEncodeDecode objFileEncDec = new FileEncodeDecode();
            /*try {
             jts = new JtsConnection("172.23.100.65");
             } catch (JtsException ex) {
             LogProcessing.errorLogs.info("Exception in creating JTS Connection: "+ex);
             }*/
            int index = getIndex(dd);
            String sessionId = cabinetapi.getSessionId().get(0);
            LogProcessing.errorLogs.info("Session ID: " + sessionId);
            CPISDocumentTxn.GetDocInFile_MT(null, "172.23.100.65", (short) 3333, "indigobpm", (short) 1, (short) 1,
                    index, sessionId, streamBuff, siteName);
            //LogProcessing.errorLogs.info("Stream buffer returned: " + streamBuff);
            byte[] byteData = streamBuff.toByteArray();
            //LogProcessing.errorLogs.info("Byte data for stream buffer: " + byteData);
            ByteArrayInputStream input = new ByteArrayInputStream(byteData);
            //LogProcessing.errorLogs.info("Converted ByteArrayInputStream(input): " + input);
            BufferedImage bi = ImageIO.read(input);
            //LogProcessing.errorLogs.info("Buffered image: " + bi);
            String data = objFileEncDec.encodeToString(bi, dd.getFileType());
            LogProcessing.errorLogs.info("Image string data: " + data);
            streamBuff.flush();
            streamBuff.close();
            cabinetapi.disconnectCabinet(sessionId);

            ConfProperty.status = "Success";
            ConfProperty.remarks = "Document downloaded successfully";
            ConfProperty.docData = data;
            ConfProperty.docType = dd.getFileType();

        } catch (JPISException | IOException e) {
            LogProcessing.errorLogs.info("Exception while downloading document: " + e);
            ConfProperty.status = "Failure";
            ConfProperty.remarks = "" + e;
            ConfProperty.docData = "null";
            ConfProperty.docType = "null";
        }
    }

    public int getIndex(DownloadDetails dd) {

        int parentFolderIndex = 0;
        int imageIndex = 0;
        String fltDate = dd.getFlightDate();
        String station = dd.getStation();
        String fltName = dd.getFlightNo();
        int dateIndex = 0;
        int stationIndex = 0;
        String query = "";
        ResultSet rs;
        Statement stmt;
        try {

            LogProcessing.errorLogs.info("Inside fetchParentFolder()");

            //Date index
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + fltDate + "'";
            LogProcessing.errorLogs.info("query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                dateIndex = rs.getInt("folderindex");
            } else {
                ConfProperty.status = "Failure";
                ConfProperty.remarks = "No document found for this date";
                ConfProperty.docData = "null";
                ConfProperty.docType = "null";
            }

            //Station index
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + station + "' and parentfolderindex=" + dateIndex;
            LogProcessing.errorLogs.info("query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                stationIndex = rs.getInt("folderindex");
            } else {
                ConfProperty.status = "Failure";
                ConfProperty.remarks = "No document found for this station";
                ConfProperty.docData = "null";
                ConfProperty.docType = "null";
            }

            //Flight index
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + fltName + "' and parentfolderindex=" + stationIndex;
            LogProcessing.errorLogs.info("query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                parentFolderIndex = rs.getInt("folderindex");
            } else {
                ConfProperty.status = "Failure";
                ConfProperty.remarks = "No document found for this flight";
                ConfProperty.docData = "null";
                ConfProperty.docType = "null";
            }

            //Image index
            query = "SELECT max(d.imageindex) as imageindex FROM indigobpm.dbo.pdbdocument d, indigobpm.dbo.pdbdocumentcontent c WHERE d.Name ='" + dd.getFileName() +"' AND d.DocumentIndex = c.DocumentIndex AND c.ParentFolderIndex =" + parentFolderIndex;
            LogProcessing.errorLogs.info("query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                imageIndex = rs.getInt("imageindex");
            } else {
                ConfProperty.status = "Failure";
                ConfProperty.remarks = "No document found with this name";
                ConfProperty.docData = "null";
                ConfProperty.docType = "null";
            }

        } catch (SQLException ex) {
            LogProcessing.errorLogs.info("Exception in fetching folder: " + ex);
            ConfProperty.status = "Failure";
            ConfProperty.remarks = "Document not found";
            ConfProperty.docData = "null";
            ConfProperty.docType = "null";

        }

        return imageIndex;
    }

    /*public int getIndex(DownloadDetails dd) {
     LogProcessing.errorLogs.info("Inside getIndex()");
     int index = 0;
     String fileName = dd.getFileName();
     LogProcessing.errorLogs.info("file name: " + fileName);
     int parentFolderIndex = fetchParentFolder(dd);
     try {
     String query = "select max(imageindex) as index from indigobpm.dbo.pdbdocument where name='" + fileName + "'";
     LogProcessing.errorLogs.info("query : " + query);
     Statement stmt = newgenConn.createStatement();
     ResultSet rs = stmt.executeQuery(query);
     while (rs.next()) {
     index = rs.getInt("index");
     }
     LogProcessing.errorLogs.info("index : " + index);
     } catch (SQLException e) {
     LogProcessing.errorLogs.info("Exception in getting image index: " + e);
     }
     return index;
     }*/
}
