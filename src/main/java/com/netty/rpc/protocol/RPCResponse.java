package com.netty.rpc.protocol;

public class RPCResponse {
    private String responseID;
    private String error;
    private Object result;

    @Override
    public String toString() {
        return "RPCResponse{" +
                "responseID='" + responseID + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }

    public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
