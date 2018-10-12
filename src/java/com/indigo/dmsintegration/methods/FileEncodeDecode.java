/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indigo.dmsintegration.methods;

import com.indigo.dmsintegration.conf.LogProcessing;
//import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Neel
 */
public class FileEncodeDecode {

    public BufferedImage decodeToImage(String imageString, String path) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            //imageByte = Base64.getDecoder().decode(imageString);
            File of = new File(path);
            FileOutputStream osf = new FileOutputStream(of);
            osf.write(imageByte);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            osf.close();
        } catch (IOException e) {
            LogProcessing.errorLogs.info("Unable to decode image");
            LogProcessing.errorLogs.info("Exception at class FileEncodeDecode => decodeToImage() => " + e);
        }
        return image;
    }

    public String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            LogProcessing.errorLogs.info("Inside encodeToString()");
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encodeBuffer(imageBytes);
            //imageString = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            LogProcessing.errorLogs.info("Unable to encode image");
            LogProcessing.errorLogs.info("Exception at class FileEncodeDecode => encodeToString() => " + e);
        }
        return imageString.replaceAll("\\r\\n", "");
    }

}
