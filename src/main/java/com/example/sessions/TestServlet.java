package com.example.sessions;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/test", asyncSupported = true)
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getAttribute("marker") == null) {
            req.setAttribute("marker", Boolean.TRUE);
            req.startAsync().dispatch();
        } else {
            resp.sendError(404);
        }
    }
}
