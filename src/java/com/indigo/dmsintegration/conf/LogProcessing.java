package com.indigo.dmsintegration.conf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LogProcessing {

    public static Logger errorLogs = null;

    public static void settingLogFiles() {
        //System.out.println("Inside Setting logs..");
        InputStream is = null;
        try {
            String filePath = "D:"  + File.separatorChar + "Indigo" + File.separatorChar + "jboss-eap-6.2" + File.separatorChar + "bin" + File.separatorChar + "indigoConf" + File.separatorChar + "IndigoDMS_Log4j.properties";
            is = new BufferedInputStream(new FileInputStream(filePath));
            Properties ps = new Properties();
            ps.load(is);
            is.close();

            //proper shutdown all nested loggers if already exists
            org.apache.log4j.LogManager.shutdown();

            //configure log property
            PropertyConfigurator.configure(ps);

            errorLogs = Logger.getLogger("Error");

        } catch (Exception e) {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException te) {
                errorLogs.info("Error in setting Logger : " + te);
            }
        }
    }

    public static void printConsoleLog(String msg) {
        LogProcessing.errorLogs.info(msg);
    }

}
