package Gameloop;

import API.Engine;
import API.EngineManager;
import Exceptions.NoSufficientMoneyException;
import GameManager.GameManager;
import Utils.ServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class EndGameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);

        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            String game_id = ServletUtils.getSessionParam(request, "gameID");

            if (game_id == null) {
                ServletUtils.SendErrorMessage("Error getting game id from session", response);
            }
            else {
                Engine game = getManager().GetGame(game_id);
                game.SetGameOver(true);
                ServletUtils.SendRedirectURL("/pages/lobby/lobby.html",response);
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
