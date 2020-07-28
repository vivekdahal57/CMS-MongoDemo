package com.cms.util;

/**
 * Created by i82325 on 7/3/2020.
 */
public class ResponseMessage {
    String error;

    public ResponseMessage(String error) {
        this.error = error;
    }

    public ResponseMessage getError() {
        return this;
    }
}
