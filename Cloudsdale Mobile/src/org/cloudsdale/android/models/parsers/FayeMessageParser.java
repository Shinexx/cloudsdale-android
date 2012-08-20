package org.cloudsdale.android.models.parsers;

import com.b3rwynmobile.fayeclient.models.FayeMessage;

public class FayeMessageParser extends Parser {

    public static FayeMessage fromJson(String json) {
        buildParser();
        return sParser.fromJson(json, FayeMessage.class);
    }
    
    public static String toJson(FayeMessage message) {
        buildParser();
        return sParser.toJson(message, FayeMessage.class);
    }
}
