/**
 *
 */
package com.indigo.dmsintegration.services;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISException;
import ISPack.ISUtil.JPISIsIndex;
import com.indigo.dmsintegration.conf.ConfProperty;
import static com.indigo.dmsintegration.conf.ConfProperty.newgenConn;
import com.indigo.dmsintegration.conf.LogProcessing;
import com.indigo.dmsintegration.jsonparse.UploadDetails;
import com.indigo.dmsintegration.methods.CabinetApi;
import com.indigo.dmsintegration.methods.FileEncodeDecode;
import com.indigo.dmsintegration.methods.FileIO;
import com.newgen.dmsapi.DMSXmlResponse;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Neel
 *
 */
public class UploadDocument {

    private JPDBRecoverDocData docDBData;
    private JPISIsIndex newIsIndex;
    private DMSXmlResponse resp;
    private CPISDocumentTxn txn;
    private int docIndex;
    private String isIndex = null;
    private String isIndexNew = "";
    private String fileLocation = "";
    private CabinetApi api;
    private ArrayList al = new ArrayList();
    private int docIndex1;
    private String isIndex1;
    IndigoCallBroker cb = new IndigoCallBroker();

    public UploadDocument() {

        docDBData = new JPDBRecoverDocData();
        newIsIndex = new JPISIsIndex();
        api = new CabinetApi();
        resp = new DMSXmlResponse();

    }

    public void createFolders_StoreFiles(UploadDetails uploadDetails) {
        try {
            FileIO objFileIO = new FileIO();
            FileEncodeDecode objFileEncDec = new FileEncodeDecode();
            String foldersPath = "";

            foldersPath = uploadDetails.getFlightDate() + File.separator + uploadDetails.getStation() + File.separator + uploadDetails.getFlightNo();

            fileLocation = "D:" + File.separator + "Indigo" + File.separator + "LocalRepository" + File.separator;
            objFileIO.createFolder(fileLocation + foldersPath);
            fileLocation = fileLocation + foldersPath + File.separator + uploadDetails.getFileName();
            LogProcessing.errorLogs.info("File Location value:" + fileLocation);
            objFileEncDec.decodeToImage(uploadDetails.getDocData(), fileLocation + "." + uploadDetails.getDocType());
            fileLocation = fileLocation + "." + uploadDetails.getDocType();
        } catch (Exception e) {
            LogProcessing.errorLogs.info("Exception at class BeginProcessing => createFolders_StoreFiles() => " + e);
        }
    }

