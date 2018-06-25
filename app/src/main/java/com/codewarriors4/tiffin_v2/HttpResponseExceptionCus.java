package com.codewarriors4.tiffin_v2;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class HttpResponseExceptionCus extends IOException
{
    private String responseMessage;
    private LinkedTreeMap<String, String> errorList;
    private int errorCode;
    public HttpResponseExceptionCus(String jsonResponse,int errorCode) {
        super(jsonResponse);
        HashMap stringStringHashMap = new Gson().fromJson(jsonResponse, HashMap.class);
        responseMessage = (String)stringStringHashMap.get("message");
        errorList = (LinkedTreeMap<String, String>)stringStringHashMap.get("errors");
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return responseMessage;
    }

    public  LinkedTreeMap<String, String> getErrorList() {
        return errorList;
    }
}
