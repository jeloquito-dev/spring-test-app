package com.playground.jeq.springtestapp.Model.API;

import java.sql.Timestamp;

public class BaseResponse<T> {

    private String request_id;
    private int status_code;

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private T result;
    public BaseResponse(String request_id, int status_code, T result) {
        this.request_id = request_id;
        this.status_code = status_code;
        this.result = result;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
