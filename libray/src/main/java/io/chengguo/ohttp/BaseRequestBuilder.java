package io.chengguo.ohttp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:07
 */
public abstract class BaseRequestBuilder<T> {
    protected String url;
    protected Map<String, String> queries;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    /**
     * 设置Url
     *
     * @return
     */
    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    /**
     * 添加Query参数
     *
     * @return
     */
    public T addQuery(String key, String value) {
        prepareQueries();
        queries.put(key, value);
        return (T) this;
    }

    /**
     * 添加Query参数
     *
     * @return
     */
    public T addQuery(Map<String, String> querys) {
        prepareQueries();
        this.queries.putAll(querys);
        return (T) this;
    }

    /**
     * 添加请求头
     *
     * @param key
     * @param value
     * @return
     */
    public T addHeader(String key, String value) {
        prepareHeaders();
        headers.put(key, value);
        return (T) this;
    }

    /**
     * 构建请求
     *
     * @return
     */
    public abstract IRequest build();

    protected void prepareQueries() {
        if (queries == null) {
            queries = new LinkedHashMap<>();
        }
    }

    protected void prepareParams() {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
    }

    private void prepareHeaders() {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
    }
}
