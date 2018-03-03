package Gameloop;

import API.Engine;
import API.EngineManager;
import Containers.BetOptionData;
import Exceptions.*;
import GameManager.GameManager;
import Move.Move;
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

public class SetMoveServlet extends HttpServlet {
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
            } else {
                Engine game = getManager().GetGame(game_id);
                if (game != null) {
                    try {
                        getManager().SetNewMove(game,username,request.getParameter("move"),request.getParameter("value"));
                        try (PrintWriter out = response.getWriter()) {
                            out.println("New Move Approved");
                            out.flush();
                        }
                    } catch (StakeNotInRangeException e) {
                        ServletUtils.SendErrorMessage("Stake is not in range", response);
                    } catch (NoSufficientMoneyException e) {
                        ServletUtils.SendErrorMessage("Player not sufficient money", response);
                    } catch (PlayerAlreadyBetException e) {
                        ServletUtils.SendErrorMessage("Player already placed bet", response);
                    } catch (ChipLessThanPotException e) {
                        ServletUtils.SendErrorMessage("Player chips less than pot", response);
                    } catch (MoveNotAllowdedException e) {
                        ServletUtils.SendErrorMessage("Not valid move", response);
                    } catch (PlayerFoldedException e) {
                        ServletUtils.SendErrorMessage("Player already folded", response);
                    }
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
