package com.example.sessions;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=utf-8");
        try {
            checkSessions(resp.getWriter(), req.getSession());
        } catch (ReflectiveOperationException e) {
            throw new ServletException(e);
        }
    }

    private void checkSessions(PrintWriter w, HttpSession session) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
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
                Long requests = (Long) requestsField.get(entry.getValue());
                if (requests > 0) {
                    w.println(entry.getKey() + " = " + requests);
                }
            }
        }
        w.println("---");
    }
}
