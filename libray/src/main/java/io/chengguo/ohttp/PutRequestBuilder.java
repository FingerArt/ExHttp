package io.chengguo.ohttp;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月26日 16:42
 */
public class PutRequestBuilder extends PostRequestBuilder {
    @Override
    public IRequest build() {
        return new PutRequest(this);
    }
}
