package Gameloop;

import API.Engine;
import API.EngineManager;
import Exceptions.HandAlreadyStartedException;
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

public class StartNewHandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // processRequest(request,response);
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        String game_id= ServletUtils.getSessionParam(request,"gameID");

        if(game_id==null)
        {
            ServletUtils.SendErrorMessage("Error getting game id from session",response);
        }
        else
        {
            Engine game=getManager().GetGame(game_id);
            if(game!=null) {
                try {
                    game.StartNewHand();
                    game.StartNewBidCycle();
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Hand Started");
                        out.flush();
                    }
                } catch (HandAlreadyStartedException e) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Hand already Started");
                        out.flush();
                    }
                } catch (NoSufficientMoneyException e) {
                    ServletUtils.SendErrorMessage("One of the players have no sufficient money.",response);
                }
            }
            else {
                ServletUtils.SendErrorMessage("Cannot find game",response);
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
