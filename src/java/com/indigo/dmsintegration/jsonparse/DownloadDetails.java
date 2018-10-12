/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.indigo.dmsintegration.jsonparse;

/**
 *
 * @author Neel
 */
public class DownloadDetails {

    private String flightNo;
    private String flightDate;
    private String station;
    private String fileName; 
    private String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    //Getters and Setters
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DownloadDetails{" + "flightNo=" + flightNo + ", flightDate=" + flightDate + ", station=" + station + ", fileName=" + fileName + ", fileType=" + fileType + '}';
    }

    
   
    
    
}
 