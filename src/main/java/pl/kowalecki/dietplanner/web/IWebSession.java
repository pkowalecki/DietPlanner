package pl.kowalecki.dietplanner.web;

import jakarta.servlet.http.HttpSession;

public interface IWebSession {

    HttpSession getHttpSession();

    String getSessionId();

    void setSession(HttpSession session);
}
