package com.example.sessions;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/info")
public class InfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter w = resp.getWriter();
        HttpSession session = req.getSession();
        try {
            Method getSessionHandler = session.getClass().getDeclaredMethod("getSessionHandler");
            Object sessionHandler = getSessionHandler.invoke(session);
            Method getSessionCache = sessionHandler.getClass().getDeclaredMethod("getSessionCache");
            Object sessionCache = getSessionCache.invoke(sessionHandler);
            Field sessionsField = sessionCache.getClass().getDeclaredField("_sessions");
            sessionsField.setAccessible(true);
            Map<?, ?> sessions = (Map<?, ?>) sessionsField.get(sessionCache);
            if (!sessions.isEmpty()) {
                Field requestsField = sessions.values().iterator().next().getClass().getDeclaredField("_requests");
                requestsField.setAccessible(true);
                for (Map.Entry<?, ?> entry : sessions.entrySet()) {
                    w.println(entry.getKey() + " = " + requestsField.get(entry.getValue()));
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new ServletException(e);
        }
    }
}
