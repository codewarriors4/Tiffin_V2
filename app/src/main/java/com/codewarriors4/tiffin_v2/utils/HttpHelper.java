package com.codewarriors4.tiffin_v2.utils;

import android.util.Log;

import com.codewarriors4.tiffin_v2.HttpResponseExceptionCus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    public static Object downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder()
                .url(address)
                .addHeader("Accept", "application/json");

        if(requestPackage.getMethod().equals("POST")){

            if(!requestPackage.getFiles().isEmpty()){
                MultipartBody.Builder mulBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                Map<String, String> requestPackageParams = requestPackage.getParams();
                Map<String, File> requestPackageFiles = requestPackage.getFiles();
                for (String key: requestPackageParams.keySet()) {
                    mulBuilder.addFormDataPart(key, requestPackageParams.get(key));
                }

                for (String key : requestPackageFiles.keySet()) {
                    mulBuilder.addFormDataPart("file",
                            requestPackageFiles.get(key).getName(),
                            RequestBody.create(MEDIA_TYPE_JPEG, requestPackageFiles.get(key)));
                }

                MultipartBody build = mulBuilder.build();
                requestBuilder.post(build);
                for (String key : requestPackage.getHeaders().keySet()) {
                    if(key.equals("Authorization")){
                        requestBuilder.header(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }else{
                        requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }
                }

            }else{
                Map<String, String> params = requestPackage.getParams();

                FormBody.Builder formData = new FormBody.Builder();
                for (String key: params.keySet()) {
                    formData.add(key, params.get(key));
                }

//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("somParam", "someValue")
//                    .build();

                RequestBody formBody = formData.build();
                requestBuilder.post(formBody);
                for (String key : requestPackage.getHeaders().keySet()) {
                    if(key.equals("Authorization")){
                        requestBuilder.header(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }else{
                        requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }
                }
            }
//            MultipartBody.Builder builder = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM);
//            Map<String, String> params = requestPackage.getParams();
//            for (String key: params.keySet()) {
//                builder.addFormDataPart(key, params.get(key));
//            }
//
//            RequestBody requestBody = builder.build();
//            requestBuilder.method("POST", requestBody);


//                builder.addFormDataPart(key, params.get(key));
//            }
        }

        if(requestPackage.getMethod().equals("JSON")){
             MediaType mediaType
                    = MediaType.parse("application/json");
            String jsonData = requestPackage.getParams().get("jsonData");
            requestBuilder
                    .post(RequestBody.create(mediaType, jsonData));
        }

        Request request = requestBuilder.build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else if(new String(response.code() + "" ).charAt(0) == '4'){
            HttpResponseExceptionCus httpResponseExceptionCus = new HttpResponseExceptionCus(response.body().string(), response.code());
            if(httpResponseExceptionCus.getMessage() != null)
                throw httpResponseExceptionCus;
            else
                return "Internal Error";

        }else{
            return "Internal Error";
        }
    }

//    private static String getResponseBody(String str){
//        try {
//            return new JSONObject(str).getString("message");
//        } catch (JSONException e) {
//            return "JSON ERROR";
//        }
//    }


}