package Gameloop;

import API.Engine;
import API.EngineManager;
import Containers.BetOptionData;
import Exceptions.ChipLessThanPotException;
import Exceptions.HandAlreadyStartedException;
import Exceptions.PlayerFoldedException;
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
import java.util.List;

public class AllowdedMovesServlet extends HttpServlet {
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
                    boolean fold=false;
                    boolean check=false;
                    boolean call=false;
                    boolean bet=false;
                    boolean raise=false;

                    for(MoveType move:game.GetAllowdedMoves()) {
                        switch(move) {
                            case CHECK:
                                check=true;
                                break;
                            case RAISE:
                                raise=true;
                                break;
                            case CALL:
                                call=true;
                                break;
                            case BET:
                                bet=true;
                                break;
                            case FOLD:
                                fold=true;
                                break;
                        }
                    }

                    int[] range=game.GetAllowdedStakeRange();
                    BetOptionData options=new BetOptionData(bet,raise,call,fold,check,range[0],range[1]);


                    Gson json=new Gson();
                    try (PrintWriter out = response.getWriter()) {
                        out.println(json.toJson(options));
                        out.flush();
                    }

                } catch (PlayerFoldedException e) {
                    ServletUtils.SendErrorMessage("Current player folded",response);
                } catch (ChipLessThanPotException e) {
                    ServletUtils.SendErrorMessage("Current player chips less than pot",response);
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
