package org.cloudsdale.android.models.parsers;

import com.google.gson.Gson;

public class Parser {
    
    protected static Gson sParser;
    
    protected static void buildParser() {
        if(sParser == null) {
            sParser = new Gson();
        }
    }

}
