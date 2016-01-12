package ru.katakin.sample;

public class Constants {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String LOG_TAG = "Sample LOG";


    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String SENDER_ID = "219284111946";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String ICON_URL= "http://openweathermap.org/img/w/";
    public static final String API_KEY = "2de143494c0b295cca9337e1e96b00e0";
    public static final int API_TIMEOUT = 15000;
    public static final int API_CONNECTION_TIMEOUT = 30000;

    public enum SERVICE_MODE {
        NAME,
        GEO
    }
}
