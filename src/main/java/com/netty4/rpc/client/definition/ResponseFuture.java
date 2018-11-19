package com.netty4.rpc.client.definition;

import com.netty4.rpc.core.protocol.RpcResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhihui.kzh
 * @create 15/11/201811:33
 */
public class ResponseFuture {

    private String requestId;
    private long startTime;

    private RpcResponse response;

    private CountDownLatch latch = new CountDownLatch(1);


    public ResponseFuture(String requestId) {
        this.requestId = requestId;
        this.startTime = System.currentTimeMillis();
    }

    public void done(RpcResponse response) {
        this.response = response;
        latch.countDown();
    }

    public boolean isDone() {
        return latch.getCount() == 0;
    }


    public RpcResponse get(long timeout) throws InterruptedException, TimeoutException {
        boolean success = latch.await(timeout, TimeUnit.MILLISECONDS);
        if (!success) {
            throw new TimeoutException("rpc request timeout:" + requestId);
        }
        return response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public RpcResponse getResponse() {
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
    }
}
