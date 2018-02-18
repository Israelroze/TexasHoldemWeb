package UserManager;

import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=ServletUtils.getSessionUser(request);

        if(username==null)
        {
            response.sendRedirect("index.html");
        }
        else {
            Gson collection = new Gson();
            ServletContext context = getServletContext();
            Object objUserManager = context.getAttribute("UserManager");
            if (objUserManager != null) {
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {
                    UserManager usermng = (UserManager) objUserManager;
                    Map<String, String> userlist = usermng.getUsers();
                    String json = collection.toJson(userlist);

                    System.out.println(json); //DEBUG

                    out.println(json);
                    out.flush();
                }
            } else {
                response.sendError(response.SC_NO_CONTENT, "No User list exist.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
