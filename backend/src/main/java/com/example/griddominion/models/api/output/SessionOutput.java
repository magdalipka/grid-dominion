package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.SessionModel;

public class SessionOutput {

  public String id;
  public String user_id;
  public String expires_at;

  public SessionOutput(SessionModel sessionModel) {
    this.id = sessionModel.getId();
    this.user_id = sessionModel.getUserId();
    this.expires_at = sessionModel.getExpiresAt().toString();
  }

}