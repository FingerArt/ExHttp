package io.chengguo.exhttp;

import android.content.Context;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月17日 18:03
 */
public final class ExHttp {
    static Context gContext;

    private ExHttp() {
    }

    public static void initialize(Context context) {
        gContext = context.getApplicationContext();
    }

    public static Context getContext() {
        testInitialize();
        return gContext;
    }

    private static void testInitialize() {
        if (gContext == null)
            throw new ExceptionInInitializerError("Please invoke ExHttp.initialize(Context)");
    }

    public static GetRequestBuilder get() {
        return new GetRequestBuilder();
    }

    public static PostRequestBuilder post() {
        return new PostRequestBuilder();
    }

    public static PutRequestBuilder put() {
        return new PutRequestBuilder();
    }

    public static DeleteRequestBuilder delete() {
        return new DeleteRequestBuilder();
    }
}
