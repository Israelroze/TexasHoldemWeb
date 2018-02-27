package UserManager;

import API.Engine;
import API.EngineManager;
import Containers.GameNodeData;
import GameManager.GameManager;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=ServletUtils.getSessionUser(request);
        if(username==null)
        {
            ServletUtils.SendErrorMessage("User dont registered", response);
        }
        else {
            Gson gson = new Gson();

            Map<String,String> userlist = new HashMap<>();

            for(String r_username: getManager().GetUserList())
            {
                userlist.put(r_username,getManager().GetUserType(r_username));
            }

            if (!userlist.isEmpty()) {
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {

                    String json = gson.toJson(userlist);

                    System.out.println(json); //DEBUG

                    out.println(json);
                    out.flush();
                }
            } else {
                ServletUtils.SendErrorMessage("User list dont exist!", response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private EngineManager getManager()
    {
        ServletContext context=getServletContext();
        Object objManager=context.getAttribute("EngineManager");

        if(objManager!= null)
        {
            return (EngineManager) context.getAttribute("EngineManager");
        }
        else
        {
            EngineManager manager=new GameManager();
            context.setAttribute("EngineManager",manager);
            return (EngineManager) context.getAttribute("EngineManager");
        }
    }
}
