package GamesList;

import API.Engine;
import API.EngineManager;
import Containers.GameNodeData;
import Exceptions.GameIDNotProvidedException;
import Exceptions.PlayerAlreadyInGameException;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                    getManager().AddPlayerToGame((String)objGameID,username);
                    if(getManager().GetGame((String)objGameID).IfEnoughPlayers())
                    {
                        ServletUtils.SendRedirectURL("./pages/table/table.html",response);
                    }
                    else
                    {
                        try (PrintWriter out = response.getWriter()) {
                            out.println("false");
                            out.flush();
                        }
                    }
                } catch (UserNameNotProvidedException e) {
                    ServletUtils.SendErrorMessage("User not provided.", response);
                } catch (GameIDNotProvidedException e) {
                    ServletUtils.SendErrorMessage("Game ID not provided.", response);
                } catch (PlayerAlreadyInGameException e) {
                    ServletUtils.SendErrorMessage("You Already joined the game.", response);
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
