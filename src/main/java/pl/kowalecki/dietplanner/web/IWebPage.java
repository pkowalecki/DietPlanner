package pl.kowalecki.dietplanner.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IWebPage {

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    IWebSession getWSession();

    boolean redir();

}
