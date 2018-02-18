package Login;

import UserManager.UserManager;
import Utils.ServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String usernameFromSession=ServletUtils.getSessionUser(request);
        if(usernameFromSession == null) {
            String username = request.getParameter("username");
            String type = request.getParameter("iscomputer");

            if (username != null) {
                //check if user name already exist
                ServletContext context = getServletContext();
                Object objUserManagment = context.getAttribute("UserManager");

                if (objUserManagment != null) {
                    UserManager userlist = (UserManager) objUserManagment;

                    if (userlist.isUserExists(username)) {
                        response.sendError(400, "User already exist.");
                        //response.setStatus(400);
                        //out.print("User already exist.");
                        //out.flush();
                    }
                    else {
                        if (type != null) {
                            userlist.addUser(username, "COMPUTER");
                        } else {
                            userlist.addUser(username, "HUMAN");
                        }

                        context.setAttribute("UserManager", userlist);
                        ServletUtils.setSessionUser(request, username);
                        response.sendRedirect("./pages/lobby/lobby.html");
                    }
                } else {
                    //first time, init user list
                    UserManager userlist = new UserManager();
                    if (type != null) {
                        userlist.addUser(username, "COMPUTER");
                    } else {
                        userlist.addUser(username, "HUMAN");
                    }

                    context.setAttribute("UserManager", userlist);
                    ServletUtils.setSessionUser(request, username);
                    response.sendRedirect("./pages/lobby/lobby.html");
                }

            } else {
                //no username provide, send error
                response.sendError(400, "Username not provided.");
                //response.setStatus(400);
                //out.print("Username not provided.");
                //out.flush();
            }
        }
        else
        {
            String username = request.getParameter("username");
            ServletContext context = getServletContext();
            Object objUserManagment = context.getAttribute("UserManager");

            if (objUserManagment != null) {
                UserManager userlist = (UserManager) objUserManagment;
                String type = request.getParameter("iscomputer");
                if (userlist.isUserExists(username)) {
                    response.
                    response.sendError(400, "User already exist.");
                    //response.setStatus(400);
                    //out.print("User already exist.");
                    //out.flush();
                } else {
                    if (type != null) {
                        userlist.addUser(username, "COMPUTER");
                    } else {
                        userlist.addUser(username, "HUMAN");
                    }

                    context.setAttribute("UserManager", userlist);
                    ServletUtils.setSessionUser(request, username);
                    //user already logged in
                    response.sendRedirect("./pages/lobby/lobby.html");
                }
            }
        }
    }
}
