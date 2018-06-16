package com.petcare.disease;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Service
public class GCMService {

	private static final String API_KEY = "AIzaSyBr7FLbOlDf2se_mBXetGy1WT3leY6Hk4g";

    public String send(String registrationId, Message message) throws IOException {
 
        Sender sender = new Sender(API_KEY);
        Result result = sender.send(message, registrationId, 5);

        if (result.getMessageId() != null)
            return "Success " + result.getMessageId() + " " +  result.getCanonicalRegistrationId();
        else
            return result.getErrorCodeName();
    }
    
	
	
}
