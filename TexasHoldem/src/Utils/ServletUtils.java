package Utils;

import API.EngineManager;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtils {

    //SESSION
    public static void setSessionUser(HttpServletRequest request,String username)
    {
        HttpSession session=request.getSession(true);
        //String username=request.getParameter("username");
        session.setAttribute("username",username);
    }
    public static String getSessionUser(HttpServletRequest request)
    {
        HttpSession session=request.getSession(false);
        if(session!=null) {
            Object objUsername = session.getAttribute("username");
            if (objUsername != null) {
                return (String) objUsername;
            }
        }
        return null;
    }

    public static void SendErrorMessage(String error_message, HttpServletResponse response) throws IOException {
        //response.setContentType("text/plain");
        //response.sendError(400, error_message);
        response.setStatus(400);
        PrintWriter out = response.getWriter();
        out.print(error_message);
        out.flush();
    }

    public static void SendRedirectURL(String URL, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(URL);
        out.flush();
    }

}
