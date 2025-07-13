package org.td024.auth.model;

import org.td024.auth.enums.TokenType;

public class TokenPayload {
    private String username;
    private TokenType tokenType;
    private int sessionNo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public int getSessionNo() {
        return sessionNo;
    }

    public void setSessionNo(int sessionNo) {
        this.sessionNo = sessionNo;
    }
}
