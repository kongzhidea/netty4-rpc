package com.netty4.rpc.core.protocol;

import java.io.Serializable;

/**
 * RPC Response
 */
public class RpcResponse extends RpcBaseModel {

    private static final long serialVersionUID = -6427527958525490529L;

    private String requestId;
    private Exception exception;
    private Object result;

    public boolean hasException() {
        return exception != null;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
