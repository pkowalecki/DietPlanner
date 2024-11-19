package pl.kowalecki.dietplanner.utils;


public class UrlTools {
    public final static String appUrl = "localhost:8081/app";

    private final static String GATEWAY_URL = "http://gateway:8080/api/v1";
    public final static String AUTH_SERVICE_URL = GATEWAY_URL + "/auth";
    public final static String MEAL_SERVICE_URL = GATEWAY_URL + "/dpa";


}
