package io.chengguo.ohttp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:07
 */
public class PostRequestBuilder extends BaseRequestBuilder<PostRequestBuilder> {
    protected Map<String, String> params;
    protected Map<String, File> files;
    protected IContentType contentTypeHandler = new FormDataContentType();

    /**
     * 像Body中添加参数
     *
     * @return
     */
    public PostRequestBuilder addParam(String key, String value) {
        prepareParams();
        params.put(key, value);
        return this;
    }

    /**
     * 像Body中添加参数
     *
     * @return
     */
    public PostRequestBuilder addParam(Map<String, String> params) {
        prepareParams();
        params.putAll(params);
        return this;
    }

    public PostRequestBuilder addFile(String key, File file) {
        prepareFiles();
        files.put(key, file);
        return this;
    }

    public PostRequestBuilder xWwwFormUrlencoded() {
        contentTypeHandler = new XWwwFormUrlencodedContentType();
        return this;
    }

    public PostRequestBuilder formData() {
        contentTypeHandler = new FormDataContentType();
        return this;
    }

    protected synchronized void prepareParams() {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
    }

    protected synchronized void prepareFiles() {
        if (files == null) {
            files = new LinkedHashMap<>();
        }
    }

    @Override
    public IRequest build() {
        return new PostRequest(this);
    }
}