package org.cloudsdale.android.test;

import java.text.MessageFormat;

public class Constants {

    // IDs
    public static final String DUMMY_ID                = "5033bc83cff4e828130008b0";
    public static final String HAMMOCK_ID              = "4ecc40133fddbd4eb0000001";
    public static final String META_ID                 = "4ee7d0253fddbd2649000586";
    public static final String TEST_CLOUD_ID           = "4fec1aebcff4e845cb00acd9";

    // Dummy values
    public static final String DUMMY_EMAIL             = "ask@cloudsdale.org";
    public static final String DUMMY_PASSWORD          = "secret1234";

    // Url Base
    public static final String CLOUDSDALE_BASE         = "http://www.cloudsdale.org";
    public static final String CLOUDSDALE_API_VERSION  = "v1";

    // Endpoints
    public static final String SESSION_ENDPOINT        = MessageFormat
                                                               .format("{0}/{1}/sessions.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION);
    public static final String HAMMOCK_LOOKUP_ENDPOINT = MessageFormat
                                                               .format("{0}/{1}/clouds/{2}.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.HAMMOCK_ID);
    public static final String HAMMOCK_CHAT_ENDPOINT   = MessageFormat
                                                               .format("{0}/{1}/clouds/{2}/chat/messages.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.HAMMOCK_ID);
    public static final String META_CHAT_ENDPOINT      = MessageFormat
                                                               .format("{0}/{1}/clouds/{2}/chat/messages.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.META_ID);
    public static final String META_DROP_ENDPOINT      = MessageFormat
                                                               .format("{0}/{1}/clouds/{2}/drops.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.META_ID);
    public static final String TEST_CHAT_ENDPOINT      = MessageFormat
                                                               .format("{0}/{1}/clouds/{2}/chat/messages.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.TEST_CLOUD_ID);
    public static final String DUMMY_LOOKUP_ENDPOINT   = MessageFormat
                                                               .format("{0}/{1}/users/{2}.json",
                                                                       Constants.CLOUDSDALE_BASE,
                                                                       Constants.CLOUDSDALE_API_VERSION,
                                                                       Constants.DUMMY_ID);

}
