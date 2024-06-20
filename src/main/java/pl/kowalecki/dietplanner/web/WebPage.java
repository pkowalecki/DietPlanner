package pl.kowalecki.dietplanner.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class WebPage implements IWebPage{

    private HttpServletRequest request;
    private HttpServletResponse response;
    private IWebSession webSession;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public IWebSession getWSession() {
        return webSession;
    }

    @Override
    public boolean redir() {
        return false;
    }
}
