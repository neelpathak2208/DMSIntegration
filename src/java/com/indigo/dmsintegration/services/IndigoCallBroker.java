/**
 *
 */
package com.indigo.dmsintegration.services;

import com.indigo.dmsintegration.conf.LogProcessing;
import java.io.IOException;
import com.newgen.dmsapi.DMSCallBroker;

/**
 * @author Neel
 *
 */
public class IndigoCallBroker {

    //private LoadPropertyFile lf;
    /**
     *
     */
    public IndigoCallBroker() {
        //lf=LoadPropertyFile.loadFile();
    }

    public String callApi(String inputXml) {
        String outputString = "Error In Call Broker";
        try {
            outputString = DMSCallBroker.execute(inputXml, "172.23.100.65", 3333, 0);
        } catch (IOException e) {
            LogProcessing.errorLogs.info("Unable to call api");
            LogProcessing.errorLogs.info("Exception at class IndigoCallBroker => callApi() => " + e);
        }
        return outputString;
    }
}
