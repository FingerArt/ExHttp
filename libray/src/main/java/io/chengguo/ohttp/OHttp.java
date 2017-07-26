package io.chengguo.ohttp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月17日 18:03
 */
public final class OHttp {
    private static final String TAG = OHttp.class.getSimpleName();
    private static HashMap<String, WeakReference<HttpURLConnection>> tags = new HashMap<>();
    static Context gContext;

    private OHttp() {
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
            throw new ExceptionInInitializerError("Please invoke OHttp.initialize(Context)");
    }

    public static void addTag(String tag, HttpURLConnection connection) {
        tags.put(tag, new WeakReference<HttpURLConnection>(connection));
        Log.d(TAG, "addTag = [" + tag + "]");
    }

    public static void cancel(@NonNull String tag) {
        WeakReference<HttpURLConnection> reference = tags.get(tag);
        if (reference != null && reference.get() != null) {
            HttpURLConnection connection = reference.get();
            connection.disconnect();
        }
        tags.remove(tag);
        Log.d(TAG, "cancelTag = [" + tag + "]");
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
