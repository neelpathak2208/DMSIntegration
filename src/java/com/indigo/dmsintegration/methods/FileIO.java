/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.methods;

import com.indigo.dmsintegration.conf.LogProcessing;
import java.io.File;

/**
 *
 * @author Neel
 */
public class FileIO {

    public void createFolder(String folderPath) {
         LogProcessing.errorLogs.info("Inside folder creation");
        File file = new File(folderPath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                LogProcessing.errorLogs.info("Folder created");
            } else {
                LogProcessing.errorLogs.info("Folder not created");
            }
        }
    }
}
