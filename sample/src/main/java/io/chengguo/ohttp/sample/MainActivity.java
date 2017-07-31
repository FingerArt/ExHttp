package io.chengguo.ohttp.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import io.chengguo.ohttp.GHttpSampleRequestCallback;
import io.chengguo.ohttp.OHttp;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream ssl = getAssets().open("cer/ssl.p12");
            OHttp.post()
                    .url("https://10.168.1.242:8888/webapi/users/login")
                    .json("{\"username\":\"zhangming\",\"password\":\"123456\"}")
                    .build()
                    .execute(new GHttpSampleRequestCallback() {
                @Override
                public void onSuccess(int responseCode, InputStream inputStream, HttpURLConnection connection) {
                    Log.d(TAG, "onSuccess() called with: responseCode = [" + responseCode + "], inputStream = [" + inputStream + "], connection = [" + connection + "]");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish() called");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
