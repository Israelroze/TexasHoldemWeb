package Login;

import UserManager.UserManager;
import Utils.ServletUtils;
import com.sun.deploy.net.HttpResponse;

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
        String username = request.getParameter("username");
        String type = request.getParameter("iscomputer");

        if(usernameFromSession == null) {
            if (username != null && !(username.equals(""))) {
                //check if user name already exist
                if(CheckAndUpdateUser(username,type))
                {
                    //user already exist
                    ServletUtils.SendErrorMessage("User Already Exist.",response);
                }
                else
                {
                    //ok, add user and login
                    ServletUtils.setSessionUser(request, username);
                    ServletUtils.SendRedirectURL("./pages/lobby/lobby.html",response);
                }
            }
            else
            {
                // user not provided
                ServletUtils.SendErrorMessage("Username not provided",response);
            }
        }
        else
        {
            //session already created, check if username already login
            ServletUtils.SendRedirectURL("./pages/lobby/lobby.html",response);
        }
    }


    private boolean CheckAndUpdateUser(String username,String type) {
        ServletContext context = getServletContext();
        Object objUserManagment = context.getAttribute("UserManager");

        if (objUserManagment != null) {
            UserManager userlist = (UserManager) objUserManagment;

            if (userlist.isUserExists(username)) {return true; }
            else
            {
                if (type != null)
                {
                    userlist.addUser(username, "COMPUTER");
                }
                else
                {
                    userlist.addUser(username, "HUMAN");
                }
            }
        }
        else
        {
            //first time, init user list
            UserManager userlist = new UserManager();
            if (type != null) {
                userlist.addUser(username, "COMPUTER");
            } else {
                userlist.addUser(username, "HUMAN");
            }

            context.setAttribute("UserManager", userlist);
        }
        return false;
    }

}

