package io.chengguo.ohttp;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月26日 16:43
 */
public class DeleteRequestBuilder extends PostRequestBuilder {
    @Override
    public IRequest build() {
        return new DeleteRequest(this);
    }
}
