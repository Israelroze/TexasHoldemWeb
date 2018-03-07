package Gameloop;

import API.Engine;
import API.EngineManager;
import Containers.BetOptionData;
import Exceptions.*;
import GameManager.GameManager;
import Move.MoveType;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LeaveGameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username= ServletUtils.getSessionUser(request);
        if(username==null)
        {
            ServletUtils.SendErrorMessage("User don't registered", response);
        }
        else {
            response.setContentType("application/json");
            String game_id = ServletUtils.getSessionParam(request, "gameID");

            if (game_id == null) {
                ServletUtils.SendErrorMessage("Error getting game id from session", response);
            }
            else {
                Engine game = getManager().GetGame(game_id);

                if (game != null) {
                    getManager().LeavePlayerGame(game_id,username);
                    ServletUtils.SendRedirectURL("../lobby/lobby.html",response);
                }
                else {
                    ServletUtils.SendErrorMessage("Cannot find game", response);
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
