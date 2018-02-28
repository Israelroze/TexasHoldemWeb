package GamesList;

import API.Engine;
import API.EngineManager;
import Containers.GameNodeData;
import Exceptions.GameIDNotProvidedException;
import Exceptions.UserNameNotProvidedException;
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
import java.util.LinkedList;
import java.util.List;

public class JoinGameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);
        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            Object objGameID=request.getParameter("GameId");
            if(objGameID!=null)
            {
                try {
                    getManager().AddPlayerToGame(username,(String)objGameID);
                    try (PrintWriter out = response.getWriter()) {
                        out.println("ok");
                        out.flush();
                    }
                } catch (UserNameNotProvidedException e) {
                    ServletUtils.SendErrorMessage("User not provided", response);
                } catch (GameIDNotProvidedException e) {
                    ServletUtils.SendErrorMessage("Gaem ID not provided", response);
                }
            }
        }
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
