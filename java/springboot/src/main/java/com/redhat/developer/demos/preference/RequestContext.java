//package com.redhat.developer.demos.preference;
//
//import java.util.HashMap;
//
///**
// * Created by cosminoprea on 3/3/20.
// */
//public class RequestContext {
//
//    public static final String REQUEST_HEADER_NAME = "x-api-key";
//
//    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();
//
//    private String token;
//
//    private HashMap<String, String> headers;
//
//    public static RequestContext getContext() {
//        RequestContext result = CONTEXT.get();
//
//        if (result == null) {
//            result = new RequestContext();
//            CONTEXT.set(result);
//        }
//
//        return result;
//    }
//
//    public HashMap<String, String> getHeaders() {
//        return headers;
//    }
//
//    public void setHeaders(HashMap<String, String> headers) {
//        this.headers = headers;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//}