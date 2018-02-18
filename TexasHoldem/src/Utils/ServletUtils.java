package Utils;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

}
