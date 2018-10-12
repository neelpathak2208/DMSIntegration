/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.indigo.dmsintegration.jsonparse;

public class UploadDetails {
    
    private String flightNo;
    private String flightDate;
    private String station;
    private String creationDateTime;
    private String fileName;
    private String docData;
    private String docType;
    private String userName;
    private String passWord;

    //Getter and Setter methods
    
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocData() {
        return docData;
    }

    public void setDocData(String docData) {
        this.docData = docData;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "UploadDetails{" + "flightNo=" + flightNo + ", flightDate=" + flightDate + ", station=" + station + ", creationDateTime=" + creationDateTime + ", fileName=" + fileName + ", docData=" + docData + ", docType=" + docType + ", userName=" + userName + ", passWord=" + passWord + '}';
    }
    
    
}
