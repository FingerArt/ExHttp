package io.chengguo.ohttp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import java.util.zip.CheckedOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 18:34
 */
@RunWith(MockitoJUnitRunner.class)
public class GHttpTest {
    @Test
    public void get() throws Exception {
        HttpURLConnection connection = OHttp.get().url("http://httpbin.org/get").addQuery("k1", "v1").addHeader("hello", "world").build().execute();
        print(connection.getInputStream());
    }

    @Test
    public void post() throws Exception {
        HttpURLConnection connection = OHttp.post().url("http://httpbin.org/post").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        print(connection.getInputStream());
    }

    @Test
    public void put() throws Exception {
        HttpURLConnection connection = OHttp.put().url("http://httpbin.org/put").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        print(connection.getInputStream());
    }

    @Test
    public void delete() throws Exception {
        HttpURLConnection connection = OHttp.delete().url("http://httpbin.org/delete").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        print(connection.getInputStream());
    }

    @Test
    public void delete_callback() throws Exception {
        IHttpRequestCallback callback = new GHttpSampleRequestCallback() {
            @Override
            public void onStart() {
                System.out.println("GHttpTest.onStart");
            }

            @Override
            public void onSuccess(int responseCode, InputStream inputStream, HttpURLConnection connection) {
                System.out.println("responseCode = [" + responseCode + "], inputStream = [" + inputStream + "], connection = [" + connection + "]");
                print(inputStream);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("GHttpTest.onError: " + e.toString());
            }

            @Override
            public void onFinish() {
                System.out.println("GHttpTest.onFinish");
            }
        };
        OHttp.get().url("http://httpbin.org/kkkkk").addQuery("q1", "qv1").build().execute(callback);
        Thread.sleep(10000);
    }

    @Test
    public void cookieTest() throws Exception {
        IHttpRequestCallback callback = new GHttpSampleRequestCallback() {
            @Override
            public void onStart() {
                System.out.println("GHttpTest.onStart");
            }

            @Override
            public void onSuccess(int responseCode, InputStream inputStream, HttpURLConnection connection) {
                System.out.println("responseCode = [" + responseCode + "], inputStream = [" + inputStream + "], connection = [" + connection + "]");
                print(inputStream);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("GHttpTest.onError: " + e.toString());
            }

            @Override
            public void onFinish() {
                System.out.println("GHttpTest.onFinish");
            }
        };
        OHttp.post()
                .url("https://test.dh-data.com:8001/webapi/evidence/obtainId")
                .addCookie("Cookie", "session.id=1700611f-2129-49ab-9366-bd2d34dac76d")
                .build()
                .execute(callback);
        Thread.sleep(10000);
    }

    @Test
    public void httpsTest() throws Exception {

    }

    @Test
    public void state() throws Exception {
        HttpURLConnection connection = OHttp.get().url("http://httpbin.org/status/255").build().execute();
        System.out.println(connection.getResponseCode());
        System.out.println(connection.getResponseMessage());
        print(connection.getInputStream());
    }

    @Test
    public void okState() throws Exception {
        OkHttpClient ok = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

        Request request = new Request.Builder().get().url("http://httpbin.org/status/502").build();
        Call call = ok.newCall(request);
        Response response = call.execute();
        System.out.println(response.code());
        System.out.println(response.body().string());
    }

    private void print(InputStream inputStream) {
        System.out.println(Utils.outputString(inputStream));
    }
}