# ExHttp

A lightweight URLConnection HTTP wrapper library for android.

## Sample

``` java
//Sync
ExHttp.get()
      .url("http://httpbin.org/get")
      .addQuery("k1", "v1")
      .addHeader("hello", "world")
      .build()
      .execute();

//Async
IHttpRequestCallback callback = 
        new IHttpRequestCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Response response) {
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onFinish() {
            }
        };
ExHttp.get()
      .url("http://httpbin.org/kkkkk")
      .addQuery("q1", "qv1")
      .build()
      .execute(callback);
```