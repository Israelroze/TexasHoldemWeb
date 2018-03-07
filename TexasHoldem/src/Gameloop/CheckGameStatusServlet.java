package Gameloop;

import API.EngineManager;
import GameManager.GameManager;
import Utils.ServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckGameStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= ServletUtils.getSessionUser(request);

        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            String game_id=getManager().IsPlayerInReadyGame(username);
            //String game_id = ServletUtils.getSessionParam(request, "gameID");
            if(game_id!=null) {
                ServletUtils.setSessionParam(request, "gameID", game_id);

                getManager().GetGame(game_id).CheckAnyPlayerOutOfMoney();
                getManager().GetGame(game_id).CheckNoHumanRegistered();
                getManager().GetGame(game_id).CheckNumOfHands();

                if (getManager().GetGame(game_id).IsGameOver()) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Game Over");
                        out.flush();
                    }
                } else {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Game Running");
                        out.flush();
                    }
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
