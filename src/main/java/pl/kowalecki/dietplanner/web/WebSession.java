package pl.kowalecki.dietplanner.web;

import jakarta.servlet.http.HttpSession;

public class WebSession implements IWebSession{
    protected HttpSession httpSession;

    public WebSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public HttpSession getHttpSession() {
        return httpSession;
    }

    @Override
    public String getSessionId() {
        return httpSession.getId();
    }

    @Override
    public void setSession(HttpSession httpSession) {
        this.httpSession=httpSession;
    }
}
