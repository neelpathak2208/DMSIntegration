/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.indigo.dmsintegration.services;

import com.indigo.dmsintegration.conf.LogProcessing;
import com.indigo.dmsintegration.jsonparse.jsonResponse;
import com.indigo.dmsintegration.main.BeginProcessing;
import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import sun.misc.BASE64Decoder;

/**
 * REST Web Service
 *
 * @author Neel
 */
@Path("dms")
public class DMSIntegrationResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Main
     */
    public DMSIntegrationResource() {
        
    }

    /**
     * Retrieves representation of an instance of com.indigo.dmsintegration.Main
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of Main
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public String putJson(String json, @HeaderParam("authorization") String authString){
        
        BeginProcessing bp = new BeginProcessing();
        
        if((authString==null)||(!isUserAuthenticated(authString))){
            return "{\"error\":\"User not authenticated\"}";
        }
        
        try {
            bp.uploadDocument(json);  
        } catch (Exception ex) {
            LogProcessing.errorLogs.info("Exception in upload putJson call: "+ex);
        }
        
        return new jsonResponse().uploadJsonResponse();
    }
    
    
    private boolean isUserAuthenticated(String authString){
         
        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        System.out.println(decodedAuth);
         
        if(decodedAuth.equals("DMSIntegration:DMSPa$$@321"))
        return true;
        else
        return false;
    }
}
