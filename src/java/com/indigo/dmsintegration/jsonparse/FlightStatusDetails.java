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
public class FlightStatusDetails {

    private String flightNo;
    private String flightDate;
    private String station;

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

    @Override
    public String toString() {
        return "StatusDetails{" + "flightNo=" + flightNo + ", flightDate=" + flightDate + ", station=" + station + '}';
    }
    
}
