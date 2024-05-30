package com.example.griddominion.models.api.input;

public class UserClanInput {
    private String clanId;

    // Constructors, getters, and setters
    public UserClanInput() {}

    public UserClanInput(String clanId) {
        this.clanId = clanId;
    }

    public String getClanId() {
        return clanId;
    }

    public void setClanId(String clanId) {
        this.clanId = clanId;
    }
}
