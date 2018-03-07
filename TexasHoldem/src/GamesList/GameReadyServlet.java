package GamesList;

import API.EngineManager;
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
import java.util.Map;

public class GameReadyServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);

        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            String game_id=getManager().IsPlayerInReadyGame(username);
            if(game_id!=null){
                ServletUtils.setSessionParam(request,"gameID",game_id);

                if(getManager().GetGame(game_id).IsPlayersReady()){
                    try (PrintWriter out = response.getWriter()) {
                        out.println("true");
                        out.flush();
                    }
                }
                else
                {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("false");
                        out.flush();
                    }
                }
                //if(!getManager().GetGame(game_id).IsGameStarted())
                //{
                  //  getManager().GetGame(game_id).StartGame();
                //}

               // ServletUtils.SendRedirectURL("/pages/table/table.html",response);
            }
            else{
                try (PrintWriter out = response.getWriter()) {
                    out.println("Game not ready yet.");
                    out.flush();
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


