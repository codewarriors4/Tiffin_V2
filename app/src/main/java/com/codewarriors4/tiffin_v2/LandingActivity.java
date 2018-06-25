package com.codewarriors4.tiffin_v2;


import android.content.Context;

import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.codewarriors4.tiffin_v2.utils.HttpHelper;
import com.codewarriors4.tiffin_v2.utils.RequestPackage;

import java.io.IOException;

import butterknife.BindView;

public class LandingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    public static final String BASE_URL = "http://52.14.64.177/api";
    public static final String REGESTRATION = "/register";

    @BindView(R.id.statusTextView)
    public TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initApp();
    }

    private void initApp()
    {
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }



    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return new LoginService(this);
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.d("Loader", "onLoadFinished: " + ((HttpResponseExceptionCus)data));
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }



    public static class LoginService extends AsyncTaskLoader<Object> {
        public LoginService(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(BASE_URL + REGESTRATION);
            requestPackage.setParam("email", "");
            requestPackage.setParam("password", "");
            requestPackage.setMethod("POST");
            try {
                Object s = HttpHelper.downloadFromFeed(requestPackage);
                return s;
            } catch (IOException e) {
                HttpResponseExceptionCus error = (HttpResponseExceptionCus)e;
                return error;
            }


        }
    }
}
