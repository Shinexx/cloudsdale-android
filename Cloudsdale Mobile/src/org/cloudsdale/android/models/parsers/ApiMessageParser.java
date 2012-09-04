package org.cloudsdale.android.models.parsers;

import org.cloudsdale.android.models.api_models.Message;

public class ApiMessageParser extends Parser {

    public static Message fromJson(String json) {
        buildParser();
        return sParser.fromJson(json, Message.class);
    }
    
    public static Message[] arrayFromJson(String json) {
        buildParser();
        return sParser.fromJson(json, Message[].class);
    }
    
    public static String toJson(Message message) {
        buildParser();
        return sParser.toJson(message, Message.class);
    }
    
    public static String arrayToJson(Message[] messages) {
        buildParser();
        return sParser.toJson(messages, Message[].class);
    }
    
}
