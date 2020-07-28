package com.cms.authentication.model;

import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;
    private final String userName;

    public JwtTokenResponse(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public String getToken() {
        return this.token;
    }

    public String getUserName() { return this.userName; }
}

