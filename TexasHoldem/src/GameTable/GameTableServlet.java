package GameTable;

import API.Engine;
import API.EngineManager;
import Card.Card;
import Containers.GameData;
import Containers.GameStatusData;
import Containers.HandData;
import Containers.UserData;
import Exceptions.FatalGameErrorException;
import Exceptions.GameOverException;
import Exceptions.NoSufficientMoneyException;
import GameManager.GameManager;
import UserManager.UserManager;
import Utils.ServletUtils;
import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class GameTableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //processRequest(request,response);
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

                try {
                    getManager().CheckGameStatus(game);
                    try (PrintWriter out = response.getWriter()) {
                        String res = BuildTableData(game,username);
                        out.println(res);
                        out.flush();
                    }
                } catch (GameOverException e) {
                    ServletUtils.SendErrorMessage("Game is over or not started yet.", response);
                } catch (FatalGameErrorException e) {
                    ServletUtils.SendErrorMessage("Fatal Error:"+e.getMessage(), response);
                }
            }
        }
    }

    private String BuildTableData(Engine game,String username) {
        Gson json=new Gson();

        List<UserData> users=new LinkedList<>();

        for(int i=0;i<game.GetTotalNumOfPlayers();i++)
        {
            String p_name=game.GetPlayerName(i);

            List<String> p_cards= new LinkedList<>();

            if(p_name.equals(username))//the player from the session
            {
                for(Card card:game.GetPlayersCards(i) )
                {
                    p_cards.add(card.toString());
                }
            }
            else
            {
                p_cards.add("back");
                p_cards.add("back");
            }

            String role;
            if (game.GetPlayerIsDealer(i)) role = "dealer";
            else if(game.GetPlayerIsBig(i)) role = "big";
            else if(game.GetPlayerIsSmall(i)) role = "small";
            else role ="no_role";

            users.add(new UserData(game.GetPlayerName(i),game.GetPlayerNumOfWins(i),game.GetPlayerPot(i),getManager().GetUserType(game.GetPlayerName(i)),p_cards ,role));
        }

        List<String> comm_cards=new LinkedList<>();
        for(Card card: game.GetCommunityCards())
        {
            comm_cards.add(card.toString());
        }

        HandData table_data=new HandData(game.GetPot(),comm_cards);


        //game status
        GameStatusData game_status=new GameStatusData(game.IsGameStarted(),game.IsGameOver(),game.IsCurrentHandStarted(),game.IsCurrentHandOver(),game.IsCurrentBidCycleFinished(),getManager().IsYourTurn(game,username));
        GameData game_data=new GameData(0,users,game.GetWinner(),table_data,game_status,game.GetNumberOfHands(),game.GetCurrentHandNumber(),game.GetTotalNumOfPlayers());

        return json.toJson(game_data);
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private EngineManager getManager() {
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


