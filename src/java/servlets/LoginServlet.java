/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utilities.User;
import utilities.UserService;

/**
 *
 * @author 677571
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("logout")) {
            String returnMessage = "Logged Out!";
            HttpSession session = request.getSession();

            session.removeAttribute("user");
            session.invalidate();

            request.setAttribute("returnMessage", returnMessage);
            response.sendRedirect("login");
        } else {
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String returnMessage = "";
        String[] checked = request.getParameterValues("remember");

        if (username == null || password == null || username == "" || password == "") {
            returnMessage = "Both fields must be entered";
            request.setAttribute("returnMessage", returnMessage);
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else {
            UserService userService = new UserService();
            boolean authenticated = userService.validate(username, password);
            if (authenticated) {
                User user = new User(username, null);
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if (checked != null && checked[0].equalsIgnoreCase("remember")) {
                    Cookie cookie = new Cookie("username", username);
                    cookie.setMaxAge(60 * 60 * 24 * 365);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                else{
                    Cookie[] cookies = request.getCookies();
                    for(Cookie cookie : cookies){
                        if(cookie.getName().equalsIgnoreCase(username))
                        {
                            cookie.setMaxAge(0);
                        }
                    }
                }
                request.setAttribute("username", username);
                getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
            } else {
                returnMessage = "Invalid Credentials";
                request.setAttribute("returnMessage", returnMessage);
                request.setAttribute("username", username);
                request.setAttribute("password", password);
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
        }

    }

}