    public int createDMSFolders(UploadDetails ud, String sessionID) {
        LogProcessing.errorLogs.info("Inside createDMSFolders()");
        int parentFolderIndex = 0;

        try {
            DMSXmlResponse output = new DMSXmlResponse();

            String query = "";
            String date = ud.getFlightDate();
            String station = ud.getStation();
            String flightName = ud.getFlightNo();
            int dateIndex = 0;
            int stationIndex = 0;
            IndigoCallBroker cb = new IndigoCallBroker();
            String outputXML = "";

            //Date folder
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + date + "'";
            LogProcessing.errorLogs.info("Date folder query : " + query);
            Statement stmt = newgenConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            LogProcessing.errorLogs.info("Resultset null condition: " + rs != null);
            if (rs.next()) {
                dateIndex = rs.getInt("folderindex");
                LogProcessing.errorLogs.info("Date folder already exists, index: " + dateIndex);

            } else {
                String inputXML = "<?xml version=\"1.0\"?><NGOAddFolder_Input><Option>NGOAddFolder</Option><CabinetName>indigobpm</CabinetName>"
                        + "<UserDBId>" + sessionID + "</UserDBId><Folder><ParentFolderIndex>8070</ParentFolderIndex><FolderName>" + date + "</FolderName>"
                        + "<CreationDateTime>2018-08-13 11:09:14.955</CreationDateTime><AccessType>I</AccessType><ImageVolumeIndex></ImageVolumeIndex>"
                        + "<FolderType>G</FolderType><Location>G</Location><Comment>Date Folder</Comment><EnableFTSFlag>N</EnableFTSFlag>"
                        + "<NoOfDocuments>0</NoOfDocuments><NoOfSubFolders>0</NoOfSubFolders><DataDefinition></DataDefinition></Folder></NGOAddFolder_Input>";

                LogProcessing.errorLogs.info("inputXML for date folder creation: " + inputXML);
                outputXML = cb.callApi(inputXML);
                output.setXmlString(outputXML);
                dateIndex = Integer.parseInt(output.getVal("FolderIndex"));
                LogProcessing.errorLogs.info("Date folder index after creation: " + dateIndex);
            }

            //Station folder
            outputXML = "";
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + station + "' and parentfolderindex=" + dateIndex;
            LogProcessing.errorLogs.info("Station folder query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                stationIndex = rs.getInt("folderindex");

            } else {

                String inputXML = "<?xml version=\"1.0\"?><NGOAddFolder_Input><Option>NGOAddFolder</Option><CabinetName>indigobpm</CabinetName>"
                        + "<UserDBId>" + sessionID + "</UserDBId><Folder><ParentFolderIndex>" + dateIndex + "</ParentFolderIndex><FolderName>" + station + "</FolderName>"
                        + "<CreationDateTime>2018-08-13 11:09:14.955</CreationDateTime><AccessType>I</AccessType><ImageVolumeIndex></ImageVolumeIndex>"
                        + "<FolderType>G</FolderType><Location>G</Location><Comment>Date Folder</Comment><EnableFTSFlag>N</EnableFTSFlag>"
                        + "<NoOfDocuments>0</NoOfDocuments><NoOfSubFolders>0</NoOfSubFolders><DataDefinition></DataDefinition></Folder></NGOAddFolder_Input>";
                outputXML = cb.callApi(inputXML);
                output.setXmlString(outputXML);
                stationIndex = Integer.parseInt(output.getVal("FolderIndex"));
            }

            //Flight folder
            outputXML = "";
            query = "select folderindex from indigobpm.dbo.pdbfolder where name='" + flightName + "' and parentfolderindex=" + stationIndex;
            LogProcessing.errorLogs.info("Flight folder query : " + query);
            stmt = newgenConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                parentFolderIndex = rs.getInt("folderindex");

            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:S");
                String currentDateTime = sdf.format(new Date());
                String inputXML = "<?xml version=\"1.0\"?><NGOAddFolder_Input><Option>NGOAddFolder</Option><CabinetName>indigobpm</CabinetName>"
                        + "<UserDBId>" + sessionID + "</UserDBId><Folder><ParentFolderIndex>" + stationIndex + "</ParentFolderIndex><FolderName>" + flightName + "</FolderName>"
                        + "<CreationDateTime>" + currentDateTime + "</CreationDateTime><AccessType>I</AccessType><ImageVolumeIndex></ImageVolumeIndex>"
                        + "<FolderType>G</FolderType><Location>G</Location><Comment>Date Folder</Comment><EnableFTSFlag>N</EnableFTSFlag>"
                        + "<NoOfDocuments>0</NoOfDocuments><NoOfSubFolders>0</NoOfSubFolders><DataDefinition></DataDefinition></Folder></NGOAddFolder_Input>";
                outputXML = cb.callApi(inputXML);
                output.setXmlString(outputXML);
                parentFolderIndex = Integer.parseInt(output.getVal("FolderIndex"));
            }

        } catch (SQLException ex) {
            LogProcessing.errorLogs.info("SQL Exception in DMS Folder creation: " + ex);
        }

