package io.chengguo.exhttp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 18:34
 */
@RunWith(MockitoJUnitRunner.class)
public class ExHttpTest {
    @Test
    public void get() throws Exception {
        Response response = ExHttp.get().url("http://httpbin.org/get").addQuery("k1", "v1").addHeader("hello", "world").build().execute();
        System.out.println(response);
    }

    @Test
    public void post() throws Exception {
        Response response = ExHttp.post().url("http://httpbin.org/post").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        System.out.println(response);
    }

    @Test
    public void put() throws Exception {
        Response response = ExHttp.put().url("http://httpbin.org/put").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        System.out.println(response);
    }

    @Test
    public void delete() throws Exception {
        Response response = ExHttp.delete().url("http://httpbin.org/delete").addQuery("q1", "qv1").addParam("p1", "pv1").build().execute();
        System.out.println(response);
    }

    @Test
    public void delete_callback() throws Exception {
        IHttpRequestCallback callback = new HttpSampleRequestCallback() {
            @Override
            public void onStart() {
                System.out.println("ExHttpTest.onStart");
            }

            @Override
            public void onSuccess(Response response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("ExHttpTest.onError: " + e.toString());
            }

            @Override
            public void onFinish() {
                System.out.println("ExHttpTest.onFinish");
            }
        };
        ExHttp.get().url("http://httpbin.org/kkkkk").addQuery("q1", "qv1").build().execute(callback);
        Thread.sleep(10000);
    }

    @Test
    public void cookieTest() throws Exception {
        IHttpRequestCallback callback = new HttpSampleRequestCallback() {
            @Override
            public void onStart() {
                System.out.println("ExHttpTest.onStart");
            }

            @Override
            public void onSuccess(Response response) {
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("ExHttpTest.onError: " + e.toString());
            }

            @Override
            public void onFinish() {
                System.out.println("ExHttpTest.onFinish");
            }
        };
        ExHttp.post()
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
        Response response = ExHttp.get().url("http://httpbin.org/delay/10").build().execute();
        System.out.println(response);
    }
}