package com.example.sessions;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String delay = req.getParameter("delay");
        req.getSession().setAttribute("test", delay);
        Instant start = Instant.now();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(Long.parseLong(delay)));
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().println("<!doctype html><p>Delayed: " + Duration.between(start, Instant.now()) + ".</p>");
    }
}
