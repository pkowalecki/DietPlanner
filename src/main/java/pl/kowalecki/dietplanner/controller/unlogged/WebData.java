package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class WebData {

    public void checkSession(HttpServletResponse response, HttpSession session) {
        System.out.println("ale wszystkie requesty lecą przez webData");

    }
}