        return parentFolderIndex;
    }

    public int isDocumentExists(String fileName, int parentFolder) {
        String query = "";
        int isExists = 0;
        try {
            query = "SELECT * FROM indigobpm.dbo.pdbdocument d, indigobpm.dbo.pdbdocumentcontent c WHERE c.DocumentIndex = d.DocumentIndex AND d.Name = '" + fileName + "' AND c.ParentFolderIndex = " + parentFolder;
            LogProcessing.errorLogs.info("isDocumentExists query: " + query);
            Statement stmt = newgenConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                isExists = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LogProcessing.errorLogs.info("Exception in isDocumentExists()->" + ex);
        }
        LogProcessing.errorLogs.info("isDocumentExists value: " + isExists);
        return isExists;
    }

    public void documentUploadCallInDMS(UploadDetails uploadDetails) {
        try {

            ConfProperty.status = "Failure";
            ConfProperty.remarks = "null";

            LogProcessing.errorLogs.info("Inside documentUploadCallInDMS(), file location:" + fileLocation);
            File f = new File(fileLocation);
            LogProcessing.errorLogs.info("Document:" + f.getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:S");
            String date = sdf.format(new Date());
            String sessionId = api.getSessionId().get(0);
            String status = "";
            int docId = 0;
            int parentFolderIndex = createDMSFolders(uploadDetails, sessionId);

            int docIndex = isDocumentExists(uploadDetails.getFileName(), parentFolderIndex);
            LogProcessing.errorLogs.info("docIndex value: " + docIndex);
            if (docIndex == 0) {
                LogProcessing.errorLogs.info("Document does not exist, inside new upload");
                String inputXML = "<?xml version=\"1.0\"?><NGOAddDocument_Input><Option>NGOAddDocument</Option><CabinetName>indigobpm</CabinetName><UserDBId>"
                        + sessionId
                        + "</UserDBId><GroupIndex></GroupIndex><Document><ParentFolderIndex>" + parentFolderIndex + "</ParentFolderIndex><NoOfPages>1</NoOfPages><AccessType>S</AccessType><DocumentName>"
                        + uploadDetails.getFileName() + "</DocumentName><CreationDateTime>" + date
                        + "</CreationDateTime><ExpiryDateTime></ExpiryDateTime><VersionFlag>N</VersionFlag><DocumentType>I</DocumentType><DocumentSize>"
                        + f.length() + "</DocumentSize><CreatedByApp></CreatedByApp><CreatedByAppName>"
                        + uploadDetails.getDocType()
                        + "</CreatedByAppName><ISIndex></ISIndex><FTSDocumentIndex>0</FTSDocumentIndex><ODMADocumentIndex></ODMADocumentIndex><Comment></Comment><Author></Author><OwnerIndex>1</OwnerIndex><EnableLog>Y</EnableLog><FTSFlag>PP</FTSFlag><NameLength>50</NameLength></Document></NGOAddDocument_Input>";
                String outputXml = null;
                CPISDocumentTxn.AddDocument_MT(null, "172.23.100.65", Short.parseShort("3333"), "indigobpm",
                        Short.parseShort("1"), fileLocation, docDBData, "1", newIsIndex);
                docIndex = newIsIndex.m_nDocIndex;
                isIndex = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;
                isIndexNew = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;
                LogProcessing.errorLogs.info("DocIndex :" + docIndex + " /isIndex :" + isIndex);
                String frontData = inputXML.substring(0, inputXML.indexOf("<ISIndex>") + 9);
                String endData = inputXML.substring(inputXML.indexOf("</ISIndex>"), inputXML.length());
                inputXML = frontData + isIndex + endData;
                LogProcessing.errorLogs.info("Input XML :" + inputXML);
                outputXml = cb.callApi(inputXML);
                resp.setXmlString(outputXml);
                LogProcessing.errorLogs.info("Call Broker to add document :" + outputXml);

                docId = Integer.parseInt(resp.getVal("DocumentIndex"));
                status = resp.getVal("Status");

                if (status.equalsIgnoreCase("0")) {
                    ConfProperty.status = "Success";
                    ConfProperty.remarks = "Document uploaded successfully";

                }
            } else {
                LogProcessing.errorLogs.info("Document exists, inside checkin");
                checkin(docIndex, uploadDetails.getDocType());
            }

            api.disconnectCabinet(sessionId);

        } catch (NumberFormatException | JPISException e) {
            LogProcessing.errorLogs.info("Call Broker to add document :" + e);
        }

    }

    public void checkin(int docId, String ext) {
        LogProcessing.errorLogs.info("Inside checkin, docid and ext: " + docId + ", " + ext);
        ArrayList dbData = getIsIndex(docId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:S");
        String date = sdf.format(new Date());
        String sessionId = api.getSessionId().get(0);
        Integer dummy = (Integer) dbData.get(0);
        if (dummy == 0) {
            LogProcessing.errorLogs.info("Document does not exist");
        }
        LogProcessing.errorLogs.info("File: " + fileLocation);
        File f = new File(fileLocation);
        try {

            CPISDocumentTxn.AddDocument_MT(null, "172.23.100.65", Short.parseShort("3333"), "indigobpm",
                    Short.parseShort("1"), fileLocation, docDBData, "1", newIsIndex);
            docIndex1 = newIsIndex.m_nDocIndex;
            isIndex1 = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;
            isIndexNew = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;

        } catch (JPISException e) {
            LogProcessing.errorLogs.info("Exception while adding document from checkin(): " + e);
        }
        String checkoutinputXml = "<?xml version=\"1.0\"?><NGOCheckinCheckoutExt_Input><Option>NGOCheckinCheckoutExt</Option><CabinetName>indigobpm</CabinetName><UserDBId>"
                + sessionId + "</UserDBId><CurrentDateTime>" + date
                + "</CurrentDateTime><LimitCount>1000</LimitCount><CheckInOutFlag>Y</CheckInOutFlag><SupAnnotVersion>N</SupAnnotVersion><Documents><Document><DocumentIndex>"
                + dbData.get(0) + "</DocumentIndex><ISIndex>" + isIndexNew
                + "</ISIndex></Document></Documents></NGOCheckinCheckoutExt_Input>\r\n" + "";
        resp.setXmlString(cb.callApi(checkoutinputXml));
        LogProcessing.errorLogs.info("Response after checkout: " + resp);
        if (Integer.parseInt(resp.getVal("Status")) == 0) {
            String checkininputXml = "<?xml version=\"1.0\"?><NGOCheckinCheckoutExt_Input><Option>NGOCheckinCheckoutExt</Option><CabinetName>indigobpm</CabinetName><UserDBId>"
                    + sessionId + "</UserDBId><CurrentDateTime>" + date
                    + "</CurrentDateTime><LimitCount>1000</LimitCount><CheckInOutFlag>N</CheckInOutFlag><SupAnnotVersion>N</SupAnnotVersion><Documents><Document><DocumentIndex>"
                    + dbData.get(0) + "</DocumentIndex><ISIndex>" + isIndexNew
                    + "</ISIndex><VersionComment>Revised</VersionComment><DocumentType>N</DocumentType><NoOfPages>" + 1
                    + "</NoOfPages><DocumentSize>" + f.length() + "</DocumentSize><CreatedByAppName>" + ext
                    + "</CreatedByAppName></Document></Documents></NGOCheckinCheckoutExt_Input>";
            resp.setXmlString(cb.callApi(checkininputXml));
            LogProcessing.errorLogs.info("Response after checkin: " + resp);
            if (Integer.parseInt(resp.getVal("Status")) == 0) {
                String propertyChange = "<?xml version=\"1.0\"?><NGOChangeDocumentProperty_Input><Option>NGOChangeDocumentProperty</Option><CabinetName>indigobpm</CabinetName><UserDBId>"
                        + sessionId + "</UserDBId><Document><DocumentIndex>" + dbData.get(0)
                        + "</DocumentIndex><NoOfPages>1</NoOfPages><DocumentName>" + resp.getVal("DocumentName")
                        + "</DocumentName><AccessDateTime>" + resp.getVal("AccessDateTime")
                        + "</AccessDateTime><ISIndex>" + isIndexNew
                        + "</ISIndex><VersionComment>Revised</VersionComment><DocumentType>I</DocumentType><DocumentSize>" + f.length()
                        + "</DocumentSize><DataDefinition></DataDefinition><RetainAnnotation>N</RetainAnnotation></Document></NGOChangeDocumentProperty_Input>";
                resp.setXmlString(cb.callApi(propertyChange));
                LogProcessing.errorLogs.info("Response after property change: " + resp);
                if (Integer.parseInt(resp.getVal("Status")) == 0) {

                    ConfProperty.status = "Success";
                    ConfProperty.remarks = "Document uploaded successfully with updated version";
                }
            }
        }
    }

    public ArrayList getIsIndex(int docId) {
        LogProcessing.errorLogs.info("Inside getIsIndex()");
        al.clear();
        try {
            String query = "select d.documentIndex,d.Name,d.ImageIndex,d.VolumeId,e.ParentFolderIndex from indigobpm.dbo.pdbdocument d,indigobpm.dbo.PDBDocumentContent e"
                    + " where e.DocumentIndex=d.DocumentIndex and e.DocumentIndex=?";
            LogProcessing.errorLogs.info("getIsIndex query: " + query);
            PreparedStatement ps = newgenConn.prepareStatement(query);
            ps.setInt(1, docId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                al.add(rs.getInt(1));
                al.add(rs.getString(2));
                al.add(rs.getInt(3));
                al.add(rs.getInt(4));
                al.add(rs.getInt(5));
            } else {
                al.add(0);
            }
        } catch (SQLException e) {
            LogProcessing.errorLogs.info("Exception in getting getIsIndex :" + e);
        }
        LogProcessing.errorLogs.info("isIndex arrayList data: " + al.toString());
        return al;
    }

}
