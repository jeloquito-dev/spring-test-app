package com.playground.jeq.springtestapp.Model;


public class BaseResponse<T> {

    private String requestId;
    private T request;

    public BaseResponse(String requestId, T request) {
        this.requestId = requestId;
        this.request = request;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

}
