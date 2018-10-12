/**
 * 
 */
package com.indigo.dmsintegration.methods;

import com.indigo.dmsintegration.conf.LogProcessing;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import org.apache.log4j.Logger;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSInputXml;
import com.newgen.dmsapi.DMSXmlResponse;

/**
 * @author Neel
 *
 */
public class CabinetApi {
	//final static Logger logger = Logger.getLogger(CabinetApi.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss:S");
	private DMSInputXml inputxml;
	private DMSXmlResponse xmlresponse;

	public CabinetApi() {
		inputxml = new DMSInputXml();
		xmlresponse=new DMSXmlResponse();
	}

	public String connectCabinet() {
		return callBroker(inputxml.getConnectCabinetXml("indigobpm", "supervisor", "supervisor351", "", "N", "0", "U", ""));
	}
	public String disconnectCabinet(String sessionId) {
		String str=inputxml.getDisconnectCabinetXml("indigobpm", sessionId);
		str=callBroker(str);
		return str;
	}
	public String callBroker(String str) {
		String outputxml = null;
		try {
			LogProcessing.errorLogs.info("Cabinet Connect Call :" + str);
			outputxml = DMSCallBroker.execute(str, "172.23.100.65", 3333, 0);
                        LogProcessing.errorLogs.info("Call Broker Output XML :"+outputxml);
		} catch (Exception e) {
			LogProcessing.errorLogs.info("Error In DMS Call Broker" + e);
		}
		return outputxml;
	}

	public ArrayList<String> getSessionId() {
            LogProcessing.errorLogs.info("Inside GetSessionId");
		ArrayList<String> al = new ArrayList<String>();
		String sessionId = null;
		String volumeId = null;
		xmlresponse.setXmlString(connectCabinet());
		if (xmlresponse.getVal("Status").equalsIgnoreCase("0")) {
			sessionId = xmlresponse.getVal("UserDBId");
			volumeId = xmlresponse.getVal("ImageVolumeIndex");
			al.add(sessionId);
			al.add(volumeId);
			LogProcessing.errorLogs.info("Session ID :" + sessionId);
			LogProcessing.errorLogs.info("Volume ID :" + volumeId);
			return al;
		} else {
			return null;
		}
	}
}
